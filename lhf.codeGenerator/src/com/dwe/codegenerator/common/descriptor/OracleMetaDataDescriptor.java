package com.dwe.codegenerator.common.descriptor;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : OracleMetaDataService
 * 설    명 : DB에서 메타정보를 추출하도록 지원하는 서비스
 * 작 성 자 : DWE
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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.dwe.codegenerator.common.db.TypeMapping;
import com.dwe.codegenerator.common.util.DateUtil;
import com.dwe.codegenerator.common.util.GeneratorUtil;
import com.dwe.codegenerator.common.util.StringUtil;

public class OracleMetaDataDescriptor {

	private final static String COLUMN_TYPE[] = new String[] { "ARRAY",
			"BIGINT", "BINARY", "BIT", "BLOB", "BOOLEAN", "CHAR", "CLOB",
			"DATALINK", "DATE", "DECIMAL", "DISTINCT", "DOUBLE", "FLOAT",
			"INTEGER", "JAVA_OBJECT", "LONGVARBINARY", "LONGVARCHAR", "NULL",
			"NUMERIC", "OTHER", "REAL", "REF", "SMALLINT", "STRUCT", "TIME",
			"TIMESTAMP", "TINYINT", "VARBINARY", "VARCHAR" };

	private Connection conn = null;

	/**
	 * @param spec
	 * @return
	 * @throws Exception
	 */
	public void setJDBCConnection(Connection aConn) throws Exception {
		try {
			conn = aConn;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * DB에서 메타정보를 추출한다.
	 * 
	 * @param owner
	 * @param sql
	 * @return
	 */
	public Map<String, Object> getDataList(String owner, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			Map<String, Object> tableList = getTableList(owner, sql);
			Map<String, Object> colCommentList = getColCommentList(owner,
					tableList);
			List<String> colPkList = getPkColList(owner, tableList);
			int nSize = rsmd.getColumnCount();
			for (int i = 1; i <= nSize; i++) {
				String columnName = rsmd.getColumnName(i);
				String attrName = GeneratorUtil.carmelNaming(columnName
						.toLowerCase());
				Map<String, Object> colInfo = getColumnInfo(rsmd, i);
				String comment = (String) colCommentList.get(columnName);
				boolean isPk = colPkList.contains(columnName);
				if (comment == null)
					comment = columnName;
				colInfo.put("comment", comment);
				colInfo.put("key", new Boolean(isPk));
				map.put("dataList." + attrName, colInfo);
			}
		} catch (Exception e) {
			error(sql, e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return map;
	}

	/**
	 * 모든 테이블, 컬럼정보를 조회한다.
	 * 
	 * @param owner
	 * @param searchType
	 * @param searchValue
	 * @param isAllUser
	 * @return
	 */
	public Map<String, Object> getTableDataList(String owner,
			String searchType, String searchValue) {
		Map<String, Object> list = new HashMap<String, Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet mtRs = null;

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT /*+ ORDERED */ DISTINCT UTC.TABLE_NAME, \n");
			sql.append("    (SELECT COMMENTS FROM ALL_TAB_COMMENTS A WHERE A.OWNER = UTC.OWNER AND A.TABLE_NAME = UTC.TABLE_NAME AND A.OWNER = UTC.OWNER AND ROWNUM = 1) TABLE_COMMENTS, \n");
			sql.append("    UTC.COLUMN_NAME,     \n");
			sql.append("    UCC.COMMENTS COLUMN_COMMENTS,    \n");
			sql.append("    UTC.DATA_TYPE,   \n");
			sql.append("    UTC.DATA_LENGTH,     \n");
			sql.append("    UTC.DATA_SCALE,   \n");
			sql.append("    UTC.NULLABLE   \n");
			sql.append("FROM \n");
			sql.append("    ALL_COL_COMMENTS UCC,   \n");
			sql.append("    ALL_TAB_COLS UTC   \n");
			sql.append("WHERE    \n");
			sql.append("    UTC.OWNER = UCC.OWNER \n");
			if(!owner.equals("")) {
	            sql.append("AND UTC.OWNER = '" + owner.toUpperCase() + "' \n");
			}
			sql.append("AND UTC.TABLE_NAME = UCC.TABLE_NAME \n");
			sql.append("AND UTC.COLUMN_NAME = UCC.COLUMN_NAME \n");
			sql.append("AND UTC.COLUMN_NAME NOT LIKE '$' \n");
			sql.append("AND ROWNUM <= 100 \n");
			sql.append("AND (   \n");
			if ("TABLE".equals(searchType)) {
				sql.append("    UTC.TABLE_NAME = UPPER('" + searchValue
						+ "')    \n");
			} else {
				sql.append("    UCC.COLUMN_NAME = UPPER('" + searchValue
						+ "')    \n");
				sql.append("    OR  \n");
				sql.append("    UCC.COMMENTS = UPPER('" + searchValue
						+ "')   \n");
			}
			sql.append("    )   \n");
			sql.append("ORDER BY TABLE_NAME, COLUMN_NAME   \n");

			ps = conn.prepareStatement(sql.toString());

			DatabaseMetaData meta = conn.getMetaData();

			rs = ps.executeQuery();

			List<String> tableMap = new ArrayList<String>();

			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				String tableComments = rs.getString("TABLE_COMMENTS");
				String columnName = rs.getString("COLUMN_NAME");
				String typeName = rs.getString("DATA_TYPE");
				String comment = rs.getString("COLUMN_COMMENTS");
				if (comment == null)
					comment = columnName;
				String displaySize = rs.getString("DATA_LENGTH");
				String scale = rs.getString("DATA_SCALE");
				if (scale == null)
					scale = "0";
				String isNotNull = rs.getString("NULLABLE");
				String label = GeneratorUtil.carmelNaming(columnName
						.toLowerCase());
				boolean isPk = false;

				Map<String, Object> colInfo = new HashMap<String, Object>();
				colInfo.put("tableName", tableName);
				colInfo.put("tableComment", tableComments == null ? tableName
						: tableComments);
				colInfo.put("label", label);
				colInfo.put("columnName", columnName);
				colInfo.put("columnType", typeName);
				colInfo.put("typeName", getJavaType(typeName, scale));
				colInfo.put("comment", comment);
				colInfo.put("displaySize", displaySize);
				colInfo.put("scale", scale);
				colInfo.put("isNotNull", "Y".equals(isNotNull) ? "true"
						: "false");
				colInfo.put("key", isPk ? "true" : "false");

				list.put("dataList." + label, colInfo);

				if (!tableMap.contains(tableName))
					tableMap.add(tableName);
			}

			List<String> pkCol = new ArrayList<String>();

			Iterator keys = tableMap.iterator();
			while (keys.hasNext()) {
				String tableName = (String) keys.next();

				mtRs = meta.getPrimaryKeys(null, null, tableName.toUpperCase());

				while (mtRs.next()) {
					pkCol.add(tableName + "|" + mtRs.getString("COLUMN_NAME"));
				}

				if (mtRs != null) {
					try {
						mtRs.close();
					} catch (Exception e) {
					}
				}
			}

			keys = list.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				Map<String, Object> colInfo = (Map<String, Object>) list
						.get(key);

				String tableName = (String) colInfo.get("tableName");
				String columnName = (String) colInfo.get("columnName");

				if (pkCol.contains(tableName + "|" + columnName)) {
					colInfo.put("key", "true");
				}
			}
		} catch (Exception e) {
			error("", e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	/**
	 * 컬럼의 Comment 정보를 조회한다.
	 */
	public Map<String, Object> getColCommentList(String owner,
			Map<String, Object> tableList) {
		Map<String, Object> colCommentList = new HashMap<String, Object>();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.COLUMN_NAME, A.COMMENTS, B.DATA_TYPE ");
			sql.append("FROM ALL_COL_COMMENTS A, ");
			sql.append("     ALL_TAB_COLUMNS B ");
			sql.append("WHERE ");
			sql.append("    A.OWNER = B.OWNER ");
			sql.append("AND A.TABLE_NAME = B.TABLE_NAME ");
			sql.append("AND A.COLUMN_NAME = B.COLUMN_NAME ");
			sql.append("AND A.TABLE_NAME = ? ");
			sql.append("AND A.OWNER = ? ");
			sql.append("ORDER BY B.COLUMN_ID ASC ");
			Iterator keys = tableList.keySet().iterator();

			while (keys.hasNext()) {
				String tableName = (String) keys.next();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, tableName.trim().toUpperCase());
				ps.setString(2, owner.trim().toUpperCase());
				rs = ps.executeQuery();
				while (rs.next()) {
					colCommentList.put(rs.getString("COLUMN_NAME"),
							rs.getString("COMMENTS"));
				}

				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			error("", e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return colCommentList;
	}

	/**
	 * 테이블 컬럼, 컬럼의 Comment 정보를 조회한다.
	 */
	public Map<String, Object> getColInfoList(String owner,
			Map<String, Object> tableList, String type,
			Map<String, Object> inputData) {
		Map<String, Object> colCommentList = new HashMap<String, Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT A.COLUMN_NAME, A.COMMENTS, B.DATA_TYPE ");
			sql.append("FROM ALL_COL_COMMENTS A, ");
			sql.append("     ALL_TAB_COLUMNS B ");
			sql.append("WHERE ");
			sql.append("    A.OWNER = B.OWNER ");
			sql.append("AND A.TABLE_NAME = B.TABLE_NAME ");
			sql.append("AND A.COLUMN_NAME = B.COLUMN_NAME ");
			sql.append("AND A.TABLE_NAME = ? ");
			sql.append("AND A.OWNER = ? ");
			sql.append("ORDER BY B.COLUMN_ID ASC ");
			Iterator keys = tableList.keySet().iterator();
			while (keys.hasNext()) {
				String tableName = (String) keys.next();
				ps = conn.prepareStatement(sql.toString());
				ps.setString(1, tableName.trim().toUpperCase());
				ps.setString(2, owner.toUpperCase());
				rs = ps.executeQuery();
				while (rs.next()) {
					String colmn = rs.getString("COLUMN_NAME");
					if (inputData != null) {
						String val = StringUtil.getText(inputData
								.get(GeneratorUtil.carmelNaming(colmn
										.toLowerCase())));
						if (val != null) {
							if (type == null || type.equals("")) {
								colCommentList.put(
										colmn,
										rs.getString("COMMENTS") + ","
												+ rs.getString("DATA_TYPE"));
							} else if (type.equals("comment")) {
								colCommentList.put(colmn,
										rs.getString("COMMENTS"));
							} else if (type.equals("type")) {
								colCommentList.put(colmn,
										rs.getString("DATA_TYPE"));
							}
						} else {
							System.out.println("xml 매핑에 컬럼(" + colmn
									+ ")가 없습니다.");
						}
					} else {
						if (type == null || type.equals("")) {
							colCommentList.put(colmn, rs.getString("COMMENTS")
									+ "," + rs.getString("DATA_TYPE"));
						} else if (type.equals("comment")) {
							colCommentList.put(colmn, rs.getString("COMMENTS"));
						} else if (type.equals("type")) {
							colCommentList
									.put(colmn, rs.getString("DATA_TYPE"));
						}
					}
				}

				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e) {
					}
				}
				if (ps != null) {
					try {
						ps.close();
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			error("", e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}

		return colCommentList;
	}

	/**
	 * 테이블의 PK 정보를 조회한다.
	 */
	public List<String> getPkColList(String owner, Map<String, Object> tableList) {
		List<String> pkColList = new ArrayList<String>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSet mtrs = null;

		try {
			Iterator keys = tableList.keySet().iterator();
			while (keys.hasNext()) {
				String tableName = (String) keys.next();
				DatabaseMetaData meta = conn.getMetaData();
				mtrs = meta.getPrimaryKeys(null, null, tableName.toUpperCase());
				while (mtrs.next()) {
					pkColList.add(mtrs.getString("COLUMN_NAME"));
				}
				if (mtrs != null) {
					try {
						mtrs.close();
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
			error("", e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}

		return pkColList;
	}

	/**
	 * 테이믈 목록 정보를 조회한다.
	 */
	public Map<String, Object> getTableList(String owner, String sql) {
		Map<String, Object> tableList = new HashMap<String, Object>();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			String statementId = "SQLVELOCITY_"
					+ DateUtil.getCurrentTimeString();
			ps = conn.prepareStatement("EXPLAIN PLAN SET STATEMENT_ID = '"
					+ statementId + "' FOR " + sql);
			ps.execute();
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			ps = conn
					.prepareStatement("SELECT DISTINCT A.TABLE_NAME, A.COMMENTS FROM ALL_TAB_COMMENTS A WHERE A.TABLE_NAME IN ( SELECT OBJECT_NAME FROM PLAN_TABLE START WITH ID= 0 AND STATEMENT_ID = '"
							+ statementId
							+ "' CONNECT BY PRIOR ID=PARENT_ID AND STATEMENT_ID='"
							+ statementId + "' GROUP BY  OBJECT_NAME )");
			rs = ps.executeQuery();
			while (rs.next()) {
				String tableName = rs.getString(1);
				String comments = rs.getString(2);
				if (tableName != null && !"".equals(tableName)) {
					tableList.put(tableName, comments);
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
			ps = conn
					.prepareStatement("DELETE FROM PLAN_TABLE WHERE STATEMENT_ID = '"
							+ statementId + "' ");
			ps.executeUpdate();
		} catch (Exception e) {
			error(sql, e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return tableList;
	}

	/**
	 * 인자로 넘겨받은 SQL문을 실행하여 실행결과를 반환한다.
	 */
	public List<Map<String, Object>> retrieveList(String owner, String sql) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			rsmd = rs.getMetaData();
			int nSize = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> mData = new HashMap<String, Object>();
				for (int i = 1; i <= nSize; i++) {
					String colNm = rsmd.getColumnName(i);
					String val = rs.getString(i);
					mData.put(colNm, val);
				}
				list.add(mData);
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
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
				}
			}
		}
		return list;
	}

	/**
	 * DB Table의 Column 정보를 조회한다.
	 */
	private Map<String, Object> getColumnInfo(ResultSetMetaData rsmd, int i) {
		Map<String, Object> colInfo = new HashMap<String, Object>();
		try {
			String columnName = rsmd.getColumnName(i);
			String attrName = GeneratorUtil.carmelNaming(columnName
					.toLowerCase());

			colInfo.put("attrName", attrName);
			colInfo.put("columnName", columnName);
			colInfo.put("label", rsmd.getColumnLabel(i));
			colInfo.put("typeName", rsmd.getColumnTypeName(i));
			colInfo.put("displaySize",
					new Integer(rsmd.getColumnDisplaySize(i)));
			colInfo.put("scale", new Integer(rsmd.getScale(i)));
			colInfo.put("isNotNull", new Boolean(
					(rsmd.isNullable(i) == 0) ? true : false));

			switch (rsmd.getColumnType(i)) {
			case Types.ARRAY:
				colInfo.put("columnType", "ARRAY");
				break;
			case Types.BIGINT:
				colInfo.put("columnType", "BIGINT");
				break;
			case Types.BINARY:
				colInfo.put("columnType", "BINARY");
				break;
			case Types.BIT:
				colInfo.put("columnType", "BIT");
				break;
			case Types.BLOB:
				colInfo.put("columnType", "BLOB");
				break;
			case Types.BOOLEAN:
				colInfo.put("columnType", "BOOLEAN");
				break;
			case Types.CHAR:
				colInfo.put("columnType", "CHAR");
				break;
			case Types.CLOB:
				colInfo.put("columnType", "CLOB");
				break;
			case Types.DATALINK:
				colInfo.put("columnType", "DATALINK");
				break;
			case Types.DATE:
				colInfo.put("columnType", "DATE");
				break;
			case Types.DECIMAL:
				colInfo.put("columnType", "DECIMAL");
				break;
			case Types.DISTINCT:
				colInfo.put("columnType", "DISTINCT");
				break;
			case Types.DOUBLE:
				colInfo.put("columnType", "DOUBLE");
				break;
			case Types.FLOAT:
				colInfo.put("columnType", "FLOAT");
				break;
			case Types.INTEGER:
				colInfo.put("columnType", "INTEGER");
				break;
			case Types.JAVA_OBJECT:
				colInfo.put("columnType", "JAVA_OBJECT");
				break;
			case Types.LONGVARBINARY:
				colInfo.put("columnType", "LONGVARBINARY");
				break;
			case Types.LONGVARCHAR:
				colInfo.put("columnType", "LONGVARCHAR");
				break;
			case Types.NULL:
				colInfo.put("columnType", "NULL");
				break;
			case Types.NUMERIC:
				colInfo.put("columnType", "NUMERIC");
				break;
			case Types.OTHER:
				colInfo.put("columnType", "OTHER");
				break;
			case Types.REAL:
				colInfo.put("columnType", "REAL");
				break;
			case Types.REF:
				colInfo.put("columnType", "REF");
				break;
			case Types.SMALLINT:
				colInfo.put("columnType", "SMALLINT");
				break;
			case Types.STRUCT:
				colInfo.put("columnType", "STRUCT");
				break;
			case Types.TIME:
				colInfo.put("columnType", "TIME");
				break;
			case Types.TIMESTAMP:
				colInfo.put("columnType", "TIMESTAMP");
				break;
			case Types.TINYINT:
				colInfo.put("columnType", "TINYINT");
				break;
			case Types.VARBINARY:
				colInfo.put("columnType", "VARBINARY");
				break;
			case Types.VARCHAR:
				colInfo.put("columnType", "VARCHAR");
				break;
			}
		} catch (Exception e) {
		}
		return colInfo;
	}

	public String[] getColumnType() {
		return COLUMN_TYPE;
	}

	public Map<String, Object> getColumnList(String owner, String tableName)
			throws Exception {
		Map<String, Object> tableList = new HashMap<String, Object>();
		tableList.put(tableName, tableName);
		return getColCommentList(owner, tableList);
	}

	public Map<String, Object> getColInfoList(String owner, String tableName,
			String type, Map<String, Object> inputData) throws Exception {
		Map<String, Object> tableList = new HashMap<String, Object>();
		tableList.put(tableName, tableName);
		return getColInfoList(owner, tableList, type, inputData);
	}

	public Map<String, Object> getPkColList(String owner, String tableName)
			throws Exception {
		Map<String, Object> tableList = new HashMap<String, Object>();
		tableList.put(tableName, tableName);

		Map<String, Object> colList = new HashMap<String, Object>();
		List<String> list = getPkColList(owner, tableList);
		int nSize = list.size();
		for (int i = 0; i < nSize; i++) {
			colList.put(list.get(i).toString(), list.get(i));
		}
		return colList;
	}

	public void error(String sql, String msg) {
		String tempSql = StringUtil.LTrim(sql);
		String queryId = "";
		if (tempSql.indexOf("*/") > -1) {
			String tempArry[] = StringUtil.split(tempSql, "*/");
			queryId = StringUtil.LRTrim(tempArry[0]);
		} else {
			queryId = "?";
		}
		System.out.println(queryId + " -> " + msg);
	}

	public String getJavaType(String colType, String scale) {
		String strRtn = "";
		try {
			strRtn = TypeMapping.getInstance().getType(colType, scale);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strRtn;
	}

}
