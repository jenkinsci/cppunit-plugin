package hudson.plugins.cppunit;

import org.junit.Test;


public class CppUnitXSLTest extends AbstractXUnitXSLTest {

    public CppUnitXSLTest() {
        super(CppUnitType.class);
    }

    @Test
    public void cppuniTestcase1() throws Exception {
        processTransformation("cppunit/testcase1/cppunit-successAndFailure.xml", "cppunit/testcase1/junit-result.xml");
    }

    @Test
    public void cppuniTestcase2() throws Exception {
        processTransformation("cppunit/testcase2/cppunit-zeroFailure.xml", "cppunit/testcase2/junit-result.xml");
    }

    @Test
    public void cppuniTestcase3() throws Exception {
        processTransformation("cppunit/testcase3/cppunit-zeroFailureAndSuccess.xml", "cppunit/testcase3/junit-result.xml");
    }

    @Test
    public void cppuniTestcase4() throws Exception {
        processTransformation("cppunit/testcase4/cppunit-zeroSuccess.xml", "cppunit/testcase4/junit-result.xml");
    }

    @Test
    public void adaTtestcase1() throws Exception {
        processTransformation("ada/ada-cppunit-successAndFailure.xml", "ada/junit-ada-cppunit-successAndFailure.xml");
    }

    @Test
    public void adaTtestcase2() throws Exception {
        processTransformation("ada/ada-cppunit-zeroFailure.xml", "ada/junit-ada-cppunit-zeroFailure.xml");
    }

    @Test
    public void adaTtestcase3() throws Exception {
        processTransformation("ada/ada-cppunit-zeroFailureAndSucces.xml", "ada/junit-ada-cppunit-zeroFailureAndSuccess.xml");
    }

        @Test
    public void adaTtestcase4() throws Exception {
        processTransformation("ada/ada-cppunit-zeroSucces.xml", "ada/junit-ada-cppunit-zeroSuccess.xml");
    }
}
