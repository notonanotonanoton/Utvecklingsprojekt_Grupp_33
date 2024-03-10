package entity;

import shared_entity.user.User;

import java.util.ArrayList;
import java.util.List;

//TODO write to file before exit!
public class ClientContacts {
    private List<User> contactList;

    //TODO load from file instead!
    public ClientContacts() {
        contactList = new ArrayList<>();
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

    public List<User> getContactList() {
        return contactList;
    }
}
