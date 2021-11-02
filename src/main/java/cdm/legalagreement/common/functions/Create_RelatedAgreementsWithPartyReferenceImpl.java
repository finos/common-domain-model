package cdm.legalagreement.common.functions;

import cdm.legalagreement.common.LegalAgreement;
import cdm.legalagreement.common.RelatedAgreement;

import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class Create_RelatedAgreementsWithPartyReferenceImpl extends Create_RelatedAgreementsWithPartyReference {
    @Override
    protected List<RelatedAgreement.RelatedAgreementBuilder> doEvaluate(List<? extends LegalAgreement> legalAgreement) {
        return emptyIfNull(legalAgreement).stream()
                .map(RelatedAgreement.builder()::setLegalAgreement)
                .collect(Collectors.toList());
    }
}
