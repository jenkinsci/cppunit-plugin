package hudson.plugins.cppunit;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CppUnitArchiverMockitoTest extends AbstractWorkspaceTest {

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
    
    @Test
    public void testNoCppUnitReportsWorkspace() throws Exception {
    	CppUnitArchiver cppunitArchiver = new CppUnitArchiver(listener, junitTargetFilePath, "*.xml",transformer);
        Boolean result = cppunitArchiver.invoke(new File(workspace.toURI()), virtualChannel);
        assertFalse("The archiver did not return false when it could not find any files", result);
    }
    
    private boolean processCppUnitWorkpace(int nbFiles)throws Exception {  
        CppUnitArchiver cppunitArchiver = new CppUnitArchiver(listener, junitTargetFilePath, "*.xml",transformer);
        Boolean result = cppunitArchiver.invoke(new File(workspace.toURI()), virtualChannel);
        verify(transformer, times(nbFiles)).transform(any(FilePath.class), eq(junitTargetFilePath));        
        return result;
    }

    //--- Workspace root
    
    //--XML files
    @Test
    public void testInovke0TransformWithXmlAtWorkpaceRoot() throws Exception {  	
    	boolean result = processCppUnitWorkpace(0);
    	Assert.assertFalse(result);
    }
    @Test
    public void testInovke1TransformWithXmlAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");    
        boolean result = processCppUnitWorkpace(1);
        Assert.assertTrue(result);
    }        
    @Test
    public void testInovke2TransformWithXmlAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");    
        boolean result = processCppUnitWorkpace(2);
        Assert.assertTrue(result);
    }
    @Test
    public void testInovke3TransformWithXmlAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");    
        workspace.createTextTempFile("cppunit-report", ".xml", "content");  
        boolean result = processCppUnitWorkpace(3);
        Assert.assertTrue(result);
    }    
    
    //--Text files
    @Test
    public void testInovke1TransformWithTextAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".txt", "content");  
        boolean result = processCppUnitWorkpace(0);
        Assert.assertFalse(result);
    }    
    @Test
    public void testInovke2TransformWithTextAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".txt", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnitWorkpace(0);
        Assert.assertFalse(result);
    }
    @Test
    public void testInovke3TransformWithTextAtWorkpaceRoot() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".txt", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        workspace.createTextTempFile("cppunit-report", ".txt", "content");
        boolean result = processCppUnitWorkpace(0);
        Assert.assertFalse(result);
    }

    //--XML and Text files
    @Test
    public void testInovke1TransformMixedAtWorkpace() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnitWorkpace(1);
        Assert.assertTrue(result);
    }
    @Test
    public void testInovke2TransformMixedAtWorkpace() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnitWorkpace(2);
        Assert.assertTrue(result);
    }
    @Test
    public void testInovke3TransformMixedAtWorkpace() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnitWorkpace(3);
        Assert.assertTrue(result);
    }    
}
