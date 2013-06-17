<?xml version="1.0" encoding="EUC-KR" standalone="no"?>
<!DOCTYPE sqlMap PUBLIC "-//iBATIS.com//DTD SQL Map 2.0//EN" "http://www.ibatis.com/dtd/sql-map-2.dtd">

<sqlMap namespace="${class.programId?upper_case}">

  <typeAlias alias="${class.name?lower_case}" type="${class.packageName}.domain.${class.name?cap_first}"/>

	<resultMap id="rm${class.name?cap_first}" class="java.util.HashMap">
<#list class.attributes as att>
    	<result column="${att.orgname?upper_case}" property="${att.name}" />
</#list>
	</resultMap>

<select id="retrieve${class.name?cap_first}" resultClass="rm${class.name?cap_first}" parameterClass="java.util.Map">
<![CDATA[ /* ${class.programId?upper_case}.retrieve${class.name?cap_first} */ ]]>
 <![CDATA[
 	${class.tableName?cap_first}
  ]]>
 </select>
 
</sqlMap>