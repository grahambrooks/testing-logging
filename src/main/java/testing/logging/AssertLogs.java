package testing.logging;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

class AssertLogs {

  public static void assertLogs(String message, Code executable) {
    List<LogEvent> events = new ArrayList<>();
    TestingLoggingAppender.CaptureClose capture = TestingLoggingAppender.startCapture((e) -> events.add(e.toImmutable()));
    try {

      executable.execute();

      for (LogEvent e : events) {
        if (e.getMessage().getFormattedMessage().equals(message)) {
          return;
        } else {
          throw new AssertionFailedError(String.format("Expected log message \"%s\" not found \"%s\" written.", message, e.getMessage().getFormattedMessage()));
        }
      }
    } catch (Exception e) {
      throw new AssertionFailedError(String.format("Exception while checking log assertion \"%s\".", message), e);
    } finally {
      capture.stop();
    }
  }

  public static void assertLogs(Level level, String message, Code executable) {
    List<LogEvent> events = new ArrayList<>();
    TestingLoggingAppender.CaptureClose capture = TestingLoggingAppender.startCapture((e) -> events.add(e.toImmutable()));
    try {

      executable.execute();

      for (LogEvent e : events) {
        if (e.getMessage().getFormattedMessage().equals(message) && e.getLevel() == level) {
          return;
        } else {
          throw new AssertionFailedError(String.format("Expected %s log message \"%s\" not found %s \"%s\" written.", level, message, e.getLevel(), e.getMessage().getFormattedMessage()));
        }
      }
    } catch (Exception e) {
      throw new AssertionFailedError(String.format("Exception while checking log assertion \"%s\".", message), e);
    } finally {
      capture.stop();
    }

  }

  public static List<LogEvent> assertLogs(Code code) {
    List<LogEvent> events = new ArrayList<>();
    TestingLoggingAppender.CaptureClose capture = TestingLoggingAppender.startCapture((e) -> events.add(e.toImmutable()));
    try {
      code.execute();
    } finally {
      capture.stop();
    }

    return events;
  }

  public interface Code {
    void execute();
  }
}
