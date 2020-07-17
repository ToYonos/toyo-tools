package info.toyonos;

import java.time.Duration;

import info.toyonos.config.ConfigProperties;
import info.toyonos.config.ConfigProperty;

@ConfigProperties(prefix = "prefix")
public class TestObjectNotFinal
{
	@ConfigProperty(defaultValue = "test1 default")
	public static String A_B_S1;

	@ConfigProperty
	public static String A_B_S2;

	@ConfigProperty(nullable = true)
	public static String A_B_S3;
	
	@ConfigProperty(nullable = true, trim = true)
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
	
	@ConfigProperty(trim=true)
	public static String[] A_B_A1;
	
	@ConfigProperty
	public static String[] A_B_A2;
	
	@ConfigProperty
	public static Integer[] A_B_A3;
	
	@ConfigProperty
	public static long[] A_B_A4;
	
	@ConfigProperty
	public static Duration A_B_D1 = null;
	
	@ConfigProperty
	public static Duration A_B_D2 = null;
	
	@ConfigProperty
	public static Duration A_B_D3 = null;
	
	@ConfigProperty
	public static Duration A_B_D4 = null;
	
	@ConfigProperty
	public static Duration A_B_D5 = null;
}