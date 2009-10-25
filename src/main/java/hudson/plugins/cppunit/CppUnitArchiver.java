package hudson.plugins.cppunit;

import hudson.FilePath;
import hudson.Util;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.plugins.cppunit.util.CppUnitLogUtil;
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
        this.listener = listener;
        this.junitTargetFilePath = junitTargetFilePath;
        this.pattern = pattern;
        this.reportTransformer = reportTransformer;
    }

    /**
     * {@inheritDoc}
     */
    public Result invoke(File basedir, VirtualChannel channel) throws IOException {

        String[] cppunitFiles = findCppUnitReports(basedir);
        if (cppunitFiles.length == 0) {
            String msg = "No CppUnit test report file(s) were found with the pattern '"
                    + pattern + "' relative to '" + basedir + "'."
                    + "  Did you enter a pattern relative to the correct directory?"
                    + "  Did you generate the XML report(s) for CppUnit?";
            CppUnitLogUtil.log(listener, msg);
            return Result.FAILURE;
        }

        CppUnitLogUtil.log(listener, "Processing " + cppunitFiles.length + " files with the pattern '" + pattern + "'.");

        boolean hasInvalidateFiles = false;
        for (String cppunitFileName : cppunitFiles) {

            File fileCppunitReportFile = new File(basedir, cppunitFileName);

            if (fileCppunitReportFile.length() == 0) {
                //Ignore the empty result file (some reason)
                String msg = "[WARNING] - The file '" + fileCppunitReportFile.getPath() + "' is empty. This file has been ignored.";
                CppUnitLogUtil.log(listener, msg);
                continue;
            }

            if (!validateCppunitResultFile(fileCppunitReportFile)) {

                //register there are unvalid files
                hasInvalidateFiles = true;

                //Ignore unvalid files
                CppUnitLogUtil.log(listener, "[WARNING] - The file '" + fileCppunitReportFile + "' is an invalid file. It has been ignored.");
                continue;
            }


            try {
                FilePath fileCppunitReport = new FilePath(fileCppunitReportFile);
                reportTransformer.transform(fileCppunitReport, junitTargetFilePath);
            }
            catch (Exception te) {
                throw new IOException2("Could not transform the CppUnit report.", te);
            }
        }

        return hasInvalidateFiles ? Result.UNSTABLE : Result.SUCCESS;
    }

    private boolean validateCppunitResultFile(File fileCppunitReportFile)
            throws FactoryConfigurationError {
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.parse(fileCppunitReportFile);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Return all CppUnit report files
     *
     * @param parentPath the parent directory
     * @return an array of strings
     */
    private String[] findCppUnitReports(File parentPath) {
        FileSet fs = Util.createFileSet(parentPath, pattern);
        DirectoryScanner ds = fs.getDirectoryScanner();
        String[] cppunitFiles = ds.getIncludedFiles();
        return cppunitFiles;
    }

}
