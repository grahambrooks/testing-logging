package testing.logging;

import org.apache.logging.log4j.LogManager;


public class AClassThatLogs {
  public static void functionLoggingWithRootLogger() {
    LogManager.getRootLogger().info("Global logger");
  }

  public static void functionThatUsesDefaultLogger() {
    LogManager.getLogger().info("Default logger info message");
  }

  public static void functionThatUsesNamedLogger() {
    LogManager.getLogger("testing").trace("Named logger info message");
  }

  public static void multipleLogMessages() {
    LogManager.getLogger().info("Message 1");
    LogManager.getLogger().info("Message 2");
    LogManager.getLogger().info("Message 3");
    LogManager.getLogger().info("Message 4");
    LogManager.getLogger().info("Message 5");
  }
}
