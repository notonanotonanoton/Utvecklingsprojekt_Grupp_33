package shared_entity.user;

import javax.swing.*;

public class User {
    private String userName;
    private ImageIcon userIcon;
    private UserStatus status;

    public User(String userName) {
        this.userName = userName;
        userIcon = new ImageIcon();
        status = UserStatus.OFFLINE;
    }
}
