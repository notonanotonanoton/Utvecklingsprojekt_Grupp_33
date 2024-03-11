package entity;

import shared_entity.user.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClientContacts class manages a list of contacts for a specific user.
 * It provides methods to add, remove, load, and save contacts to/from a file.
 */
public class ClientContacts {
    private List<User> contactList;
    private final String userFileName;

    public ClientContacts(String username) {
        this.userFileName = username + "_contacts.dat";
        contactList = loadContactsFromFile();
    }

    public void addContact(User contact) {
        contactList.addLast(contact);
        System.out.println("Added " + contact + " to Contacts");
    }

    public void removeContact(String username) {
        for(User user : contactList) {
            if(user.getUserName().equals(username)) {
                contactList.remove(user);
                System.out.println("Removed " + user + " from Contacts");
                return;
            }
        }
    }

    private List<User> loadContactsFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(userFileName))) {
            return (ArrayList<User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading contacts from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveUsersToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(userFileName))) {
            out.writeObject(contactList);
            out.flush();
            System.out.println("Contacts saved to file!");
        } catch (IOException e) {
            System.out.println("Error saving contacts to file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<User> getContactList() {
        return contactList;
    }
}
