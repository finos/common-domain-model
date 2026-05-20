package cdm.migration.fpml.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RosettaModelInventory {
    public final List<RosettaTypeInfo> types = new ArrayList<RosettaTypeInfo>();
    public final Map<String, RosettaTypeInfo> typeByQualifiedName = new HashMap<String, RosettaTypeInfo>();
    public final Map<String, List<RosettaTypeInfo>> typeBySimpleName = new HashMap<String, List<RosettaTypeInfo>>();

    public void addType(RosettaTypeInfo typeInfo) {
        types.add(typeInfo);
        typeByQualifiedName.put(typeInfo.qualifiedName, typeInfo);
        List<RosettaTypeInfo> list = typeBySimpleName.get(typeInfo.simpleName);
        if (list == null) {
            list = new ArrayList<RosettaTypeInfo>();
            typeBySimpleName.put(typeInfo.simpleName, list);
        }
        list.add(typeInfo);
    }

    public RosettaTypeInfo findType(String namespace, String typeName) {
        if (typeName == null) {
            return null;
        }
        if (typeName.contains(".")) {
            RosettaTypeInfo direct = typeByQualifiedName.get(typeName);
            if (direct != null) {
                return direct;
            }
        }
        if (namespace != null) {
            RosettaTypeInfo byNs = typeByQualifiedName.get(namespace + "." + typeName);
            if (byNs != null) {
                return byNs;
            }
        }
        List<RosettaTypeInfo> bySimple = typeBySimpleName.get(typeName);
        if (bySimple != null && bySimple.size() == 1) {
            return bySimple.get(0);
        }
        return null;
    }
}
