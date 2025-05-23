package cdm.legaldocumentation.common.processor;

import cdm.legaldocumentation.common.UmbrellaAgreement;
import cdm.legaldocumentation.common.UmbrellaAgreementEntity;
import cdm.legaldocumentation.common.UmbrellaAgreementEntity.UmbrellaAgreementEntityBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.translation.Path.PathElement;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.regnosys.rosetta.common.translation.Path.parse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UmbrellaAgreementEntityMappingProcessorTest {

	private static final String PRINCIPAL_NAME_0 = "PrincipalName0";
	private static final String LEI_0 = "Lei0";
	private static final String ADDITIONAL_0 = "Additional0";
	private static final String PRINCIPAL_NAME_1 = "PrincipalName1";
	private static final String LEI_1 = "Lei1";
	private static final String ADDITIONAL_1 = "Additional1";

	@Test
	void shouldMapMultipleUmbrellaAgreementEntity() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.agreementTerms.umbrellaAgreement.parties");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(getEmptyMapping(getXmlPath("principal_name", 0), PRINCIPAL_NAME_0));
		mappings.add(getEmptyMapping(getXmlPath("lei", 0), LEI_0));
		mappings.add(getEmptyMapping(getXmlPath("additional", 0), ADDITIONAL_0));
		mappings.add(getEmptyMapping(getXmlPath("principal_name", 1), PRINCIPAL_NAME_1));
		mappings.add(getEmptyMapping(getXmlPath("lei", 1), LEI_1));
		mappings.add(getEmptyMapping(getXmlPath("additional", 1), ADDITIONAL_1));
		MappingContext context = new MappingContext(mappings, Collections.emptyMap(), null, null);

		UmbrellaAgreement.UmbrellaAgreementBuilder parent = UmbrellaAgreement.builder();
		List<UmbrellaAgreementEntityBuilder> builders = Collections.singletonList(mock(UmbrellaAgreementEntityBuilder.class));

		// test
		Path synonymPath = Path.parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule");
		UmbrellaAgreementEntityMappingProcessor processor = new UmbrellaAgreementEntityMappingProcessor(rosettaPath, Collections.singletonList(synonymPath), context);
		processor.map(synonymPath, builders, parent);
		UmbrellaAgreement umbrellaAgreement = parent.build();

		// assert

		List<? extends UmbrellaAgreementEntity> parties = umbrellaAgreement.getParties();
		assertNotNull(parties);
		assertEquals(2, parties.size());

		UmbrellaAgreementEntity party0 = parties.get(0);
		assertEquals(PRINCIPAL_NAME_0, party0.getName().getValue());
		assertEquals(LEI_0, party0.getEntityId().get(0).getValue());
		assertEquals(ADDITIONAL_0, party0.getTerms());

		UmbrellaAgreementEntity party1 = parties.get(1);
		assertEquals(PRINCIPAL_NAME_1, party1.getName().getValue());
		assertEquals(LEI_1, party1.getEntityId().get(0).getValue());
		assertEquals(ADDITIONAL_1, party1.getTerms());

		assertTrue(mappings.stream().allMatch(mapping -> mapping.getError() == null && mapping.isCondition()));
	}

	@Test
	void shouldMapSimpleUmbrellaAgreementEntity() {
		// set up
		RosettaPath rosettaPath = RosettaPath.valueOf("LegalAgreement.agreementTerms.umbrellaAgreement.parties");
		List<Mapping> mappings = new ArrayList<>();
		mappings.add(getEmptyMapping(getXmlPath("principal_name"), PRINCIPAL_NAME_0));
		mappings.add(getEmptyMapping(getXmlPath("lei"), LEI_0));
		mappings.add(getEmptyMapping(getXmlPath("additional"), ADDITIONAL_0));
		MappingContext context = new MappingContext(mappings, Collections.emptyMap(), null, null);

		UmbrellaAgreement.UmbrellaAgreementBuilder parent = UmbrellaAgreement.builder();
		List<UmbrellaAgreementEntityBuilder> builders = Collections.singletonList(mock(UmbrellaAgreementEntityBuilder.class));

		// test
		Path synonymPath = Path.parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule");
		UmbrellaAgreementEntityMappingProcessor processor = new UmbrellaAgreementEntityMappingProcessor(rosettaPath, Collections.emptyList(), context);
		processor.map(synonymPath, builders, parent);
		UmbrellaAgreement umbrellaAgreement = parent.build();

		// assert

		List<? extends UmbrellaAgreementEntity> parties = umbrellaAgreement.getParties();
		assertNotNull(parties);
		assertEquals(1, parties.size());

		UmbrellaAgreementEntity party0 = parties.get(0);
		assertEquals(PRINCIPAL_NAME_0, party0.getName().getValue());
		assertEquals(LEI_0, party0.getEntityId().get(0).getValue());
		assertEquals(ADDITIONAL_0, party0.getTerms());

		assertTrue(mappings.stream().allMatch(mapping -> mapping.getError() == null && mapping.isCondition()));
	}

	private Path getXmlPath(String attribute, int index) {
		return parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule")
				.addElement(new PathElement(attribute, index));
	}

	private Path getXmlPath(String attribute) {
		return parse("answers.partyA.umbrella_agreement_and_principal_identification.principal_identification_schedule")
				.addElement(new PathElement(attribute));
	}

	private Mapping getEmptyMapping(Path xmlPath, String xmlValue) {
		return new Mapping(xmlPath, xmlValue, null, null, "no destination", false, false, false);
	}
}