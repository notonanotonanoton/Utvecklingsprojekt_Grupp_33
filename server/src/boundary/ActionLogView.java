package boundary;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ActionLogView { // remove print statements in server to use? show after every print?
    private Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ActionLogView() {
        this.scanner = new Scanner(System.in);
        Thread thread = new Thread(this::askForDateForTraffic); // own thread so doesn't interrupt main thread
        thread.start();
    }

    private void askForDateForTraffic() {
        while (true) {
            System.out.println("Enter the start date and time (yyyy-MM-dd HH:mm:ss): ");
            String startDateTime = scanner.nextLine();
            System.out.println("Enter the end date and time (yyyy-MM-dd HH:mm:ss): ");
            String endDateTime = scanner.nextLine();
            LocalDateTime startTime = LocalDateTime.parse(startDateTime, formatter);
            LocalDateTime endTime = LocalDateTime.parse(endDateTime, formatter);
            displayTrafficBetweenDates("activity_log.txt", startTime, endTime);
        }
    }

    public void displayTrafficBetweenDates(String filePath, LocalDateTime startTime, LocalDateTime endTime) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String timestampString = parts[0] + " " + parts[1];
                LocalDateTime timestamp = LocalDateTime.parse(timestampString, formatter);
                if (timestamp.isAfter(startTime) && timestamp.isBefore(endTime)) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException - " + e.getMessage());
            e.printStackTrace();
        }
    }
}