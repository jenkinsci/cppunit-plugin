package hudson.plugins.cppunit;

import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.cppunit.util.Messages;
import hudson.remoting.VirtualChannel;
import hudson.util.IOException2;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.transform.TransformerException;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;

/**
 * Class responsible for transforming all CppUnit files to JUnit files and then run them all through the JUnit result archiver.
 * 
 * @author Gregory Boissinot
 */
public class CppUnitArchiver implements FilePath.FileCallable<Result>, Serializable {

    private static final long serialVersionUID = 1L;

    private BuildListener listener;
    private final FilePath junitTargetFilePath;
    private CppUnitTransformer reportTransformer;
    private final String pattern;
    
    public CppUnitArchiver(BuildListener listener, FilePath junitTargetFilePath, String pattern, CppUnitTransformer reportTransformer) throws TransformerException {
    	this.listener=listener;
    	this.junitTargetFilePath=junitTargetFilePath;
        this.pattern = pattern;
        this.reportTransformer=reportTransformer;
    }

    /** {@inheritDoc} */
    public Result invoke(File moduleRoot, VirtualChannel channel) throws IOException {
        
        String[] cppunitFiles = findCppUnitReports(moduleRoot);        
        if (cppunitFiles.length==0){
	            String msg = "No CppUnit test report file(s) were found with the pattern '"
	                + pattern + "' relative to '"+ moduleRoot + "'."
	                + "  Did you enter a pattern relative to the correct directory?"
	                + "  Did you generate the XML report(s) for CppUnit?";		
	            Messages.log(listener,msg);
	            return Result.FAILURE;
        }
                
        Messages.log(listener,"Processing "+cppunitFiles.length+ " files with the pattern '" + pattern + "'.");
        
        boolean hasInvalidateFiles = false;
        for (String cppunitFileName : cppunitFiles) {
        	
        	FilePath fileCppunitReport =  new FilePath(new File(moduleRoot, cppunitFileName));
        	
        	if (validateCppunitResultFile(fileCppunitReport)){        	
	            try {
	            	reportTransformer.transform(fileCppunitReport, junitTargetFilePath);            	
	            } 
	            catch (Exception te) {
	                throw new IOException2("Could not transform the CppUnit report.", te);
	            }
        	}
        	else {
        		hasInvalidateFiles=true;
        	}
        }
        
        return hasInvalidateFiles?Result.UNSTABLE:Result.SUCCESS;
    }

	private boolean validateCppunitResultFile(FilePath fileCppunitReport)
			throws FactoryConfigurationError {
		try{
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.parse(new File(fileCppunitReport.toURI()));
			return true;
		}
		catch (Exception e){
			Messages.log(listener, "[WARNING] - The file '" + fileCppunitReport  + "' is an invalid file. It has been ignored.");
			return false;
		}
	}
    
    /**
     * Return all CppUnit report files
     * 
     * @param parentPath the parent directory
     * @return an array of strings
     */
    private String[] findCppUnitReports(File parentPath)  {
        FileSet fs = Util.createFileSet(parentPath, pattern);
        DirectoryScanner ds = fs.getDirectoryScanner();
        String[] cppunitFiles = ds.getIncludedFiles();
        return cppunitFiles;
    }

}
