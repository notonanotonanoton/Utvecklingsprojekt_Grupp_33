package shared_entity.user;

import javax.swing.ImageIcon;
import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private ImageIcon userIcon;

    public User(String userName) {
        this.userName = userName;
        userIcon = new ImageIcon();
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

    @Override
    public String toString() {
        return userName;
    }
}
