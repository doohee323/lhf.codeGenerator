package com.dwe.codegenerator.common.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import com.dwe.codegenerator.common.descriptor.AttributeDescriptor;
import com.dwe.codegenerator.common.util.GeneratorUtil;

public class TableImporter {

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @return
	 * @throws Exception
	 */
	public ArrayList<AttributeDescriptor> tableToArrayList(Connection conn,
			String tableName) throws Exception {
		ArrayList<AttributeDescriptor> list = new ArrayList<AttributeDescriptor>();
		ResultSet rs = null;
		try {
			// DatabaseMetaData dbmd = conn.getMetaData();
			AttributeDescriptor attrDesc = null;

			// rs = dbmd.getColumns(null, null, tableName, "%");

			PreparedStatement pstmt = conn.prepareStatement(tableName);
			rs = pstmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				attrDesc = rsToAttrDesc(rsmd, i);
				list.add(attrDesc);
			}
			attrDesc.toString();
		} finally {
			Database.close(rs);
		}
		return list;
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private AttributeDescriptor rsToAttrDesc(ResultSetMetaData rsmd, int i)
			throws Exception {
		// TABLE_CAT TABLE_SCHEM TABLE_NAME COLUMN_NAME DATA_TYPE TYPE_NAME
		// COLUMN_SIZE BUFFER_LENGTH DECIMAL_DIGITS NUM_PREC_RADIX NULLABLE
		// REMARKS COLUMN_DEF SQL_DATA_TYPE SQL_DATETIME_SUB CHAR_OCTET_LENGTH
		// ORDINAL_POSITION IS_NULLABLE

		AttributeDescriptor attrDesc = new AttributeDescriptor();
		attrDesc.setName(GeneratorUtil.carmelNaming(rsmd.getColumnLabel(i), "_"));
		attrDesc.setOrgname(rsmd.getColumnLabel(i));
		attrDesc.setSqlType(rsmd.getColumnTypeName(i).toUpperCase());
		if (rsmd.getColumnTypeName(i).equals("VARCHAR")
				|| rsmd.getColumnTypeName(i).equals("VARCHAR2")
				|| rsmd.getColumnTypeName(i).equals("CHAR")
				|| rsmd.getColumnTypeName(i).equals("CLOB")
				|| rsmd.getColumnTypeName(i).equals("NCLOB")) {
			attrDesc.setJavaType("String");
		} else if (rsmd.getColumnTypeName(i).equals("FLOAT")
				|| rsmd.getColumnTypeName(i).equals("LONG")
				|| rsmd.getColumnTypeName(i).equals("INTEGER")
				|| rsmd.getColumnTypeName(i).equals("NUMBER")
				|| rsmd.getColumnTypeName(i).equals("NUMERIC")) {
			attrDesc.setJavaType("BigDecimal");
		} else if (rsmd.getColumnTypeName(i).equals("TIMESTAMP")
				|| rsmd.getColumnTypeName(i).equals("DATE")) {
			attrDesc.setJavaType("Date");
		} else {
			attrDesc.setJavaType("String");
		}
		attrDesc.setSqlLength(String.valueOf(rsmd.getColumnDisplaySize(i)));
		attrDesc.setSqlDecimalDigits(rsmd.getScale(i));

		// sqlType --> javaType ��ȯ
		// attrDesc.setType(TypeMapping.getInstance().getType(attrDesc));

		return attrDesc;
	}

	/**
	 * 
	 * @param conn
	 * @param tableName
	 * @param columnList
	 * @return
	 * @throws Exception
	 */
	public ArrayList<AttributeDescriptor> pkToArrayList(Connection conn,
			String tableName, ArrayList<AttributeDescriptor> columnList)
			throws Exception {
		ArrayList<AttributeDescriptor> pkList = new ArrayList<AttributeDescriptor>();
		ResultSet rs = null;
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			AttributeDescriptor attrDesc = null;

			tableName = tableName.toUpperCase().substring(
					tableName.toUpperCase().lastIndexOf("FROM") + 5,
					tableName.length());
			rs = dbmd.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				String name = GeneratorUtil.carmelNaming(rs
						.getString("COLUMN_NAME"));
				for (int i = 0; i < columnList.size(); i++) {
					attrDesc = (AttributeDescriptor) columnList.get(i);

					if (name.equals(attrDesc.getName())) {
						pkList.add(attrDesc);
						break;
					}
				}
			}
		} finally {
			Database.close(rs);
		}
		return pkList;
	}

	public static void main(String[] agrs) {
		Connection conn = null;
		try {
			conn = Database.getConnection();

			TableImporter importer = new TableImporter();

			ArrayList<AttributeDescriptor> columnList = importer
					.tableToArrayList(conn, "DWE_COM_AUTH_MENU");
			for (int i = 0; i < columnList.size(); i++) {
				System.out.println((AttributeDescriptor) columnList.get(i));
			}
			ArrayList<AttributeDescriptor> pkList = importer.pkToArrayList(
					conn, "TBCO700", columnList);
			for (int i = 0; i < pkList.size(); i++) {
				System.out.println("@@@@@@@@@"
						+ (AttributeDescriptor) pkList.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Database.close(conn);
		}

	}
}