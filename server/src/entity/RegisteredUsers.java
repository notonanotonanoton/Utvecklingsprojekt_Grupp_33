package entity;

import shared_entity.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public final class RegisteredUsers {

    private static RegisteredUsers INSTANCE;
    private ArrayList<User> userList;
    private final static String FILE_NAME = "users.dat";

    public RegisteredUsers() {
        loadUsersFromFile();
        if (userList == null) {
            userList = new ArrayList<>();
        }
    }

    public static synchronized RegisteredUsers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RegisteredUsers();
        }
        return INSTANCE;
    }

    public synchronized User findUser(String userName) {
        System.out.println("Registered Users Object: Pre-existing users: " + userList);
        for (User user : userList) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public synchronized void addUser(User user) {
        userList.addLast(user);
    }

    public synchronized void saveUsersToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(userList);
            System.out.println("Users saved to file!");
        } catch (IOException e) {
            System.out.println("IOException, failed to save users to file - " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void loadUsersFromFile() {
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
