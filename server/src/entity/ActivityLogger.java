package entity;

import java.time.LocalDateTime;

// The ActivityLogger interface defines methods for logging information.
public interface ActivityLogger {
    void logInfo(String message, LocalDateTime date);
}
