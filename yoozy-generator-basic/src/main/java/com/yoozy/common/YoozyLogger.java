package com.yoozy.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.*;

public class YoozyLogger extends Logger {

    public static final Logger log = Logger.getLogger(YoozyLogger.class.getName());

    /**
     * Protected method to construct a logger for a named subsystem.
     * <p>
     * The logger will be initially configured with a null Level
     * and with useParentHandlers set to true.
     * @param name               A name for the logger.  This should
     *                           be a dot-separated name and should normally
     *                           be based on the package name or class name
     *                           of the subsystem, such as java.net
     *                           or javax.swing.  It may be null for anonymous Loggers.
     * @param resourceBundleName name of ResourceBundle to be used for localizing
     *                           messages for this logger.  May be null if none
     *                           of the messages require localization.
     * @throws MissingResourceException if the resourceBundleName is non-null and
     *                                  no corresponding resource can be found.
     */
    protected YoozyLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
        initLogger();
    }

    public static YoozyLogger getLogger(String name) {
        Logger l = Logger.getLogger(name);
        YoozyLogger logger = new YoozyLogger(l.getName(), l.getResourceBundleName());
        return logger;
    }


    public void initLogger() {
        YoozyLogger logger = this;
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new Formatter() {
            final String format = "%s%s%n";

            @Override
            public String format(LogRecord record) {
                /* format : %1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS %1$Tp %2$s%n%4$s: %5$s%6$s%n */
                // dat.setTime(record.getMillis());
                String source;
                if (record.getSourceClassName() != null) {
                    source = record.getSourceClassName();
                    if (record.getSourceMethodName() != null) {
                        source += " " + record.getSourceMethodName();
                    }
                } else {
                    source = record.getLoggerName();
                }
                String message = formatMessage(record);
                switch (record.getLevel().getName()) {
                    case "SEVERE":
                        message = "\033[31m" + message + "\033[0m";
                        break;
                    case "WARNING":
                        message = "\033[33m" + message + "\033[0m";
                        break;
                    case "INFO":
                        message = "\033[32m" + message + "\033[0m";
                        break;
                    case "CONFIG":
                        message = "\033[34m" + message + "\033[0m";
                        break;
                    case "FINE":
                        message = "\033[35m" + message + "\033[0m";
                        break;
                    case "FINER":
                        message = "\033[36m" + message + "\033[0m";
                        break;
                    case "FINEST":
                        message = "\033[37m" + message + "\033[0m";
                        break;
                    case "OFF":
                        message = "\033[38m" + message + "\033[0m";
                        break;
                    case "ALL":
                        message = "\033[39m" + message + "\033[0m";
                        break;
                    default:
                        break;
                        
                }
                String throwable = "";
                if (record.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    pw.println();
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    throwable = sw.toString();
                }
                return String.format(format,
                        // dat,
                        // source,
                        // record.getLoggerName(),
                        // record.getLevel().getLocalizedLevelName(),
                        message, throwable);
            }
        });
        logger.addHandler(handler);
        logger.setLevel(Level.ALL);
    }


    public static void main(String[] args) throws InterruptedException {

        YoozyLogger logger = YoozyLogger.getLogger(YoozyLogger.class.getName());

        // 记录不同级别的日志
        logger.severe("这是一条 SEVERE 级别的日志");
        logger.warning("这是一条 WARNING 级别的日志");
        logger.info("这是一条 INFO 级别的日志");
        logger.config("这是一条 CONFIG 级别的日志");
        logger.fine("这是一条 FINE 级别的日志");
        logger.finer("这是一条 FINER 级别的日志");
        logger.finest("这是一条 FINEST 级别的日志");
    }
}
