package cdm.product.asset.processor;

import cdm.product.common.settlement.ResolvablePriceQuantity;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class FeeLegMappingProcessor extends MappingProcessor {

    public FeeLegMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        // find all mappings mapped to this InterestRatePayout
        List<Mapping> legPriceQuantityMappings = filterMappingForModelPath(PathUtils.toPath(getModelPath()));
        // filter to reference mappings
        List<Mapping> legReferenceMappings = legPriceQuantityMappings.stream()
                .filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
                .collect(Collectors.toList());
        // if both protection terms and feeLeg mappings exist, we have a duplicate mapping
        if (containsPathElement(legReferenceMappings, "feeLeg")
                && containsPathElement(legReferenceMappings, "protectionTerms")) {
            // remove duplicate mapping - protection terms
            Path notionalPath = synonymPath.addElement("calculationAmount").addElement("amount");
            List<Mapping> notionalMappings = legPriceQuantityMappings.stream()
                    .filter(m -> m.getXmlPath().nameIndexMatches(notionalPath))
                    .collect(Collectors.toList());
            getMappings().removeAll(notionalMappings);
            legPriceQuantityMappings.removeAll(notionalMappings);
            // remove duplicate mapping - id
            Path idPath = synonymPath.addElement("calculationAmount").addElement("id");
            List<Mapping> idMappings = legPriceQuantityMappings.stream()
                    .filter(m -> m.getXmlPath().nameIndexMatches(idPath))
                    .collect(Collectors.toList());
            if (getMappings().removeAll(idMappings)) {
                // blank out any id / external key mappings from the protection terms
                ((ResolvablePriceQuantity.ResolvablePriceQuantityBuilder) builder).getOrCreateMeta().setExternalKey(null);
            }
            legPriceQuantityMappings.removeAll(idMappings);

            // update remaining mappings to success
            legPriceQuantityMappings.forEach(this::updateMappingSuccess);
        }
    }

    private List<Mapping> filterMappingForModelPath(Path modelPath) {
        return getMappings().stream()
                .filter(m -> m.getRosettaPath() != null)
                .filter(m -> modelPath.fullStartMatches(m.getRosettaPath()))
                .collect(Collectors.toList());
    }

    private boolean containsPathElement(List<Mapping> referenceMappings, String pathElement) {
        return referenceMappings.stream()
                .anyMatch(m -> m.getXmlPath().toString().contains(pathElement));
    }

    private void updateMappingSuccess(Mapping mapping) {
        mapping.setError(null);
        mapping.setCondition(true);
        mapping.setDuplicate(false);
    }
}
