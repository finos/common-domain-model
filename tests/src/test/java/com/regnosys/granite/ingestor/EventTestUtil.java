package com.regnosys.granite.ingestor;

import cdm.event.common.EventEffect;
import cdm.event.workflow.WorkflowStep;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Collections;
import java.util.List;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class EventTestUtil {
	static void assertEventEffects(WorkflowStep event) {

		AssertIngestion.KeyCollector globalKeyCollector = new AssertIngestion.KeyCollector();
		event.toBuilder().process(RosettaPath.valueOf("WorkflowStep"), globalKeyCollector);
		List<String> collectedKeys = globalKeyCollector.report.collectedKeys;

		EventEffect eventEffect = event.getBusinessEvent().getEventEffect();

		// EffectedContract
		ofNullable(eventEffect).map(EventEffect::getEffectedTrade).orElse(Collections.emptyList()).forEach(referenceContractKey ->
			assertThat("EventEffect.effectedTrade globalKey does not match", collectedKeys, hasItem(referenceContractKey.getGlobalReference())));
		// Contract
		ofNullable(eventEffect).map(EventEffect::getTrade).orElse(Collections.emptyList()).forEach(contractKey ->
			assertThat("EventEffect.trade globalKey does not match", collectedKeys, hasItem(contractKey.getGlobalReference())));
		// ProductIdentifier
		ofNullable(eventEffect).map(EventEffect::getProductIdentifier).orElse(Collections.emptyList()).forEach(productIdentifierKey ->
			assertThat("EventEffect.Security globalKey does not match", collectedKeys, hasItem(productIdentifierKey.getGlobalReference())));
		// Transfer
		ofNullable(eventEffect).map(EventEffect::getTransfer).orElse(Collections.emptyList()).forEach(transferKey ->
			assertThat("EventEffect.Transfer globalKey does not match", collectedKeys, hasItem(transferKey.getGlobalReference())));
	}
}
