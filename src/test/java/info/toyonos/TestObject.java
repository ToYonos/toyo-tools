package info.toyonos;

import java.time.Duration;

import info.toyonos.config.ConfigProperties;
import info.toyonos.config.ConfigProperty;


@ConfigProperties(prefix = "prefix")
public class TestObject
{
	@ConfigProperty(defaultValue = "test1 default")
	public static final String A_B_S1 = null;

	@ConfigProperty
	public static final String A_B_S2 = null;

	@ConfigProperty(nullable = true)
	public static final String A_B_S3 = null;
	
	@ConfigProperty(nullable = true, trim = true)
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
	
	@ConfigProperty(trim=true)
	public static final String[] A_B_A1 = null;
	
	@ConfigProperty
	public static final String[] A_B_A2 = null;
	
	@ConfigProperty
	public static final Integer[] A_B_A3 = null;
	
	@ConfigProperty
	public static final long[] A_B_A4 = null;
	
	@ConfigProperty
	public static final Duration A_B_D1 = null;
	
	@ConfigProperty
	public static final Duration A_B_D2 = null;
	
	@ConfigProperty
	public static final Duration A_B_D3 = null;
	
	@ConfigProperty
	public static final Duration A_B_D4 = null;
	
	@ConfigProperty
	public static final Duration A_B_D5 = null;
}