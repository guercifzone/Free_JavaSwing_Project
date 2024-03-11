import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class zooming extends JEditorPane {

    JComboBox zoomCombo=new JComboBox(new String[] {"50%","75%", "100%","200%", "250%"});

    public static void main(String[] args) throws Exception {
        JFrame frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        zooming scaledTextPane = new zooming();
        scaledTextPane.setEditorKit(new ScaledEditorKit());
        scaledTextPane.getDocument().putProperty("i18n", Boolean.TRUE);
        scaledTextPane.getDocument().putProperty("ZOOM_FACTOR", new Double(2.5));
        JScrollPane scroll=new JScrollPane(scaledTextPane);
        frame.getContentPane().add(scroll);
        frame.getContentPane().add(scaledTextPane.zoomCombo, BorderLayout.NORTH);
        scaledTextPane.setPage(new URL("http://google.com"));
        frame.setSize(200,200);
        frame.show();
    }


    public zooming() {
        super();

        zoomCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = (String) zoomCombo.getSelectedItem();
                s = s.substring(0, s.length() - 1);
                double scale = new Double(s).doubleValue() / 100;
                zooming.this.getDocument().putProperty("ZOOM_FACTOR", new Double(scale));
                try {
                    zooming.this.getDocument().insertString(0, "", null);    //refresh
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        zoomCombo.setSelectedItem("250%");
    }
    public void repaint(int x, int y, int width, int height) {
        super.repaint(0,0,getWidth(),getHeight());
    }
}
class ScaledEditorKit extends StyledEditorKit {
    public ViewFactory getViewFactory() {
        return new StyledViewFactory();
    }
    class StyledViewFactory implements ViewFactory {

        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new LabelView(elem);
                } else if (kind.equals(AbstractDocument.
                        ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.
                        SectionElementName)) {
                    return new ScaledView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.
                        ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }

    }
}
class ScaledView extends BoxView{
    public ScaledView(Element elem, int axis) {
        super(elem,axis);
    }
    public double getZoomFactor() {
        Double scale=(Double)getDocument().getProperty("ZOOM_FACTOR");
        if (scale!=null) {
            return scale.doubleValue();
        }

        return 1;
    }
    public void paint(Graphics g, Shape allocation) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        double zoomFactor = getZoomFactor();
        AffineTransform old=g2d.getTransform();
        g2d.scale(zoomFactor, zoomFactor);
        super.paint(g2d, allocation);
        g2d.setTransform(old);
    }
    public float getMinimumSpan(int axis) {
        float f = super.getMinimumSpan(axis);
        f *= getZoomFactor();
        return f;
    }
    public float getMaximumSpan(int axis) {
        float f = super.getMaximumSpan(axis);
        f *= getZoomFactor();
        return f;
    }

    public float getPreferredSpan(int axis) {
        float f = super.getPreferredSpan(axis);
        f *= getZoomFactor();
        return f;
    }
    protected void layout(int width, int height) {
        super.layout(new Double(width / getZoomFactor()).intValue(), new Double(height * getZoomFactor()).intValue());
    }

    public Shape modelToView(int pos, Shape a, Position.Bias b)
            throws BadLocationException {
        double zoomFactor = getZoomFactor();
        Rectangle alloc;
        alloc = a.getBounds();
        Shape s = super.modelToView(pos, alloc, b);
        alloc = s.getBounds();
        alloc.x*=zoomFactor;
        alloc.y*=zoomFactor;
        alloc.width*=zoomFactor;
        alloc.height*=zoomFactor;

        return alloc;
    }
    public int viewToModel(float x, float y, Shape a,
                           Position.Bias[] bias) {
        double zoomFactor = getZoomFactor();
        Rectangle alloc = a.getBounds();
        x/=zoomFactor;
        y/=zoomFactor;
        alloc.x/=zoomFactor;
        alloc.y/=zoomFactor;
        alloc.width/=zoomFactor;
        alloc.height/=zoomFactor;

        return super.viewToModel(x, y, alloc, bias);
    }


}