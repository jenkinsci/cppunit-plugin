package hudson.plugins.cppunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class XUnitXSLUtil {


    public static String readXmlAsString(InputStream input)
            throws IOException {
        String xmlString = "";

        if (input == null) {
            throw new IOException("The input stream object is null.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        while (line != null) {
            xmlString += line + "\n";
            line = reader.readLine();
        }
        reader.close();

        return xmlString;
    }


}
