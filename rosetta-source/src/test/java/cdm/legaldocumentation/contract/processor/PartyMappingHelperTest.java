package cdm.legaldocumentation.contract.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.product.template.TradableProduct;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static cdm.product.template.TradableProduct.TradableProductBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class PartyMappingHelperTest {

	private static final Path PAYER_XML_PATH = Path.parse("dataDocument.trade.swap.swapStream[0].payerPartyReference.href");
	private static final Path RECEIVER_XML_PATH = Path.parse("dataDocument.trade.swap.swapStream[0].receiverPartyReference.href");

	private static final RosettaPath PAYER_MODEL_PATH = RosettaPath.valueOf(
			"Trade.product.economicTerms.payout.interestRatePayout(0).payerReceiver.payer");
	private static final RosettaPath RECEIVER_MODEL_PATH = RosettaPath.valueOf(
			"Trade.product.economicTerms.payout.interestRatePayout(0).payerReceiver.receiver");

	private static final String PAYER_PARTY_REF = "p1";
	private static final String RECEIVER_PARTY_REF = "p2";

	private MappingContext context;
	private TradableProductBuilder tradableProductBuilder;

	@BeforeEach
	void setUp() {
		context = new MappingContext(getMappings(PAYER_XML_PATH, PAYER_PARTY_REF, RECEIVER_XML_PATH, RECEIVER_PARTY_REF),
				Collections.emptyMap(),
				Collections.emptyMap());
		tradableProductBuilder = TradableProduct.builder();
	}

	@Test
	void shouldMapPayerToParty1() {
		PartyMappingHelper helper = new PartyMappingHelper(context, tradableProductBuilder, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();
		Path synonymPath = PAYER_XML_PATH.getParent();
		helper.setCounterpartyRoleEnum(PAYER_MODEL_PATH, synonymPath, builder::setPayer);

		assertEquals(CounterpartyRoleEnum.PARTY_1, builder.getPayer());
		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		Mapping updatedMapping = context.getMappings().get(0);
		assertEquals(PAYER_XML_PATH, updatedMapping.getXmlPath());
		assertEquals(PAYER_PARTY_REF, updatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(PAYER_MODEL_PATH), updatedMapping.getRosettaPath());
		assertNull(updatedMapping.getRosettaValue());
		assertNull(updatedMapping.getError());
		assertTrue(updatedMapping.isCondition());
	}

	@Test
	void shouldMapReceiverToParty1() {
		PartyMappingHelper helper = new PartyMappingHelper(context, tradableProductBuilder, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();
		Path synonymPath = RECEIVER_XML_PATH.getParent();
		helper.setCounterpartyRoleEnum(RECEIVER_MODEL_PATH, synonymPath, builder::setReceiver);

		assertEquals(CounterpartyRoleEnum.PARTY_1, builder.getReceiver());
		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		Mapping updatedMapping = context.getMappings().get(1);
		assertEquals(RECEIVER_XML_PATH, updatedMapping.getXmlPath());
		assertEquals(RECEIVER_PARTY_REF, updatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(RECEIVER_MODEL_PATH), updatedMapping.getRosettaPath());
		assertNull(updatedMapping.getRosettaValue());
		assertNull(updatedMapping.getError());
		assertTrue(updatedMapping.isCondition());
	}

	@Test
	void shouldMapPayerRefToParty1AndReceiverRefToParty2() {
		PartyMappingHelper helper = new PartyMappingHelper(context, tradableProductBuilder, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();

		helper.setCounterpartyRoleEnum(PAYER_PARTY_REF, builder::setPayer);

		assertEquals(CounterpartyRoleEnum.PARTY_1, builder.getPayer());
		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		helper.setCounterpartyRoleEnum(RECEIVER_PARTY_REF, builder::setReceiver);

		assertEquals(CounterpartyRoleEnum.PARTY_2, builder.getReceiver());
		assertTrue(helper.getBothCounterpartiesCollectedFuture().isDone());
	}

	@Test
	void shouldMapPayerToParty1AndReceiverToParty2() throws ExecutionException, InterruptedException, TimeoutException {
		PartyMappingHelper helper = new PartyMappingHelper(context, tradableProductBuilder, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();

		// test
		Path payerSynonymPath = PAYER_XML_PATH.getParent();
		helper.setCounterpartyRoleEnum(PAYER_MODEL_PATH, payerSynonymPath, builder::setPayer);

		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		// test
		Path receiverSynonymPath = RECEIVER_XML_PATH.getParent();
		helper.setCounterpartyRoleEnum(RECEIVER_MODEL_PATH, receiverSynonymPath, builder::setReceiver);

		assertEquals(CounterpartyRoleEnum.PARTY_1, builder.getPayer());
		assertEquals(CounterpartyRoleEnum.PARTY_2, builder.getReceiver());

		assertTrue(helper.getBothCounterpartiesCollectedFuture().isDone());
		Map<String, CounterpartyRoleEnum> partyExternalReferenceToCounterpartyRoleEnumMap = helper.getBothCounterpartiesCollectedFuture().get();
		assertNotNull(partyExternalReferenceToCounterpartyRoleEnumMap);
		assertEquals(CounterpartyRoleEnum.PARTY_1, partyExternalReferenceToCounterpartyRoleEnumMap.get(PAYER_PARTY_REF));
		assertEquals(CounterpartyRoleEnum.PARTY_2, partyExternalReferenceToCounterpartyRoleEnumMap.get(RECEIVER_PARTY_REF));

		 // test PartyMappingHelper.addCounterparties()
		helper.addCounterparties();

		// wait for invoked tasks to complete before assertions
		assertEquals(1, context.getInvokedTasks().size());
		context.getInvokedTasks().get(0).get(1000, TimeUnit.MILLISECONDS);

		List<? extends Counterparty.CounterpartyBuilder> counterpartiesRaw = tradableProductBuilder.getCounterparty();
		List<Counterparty.CounterpartyBuilder> counterparties = counterpartiesRaw.stream().map(Counterparty.CounterpartyBuilder.class::cast).collect(Collectors.toList());
		assertThat(counterparties, hasSize(2));
		assertThat(counterparties,
				hasItems(
						Counterparty.builder()
								.setRole(CounterpartyRoleEnum.PARTY_1)
								.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(PAYER_PARTY_REF).build()),
						Counterparty.builder()
								.setRole(CounterpartyRoleEnum.PARTY_2)
								.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(RECEIVER_PARTY_REF).build())));

		Counterparty.CounterpartyBuilder counterparty1 = counterparties.get(0);
		assertEquals(CounterpartyRoleEnum.PARTY_1, counterparty1.getRole());
		assertEquals(PAYER_PARTY_REF, counterparty1.getOrCreatePartyReference().getExternalReference());

		Counterparty.CounterpartyBuilder counterparty2 = counterparties.get(1);
		assertEquals(CounterpartyRoleEnum.PARTY_2, counterparty2.getRole());
		assertEquals(RECEIVER_PARTY_REF, counterparty2.getOrCreatePartyReference().getExternalReference());

		// assert mappings
		Mapping payerUpdatedMapping = context.getMappings().get(0);
		assertEquals(PAYER_XML_PATH, payerUpdatedMapping.getXmlPath());
		assertEquals(PAYER_PARTY_REF, payerUpdatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(PAYER_MODEL_PATH), payerUpdatedMapping.getRosettaPath());
		assertNull(payerUpdatedMapping.getRosettaValue());
		assertNull(payerUpdatedMapping.getError());
		assertTrue(payerUpdatedMapping.isCondition());

		Mapping receiverUpdatedMapping = context.getMappings().get(1);
		assertEquals(RECEIVER_XML_PATH, receiverUpdatedMapping.getXmlPath());
		assertEquals(RECEIVER_PARTY_REF, receiverUpdatedMapping.getXmlValue());
		assertEquals(PathUtils.toPath(RECEIVER_MODEL_PATH), receiverUpdatedMapping.getRosettaPath());
		assertNull(receiverUpdatedMapping.getRosettaValue());
		assertNull(receiverUpdatedMapping.getError());
		assertTrue(receiverUpdatedMapping.isCondition());
	}

	private List<Mapping> getMappings(Path payerXmlPath, String payerXmlValue, Path receiverXmlPath, String receiverXmlValue) {
		return Arrays.asList(
				new Mapping(payerXmlPath,
						payerXmlValue,
						null,
						null,
						"no destination",
						false,
						false,
						false),
				new Mapping(receiverXmlPath,
						receiverXmlValue,
						null,
						null,
						"no destination",
						false,
						false,
						false)
		);
	}
}