package hudson.plugins.cppunit;


import com.thalesgroup.hudson.plugins.xunit.types.XUnitType;
import com.thalesgroup.hudson.plugins.xunit.types.XUnitTypeDescriptor;
import com.thalesgroup.hudson.library.tusarconversion.TestsTools;
import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * CppUnit Type
 *
 * @author Gregory Boissinot
 */
public class CppUnitType extends XUnitType {

    @DataBoundConstructor
    public CppUnitType(String pattern, boolean faildedIfNotNew, boolean deleteJUnitFiles) {
        super(TestsTools.CPPUNIT, pattern, faildedIfNotNew, deleteJUnitFiles);
    }

    public XUnitTypeDescriptor<?> getDescriptor() {
        return new CppUnitType.DescriptorImpl();
    }

    @Extension
    public static class DescriptorImpl extends XUnitTypeDescriptor<CppUnitType> {

        public DescriptorImpl() {
            super(CppUnitType.class);
        }

        @Override
        public String getDisplayName() {
            return TestsTools.CPPUNIT.getLabel();
        }

        public String getId() {
            return "cppunit";
        }
    }
}
