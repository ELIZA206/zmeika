import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Window extends JFrame {


    public Window() throws IOException {
        //название
        setTitle("Snake");
        //при закрытие окна отключается и сама программа
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //размеры
        setSize(450,390);
        setLocation(400,400);
        //нельзя менять размеры и передвигать окно
        setLocationRelativeTo(null);
        setResizable(false);
        add(new Game());
        setVisible(true);
    }
    public static void main(String[] args) throws IOException {
        //создание окна
        Window mw = new Window();
        // Game s = new Game();
        //s.saveGame();
    }

}
