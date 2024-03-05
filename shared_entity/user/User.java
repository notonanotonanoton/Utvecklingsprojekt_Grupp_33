package shared_entity.user;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;


public class User implements Serializable {
    private String userName;
    private ImageIcon userIcon;
    private UserStatus status;
    private List<User> contacts;

    public User(String userName) {
        this.userName = userName;
        userIcon = new ImageIcon();
        status = UserStatus.OFFLINE;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof User)
            return userName.equals(((User)obj).getUserName());
        return false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ImageIcon getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(ImageIcon userIcon) {
        this.userIcon = userIcon;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void addContact(User user) {
        contacts.add(user);
    }

    @Override
    public String toString() {
        return userName;
    }
}
