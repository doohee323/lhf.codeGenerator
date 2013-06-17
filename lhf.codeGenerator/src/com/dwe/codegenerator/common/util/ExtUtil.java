package com.dwe.codegenerator.common.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 
 * ---------------------------------------------------------------
 * </pre>
 * 
 * @version 1.0
 */
public class ExtUtil {

	public static boolean bRuning = false;

	public static String step = "1";

	public static String debugString = "";

	public static FileOutputStream fos4 = null;

	public static BufferedOutputStream bos4 = null;

	public static PrintStream out4 = null;

	public static PrintWriter appender4 = null;

	public static List<String> dutySysCdList = new ArrayList<String>();

	/**
     */
	public static String mklog(String str) {
		try {
			str = StringUtil.replace(str, "{", "");
			str = StringUtil.replace(str, "}", "");
			str = StringUtil.replace(str, " ", "");
			// str = StringUtil.replace(str, "srcCls=", "");
			// str = StringUtil.replace(str, "serviceId=", "");
			// str = StringUtil.replace(str, "filePath=", "");
			// str = StringUtil.replace(str, "reqUri=", "");
			// str = StringUtil.replace(str, "dutySysCd=", "");
			// str = StringUtil.replace(str, "menuCd=", "");
			// str = StringUtil.replace(str, "tableId=", "");
			// str = StringUtil.replace(str, "queryId=", "");
			// str = StringUtil.replace(str, "procedureId=", "");
			// str = StringUtil.replace(str, "CRUD=", "");
			// str = StringUtil.replace(str, "sysCd=", "");
			// str = StringUtil.replace(str, "analy1=", "");
			// str = StringUtil.replace(str, "analy2=", "");
			// str = StringUtil.replace(str, "analy3=", "");
			// str = StringUtil.replace(str, "analy4=", "");
			str = StringUtil.replace(str, "oggCd=", "");
			str = StringUtil.replace(str, "oggTime=", "");
			if (str.indexOf(debugString) > -1) {
				log("");
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		str = step + "," + str;
		return str;
	}

	public static String convertJDoc(String strStr) {
		String strTmp = "";
		try {
			strStr = StringUtil.replace(strStr, "* 설명", "* @desc");
			strStr = StringUtil.replace(strStr, "* 파라미터", "* @param");
			strStr = StringUtil.replace(strStr, "* 리턴값", "* @return");
			strStr = StringUtil.replace(strStr, "* 작성자", "* @author");
			strStr = StringUtil.replace(strStr, "* 작성일", "* @since");
			String arryStr[] = strStr.split("\n");
			for (int i = 0; i < arryStr.length; i++) {
				if (!StringUtil.LTrim(arryStr[i]).startsWith("*")
						&& !StringUtil.LTrim(arryStr[i]).startsWith("/*")
						&& !StringUtil.LTrim(arryStr[i]).startsWith("//")
						&& !StringUtil.LRTrim(arryStr[i]).equals("")) {
					strTmp += StringUtil.LRTrim(arryStr[i]) + "\n";
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return strTmp;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public static String setSqlParam(String sql) {
		try {
			while (sql.indexOf("#") > -1) {
				String tmp2 = sql.substring(0, sql.indexOf("#"));
				String tmp3 = StringUtil.LTrim(sql.substring(
						sql.indexOf("#", sql.indexOf("#") + 1) + 1,
						sql.length()));
				if (tmp3.startsWith("->")) {
					if (tmp3.indexOf("\n") > -1) {
						tmp3 = tmp3.substring(tmp3.indexOf("\n") + 1,
								tmp3.length());
					}
				} else {
					sql = tmp2 + "\"\"" + tmp3;
				}
			}
			sql = StringUtil.LTrim(StringUtil.replace(sql, "\"", "'"));
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return sql;
	}

	public static String trimJava(String strStr) {
		String strTmp = "";
		String arryStr[] = strStr.split("\n");
		boolean bChk = false;
		for (int i = 0; i < arryStr.length; i++) {
			if (!StringUtil.LTrim(arryStr[i]).startsWith("*")
					// && !StringUtil.LTrim(arryStr[i]).startsWith("@")
					&& !StringUtil.LTrim(arryStr[i]).startsWith("/*")
					&& !StringUtil.LTrim(arryStr[i]).startsWith("//")
					&& !StringUtil.LTrim(arryStr[i]).startsWith("import ")
					&& !StringUtil.LRTrim(arryStr[i]).equals("")
					&& !StringUtil.LTrim(arryStr[i]).startsWith("package ")) {
				if (arryStr[i].indexOf(" class ") > -1
						&& !arryStr[i].trim().endsWith("{")) {
					strTmp += StringUtil.LRTrim(arryStr[i]) + " {\n";
					bChk = true;
				} else if (bChk == true
						&& StringUtil.LRTrim(arryStr[i]).endsWith("{")) {
					bChk = false;
				} else if (bChk == false) {
					strTmp += StringUtil.LRTrim(arryStr[i]) + "\n";
				}
			}
		}
		return strTmp;
	}

	public static String trimXml(String strStr) {
		String strTmp = "";
		try {
			String arryStr[] = strStr.split("\n");
			boolean bChk = false;
			for (int i = 0; i < arryStr.length; i++) {
				String tmp = StringUtil.LTrim(arryStr[i]);
				if (!tmp.equals("")) {
					if (tmp.startsWith("<!--") && tmp.indexOf("-->") > -1) {
					} else if (!tmp.startsWith("<!--")
							&& tmp.indexOf("<!--") > -1) {
						strTmp += tmp.substring(0, tmp.indexOf("<!--")) + "\n";
					} else if (bChk == true && tmp.indexOf("-->") > -1) {
						bChk = false;
					} else {
						if (bChk == false) {
							if (tmp.startsWith("<!--")) {
								bChk = true;
							} else {
								strTmp += tmp + "\n";
							}
						}
					}
				}
			}
			arryStr = strTmp.split("\n");
			strTmp = "";
			for (int i = 0; i < arryStr.length; i++) {
				if (!StringUtil.LTrim(arryStr[i]).startsWith("--")
						&& !StringUtil.LTrim(arryStr[i]).startsWith("->")
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isNotEmpty") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isEmpty") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<dynamic") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<iterate") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<include") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isEqual") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isNotEqual") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isNotNull") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("<isNull") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isNotEmpty") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isEmpty") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</dynamic") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</iterate") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</include") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isEqual") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isNotEqual") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isNotNull") == -1
						&& StringUtil.LTrim(arryStr[i]).indexOf("</isNull") == -1) {
					if (StringUtil.LTrim(arryStr[i]).indexOf("--") > -1
							&& StringUtil.LTrim(arryStr[i]).indexOf("/*") == -1
							&& StringUtil.LTrim(arryStr[i]).indexOf("<!--") == -1) {
						strTmp += arryStr[i].substring(0,
								arryStr[i].indexOf("--"))
								+ "\n";
					} else {
						strTmp += arryStr[i] + "\n";
					}
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return strTmp;
	}

	public static String trimSql(String oriStr) {
		String strTmp = "";
		try {
			String strStr = oriStr;
			strStr = StringUtil.replace(strStr, "/ *", "/*");
			strStr = StringUtil.replace(strStr, "* /", "*/");
			String arryStr[] = strStr.split("\n");
			boolean bChk = false;
			for (int i = 0; i < arryStr.length; i++) {
				String tmp = StringUtil.LTrim(arryStr[i]);
				if (!tmp.equals("")) {
					if (tmp.startsWith("--") && tmp.indexOf("/*") == -1
							&& tmp.endsWith("*/")) {
						strTmp += tmp + "\n";
					} else if (tmp.startsWith("--")) {
					} else if (!tmp.startsWith("/*") && tmp.indexOf("--") > -1) {
						strTmp += tmp.substring(0, tmp.indexOf("--")) + "\n";
					} else if (!tmp.startsWith("/*") && tmp.indexOf("/*") > -1) {
						if (tmp.indexOf("*/") > -1) {
							strTmp += tmp.substring(0, tmp.indexOf("/*"))
									+ "\n";
						} else {
							bChk = true;
						}
					} else if (tmp.startsWith("/*") && tmp.indexOf("*/") == -1) {
						bChk = true;
					} else if (bChk == true && tmp.indexOf("*/") > -1) {
						bChk = false;
					} else {
						if (bChk == false) {
							if (tmp.startsWith("/*")
									&& tmp.trim().endsWith("*/")) {
							} else {
								strTmp += tmp + "\n";
							}
						}
					}
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return strTmp;
	}

	public static String trimProc(String oriStr) {
		String strTmp = "";
		try {
			String strStr = oriStr;
			strStr = strStr.toUpperCase();
			strStr = StringUtil.replace(strStr, "$%", "%");
			while (strStr.indexOf("  ") > -1) {
				strStr = StringUtil.replace(strStr, "  ", " ");
			}
			strStr = StringUtil.replace(strStr, "MERGE INTO", "MERGEINTO");
			strStr = StringUtil.replace(strStr, "INSERT INTO", "INSERTINTO");
			strStr = StringUtil.replace(strStr, " INTO ", "\nINTO ");
			strStr = StringUtil.replace(strStr, "MERGEINTO", "MERGE INTO");
			strStr = StringUtil.replace(strStr, "INSERTINTO", "INSERT INTO");
			strTmp = trimSql(strStr);

			strTmp = StringUtil.replace(strTmp, "INSERT\nINTO", "INSERT INTO");
			String arryStr[] = strTmp.split("\n");
			strTmp = "";
			boolean bChk = false; // /*
			boolean bChk2 = false; // INTO
			for (int i = 0; i < arryStr.length; i++) {
				String tmp = StringUtil.LRTrim(arryStr[i]);
				if (tmp.startsWith("/*")) {
					bChk = true;
				}
				if (bChk == false
						&& bChk2 == false
						&& !(tmp.startsWith("INTO ") && StringUtil.LTrim(
								arryStr[i - 1]).indexOf("INSERT") == -1)) {
					strTmp += tmp + "\n";
				}
				if (tmp.startsWith("INTO ") && tmp.endsWith(",")) {
					bChk2 = true;
				}
				if (bChk == false && tmp.startsWith("INTO ")
						&& tmp.indexOf(" FROM ") > -1) {
					if (bChk2 == false) {
						strTmp += tmp.substring(tmp.indexOf(" FROM"),
								tmp.length())
								+ "\n";
					} else if (bChk2 == true && !tmp.endsWith(",")) {
						bChk2 = false;
					}
				} else if (bChk == false && tmp.startsWith("INTO ")
						&& tmp.endsWith(";")) {
					strTmp += "\n ;";
				}
				if (bChk == true && tmp.endsWith("*/")) {
					bChk = false;
				}
				if (bChk2 == true && !tmp.endsWith(",")) {
					bChk2 = false;
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return strTmp;
	}

	/**
	 * <pre>
	 * </pre>
	 */
	public static String setSqlParamInProc(String sql,
			Map<String, Object> params) {
		try {
			sql = StringUtil.replace(sql, "SELECT\n", "SELECT \n");
			sql = StringUtil.replace(sql, "INSERT\n", "INSERT \n");
			sql = StringUtil.replace(sql, "UPDATE\n", "UPDATE \n");
			sql = StringUtil.replace(sql, "DELETE\n", "DELETE \n");
			sql = StringUtil.replace(sql, "FROM\n", "FROM \n");
			sql = StringUtil.replace(sql, "WHERE\n", "WHERE \n");
			sql = StringUtil.replace(sql, "\n", " ");
			sql = StringUtil.replace(sql, "\n", " ");
			sql = StringUtil.replace(sql, "\t", " ");
			sql = StringUtil.replace(sql, ",", " , ");
			sql = StringUtil.replace(sql, "||", " || ");
			sql = StringUtil.replace(sql, "+", " + ");
			sql = StringUtil.replace(sql, "-", " - ");
			sql = StringUtil.replace(sql, "*", " * ");
			sql = StringUtil.replace(sql, "/", " / ");
			sql = StringUtil.replace(sql, "(", " ( ");
			sql = StringUtil.replace(sql, ")", " ) ");
			while (sql.indexOf("  ") > -1) {
				sql = StringUtil.replace(sql, "  ", " ");
			}
			sql += " ";
			while (sql.indexOf(" P_") > -1) {
				String strBef = sql.substring(0, sql.indexOf(" P_"));
				String strAft = sql.substring(
						sql.indexOf(" ", sql.indexOf(" P_") + 1) + 1,
						sql.length());
				sql = strBef + " '1' " + strAft;
			}
			Set dataKeySet = params.keySet();
			Iterator dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				String col = dataKey;
				if (col.startsWith("%")) {
					col = col.substring(1, col.length());
					String val = StringUtil.getText(params.get(dataKey));
					sql = StringUtil.replace(sql, col, val);
				}
			}
			dataKeySet = params.keySet();
			dataIterator = dataKeySet.iterator();
			while (dataIterator.hasNext()) {
				String dataKey = dataIterator.next().toString();
				if (dataKey.startsWith("%")) {
					dataKey = dataKey.substring(1, dataKey.length());
				}
				if (sql.indexOf(" " + dataKey) > -1) {
					String strBef = sql
							.substring(0, sql.indexOf(" " + dataKey));
					if (strBef.trim().endsWith(" AS"))
						continue;
					sql = StringUtil.replace(sql, " " + dataKey + " ", " '1' ");
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return sql;
	}

	public static Map<String, Object> setPrcParam(String sqlSrc) {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String strOri = sqlSrc;
			String tmpSql = StringUtil.replace(strOri, "\t", " ");
			String sqlArry[] = null;
			while (tmpSql.indexOf("  ") > -1) {
				tmpSql = StringUtil.replace(tmpSql, "  ", " ");
			}

			sqlArry = tmpSql.split("\n");
			for (int i = 0; i < sqlArry.length; i++) {
				String tmp = sqlArry[i];
				if (tmp.equals(""))
					continue;
				tmp = StringUtil.LTrim(StringUtil.replace(tmp, ",", ""));
				try {
					if (tmp.indexOf(":=") > -1)
						tmp = StringUtil.replace(tmp, "'", "\"");
					if (tmp.indexOf("'") == -1
							&& (tmp.indexOf(" VARCHAR") > -1
									|| tmp.indexOf(" NUMBER") > -1 || tmp
									.indexOf("%TYPE") > -1)) {
						tmp = tmp.substring(0, tmp.indexOf(" "));
						params.put(tmp, tmp);
					}
					if (tmp.indexOf("%ROWTYPE") > -1) {
						String bef = tmp.substring(0, tmp.indexOf(" "));
						tmp = tmp.substring(bef.length(), tmp.indexOf("%"));
						tmp = StringUtil.LRTrim(tmp);
						params.put("%" + bef, tmp);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

			sqlArry = strOri.split("\n");
			for (int i = 0; i < sqlArry.length; i++) {
				String tmp = sqlArry[i];
				if (tmp.equals(""))
					continue;
				tmp = StringUtil.LTrim(StringUtil.replace(tmp, ",", ""));
				try {
					if (tmp.indexOf(":=") > -1) {
						tmp = tmp.substring(0, tmp.indexOf(":=")).trim();
						params.put(tmp, tmp);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

			sqlSrc = strOri;
			while (sqlSrc.indexOf("\nFUNCTION ") > -1) {
				tmpSql = "";
				if (sqlSrc.indexOf("\nFUNCTION ") > -1) {
					tmpSql = sqlSrc
							.substring(
									sqlSrc.indexOf("(",
											sqlSrc.indexOf("\nFUNCTION ")) + 1,
									sqlSrc.indexOf(")",
											sqlSrc.indexOf("\nFUNCTION ")));
				}
				sqlSrc = sqlSrc.substring(
						sqlSrc.indexOf("(", sqlSrc.indexOf("\nFUNCTION ")),
						sqlSrc.length());
				sqlArry = tmpSql.split("\n");
				for (int i = 0; i < sqlArry.length; i++) {
					String tmp = StringUtil.LRTrim(sqlArry[i]);
					if (tmp.equals(""))
						continue;
					tmp = StringUtil.replace(tmp, ",", "");
					tmp = tmp.substring(0, tmp.indexOf(" "));
					params.put(tmp, tmp);
				}
			}

			sqlSrc = strOri;
			while (sqlSrc.indexOf("\nFOR ") > -1) {
				tmpSql = "";
				String paramId = "";
				if (sqlSrc.indexOf("\nFOR ") > -1) {
					paramId = sqlSrc.substring(sqlSrc.indexOf("\nFOR ")
							+ "\nFOR ".length(),
							sqlSrc.indexOf(" IN", sqlSrc.indexOf("\nFOR ")));
					try {
						if (sqlSrc.indexOf("SELECT", sqlSrc.indexOf("\nFOR ")) > -1
								&& sqlSrc.indexOf("FROM",
										sqlSrc.indexOf("\nFOR ")) > sqlSrc
										.indexOf("SELECT",
												sqlSrc.indexOf("\nFOR "))
										+ "SELECT ".length()) {
							tmpSql = sqlSrc.substring(
									sqlSrc.indexOf("SELECT",
											sqlSrc.indexOf("\nFOR "))
											+ "SELECT".length(),
									sqlSrc.indexOf("FROM",
											sqlSrc.indexOf("\nFOR ")));
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				if (tmpSql.equals(""))
					tmpSql = sqlSrc;
				sqlSrc = sqlSrc.substring(
						sqlSrc.indexOf(" IN", sqlSrc.indexOf("\nFOR ")),
						sqlSrc.length());
				if (tmpSql.indexOf("FROM") > -1)
					tmpSql = tmpSql.substring(0, tmpSql.indexOf("FROM"));
				sqlArry = tmpSql.split("\n");
				for (int i = 0; i < sqlArry.length; i++) {
					String tmp = StringUtil.LRTrim(sqlArry[i]);
					if (tmp.equals(""))
						continue;
					tmp = StringUtil.replace(tmp, ",", "");
					if (tmp.indexOf(" AS ") > -1)
						tmp = tmp.substring(tmp.indexOf(" AS ") + 4,
								tmp.length());
					if (tmp.indexOf(".") > -1)
						tmp = tmp.substring(tmp.lastIndexOf(".") + 1,
								tmp.length());
					if (tmp.indexOf(" ") > -1)
						tmp = tmp.substring(tmp.lastIndexOf(" ") + 1,
								tmp.length());
					if (tmp.length() > 1) {
						params.put(paramId + "." + tmp, paramId + "." + tmp);
					}
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return params;
	}

	public static void errLogFlush() {
		try {
			if (appender4 != null) {
				appender4.flush();
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		} finally {
			try {
				if (appender4 != null)
					appender4.close();
				if (fos4 != null)
					fos4.close();
				if (bos4 != null)
					bos4.close();
				if (out4 != null)
					out4.close();
			} catch (Exception e) {
				ExtUtil.getMessage(e);
			}
		}
	}

	public static void closeCon(PreparedStatement ps, ResultSet rs,
			Connection conn) {
		try {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e1) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e1) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e1) {
				}
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
	}

	public static void getMessage(Exception e) {
		System.out.println(e.getMessage());
	}

	public static void log(String alog) {
		if (!alog.equals("")) {
			System.out.println(alog);
		}
	}

	public static boolean includeStep(String list, String step) {
		String stepArry[] = list.split(",");
		for (int i = 0; i < stepArry.length; i++) {
			if (stepArry[i].equals(step)) {
				return true;
			}
		}
		return false;
	}

	public static String getQueryIdFromSql(String sql) {
		String queryId = "";
		try {
			String tempSql = StringUtil.LRTrim(sql);
			String tempArry[] = null;
			tempArry = StringUtil.split(tempSql, "\n");
			if (tempArry[0].indexOf("*/") > -1) {
				queryId = StringUtil.replace(tempArry[0], "/*", "");
				queryId = StringUtil.replace(queryId, "*/", "");
				queryId = StringUtil.LRTrim(queryId);
			} else {
				if (tempSql.indexOf("\n") > -1)
					tempSql = tempSql.substring(0, tempSql.indexOf("\n"));
				queryId = tempSql;
			}
		} catch (Exception e) {
		}
		return queryId;
	}

	public static String getTableIds(Map<String, Object> tableList) {
		String tableIds = "";
		Set dataKeySet = tableList.keySet();
		Iterator dataIterator = dataKeySet.iterator();
		while (dataIterator.hasNext()) {
			String col = dataIterator.next().toString();
			String tableId = StringUtil.getText(tableList.get(col));
			if (col.equals("tableId")) {
				if (!tableId.equals("")) {
					tableIds += StringUtil.getText(tableList.get(col)) + "^";
				}
			} else if (!col.equals("queryId") && !col.equals("CRUD")) {
				tableIds += col + "^";
			}
		}
		if (tableIds.indexOf("^") > -1)
			tableIds = tableIds.substring(0, tableIds.length() - 1);

		return tableIds;
	}

	public static void debug(Map<String, Object> info) {
		if (info.toString().indexOf(debugString) > -1) {
			log(info.toString());
		}
	}

	public static String bytesToString(String bytes) {
		String result = "";
		try {
			int i = bytes.length() / 8;
			int pos = 0;
			byte[] buffer = new byte[i];
			for (int j = 0; j < i; j++) {
				String temp = bytes.substring(pos, pos + 8);
				buffer[j] = (byte) Integer.parseInt(temp, 2);
				pos += 8;
			}
			result = new String(buffer, "UTF-8");
			System.out.println("Result: " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String maina(String bin) {
		StringBuilder b = new StringBuilder();
		int len = bin.length();
		int i = 0;
		while (i + 8 <= len) {
			char c = convert(bin.substring(i, i + 8));
			i += 8;
			b.append(c);
		}
		return b.toString();
	}

	private static char convert(String bs) {
		return (char) Integer.parseInt(bs, 2);
	}

	public static Map<String, Object> getCRUD(String strCRUD, String sql,
			String tableIds, Map<String, Object> tableList) {
		try {
			if (sql.startsWith("CALL")) {
			} else {
				if (tableIds.indexOf("^") > -1) {
					tableIds = StringUtil.replace(tableIds, "DUAL^", "");
					tableIds = StringUtil.replace(tableIds, "^DUAL", "");
					if (tableIds.startsWith("^"))
						tableIds = tableIds.substring(1, tableIds.length());
					String tableIdArry[] = tableIds.split("\\^");
					if (!sql.startsWith("SELECT")) {
						String tableId2 = tableIdArry[0];
						for (int i = 1; i < tableIdArry.length; i++) {
							if (sql.indexOf(" " + tableIdArry[i]) < sql
									.indexOf(" " + tableIdArry[i - 1])) {
								tableId2 = tableIdArry[i];
							}
						}
						if (tableIds.indexOf(tableId2) != 0) {
							tableIds = StringUtil.replace(tableIds, "^"
									+ tableId2, "");
							tableIds = tableId2 + "^" + tableIds;
						}
					}
					for (int i = 1; i < tableIdArry.length; i++) {
						strCRUD += "^R";
					}
				}
			}
			tableList.put("CRUD", strCRUD);
			tableList.put("tableId", tableIds);
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return tableList;
	}

	public static List<String> getArryParam(String strSrc) {
		List<String> arryList = new ArrayList<String>();
		String inputArry[] = StringUtil.getText(strSrc).split(",");
		for (int i = 0; i < inputArry.length; i++) {
			arryList.add(inputArry[i]);
		}
		return arryList;
	}
}
