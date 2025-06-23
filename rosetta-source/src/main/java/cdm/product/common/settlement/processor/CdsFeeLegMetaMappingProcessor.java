package cdm.product.common.settlement.processor;

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

import static cdm.product.common.settlement.ResolvablePriceQuantity.ResolvablePriceQuantityBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;

@SuppressWarnings("unused")
public class CdsFeeLegMetaMappingProcessor extends MappingProcessor {

    public CdsFeeLegMetaMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path protectionTermsCalculationAmountSynonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        if (!protectionTermsCalculationAmountSynonymPath.endsWith("protectionTerms", "calculationAmount", "amount")) {
            return;
        }
        
        // find all mappings mapped to this InterestRatePayout
        Path modelPath = PathUtils.toPath(getModelPath().getParent());
        List<Mapping> legPriceQuantityMappings = filterMappingForModelPath(modelPath);

        if (isDuplicateInterestRatePayout(modelPath)) {
            // remove mapped data
            ((ResolvablePriceQuantityBuilder) parent).setQuantitySchedule(null);
            // remove mappings
            getMappings().removeAll(legPriceQuantityMappings);
            return;
        }
        
        // filter to reference mappings
        List<Mapping> legReferenceMappings = legPriceQuantityMappings.stream()
                .filter(m -> m.getRosettaValue() instanceof Reference.ReferenceBuilder)
                .collect(Collectors.toList());
        // if both protection terms and feeLeg mappings exist, we have a duplicate mapping
        if (containsPathElement(legReferenceMappings, "feeLeg")
                && containsPathElement(legReferenceMappings, "protectionTerms")) {
            // remove duplicate mapping - protection terms
            Path notionalPath = protectionTermsCalculationAmountSynonymPath;
            List<Mapping> notionalMappings = legPriceQuantityMappings.stream()
                    .filter(m -> m.getXmlPath().nameIndexMatches(notionalPath))
                    .collect(Collectors.toList());
            getMappings().removeAll(notionalMappings);
            legPriceQuantityMappings.removeAll(notionalMappings);
            // remove duplicate mapping - id
            Path idPath = protectionTermsCalculationAmountSynonymPath.getParent().addElement("id");
            List<Mapping> idMappings = legPriceQuantityMappings.stream()
                    .filter(m -> m.getXmlPath().nameIndexMatches(idPath))
                    .collect(Collectors.toList());
            if (getMappings().removeAll(idMappings)) {
                // blank out any id / external key mappings from the protection terms
                ((ResolvablePriceQuantityBuilder) parent).getOrCreateMeta().setExternalKey(null);
            }
            legPriceQuantityMappings.removeAll(idMappings);

            // update remaining mappings to success
            legPriceQuantityMappings.forEach(this::updateMappingSuccess);
        }
    }

    private static Boolean isDuplicateInterestRatePayout(Path modelPath) {
        return subPath("payout", modelPath).map(Path::getLastElement).map(e -> e.forceGetIndex() > 1).orElse(false);
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
