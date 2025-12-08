package cdm.base.staticdata.identifier.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingFail;
import static com.rosetta.model.metafields.FieldWithMetaString.FieldWithMetaStringBuilder;

@SuppressWarnings("unused")
public class LinkIdMappingProcessor extends MappingProcessor {

    public static final List<String> EVENT_LINK_ID_SCHEMES =
            Arrays.asList("http://www.fpml.org/coding-scheme/external/esma-ptrr-compression",
                    "http://www.fpml.org/coding-scheme/external/esma-ptrr-portfolio-rebalancing",
                    "http://www.fpml.org/coding-scheme/external/esma-ptrr-margin-management");

    public static final String TRADE_LINK_ID_SCHEME =
            "http://www.fpml.org/coding-scheme/external/esma-report-tracking-number";

    public LinkIdMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        updateSchemeAndSource(synonymPath, (FieldWithMetaStringBuilder) builder);
    }

    protected void updateSchemeAndSource(Path linkIdSynonymPath, FieldWithMetaStringBuilder builder) {
        setValueAndOptionallyUpdateMappings(linkIdSynonymPath.addElement("linkIdScheme"), linkIdScheme -> {
                    String modelPath = getModelPath().buildPath();
                    if ((EVENT_LINK_ID_SCHEMES.contains(linkIdScheme) && modelPath.contains(".eventIdentifier"))
                            || (TRADE_LINK_ID_SCHEME.equals(linkIdScheme) && modelPath.contains(".tradeIdentifier"))) {
                        // Update scheme
                        builder.getOrCreateMeta().setScheme(linkIdScheme);
                        return true;
                    } else {
                        // If the linkIdScheme does not match, set linkId to null, and update mapping stats to specify that it is not mapped
                        builder.setValue(null);
                        getNonNullMappings(linkIdSynonymPath)
                                .forEach(m -> updateMappingFail(m, "no destination"));
                        return false;
                    }
                },
                getMappings(),
                getModelPath());
    }

    private List<Mapping> getNonNullMappings(Path synonymPath) {
        return getMappings().stream()
                .filter(m -> synonymPath.nameIndexMatches(m.getXmlPath()))
                .collect(Collectors.toList());
    }
}
