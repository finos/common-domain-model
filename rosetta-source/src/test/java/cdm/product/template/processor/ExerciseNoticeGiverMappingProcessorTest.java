package cdm.product.template.processor;

import cdm.product.template.ExerciseNotice;
import cdm.product.template.ExerciseNoticeGiverEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseNoticeGiverMappingProcessorTest {

	private static final RosettaPath MODEL_PATH = RosettaPath.valueOf(
			"Trade.product.economicTerms.payout.optionPayout(0).exerciseTerms.exerciseProcedure.manualExercise.exerciseNotice.exerciseNoticeGiver");
	private static final Path SYNONYM_PATH = Path.parse("dataDocument.trade.bondOption.exerciseProcedure.manualExercise.exerciseNotice.partyReference");

	@Test
	void shouldMapExerciseNoticeGiverToBuyer() {
		MappingContext context = new MappingContext(getMappings("p1"), Collections.emptyMap(), null, null);

		// test
		ExerciseNoticeGiverMappingProcessor mapper = new ExerciseNoticeGiverMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);
		ExerciseNotice.ExerciseNoticeBuilder parent = ExerciseNotice.builder();
		mapper.mapBasic(SYNONYM_PATH, Optional.empty(), parent);

		// assert
		assertEquals(ExerciseNoticeGiverEnum.BUYER, parent.getExerciseNoticeGiver());

		Mapping updatedMapping = context.getMappings().get(0);
		assertEquals(SYNONYM_PATH.addElement("href"), updatedMapping.getXmlPath());
		assertEquals("p1", updatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(MODEL_PATH), updatedMapping.getRosettaPath());
		assertNull(updatedMapping.getError());
		assertTrue(updatedMapping.isCondition());
	}

	@Test
	void shouldMapExerciseNoticeGiverToSeller() {
		MappingContext context = new MappingContext(getMappings("p2"), Collections.emptyMap(), null, null);

		// test
		ExerciseNoticeGiverMappingProcessor mapper = new ExerciseNoticeGiverMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);
		ExerciseNotice.ExerciseNoticeBuilder parent = ExerciseNotice.builder();
		mapper.mapBasic(SYNONYM_PATH, Optional.empty(), parent);

		// assert
		assertEquals(ExerciseNoticeGiverEnum.SELLER, parent.getExerciseNoticeGiver());

		Mapping updatedMapping = context.getMappings().get(0);
		assertEquals(SYNONYM_PATH.addElement("href"), updatedMapping.getXmlPath());
		assertEquals("p2", updatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(MODEL_PATH), updatedMapping.getRosettaPath());
		assertNull(updatedMapping.getError());
		assertTrue(updatedMapping.isCondition());
	}

	@Test
	void shouldFailToMapExerciseNoticeGiver() {
		MappingContext context = new MappingContext(getMappings("p3"), Collections.emptyMap(), null, null);

		// test
		ExerciseNoticeGiverMappingProcessor mapper = new ExerciseNoticeGiverMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);
		ExerciseNotice.ExerciseNoticeBuilder parent = ExerciseNotice.builder();
		mapper.mapBasic(SYNONYM_PATH, Optional.empty(), parent);

		// assert
		assertNull(parent.getExerciseNoticeGiver());

		Mapping updatedMapping = context.getMappings().get(0);
		assertEquals(SYNONYM_PATH.addElement("href"), updatedMapping.getXmlPath());
		assertEquals("p3", updatedMapping.getXmlValue());
		assertNull(updatedMapping.getRosettaPath());
		assertEquals("no destination", updatedMapping.getError());
		assertTrue(updatedMapping.isCondition());
	}

	private List<Mapping> getMappings(String synonymValue) {
		return Arrays.asList(
				// exercise notice giver mapping (initially unmapped)
				new Mapping(SYNONYM_PATH.addElement("href"), synonymValue, null, null, "no destination", false, false, false),
				// option buyer mapping
				new Mapping(Path.parse("dataDocument.trade.bondOption.buyerPartyReference.href"),
						"p1",
						Path.parse("Trade.product.economicTerms.payout.optionPayout[0].buyerSeller.buyer"),
						"p1",
						null,
						false,
						true,
						false),
				// option seller mapping
				new Mapping(Path.parse("dataDocument.trade.bondOption.sellerPartyReference.href"),
						"p2",
						Path.parse("Trade.product.economicTerms.payout.optionPayout[0].buyerSeller.seller"),
						"p2",
						null,
						false,
						true,
						false)

		);
	}
}