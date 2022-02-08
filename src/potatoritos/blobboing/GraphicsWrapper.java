package potatoritos.blobboing;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class GraphicsWrapper {
    // A wrapper for g2d that adds scaling functionality

    private double renderScale;
    private Graphics2D g2d;

    public GraphicsWrapper(Graphics2D g2d, double renderScale) {
        this.g2d = g2d;
        this.renderScale = renderScale;
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setFont(new Font("Monospaced", Font.BOLD, 72));
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    // Description: Scales a value by renderScale
    // Parameters:
    //      val: the value to scale
    // Return: the scaled value
    private int scale(double val) {
        return (int)Math.round(val * renderScale);
    }

    // Description: Sets colour
    // Parameters:
    //      colour: the colour to set
    public void setColor(Color colour) {
        g2d.setColor(colour);
    }

    // Description: Sets transparency
    // Parameters:
    //      alpha: a value in the interval [0, 1] - the transparency
    public void setAlpha(float alpha) {
        g2d.setComposite(AlphaComposite.SrcOver.derive(alpha));
    }

    // Description: Draws a filled rectangle
    // Parameters:
    //      x1: the x coordinate of the top left corner
    //      y1: the y coordinate of the top left corner
    //      x2: the x coordinate of the bottom right corner
    //      y2: the y coordinate of the bottom right corner
    public void fillRect(double x1, double y1, double x2, double y2) {
        g2d.fillRect(scale(x1), scale(y1), scale(x2), scale(y2));
    }

    // Description: Draws a rectangle
    // Parameters:
    //      x1: the x coordinate of the top left corner
    //      y1: the y coordinate of the top left corner
    //      width: the width of the rectangle
    //      height: the height of the rectangle
    public void drawRect(double x, double y, double width, double height) {
        g2d.drawRect(scale(x), scale(y), scale(width), scale(height));
    }

    // Description: Draws an image
    // Parameters:
    //      image: the image to draw
    //      x: the x coordinate of the top left corner
    //      y: the y coordinate of the top left corner
    //      width: the width of the image
    //      height: the height of the image
    public void drawImage(Image image, double x, double y, double width, double height) {
        g2d.drawImage(image, scale(x), scale(y), scale(width), scale(height), null);
    }

    // Description: Draws an image
    // Parameters:
    //      image: the image to draw
    //      srcx1: the x coordinate of the top left corner of the
    //             window of the image to draw
    //      srcy1: the y coordinate of the top left corner of the
    //             window of the image to draw
    //      srcx2: the x coordinate of the bottom right corner of the
    //             window of the image to draw
    //      srcy2: the y coordinate of the bottom right corner of the
    //             window of the image to draw
    //      x: the x coordinate of the top left corner
    //      y: the y coordinate of the top left corner
    //      width: the width of the image
    //      height: the height of the image
    public void drawImage(Image image, int srcx1, int srcy1, int srcx2, int srcy2,
                          double x, double y, double width, double height) {
        g2d.drawImage(image, scale(x), scale(y), scale(x+width), scale(y+height),
                srcx1, srcy1, srcx2, srcy2, null);
    }

    // Description: Applies a rotation transformation
    // Parameters:
    //      radians: the amount of radians to rotate by
    //      x: the x coordinate of the center of rotation
    //      y: the y coordinate of the center of rotation
    public void rotate(double radians, double x, double y) {
        g2d.rotate(radians, scale(x), scale(y));
    }

    // Description: Gets the current transformation of the g2d
    // Return: g2d's current transformation
    public AffineTransform getTransform() {
        return g2d.getTransform();
    }

    // Description: Applies an AffineTransform
    // Parameters:
    //      trans: the transformation
    public void setTransform(AffineTransform trans) {
        g2d.setTransform(trans);
    }

    // Description: Draws text
    // Parameters:
    //      text: the text
    //      x: the x coordinate of the text
    //      y: the y coordinate of the text
    public void drawString(String text, double x, double y) {
        g2d.drawString(text, scale(x), scale(y));
    }
}
