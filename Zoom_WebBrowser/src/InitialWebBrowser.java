import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
public class InitialWebBrowser {
    protected static void setPage(JEditorPane jEditorPane,String url){
        try {
            jEditorPane.setPage(url);
        }catch (IOException e){
            System.err.println(e);
            System.exit(-1);
        }
    }
    class backButtonListener implements ActionListener{
protected JEditorPane jEditorPane;
protected JLabel label;
protected JButton backButton;
protected  Vector history;
        public    backButtonListener(JEditorPane jEditorPane, JLabel label, JButton backButton, Vector history) {
            this.jEditorPane = jEditorPane;
            this.label = label;
            this.backButton =backButton;
            this.history = history;

        }
        @Override
        public void actionPerformed(ActionEvent evt) {
try {
    String curl = (String) history.lastElement();
    history.removeElement(curl);
    curl = (String) history.lastElement();
    System.out.println("Back to0" +curl);
    setPage(jEditorPane,curl);
    label.setText("<html><b>URL:</b>"+curl);
    if (history.size() == 1)
        backButton.setEnabled(false);
}catch (Exception ex){
    System.out.println("Exeption" + ex);
}
        }
    }
    class LinkFllower implements HyperlinkListener {
        protected JEditorPane jEditorPane;
        protected JLabel label;
        protected JButton backButton;
        protected  Vector history;
        public    LinkFllower(JEditorPane jEditorPane, JLabel label, JButton backButton, Vector history) {
            this.jEditorPane = jEditorPane;
            this.label = label;
            this.backButton =backButton;
            this.history = history;

        }
        @Override
        public void hyperlinkUpdate(HyperlinkEvent evt) {
if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED){
    try {
        String currentURL = evt.getURL().toString();
        history.add(currentURL);
        backButton.setEnabled(true);
        System.out.println("Going to "+ currentURL);
        setPage(jEditorPane,currentURL);
        label.setText("<html><b>URL:</b>"+currentURL);
    }catch (Exception e){
        System.out.println("Error: Trouble fetching  url");
    }
}
        }
    }
    public InitialWebBrowser(String initialPage){
        Vector history = new Vector();
        history.add(initialPage);
        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);
        setPage(jEditorPane,initialPage);

        JScrollPane scrollPane = new JScrollPane();
        JFrame jFrame = new JFrame("Initial WebPage");
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               System.exit(0);
            }
        });
        JLabel label = new JLabel("<html><b>URL:</b>"+initialPage);
        JButton backbutton = new JButton("Back");
        backbutton.setActionCommand("back");
        backbutton.setToolTipText("Go to previous Page");
        backbutton.setEnabled(false);
        backbutton.addActionListener(new backButtonListener(jEditorPane,label,backbutton,history));
        JButton exitButton = new JButton("Exit");
        exitButton.setActionCommand("exit");
        exitButton.setToolTipText("quit this appliquation");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        JToolBar toolBar = new JToolBar();
        toolBar.add(backbutton);
        toolBar.add(exitButton);
        jEditorPane.addHyperlinkListener(new LinkFllower(jEditorPane,label,backbutton,history));
        JPanel contentpane = (JPanel) jFrame.getContentPane();
        contentpane.setLayout(new BorderLayout());
        contentpane.setPreferredSize(new Dimension(400,100));
        contentpane.add(toolBar,BorderLayout.NORTH);
        contentpane.add(scrollPane,BorderLayout.CENTER);
        contentpane.add(label,BorderLayout.SOUTH);
        jFrame.pack();
        jFrame.setSize(640,360);
        jFrame.setVisible(true);
    }

    public static void main(String[] args){
        String initialPage = new String("https://guercifzone-ar.blogspot.com/");
        if (args.length>0) initialPage = args[0];
        InitialWebBrowser initialWebBrowser = new InitialWebBrowser(initialPage);
    }
}
