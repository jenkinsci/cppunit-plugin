package hudson.plugins.cppunit;


import com.thalesgroup.dtkit.metrics.hudson.api.descriptor.TestTypeDescriptor;
import com.thalesgroup.dtkit.metrics.hudson.api.type.TestType;
import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * CppUnit Type
 *
 * @author Gregory Boissinot
 */

public class CppUnitPluginType extends TestType {

    @DataBoundConstructor
    public CppUnitPluginType(String pattern, boolean faildedIfNotNew, boolean deleteOutputFiles) {
        super(pattern, faildedIfNotNew, deleteOutputFiles);
    }

    public TestTypeDescriptor<?> getDescriptor() {
        return new CppUnitPluginType.DescriptorImpl();
    }

    @Extension
    public static class DescriptorImpl extends TestTypeDescriptor<CppUnitPluginType> {

        public DescriptorImpl() {
            super(CppUnitPluginType.class, CppUnitInputMetric.class);
        }

        public String getId() {
            return CppUnitPluginType.class.getCanonicalName();
        }

    }

}
