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
 * @date    2021-5-6 11:30:26
 */
public class CfgGlobalLoad implements IConfigScript{

    @Override
    public void load() {
        Map<Integer, CfgGlobal> map = new HashMap<>(1);
        map.put(1, new CfgGlobal(6, 0.5f, 100, 0, 3, 500, 5000, -0.75f, 0.3f));
        CfgGlobal.all().clear();
        CfgGlobal.all().putAll(map);
    }
}