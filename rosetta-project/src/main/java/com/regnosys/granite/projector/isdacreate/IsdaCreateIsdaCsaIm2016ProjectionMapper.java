package com.regnosys.granite.projector.isdacreate;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.legaldocumentation.common.LegalAgreement;
import com.rosetta.model.lib.meta.FieldWithMeta;
import org.isda.isdacreate.shared.Document;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

abstract class IsdaCreateIsdaCsaIm2016ProjectionMapper {

	static final String APPLICABLE = "applicable";
	static final String NOT_APPLICABLE = "not_applicable";
	static final String OTHER = "other";
	static final String EXCLUDE = "exclude";
	static final String NONE_SPECIFIED = "none_specified";
	static final String NOT_SPECIFIED = "not_specified";
	static final String NONE = "none";
	static final String TRUE = "true";
	static final String FALSE = "false";
	static final String STANDARD = "standard";
	static final String SELECT_LOCATION = "select_location";

	private final IsdaCreateEnumMapper enumMapper = new IsdaCreateEnumMapper();

	String getId(LegalAgreement legalAgreement) {
		return Optional.ofNullable(legalAgreement)
			.map(LegalAgreement::getIdentifier)
			.flatMap(i -> i.stream()
				.map(Identifier::getAssignedIdentifier)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(AssignedIdentifier::getIdentifier)
				.map(FieldWithMeta::getValue)
				.findFirst())
			.orElse(null);
	}

	Document getDocument(LegalAgreement legalAgreement) {
		var document = new Document();

		Optional.ofNullable(legalAgreement)
			.ifPresent(a -> {
				document.setYear(2016); // Only InitialMargin2016 is currently supported
				Optional.ofNullable(a.getLegalAgreementIdentification())
					.ifPresent(t -> {
						Optional.ofNullable(t.getGoverningLaw()).ifPresent(governingLawEnum -> {
							document.setGoverningLaw(enumMapper.getGoverningLaw(governingLawEnum));
							switch(governingLawEnum) {
							case GBEN:
								document.setId("016764cc-5e05-6935-2562-0ad2d0b51285");
								document.setAbbreviation("2016 IM CSD (Eng Law)");
								document.setDescription("ISDA 2016 Phase One IM Credit Support Deed (Security Interest - English Law)");
								document.setHasSchema(true);
								break;
							case USNY:
								document.setId("016764cc-5e03-36c6-fc79-7f6344b867d0");
								document.setAbbreviation("2016 IM CSA (NY Law)");
								document.setDescription("ISDA 2016 Phase One Credit Support Annex for Initial Margin (IM) (Security Interest - New York Law)");
								document.setHasSchema(true);
								break;
							}
						});
						Optional.ofNullable(t.getPublisher()).map(enumMapper::getPublisher).ifPresent(document::setPublisher);
					});

			});

		return document;
	}
}
