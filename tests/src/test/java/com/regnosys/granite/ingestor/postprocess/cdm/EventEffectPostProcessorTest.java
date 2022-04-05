package com.regnosys.granite.ingestor.postprocess.cdm;

import cdm.event.common.TradeState;
import cdm.event.common.TransferPrimitive;
import cdm.event.common.TransferPrimitive.TransferPrimitiveBuilder;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.WorkflowStep.WorkflowStepBuilder;
import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep.KeyPostProcessReport;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.processor.EventEffectProcessStep;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EventEffectPostProcessorTest {

	private static GlobalKeyProcessStep globalKeyCollector = Mockito.mock(GlobalKeyProcessStep.class);
	private EventEffectProcessStep unit = new EventEffectProcessStep(globalKeyCollector);
	
	private static final Date EPOCH = Date.of(LocalDate.EPOCH);
	
	@SuppressWarnings("unchecked")
	@Test
	void shouldAddGlobalKeysForNovationEvent() {
		WorkflowStepBuilder event = WorkflowStep.builder();
		event.getOrCreateBusinessEvent().setEffectiveDate(EPOCH);

		KeyPostProcessReport keyReport = globalKeyCollector.new KeyPostProcessReport(null,
				ImmutableMap.<RosettaPath, GlobalKey>builder()
				.put(RosettaPath.valueOf("primitive.inception.after"), tradeState("test-hash-contract-1"))
				.put(RosettaPath.valueOf("primitive.quantityChange(0).before"), tradeState("test-hash-contract-2"))
				.put(RosettaPath.valueOf("primitive.quantityChange(0).after"), tradeState("test-hash-contract-3"))
				.build());
		
		when(globalKeyCollector.runProcessStep(any(Class.class), any(RosettaModelObjectBuilder.class)))
		.thenReturn(keyReport);

		unit.runProcessStep(WorkflowStep.class, event);

		assertThat(event.getBusinessEvent().getEventEffect().getTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				containsInAnyOrder("test-hash-contract-1", "test-hash-contract-3"));
		assertThat(event.getBusinessEvent().getEventEffect().getEffectedTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				contains("test-hash-contract-2"));
	}

	
	@SuppressWarnings("unchecked")
	@Test
	void shouldAddGlobalKeysForPaymentEvent() {
		WorkflowStepBuilder event = WorkflowStep.builder();
		event.getOrCreateBusinessEvent().setEffectiveDate(EPOCH);

		KeyPostProcessReport keyReport = globalKeyCollector.new KeyPostProcessReport(null,
				ImmutableMap.<RosettaPath, GlobalKey>builder()
				.put(RosettaPath.valueOf("primitive.payment(0)"), transfer("test-hash-payment-1"))
				.build());
		
		when(globalKeyCollector.runProcessStep(any(Class.class), any(RosettaModelObjectBuilder.class)))
		.thenReturn(keyReport);

		unit.runProcessStep(WorkflowStep.class, event);

		assertThat(event.getBusinessEvent().getEventEffect().getTransfer().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				contains("test-hash-payment-1"));
	}

	@SuppressWarnings("unchecked")
	@Test
	void shouldAddGlobalKeysForQuantityChangeEvent() {
		WorkflowStepBuilder event = WorkflowStep.builder();
		event.getOrCreateBusinessEvent().setEffectiveDate(EPOCH);

		KeyPostProcessReport keyReport = globalKeyCollector.new KeyPostProcessReport(null,
				ImmutableMap.<RosettaPath, GlobalKey>builder()
				.put(RosettaPath.valueOf("primitive.quantityChange(0).before"), tradeState("test-hash-contract-1"))
				.put(RosettaPath.valueOf("primitive.quantityChange(0).after"), tradeState("test-hash-contract-2"))
				.put(RosettaPath.valueOf("primitive.quantityChange(1).before"), tradeState("test-hash-execution-1"))
				.put(RosettaPath.valueOf("primitive.quantityChange(1).after"), tradeState("test-hash-execution-2"))
				.build());
		
		when(globalKeyCollector.runProcessStep(any(Class.class), any(RosettaModelObjectBuilder.class)))
		.thenReturn(keyReport);

		unit.runProcessStep(WorkflowStep.class, event);

		assertThat(event.getBusinessEvent().getEventEffect().getEffectedTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				hasItem("test-hash-contract-1"));
		assertThat(event.getBusinessEvent().getEventEffect().getTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				hasItem("test-hash-contract-2"));
		assertThat(event.getBusinessEvent().getEventEffect().getEffectedTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				hasItem("test-hash-execution-1"));
		assertThat(event.getBusinessEvent().getEventEffect().getTrade().stream().map(c->c.getGlobalReference()).collect(Collectors.toList()),
				hasItem("test-hash-execution-2"));
	}

	private GlobalKey tradeState(String key) {
		TradeState.TradeStateBuilder builder = TradeState.builder();
		builder.getOrCreateMeta().setGlobalKey(key);
		return builder;
	}

	private GlobalKey transfer(String key) {
		TransferPrimitiveBuilder builder= TransferPrimitive.builder();
		builder.getOrCreateMeta().setGlobalKey(key);
		return builder;
	}
}