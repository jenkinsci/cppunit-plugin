package hudson.plugins.cppunit;

import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.Serializable;


public class CppUnitPublisher extends Recorder implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient String testResultsPattern = null;

    private transient boolean useWorkspaceBaseDir = false;

    public String getTestResultsPattern() {
        return testResultsPattern;
    }

    public boolean isUseWorkspaceBaseDir() {
        return useWorkspaceBaseDir;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    private Object readResolve() {
        return new CppUnitType(testResultsPattern, true, true);
    }

}
