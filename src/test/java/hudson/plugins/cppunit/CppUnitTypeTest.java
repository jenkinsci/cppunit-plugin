package hudson.plugins.cppunit;

import com.thalesgroup.dtkit.metrics.model.InputMetric;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.anyString;

import java.lang.reflect.Method;

public class CppUnitTypeTest {

    private void backwardCompatibility(String pattern, boolean faildedIfNotNew, boolean deleteJUnitFiles) throws Exception {

        //An old instance can be created and the its getDescriptor method returns null
        CppUnitType cppUnitType = new CppUnitType(pattern, faildedIfNotNew, deleteJUnitFiles);
        Assert.assertNull(cppUnitType.getDescriptor());

        //Test new Object type
        Method readResolveMethod = CppUnitType.class.getMethod("readResolve");
        Object object = readResolveMethod.invoke(cppUnitType);
        Assert.assertTrue(object.getClass() == CppUnitPluginType.class);

        CppUnitPluginType cppUnitPluginType = (CppUnitPluginType) object;
        Assert.assertNotNull(cppUnitPluginType.getDescriptor());

        Assert.assertEquals(cppUnitType.getPattern(), cppUnitPluginType.getPattern());
        Assert.assertEquals(cppUnitType.isDeleteJUnitFiles(), cppUnitPluginType.isDeleteOutputFiles());
        Assert.assertEquals(cppUnitType.isFaildedIfNotNew(), cppUnitPluginType.isFaildedIfNotNew());

        InputMetric inputMetric = cppUnitPluginType.getInputMetric();
        Assert.assertNotNull(inputMetric);
    }

    @Test
    public void test1() throws Exception {
        backwardCompatibility(anyString(), true, true);
    }

    @Test
    public void test2() throws Exception {
        backwardCompatibility(anyString(), true, false);
    }

    @Test
    public void test3() throws Exception {
        backwardCompatibility(anyString(), false, true);
    }

    @Test
    public void test4() throws Exception {
        backwardCompatibility(anyString(), false, false);
    }

}
