package control;

import boundary.LoginBoundary;
import entity.ActivityFileLogger;
import entity.ClientConnection;
import entity.ClientConnectionList;
import entity.RegisteredUsers;
import shared_entity.message.LoginMessage;
import shared_entity.user.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class LoginHandler extends Thread {
    private ServerSocket serverSocket;
    private RegisteredUsers registeredUsers;
    private ClientConnectionList clientConnectionList;
    private Server server;
    private ActivityFileLogger logger;

    public LoginHandler(ServerSocket serverSocket, Server server, RegisteredUsers registeredUsers) {
        this.serverSocket = serverSocket;
        this.registeredUsers = registeredUsers; //TODO read from file instead
        clientConnectionList = ClientConnectionList.getInstance();
        this.server = server;
        this.logger = new ActivityFileLogger();
        start();
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                new ClientLogin(serverSocket.accept());
                System.out.println("Server connected with client");
            } catch (IOException ioe) {
                System.out.println("Server: Client Socket Error");
            }
        }
    }

    public class ClientLogin {
        private User user;
        private ClientConnection clientConnection;
        private LoginBoundary loginBoundary;

        public ClientLogin(Socket clientSocket) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
                oos.flush(); //required because of buffer
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                clientConnection = new ClientConnection(clientSocket, oos, ois);
                loginBoundary = new LoginBoundary(this, oos, ois);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.out.println("Login Server: Initialization Error");
            }
            loginUser();
        }

        public void loginUser() {
            int responseToClient = 10;
            String username = loginBoundary.readUsernameFromClient();
            System.out.println("Username: " + username + " was put in from user");
            //TODO currently looping infinitely if this doesn't pass because Client automatically inputs same name
            while (!(username.length() > 3 && username.length() < 18 && username.matches("^[a-zA-Z0-9]+"))) {
                System.out.println("Invalid username");
                loginBoundary.writeResponseToClient(responseToClient);
                username = loginBoundary.readUsernameFromClient();
            }
            user = registeredUsers.findUser(username);
            if (user != null) {
                responseToClient = 11;
                System.out.println("Logging in to user: " + username);
                logger.logInfo("Logging in to user " + username, LocalDateTime.now());
            } else {
                responseToClient = 12;
                System.out.println("Creating new user: " + username);
                logger.logInfo("Creating new user " + username, LocalDateTime.now());
                user = new User(username);
                registeredUsers.addUser(user);
                loginBoundary.writeResponseToClient(responseToClient);
                try {
                    byte[] profilePicture = loginBoundary.readProfilePictureFromClient();
                    ByteArrayInputStream bis = new ByteArrayInputStream(profilePicture);
                    BufferedImage bufferedImage = ImageIO.read(bis);
                    ImageIcon imageIcon = new ImageIcon(bufferedImage);
                    user.setUserIcon(imageIcon);
                    System.out.println("Nås?");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.out.println("Login Server: Profile Picture Error");
                }
            }
            LoginMessage loginMessage = new LoginMessage(user);
            loginBoundary.writeUserMessageToClient(loginMessage);

            server.connectClient(user, clientConnection);
            logger.logInfo("Connected user " + user + " to main server", LocalDateTime.now());
            System.out.println("Connected client to main server");
        }
    }
}
