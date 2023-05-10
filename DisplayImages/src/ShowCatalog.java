// импортируем необходимые классы
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ShowCatalog {
    // создаем необходимые поля
    private JFrame frame; // главный фрейм программы
    private JPanel panel; // панель на фрейме
    private JButton updateBtn; // кнопка обновления подключенных мониторов
    private JButton applyBtn;  // кнопка вывода изображений
    private JCheckBox[] checkBoxes; // массив флажков для выбора мониторов
    private GraphicsDevice[] devices; // массив подключенных мониторов
    private int numDisplays; // количество подключенных мониторов
    private File directory; // путь к каталогу с изображениями
    private BufferedImage[] images; // массив изображений на ноутбуке

    public ShowCatalog() {
        // инициализируем и настраиваем окно
        frame = new JFrame("Show Catalog");
        panel = new JPanel();
        updateBtn = new JButton("Update Connections");
        applyBtn = new JButton("Apply");

        // получаем массив устройств, подключенных к компьютеру
        devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        numDisplays = devices.length;

        // создаем и добавляем чекбоксы для каждого подключенного монитора
        checkBoxes = new JCheckBox[numDisplays];
        for (int i = 0; i < numDisplays; i++) {
            checkBoxes[i] = new JCheckBox("Monitor " + (i + 1));
            panel.add(checkBoxes[i]);
        }

        // добавляем обработчики для кнопок
        updateBtn.addActionListener(e -> updateConnections());

        applyBtn.addActionListener(e -> {
            int imageIndex = 0;
            for (int i = 0; i < numDisplays; i++) {
                if (checkBoxes[i].isSelected()) {
                    showImage(devices[i], images[imageIndex]); // на i-e устройство выводим i-e изображение
                    imageIndex = (imageIndex + 1) % images.length; //расчитываем индекс следующего изображения в каталоге
                }
            }
        });

        // добавляем кнопки на панель
        panel.add(updateBtn);
        panel.add(applyBtn);

        // добавляем панель на окно и настраиваем окно
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    private void updateConnections() {
        // очищаем панель от старых компонентов
        panel.removeAll();

        // получаем массив устройств, подключенных к компьютеру
        devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        numDisplays = devices.length;

        // добавляем чекбоксы для каждого подключенного монитора
        for (int i = 0; i < numDisplays; i++) {
            panel.add(checkBoxes[i]);
        }

        // добавляем кнопки на панель и перерисовываем панель
        panel.add(updateBtn);
        panel.add(applyBtn);
        panel.repaint();
    }

    private void showImage(GraphicsDevice device, BufferedImage image) {
        // создаем новое окно и настраиваем его
        JFrame imageFrame = new JFrame(device.getDefaultConfiguration());

        // создаем метку для изображения
        JLabel imageLabel = new JLabel();

        // получаем размеры экрана и масштабируем изображение
        Rectangle bounds = device.getDefaultConfiguration().getBounds();
        int width = bounds.width;
        int height = bounds.height;

        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // добавляем изображение на метку и метку на окно
        imageLabel.setIcon(new javax.swing.ImageIcon(scaledImage));
        imageFrame.getContentPane().add(imageLabel);

        // настраиваем окно
        imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        imageFrame.setUndecorated(true);
        imageFrame.setBounds(bounds);
        imageFrame.setVisible(true);
    }

    public void setDirectory(String path) {
        // задаем путь к каталогу с изображениями и загружаем все изображения в массив
        directory = new File(path);
        File[] files = directory.listFiles();
        images = new BufferedImage[files.length];
        try {
            for (int i = 0; i < files.length; i++) {
                images[i] = ImageIO.read(files[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ShowCatalog showCatalog = new ShowCatalog();
        showCatalog.setDirectory("catalog"); // указываем путь к каталогу с изображениями на ноутбуке
    }
}