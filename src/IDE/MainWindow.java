package IDE;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {
    private JTextPane pane;
    JTextArea lines;
    public MainWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add(scrollPane);
        pane = new JTextPane();
        JScrollPane scrollPane = new JScrollPane();
        lines = new JTextArea("1");
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);
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
        add(scrollPane);
        scrollPane.getViewport().add(pane);
        scrollPane.setRowHeaderView(lines);
        //this.add(area);
        this.pack();
        this.setSize(new Dimension(500, 500));
        this.repaint();
        this.revalidate();

    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
