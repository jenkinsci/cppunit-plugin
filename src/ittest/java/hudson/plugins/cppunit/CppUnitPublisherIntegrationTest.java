package hudson.plugins.cppunit;

import org.jvnet.hudson.test.HudsonTestCase;
import org.jvnet.hudson.test.SingleFileSCM;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Shell;

import java.util.ArrayList;
import java.util.List;


public class CppUnitPublisherIntegrationTest extends HudsonTestCase {


    public void testPeformAnUnstableTest() throws Exception {

        FreeStyleProject project = createFreeStyleProject();

        List<SingleFileSCM> files = new ArrayList<SingleFileSCM>(1);

        String cppunitFileName = "cppunit-successAndFailure.xml";
        files.add(new SingleFileSCM(cppunitFileName, getClass().getResource(cppunitFileName)));
        project.setScm(new MultiFileSCM(files));
        project.getBuildersList().add(new Shell("touch " + cppunitFileName));
        String pattern = cppunitFileName;
        CppUnitPublisher cppUnitPublisher = new CppUnitPublisher();
        cppUnitPublisher.setTestResultsPattern(cppunitFileName);
        cppUnitPublisher.setUseWorkspaceBaseDir(true);
        project.getPublishersList().add(cppUnitPublisher);

        FreeStyleBuild build = project.scheduleBuild2(0).get();

        //Build status
        assertBuildStatus(Result.UNSTABLE, build);

        //Build log
        StringBuffer expectedLog = new StringBuffer();
        expectedLog.append("[CppUnit] Recording of the CppUnit tests results.\r\n");
        expectedLog.append("[CppUnit] Processing 1 files with the pattern '" + pattern + "'.\r\n");
        expectedLog.append("[CppUnit] End recording of the CppUnit tests results.\r\n");
        assertLogContains(expectedLog.toString(), build);
    }


    public void testNoReport() throws Exception {

        FreeStyleProject project = createFreeStyleProject();
        String cppunitFileName = "cppunit-successAndFailure.xml";
        CppUnitPublisher cppUnitPublisher = new CppUnitPublisher();
        project.getPublishersList().add(cppUnitPublisher);
        cppUnitPublisher.setTestResultsPattern(cppunitFileName);
        cppUnitPublisher.setUseWorkspaceBaseDir(true);
        FreeStyleBuild build = project.scheduleBuild2(0).get();

        //Build status
        assertBuildStatus(Result.FAILURE, build);

        //Build log
        StringBuffer expectedLog = new StringBuffer();
        expectedLog.append("[CppUnit] Recording of the CppUnit tests results.\r\n");
        expectedLog.append("[CppUnit] No CppUnit test report file(s) were found with the pattern '" + cppunitFileName + "' relative to '" + build.getWorkspace() + "'.  Did you enter a pattern relative to the correct directory?  Did you generate the XML report(s) for CppUnit?\r\n");
        expectedLog.append("[CppUnit] End recording of the CppUnit tests results.\r\n");
        assertLogContains(expectedLog.toString(), build);
    }


}
