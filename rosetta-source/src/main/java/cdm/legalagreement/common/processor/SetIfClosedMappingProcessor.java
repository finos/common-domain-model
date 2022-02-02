package cdm.legalagreement.common.processor;

import cdm.legalagreement.common.ClosedState;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class SetIfClosedMappingProcessor extends MappingProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetIfClosedMappingProcessor.class);

	public SetIfClosedMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		ClosedState.ClosedStateBuilder closedState = (ClosedState.ClosedStateBuilder) parent;
		if (closedState.getState() == null) {
			remove(synonymPath, "activityDate", closedState::setActivityDate);
			remove(synonymPath, "effectiveDate", closedState::setEffectiveDate);
			remove(synonymPath, "lastPaymentDate", closedState::setLastPaymentDate);
		}
	}

	private void remove(Path synonymPath, String attribute, Consumer<Date> setter) {
		if (getModelPath().getElement().getPath().equals(attribute)) {
			setter.accept(null);
			getNonNullMapping(synonymPath).ifPresent(m -> getMappings().remove(m));
		}
	}

	private Optional<Mapping> getNonNullMapping(Path synonymPath) {
		Path modelPath = PathUtils.toPath(getModelPath());
		return getMappings().stream()
				.filter(m -> m.getRosettaPath() != null)
				.filter(m -> m.getRosettaPath().nameIndexMatches(modelPath))
				.filter(m -> synonymPath.nameIndexMatches(m.getXmlPath()))
				.filter(m -> m.getXmlValue() != null)
				.findFirst();
	}
}
