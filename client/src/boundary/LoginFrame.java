package boundary;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoginFrame extends JFrame {

    private LoginView loginView;

    LoginFrame(LoginView loginView) {
        initComponents();
        this.loginView = loginView;
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onExit();
            }
        });
        this.setVisible(true);
    }

    private void initComponents() {
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setSize(100,50);
        JTextField usernameField = new JTextField(20);
        JButton loginButton = new JButton("Login");
        JPanel panel = new JPanel();
        panel.setVisible(true);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(loginButton);
        add(panel);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a username.");
                return;
            }
            loginView.sendUsernameToServer(username);
        });
    }

    public void selectProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        while (true) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                    if (isValidFormat(selectedFile)) {
                        loginView.sendProfilePictureToServer(fileContent);
                        break; // Exit the loop if the file is valid
                    } else {
                        JOptionPane.showMessageDialog(this, "Please select a valid image file.");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    private boolean isValidFormat(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png");
    }

    public void onExit() {
        dispose();
    }
}
