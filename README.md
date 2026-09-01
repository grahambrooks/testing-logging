# testing-logging 🧪

> **Stop asserting logs like it's 2010.** A simple, elegant Java library for testing Log4j logging in unit tests.

Testing logging is notoriously tricky—typically you're left capturing stderr, mocking loggers, or worse, just hoping logs work. This project demonstrates a practical, maintainable approach to unit testing your application's logging behavior.

## 🎯 What's This About?

Ever wanted to verify that your code logs the right messages at the right levels? This library provides:

- **Simple API** — Clean, readable assertions for log messages
- **Zero Dependencies** — Uses only JUnit 5 and Log4j 2
- **Type-Safe** — Strongly typed assertions with compile-time checking
- **Modern Stack** — Built on JUnit 6+ and Maven 3.x for latest Java compatibility

## 🚀 Quick Start

```java
@Test
public void testLogging() {
    List<LogEvent> events = assertLogs(() -> 
        MyClass.doSomethingThatLogs()
    );
    
    assertEquals("Expected message", events.get(0).getMessage().getFormattedMessage());
}
```

Or verify specific log levels:

```java
@Test
public void testErrorLogging() {
    assertLogs(Level.ERROR, "Database connection failed", () -> 
        database.connect()
    );
}
```

## 🏗️ Architecture

The library works by:
1. Creating a custom Log4j `TestingLoggingAppender` 
2. Programmatically registering it with the LoggerContext
3. Capturing log events during test execution
4. Providing fluent assertions on captured events

## 📋 Requirements

- **Java 8+** (tested with JDK 11+)
- **Maven 3.6+**
- **JUnit 5.14+** and **Log4j 2.26+**

## 🧪 Running Tests

```bash
# Run all tests
mvn clean test

# Run a specific test
mvn test -Dtest=AClassThatLogsTest#testMultipleLogsDuringASingleCall
```

## 📚 Key Features

| Feature | Details |
|---------|---------|
| **Message Matching** | Assert exact log messages without regex hassles |
| **Level Checking** | Verify logs at specific levels (INFO, WARN, ERROR, etc.) |
| **Multiple Events** | Capture and assert on sequences of log events |
| **Thread-Safe** | Uses ReadWriteLock for concurrent test execution |
| **Minimal Setup** | No XML config files needed—pure Java configuration |

## 🔧 How It Works

The `TestingLoggingAppender` implements Log4j's `AbstractAppender` and:
- Maintains a static list of capture functions
- Intercepts log events before they hit the console
- Provides hook-based capture mechanism via `startCapture()`
- Stores events in a thread-safe list for assertions

## 🎓 Use Cases

✅ Verify error handling logs  
✅ Confirm startup/shutdown logging  
✅ Test multi-step processes with intermediate logging  
✅ Validate audit trail generation  
✅ Integration testing with logging verification  

## 📝 Example Test

```java
@BeforeAll
public static void setup() {
    // Custom appender is registered here
    AClassThatLogsTest.addAppender();
}

@Test
public void testMultipleLogsDuringASingleCall() {
    List<LogEvent> events = assertLogs(() -> 
        AClassThatLogs.multipleLogMessages()
    );
    
    assertEquals(5, events.size());
    assertEquals("Message 1", events.get(0).getMessage().getFormattedMessage());
    assertEquals("Message 5", events.get(4).getMessage().getFormattedMessage());
}
```

## 🤝 Contributing

This is a demonstration project, but improvements are welcome! Areas of interest:
- Additional assertion helpers
- Performance optimizations
- Kotlin/Spring Boot integration examples
- Async logging support

## 📄 License

MIT License - use freely in your projects

---

**Made with ❤️ for developers who care about testing logging properly.**
