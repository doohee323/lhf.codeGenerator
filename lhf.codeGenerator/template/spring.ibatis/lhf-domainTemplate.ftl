package ${class.packageName}.domain;

<#include "./_JavaTemplates.ftl">

public class ${class.name?cap_first} {

<#list class.attributes as att>
    private ${att.javaType} ${att.name};
</#list>

<#list class.attributes as att>

    /**
     * ${att.name} getter
     * @return Returns the ${att.name}
     */
    public ${att.javaType} get${att.name?cap_first}() {
        return this.${att.name};
    }
    /**
     * ${att.name} setter
     * @param ${att.name} The ${att.name} to set.
     */
    public void set${att.name?cap_first}(${att.javaType} ${att.name}) {
        this.${att.name} = ${att.name};
    }
</#list>
}