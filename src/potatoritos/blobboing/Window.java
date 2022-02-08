package potatoritos.blobboing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {
    private JFrame frame;
    private BufferStrategy buffer;
    private BufferedImage bufferedImage;

    private Graphics graphics;
    private GraphicsWrapper gw;

    private KeyListener keyListener;

    private int width;
    private int height;
    private int refreshRate;

    private int horizontalPadding;
    private int verticalPadding;
    private int renderWidth;
    private int renderHeight;

    private GraphicsConfiguration graphicsConf;

    public Window() {
        // Create a JFrame
        keyListener = new KeyListener();
        frame = new JFrame();
        frame.setIgnoreRepaint(true);
        frame.setUndecorated(true);
        frame.setFocusable(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Blob Boing!");
        frame.addKeyListener(keyListener);

        // Determine display settings
        GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDev = graphicsEnv.getDefaultScreenDevice();
        graphicsConf = graphicsDev.getDefaultConfiguration();
        DisplayMode displayMode = graphicsDev.getDisplayMode();
        width = displayMode.getWidth();
        height = displayMode.getHeight();
        refreshRate = displayMode.getRefreshRate();

        graphicsDev.setFullScreenWindow(frame);
        frame.createBufferStrategy(2);
        buffer = frame.getBufferStrategy();
        graphics = buffer.getDrawGraphics();

        // Determine the padding required to preserve a 16:9 ratio
        double scale = 16.0 / 9;
        if (height * scale > width) {
            renderWidth = width;
            renderHeight = (int)Math.round(width / scale);
            horizontalPadding = 0;
            verticalPadding = (height - renderHeight)/2;
        } else {
            renderWidth = (int)Math.round(height * scale);
            renderHeight = height;
            horizontalPadding = (width - renderWidth)/2;
            verticalPadding = 0;
        }

        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, width, height);

        // Create a BufferedImage to draw on
        bufferedImage = createBufferedImage();
        Graphics2D g2d = bufferedImage.createGraphics();
        gw = new GraphicsWrapper(g2d, (double)renderHeight / 1080);

        System.out.println((double)renderHeight / 1080);
    }
    // Description: Getter method for renderWidth
    // Return: the width of the rendering display
    public int getRenderWidth() {
        return renderWidth;
    }
    // Description: Getter method for renderHeight
    // Return: the height of the rendering display
    public int getRenderHeight() {
        return renderHeight;
    }

    // Description: Creates a BufferedImage
    // Return: a BufferedImage
    public BufferedImage createBufferedImage() {
        return graphicsConf.createCompatibleImage(renderWidth, renderHeight);
    }

    // Description: Getter method for width
    // Return: the width of the user's display
    public int getWidth() {
        return width;
    }

    // Description: Getter method for height
    // Return: the height of the user's display
    public int getHeight() {
        return height;
    }

    // Description: Getter method for refreshRate
    // Return: the refresh rate of the user's display
    public int getRefreshRate() {
        return refreshRate;
    }

    // Description: Getter method for keyListener
    // Return: keyListener
    public KeyListener getKeyListener() {
        return keyListener;
    }

    // Description: Getter method for gw
    // Return: gw
    public GraphicsWrapper getGraphics() {
        return gw;
    }

    // Description: Transfers the contents of the BufferedImage
    //              to the screen
    public void blit() {
        graphics.drawImage(bufferedImage, horizontalPadding,
                verticalPadding, width-horizontalPadding,
                height-verticalPadding, 0, 0, renderWidth,
                renderHeight, null);

        if (!buffer.contentsLost()) {
            buffer.show();
        }
    }
}
