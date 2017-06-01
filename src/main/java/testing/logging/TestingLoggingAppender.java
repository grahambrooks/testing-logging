package testing.logging;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

@Plugin(name = "TestingLoggingAppender", category = "Core", elementType = "appender", printObject = true)
public class TestingLoggingAppender extends AbstractAppender {

  private static final List<CaptureFunction> captures = new ArrayList<>();
  private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final Lock readLock = rwLock.readLock();
  List<LogEvent> events = new ArrayList<LogEvent>();

  protected TestingLoggingAppender(String name, Filter filter, final boolean ignoreExceptions) {
    super(name, filter, PatternLayout.createDefaultLayout(), ignoreExceptions);
  }

  @PluginFactory
  public static TestingLoggingAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Filter") final Filter filter) {
    if (name == null) {
      LOGGER.error("No name provided for TestingLoggingAppender");
      return null;
    }
    return new TestingLoggingAppender(name, filter, true);
  }

  public static CaptureClose startCapture(CaptureFunction cf) {
    captures.add(cf);
    return CaptureClose.from(cf);
  }

  public void append(LogEvent event) {
    readLock.lock();
    try {
      for (CaptureFunction c : captures) {
        c.accept(event);
      }
      events.add(event);
    } finally {
      readLock.unlock();
    }
  }

  interface CaptureFunction extends Consumer<LogEvent> {
  }

  static class CaptureClose {

    private final CaptureFunction cf;

    CaptureClose(CaptureFunction cf) {

      this.cf = cf;
    }

    public static CaptureClose from(CaptureFunction cf) {
      return new CaptureClose(cf);
    }

    public void stop() {
      captures.remove(cf);
    }
  }
}

