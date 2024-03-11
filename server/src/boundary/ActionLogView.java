package boundary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The ActionLogView class provides functionality to view activity logs within a specified time range.
 * It prompts the user to enter start and end date and time, then displays the activity logs between these timestamps.
 */
public class ActionLogView {
    private Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor for the ActionLogView class.
     * Initializes the scanner and starts a separate thread to continuously prompt the user for date inputs.
     */
    public ActionLogView() {
        this.scanner = new Scanner(System.in);
        Thread thread = new Thread(this::askForDateForTraffic);
        thread.start();
    }

    /**
     * Method to continuously prompt the user for start and end date and time inputs, and display traffic logs accordingly.
     */
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

    /**
     * Method to display traffic logs from a specified file within a given time range.
     * @param filePath The path to the log file.
     * @param startTime The start time of the time range.
     * @param endTime The end time of the time range.
     */
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