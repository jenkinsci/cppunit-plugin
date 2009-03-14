package hudson.plugins.cppunit;

import hudson.Plugin;
import hudson.tasks.BuildStep;

/**
 * 
 * The CppUnit Publisher entry point
 *     
 * @author Gregory Boissinot  
 * @plugin cppunit
 * 
 */
public class PluginImpl extends Plugin {
	
    @Override
    public void start() throws Exception {
        BuildStep.PUBLISHERS.addRecorder(CppUnitPublisher.DESCRIPTOR);
    }
    
    
}
