package hudson.plugins.cppunit;

import static org.junit.Assert.assertFalse;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CppUnitArchiverTest extends AbstractWorkspaceTest {

    private BuildListener buildListener;
    private Mockery context;
    private Mockery classContext;
    private CppUnitArchiver cppunitArchiver;
    private VirtualChannel virtualChannel;
    private CppUnitTransformer transformer;

    @Before
    public void setUp() throws Exception {
        super.createWorkspace();

        context = new Mockery();
        classContext = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        buildListener = classContext.mock(BuildListener.class);
        virtualChannel = context.mock(VirtualChannel.class);
        transformer = context.mock(CppUnitTransformer.class);
    }

    @After
    public void tearDown() throws Exception {
        super.deleteWorkspace();
    }



    @Test
    public void testTransformOfTwoReports() throws Exception {
    
    
    	cppunitArchiver = new CppUnitArchiver(buildListener, "*.xml",transformer);
        workspace.createTextTempFile("cppunit-report", ".xml", "content");
        workspace.createTextTempFile("cppunit-report", ".xml", "content");

        context.checking(new Expectations() {
            {
                exactly(2).of(transformer).transform(with(any(String.class)), with(any(InputStream.class)), with(any(File.class)));
            }
        });
        
        
       
        classContext.checking(new Expectations() {
            {
                ignoring(buildListener).getLogger();
                will(returnValue(new PrintStream(new ByteArrayOutputStream())));
            }
        });
       
        
        
        cppunitArchiver.invoke(parentFile, virtualChannel);

        context.assertIsSatisfied();
    }



    @Test
    public void testNoCppUnitReports() throws Exception {
        classContext.checking(new Expectations() {
            {
                ignoring(buildListener).getLogger();
                will(returnValue(new PrintStream(new ByteArrayOutputStream())));
                one(buildListener).fatalError(with(any(String.class)));
            }
        });
        cppunitArchiver = new CppUnitArchiver(buildListener, "*.xml",transformer);
        Boolean result = cppunitArchiver.invoke(parentFile, virtualChannel);
        assertFalse("The archiver did not return false when it could not find any files", result);
    }
}
