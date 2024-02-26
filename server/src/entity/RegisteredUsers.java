package entity;

import shared_entity.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO make singleton
public class RegisteredUsers {
    private ArrayList<User> userList;
    private final static String FILE_NAME = "users.dat";

    public RegisteredUsers() {
        userList = new ArrayList<>();
        //loadUsersFromFile();
    }

    public User findUser(String username) {
        System.out.println(userList);
        for (User user : userList) {
            if (user.equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user) {
        userList.addLast(user);
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(userList);
            System.out.println("Users saved to file!");
        } catch (IOException e) {
            System.out.println("IOException, failed to save users to file - " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadUsersFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            userList = (ArrayList<User>) in.readObject(); //does it have to be List?
        } catch (IOException e) {
            System.out.println("IOException, failed to load users from file - " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
