package testing.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;

import java.util.List;

public class Assertions {
  public static void assertLogs(String message, AssertLogs.Code executable) {
    AssertLogs.assertLogs(message, executable);
  }

  public static void assertLogs(Level level, String message, AssertLogs.Code executable) {
    AssertLogs.assertLogs(level, message, executable);
  }

  public static List<LogEvent> assertLogs(AssertLogs.Code code) {
    return AssertLogs.assertLogs(code);
  }
}
