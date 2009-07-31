package hudson.plugins.cppunit.util;

import java.io.PrintStream;
import java.io.Serializable;

public class Messages implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * Log output to the given logger, using the CppUnit identifier
     * @param logger The logger
     * @param message The message to be outputted
     */
    public static void log(PrintStream logger, final String message) {    	
    	logger.println("[CppUnit] " + message);
    }
    
}

