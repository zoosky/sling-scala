package org.apache.sling.scripting.scala.engine;

import org.slf4j.Logger;

import scala.tools.nsc.Settings;
import scala.tools.nsc.util.Position;

public class LogReporter extends BacklogReporter {
    private final Logger logger;

    public LogReporter(Logger logger, Settings settings) {
        super(settings);
        this.logger = logger;
    }

    @Override
    public void display(Position pos, String msg, Severity severity) {
        super.display(pos, msg, severity);
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

}
