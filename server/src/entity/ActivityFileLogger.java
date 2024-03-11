package entity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ActivityFileLogger implements ActivityLogger{
    private static final String LOG_FILE_PATH = "activity_log.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // DateTimeFormatter thread-safe


    @Override
    public void logInfo(String message, LocalDateTime date) {
        writeToLogFile("[INFO] " + message, date);
    }

    @Override
    public void logError(String message, LocalDateTime date) {
        writeToLogFile("[ERROR] " + message, date);

    }

    private void writeToLogFile(String logMessage, LocalDateTime date) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            String formattedDateTime = date.format(DATE_FORMATTER);
            writer.write(formattedDateTime + " " + logMessage);
            writer.newLine();
        } catch (IOException ioe) {
            System.out.println("IO-exception: " + ioe.getMessage());
            ioe.printStackTrace();
        }
    }
}
