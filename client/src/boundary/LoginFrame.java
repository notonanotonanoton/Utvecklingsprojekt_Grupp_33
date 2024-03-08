package boundary;
import javax.swing.*;

public class LoginFrame extends JFrame {

    private LoginView loginView;

    //TODO add LoginView parameter
    LoginFrame(LoginView loginView) {
        initComponents();
        this.loginView = loginView;
        this.setTitle("Login");
        this.setSize(300, 200);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

//    public static void main(String[] args) {
//        LoginFrame loginFrame = new LoginFrame();
//    }
}
