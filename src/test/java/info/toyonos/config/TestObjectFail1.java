package info.toyonos.config;

@ConfigProperties(prefix = "prefix")
public class TestObjectFail1
{
	@ConfigProperty
	public static final String A_B_S10 = null;
}