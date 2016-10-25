package info.toyonos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import info.toyonos.config.ConfigProperties;
import info.toyonos.config.ConfigProperty;
import info.toyonos.config.ConfigPropertyBinder;
import info.toyonos.config.ConfigPropertyException;
import info.toyonos.config.MissingConfigPropertyException;
import info.toyonos.config.adapter.PropertiesAdapter;
import info.toyonos.config.adapter.SimpleConfigPropertyAdapter;

@RunWith(DataProviderRunner.class)
@SuppressWarnings("unused")
public class ConfigPropertyBinderTest
{
	@DataProvider
	public static Object[][] extractPropertiesTestDataProvider()
	{
		return new Object[][]
		{
			{ TestObject.class },
			{ TestObjectNotFinal.class },
		};  
	}

	@UseDataProvider("extractPropertiesTestDataProvider")
	@Test
	public void extractPropertiesTest(Class<?> target) throws Exception
	{		
		Map<String, String> config = new HashMap<>();
		config.put("prefix.a.b.s1", "test1");
		config.put("prefix.a.b.s2", "test2");
		config.put("prefix.a.b.s4", "test4");
		config.put("prefix.a.b.s7", "test7 dummy");
		config.put("prefix2.a.b.s7", "test7");
		
		config.put("prefix.a.b.l1", "42");
		config.put("prefix.a.b.i1", "5");
		config.put("prefix.a.b.b1", "true");
		config.put("prefix.a.b.a1", "a,b,c");
		config.put("prefix.a.b.a2", "d; e; f ");
		config.put("prefix.a.b.a3", "1,2,3");
		config.put("prefix.a.b.a4", "4,5,6");

		Assert.assertNull(target.getField("A_B_S1").get(null));
		Assert.assertNull(target.getField("A_B_S2").get(null));
		Assert.assertNull(target.getField("A_B_S3").get(null));
		Assert.assertNull(target.getField("A_B_S4").get(null));
		Assert.assertNull(target.getField("A_B_S5").get(null));
		Assert.assertNull(target.getField("A_B_S6").get(null));
		Assert.assertNull(target.getField("A_B_L1").get(null));
		Assert.assertNull(target.getField("A_B_I1").get(null));
		Assert.assertNull(target.getField("A_B_B1").get(null));
		Assert.assertNull(target.getField("A_B_A1").get(null));
		Assert.assertNull(target.getField("A_B_A2").get(null));
		Assert.assertNull(target.getField("A_B_A3").get(null));
		Assert.assertNull(target.getField("A_B_A4").get(null));

		ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(config), target).bind();

		Assert.assertEquals("test1", target.getField("A_B_S1").get(null));
		Assert.assertEquals("test2", target.getField("A_B_S2").get(null));
		Assert.assertNull(target.getField("A_B_S3").get(null));
		Assert.assertEquals("test4", target.getField("A_B_S4").get(null));
		Assert.assertEquals("test5 default", target.getField("A_B_S5").get(null));
		Assert.assertEquals("test6 default", target.getField("A_B_S6").get(null));
		Assert.assertEquals("test7", target.getField("A_B_S7").get(null));
		Assert.assertNull(target.getField("A_B_S8").get(null));
		
		Assert.assertEquals(Long.valueOf(42L), target.getField("A_B_L1").get(null));
		Assert.assertEquals(Integer.valueOf(5), target.getField("A_B_I1").get(null));
		Assert.assertTrue(Boolean.parseBoolean(target.getField("A_B_B1").get(null).toString()));
		Assert.assertArrayEquals(new String[]{"a", "b", "c"}, (String[]) target.getField("A_B_A1").get(null));
		Assert.assertArrayEquals(new String[]{"d", "e", "f"}, (String[]) target.getField("A_B_A2").get(null));
		Assert.assertArrayEquals(new Integer[]{1, 2, 3}, (Integer[]) target.getField("A_B_A3").get(null));
		Assert.assertArrayEquals(new long[]{4L, 5L, 6L}, (long[]) target.getField("A_B_A4").get(null));
	}
	
	@Test(expected=MissingConfigPropertyException.class)
	public void extractPropertiesMissingConfigPropertyExceptionTest()
	{
		ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(new HashMap<String, String>()), TestObjectFail1.class).bind();
	}
	
	@Test(expected=ConfigPropertyException.class)
	public void extractPropertiesConfigPropertyExceptionNoConfigPropertyAdapterClassTest()
	{
		 ConfigPropertyBinder.create(TestObjectFail2.class).bind();
	}
	
	@Test(expected=ConfigPropertyException.class)
	public void extractPropertiesConfigPropertyExceptionNoConfigPropertiesAnnotationTest()
	{
		ConfigPropertyBinder.create(TestObjectFail3.class).bind();
	}
	
	@Test(expected=IllegalStateException.class)
	public void extractPropertiesIllegalStateExceptionTest()
	{
		ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(new HashMap<String, String>()), TestObjectFail4.class).bind();
	}
	
	@ConfigProperties(prefix = "prefix")
	private static class TestObject
	{
		@ConfigProperty(defaultValue = "test1 default")
		public static final String A_B_S1 = null;

		@ConfigProperty
		public static final String A_B_S2 = null;

		@ConfigProperty(nullable = true)
		public static final String A_B_S3 = null;
		
		@ConfigProperty(nullable = true)
		public static final String A_B_S4 = null;
		
		@ConfigProperty(nullable = true, defaultValue="test5 default")
		public static final String A_B_S5 = null;

		@ConfigProperty(defaultValue = "test6 default")
		public static final String A_B_S6 = null;

		@ConfigProperty(prefix="prefix2")
		public static final String A_B_S7 = null;

		public static final String A_B_S8 = null;

		@ConfigProperty
		public static final Long A_B_L1 = null;
		
		@ConfigProperty
		public static final Integer A_B_I1 = null;
		
		@ConfigProperty
		public static final Boolean A_B_B1 = null;
		
		@ConfigProperty
		public static final String[] A_B_A1 = null;
		
		@ConfigProperty(separator="\\s*;\\s*", trim=true)
		public static final String[] A_B_A2 = null;
		
		@ConfigProperty
		public static final Integer[] A_B_A3 = null;
		
		@ConfigProperty
		public static final long[] A_B_A4 = null;
	}

	@ConfigProperties(prefix = "prefix")
	private static class TestObjectNotFinal
	{
		@ConfigProperty(defaultValue = "test1 default")
		public static String A_B_S1;

		@ConfigProperty
		public static String A_B_S2;

		@ConfigProperty(nullable = true)
		public static String A_B_S3;
		
		@ConfigProperty(nullable = true)
		public static String A_B_S4;
		
		@ConfigProperty(nullable = true, defaultValue="test5 default")
		public static String A_B_S5;

		@ConfigProperty(defaultValue = "test6 default")
		public static String A_B_S6;

		@ConfigProperty(prefix="prefix2")
		public static String A_B_S7;

		public static String A_B_S8;

		@ConfigProperty
		public static Long A_B_L1;
		
		@ConfigProperty
		public static Integer A_B_I1;
		
		@ConfigProperty
		public static Boolean A_B_B1;
		
		@ConfigProperty
		public static String[] A_B_A1;
		
		@ConfigProperty(separator="\\s*;\\s*", trim=true)
		public static String[] A_B_A2;
		
		@ConfigProperty
		public static Integer[] A_B_A3;
		
		@ConfigProperty
		public static long[] A_B_A4;
	}
	
	@ConfigProperties(prefix = "prefix")
	private static class TestObjectFail1
	{
		@ConfigProperty
		public static final String A_B_S1 = null;
	}
	
	@ConfigProperties(prefix = "prefix")
	private static class TestObjectFail2
	{
		@ConfigProperty
		public static final String A_B_S1 = null;
	}

	private static class TestObjectFail3
	{
		@ConfigProperty
		public static final String A_B_S1 = null;
	}
	
	@ConfigProperties(prefix = "prefix")
	private static class TestObjectFail4
	{
		@ConfigProperty
		public static final String A_B_S1 = "test";
	}
}
