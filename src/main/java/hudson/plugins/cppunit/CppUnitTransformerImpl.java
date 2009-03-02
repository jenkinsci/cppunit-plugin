package hudson.plugins.cppunit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

public class CppUnitTransformerImpl implements CppUnitTransformer{

    private transient boolean xslIsInitialized;
    private transient Transformer cppunitXMLTransformer;    
    
    private static final String JUNIT_FILE_POSTFIX = ".xml";
    private static final String JUNIT_FILE_PREFIX = "TEST-";
    
    public static final String CPPUNIT_TO_JUNIT_XSL = "cppunit-to-junit.xsl";    
	
    /**
     * Transform the cppunit file into several a junit files in the output path
     * 
     * @param cppunitFileStream the cppunit file stream to transform
     * @param junitOutputPath the output path to put all junit files
     * @throws IOException thrown if there was any problem with the transform.
     * @throws TransformerException
     * @throws SAXException
     * @throws ParserConfigurationException 
     */
    public void transform(String cppunitFileName, InputStream cppunitFileStream, File junitOutputPath) throws IOException, TransformerException,
            SAXException, ParserConfigurationException {
        
    	initializeProcessor();        
        File junitTargetFile = new File(junitOutputPath, JUNIT_FILE_PREFIX + cppunitFileName + JUNIT_FILE_POSTFIX);
        FileOutputStream fileOutputStream = new FileOutputStream(junitTargetFile);
        try {
        	cppunitXMLTransformer.transform(new StreamSource(cppunitFileStream), new StreamResult(fileOutputStream));
        } finally {
            fileOutputStream.close();
        }
    }    
    
    private void initializeProcessor() throws TransformerFactoryConfigurationError, TransformerConfigurationException,ParserConfigurationException {
    	if (!xslIsInitialized) {
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		cppunitXMLTransformer = transformerFactory.newTransformer(new StreamSource(this.getClass().getResourceAsStream(CPPUNIT_TO_JUNIT_XSL)));
    		xslIsInitialized = true;
    	}
    }    	
}
