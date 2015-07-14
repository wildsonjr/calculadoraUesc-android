package br.eti.wjr.util;

public class NumericParser {
	public static Double tryParseDouble(String str) {
		return NumericParser.tryParseDouble(str, null);
	}
	
	public static Double tryParseDouble(String str, Double _default) {
		try {
			return Double.valueOf(str);
		} catch (NumberFormatException e) {
			return _default;
		}
	}
	
	
	
	public static Float tryParseFloat(String str) {
		return NumericParser.tryParseFloat(str, null);
	}
	
	public static Float tryParseFloat(String str, Float _default) {
		try {
			return Float.valueOf(str);
		} catch (NumberFormatException e) {
			return _default;
		}
	}
	
	
	
	public static Integer tryParseInt(String str) {
		return NumericParser.tryParseInt(str, null);
	}
	
	public static Integer tryParseInt(String str, Integer _default) {
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			return _default;
		}
	}
}
