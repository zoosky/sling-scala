package org.apache.sling.scripting.scala.engine;

import org.slf4j.Logger;

import scala.tools.nsc.Settings;
import scala.tools.nsc.reporters.AbstractReporter;
import scala.tools.nsc.util.Position;

public class LoggingReporter extends AbstractReporter {
    private final Logger logger;
    private final Settings settings;

    public LoggingReporter(Logger logger, Settings settings) {
        super();
        this.logger = logger;
        this.settings = settings;
    }

    @Override
    public void display(Position pos, String msg, Severity severity) {
        if (INFO().equals(severity)) {
            logger.info("{}: {}", msg, pos);
        }
        else if (WARNING().equals(severity)) {
            logger.warn("{}: {}", msg, pos);
        }
        else if (ERROR().equals(severity)) {
            logger.error("{}: {}", msg, pos);
        }
        else {
            throw new IllegalArgumentException("Severtiy out of range");
        }
    }

    @Override
    public void displayPrompt() {
        // empty
    }

    @Override
    public Settings settings() {
        return settings;
    }

}
