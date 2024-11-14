package cdm.event.common.processor;

import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingSuccess;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * Created by Tradeheader, SL
 *
 * @author gopazoTH
 * @since 18/09/2024
 */

@SuppressWarnings("unused")
public class RelatedPartyRoleMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedPartyRoleMappingProcessor.class);

    public RelatedPartyRoleMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {

        Collection<PartyRoleMapping> partyRoleMappings = getMappings().stream()
                // find all partyTradeInformation.relatedParty mappings
                .filter(m -> synonymPath.getParent().nameStartMatches(m.getXmlPath()))
                // group by relatedParty path index
                .collect(Collectors.groupingBy(this::getKey, collectingAndThen(toList(), this::toPartyRoleMapping)))
                .values();

        partyRoleMappings.forEach(m -> {
            Trade.TradeBuilder tradeBuilder = (Trade.TradeBuilder) parent;

            // Check if m.role is non-null before calling fromDisplayName
            if (m != null && m.role != null) {
                try {
                    // Normalize the XML value to match the enum constant (convert to uppercase and replace spaces/underscores)
                    PartyRoleEnum roleEnum = PartyRoleEnum.fromDisplayName(m.role);

                    // Create the PartyRoleBuilder and add the necessary values
                    PartyRole.PartyRoleBuilder partyRoleBuilder = PartyRole.builder()
                            .setRole(roleEnum);

                    // Check if m.partyReference is non-null before setting it
                    if (m.partyReference != null) {
                        partyRoleBuilder.setPartyReference(ReferenceWithMetaParty.builder()
                                .setExternalReference(m.partyReference));
                    }

                    // Check if m.ownershipPartyReference is non-null before setting it
                    if (m.ownershipPartyReference != null) {
                        partyRoleBuilder.setOwnershipPartyReference(ReferenceWithMetaParty.builder()
                                .setExternalReference(m.ownershipPartyReference));
                    }

                    // Add the PartyRole to the trade
                    tradeBuilder.addPartyRole(partyRoleBuilder);

                    // Update mappings if they are non-null
                    if (m.partyReferenceMapping != null && m.partyReferenceModelPath != null) {
                        updateMappingSuccess(m.partyReferenceMapping, m.partyReferenceModelPath);
                    }
                    if (m.roleMapping != null && m.roleModelPath != null) {
                        updateMappingSuccess(m.roleMapping, m.roleModelPath);
                    }
                }
                catch (IllegalArgumentException e) {
                    // If the value is not a valid enum constant, do nothing and skip this iteration
                    LOGGER.warn("Invalid PartyRoleEnum: " + m.role, e);

                }
            }


        });
    }

    /**
     * Group by path index, e.g. collect all mappings under relatedParty(index)
     * (nonpublicExecutionReport.trade.tradeHeader.partyTradeInformation(0).relatedParty(10))
     */
    private String getKey(Mapping m) {
        int partyTradeInformationIndex = getPathIndex(m, "partyTradeInformation");
        int relatedPartyIndex = getPathIndex(m, "relatedParty");
        return partyTradeInformationIndex + "-" + relatedPartyIndex;
    }

    private int getPathIndex(Mapping m, String elementName) {
        return subPath(elementName, m.getXmlPath())
                .map(Path::getLastElement)
                .map(Path.PathElement::forceGetIndex)
                .orElse(-1);
    }

    private PartyRoleMapping toPartyRoleMapping(List<Mapping> mappings) {
        try {
            if (!mappings.isEmpty()) {
                // Get the xmlPath from the first element in the mappings list
                String xmlPath = mappings.get(0).getXmlPath().toString();

                // Extract the index value from partyTradeInformation(x) in the xmlPath
                Matcher matcher = Pattern.compile(".*partyTradeInformation\\((\\d+)\\).*").matcher(xmlPath);

                if (matcher.find()) {
                    String index = matcher.group(1);  // This captures the index (x) from partyTradeInformation(x)

                    // Dynamically build the pattern using the extracted index
                    Pattern dynamicPattern = Pattern.compile(".*partyTradeInformation\\(" + index + "\\)\\.partyReference.*");

                    // Apply the dynamic pattern to filter mappings
                    Mapping ownershipPartyReferenceMapping = getMapping(
                            getMappings().stream()
                                    .filter(m -> m.getXmlValue() != null && dynamicPattern.matcher(m.getXmlPath().toString()).matches())
                                    .collect(Collectors.toList()),
                            Path.parse("partyReference.href")
                    );

                    // Proceed with your existing mapping logic
                    RosettaPath ownershipPartyReferenceModelPath = getModelPath().newSubPath("ownershipPartyReference").newSubPath("externalReference");
                    Mapping partyReferenceMapping = getMapping(mappings.stream().filter(m -> m.getXmlValue() != null).collect(Collectors.toList()), Path.parse("partyReference.href"));
                    RosettaPath partyReferenceModelPath = getModelPath().newSubPath("partyReference").newSubPath("externalReference");
                    Mapping roleMapping = getMapping(mappings.stream().filter(m -> m.getXmlValue() != null).collect(Collectors.toList()), Path.parse("role"));
                    RosettaPath roleModelPath = getModelPath().newSubPath("role");

                    // Build and return the PartyRoleMapping if the mappings are valid
                    if (partyReferenceMapping != null && partyReferenceMapping.getXmlValue() != null
                            && roleMapping != null && roleMapping.getXmlValue() != null) {                        return new PartyRoleMapping(
                            String.valueOf(ownershipPartyReferenceMapping.getXmlValue()),
                            ownershipPartyReferenceMapping,
                            ownershipPartyReferenceModelPath,
                            String.valueOf(partyReferenceMapping.getXmlValue()),
                            partyReferenceMapping,
                            partyReferenceModelPath,
                            String.valueOf(roleMapping.getXmlValue()),
                            roleMapping,
                            roleModelPath
                    );
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to build party role mapping from mappings {}", mappings, e);
        }
        return null;
    }


    private Mapping getMapping(List<Mapping> mappings, Path endsWith) {
        return mappings.stream().filter(m -> m.getXmlPath().endsWith(endsWith)).findFirst().orElse(null);
    }

    private static class PartyRoleMapping {

        private final String ownershipPartyReference;
        private final Mapping ownershipPartyReferenceMapping;
        private final RosettaPath ownershipPartyReferenceModelPath;
        private final String partyReference;
        private final Mapping partyReferenceMapping;
        private final RosettaPath partyReferenceModelPath;
        private final String role;
        private final Mapping roleMapping;
        private final RosettaPath roleModelPath;

        public PartyRoleMapping(String ownershipPartyReference, Mapping ownershipPartyReferenceMapping, RosettaPath ownershipPartyReferenceModelPath,String partyReference, Mapping partyReferenceMapping, RosettaPath partyReferenceModelPath, String role, Mapping roleMapping, RosettaPath roleModelPath) {
            this.ownershipPartyReference = ownershipPartyReference;
            this.ownershipPartyReferenceMapping = ownershipPartyReferenceMapping;
            this.ownershipPartyReferenceModelPath = ownershipPartyReferenceModelPath;
            this.partyReference = partyReference;
            this.partyReferenceMapping = partyReferenceMapping;
            this.partyReferenceModelPath = partyReferenceModelPath;
            this.role = role;
            this.roleMapping = roleMapping;
            this.roleModelPath = roleModelPath;
        }
    }
}