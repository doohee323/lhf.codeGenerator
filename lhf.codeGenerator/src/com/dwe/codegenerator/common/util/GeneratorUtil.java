package com.dwe.codegenerator.common.util;

public class GeneratorUtil {

	public static String firstToUpperCase(String str) {
		String post = str.substring(1, str.length());
		String first = ("" + str.charAt(0)).toUpperCase();
		return first + post;
	}

	public static String carmelNaming(String str) {
		return carmelNaming(str, "_");
	}

	public static String carmelNaming(String str, String pattern) {
		String ArrString[] = str.toLowerCase().split(pattern);
		String returnString = "";

		for (int i = 0; i < ArrString.length; i++) {
			if (i > 0)
				ArrString[i] = firstToUpperCase(ArrString[i]);

			returnString += ArrString[i];
		}
		return returnString;
	}

	/**
	 * DB컬럼명 혹은 변경하고자하는 베이스명을 이용하여 해당하는 set method의 명을 리턴한다.
	 * 
	 * @param columnName
	 *            데이터베이스의 컬럼명 혹은 변환하고자하는 베이스명
	 * @return String 컬럼명을 기준으로 해당 컬럼의 set method명
	 */
	public static String getSetMethodName(String columnName) {
		String str = "";
		str = carmelNaming(columnName, "_");
		return "set" + str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	/**
	 * DB컬럼명 혹은 변경하고자하는 베이스명을 이용하여 해당하는 get method의 명을 리턴한다.
	 * 
	 * @param columnName
	 *            데이터베이스의 컬럼명 혹은 변환하고자하는 베이스명
	 * @return String 컬럼명을 기준으로 해당 컬럼의 get method명
	 */
	public static String getGetMethodName(String columnName) {
		String str = "";
		str = carmelNaming(columnName, "_");
		return "get" + str.substring(0, 1).toUpperCase()
				+ str.substring(1, str.length());
	}

	public static String buildControllerUrl(String packageName, String systemId) {
		// /ptp1/ptp101/ptp10101
		String strArry[] = packageName.split("\\.");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append('/');
		stringBuilder.append(strArry[3]);
		stringBuilder.append('/');
		stringBuilder.append(strArry[4]);
		stringBuilder.append('/');
		stringBuilder.append(systemId.toLowerCase());
		stringBuilder.append("/*");
		return stringBuilder.toString();
	}

}