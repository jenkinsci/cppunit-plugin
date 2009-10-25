package hudson.plugins.cppunit;

import hudson.FilePath;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

/**
 * Class responsible for transforming a CppUnit file to a JUnit file and then run them all through the JUnit result archiver.
 *
 * @author Gregory Boissinot
 */
public class CppUnitTransformer implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Transformer cppunitXMLTransformer;

    private static final String JUNIT_FILE_POSTFIX = ".xml";
    private static final String JUNIT_FILE_PREFIX = "TEST-";

    public static final String CPPUNIT_TO_JUNIT_XSL = "cppunit-to-junit.xsl";

    /**
     * Transform the cppunit file into several a junit files in the output path
     *
     * @param cppunitFileName the cppunit file  to transform
     * @param junitOutputPath the output path to put all junit files
     * @throws IOException                  thrown if there was any problem with the transform.
     * @throws TransformerException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void transform(FilePath cppunitFileName, FilePath junitOutputPath) throws IOException, TransformerException,
            SAXException, ParserConfigurationException, InterruptedException, IOException {

        initializeProcessor();
        FilePath junitTargetFile = new FilePath(junitOutputPath, JUNIT_FILE_PREFIX + cppunitFileName.hashCode() + JUNIT_FILE_POSTFIX);
        cppunitXMLTransformer.transform(new StreamSource(new File(cppunitFileName.toURI())), new StreamResult(new File(junitTargetFile.toURI())));
    }

    private void initializeProcessor()
            throws TransformerFactoryConfigurationError, TransformerConfigurationException, ParserConfigurationException,
            InterruptedException, IOException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        StreamSource streamSourceXSL;
        streamSourceXSL = new StreamSource(this.getClass().getResourceAsStream(CPPUNIT_TO_JUNIT_XSL));
        cppunitXMLTransformer = transformerFactory.newTransformer(streamSourceXSL);
    }
}
