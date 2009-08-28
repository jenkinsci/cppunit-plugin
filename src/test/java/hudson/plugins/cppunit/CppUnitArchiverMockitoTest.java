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
    
    @Test
    public void testNoCppUnitReportsWorkspace() throws Exception {
    	CppUnitArchiver cppunitArchiver = new CppUnitArchiver(listener, junitTargetFilePath, "*.xml",transformer);
        Result result = cppunitArchiver.invoke(new File(workspace.toURI()), virtualChannel);
        Assert.assertEquals("The archiver did not return false when it could not find any files",Result.FAILURE, result);
    }
    
    private void processCppUnitWorkpace(int nbFiles)throws Exception {  
        CppUnitArchiver cppunitArchiver = new CppUnitArchiver(listener, junitTargetFilePath, "*.xml",transformer);
        Result result = cppunitArchiver.invoke(new File(workspace.toURI()), virtualChannel);
        verify(transformer, times(nbFiles)).transform(any(FilePath.class), eq(junitTargetFilePath));        
    }

    //--- Workspace root
    
    //--XML files
    @Test
    public void testInovke0TransformWithXmlAtWorkpaceRoot() throws Exception {  	
    	processCppUnitWorkpace(0);    
    }
    
    private void createTempFileValidContent(String extension) throws Exception {      	
    	InputStream is = this.getClass().getResourceAsStream("validation/validResultFile.xml");
    	StringBuffer sb= new StringBuffer();
    	int c;
    	while ((c=is.read())!=-1){
    		sb.append((char)c);
    	}
    	workspace.createTextTempFile("cppunit-report", extension, sb.toString());
    }
    
    @Test
    public void testInovke1TransformWithXmlAtWorkpaceRoot() throws Exception {  	        
    	createTempFileValidContent(".xml");
        processCppUnitWorkpace(1);       
    }
    
    @Test
    public void testInovke2TransformWithXmlAtWorkpaceRoot() throws Exception {  	
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".xml");  
        processCppUnitWorkpace(2);        
    }
    
    @Test
    public void testInovke3TransformWithXmlAtWorkpaceRoot() throws Exception {  	
    	createTempFileValidContent(".xml");
        createTempFileValidContent(".xml");    
        createTempFileValidContent(".xml");  
        processCppUnitWorkpace(3);
    }    
    
    //--Text files
    @Test
    public void testInovke1TransformWithTextAtWorkpaceRoot() throws Exception {  	
    	createTempFileValidContent(".txt");
        processCppUnitWorkpace(0);
    }    
    
    @Test
    public void testInovke2TransformWithTextAtWorkpaceRoot() throws Exception {  	
    	createTempFileValidContent(".txt");
    	createTempFileValidContent(".txt");  
        processCppUnitWorkpace(0);
    }
    @Test
    public void testInovke3TransformWithTextAtWorkpaceRoot() throws Exception {  	
    	createTempFileValidContent(".txt");
    	createTempFileValidContent(".txt");
    	createTempFileValidContent(".txt");
        processCppUnitWorkpace(0);
    }

    //--XML and Text files
    @Test
    public void testInovke1TransformMixedAtWorkpace() throws Exception {  	
    	createTempFileValidContent(".xml");
        createTempFileValidContent(".txt");  
        processCppUnitWorkpace(1);
    }
    @Test
    public void testInovke2TransformMixedAtWorkpace() throws Exception {  	
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".txt");    
        processCppUnitWorkpace(2);

    }
    @Test
    public void testInovke3TransformMixedAtWorkpace() throws Exception {  	
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".xml");
    	createTempFileValidContent(".txt");    
        processCppUnitWorkpace(3);
    }    
}
