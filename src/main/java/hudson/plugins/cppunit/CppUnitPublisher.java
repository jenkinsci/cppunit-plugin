package hudson.plugins.cppunit;

import com.thalesgroup.dtkit.metrics.hudson.api.type.TestType;
import com.thalesgroup.hudson.plugins.xunit.XUnitPublisher;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.Serializable;


@SuppressWarnings("unused")
public class CppUnitPublisher extends Recorder implements Serializable {

    private String testResultsPattern = null;

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
        return new XUnitPublisher(new TestType[]{new CppUnitPluginType(testResultsPattern, false, true)});
    }

}
