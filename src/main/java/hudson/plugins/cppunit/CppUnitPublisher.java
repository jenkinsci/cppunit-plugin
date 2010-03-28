package hudson.plugins.cppunit;

import com.thalesgroup.hudson.plugins.xunit.XUnitPublisher;
import com.thalesgroup.hudson.plugins.xunit.types.XUnitType;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.Serializable;


public class CppUnitPublisher extends Recorder implements Serializable {

    private static final long serialVersionUID = 1L;

    private String testResultsPattern = null;

    private boolean useWorkspaceBaseDir = false;

    @SuppressWarnings("unused")
    public String getTestResultsPattern() {
        return testResultsPattern;
    }

    @SuppressWarnings("unused")
    public boolean isUseWorkspaceBaseDir() {
        return useWorkspaceBaseDir;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    private Object readResolve() {
        return new XUnitPublisher(new XUnitType[]{new CppUnitType(testResultsPattern, false, true)});
    }

}
