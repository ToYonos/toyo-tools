package info.toyonos;


import info.toyonos.config.ConfigProperties;
import info.toyonos.config.ConfigProperty;

@ConfigProperties(prefix = "prefix")
public class TestObjectFail1
{
	@ConfigProperty
	public static final String A_B_S10 = null;
}