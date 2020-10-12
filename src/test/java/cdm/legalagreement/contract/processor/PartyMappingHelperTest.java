package cdm.legalagreement.contract.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.product.template.TradableProduct;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static cdm.base.staticdata.party.PayerReceiver.PayerReceiverBuilder;
import static cdm.product.template.TradableProduct.TradableProductBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class PartyMappingHelperTest {

	private static final Path PAYER_XML_PATH = Path.parse("dataDocument.trade.swap.swapStream[0].payerPartyReference.href");
	private static final Path RECEIVER_XML_PATH = Path.parse("dataDocument.trade.swap.swapStream[0].receiverPartyReference.href");

	private static final RosettaPath PAYER_MODEL_PATH = RosettaPath.valueOf(
			"Contract.tradableProduct.product.contractualProduct.economicTerms.payout.interestRatePayout(0).payerReceiver.payer");
	private static final RosettaPath RECEIVER_MODEL_PATH = RosettaPath.valueOf(
			"Contract.tradableProduct.product.contractualProduct.economicTerms.payout.interestRatePayout(0).payerReceiver.receiver");

	private static final String PAYER_PARTY_REF = "p1";
	private static final String RECEIVER_PARTY_REF = "p2";

	private MappingContext context;

	@BeforeEach
	void setUp() {
		context = new MappingContext(getMappings(PAYER_XML_PATH, PAYER_PARTY_REF, RECEIVER_XML_PATH, RECEIVER_PARTY_REF), Collections.emptyMap());
	}

	@Test
	void shouldMapPayerToParty1() {
		PartyMappingHelper helper = new PartyMappingHelper(context, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();
		Path synonymPath = PAYER_XML_PATH.getParent();
		helper.setCounterpartyEnum(PAYER_MODEL_PATH, synonymPath, builder::setPayer);

		assertEquals(CounterpartyEnum.PARTY_1, builder.getPayer());
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
		PartyMappingHelper helper = new PartyMappingHelper(context, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();
		Path synonymPath = RECEIVER_XML_PATH.getParent();
		helper.setCounterpartyEnum(RECEIVER_MODEL_PATH, synonymPath, builder::setReceiver);

		assertEquals(CounterpartyEnum.PARTY_1, builder.getReceiver());
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
	void shouldMapPayerToParty1AndReceiverToParty2() throws ExecutionException, InterruptedException, TimeoutException {
		PartyMappingHelper helper = new PartyMappingHelper(context, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();

		// test
		Path payerSynonymPath = PAYER_XML_PATH.getParent();
		helper.setCounterpartyEnum(PAYER_MODEL_PATH, payerSynonymPath, builder::setPayer);

		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		// test
		Path receiverSynonymPath = RECEIVER_XML_PATH.getParent();
		helper.setCounterpartyEnum(RECEIVER_MODEL_PATH, receiverSynonymPath, builder::setReceiver);

		assertEquals(CounterpartyEnum.PARTY_1, builder.getPayer());
		assertEquals(CounterpartyEnum.PARTY_2, builder.getReceiver());

		assertTrue(helper.getBothCounterpartiesCollectedFuture().isDone());
		Map<String, CounterpartyEnum> partyExternalReferenceToCounterpartyEnumMap = helper.getBothCounterpartiesCollectedFuture().get();
		assertNotNull(partyExternalReferenceToCounterpartyEnumMap);
		assertEquals(CounterpartyEnum.PARTY_1, partyExternalReferenceToCounterpartyEnumMap.get(PAYER_PARTY_REF));
		assertEquals(CounterpartyEnum.PARTY_2, partyExternalReferenceToCounterpartyEnumMap.get(RECEIVER_PARTY_REF));

		 // test PartyMappingHelper.addCounterparties()
		TradableProductBuilder tradableProductBuilder = TradableProduct.builder();
		helper.supplyTradableProductBuilder(tradableProductBuilder);
		helper.addCounterparties();

		// wait for invoked tasks to complete before assertions
		assertEquals(1, context.getInvokedTasks().size());
		context.getInvokedTasks().get(0).get(1000, TimeUnit.MILLISECONDS);

		List<Counterparty.CounterpartyBuilder> counterparties = tradableProductBuilder.getCounterparties();
		assertThat(counterparties, hasSize(2));
		// TODO add equals and hashcode to ReferenceWithMeta objects
//		assertThat(counterparties,
//				hasItems(
//						Counterparty.builder()
//								.setCounterparty(CounterpartyEnum.PARTY_1)
//								.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(PAYER_PARTY_REF).build()),
//						Counterparty.builder()
//								.setCounterparty(CounterpartyEnum.PARTY_2)
//								.setPartyReference(ReferenceWithMetaParty.builder().setExternalReference(RECEIVER_PARTY_REF).build())));

		Counterparty.CounterpartyBuilder counterparty1 = counterparties.get(0);
		assertEquals(CounterpartyEnum.PARTY_1, counterparty1.getCounterparty());
		assertEquals(PAYER_PARTY_REF, counterparty1.getOrCreatePartyReference().getExternalReference());

		Counterparty.CounterpartyBuilder counterparty2 = counterparties.get(1);
		assertEquals(CounterpartyEnum.PARTY_2, counterparty2.getCounterparty());
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

	@Test
	void shouldMapCounterpartyBecauseModelPathOutsideProduct() {
		PartyMappingHelper helper = new PartyMappingHelper(context, null);

		PayerReceiverBuilder builder = PayerReceiver.builder();
		Path synonymPath = PAYER_XML_PATH.getParent();
		helper.setCounterpartyEnum(RosettaPath.valueOf("Contract.collateral.independentAmount.payer"), synonymPath, builder::setPayer);

		assertNull(builder.getPayer());
		assertFalse(helper.getBothCounterpartiesCollectedFuture().isDone());

		Mapping updatedMapping = context.getMappings().get(0);
		assertEquals(PAYER_XML_PATH, updatedMapping.getXmlPath());
		assertEquals(PAYER_PARTY_REF, updatedMapping.getXmlValue());
		assertNull(updatedMapping.getRosettaPath());
		assertNull(updatedMapping.getRosettaValue());
		assertEquals("no destination", updatedMapping.getError());
		assertFalse(updatedMapping.isCondition());
	}

	@NotNull
	private List<Mapping> getMappings(Path payerXmlPath, String payerXmlValue, Path receiverXmlPath, String receiverXmlValue) {
		return Arrays.asList(
				new Mapping(payerXmlPath,
						payerXmlValue,
						null,
						null,
						"no destination",
						false,
						false),
				new Mapping(receiverXmlPath,
						receiverXmlValue,
						null,
						null,
						"no destination",
						false,
						false)
		);
	}
}