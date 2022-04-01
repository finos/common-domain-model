package cdm.event.common.processor;

import cdm.legalagreement.common.*;
import cdm.legalagreement.common.AgreementName.AgreementNameBuilder;
import cdm.legalagreement.common.metafields.FieldWithMetaContractualDefinitionsEnum;
import cdm.legalagreement.contract.BrokerConfirmationTypeEnum;
import cdm.legalagreement.csa.CreditSupportAgreementTypeEnum;
import cdm.legalagreement.master.MasterAgreementTypeEnum;
import cdm.legalagreement.master.MasterConfirmationAnnexTypeEnum;
import cdm.legalagreement.master.MasterConfirmationTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cdm.event.common.ContractDetails.ContractDetailsBuilder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

public class DocumentationMappingProcessor extends MappingProcessor {

    public DocumentationMappingProcessor(RosettaPath rosettaPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(rosettaPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        List<LegalAgreement> documentation = new ArrayList<>();
        getMasterAgreement(synonymPath).ifPresent(documentation::add);
        getMasterConfirmation(synonymPath).ifPresent(documentation::add);
        getBrokerConfirmation(synonymPath).ifPresent(documentation::add);
        getCreditSupportAgreement(synonymPath).ifPresent(documentation::add);
        getConfirmation(synonymPath).ifPresent(documentation::add);
        getOtherAgreement(synonymPath).ifPresent(documentation::add);

        ContractDetailsBuilder contractDetailsBuilder = (ContractDetailsBuilder) parent;
        contractDetailsBuilder.setDocumentation(documentation);
    }

    private Optional<LegalAgreement> getMasterAgreement(Path synonymPath) {
        Path masterAgreementPath = synonymPath.addElement("masterAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path masterAgreementTypePath = masterAgreementPath.addElement("masterAgreementType");
        setValueAndUpdateMappings(masterAgreementTypePath,
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    getSynonymToEnumMap().getEnumValueOptional(MasterAgreementTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterAgreementTypeValue);
                });
        setValueAndUpdateMappings(masterAgreementTypePath.addElement("masterAgreementTypeScheme"),
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterAgreementType().getOrCreateMeta().setScheme(xmlValue);
                });

        setValueAndUpdateMappings(masterAgreementPath.addElement("masterAgreementVersion"),
                xmlValue -> builder.getOrCreateLegalAgreementType().setVintage(Integer.valueOf(xmlValue)));

        setValueAndUpdateMappings(masterAgreementPath.addElement("masterAgreementDate"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)));

        return setAgreementType(builder, LegalAgreementTypeEnum.MASTER_AGREEMENT);
    }

    private Optional<LegalAgreement> getMasterConfirmation(Path synonymPath) {
        Path masterConfirmationPath = synonymPath.addElement("masterConfirmation");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path masterConfirmationTypePath = masterConfirmationPath.addElement("masterConfirmationType");
        setValueAndUpdateMappings(masterConfirmationTypePath,
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    getSynonymToEnumMap().getEnumValueOptional(MasterConfirmationTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterConfirmationTypeValue);
                });
        setValueAndUpdateMappings(masterConfirmationTypePath.addElement("masterConfirmationTypeScheme"),
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterConfirmationType().getOrCreateMeta().setScheme(xmlValue);
                });

        Path masterConfirmationAnnexTypePath = masterConfirmationPath.addElement("masterConfirmationAnnexType");
        setValueAndUpdateMappings(masterConfirmationAnnexTypePath,
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    getSynonymToEnumMap().getEnumValueOptional(MasterConfirmationAnnexTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setMasterConfirmationAnnexTypeValue);
                });
        setValueAndUpdateMappings(masterConfirmationAnnexTypePath.addElement("masterConfirmationAnnexTypeScheme"),
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    agreementName.getOrCreateMasterConfirmationAnnexType().getOrCreateMeta().setScheme(xmlValue);
                });

        setValueAndUpdateMappings(masterConfirmationPath.addElement("masterConfirmationDate"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)));

        return setAgreementType(builder, LegalAgreementTypeEnum.MASTER_CONFIRMATION);
    }

    private Optional<LegalAgreement> getBrokerConfirmation(Path synonymPath) {
        Path brokerConfirmationPath = synonymPath.addElement("brokerConfirmation");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        setValueAndUpdateMappings(brokerConfirmationPath.addElement("brokerConfirmationType"),
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    getSynonymToEnumMap().getEnumValueOptional(BrokerConfirmationTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setBrokerConfirmationType);
                });

        return setAgreementType(builder, LegalAgreementTypeEnum.BROKER_CONFIRMATION);
    }

    private Optional<LegalAgreement> getCreditSupportAgreement(Path synonymPath) {
        Path creditSupportAgreementPath = synonymPath.addElement("creditSupportAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        Path creditSupportAgreementTypePath = creditSupportAgreementPath.addElement("type");
        setValueAndUpdateMappings(creditSupportAgreementTypePath,
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    getSynonymToEnumMap().getEnumValueOptional(CreditSupportAgreementTypeEnum.class, xmlValue)
                            .ifPresent(agreementName::setCreditSupportAgreementTypeValue);
                });
        setValueAndUpdateMappings(creditSupportAgreementTypePath.addElement("creditSupportAgreementTypeScheme"),
                xmlValue -> {
                    AgreementNameBuilder agreementName = builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    agreementName.getOrCreateCreditSupportAgreementType().getOrCreateMeta().setScheme(xmlValue);
                });

        setValueAndUpdateMappings(creditSupportAgreementPath.addElement("date"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)));

        return setAgreementType(builder, LegalAgreementTypeEnum.CREDIT_SUPPORT_AGREEMENT);
    }

    private Optional<LegalAgreement> getConfirmation(Path synonymPath) {
        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        filterListMappings(getMappings(), synonymPath.addElement("contractualDefinitions")).stream()
                .filter(m -> m.getXmlValue() != null)
                .forEach(m -> {
                    FieldWithMetaContractualDefinitionsEnum.FieldWithMetaContractualDefinitionsEnumBuilder contractualDefinitionsBuilder = FieldWithMetaContractualDefinitionsEnum.builder();

                    Path contractualDefinitionsPath = m.getXmlPath();
                    setValueAndUpdateMappings(contractualDefinitionsPath,
                            xmlValue -> getSynonymToEnumMap().getEnumValueOptional(ContractualDefinitionsEnum.class, xmlValue)
                                        .ifPresent(contractualDefinitionsBuilder::setValue));
                    setValueAndUpdateMappings(contractualDefinitionsPath.addElement("contractualDefinitionsScheme"),
                            xmlValue -> contractualDefinitionsBuilder.getOrCreateMeta().setScheme(xmlValue));

                    if (contractualDefinitionsBuilder.hasData()) {
                        builder.getOrCreateLegalAgreementType()
                                .getOrCreateAgreementName()
                                .addContractualDefinitionsType(contractualDefinitionsBuilder);
                    }
                });

        // find all contractualMatrix list items
        filterListMappings(getMappings(), synonymPath.addElement("contractualMatrix"))
                // for each item, check if matrixType/matrixTerm is mapped
                .forEach(m -> {
                    AgreementNameBuilder agreementName =
                            builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();
                    ContractualMatrix.ContractualMatrixBuilder contractualMatrixBuilder = ContractualMatrix.builder();

                    Path matrixTypePath = m.getXmlPath().addElement("matrixType");
                    setValueAndUpdateMappings(matrixTypePath,
                        xmlValue -> getSynonymToEnumMap().getEnumValueOptional(MatrixTypeEnum.class, xmlValue)
                                    .ifPresent(contractualMatrixBuilder::setMatrixTypeValue));
                    setValueAndUpdateMappings(matrixTypePath.addElement("matrixTypeScheme"),
                            xmlValue -> contractualMatrixBuilder.getOrCreateMatrixType().getOrCreateMeta().setScheme(xmlValue));

                    Path matrixTermPath = m.getXmlPath().addElement("matrixTerm");
                    setValueAndUpdateMappings(matrixTermPath,
                            xmlValue -> getSynonymToEnumMap().getEnumValueOptional(MatrixTermEnum.class, xmlValue)
                                        .ifPresent(contractualMatrixBuilder::setMatrixTermValue));
                    setValueAndUpdateMappings(matrixTermPath.addElement("matrixTermScheme"),
                            xmlValue -> contractualMatrixBuilder.getOrCreateMatrixTerm().getOrCreateMeta().setScheme(xmlValue));

                    if (contractualMatrixBuilder.hasData()) {
                        agreementName.addContractualMatrix(contractualMatrixBuilder);
                    }
                });

        // find all contractualTermsSupplement list items
        filterListMappings(getMappings(), synonymPath.addElement("contractualTermsSupplement"))
                // for each item, check if type is mapped
                .forEach(m -> {
                    AgreementNameBuilder agreementName =
                            builder.getOrCreateLegalAgreementType().getOrCreateAgreementName();

                    ContractualTermsSupplement.ContractualTermsSupplementBuilder contractualTermsSupplementBuilder = ContractualTermsSupplement.builder();

                    Path typePath = m.getXmlPath().addElement("type");
                    setValueAndUpdateMappings(typePath,
                            xmlValue -> getSynonymToEnumMap().getEnumValueOptional(ContractualSupplementTypeEnum.class, xmlValue)
                                        .ifPresent(contractualTermsSupplementBuilder::setContractualTermsSupplementTypeValue));
                    setValueAndUpdateMappings(typePath.addElement("contractualSupplementScheme"),
                            xmlValue -> contractualTermsSupplementBuilder.getOrCreateContractualTermsSupplementType().getOrCreateMeta().setScheme(xmlValue));

                    setValueAndUpdateMappings(m.getXmlPath().addElement("publicationDate"),
                            xmlValue -> contractualTermsSupplementBuilder.setPublicationDate(parseDate(xmlValue)));

                    if (contractualTermsSupplementBuilder.hasData()) {
                        agreementName.addContractualTermsSupplement(contractualTermsSupplementBuilder);
                    }
                });

        return setAgreementType(builder, LegalAgreementTypeEnum.CONFIRMATION);
    }

    @NotNull
    private Date parseDate(String xmlValue) {
        return Date.parse(xmlValue.replace("Z", ""));
    }

    private Optional<LegalAgreement> getOtherAgreement(Path synonymPath) {
        Path otherAgreementPath = synonymPath.addElement("otherAgreement");

        LegalAgreement.LegalAgreementBuilder builder = LegalAgreement.builder();

        setValueAndUpdateMappings(otherAgreementPath.addElement("type"),
                xmlValue -> builder.getOrCreateLegalAgreementType().getOrCreateAgreementName().setOtherAgreement(xmlValue));

        setValueAndUpdateMappings(otherAgreementPath.addElement("version"),
                xmlValue -> builder.getOrCreateLegalAgreementType().setVintage(Integer.valueOf(xmlValue)));

        setValueAndUpdateMappings(otherAgreementPath.addElement("date"),
                xmlValue -> builder.setAgreementDate(parseDate(xmlValue)));

        return setAgreementType(builder, LegalAgreementTypeEnum.OTHER);
    }

    @NotNull
    private Optional<LegalAgreement> setAgreementType(LegalAgreement.LegalAgreementBuilder builder, LegalAgreementTypeEnum masterAgreement) {
        if (builder.hasData()) {
            builder.getOrCreateLegalAgreementType()
                    .getOrCreateAgreementName()
                    .setAgreementType(masterAgreement);
            return Optional.of(builder);
        } else {
            return Optional.empty();
        }
    }
}
