import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class App {
    private JFrame window;
    private JButton[] menuButtons = new JButton[2];
    private JLabel gameLabel;
    private ImageIcon[] btnIcons = new ImageIcon[2];
    private Icon gameIcon;
    private ImageIcon imageIcon;

    public App() {
        imageIcon = new ImageIcon("Image/Logo.png");
        window = new JFrame("L1ght Y0ur Brain");
        window.setIconImage(imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
        window.setSize(960, 768);
        menuSelection();
        window.getContentPane().setBackground(Color.decode("#2b2b2b"));
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void menuSelection() {
        menuButtons[0] = new JButton();
        menuButtons[1] = new JButton();

        gameLabel = new JLabel();
        gameIcon = new ImageIcon("Image/MenuIcon.png");
        gameLabel.setBounds(220, 30, 512, 384);

        gameLabel.setIcon(gameIcon);
        window.add(gameLabel);
        setupMenuButtons();
    }

    private void setupMenuButtons() {
        for (int i = 0; i < menuButtons.length; i++) {
            String imgPath = "Image/gameMenu" + (i + 1) + ".png";
            btnIcons[i] = new ImageIcon(imgPath);

            menuButtons[i].setBounds(384, (400 + (i * 85)), 200, 55);
            Image img = btnIcons[i].getImage().getScaledInstance(menuButtons[i].getWidth(),
                    menuButtons[i].getHeight(), Image.SCALE_SMOOTH);
            menuButtons[i].setIcon(new ImageIcon(img));

            window.add(menuButtons[i]);
            menuButtons[i].addActionListener(new ButtonHandler());
            menuButtons[i].addMouseListener(new MouseOverHandler());
        }
    }

    private class ButtonHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();

            if (clickedButton == menuButtons[0]) {
                DisPlay game = new DisPlay();
                window.dispose();
            } else if (clickedButton == menuButtons[1]) {
                window.dispose();
            }
        }
    }

    private class MouseOverHandler extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            for (int i = 0; i < menuButtons.length; i++) {
                if (source == menuButtons[i]) {
                    menuButtons[i].setIcon(new ImageIcon((Image) (new ImageIcon("Image/gameMenu" + (i + 3) + ".png"))
                            .getImage().getScaledInstance(menuButtons[i].getWidth(), menuButtons[i].getHeight(),
                                    Image.SCALE_SMOOTH)));
                    break;
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            for (int i = 0; i < menuButtons.length; i++) {
                if (source == menuButtons[i]) {
                    menuButtons[i].setBounds(384, (400 + (i * 85)), 200, 55);
                    Image img = btnIcons[i].getImage().getScaledInstance(menuButtons[i].getWidth(),
                            menuButtons[i].getHeight(), Image.SCALE_SMOOTH);
                    menuButtons[i].setIcon(new ImageIcon(img));
                    break;
                }
            }
        }
    }
}
