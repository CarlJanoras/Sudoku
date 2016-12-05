import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.imageio.ImageIO;
import java.io.File;


class ImagePanel extends JComponent {

  private Image image;
    public ImagePanel() throws Exception{
        BufferedImage bi = ImageIO.read(new File("main.jpeg"));
        this.image = bi;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0, this.getWidth(), this.getHeight(), this);
    }

}
