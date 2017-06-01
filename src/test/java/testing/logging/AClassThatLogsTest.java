package testing.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.apache.logging.log4j.Level.INFO;
import static org.apache.logging.log4j.Level.TRACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static testing.logging.Assertions.assertLogs;

class AClassThatLogsTest {


  @BeforeAll
  public static void addAppender() {
    ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
//    builder.setStatusLevel(Level.DEBUG);
    builder.setConfigurationName("LogTestingBuilder");

    AppenderComponentBuilder appenderBuilder = builder.newAppender("TestLogging", "TestingLoggingAppender");

    builder.add(appenderBuilder);

    builder.add(builder.newRootLogger(Level.ALL).add(builder.newAppenderRef("TestLogging")));
    LoggerContext context = Configurator.initialize(builder.build());
    context.start();
//    LoggerContext.getContext().start(builder.build());
  }

  @Test
  @DisplayName("Some long name that could be the method name.")
  public void testLogging() {
//    assertThrows(Exception.class, () -> AClassThatLogs.functionLoggingWithRootLogger());
    assertLogs("Global logger", () -> AClassThatLogs.functionLoggingWithRootLogger());
    assertLogs(INFO, "Global logger", () -> AClassThatLogs.functionLoggingWithRootLogger());
  }

  @Test
  public void testDefaultLoggerCapture() {
    assertLogs(INFO, "Default logger info message", () -> AClassThatLogs.functionThatUsesDefaultLogger());
  }

  @Test
  public void testNamedLoggerCapture() {
    assertLogs(TRACE, "Named logger info message", () -> AClassThatLogs.functionThatUsesNamedLogger());
  }

  @Test
  public void testMultipleLogsDuringASingleCall() {

    List<LogEvent> logEvents = assertLogs(() -> AClassThatLogs.multipleLogMessages());

    assertEquals("Message 1", logEvents.get(0).getMessage().getFormattedMessage());
    assertEquals("Message 2", logEvents.get(1).getMessage().getFormattedMessage());
    assertEquals("Message 3", logEvents.get(2).getMessage().getFormattedMessage());
    assertEquals("Message 4", logEvents.get(3).getMessage().getFormattedMessage());
    assertEquals("Message 5", logEvents.get(4).getMessage().getFormattedMessage());
  }
}
