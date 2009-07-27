package hudson.plugins.cppunit;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Transform;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;



public class CppUnitToJUnitXslTest {

	@Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }

    @Test
    public void testSuccessAndFailure() throws Exception {
    	processTransform("cppunit-successAndFailure.xml","junit-cppunit-successAndFailure.xml");
    }
    @Test
    public void testZeroFailure() throws Exception {
    	processTransform("cppunit-zeroFailure.xml","junit-cppunit-zeroFailure.xml");
    }
    @Test
    public void testZeroSuccess() throws Exception {
    	processTransform("cppunit-zeroSuccess.xml","junit-cppunit-zeroSuccess.xml");
    }
    @Test
    public void testZeroSuccessAndFailure() throws Exception {
    	processTransform("cppunit-zeroFailureAndSuccess.xml","junit-cppunit-zeroFailureAndSuccess.xml");   
    }

    
    @Test
    public void testAdaSuccessAndFailure() throws Exception {
    	processTransform("ada/ada-cppunit-successAndFailure.xml","ada/junit-ada-cppunit-successAndFailure.xml");
    }
    @Test
    public void testAdaZeroFailure() throws Exception {
    	processTransform("ada/ada-cppunit-zeroFailure.xml","ada/junit-ada-cppunit-zeroFailure.xml");
    }
    @Test
    public void testAdaZeroSuccess() throws Exception {
    	processTransform("ada/ada-cppunit-zeroSuccess.xml","ada/junit-ada-cppunit-zeroSuccess.xml");
    }
    @Test
    public void testAdaZeroSuccessAndFailure() throws Exception {
    	processTransform("ada/ada-cppunit-zeroFailureAndSuccess.xml","ada/junit-ada-cppunit-zeroFailureAndSuccess.xml");   
    }
    
    
    private void processTransform(String cppunitFile, String destJunitXMLFile) throws Exception {

        Transform myTransform = new Transform(
        						new InputSource(this.getClass().getResourceAsStream(cppunitFile)),
        						new InputSource(this.getClass().getResourceAsStream(CppUnitTransformer.CPPUNIT_TO_JUNIT_XSL)));

        Diff myDiff = new Diff(readXmlAsString(destJunitXMLFile), myTransform.getResultString());
        assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());
    }
    
    
    private String readXmlAsString(String resourceName) throws IOException {
        String xmlString = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourceName)));
        String line = reader.readLine();
        while (line != null) {
            xmlString += line + "\n";
            line = reader.readLine();
        }
        reader.close();

        return xmlString;
    }
}
