package com.regnosys.granite.projector.isdacreate;

import cdm.legaldocumentation.common.LegalAgreement;
import org.isda.isdacreate.isda.csdim2016.englaw.Answers;
import org.isda.isdacreate.isda.csdim2016.englaw.IsdaCreateIsdaCsdIm2016EngLaw;

class IsdaCreateIsdaCsdIm2016EngLawProjectionMapper extends IsdaCreateIsdaCsaIm2016ProjectionMapper {

	IsdaCreateIsdaCsdIm2016EngLaw getIsdaCreate(LegalAgreement legalAgreement) {
		var isdaCreateEngLaw = new IsdaCreateIsdaCsdIm2016EngLaw();
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

