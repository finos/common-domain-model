package cdm.legalagreement.common.processor;

import cdm.legalagreement.common.AgreementName;
import cdm.legalagreement.common.LegalAgreement;
import cdm.legalagreement.common.LegalAgreementPublisherEnum;
import cdm.legalagreement.csa.CreditSupportAgreementTypeEnum;
import cdm.legalagreement.master.MasterAgreementTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cdm.legalagreement.common.LegalAgreement.LegalAgreementBuilder;
import static cdm.legalagreement.common.LegalAgreement.builder;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;

public class RelatedAgreementMappingProcessor extends MappingProcessor {

    public RelatedAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        LegalAgreementBuilder agreementTermsBuilder = (LegalAgreementBuilder) parent;
        getRelatedAgreement(synonymPath).ifPresent(agreementTermsBuilder::addRelatedAgreements);
        PARTIES.forEach(party -> getRelatedAgreementForParty(synonymPath, party).ifPresent(agreementTermsBuilder::addRelatedAgreements));

        associateIsdaMasterAgreement(synonymPath, builder.stream()
                .map(LegalAgreementBuilder.class::cast)
                .filter(this::isMasterAgreement)
                .collect(Collectors.toList())
        );
    }

    private void associateIsdaMasterAgreement(Path synonymPath, List<LegalAgreementBuilder> relatedAgreementBuilders) {
        for (LegalAgreementBuilder relatedAgreementBuilder : relatedAgreementBuilders) {
            setValueAndOptionallyUpdateMappings(synonymPath.addElement("isda_master_agreement_form"),
                    (vintage) -> {
                        relatedAgreementBuilder.getOrCreateAgreementType().setVintage(Integer.valueOf(vintage));
                        return true;
                    },
                    getMappings(), getModelPath());
        }
    }

    private boolean isMasterAgreement(LegalAgreementBuilder relatedAgreementBuilder) {
        return Optional.of(relatedAgreementBuilder)
                .map(LegalAgreementBuilder::getAgreementType)
                .filter(x -> Optional.ofNullable(x.getAgreementName())
                        .map(AgreementName::getMasterAgreementType)
                        .isPresent())
                .filter(x -> x.getPublisher() == LegalAgreementPublisherEnum.ISDA)
                .isPresent();
    }

    private Optional<LegalAgreement> getRelatedAgreement(Path synonymPath) {
        LegalAgreementBuilder legalAgreementBuilder = builder();

        setValueAndOptionallyUpdateMappings(synonymPath.addElement("collateral_transfer_agreement_date"),
                (date) -> setAgreementDetails(synonymPath, legalAgreementBuilder, date), getMappings(), getModelPath());

        setValueAndOptionallyUpdateMappings(synonymPath.addElement("master_agreement_date"),
                (date) -> setAgreementDetails(synonymPath, legalAgreementBuilder, date), getMappings(), getModelPath());

        return legalAgreementBuilder.hasData() ? Optional.of(legalAgreementBuilder.build()) : Optional.empty();
    }

    private Optional<LegalAgreement> getRelatedAgreementForParty(Path synonymPath, String party) {
        LegalAgreement.LegalAgreementBuilder relatedAgreementBuilder = LegalAgreement.builder();
        setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_date_of_security_agreement"),
                (date) -> setAgreementDetails(synonymPath, relatedAgreementBuilder, date), getMappings(), getModelPath());

        return relatedAgreementBuilder.hasData() ? Optional.of(relatedAgreementBuilder.build()) : Optional.empty();
    }

    @NotNull
    private Boolean setAgreementDetails(Path synonymPath, LegalAgreementBuilder legalAgreementBuilder, String date) {
        legalAgreementBuilder.setAgreementDate(Date.parse(date));
        switch (synonymPath.getLastElement().getPathName()) {
            case "collateral_transfer_agreement":
            case "date_of_collateral_transfer_agreement":
                legalAgreementBuilder
                        .getOrCreateAgreementType()
                        .getOrCreateAgreementName()
                        .getOrCreateCreditSupportAgreementName()
                        .setCreditSupportAgreementType(CreditSupportAgreementTypeEnum.COLLATERAL_TRANSFER_AGREEMENT);
                return true;
            case "date_of_isda_master_agreement":
                legalAgreementBuilder
                        .getOrCreateAgreementType()
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setAgreementName(AgreementName.builder()
                                .setMasterAgreementType(MasterAgreementTypeEnum.ISDA));
                return true;
            case "date_of_euroclear_security_agreement":
                legalAgreementBuilder.getOrCreateAgreementType()
                        .setPublisher(LegalAgreementPublisherEnum.ISDA_EUROCLEAR)
                        .getOrCreateAgreementName().setSecurityAgreement(true);
                return true;
            default:
                return false;
        }
    }
}
