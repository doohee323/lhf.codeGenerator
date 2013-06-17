     <Dataset id="ds_${class.name?cap_first}" preload="true" firefirstcount="0" firenextcount="0" useclientlayout="true" updatecontrol="true" enableevent="true">
        <ColumnInfo>
<#list class.attributes as att>
<#if att.name != "OGG_CD" && att.name != "OGG_TIME">
<#if att.sqlType == "NUMERIC" || att.sqlType == "INTEGER" || att.sqlType == "NUMBER" || att.sqlType == "FLOAT" || att.sqlType == "DOUBLE" || att.sqlType == "DECIMAL">
		  <Column id="${att.name}" size="256" type="BIGDECIMAL" />
<#elseif att.sqlType == "DATE" ||  att.sqlType == "TIMESTAMP" ||  att.sqlType == "TIMESTAMP(6)">
		  <Column id="${att.name}" size="256" type="STRING" />
<#else>
		  <Column id="${att.name}" size="256" type="STRING" />
</#if>
</#if>
</#list>
        </ColumnInfo>
      </Dataset>
  
    <Bind>
<#assign row = 1>
<#list class.attributes as att>
<#if att.name != "OGG_CD" && att.name != "OGG_TIME">
	<BindItem id="item${row}" compid="div_Main.edt_${att.name?cap_first}" propid="value" datasetid="ds_${class.name?cap_first}" columnid="${class.name}"/>	
<#assign row = row+1?int>		  
</#if>
</#list>    
    </Bind>
     
	<Grid id="grd_List" taborder="8" binddataset="ds_${class.name?cap_first}" autoenter="select" useinputpanel="false" autofittype="col" position="absolute 0 96 989 564">
          <Formats>
            <Format id="default">
              <Columns>
<#list class.attributes as att>
<#if att.name != "OGG_CD" && att.name != "OGG_TIME">
		  		<Column size="80"/>
</#if>
</#list>   
              </Columns>
              <Rows>
                <Row size="24" band="head"/>
                <Row size="20"/>
              </Rows>
              <Band id="head">
                <Cell displaytype="checkbox" edittype="checkbox" text="0"/>
<#assign row = 1>
<#list class.attributes as att>
<#if att.name != "OGG_CD" && att.name != "OGG_TIME">
		  		<Cell col="${row}" text="${att.desc}"/>
<#assign row = row+1?int>		
</#if>
</#list> 
              </Band>
              <Band id="body">
                <Cell displaytype="checkbox" edittype="checkbox" expr="expr:v_ChkGrdList[currow]"/>
<#assign row = 1>
<#list class.attributes as att>
<#if att.name != "OGG_CD" && att.name != "OGG_TIME">
		  		<Cell col="${row}" edittype="text" text="bind:${att.name}" editautoselect="true" editlengthunit="utf8"/>
<#assign row = row+1?int>		
</#if>
</#list>                 
              </Band>
            </Format>
          </Formats>
        </Grid>