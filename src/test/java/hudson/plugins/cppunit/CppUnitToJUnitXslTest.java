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
    public void testTransformation() throws Exception {

        Transform myTransform = new Transform(
        						new InputSource(this.getClass().getResourceAsStream("cppunit-example1.xml")),
        						new InputSource(this.getClass().getResourceAsStream(CppUnitTransformerImpl.CPPUNIT_TO_JUNIT_XSL)));

        Diff myDiff = new Diff(readXmlAsString("junit-cppunit-example1.xml"), myTransform.getResultString());
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
