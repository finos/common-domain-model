package org.isda.cdm.functions;

import org.isda.cdm.LegalAgreement;
import org.isda.cdm.functions.EmptyLegalAgreement;

public class EmptyLegalAgreementImpl extends EmptyLegalAgreement {

    @Override
    protected LegalAgreement doEvaluate() {
        return null;
    }
}
