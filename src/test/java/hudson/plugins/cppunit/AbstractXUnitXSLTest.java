package hudson.plugins.cppunit;

import com.thalesgroup.hudson.library.tusarconversion.ConversionUtil;
import com.thalesgroup.hudson.library.tusarconversion.exception.ConversionException;
import com.thalesgroup.hudson.library.tusarconversion.model.InputType;
import com.thalesgroup.hudson.plugins.xunit.types.XUnitType;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.xml.sax.SAXException;

import java.io.*;


public class AbstractXUnitXSLTest {

    private Class<? extends XUnitType> type;

    protected AbstractXUnitXSLTest(Class<? extends XUnitType> type) {
        this.type = type;
        setUp();
    }

    public void setUp() {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setNormalizeWhitespace(true);
        XMLUnit.setIgnoreComments(true);
    }


    protected void conversion(InputType type, String inputPath, String resultPath) throws ConversionException, IOException, SAXException {

        // define the streams (input/output)
        InputStream inputStream = this.getClass().getResourceAsStream(inputPath);

        File target = File.createTempFile("result", "xml");
        OutputStream outputStream = new FileOutputStream(target);

        // convert the input xml file
        ConversionUtil.convert(type, inputStream, outputStream);

        // compare with expected result
        InputStream expectedResult = this.getClass().getResourceAsStream(resultPath);
        InputStream fisTarget = new FileInputStream(target);

        Diff myDiff = new Diff(XUnitXSLUtil.readXmlAsString(expectedResult), XUnitXSLUtil.readXmlAsString(fisTarget));

        Assert.assertTrue("XSL transformation did not work" + myDiff, myDiff.similar());

        fisTarget.close();

    }


}
