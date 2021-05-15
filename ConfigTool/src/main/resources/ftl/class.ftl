/*
 * @AUTO GENERATED
 * AUTO GENERATED FILE, DON'T TO EDIT.
 */
package cfg;

<#list info.importClass as clazz>
import client.struct.${clazz};
</#list>
<#if info.importClass?size gt 0>

</#if>
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author  gongshengjun
 * @date    ${date?datetime}
 */
public class Cfg${info.name?cap_first} {

    private static final long INIT_TIME = System.currentTimeMillis();

    private static final Map<Integer, Cfg${info.name?cap_first}> map = new LinkedHashMap<>();

<#list info.columnInfoList as col>
    /**
     * ${col.desc}
     */
    private final ${col.type} ${col.name};

</#list>

    public Cfg${info.name?cap_first}(<#rt>
<#list info.columnInfoList as col>
    <#if col.type?length gt 5>String<#else>${col.type}</#if> ${col.name}<#sep>, <#t>
</#list>) {
<#list info.columnInfoList as col>
    <#if ["int", "float", "long"]?seq_contains(col.type)>
        this.${col.name} = ${col.name};
    <#elseif col.type?contains("Map")>
        this.${col.name} = new LinkedHashMap<>();
        for (String s : ${col.name}.split("\\|")) {
            if (s.trim().isEmpty()) continue;
        <#if !col.type?contains("ReadArray")>
            <#assign funcName = col.baseType.wrapValue + ".parse" + col.baseType.value?cap_first>
            this.${col.name}.put(Integer.parseInt(s.split(":")[0]), ${funcName}(s.split(":")[1]));
        <#else>
            this.${col.name}.put(Integer.parseInt(s.split(":")[0]), new ${col.baseType.wrapValue}ReadArray(s.split(":")[1], "_"));
        </#if>
        }
    <#elseif col.type?contains("ReadArrayEs")>
        this.${col.name} = new ${col.type}(${col.name}, "\\|", "_");
    <#elseif col.type?contains("ReadArray")>
        this.${col.name} = new ${col.type}(${col.name}, "_");
    </#if>
</#list>
    }

<#list info.columnInfoList as col>
    public ${col.type} get${col.name?cap_first}() {
        return ${col.name};
    }
    <#sep>

</#list>

<#if ["global"]?seq_contains(info.name)>
    public static Cfg${info.name?cap_first} get() {
        return Objects.requireNonNull(map.get(1));
    }
<#else>
    public static Cfg${info.name?cap_first} get(int id) {
        return map.get(id);
    }

    public static Cfg${info.name?cap_first} getNotNull(int id) {
        return Objects.requireNonNull(map.get(id));
    }
</#if>

    public static Map<Integer, Cfg${info.name?cap_first}> all() {
        return map;
    }

}