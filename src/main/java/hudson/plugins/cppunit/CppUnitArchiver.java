package hudson.plugins.cppunit;

import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import hudson.util.IOException2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.transform.TransformerException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

/**
 * Class responsible for transforming all CppUnit files to JUnit files and then run them all through the JUnit result archiver.
 * 
 * @author Gregory Boissinot
 */
public class CppUnitArchiver implements FilePath.FileCallable<Boolean>, Serializable {

    private static final long serialVersionUID = 1L;

    public static final String JUNIT_REPORTS_PATH = "temporary-junit-reports";

    private CppUnitTransformer reportTransformer;
    
    // Build related objects
    private final BuildListener listener;
    private final String testResultsPattern;

    public CppUnitArchiver(BuildListener listener, String testResults, CppUnitTransformer reportTransformer) throws TransformerException {
        this.listener = listener;
        this.testResultsPattern = testResults;
        this.reportTransformer=reportTransformer;
    }

    /** {@inheritDoc} */
    public Boolean invoke(File ws, VirtualChannel channel) throws IOException {
        
    	boolean retValue = false;
        String[] cppunitFiles = findCppUnitReports(ws);
        
        if (cppunitFiles.length > 0) {
            File junitOutputPath = new File(ws, JUNIT_REPORTS_PATH);
            junitOutputPath.mkdirs();
    
            for (String cppunitFileName : cppunitFiles) {
            	File fileCppunitReport =  new File(ws, cppunitFileName);
                FileInputStream fileStream = new FileInputStream(fileCppunitReport);
                String fileCppunitReportName = fileCppunitReport.getName();
                try {
                	reportTransformer.transform(fileCppunitReportName, fileStream, junitOutputPath);
                } catch (Exception te) {
                    throw new IOException2("Could not transform the CppUnit report.", te);
                }
                finally {
                    fileStream.close();
                }
            }
            
            retValue= true;
        } 

        return retValue;
    }

    /**
     * Return all CppUnit report files
     * 
     * @param parentPath parent
     * @return an array of strings
     */
    private String[] findCppUnitReports(File parentPath)  {
        FileSet fs = Util.createFileSet(parentPath,testResultsPattern);
        DirectoryScanner ds = fs.getDirectoryScanner();

        String[] cppunitFiles = ds.getIncludedFiles();
        if (cppunitFiles.length == 0) {
            // no test result. Most likely a configuration error or fatal problem
            listener.fatalError("No CppUnit test report files were found. Configuration error?");
        }
        return cppunitFiles;
    }
    
    
    
    
}
