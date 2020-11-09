package cdm.legalagreement.common.processor;

import cdm.legalagreement.common.*;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.DateImpl;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.IsdaCreateMappingProcessorUtils.PARTIES;

public class RelatedAgreementMappingProcessor extends MappingProcessor {

    public RelatedAgreementMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
        super(modelPath, synonymPaths, mappingContext);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
        LegalAgreement.LegalAgreementBuilder agreementTermsBuilder = (LegalAgreement.LegalAgreementBuilder) parent;
        getRelatedAgreement(synonymPath).ifPresent(agreementTermsBuilder::addRelatedAgreements);
        PARTIES.forEach(party -> getRelatedAgreementForParty(synonymPath, party).ifPresent(agreementTermsBuilder::addRelatedAgreements));

        associateIsdaMasterAgreement(synonymPath, builder.stream()
                .map(RelatedAgreement.RelatedAgreementBuilder.class::cast)
                .filter(this::isMasterAgreement)
                .collect(Collectors.toList())
        );
    }

    private void associateIsdaMasterAgreement(Path synonymPath, List<RelatedAgreement.RelatedAgreementBuilder> relatedAgreementBuilders) {
        for (RelatedAgreement.RelatedAgreementBuilder relatedAgreementBuilder : relatedAgreementBuilders) {
            setValueAndOptionallyUpdateMappings(synonymPath.addElement("isda_master_agreement_form"),
                    (vintage) -> {
                        relatedAgreementBuilder.getOrCreateLegalAgreement().getOrCreateAgreementType().setVintage(vintage);
                        return true;
                    },
                    getMappings(), getModelPath());
        }
    }

    private boolean isMasterAgreement(RelatedAgreement.RelatedAgreementBuilder relatedAgreementBuilder) {
        return Optional.of(relatedAgreementBuilder)
                .map(RelatedAgreement.RelatedAgreementBuilder::getLegalAgreement)
                .map(LegalAgreement.LegalAgreementBuilder::getAgreementType)
                .filter(x -> x.getName() == LegalAgreementNameEnum.MASTER_AGREEMENT)
                .filter(x -> x.getPublisher() == LegalAgreementPublisherEnum.ISDA)
                .isPresent();
    }


    private Optional<RelatedAgreement> getRelatedAgreement(Path synonymPath) {
        RelatedAgreement.RelatedAgreementBuilder relatedAgreementBuilder = RelatedAgreement.builder();

        setValueAndOptionallyUpdateMappings(synonymPath.addElement("collateral_transfer_agreement_date"),
                (date) -> setAgreementDetails(synonymPath, relatedAgreementBuilder, date), getMappings(), getModelPath());

        setValueAndOptionallyUpdateMappings(synonymPath.addElement("master_agreement_date"),
                (date) -> setAgreementDetails(synonymPath, relatedAgreementBuilder, date), getMappings(), getModelPath());

        return relatedAgreementBuilder.hasData() ? Optional.of(relatedAgreementBuilder.build()) : Optional.empty();
    }


    private Optional<RelatedAgreement> getRelatedAgreementForParty(Path synonymPath, String party) {
        RelatedAgreement.RelatedAgreementBuilder relatedAgreementBuilder = RelatedAgreement.builder();
        setValueAndOptionallyUpdateMappings(synonymPath.addElement(party + "_date_of_security_agreement"),
                (date) -> setAgreementDetails(synonymPath, relatedAgreementBuilder, date), getMappings(), getModelPath());

        return relatedAgreementBuilder.hasData() ? Optional.of(relatedAgreementBuilder.build()) : Optional.empty();
    }

    @NotNull
    private Boolean setAgreementDetails(Path synonymPath, RelatedAgreement.RelatedAgreementBuilder relatedAgreementBuilder, String date) {
        LegalAgreement.LegalAgreementBuilder legalAgreementBuilder = relatedAgreementBuilder.getOrCreateLegalAgreement();
        legalAgreementBuilder.setAgreementDate(DateImpl.of(LocalDate.parse(date)));
        switch (synonymPath.getLastElement().getPathName()) {
            case "collateral_transfer_agreement":
            case "date_of_collateral_transfer_agreement":
                legalAgreementBuilder.getOrCreateAgreementType()
                        .setName(LegalAgreementNameEnum.COLLATERAL_TRANSFER_AGREEMENT);
                return true;
            case "date_of_isda_master_agreement":
                legalAgreementBuilder.getOrCreateAgreementType()
                        .setPublisher(LegalAgreementPublisherEnum.ISDA)
                        .setName(LegalAgreementNameEnum.MASTER_AGREEMENT);
                return true;
            case "date_of_euroclear_security_agreement":
                legalAgreementBuilder.getOrCreateAgreementType()
                        .setPublisher(LegalAgreementPublisherEnum.ISDA_EUROCLEAR)
                        .setName(LegalAgreementNameEnum.SECURITY_AGREEMENT);
                return true;
            default:
                return false;
        }
    }
}
