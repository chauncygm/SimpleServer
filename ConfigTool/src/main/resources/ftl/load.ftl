/*
* @AUTO GENERATED
* AUTO GENERATED FILE, DON'T TO EDIT.
*/
package cfg;

import java.util.Map;
import java.util.HashMap;

import manager.IConfigScript;

/**
 * @author  gongshengjun
 * @date    ${date?datetime}
 */
public class Cfg${info.name?cap_first}Load implements IConfigScript{

    @Override
    public void load() {
        Map<Integer, Cfg${info.name?cap_first}> map = new HashMap<>(${info.dataInfoList?size});
        <#list info.dataInfoList as col>
        <#assign id = col[0]>
        <#if ["global"]?seq_contains(info.name)><#assign id = 1></#if>
        map.put(${id}, new Cfg${info.name?cap_first}(<#rt>
        <#lt><#list col as cellData>
            <#assign isBase = ["int", "float", "long"]?seq_contains(info.columnInfoList[cellData?counter - 1].type)>
            <#if isBase>${cellData}<#else>"${cellData}"<#t></#if><#sep>, <#t>
        <#lt></#list>));
        </#list>
        Cfg${info.name?cap_first}.all().clear();
        Cfg${info.name?cap_first}.all().putAll(map);
    }
}