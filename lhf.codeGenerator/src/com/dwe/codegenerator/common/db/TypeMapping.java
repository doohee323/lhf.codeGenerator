package com.dwe.codegenerator.common.db;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.dwe.codegenerator.common.util.StringUtil;

public final class TypeMapping {

	private Properties props = new Properties();

	private static TypeMapping xfb;

	private static Map<String, Object> typeMap;

	private TypeMapping() {
		try {
			props.load(ClassLoader.getSystemClassLoader().getResourceAsStream(
					"mapping.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     */
	public static synchronized TypeMapping getInstance() {
		if (xfb == null) {
			xfb = new TypeMapping();
			typeMap = new HashMap<String, Object>();
		}
		return xfb;
	}

	public String getType(String type, String scale) throws Exception {
		if (type == null) {
			throw new Exception("Not Found Mapping Type : " + type);
		}

		if (typeMap.get(type) != null) {
			return StringUtil.getText(typeMap.get(type));
		}

		Set keySet = props.keySet();
		Iterator iter = keySet.iterator();
		while (iter.hasNext()) {
			String col = (String) iter.next();
			if (col.equals(type)) {
				String strType = getTypeProp(
						StringUtil.getText(props.get(col)), scale);
				typeMap.put(col, strType);
				return strType;
			}
		}

		return "";
	}

	public String getTypeProp(String type, String scale) throws Exception {
		String[] types = type.split("/");
		int lengthTypes = types.length;
		if (lengthTypes == 1) {
			return type.trim();
		}

		int nScale = Integer.parseInt(scale);

		String[] typesArray = null;

		for (int i = 0; i < lengthTypes; i++) {
			typesArray = types[i].split(",");
			if (typesArray.length == 5) {
				String strType = typesArray[0].trim();
				String strMinDecimalDigits = typesArray[1].trim();
				String strMaxDecimalDigits = typesArray[2].trim();
				String strMinColumnSize = typesArray[3].trim();
				String strMaxColumnSize = typesArray[4].trim();

				if (("-".equals(strMinDecimalDigits) || nScale >= Integer
						.parseInt(strMinDecimalDigits))

						&& ("-".equals(strMaxDecimalDigits) || nScale <= Integer
								.parseInt(strMaxDecimalDigits))

						&& ("-".equals(strMinColumnSize) || nScale >= Integer
								.parseInt(strMinColumnSize))

						&& ("-".equals(strMaxColumnSize) || nScale <= Integer
								.parseInt(strMaxColumnSize))) {
					return strType;
				}
			} else {
				throw new Exception("Invalid Definition maping.properties");
			}
		}
		return "";
	}
}
