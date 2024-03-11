import javax.swing.*;
import java.net.URL;

public class Quick_WebCode {
public static void main (String [] arg)throws Exception{
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JEditorPane editorPane = new JEditorPane();

    editorPane.setPage(new URL("http://www.java2s.com"));

    frame.add(new JScrollPane(editorPane));

    frame.setSize(300, 200);
    frame.setVisible(true);
}

}
