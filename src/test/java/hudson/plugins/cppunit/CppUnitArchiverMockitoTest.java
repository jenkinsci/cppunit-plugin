package hudson.plugins.cppunit;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;

import java.io.File;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class CppUnitArchiverMockitoTest extends AbstractWorkspaceTest {

	private BuildListener buildListener;
    private VirtualChannel virtualChannel;
    private CppUnitTransformer transformer;
    
    @Before
    public void setUp() throws Exception {
        super.createWorkspace();        
        virtualChannel=mock(VirtualChannel.class);
        transformer=mock(CppUnitTransformer.class);
        buildListener=mock(BuildListener.class);
    }
    
    @Test
    public void rtestNoCppUnitReports() throws Exception {
    	CppUnitArchiver cppunitArchiver = new CppUnitArchiver(buildListener, "*.xml",transformer);
        Boolean result = cppunitArchiver.invoke(parentFile, virtualChannel);
        assertFalse("The archiver did not return false when it could not find any files", result);
    }
    
    private boolean processCppUnit(int nbFiles)throws Exception {  
        CppUnitArchiver cppunitArchiver = new CppUnitArchiver(buildListener, "*.xml",transformer);
        Boolean result = cppunitArchiver.invoke(parentFile, virtualChannel);
        verify(transformer, times(nbFiles)).transform(anyString(),any(InputStream.class), any(File.class));        
        return result;
    }

    @Test
    public void testInovke0TransformWithXml() throws Exception {  	
    	boolean result = processCppUnit(0);
    	Assert.assertFalse(result);
    }
    @Test
    public void testInovke1TransformWithXml() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");    
        boolean result = processCppUnit(1);
        Assert.assertTrue(result);
    }
        
    @Test
    public void testInovke2TransformWithXml() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");    
        boolean result = processCppUnit(2);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testInovke2TransformWithText() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".txt", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnit(0);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testInovke2TransformMixed() throws Exception {  	
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".txt", "content");    
        boolean result = processCppUnit(1);
        Assert.assertTrue(result);
    }
    
    
}
