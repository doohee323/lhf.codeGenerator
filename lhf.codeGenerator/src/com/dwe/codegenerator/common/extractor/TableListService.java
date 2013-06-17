package com.dwe.codegenerator.common.extractor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dwe.codegenerator.common.util.ExtUtil;
import com.dwe.codegenerator.common.util.StringUtil;

public class TableListService {

	private Connection conn = null;

	public Map<String, Object> getTableList(String sql) {
		if (sql.startsWith("/*")) {
			String bef = sql.substring(0, sql.indexOf("*/") + 2);
			String aft = sql.substring(sql.indexOf("*/") + 2, sql.length());
			sql = bef + aft.toUpperCase();
		}
		return getTableFromSql(sql);
	}

	public Map<String, Object> getTableFromSql(String sql) {
		Map<String, Object> tableList = new HashMap<String, Object>();
		tableList = getTableFromSql(sql, tableList);
		return tableList;
	}

	public Map<String, Object> getTableFromSql(String sql,
			Map<String, Object> tableList) {
		String tempSql = StringUtil.LRTrim(sql);
		String queryId = "";
		String tableIds = "";
		String tempArry[] = null;
		try {
			tempSql = StringUtil.replace(tempSql, "\t", " ");
			tempArry = StringUtil.split(tempSql, "\n");
			if (tempArry[0].indexOf("*/") > -1) {
				queryId = StringUtil.replace(tempArry[0], "/*", "");
				queryId = StringUtil.replace(queryId, "*/", "");
				queryId = StringUtil.LRTrim(queryId);
			} else {
				if (tempSql.indexOf("\n") > -1)
					tempSql = tempSql.substring(0, tempSql.indexOf("\n"));
				queryId = tempSql;
				if (queryId.length() > 50)
					queryId = queryId.substring(0, 50);
			}
			tempSql = ExtUtil.trimSql(tempSql);
			tempSql = tempSql.toUpperCase();

			tempSql = StringUtil.replace(tempSql, "WITH\n", "WITH ");
			tempSql = StringUtil.replace(tempSql, "INSERT\n", "INSERT ");
			tempSql = StringUtil.replace(tempSql, "UPDATE\n", "UPDATE ");
			tempSql = StringUtil.replace(tempSql, "DELETE\n", "DELETE ");
			tempSql = StringUtil.replace(tempSql, "MERGE\n", "MERGE ");
			tempSql = StringUtil.replace(tempSql, "SELECT\n", "SELECT ");
			tempSql = StringUtil.replace(tempSql, "\nFROM\n", "\nFROM ");
			tempSql = StringUtil.replace(tempSql, "\nSTART", " START");
			tempSql = StringUtil.replace(tempSql, "START\n", "START ");
			tempSql = StringUtil.replace(tempSql, " FROM\n", " FROM ");

			if (tempSql.startsWith("(")) {
				tempSql = StringUtil.LRTrim(tempSql.substring(1,
						tempSql.length() - 1));
			}

			tempSql = StringUtil.replace(tempSql, "\nJOIN ", " FROM ");
			tempSql = StringUtil.replace(tempSql, " JOIN ", " FROM ");
			tempSql = StringUtil.replace(tempSql, " JOIN\n", " FROM ");
			if (tempSql.length() < 10)
				return new HashMap<String, Object>();
			List<String> withAlias = new ArrayList<String>();
			if (tempSql.indexOf("WITH ") > -1) {
				String strArry[] = tempSql.split("WITH ");
				for (int x = 0; x < strArry.length; x++) {
					if (tempSql.indexOf("WITH ") - 15 > -1) {
						if (tempSql.indexOf(" ", tempSql.indexOf("WITH ") - 15) < tempSql
								.indexOf("WITH ")) {
							String tmp = "";
							tmp = tempSql.substring(
									tempSql.indexOf(" ",
											tempSql.indexOf("WITH ") - 15),
									tempSql.indexOf("WITH "));
							if (tmp.indexOf("START ") > -1)
								continue;
						}
					}
					String tableId = tempSql.substring(tempSql.indexOf("WITH ")
							+ "WITH ".length(), tempSql.length());
					tableId = StringUtil.LTrim(tableId);
					if (tableId.indexOf(" ") > -1) {
						tableId = tableId.substring(0, tableId.indexOf(" "));
						if (!withAlias.contains(tableId))
							withAlias.add(tableId);
					}
				}
			}

			List<String> procList = new ArrayList<String>();
			if (tempSql.startsWith("SP_")) {
				String tableId = tempSql.substring(tempSql.indexOf("SP_"),
						tempSql.length());
				if (tableId.indexOf("(") > -1) {
					tableId = tableId.substring(0, tableId.indexOf("("));
					if (tableId.indexOf(")") > -1) {
						System.out.println("");
					}
					tableId = StringUtil.RTrim(tableId);
					procList.add(tableId);
					tableIds = tableId;
				}
			} else if (tempSql.indexOf(" SP_") > -1
					&& tempSql.indexOf("CALL ") == -1) {
				String strArry[] = tempSql.split(" SP_");
				for (int x = 1; x < strArry.length; x++) {
					String tableId = strArry[x]
							.substring(strArry[x].indexOf(" SP_") + 1,
									strArry[x].length());
					tableId = StringUtil.LTrim(tableId);
					if (tableId.indexOf("(") > -1) {
						tableId = tableId.substring(0, tableId.indexOf("("));
						if (tableId.indexOf("=") > -1
								|| tableId.indexOf(",") > -1
								|| tableId.indexOf("*") > -1
								|| tableId.indexOf("+") > -1) {
							continue;
						}
						tableId = "SP_" + StringUtil.RTrim(tableId);
						if (!procList.contains(tableId)) {
							procList.add(tableId);
							tableIds = tableIds + "^" + tableId;
						}
					}
				}
				if (tableIds.startsWith("^"))
					tableIds = tableIds.substring(1, tableIds.length());
			}
			List<String> funcList = new ArrayList<String>();
			if (tempSql.indexOf(" FN_") > -1) {
				String strArry[] = tempSql.split(" FN_");
				for (int x = 1; x < strArry.length; x++) {
					String tableId = strArry[x]
							.substring(strArry[x].indexOf(" FN_") + 1,
									strArry[x].length());
					tableId = StringUtil.LTrim(tableId);
					if (tableId.indexOf("(") > -1) {
						tableId = tableId.substring(0, tableId.indexOf("("));
						tableId = "FN_" + StringUtil.RTrim(tableId);
						if (!funcList.contains(tableId)) {
							funcList.add(tableId);
							tableIds = tableIds + "^" + tableId;
						}
					}
				}
				if (tableIds.startsWith("^"))
					tableIds = tableIds.substring(1, tableIds.length());
			}
			if (tempSql.startsWith("WITH ")) {
				tempSql = tempSql.substring(tempSql.indexOf("SELECT"),
						tempSql.length());
			}
			if (tempSql.startsWith("INSERT")) {
				String tableId = tempSql.substring(
						tempSql.indexOf("INTO ") + 5,
						tempSql.indexOf("\n", tempSql.indexOf("INTO ") + 5));
				tableId = StringUtil.LTrim(tableId);
				if (tableId.indexOf(" ") > -1) {
					tableId = tableId.substring(0, tableId.indexOf(" "));
				}
				tableId = StringUtil.replace(tableId, "(", "");
				tableIds = tableIds + "^"
						+ getTableBySelect(tempSql, tempArry, tableId);
				tableList = ExtUtil.getCRUD("C", sql, tableIds, tableList);
			} else if (tempSql.startsWith("UPDATE")) {
				String tableId = "";
				if (tempSql.substring(tempSql.indexOf("UPDATE ") + 6,
						tempSql.indexOf("UPDATE ") + 10).indexOf("(") == -1) {
					tableId = tempSql.substring(tempSql.indexOf("UPDATE ") + 6,
							tempSql.indexOf("\n"));
					tableId = StringUtil.LTrim(tableId);
					if (tableId.indexOf(" ") > -1) {
						tableId = tableId.substring(0, tableId.indexOf(" "));
					}
				}
				tableIds = tableIds + "^"
						+ getTableBySelect(tempSql, tempArry, tableId);
				tableList = ExtUtil.getCRUD("U", sql, tableIds, tableList);
			} else if (tempSql.startsWith("DELETE")) {
				String tableId = "";
				if (tempSql.indexOf("FROM ") > -1) {
					tableId = tempSql
							.substring(
									tempSql.indexOf("FROM ") + 5,
									tempSql.indexOf("\n",
											tempSql.indexOf("FROM ") + 5));
				} else {
					tableId = tempSql.substring(tempSql.indexOf(" "),
							tempSql.indexOf("\n", tempSql.indexOf(" ")));
				}
				tableId = StringUtil.LTrim(tableId);
				if (tableId.indexOf(" ") > -1) {
					tableId = tableId.substring(0, tableId.indexOf(" "));
				}
				tableIds = tableIds + "^"
						+ getTableBySelect(tempSql, tempArry, tableId);
				tableList = ExtUtil.getCRUD("D", sql, tableIds, tableList);
			} else if (tempSql.startsWith("SELECT")) {
				tableIds = getTableBySelect(tempSql, tempArry, tableIds);
				tableList = ExtUtil.getCRUD("R", sql, tableIds, tableList);
			} else if (tempSql.startsWith("MERGE")) {
				String tableId = "";
				tableId = tempSql.substring(tempSql.indexOf("INTO ") + 5,
						tempSql.indexOf("\n", tempSql.indexOf("INTO ") + 5));
				tableId = StringUtil.LTrim(tableId);
				if (tableId.indexOf(" ") > -1) {
					tableId = tableId.substring(0, tableId.indexOf(" "));
				}
				tableIds = tableIds + "^"
						+ getTableBySelect(tempSql, tempArry, tableId);
				tableList = ExtUtil.getCRUD("M", sql, tableIds, tableList);
			} else if (tempSql.startsWith("{")) {
				queryId = StringUtil.replace(queryId, ",", "_");
				tableList = ExtUtil.getCRUD("P", sql, tableIds, tableList);
			} else if (tempSql.startsWith("SP_")) {
				queryId = tableIds;
				tableList = ExtUtil.getCRUD("P", sql, tableIds, tableList);
			}
			if (tableIds.equals("")) {
				if (tempSql.startsWith("{")) {
					ExtUtil.log("");
				} else if (queryId.indexOf(" ") > -1) {
					queryId = "";
				} else {
					ExtUtil.log("");
				}
			} else {
				String tableId = StringUtil.getText(tableList.get("tableId"))
						+ "^";
				String CRUD = StringUtil.getText(tableList.get("CRUD")) + "^";
				for (int i = 0; i < withAlias.size(); i++) {
					if (tableId.indexOf(StringUtil.getText(withAlias.get(i))
							+ "^") > -1) {
						tableId = StringUtil.replace(tableId,
								StringUtil.getText(withAlias.get(i)) + "^", "");
						int pos = (i + 1) * 2;
						CRUD = CRUD.substring(0, pos - 2)
								+ CRUD.substring(pos, CRUD.length());
					}
					if (tableId.startsWith("^"))
						tableId = tableId.substring(1, tableId.length());
					if (CRUD.startsWith("^"))
						CRUD = CRUD.substring(1, CRUD.length());
				}
				if (tableId.endsWith("^"))
					tableId = tableId.substring(0, tableId.length() - 1);

				String tableIdArry[] = tableId.split("\\^");
				String CRUDArry[] = CRUD.split("\\^");
				CRUD = "";
				int j = 0;
				for (int i = 0; i < tableIdArry.length; i++) {
					if (tableIdArry[i].startsWith("FN_")) {
						CRUD += "F^";
					} else if (tableIdArry[i].startsWith("SP_")) {
						CRUD += "P^";
					} else {
						CRUD += CRUDArry[j] + "^";
					}
					j++;
				}
				if (CRUD.endsWith("^"))
					CRUD = CRUD.substring(0, CRUD.length() - 1);

				tableList.put("tableId", tableId);
				tableList.put("CRUD", CRUD);
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}

		tableList.put("queryId", queryId);
		return tableList;
	}

	public String getTableBySelect(String oriSql, String tempArry[],
			String tableIds) {
		try {
			String tempSql = oriSql;
			if (tempSql.indexOf("SELECT ") == -1)
				return tableIds;
			if (!tableIds.equals(""))
				tableIds = tableIds + "^";
			tempSql = StringUtil.replace(tempSql, "\n", " ");
			while (tempSql.indexOf("  ") > -1) {
				tempSql = StringUtil.replace(tempSql, "  ", " ");
			}
			while (tempSql.indexOf("FROM ") > -1) {
				String tableId = "";
				if (tempSql.indexOf(" ", tempSql.indexOf("FROM ")) > -1) {
					tableId = StringUtil.LTrim(tempSql.substring(
							tempSql.indexOf("FROM ") + 5,
							tempSql.indexOf(" ", tempSql.indexOf("FROM ")
									+ "FROM ".length())));
					if (tableId.indexOf(")") > -1) {
						tableId = tableId.substring(0, tableId.indexOf(")"));
					}
				}
				if (tableId.indexOf(" ") > -1)
					tableId = tableId.substring(0, tableId.indexOf(" "));
				if (tableId.startsWith("("))
					tableId = "";
				if (tableId.equals("")) {
					tempSql = tempSql.substring(tempSql.indexOf("FROM ") + 5,
							tempSql.length());
				} else {
					tempSql = tempSql.substring(tempSql.indexOf(tableId) + 1,
							tempSql.length());
					if (tempSql.indexOf(" WHERE ") > -1) {
						if (tempSql.indexOf(",") != -1
								&& tempSql.indexOf(",") < tempSql
										.indexOf(" WHERE ")) {
							tempSql = " FROM"
									+ tempSql.substring(
											tempSql.indexOf(",") + 1,
											tempSql.length());
						}
					} else {
						if (tempSql.indexOf(",") != -1) {
							tempSql = " FROM" + tempSql;
						}
					}
					if (tableId.indexOf("(") == -1
							&& tableId.indexOf(",") == -1
							&& tableId.indexOf("/*") == -1
							&& tableId.indexOf("*/") == -1
							&& tableId.indexOf("=") == -1) {
						tableIds += tableId + "^";
					}
				}
			}
			if (tableIds.endsWith("^"))
				tableIds = tableIds.substring(0, tableIds.length() - 1);
			if (tableIds.indexOf("^") > -1) {
				String tableArry[] = tableIds.split("\\^");
				List<String> tmpArry = new ArrayList<String>();
				for (int i = 0; i < tableArry.length; i++) {
					if (!tmpArry.contains(tableArry[i])) {
						tmpArry.add(tableArry[i]);
					}
				}
				tableIds = tmpArry.toString();
				tableIds = tableIds.substring(1, tableIds.length() - 1);
				tableIds = StringUtil.replace(tableIds, " ", "");
				tableIds = StringUtil.replace(tableIds, ",", "^");
			}
		} catch (Exception e) {
			ExtUtil.getMessage(e);
		}
		return tableIds;
	}

	public List<String> getAllTableList(String dbSpec) {
		List<String> tableList = new ArrayList<String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String owner = "";
			// String owner =
			// StringUtil.getText(appProperties.getProperty("com.lhf." + dbSpec
			// + ".database.schema")).toUpperCase();
			// conn = getJDBCConnection(dbSpec);
			ps = conn
					.prepareStatement("SELECT OBJECT_NAME FROM DBA_OBJECTS WHERE OBJECT_TYPE = 'TABLE' AND OWNER = '"
							+ owner + "'");
			rs = ps.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString(1);
				if (tableName != null && !"".equals(tableName)) {
					tableList.add(tableName);
				}
			}
			if (tableList.size() == 0) {
				Integer.parseInt("");
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			ExtUtil.closeCon(ps, rs, conn);
		}
		return tableList;
	}

}
