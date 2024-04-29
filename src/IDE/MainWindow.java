package IDE;

import Lexing.Exceptions.LexingException;
import Lexing.Lexer;
import Lexing.Token;
import Parsing.Parser;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainWindow extends JFrame implements ActionListener {
    private JTextPane pane;
    JTextArea lines;
    JLabel label;
    Lexer lexer = new Lexer();
    Parser parser = new Parser();

    public MainWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //add(scrollPane);
        pane = new JTextPane();
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();
        lines = new JTextArea("1");
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);
        label = new JLabel("Some text");
        pane.getDocument().addDocumentListener(new DocumentListener() {
            public String getText() {
                int caretPosition = pane.getDocument().getLength();
                Element root = pane.getDocument().getDefaultRootElement();
                String text = "1" + System.getProperty("line.separator");
                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    text += i + System.getProperty("line.separator");
                }
                return text;
            }
            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            @Override
            public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
            }

        });

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                lexer.clear();
                LinkedList<Token> tokens = lexer.read(pane.getText());
                Style defaultStyle = StyleContext.
                        getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
                pane.getStyledDocument().setCharacterAttributes(0, pane.getStyledDocument().getLength(), defaultStyle, true);

                String keywords = "\\belif|while|if|class|def|try|else|\\b";
                Pattern compiledPattern = Pattern.compile(keywords);
                Matcher matcher = null;
                try {
                    matcher = compiledPattern.matcher(pane.getDocument().getText(0, pane.getDocument().getLength()));
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                while (matcher.find()){
                    System.out.println("found");
                    AttributeSet attr = new SimpleAttributeSet();
                    StyledDocument style = pane.getStyledDocument();
                    AttributeSet oldSet = style.getCharacterElement(0).getAttributes();
                    StyleContext sc = StyleContext.getDefaultStyleContext();
                    AttributeSet s = sc.addAttribute(oldSet, StyleConstants.Foreground, Color.ORANGE);
                    style.setCharacterAttributes(matcher.start(), matcher.end() - matcher.start(), s, true);
                    pane.repaint();
                    System.out.println(pane.getText());
                    System.out.println(pane.getText().length());
                }

                if (lexer.isWithoutError()) {
                    parser.clear();
                    parser.parse(tokens);
                }
                else{
                    LinkedList<LexingException> exceptions = lexer.getExceptions();
                    StyledDocument style = pane.getStyledDocument();
                    Element root = pane.getDocument().getDefaultRootElement();
                    for (LexingException exception: exceptions){
                        int lineNum = exception.getLineNum();
                        int position = exception.getPosition();
                        int offset = root.getElement( lineNum - 1 ).getStartOffset() + position;
                        System.out.println(lineNum + " " + position + " " + offset);
                        AttributeSet oldSet = style.getCharacterElement(0).getAttributes();
                        StyleContext sc = StyleContext.getDefaultStyleContext();
                        AttributeSet s = sc.addAttribute(oldSet, StyleConstants.Background, Color.RED);
                        style.setCharacterAttributes(offset, 1, s, true);
                        pane.repaint();
                    }
                }
            }
        }, 1, 2, TimeUnit.SECONDS);


        //scrollPane.add(pane);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.getViewport().add(pane);
        scrollPane.setRowHeaderView(lines);
        //this.add(area);
        add(label, BorderLayout.SOUTH);
        this.pack();
        ((AbstractDocument)this.pane.getDocument()).setDocumentFilter(new DocumentFilter(){
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) {
                try {
                    if (length == 1 && offset >= 3) {
                        if (pane.getDocument().getText(offset - 3, 4).equals(" ".repeat(4))) {
                            fb.remove(offset - 3, 4);
                        } else
                            fb.remove(offset, length);

                    } else
                        fb.remove(offset, length);
                }
                catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }

        });

        pane.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                int caretPos = pane.getCaretPosition();
                int rowNum = (caretPos == 0) ? 1 : 0;
                for (int offset = caretPos; offset > 0;) {
                    try {
                        offset = Utilities.getRowStart(pane, offset) - 1;
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                    rowNum++;
                }
                int offset = 0;
                try {
                    offset = Utilities.getRowStart(pane, caretPos);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
                int colNum = caretPos - offset + 1;
                label.setText("Ln:"+rowNum + " Col:" + colNum);
            }
        });

        //StyledDocument doc = new DefaultStyledDocument();
        //doc.
        //pane = new JTextPane()
        pane.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_TAB)
                {
                    e.consume();
                    try {
                        pane.getDocument().insertString(pane.getCaretPosition(), " ".repeat(4), null);
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });


        this.setSize(new Dimension(500, 500));
        this.repaint();
        this.revalidate();

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public int getLineNum(int caretPos){
        int rowNum = (caretPos == 0) ? 1 : 0;
        for (int offset = caretPos; offset > 0;) {
            try {
                offset = Utilities.getRowStart(pane, offset) - 1;
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
            }
            rowNum++;
        }
        return rowNum;
    }

    public int getColNum(int caretPos){
        int offset = 0;
        try {
            offset = Utilities.getRowStart(pane, caretPos);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        int colNum = caretPos - offset + 1;
        return colNum;
    }
}
