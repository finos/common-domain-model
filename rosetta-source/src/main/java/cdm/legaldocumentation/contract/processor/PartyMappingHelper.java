package cdm.legaldocumentation.contract.processor;

import cdm.base.staticdata.party.AncillaryParty;
import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.product.template.TradableProduct.TradableProductBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cdm.base.staticdata.party.AncillaryParty.AncillaryPartyBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Helper class for FpML mapper processors.
 * <p>
 * Handles Counterparty and AncillaryParty.  A new instance is created for each TradableProduct.
 */
public class PartyMappingHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PartyMappingHelper.class);

    static final String PARTY_MAPPING_HELPER_KEY = "PARTY_MAPPING_HELPER";

    private final Map<String, CounterpartyRoleEnum> partyExternalReferenceToCounterpartyRoleEnumMap;
    private final List<Mapping> mappings;
    private final CompletableFuture<Map<String, CounterpartyRoleEnum>> bothCounterpartiesCollected = new CompletableFuture<>();
    private final TradableProductBuilder tradableProductBuilder;
    private final Function<String, Optional<String>> translator;
    private final ExecutorService executor;
    private final List<CompletableFuture<?>> invokedTasks;

    PartyMappingHelper(MappingContext context, TradableProductBuilder tradableProductBuilder, Function<String, Optional<String>> translator) {
        this.mappings = context.getMappings();
        this.executor = context.getExecutor();
        this.invokedTasks = context.getInvokedTasks();
        this.tradableProductBuilder = tradableProductBuilder;
        this.translator = translator;
        this.partyExternalReferenceToCounterpartyRoleEnumMap = new LinkedHashMap<>();
    }

    /**
     * Get an instance of this party mapping helper from the MappingContext params.
     */
    public synchronized static Optional<PartyMappingHelper> getInstance(MappingContext mappingContext) {
        return Optional.ofNullable((PartyMappingHelper) mappingContext.getMappingParams().get(PARTY_MAPPING_HELPER_KEY));
    }

    /**
     * Get an instance of this party mapping helper from the MappingContext params, or if not found throw exception.
     */
    public synchronized static PartyMappingHelper getInstanceOrThrow(MappingContext mappingContext) {
        return getInstance(mappingContext).orElseThrow(() -> new IllegalStateException("PartyMappingHelper not found."));
    }

    /**
     * Looks up party external reference synonym path, and maps value to CounterpartyRoleEnum, then sets on the given builder.
     */
    public void setCounterpartyRoleEnum(RosettaPath modelPath, Path synonymPath, Consumer<CounterpartyRoleEnum> setter) {
        setValueAndOptionallyUpdateMappings(
                synonymPath.addElement("href"), // synonym path to party external reference
                (partyExternalReference) -> {
                    // Map externalRef to CounterpartyRoleEnum and update builder object
                    String partyReference = translatePartyExternalReference(partyExternalReference);
                    Optional<CounterpartyRoleEnum> counterpartyEnum = getOrCreateCounterpartyRoleEnum(partyReference);
                    counterpartyEnum.ifPresent(setter);
                    return counterpartyEnum.isPresent(); // return true to update synonym mapping stats to success
                },
                mappings,
                modelPath);
    }

    /**
     * Maps party external reference to CounterpartyRoleEnum, then sets on the given builder.
     */
    public void setCounterpartyRoleEnum(String partyReference, Consumer<CounterpartyRoleEnum> setter) {
        // Map externalRef to CounterpartyRoleEnum and update builder object
        getOrCreateCounterpartyRoleEnum(translatePartyExternalReference(partyReference))
                .ifPresent(setter);
    }

    /**
     * Apply party external reference translation if translator provided
     */
    public String translatePartyExternalReference(String partyExternalReference) {
        return Optional.ofNullable(translator)
                .flatMap(t -> t.apply(partyExternalReference))
                .orElse(partyExternalReference);
    }

    /**
     * Waits until the partyExternalReference to CounterpartyRoleEnum map is ready, then adds both Counterparty instances to TradableProductBuilder.
     */
    void addCounterparties() {
        // when the tradableProductBuilder has been supplied and both counterparties have been collected, update tradableProduct.counterparties
        invokedTasks.add(bothCounterpartiesCollected
                .thenAcceptAsync(map -> {
                    LOGGER.info("Setting TradableProduct.counterparty");
                    tradableProductBuilder
                            .setCounterparty(map.entrySet().stream()
                                    .map(extRefCounterpartyEntry -> Counterparty.builder()
                                            .setRole(extRefCounterpartyEntry.getValue())
                                            .setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(extRefCounterpartyEntry.getKey()))
                                            .build())
                                    .collect(Collectors.toList()));
                }, executor));
    }

    public CompletableFuture<Map<String, CounterpartyRoleEnum>> getBothCounterpartiesCollectedFuture() {
        return bothCounterpartiesCollected;
    }

    /**
     * Looks up externalReference in the map and returns the corresponding CounterpartyRoleEnum.
     */
    private Optional<CounterpartyRoleEnum> getOrCreateCounterpartyRoleEnum(String externalReference) {
        synchronized (partyExternalReferenceToCounterpartyRoleEnumMap) {
            Optional<CounterpartyRoleEnum> counterpartyEnum = Optional.ofNullable(partyExternalReferenceToCounterpartyRoleEnumMap.computeIfAbsent(
                    externalReference,
                    (key) -> {
                        if (partyExternalReferenceToCounterpartyRoleEnumMap.isEmpty()) {
                            LOGGER.info("Adding CounterpartyRoleEnum.PARTY_1 for {}", externalReference);
                            return CounterpartyRoleEnum.PARTY_1;
                        } else if (partyExternalReferenceToCounterpartyRoleEnumMap.size() == 1) {
                            LOGGER.info("Adding CounterpartyRoleEnum.PARTY_2 for {}", externalReference);
                            return CounterpartyRoleEnum.PARTY_2;
                        } else {
                            return null;
                        }
                    }));
            // If both counterparties have been added to the map, then complete the future
            if (!bothCounterpartiesCollected.isDone() && partyExternalReferenceToCounterpartyRoleEnumMap.size() == 2) {
                LOGGER.debug("Both counterparties collected");
                bothCounterpartiesCollected.complete(partyExternalReferenceToCounterpartyRoleEnumMap);
            }
            return counterpartyEnum;
        }
    }

    /**
     * Set role with given setter and add associated partyExternalReference to tradableProduct.ancillaryParty.
     */
    public void setAncillaryRoleEnum(RosettaPath modelPath, Path synonymPath, Consumer<AncillaryRoleEnum> setter, AncillaryRoleEnum role) {
        setValueAndUpdateMappings(synonymPath.addElement("href"),
                partyExternalReference -> {
                    String translatedPartyRef = translatePartyExternalReference(partyExternalReference);
                    LOGGER.info("Adding {} for {}", role, translatedPartyRef);
                    setter.accept(role);
                    // add to tradableProduct
                    addAncillaryParty(translatedPartyRef, role);
                },
                mappings,
                modelPath);
    }

    /**
     * Add role and associated partyExternalReference to tradableProduct.ancillaryParty.
     */
    public void addAncillaryParty(String partyExternalReference, AncillaryRoleEnum role) {
        synchronized (tradableProductBuilder) {
            LOGGER.info("Adding {} as {} to TradableProduct.ancillaryParty", partyExternalReference, role);
            @SuppressWarnings("unchecked")
            List<AncillaryPartyBuilder> ancillaryParties = (List<AncillaryPartyBuilder>) emptyIfNull(tradableProductBuilder.getAncillaryParty());
            Optional<AncillaryPartyBuilder> ancillaryPartyReference = ancillaryParties.stream()
                    .filter(r -> role == r.getRole())
                    .findFirst();
            if (ancillaryPartyReference.isPresent()) {
                // Update existing entry
                ancillaryPartyReference.get()
                        .addPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference).build());
            } else {
                // Add new entry
                ancillaryParties.add(AncillaryParty.builder()
                        .setRole(role)
                        .addPartyReference(ReferenceWithMetaParty.builder().setExternalReference(partyExternalReference)));
            }
            // Clear ancillary parties, and re-add sorted list so we don't get diffs on the list order on each ingestion
            tradableProductBuilder
                    .setAncillaryParty(ancillaryParties.stream()
                            .map(AncillaryPartyBuilder::build)
                            .sorted(Comparator.comparing(AncillaryParty::getRole))
                            .collect(Collectors.toList()));
        }
    }
}
