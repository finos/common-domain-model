package org.isda.cdm.processor;

import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep.KeyPostProcessReport;
import com.regnosys.rosetta.common.hashing.SimpleBuilderProcessor;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.GlobalKeyBuilder;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
import com.rosetta.model.lib.process.BuilderProcessor.Report;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.BusinessEvent.BusinessEventBuilder;

import cdm.base.staticdata.asset.common.ProductIdentifier.ProductIdentifierBuilder;
import cdm.base.staticdata.asset.common.metafields.ReferenceWithMetaProductIdentifier;
import cdm.base.staticdata.asset.common.metafields.ReferenceWithMetaProductIdentifier.ReferenceWithMetaProductIdentifierBuilder;

import org.isda.cdm.Contract.ContractBuilder;
import org.isda.cdm.EventEffect.EventEffectBuilder;
import org.isda.cdm.Execution.ExecutionBuilder;
import org.isda.cdm.TransferPrimitive.TransferPrimitiveBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaContract;
import org.isda.cdm.metafields.ReferenceWithMetaContract.ReferenceWithMetaContractBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaExecution;
import org.isda.cdm.metafields.ReferenceWithMetaExecution.ReferenceWithMetaExecutionBuilder;
import org.isda.cdm.metafields.ReferenceWithMetaTransferPrimitive;
import org.isda.cdm.metafields.ReferenceWithMetaTransferPrimitive.ReferenceWithMetaTransferPrimitiveBuilder;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;


public class EventEffectProcessStep implements PostProcessStep{
	
	static final RosettaPath BEFORE = RosettaPath.valueOf("primitive.*.before");
	static final RosettaPath AFTER = RosettaPath.valueOf("primitive.*.after");
	static final RosettaPath ANY = RosettaPath.valueOf("*");
	static final RosettaPath FORWARD_CONTRACT_PATHS = RosettaPath.valueOf("forwardPayout.product.contract");
	static final RosettaPath FORWARD_CONTRACTUAL_PRODUCT_PATHS = RosettaPath.valueOf("forwardPayout.product.contractualProduct");

	
	private static Map<BiPredicate<RosettaPath, Class<?>>, BiConsumer<EventEffectBuilder,String>> effectSetters  = 
			ImmutableMap.<BiPredicate<RosettaPath, Class<?>>, BiConsumer<EventEffectBuilder,String>>builder()
			.put(matches(BEFORE, ContractBuilder.class), (EventEffectBuilder e,String s) -> e.addEffectedContractBuilder(contractRef(s)))
			.put(matches(AFTER, ContractBuilder.class), (EventEffectBuilder e,String s) -> e.addContractBuilder(contractRef(s)))
			.put(matches(BEFORE, ExecutionBuilder.class), (EventEffectBuilder e,String s) -> e.addEffectedExecutionBuilder(executionRef(s)))
			.put(matches(AFTER, ExecutionBuilder.class), (EventEffectBuilder e,String s) -> e.addExecutionBuilder(executionRef(s)))
			.put(matches(ANY, ProductIdentifierBuilder.class), (EventEffectBuilder e,String s) -> e.addProductIdentifierBuilder(productRef(s)))
			.put(matches(ANY, TransferPrimitiveBuilder.class), (EventEffectBuilder e,String s) -> e.addTransferBuilder(transferRef(s)))
			.build();
	
	private static BiPredicate<RosettaPath, Class<?>> matches(RosettaPath matchPath, Class<?> matchClass) {
		return (RosettaPath p,Class<?> c)-> {
				return matchClass.isAssignableFrom(c) 
				&& p.containsPath(matchPath) 
				&& !p.containsPath(FORWARD_CONTRACT_PATHS) && !p.containsPath(FORWARD_CONTRACTUAL_PRODUCT_PATHS);};
	}

	private final RosettaKeyProcessStep keyProcessor;

	public EventEffectProcessStep(RosettaKeyProcessStep keyProcessor) {
		this.keyProcessor = keyProcessor;
	}

	@Override
	public Integer getPriority() {
		return 3;
	}

	@Override
	public String getName() {
		return "Event Effect Processor";
	}

	@Override
	public <T extends RosettaModelObject> PostProcessorReport runProcessStep(Class<T> topClass,
			RosettaModelObjectBuilder builder) {
		RosettaPath path = RosettaPath.valueOf(topClass.getSimpleName());
		EventEffectPostProcessReport report = new EventEffectPostProcessReport();
		EventEffectProcessor processor = new EventEffectProcessor(report,  keyProcessor.runProcessStep(topClass, builder));
		processor.processRosetta(path, topClass, builder, null);
		builder.process(path, processor);
		return report;
	}

	private static ReferenceWithMetaContractBuilder contractRef(String key) {
		ReferenceWithMetaContractBuilder contractReference = ReferenceWithMetaContract.builder().setGlobalReference(key);
		return contractReference;
	}
	
	private static ReferenceWithMetaExecutionBuilder executionRef(String key) {
		ReferenceWithMetaExecutionBuilder contractReference = ReferenceWithMetaExecution.builder().setGlobalReference(key);
		return contractReference;
	}

	private static ReferenceWithMetaProductIdentifierBuilder productRef(String key) {
		ReferenceWithMetaProductIdentifierBuilder contractReference = ReferenceWithMetaProductIdentifier.builder().setGlobalReference(key);
		return contractReference;
	}

	private static ReferenceWithMetaTransferPrimitiveBuilder transferRef(String key) {
		ReferenceWithMetaTransferPrimitiveBuilder contractReference = ReferenceWithMetaTransferPrimitive.builder().setGlobalReference(key);
		return contractReference;
	}
	
	private class EventEffectProcessor extends SimpleBuilderProcessor {

		private final EventEffectPostProcessReport report;
		private final Map<RosettaPath, GlobalKeyBuilder<?>> globalKeyMap;

		public EventEffectProcessor(EventEffectPostProcessReport report, KeyPostProcessReport keyPostProcessReport) {
			this.report = report;
			globalKeyMap = keyPostProcessReport.getKeyMap();
		}

		@Override
		public <R extends RosettaModelObject> boolean processRosetta(RosettaPath path, Class<? extends R> rosettaType,
				RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent, AttributeMeta... metas) {
			if (builder instanceof BusinessEventBuilder && builder.hasData()) {
				((BusinessEventBuilder) builder).getOrCreateEventEffect();
			}
			if (builder instanceof EventEffectBuilder) {
				EventEffectBuilder eventEffect = (EventEffectBuilder) builder;
				for (Entry<RosettaPath, GlobalKeyBuilder<?>> entry:globalKeyMap.entrySet()) {
					for (Entry<BiPredicate<RosettaPath, Class<?>>, BiConsumer<EventEffectBuilder, String>> test:effectSetters.entrySet()) {
						if (test.getKey().test(entry.getKey(), entry.getValue().getClass())) {
							test.getValue().accept(eventEffect, entry.getValue().getMeta().getGlobalKey());
						}
					}
				}
			}
			return true;
		}

		@Override
		public <T> void processBasic(RosettaPath path, Class<T> rosettaType, T instance,
				RosettaModelObjectBuilder parent, AttributeMeta... metas) {
		}

		@Override
		public Report report() {
			return report;
		}
		
	}
	
	class EventEffectPostProcessReport implements Report, PostProcessorReport {
		@Override
		public RosettaModelObjectBuilder getResultObject() {
			return null;
		}
		
	}

}
