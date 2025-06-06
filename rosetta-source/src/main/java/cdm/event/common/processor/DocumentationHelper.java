package cdm.event.common.processor;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.legaldocumentation.common.*;
import cdm.legaldocumentation.common.metafields.FieldWithMetaContractualDefinitionsEnum;
import cdm.legaldocumentation.contract.processor.PartyMappingHelper;
import cdm.legaldocumentation.master.MasterAgreementTypeEnum;
import cdm.legaldocumentation.master.MasterConfirmationAnnexTypeEnum;
import cdm.legaldocumentation.master.MasterConfirmationTypeEnum;
import cdm.product.collateral.CreditSupportAgreementTypeEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.SynonymToEnumMap;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.filterListMappings;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndUpdateMappings;

public class DocumentationHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentationHelper.class);
    private final RosettaPath rosettaPath;
    private final MappingContext mappingContext;
    private final List<Mapping> mappings;
    private final SynonymToEnumMap synonymToEnumMap;
    private final ExecutorService executor;
    private final List<CompletableFuture<?>> invokedTasks;

    public DocumentationHelper(RosettaPath rosettaPath, MappingContext mappingContext) {
        this.rosettaPath = rosettaPath;
        this.mappingContext = mappingContext;
        this.mappings = mappingContext.getMappings();
        this.executor = mappingContext.getExecutor();
        this.invokedTasks = mappingContext.getInvokedTasks();
        this.synonymToEnumMap = mappingContext.getSynonymToEnumMap();
    }

    public List<LegalAgreement> getDocumentation(Path synonymPath) {
        List<LegalAgreement> documentation = new ArrayList<>();
        getMasterAgreement(synonymPath).ifPresent(documentation::add);
        getMasterConfirmation(synonymPath).ifPresent(documentation::add);
        getBrokerConfirmation(synonymPath).ifPresent(documentation::add);
        getCreditSupportAgreement(synonymPath).ifPresent(documentation::add);
        getConfirmation(synonymPath).ifPresent(documentation::add);
        getOtherAgreement(synonymPath).ifPresent(documentation::add);
        return documentation;
    }

    private Optional<LegalAgreement> getMasterAgreement(Path synonymPath) {
        Path masterAgreementPath = synonymPath.addElement("masterAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path masterAgreementTypePath = masterAgreementPath.addElement("masterAgreementType");
        setValueAndUpdateMappings(masterAgreementTypePath,
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    synonymToEnumMap.getEnumValueOptional(MasterAgreementTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterAgreementTypeValue);
                },
                mappings,
                rosettaPath);
        setValueAndUpdateMappings(masterAgreementTypePath.addElement("masterAgreementTypeScheme"),
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterAgreementType().getOrCreateMeta().setScheme(xmlValue);
                },
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(masterAgreementPath.addElement("masterAgreementVersion"),
                xmlValue -> builder.getOrCreateLegalAgreementIdentification().setVintage(Integer.valueOf(xmlValue)),
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(masterAgreementPath.addElement("masterAgreementDate"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)),
                mappings,
                rosettaPath);

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.MASTER_AGREEMENT);
    }

    private Optional<LegalAgreement> getMasterConfirmation(Path synonymPath) {
        Path masterConfirmationPath = synonymPath.addElement("masterConfirmation");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path masterConfirmationTypePath = masterConfirmationPath.addElement("masterConfirmationType");
        setValueAndUpdateMappings(masterConfirmationTypePath,
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    synonymToEnumMap.getEnumValueOptional(MasterConfirmationTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterConfirmationTypeValue);
                },
                mappings,
                rosettaPath);
        setValueAndUpdateMappings(masterConfirmationTypePath.addElement("masterConfirmationTypeScheme"),
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterConfirmationType().getOrCreateMeta().setScheme(xmlValue);
                },
                mappings,
                rosettaPath);

        Path masterConfirmationAnnexTypePath = masterConfirmationPath.addElement("masterConfirmationAnnexType");
        setValueAndUpdateMappings(masterConfirmationAnnexTypePath,
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    synonymToEnumMap.getEnumValueOptional(MasterConfirmationAnnexTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterConfirmationAnnexTypeValue);
                },
                mappings,
                rosettaPath);
        setValueAndUpdateMappings(masterConfirmationAnnexTypePath.addElement("masterConfirmationAnnexTypeScheme"),
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterConfirmationAnnexType().getOrCreateMeta().setScheme(xmlValue);
                },
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(masterConfirmationPath.addElement("masterConfirmationDate"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)),
                mappings,
                rosettaPath);

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.MASTER_CONFIRMATION);
    }

    private Optional<LegalAgreement> getBrokerConfirmation(Path synonymPath) {
        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.BROKER_CONFIRMATION);
    }

    private Optional<LegalAgreement> getCreditSupportAgreement(Path synonymPath) {
        Path creditSupportAgreementPath = synonymPath.addElement("creditSupportAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path creditSupportAgreementTypePath = creditSupportAgreementPath.addElement("type");
        setValueAndUpdateMappings(creditSupportAgreementTypePath,
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    synonymToEnumMap.getEnumValueOptional(CreditSupportAgreementTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setCreditSupportAgreementTypeValue);
                },
                mappings,
                rosettaPath);
        setValueAndUpdateMappings(creditSupportAgreementTypePath.addElement("creditSupportAgreementTypeScheme"),
                xmlValue -> {
                    AgreementName.AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    agreementName.getOrCreateCreditSupportAgreementType().getOrCreateMeta().setScheme(xmlValue);
                },
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(creditSupportAgreementPath.addElement("date"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)),
                mappings,
                rosettaPath);

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.CREDIT_SUPPORT_AGREEMENT);
    }

    private Optional<LegalAgreement> getConfirmation(Path synonymPath) {
        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        filterListMappings(mappings, synonymPath.addElement("contractualDefinitions")).stream()
                .filter(m -> m.getXmlValue() != null)
                .forEach(m -> {
                    FieldWithMetaContractualDefinitionsEnum.FieldWithMetaContractualDefinitionsEnumBuilder contractualDefinitionsBuilder = FieldWithMetaContractualDefinitionsEnum.builder();

                    Path contractualDefinitionsPath = m.getXmlPath();
                    setValueAndUpdateMappings(contractualDefinitionsPath,
                            xmlValue -> synonymToEnumMap.getEnumValueOptional(ContractualDefinitionsEnum.class, xmlValue)
                                    .ifPresent(contractualDefinitionsBuilder::setValue),
                            mappings,
                            rosettaPath);
                    setValueAndUpdateMappings(contractualDefinitionsPath.addElement("contractualDefinitionsScheme"),
                            xmlValue -> contractualDefinitionsBuilder.getOrCreateMeta().setScheme(xmlValue),
                            mappings,
                            rosettaPath);

                    if (contractualDefinitionsBuilder.hasData()) {
                        builder.getOrCreateLegalAgreementIdentification()
                                .getOrCreateAgreementName()
                                .addContractualDefinitionsType(contractualDefinitionsBuilder);
                    }
                });

        // find all contractualMatrix list items
        filterListMappings(mappings, synonymPath.addElement("contractualMatrix"))
                // for each item, check if matrixType/matrixTerm is mapped
                .forEach(m -> {
                    AgreementName.AgreementNameBuilder agreementName =
                            builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();
                    ContractualMatrix.ContractualMatrixBuilder contractualMatrixBuilder = ContractualMatrix.builder();

                    Path matrixTypePath = m.getXmlPath().addElement("matrixType");
                    setValueAndUpdateMappings(matrixTypePath,
                            xmlValue -> synonymToEnumMap.getEnumValueOptional(MatrixTypeEnum.class, xmlValue)
                                    .ifPresent(contractualMatrixBuilder::setMatrixTypeValue),
                            mappings,
                            rosettaPath);
                    setValueAndUpdateMappings(matrixTypePath.addElement("matrixTypeScheme"),
                            xmlValue -> contractualMatrixBuilder.getOrCreateMatrixType().getOrCreateMeta().setScheme(xmlValue),
                            mappings,
                            rosettaPath);

                    Path matrixTermPath = m.getXmlPath().addElement("matrixTerm");
                    setValueAndUpdateMappings(matrixTermPath,
                            xmlValue -> synonymToEnumMap.getEnumValueOptional(MatrixTermEnum.class, xmlValue)
                                    .ifPresent(contractualMatrixBuilder::setMatrixTermValue),
                            mappings,
                            rosettaPath);
                    setValueAndUpdateMappings(matrixTermPath.addElement("matrixTermScheme"),
                            xmlValue -> contractualMatrixBuilder.getOrCreateMatrixTerm().getOrCreateMeta().setScheme(xmlValue),
                            mappings,
                            rosettaPath);

                    if (contractualMatrixBuilder.hasData()) {
                        agreementName.addContractualMatrix(contractualMatrixBuilder);
                    }
                });

        // find all contractualTermsSupplement list items
        filterListMappings(mappings, synonymPath.addElement("contractualTermsSupplement"))
                // for each item, check if type is mapped
                .forEach(m -> {
                    AgreementName.AgreementNameBuilder agreementName =
                            builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName();

                    ContractualTermsSupplement.ContractualTermsSupplementBuilder contractualTermsSupplementBuilder = ContractualTermsSupplement.builder();

                    Path typePath = m.getXmlPath().addElement("type");
                    setValueAndUpdateMappings(typePath,
                            xmlValue -> synonymToEnumMap.getEnumValueOptional(ContractualSupplementTypeEnum.class, xmlValue)
                                    .ifPresent(contractualTermsSupplementBuilder::setContractualTermsSupplementTypeValue),
                            mappings,
                            rosettaPath);
                    setValueAndUpdateMappings(typePath.addElement("contractualSupplementScheme"),
                            xmlValue -> contractualTermsSupplementBuilder.getOrCreateContractualTermsSupplementType().getOrCreateMeta().setScheme(xmlValue),
                            mappings,
                            rosettaPath);

                    setValueAndUpdateMappings(m.getXmlPath().addElement("publicationDate"),
                            xmlValue -> contractualTermsSupplementBuilder.setPublicationDate(parseDate(xmlValue)),
                            mappings,
                            rosettaPath);

                    if (contractualTermsSupplementBuilder.hasData()) {
                        agreementName.addContractualTermsSupplement(contractualTermsSupplementBuilder);
                    }
                });

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.CONFIRMATION);
    }


    private Date parseDate(String xmlValue) {
        return Date.parse(xmlValue.replace("Z", ""));
    }

    private Optional<LegalAgreement> getOtherAgreement(Path synonymPath) {
        Path otherAgreementPath = synonymPath.addElement("otherAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        setValueAndUpdateMappings(otherAgreementPath.addElement("type"),
                xmlValue -> builder.getOrCreateLegalAgreementIdentification().getOrCreateAgreementName().setOtherAgreement(xmlValue),
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(otherAgreementPath.addElement("version"),
                xmlValue -> builder.getOrCreateLegalAgreementIdentification().setVintage(Integer.valueOf(xmlValue)),
                mappings,
                rosettaPath);

        setValueAndUpdateMappings(otherAgreementPath.addElement("date"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)),
                mappings,
                rosettaPath);

        if (builder.hasData()) {
            setContractualParty(builder);
        }

        return setAgreementType(builder, LegalAgreementTypeEnum.OTHER);
    }

    private Optional<LegalAgreement> setAgreementType(LegalAgreement.LegalAgreementBuilder builder, LegalAgreementTypeEnum masterAgreement) {
        if (builder.hasData()) {
            builder.getOrCreateLegalAgreementIdentification()
                    .getOrCreateAgreementName()
                    .setAgreementType(masterAgreement);
            return Optional.of(builder);
        } else {
            return Optional.empty();
        }
    }

    private void setContractualParty(LegalAgreement.LegalAgreementBuilder builder) {
        PartyMappingHelper.getInstance(mappingContext).ifPresent(helper -> {
            LOGGER.debug("Waiting for counterparties to be collected before updating contractual parties");
            // wait until both counterparties have been collected before getting party references.
            // also, add task to invokedTasks so the mapping process does not get shutdown prematurely.
            invokedTasks.add(helper.getBothCounterpartiesCollectedFuture().thenAcceptAsync(counterpartyMap -> {
                Set<String> counterpartyExternalRefs = counterpartyMap.keySet();
                LOGGER.info("Setting contractual party references {}", counterpartyExternalRefs);
                List<ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder> contractualParties =
                        counterpartyExternalRefs.stream()
                                .map(counterpartyRef ->
                                        ReferenceWithMetaParty.builder().setExternalReference(counterpartyRef))
                                .collect(Collectors.toList());
                builder.setContractualParty(contractualParties);
            }, executor));
        });
    }
}
