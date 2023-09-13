import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    private Image menu;


    public void loadImages() {
        ImageIcon iif = new ImageIcon("fon.png");
        menu = iif.getImage();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menu, 0, 0, this);
    }
}
