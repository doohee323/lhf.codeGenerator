package com.dwe.codegenerator.common.descriptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassDescriptor {

	private String name;

	private String programId;

	private String packageName;

	private String tableName;

	private String tableId;

	private String tableDesc;

	private List<AttributeDescriptor> attributes = new ArrayList<AttributeDescriptor>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		if ((packageName == null) || (packageName.equals(""))) {
			return "";
		}
		return packageName.toLowerCase();
	}

	public String getPackageNamePath() {
		if ((packageName == null) || (packageName.equals(""))) {
			return "";
		}
		String[] packageNameArray = packageName.split("[.]");
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < packageNameArray.length; i++) {
			buf.append(packageNameArray[i]);
			buf.append(File.separator);
		}
		return buf.toString();
	}

	public String getFullName() {
		if ((packageName == null) || (packageName.equals(""))) {
			return name;
		}
		return packageName.toLowerCase() + "." + name;
	}

	public List<AttributeDescriptor> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeDescriptor> attributes) {
		this.attributes = attributes;
	}

	public String getProgramId() {
		return programId;
	}

	public void setProgramId(String programId) {
		this.programId = programId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
}