package info.toyonos.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import info.toyonos.config.adapter.SimpleConfigPropertyAdapter;

@TestMethodOrder(OrderAnnotation.class)
public class ConfigPropertyBinderTest
{	
	@Test
    @Order(1)
	public void unbindedTest() throws Exception
	{
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S2"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S3"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S4"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S5"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S6"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S7"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_S8"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_L1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_I1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_B1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_A1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_A2"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_A3"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_A4"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_D1"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_D2"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_D3"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_D4"));
		Assertions.assertNull(getFieldValue(TestObject.class, "A_B_D5"));
	}

	@ParameterizedTest
	@ValueSource(classes = {TestObject.class, TestObjectNotFinal.class})
	@Order(2)
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
		config.put("prefix.a.b.a1", "   a  ;b ;c ");
		config.put("prefix.a.b.a2", "1;2;3");
		config.put("prefix.a.b.a3", "1;2;3");
		config.put("prefix.a.b.a4", "4;5;6");
		config.put("prefix.a.b.d1", "5ms");
		config.put("prefix.a.b.d2", "5s");
		config.put("prefix.a.b.d3", "5m");
		config.put("prefix.a.b.d4", "5h");
		config.put("prefix.a.b.d5", "5d");

		Assertions.assertNull(target.getField("A_B_S1").get(null));
		Assertions.assertNull(target.getField("A_B_S2").get(null));
		Assertions.assertNull(target.getField("A_B_S3").get(null));
		Assertions.assertNull(target.getField("A_B_S4").get(null));
		Assertions.assertNull(target.getField("A_B_S5").get(null));
		Assertions.assertNull(target.getField("A_B_S6").get(null));
		Assertions.assertNull(target.getField("A_B_L1").get(null));
		Assertions.assertNull(target.getField("A_B_I1").get(null));
		Assertions.assertNull(target.getField("A_B_B1").get(null));
		Assertions.assertNull(target.getField("A_B_A1").get(null));
		Assertions.assertNull(target.getField("A_B_A2").get(null));
		Assertions.assertNull(target.getField("A_B_A3").get(null));
		Assertions.assertNull(target.getField("A_B_A4").get(null));

		ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(config), target).bind();
		
		Assertions.assertEquals("test1", getFieldValue(target, "A_B_S1"));
		Assertions.assertEquals("test2", getFieldValue(target, "A_B_S2"));
		Assertions.assertNull(getFieldValue(target, "A_B_S3"));
		Assertions.assertEquals("test4", getFieldValue(target, "A_B_S4"));
		Assertions.assertEquals("test5 default", getFieldValue(target, "A_B_S5"));
		Assertions.assertEquals("test6 default", getFieldValue(target, "A_B_S6"));
		Assertions.assertEquals("test7", getFieldValue(target, "A_B_S7"));
		Assertions.assertNull(getFieldValue(target, "A_B_S8"));
		
		Assertions.assertEquals(Long.valueOf(42L), getFieldValue(target, "A_B_L1"));
		Assertions.assertEquals(Integer.valueOf(5), getFieldValue(target, "A_B_I1"));
		Assertions.assertEquals(Boolean.TRUE, getFieldValue(target, "A_B_B1"));
		Assertions.assertArrayEquals(new String[]{"a", "b", "c"}, (String[]) getFieldValue(target, "A_B_A1"));
		Assertions.assertArrayEquals(new String[]{"1", "2", "3"}, (String[]) getFieldValue(target, "A_B_A2"));
		Assertions.assertArrayEquals(new Integer[]{1, 2, 3}, (Integer[]) getFieldValue(target, "A_B_A3"));
		Assertions.assertArrayEquals(new long[]{4L, 5L, 6L}, (long[]) getFieldValue(target, "A_B_A4"));
		Assertions.assertEquals(Duration.ofMillis(5), getFieldValue(target, "A_B_D1"));
		Assertions.assertEquals(Duration.ofMillis(5000), getFieldValue(target, "A_B_D2"));
		Assertions.assertEquals(Duration.ofMillis(300_000), getFieldValue(target, "A_B_D3"));
		Assertions.assertEquals(Duration.ofMillis(18_000_000), getFieldValue(target, "A_B_D4"));
		Assertions.assertEquals(Duration.ofMillis(432_000_000), getFieldValue(target, "A_B_D5"));
	}
	
	@Test
	public void extractPropertiesMissingConfigPropertyExceptionTest()
	{
		Assertions.assertThrows(
			MissingConfigPropertyException.class,
			() -> ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(new HashMap<String, String>()), TestObjectFail1.class).bind()
		);
	}
	
	@Test
	public void extractPropertiesConfigPropertyExceptionNoConfigPropertyAdapterClassTest()
	{
		Assertions.assertThrows(
			ConfigPropertyException.class,
			() -> ConfigPropertyBinder.create(TestObjectFail2.class).bind()
		);
	}
	
	@Test
	public void extractPropertiesConfigPropertyExceptionNoConfigPropertiesAnnotationTest()
	{
		Assertions.assertThrows(
			ConfigPropertyException.class,
			() -> ConfigPropertyBinder.create(TestObjectFail3.class).bind()
		);
	}
	
	@Test
	public void extractPropertiesIllegalStateExceptionTest()
	{
		Assertions.assertThrows(
			IllegalStateException.class,
			() -> ConfigPropertyBinder.create(new SimpleConfigPropertyAdapter(new HashMap<String, String>()), TestObjectFail4.class).bind()
		);
	}
	
	private Object getFieldValue(Class<?> target, String field) throws Exception
	{
		return target.getField(field).get(null);
	}
}
