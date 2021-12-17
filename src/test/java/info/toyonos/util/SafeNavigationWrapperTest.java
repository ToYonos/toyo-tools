package info.toyonos.util;

import static info.toyonos.util.SafeNavigationWrapper.$;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SafeNavigationWrapperTest
{
	@Test
	public void parentTest() throws Exception
	{
		Parent a = null;
		Parent b = new Parent("b", a);
		Parent c = new Parent("c", b);
		Parent d = new Parent("d", c);
		
		Assertions.assertNull($(a).$(Parent::getChild).$(Parent::getChild).get());
		Assertions.assertNull($(b).$(Parent::getChild).$(Parent::getChild).get());
		Assertions.assertNull($(c).$(Parent::getChild).$(Parent::getChild).get());
		Assertions.assertNotNull($(d).$(Parent::getChild).$(Parent::getChild).get());
		
		Assertions.assertEquals("c", $(c).$(Parent::getChild).$(Parent::getChild).get(c).getName());
		Assertions.assertEquals("b", $(d).$(Parent::getChild).$(Parent::getChild).get().getName());
		Assertions.assertTrue($(d).$(Parent::getChild).$(Parent::getChild).equals("b"));
		Assertions.assertTrue($(d).$(Parent::getChild).$(Parent::getChild).equalsIgnoreCase("B"));
		Assertions.assertFalse($(d).$(Parent::getChild).$(Parent::getChild).empty());
		Assertions.assertTrue($(d).$(Parent::getChild).$(Parent::getChild).notEmpty());
	}
}