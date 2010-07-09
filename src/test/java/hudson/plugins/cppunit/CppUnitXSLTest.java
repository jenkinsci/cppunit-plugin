package hudson.plugins.cppunit;

import org.junit.Test;


public class CppUnitXSLTest extends AbstractXUnitXSLTest {


    @Test
    public void cppuniTestcase1() throws Exception {
        convertAndValidate(CppUnitInputMetric.class, "cppunit/testcase1/cppunit-successAndFailure.xml", "cppunit/testcase1/junit-result.xml");
    }

    @Test
    public void cppuniTestcase2() throws Exception {
        convertAndValidate(CppUnitInputMetric.class, "cppunit/testcase2/cppunit-zeroFailure.xml", "cppunit/testcase2/junit-result.xml");
    }

    @Test
    public void cppuniTestcase3() throws Exception {
        convertAndValidate(CppUnitInputMetric.class, "cppunit/testcase3/cppunit-zeroFailureAndSuccess.xml", "cppunit/testcase3/junit-result.xml");
    }

    @Test
    public void cppuniTestcase4() throws Exception {
        convertAndValidate(CppUnitInputMetric.class, "cppunit/testcase4/cppunit-zeroSuccess.xml", "cppunit/testcase4/junit-result.xml");
    }

}
