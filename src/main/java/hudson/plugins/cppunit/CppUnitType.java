package hudson.plugins.cppunit;


import com.thalesgroup.dtkit.metrics.hudson.api.descriptor.TestTypeDescriptor;
import com.thalesgroup.hudson.plugins.xunit.types.XUnitType;

/**
 * CppUnit Type
 *
 * @author Gregory Boissinot
 */
@SuppressWarnings("unused")
public class CppUnitType extends XUnitType {

    public CppUnitType(String pattern, boolean faildedIfNotNew, boolean deleteJUnitFiles) {
        super(pattern, faildedIfNotNew, deleteJUnitFiles);
    }

    public TestTypeDescriptor getDescriptor() {
        return null;
    }

    /**
     * Call at Hudson startup for backward compatibility
     *
     * @return an new hudson object
     */
    public Object readResolve() {
        return new CppUnitPluginType(this.getPattern(), this.isFaildedIfNotNew(), this.isDeleteJUnitFiles());
    }


}
