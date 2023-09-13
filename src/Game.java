import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    //используемые значения
    private final int SIZE = 320;
    //размер клеточки
    private final int SIZEIM= 16;
    private final int ALL_SNAKES = 400;
    //картинки
    private Image snake;
    private Image apple;
    private Image menu;
    private Image fon;
    private Image but;
    //координаты яблока
    private int appleX;
    private int appleY;
    //координаты каждой клетки змеи
    private int[] x = new int[ALL_SNAKES];
    private int[] y = new int[ALL_SNAKES];
    //длина змеи
    private int count;
    //таймер
    private Timer timer;
    //используется для хождения(изначально змейка смотрит вправо)
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    //в игре ты или нет(изначально стоит true)
    private boolean inGame = true;


    //неиспользоанное перечисление
    public static enum STATES {MENU, START}
    ;
    //Меню нерабочее
    public static STATES states = STATES.START;


    private int n = 3;
    private Color color1 = Color.BLACK;
    private static int mouseX;
    private static int mouseY;
    private int a = 120;
    private int b = 0;
    private int c = 100;
    private int d = 100;
    String[] list = new String[]{"Start", "Load", "Exit"};

    //конструктор, в котором вызываются все основные методы
    public Game() throws IOException {
        //Нерабочее меню
        if (states.equals(STATES.MENU)) {
            loadImagesformenu();
            addMouseMotionListener(new Listener());
            addMouseListener(new Listener());
        }
        if (states.equals(STATES.START)) {
            loadImages();
            initGame();
            //добавление работы клавишей клавиатуры
            addKeyListener(new FieldKeyListener());
            setFocusable(true);
            //кнопки на панели: restart stop exit save load
            JButton start = new JButton("Restart");
            start.addActionListener(e ->
            {
                try {
                    new Window();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            });

            add(start, BorderLayout.PAGE_START);
            JButton stop = new JButton("Stop");
            stop.addActionListener(e -> timer.stop());
            add(stop, BorderLayout.PAGE_END);
            JButton exit = new JButton("Exit");
            exit.addActionListener(e -> System.exit(0));
            add(exit, BorderLayout.PAGE_END);
            JButton save = new JButton("Save");
            save.addActionListener(e -> {
                try {
                    saveGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(save, BorderLayout.PAGE_END);
            JButton load = new JButton("Load");
            load.addActionListener(e -> {
                try {
                    loadGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            add(load, BorderLayout.PAGE_END);
        }
    }
    //первоначальное состояние игры
    public void initGame() {
        count = 3;
        for (int i = 0; i < count; i++) {
            x[i] = 48 - i * SIZEIM;
            y[i] = 48;
        }
        //таймер, 200мс с какой частотой изменяется картинка
        timer = new Timer(200, this);
        timer.start();
        createApple();
    }
    // создание яблока в рандомном месте карты и возращение значения координаты яблока в виде list
    public ArrayList<String> createApple() {
        ArrayList<String> m = new ArrayList<>();
        appleX = new Random().nextInt(20) * SIZEIM;
        appleY = new Random().nextInt(20) * SIZEIM;
        m.add(String.valueOf(appleX));
        m.add(String.valueOf(appleY));
        return m;
    }
    // загрузка изображений в программу
    public void loadImages() {
        ImageIcon iif = new ImageIcon("fon.png");
        fon = iif.getImage();
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("snake.png");
        snake = iid.getImage();
    }
    // переопределенный метод Jpanel для того, чтобы картины смогли отбразиться в окне
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menu, 0, 0, this);
        for (int i = 0; i < n; i++) {
            g.drawImage(but, a, (b + 75) * i, null);
            g.setColor(color1);
            Font font = new Font("Arial", Font.ITALIC, 60);
            long length = (int) g.getFontMetrics().getStringBounds(list[i], g).getWidth();
            g.drawString(list[i], (a + c / 2) - (int) (length / 2), ((b + 100) * i + d / 3 * 2));
        }
        if (inGame) {
            g.drawImage(fon, 0, 0, this);
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < count; i++) {
                g.drawImage(snake, x[i], y[i], this);
            }

        } else {

        }
    }
    //метод хождения змейки
    public void move() {
        for (int i = count; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= SIZEIM;
        }
        if (right) {
            x[0] += SIZEIM;
        }
        if (up) {
            y[0] -= SIZEIM;
        }
        if (down) {
            y[0] += SIZEIM;
        }
    }
    // проверка на проигрыш
    public void checkloose() {
        for (int i = count; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }
    // проверяет, съедено яблоко или нет и возращает значение длины змейки в последний момент
    public String checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            count++;
            createApple();
        }
        return String.valueOf(count);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (states.equals(STATES.MENU)) {
            if (mouseX > a && mouseX < a + c && mouseY > (b + 100) * 1 && mouseY < (b + 100) * 1 + d) {
                list[1] = "Play!!";
            } else {
                list[1] = "Load";
            }
            repaint();
        }
        if (states.equals(STATES.START)) {
            if (inGame) {
                checkApple();
                checkloose();
                move();

            }
            repaint();
        }
    }
    // имплементированный метод ActionListener для привязки к клавиатуре
    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_CAPS_LOCK) {
                try {
                    Window mw = new Window();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && !down) {
                right = false;
                up = true;
                left = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                right = false;
                down = true;
                left = false;
            }
        }
    }
    //неиспользованный метод для подключения мыши
    class Listener implements MouseMotionListener, MouseListener {


        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public void loadImagesformenu() {
        ImageIcon iif = new ImageIcon("fon.png");
        menu = iif.getImage();
        ImageIcon iik = new ImageIcon("but.png");
        but = iik.getImage();
    }
    //сохранение игры в файл save.txt с помощью bufferedwriter
    public void saveGame() throws IOException {
        if (inGame) {
            BufferedWriter writer = new BufferedWriter(new FileWriter("save.txt"));
            for (String mas : createApple()) {
                writer.write(mas + " ");
            }
            writer.newLine();
            writer.write(checkApple());
            writer.newLine();
            //использование стримов для преобразования int[] массива в строку
            String stx = Arrays.stream(x)
                    .mapToObj(String::valueOf)
                    .reduce((x, y) -> x + " " + y)
                    .get();
            String sty = Arrays.stream(y)
                    .mapToObj(String::valueOf)
                    .reduce((x, y) -> x + " " + y)
                    .get();
            writer.write(stx);
            writer.newLine();
            writer.write(sty);
            writer.flush();
            //закрытие потока
            writer.close();
        }
    }
    //Загрузка игры из файла save.txt с помощью BufferedReader
    public void loadGame() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
        String line;
        int i = 0;
        String[] k = new String[1];

        String[] LX = new String[ALL_SNAKES];
        String[] LY = new String[ALL_SNAKES];
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if(i == 0) {
                k = line.split(" ");
            }
            if(i == 1) {
                count = Integer.parseInt(line);
            }
            if (i == 2) {
                LX = line.split(" ");
            }
            if (i == 3) {
                LY = line.split(" ");
            }
            i++;
        }
        appleX = Integer.parseInt(k[0]);
        appleY = Integer.parseInt(k[1]);
        for (int a = 0; a < LY.length; a++) {
            y[a] = Integer.parseInt(LY[a]);
        }
        for (int a = 0; a < LX.length; a++) {
            System.out.println(LX[a]);
            x[a] = Integer.parseInt(LX[a]);
        }
        System.out.println(count);
        Game game = new Game();
    }
}