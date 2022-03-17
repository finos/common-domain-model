package com.regnosys.granite.projector.isdacreate;

import cdm.legalagreement.common.LegalAgreement;
import org.isda.isdacreate.isda.csaim2016.nylaw.Answers;
import org.isda.isdacreate.isda.csaim2016.nylaw.IsdaCreateIsdaCsaIm2016NyLaw;

class IsdaCreateIsdaCsaIm2016NyLawProjectionMapper extends IsdaCreateIsdaCsaIm2016ProjectionMapper {

	IsdaCreateIsdaCsaIm2016NyLaw getIsdaCreate(LegalAgreement legalAgreement) {
		var isdaCreateEngLaw = new IsdaCreateIsdaCsaIm2016NyLaw();
		isdaCreateEngLaw.setId(getId(legalAgreement));
		isdaCreateEngLaw.setDocument(getDocument(legalAgreement));
		isdaCreateEngLaw.setAnswers(getAnswers(legalAgreement));
		return isdaCreateEngLaw;
	}

	private Answers getAnswers(LegalAgreement legalAgreement) {
		var answers = new Answers();
		return answers;
	}
}

