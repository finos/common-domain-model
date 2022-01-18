package cdm.event.common.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.regnosys.rosetta.common.util.PathUtils;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;

@SuppressWarnings("unused")
public class NovationTradeDateMappingProcessor extends MappingProcessor {

	private final boolean isContractFormationAfterTrade;

	public NovationTradeDateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.isContractFormationAfterTrade =
				Path.parse("WorkflowStep.businessEvent.primitives.contractFormation.after").nameStartMatches(PathUtils.toPath(modelPath));
	}

	@Override
	public <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		if (isContractFormationAfterTrade) {
			setValueAndUpdateMappings(synonymPath,
					xmlValue -> {
						Date novationTradeDate = DateImpl.of(LocalDate.parse(xmlValue, DateTimeFormatter.ISO_LOCAL_DATE));
						((FieldWithMetaDate.FieldWithMetaDateBuilder) parent).setValue(novationTradeDate);
						// remove old mapping
						getNonNullMappingForModelPath(getMappings(), PathUtils.toPath(getModelPath())).ifPresent(getMappings()::remove);
					});
		}
	}
}
