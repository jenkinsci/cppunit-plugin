package hudson.plugins.cppunit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.remoting.VirtualChannel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CppUnitResultMockitoTest extends AbstractWorkspaceTest {

    private VirtualChannel virtualChannel;
    private BuildListener listener;
    private CppUnitTransformer transformer;
    private FilePath junitTargetFilePath;
	
    @Before
    public void setUp() throws Exception {
        createWorkspace();        
        virtualChannel=mock(VirtualChannel.class);
        transformer=mock(CppUnitTransformer.class);
        listener=mock(BuildListener.class);
        when(listener.getLogger()).thenReturn(new PrintStream(new ByteArrayOutputStream())); 
		File parentTargetJunitFile = Util.createTempDir();
		junitTargetFilePath = new FilePath(parentTargetJunitFile);
        if (junitTargetFilePath.exists()) {
        	junitTargetFilePath.deleteRecursive();
        }
        junitTargetFilePath.mkdirs();  
    }
    
    @After
    public void tearDown() throws Exception{
    	if (junitTargetFilePath!=null){
    		junitTargetFilePath.deleteRecursive();
    	}
    }   
    
    private Result processCppUnitWorkpace()throws Exception {  
        CppUnitArchiver cppunitArchiver = new CppUnitArchiver(listener, junitTargetFilePath, "*.xml",transformer);
        Result result = cppunitArchiver.invoke(new File(workspace.toURI()), virtualChannel);
        return result;
    }
    
    private void createTempFileValidContent(boolean validFile, String extension) throws Exception { 
    	String resourceFile = validFile?"validation/validResultFile.xml":"validation/unvalidResultFile.xml";
    	InputStream is = this.getClass().getResourceAsStream(resourceFile);
    	StringBuffer sb= new StringBuffer();
    	int c;
    	while ((c=is.read())!=-1){
    		sb.append((char)c);
    	}
    	workspace.createTextTempFile("cppunit-report", extension, sb.toString());
    }
    
    
    @Test
    public void testInovke1ValidFile() throws Exception {  	
    	createTempFileValidContent(true, ".xml");  
        Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With one valid file, the result must be SUCCESS", Result.SUCCESS, actualResult);        
    }
    
    @Test
    public void testInovke2ValidFile() throws Exception {  	
    	createTempFileValidContent(true, ".xml");  
    	createTempFileValidContent(true, ".xml");  
        Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With two valid file, the result must be SUCCESS", Result.SUCCESS, actualResult);        
    }

    @Test
    public void testInovke3ValidFile() throws Exception {  	
    	createTempFileValidContent(true, ".xml");  
    	createTempFileValidContent(true, ".xml");  
    	createTempFileValidContent(true, ".xml");  
    	Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With three valid file, the result must be SUCCESS", Result.SUCCESS, actualResult);        
    }

    @Test
    public void testInovkeNoneFile() throws Exception {  	  
        Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With no file, the result must be FAILURE", Result.FAILURE, actualResult);        
    }    
    
    @Test
    public void testInovke1UnValidFile() throws Exception {  	
    	createTempFileValidContent(false, ".xml");  
        Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With one invalid file, the result must be UNSTABLE", Result.UNSTABLE, actualResult);        
    }
    
    @Test
    public void testInovke2UnValidFile() throws Exception {  	
    	createTempFileValidContent(false, ".xml");  
    	createTempFileValidContent(false, ".xml");  
        Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With two invalid file, the result must be UNSTABLE", Result.UNSTABLE, actualResult);        
    }

    @Test
    public void testInovke3UnValidFile() throws Exception {  	
    	createTempFileValidContent(false, ".xml");  
    	createTempFileValidContent(false, ".xml");  
    	createTempFileValidContent(false, ".xml");  
    	Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With three invalid file, the result must be UNSTABLE", Result.UNSTABLE, actualResult);        
    }
    
    @Test
    public void testInovkeMixFiles() throws Exception {  	
    	createTempFileValidContent(true, ".xml");  
    	createTempFileValidContent(false, ".xml");  
    	createTempFileValidContent(false, ".xml");  
    	Result actualResult = processCppUnitWorkpace();
    	Assert.assertEquals("With valid and invalid files, the result must be UNSTABLE", Result.UNSTABLE, actualResult);        
    }
}
