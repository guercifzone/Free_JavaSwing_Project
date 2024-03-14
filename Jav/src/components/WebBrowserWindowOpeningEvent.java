/*
 * Christopher Deckers (chrriis@nextencia.net)
 * http://www.nextencia.net
 *
 * See the file "readme.txt" for information on usage and redistribution of
 * this file, and for a DISCLAIMER OF ALL WARRANTIES.
 */
package chrriis.dj.sweet.components;

import org.eclipse.swt.graphics.Point;

/**
 * This event is sent after the window was created, meaning after a WindowWillOpen event. The new web browser will have its appearance set (bars visibility, bounds of the containing window if any).
 * This new web browser may or may not have received navigation events.
 * @author Christopher Deckers
 */
public class WebBrowserWindowOpeningEvent extends WebBrowserEvent {

  private JWebBrowser newWebBrowser;
  private Point location;
  private Point size;

  public WebBrowserWindowOpeningEvent(JWebBrowser webBrowser, JWebBrowser newWebBrowser, Point location, Point size) {
    super(webBrowser);
    this.newWebBrowser = newWebBrowser;
    this.location = location;
    this.size = size;
  }

  public JWebBrowser getNewWebBrowser() {
    return newWebBrowser;
  }

  /**
   * @return the location, or null for default behavior.
   */
  public Point getLocation() {
    return location;
  }

  /**
   * @return the size, or null for default behavior.
   */
  public Point getSize() {
    return size;
  }

}
