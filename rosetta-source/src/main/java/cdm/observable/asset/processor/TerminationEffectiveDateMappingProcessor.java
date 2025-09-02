package cdm.observable.asset.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;

import java.util.List;

import static cdm.base.datetime.AdjustableOrRelativeDate.AdjustableOrRelativeDateBuilder;

/**
 * FpML mapper.  Sets
 */
@SuppressWarnings("unused") // used in generated code
public class TerminationEffectiveDateMappingProcessor extends MappingProcessor {

    public TerminationEffectiveDateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        if (isTermination(synonymPath) && isChangeInstruction()) {
            setValueAndUpdateMappings(synonymPath,
                    xmlValue -> {
                        AdjustableOrRelativeDateBuilder adjustableOrRelativeDateBuilder = (AdjustableOrRelativeDateBuilder) builder;
                        adjustableOrRelativeDateBuilder.getOrCreateAdjustableDate().setAdjustedDateValue(parseDate(xmlValue));
                    });
        }
    }

    private Date parseDate(String dateString) {
        if (dateString.endsWith("Z")) {
            dateString = dateString.substring(0, dateString.length() - 1);
        }
        return Date.parse(dateString);
    }

    private boolean isTermination(Path synonymPath) {
        Path parentPath = synonymPath.getParent();
        return "termination".equals(parentPath.getLastElement().getPathName());
    }

    private boolean isChangeInstruction() {
        return "change".equals(getModelPath().getParent().getElement().getPath());
    }
    
    
}
