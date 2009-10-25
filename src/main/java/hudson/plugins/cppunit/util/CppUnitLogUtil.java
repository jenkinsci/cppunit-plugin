package hudson.plugins.cppunit.util;

import hudson.model.BuildListener;

import java.io.Serializable;

public class CppUnitLogUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Log output to the given logger, using the CppUnit identifier
     *
     * @param listener The current listener
     * @param message  The message to be outputted
     */
    public static void log(BuildListener listener, final String message) {
        listener.getLogger().println("[CppUnit] " + message);
    }
}

