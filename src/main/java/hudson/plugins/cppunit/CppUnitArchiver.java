package hudson.plugins.cppunit;

import hudson.AbortException;
import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import hudson.util.IOException2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.xml.sax.SAXException;

/**
 * Class responsible for transforming CppUnit to JUnit files and then run them all through the JUnit result archiver.
 * 
 */
public class CppUnitArchiver implements FilePath.FileCallable<Boolean>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String JUNIT_REPORTS_PATH = "temporary-junit-reports";

    public static final String JUNIT_FILE_POSTFIX = ".xml";
    public static final String JUNIT_FILE_PREFIX = "TEST-";

    public static final String CPPUNIT_TO_JUNIT_XSL = "cppunit-to-junit.xsl";

    private transient boolean xslIsInitialized;
    private transient Transformer cppunitTransformer;    
    
    // Build related objects
    private final BuildListener listener;
    private final String testResultsPattern;

    public CppUnitArchiver(BuildListener listener, String testResults) throws TransformerException {
        this.listener = listener;
        this.testResultsPattern = testResults;
    }

    /** {@inheritDoc} */
    public Boolean invoke(File ws, VirtualChannel channel) throws IOException {
        Boolean retValue = Boolean.TRUE;
        String[] cppunitFiles = findCppUnitReports(ws);
        if (cppunitFiles.length > 0) {
            File junitOutputPath = new File(ws, JUNIT_REPORTS_PATH);
            junitOutputPath.mkdirs();
    
            for (String cppunitFileName : cppunitFiles) {
            	File fileCppunitReport =  new File(ws, cppunitFileName);
                FileInputStream fileStream = new FileInputStream(fileCppunitReport);
                String fileCppunitReportName = fileCppunitReport.getName();
                try {
                    transform(fileCppunitReportName, fileStream, junitOutputPath);
                } catch (TransformerException te) {
                    throw new IOException2(
                            "Could not transform the CppUnit report.", te);
                } catch (SAXException se) {
                    throw new IOException2(
                            "Could not transform the CppUnit report.", se);
                } catch (ParserConfigurationException pce) {
                    throw new IOException2(
                            "Could not initalize the XML parser.", pce);
                } finally {
                    fileStream.close();
                }
            }
        } else {
            retValue = Boolean.FALSE;
        }

        return retValue;
    }

    /**
     * Return all CppUnit report files
     * 
     * @param parentPath parent
     * @return an array of strings
     */
    private String[] findCppUnitReports(File parentPath) throws AbortException {
        FileSet fs = Util.createFileSet(parentPath,testResultsPattern);
        DirectoryScanner ds = fs.getDirectoryScanner();

        String[] cppunitFiles = ds.getIncludedFiles();
        if (cppunitFiles.length == 0) {
            // no test result. Most likely a configuration error or fatal problem
            listener.fatalError("No CppUnit test report files were found. Configuration error?");
        }
        return cppunitFiles;
    }
    
    
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
    private void transform(String cppunitFileName, InputStream cppunitFileStream, File junitOutputPath) throws IOException, TransformerException,
            SAXException, ParserConfigurationException {
        
    	initializeProcessor();        
        File junitTargetFile = new File(junitOutputPath, JUNIT_FILE_PREFIX + cppunitFileName + JUNIT_FILE_POSTFIX);
        FileOutputStream fileOutputStream = new FileOutputStream(junitTargetFile);
        try {
        	cppunitTransformer.transform(new StreamSource(cppunitFileStream), new StreamResult(fileOutputStream));
        } finally {
            fileOutputStream.close();
        }
    }    
    
    private void initializeProcessor() throws TransformerFactoryConfigurationError, TransformerConfigurationException,ParserConfigurationException {
    	if (!xslIsInitialized) {
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		cppunitTransformer = transformerFactory.newTransformer(new StreamSource(this.getClass().getResourceAsStream(CPPUNIT_TO_JUNIT_XSL)));
    		xslIsInitialized = true;
    	}
    }        
    
}
