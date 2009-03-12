package hudson.plugins.cppunit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public interface CppUnitTransformer {
	
    public void transform(String cppunitFileName, InputStream cppunitFileStream, File junitOutputPath) throws IOException, TransformerException, SAXException, ParserConfigurationException, InterruptedException, IOException ;
}
