package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CounterpartyMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterpartyMappingProcessor.class);

	public CounterpartyMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override public <R extends RosettaModelObject> boolean processRosetta(RosettaPath currentPath, Class<? extends R> rosettaType,
			RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		return super.processRosetta(currentPath, rosettaType, builder, parent, meta);
	}

	@Override public <R extends RosettaModelObject> boolean processRosetta(RosettaPath currentPath, Class<? extends R> rosettaType,
			List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		return super.processRosetta(currentPath, rosettaType, builder, parent, meta);
	}

	@Override public <T> void processBasic(RosettaPath currentPath, Class<T> rosettaType, T instance, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
//		if (RosettaPath.valueOf("Contract.tradableProduct.product.contractualProduct.economicTerms.payout.interestRatePayout(1).payerReceiver.payer").matchesIgnoringIndex(currentPath)) {
//			System.out.println("blah");
//		}
		super.processBasic(currentPath, rosettaType, instance, parent, meta);

		List<Path> xmlPathsToHere = null;
		List<String> synonymValues = xmlPathsToHere.stream()
				.map(xmlPath -> Stream.of("top.leg")
						.map(Path::parse)
						.map(xmlPath::append)
						.map(Path::toString))
				.flatMap(i -> i)
				.collect(Collectors.toList());
	}

	@Override public <T> void processBasic(RosettaPath currentPath, Class<T> rosettaType, List<T> instance, RosettaModelObjectBuilder parent,
			AttributeMeta... meta) {
		super.processBasic(currentPath, rosettaType, instance, parent, meta);
	}

	@Override
	protected <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		LOGGER.info("****1 {} {}", builder, parent);
	}

	@Override
	protected <T> void mapBasic(T value, RosettaModelObjectBuilder parent) {
		LOGGER.info("****2 {} {}", value, parent);
	}
}
