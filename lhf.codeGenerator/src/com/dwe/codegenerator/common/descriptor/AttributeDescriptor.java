package com.dwe.codegenerator.common.descriptor;

public class AttributeDescriptor {

	private String name; // COLUMN_NAME

	private String orgname; // COLUMN_NAME

	private String desc; // COLUMN_DESC

	private String javaType; // java type

	private String sqlType; // TYPE_NAME

	private String sqlLength; // COLUMN_SIZE

	private int sqlDecimalDigits; //

	/**
	 * toString methode: creates a String representation of the object
	 * 
	 * @return the String representation
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("AttributeDescriptor[");
		buffer.append("name = ").append(name);
		buffer.append(", desc = ").append(desc);
		buffer.append(", javaType = ").append(javaType);
		buffer.append(", sqlType = ").append(sqlType);
		buffer.append(", sqlLength = ").append(sqlLength);
		buffer.append(", sqlDecimalDigits = ").append(sqlDecimalDigits);
		buffer.append("]");
		return buffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public String getSqlLength() {
		return sqlLength;
	}

	public void setSqlLength(String sqlLength) {
		this.sqlLength = sqlLength;
	}

	public int getSqlDecimalDigits() {
		return sqlDecimalDigits;
	}

	public void setSqlDecimalDigits(int sqlDecimalDigits) {
		this.sqlDecimalDigits = sqlDecimalDigits;
	}

}