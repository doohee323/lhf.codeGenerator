package com.dwe.codegenerator.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Database {

	public static Connection getConnection() throws Exception {
		Properties props = new Properties();
		props.load(ClassLoader.getSystemClassLoader().getResourceAsStream(
				"database.properties"));

		String url = props.getProperty("url");
		String driver = props.getProperty("driver");
		String username = props.getProperty("username");
		String password = props.getProperty("password");

		Class.forName(driver).newInstance();
		return DriverManager.getConnection(url, username, password);
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
}