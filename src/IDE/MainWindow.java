package IDE;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame implements ActionListener {
    private JTextPane pane;
    JTextArea lines;
    JLabel label;
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
}
