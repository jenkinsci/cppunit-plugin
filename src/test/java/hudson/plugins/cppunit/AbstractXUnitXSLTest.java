package hudson.plugins.cppunit;


import com.google.inject.Guice;
import com.thalesgroup.dtkit.metrics.api.InputMetricXSL;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;

import java.io.File;

public class AbstractXUnitXSLTest {

    @Before
    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }


    public void convertAndValidate(Class<? extends InputMetricXSL> classType, String inputXMLPath, String expectedResultPath) throws Exception {

        InputMetricXSL inputMetricXSL = Guice.createInjector().getInstance(classType);

        File outputXMLFile = File.createTempFile("result", "xml");
        File inputXMLFile = new File(this.getClass().getResource(inputXMLPath).toURI());

        //The input file must be valid
        Assert.assertTrue(inputMetricXSL.validateInputFile(inputXMLFile));

        inputMetricXSL.convert(inputXMLFile, outputXMLFile);
        Diff myDiff = new Diff(XSLUtil.readXmlAsString(new File(this.getClass().getResource(expectedResultPath).toURI())), XSLUtil.readXmlAsString(outputXMLFile));
        Assert.assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());

        //The generated output file must be valid
        Assert.assertTrue(inputMetricXSL.validateOutputFile(outputXMLFile));

        outputXMLFile.deleteOnExit();
    }


}
