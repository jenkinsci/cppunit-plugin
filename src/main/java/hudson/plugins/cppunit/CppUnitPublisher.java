package hudson.plugins.cppunit;

import com.thalesgroup.dtkit.metrics.hudson.api.type.TestType;
import com.thalesgroup.hudson.plugins.xunit.XUnitPublisher;
import com.thalesgroup.hudson.plugins.xunit.types.CustomType;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Recorder;

import java.io.Serializable;


@SuppressWarnings("unused")
public class CppUnitPublisher extends Recorder implements Serializable {

    private String testResultsPattern;

    private transient boolean useWorkspaceBaseDir;

    private transient boolean useCustomStylesheet;

    private transient String customStylesheet;

    public String getTestResultsPattern() {
        return testResultsPattern;
    }

    public boolean isUseWorkspaceBaseDir() {
        return useWorkspaceBaseDir;
    }

    public boolean getUseCustomStylesheet() {
        return useCustomStylesheet;
    }

    public String getCustomStylesheet() {
        return customStylesheet;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    private Object readResolve() {
        TestType[] testTypes = new TestType[1];
        if (useCustomStylesheet) {
            testTypes[0] = new CustomType(testResultsPattern, customStylesheet, false, true);
        } else {
            testTypes[0] = new CppUnitPluginType(testResultsPattern, false, true);
        }

        return new XUnitPublisher(testTypes);
    }

}
