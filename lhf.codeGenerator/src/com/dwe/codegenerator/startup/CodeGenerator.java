package com.dwe.codegenerator.startup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.dwe.codegenerator.common.db.Database;
import com.dwe.codegenerator.common.descriptor.AttributeDescriptor;
import com.dwe.codegenerator.common.descriptor.ClassDescriptor;
import com.dwe.codegenerator.common.descriptor.OracleMetaDataDescriptor;
import com.dwe.codegenerator.common.extractor.TableListService;
import com.dwe.codegenerator.common.util.DateUtil;
import com.dwe.codegenerator.common.util.GeneratorUtil;
import com.dwe.codegenerator.common.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class CodeGenerator {

	private static final String ENDFIX_PKCLASS = "PK";

	private static final String TEMPLATE_DIR = "template/spring.ibatis";

	private static final String OUTPUT_DIR = "outfile" + java.io.File.separator;

	private OracleMetaDataDescriptor service = new OracleMetaDataDescriptor();

	private TableListService tableListService = new TableListService();

	private List<AttributeDescriptor> attributeList = new ArrayList<AttributeDescriptor>();

	private List<AttributeDescriptor> attributeList2 = new ArrayList<AttributeDescriptor>();

	private List<AttributeDescriptor> pkAttributeList = new ArrayList<AttributeDescriptor>();

	private ClassDescriptor classDesc = new ClassDescriptor();

	private ClassDescriptor pkClassDesc = new ClassDescriptor();

	private String owner = "";

	private Connection conn = null;

	public void start() throws Exception {
		try {
			String projectName = "";
			String programId = "";
			String packageName = "";
			String domainName = "";
			String sql = "";

			BufferedReader in = null;
			try {
				int Cnt = 1;
				String ArrValue[] = null;
				in = new BufferedReader(new FileReader(
						System.getProperty("user.dir")
								+ "/properties/query.properties"));
				String s;
				while ((s = in.readLine()) != null) {
					if (Cnt <= 4) {
						ArrValue = s.split("=");
						if (ArrValue[0].equals("PROJECT_NAME")) {
							projectName = ArrValue[1];
						}
						if (ArrValue[0].equals("PROGRAM_ID")) {
							programId = ArrValue[1];
						}
						if (ArrValue[0].equals("PACKAGE_NAME")) {
							packageName = ArrValue[1];
						}
						if (ArrValue[0].equals("DOMAIN_NAME")) {
							domainName = ArrValue[1];
						}
					} else {
						if (Cnt > 5)
							sql += " " + s;
						else
							sql += s;
					}
					Cnt++;
				}
			} catch (IOException e) {
				System.exit(1);
			} finally {
				in.close();
			}

			try {
				Properties props = new Properties();
				props.load(ClassLoader.getSystemClassLoader()
						.getResourceAsStream("database.properties"));

				String url = props.getProperty("url");
				String driver = props.getProperty("driver");
				String username = props.getProperty("username");
				String password = props.getProperty("password");
				owner = props.getProperty("owner");

				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url, username, password);

				service.setJDBCConnection(conn);
				extractTableFromSql(sql);
			} catch (Exception e) {
				throw e;
			}

			classDesc.setTableName(sql);
			classDesc.setProgramId(programId);
			classDesc.setPackageName(packageName);
			classDesc.setName(domainName);
			classDesc.setAttributes(attributeList2);

			pkClassDesc.setPackageName(packageName);
			pkClassDesc.setName(domainName + ENDFIX_PKCLASS);
			pkClassDesc.setAttributes(pkAttributeList);

			Properties props = new Properties();
			props.load(new FileInputStream(new File(TEMPLATE_DIR,
					"_file.mapping.properties")));

			Enumeration enumer = props.keys();
			while (enumer.hasMoreElements()) {
				String key = (String) enumer.nextElement();
				String value = props.getProperty(key).trim();
				String[] valueArray = value.split("[,]");
				String templateFileName = key;
				String generatedFileType = valueArray[0].trim();
				String generatedFileName = valueArray[1].trim();

				generatorFile(projectName, generatedFileType, templateFileName,
						packageName, programId, generatedFileName);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Database.close(conn);
		}
	}

	private void extractTableFromSql(String sql) {
		try {
			Map<String, Object> tableList = tableListService.getTableList(sql);
			String tableIds = StringUtil.getText(tableList.get("tableId"));
			String tableId[] = tableIds.split("\\^");
			for (int i = 0; i < tableId.length; i++) {
				extractColumnInfoFromTable(StringUtil.getText(tableId[i]));
			}

			ResultSet rs = null;
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					Iterator dataIterator2 = attributeList.iterator();
					while (dataIterator2.hasNext()) {
						AttributeDescriptor colInfo = (AttributeDescriptor) dataIterator2
								.next();
						if (rsmd.getColumnLabel(i).equals(colInfo.getOrgname())) {
							attributeList2.add(colInfo);
							break;
						}
					}
				}
			} finally {
				Database.close(rs);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void extractColumnInfoFromTable(String tableId) {
		String tableDesc = "";
		Map<String, Object> tableInfo = service.getTableDataList(owner,
				"TABLE", tableId);

		Iterator keys = tableInfo.keySet().iterator();
		while (keys.hasNext()) {
			AttributeDescriptor colInfo = new AttributeDescriptor();
			boolean isPK = false;
			String col = (String) keys.next();
			Map<String, Object> columnInfo = (Map<String, Object>) tableInfo
					.get(col);
			Iterator keys2 = columnInfo.keySet().iterator();
			while (keys2.hasNext()) {
				String col2 = (String) keys2.next();
				String val = StringUtil.getText(columnInfo.get(col2));
				if (col2.equals("tableName")
						&& StringUtil.getText(classDesc.getTableId())
								.equals("")) {
					classDesc.setTableId(tableId);
				} else if (col2.equals("tableComment")
						&& StringUtil.getText(classDesc.getTableDesc()).equals(
								"")) {
					classDesc.setTableDesc(tableDesc);
				} else if (col2.equals("label")) {
					colInfo.setName(val);
				} else if (col2.equals("columnName")) {
					colInfo.setOrgname(val);
				} else if (col2.equals("comment")) {
					colInfo.setDesc(val);
				} else if (col2.equals("columnType")) {
					colInfo.setSqlType(val);
				} else if (col2.equals("typeName")) {
					colInfo.setJavaType(val);
				} else if (col2.equals("displaySize")) {
					colInfo.setSqlLength(val);
				} else if (col2.equals("scale")) {
					colInfo.setSqlLength(val);
				} else if (col2.equals("key")) {
					if (val.equals("true")) {
						isPK = true;
					}
				}
			}
			attributeList.add(colInfo);
			if (isPK == true) {
				pkAttributeList.add(colInfo);
			}
		}
	}

	private void generatorFile(String projectName, String generateFileType,
			String templateFileName, String packageName, String programId,
			String generatedFileName) throws Exception {
		Configuration cfg = new Configuration();
		cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_DIR));
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		Template temp = cfg.getTemplate(templateFileName);

		Map<String, Object> root = new HashMap<String, Object>();
		root.put("currentDate", DateUtil.getCurrentDateString("yyyy/MM/dd"));
		root.put("urlMapping",
				GeneratorUtil.buildControllerUrl(packageName, programId));
		root.put("class", classDesc);
		root.put("pkClass", pkClassDesc);

		String lastFolderNm = "";
		if (templateFileName.equals("lhf-domainTemplate.ftl")) {
			lastFolderNm = "domain";
			generatedFileName = generatedFileName.replaceAll("<class.name>",
					classDesc.getName()).replaceAll("<class.name.lowercase>",
					classDesc.getName().toLowerCase());
		} else if (templateFileName.equals("lhf-ibatisTemplate.ftl")) {
			lastFolderNm = "sqlmap";
			generatedFileName = generatedFileName.replaceAll("<class.name>",
					programId.toUpperCase());
		} else if (templateFileName.equals("lhf-serviceTemplate.ftl")) {
			lastFolderNm = "service";
			generatedFileName = generatedFileName.replaceAll("<class.name>",
					programId.toUpperCase());
		} else if (templateFileName.equals("lhf-controlTemplate.ftl")) {
			lastFolderNm = "controller";
			generatedFileName = generatedFileName.replaceAll("<class.name>",
					programId.toUpperCase());
		} else if (templateFileName.equals("lhf-uiTemplate.ftl")) {
			lastFolderNm = "xui";
			generatedFileName = generatedFileName.replaceAll("<class.name>",
					programId.toUpperCase());
		}

		File newDir = new File(OUTPUT_DIR + projectName + File.separator
				+ classDesc.getPackageNamePath() + lastFolderNm);
		if (!newDir.isDirectory()) {
			newDir.mkdirs();
		}
		File newFile = new File(newDir, generatedFileName);

		BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));

		temp.process(root, writer);
		writer.flush();
		writer.close();

		System.out.println(OUTPUT_DIR + generateFileType + File.separator
				+ classDesc.getPackageNamePath() + lastFolderNm
				+ File.separator + generatedFileName + " generated!");
	}

	public static void main(String args[]) throws Exception {
		CodeGenerator generator = new CodeGenerator();
		generator.start();
	}
}