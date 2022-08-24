package cdm.base.staticdata.asset.common.processor;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class TaxonomySourceMappingProcessor extends MappingProcessor {

    public TaxonomySourceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        setValueAndUpdateMappings(synonymPath.addElement("productTypeScheme"),
                xmlValue -> {
                    ((FieldWithMetaString.FieldWithMetaStringBuilder) builder).getOrCreateMeta().setScheme(xmlValue);

                    Optional.ofNullable(getTaxonomySourceEnum(xmlValue))
                            .ifPresent(source -> ((ProductTaxonomy.ProductTaxonomyBuilder) parent).setTaxonomySource(source));
                });
    }

    private TaxonomySourceEnum getTaxonomySourceEnum(String scheme) {
        if (scheme.contains("product-taxonomy")) {
            return TaxonomySourceEnum.ISDA;
        } else if (scheme.contains("iso10962")) {
            return TaxonomySourceEnum.CFI;
        } else if (scheme.contains("emir-contract-type")) {
            return TaxonomySourceEnum.EMIR;
        } else {
            return null;
        }
    }
}