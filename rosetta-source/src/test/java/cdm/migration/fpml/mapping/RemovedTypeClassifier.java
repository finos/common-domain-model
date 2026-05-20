package cdm.migration.fpml.mapping;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cdm.migration.fpml.model.RosettaModelInventory;
import cdm.migration.fpml.model.RosettaTypeInfo;

public class RemovedTypeClassifier {
    public Map<String, RemovedTypeClassification> classifyRemovedTypes(RosettaModelInventory oldModel, RosettaModelInventory newModel) {
        Map<String, RemovedTypeClassification> result = new HashMap<String, RemovedTypeClassification>();
        Set<String> newTypes = new HashSet<String>(newModel.typeByQualifiedName.keySet());
        for (RosettaTypeInfo oldType : oldModel.types) {
            if (!newTypes.contains(oldType.qualifiedName)) {
                result.put(oldType.qualifiedName, classify(oldType.simpleName));
            }
        }
        return result;
    }

    private RemovedTypeClassification classify(String simpleName) {
        if (simpleName == null) {
            return RemovedTypeClassification.UNKNOWN;
        }
        if (simpleName.endsWith("Sequence")
                || simpleName.matches(".*Sequence\\d+")
                || simpleName.endsWith("Choice")
                || simpleName.endsWith("Group")
                || simpleName.endsWith("Attributes")
                || simpleName.endsWith("AttributeGroup")) {
            return RemovedTypeClassification.LIKELY_WRAPPER;
        }
        if (simpleName.endsWith("Model")) {
            return RemovedTypeClassification.POSSIBLE_WRAPPER;
        }
        if (simpleName.endsWith("Type")) {
            return RemovedTypeClassification.POSSIBLE_DOMAIN_TYPE;
        }
        return RemovedTypeClassification.UNKNOWN;
    }
}
