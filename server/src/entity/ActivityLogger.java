package entity;

import java.time.LocalDateTime;

public interface ActivityLogger {
    void logInfo(String message, LocalDateTime date);
    void logError(String message, LocalDateTime date);
}
