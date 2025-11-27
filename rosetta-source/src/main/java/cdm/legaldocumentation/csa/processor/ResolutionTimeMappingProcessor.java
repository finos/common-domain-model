package cdm.legaldocumentation.csa.processor;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenterTime;
import cdm.legaldocumentation.csa.NotificationTime;
import cdm.legaldocumentation.csa.NotificationTimeElection;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static cdm.base.datetime.BusinessCenterTime.BusinessCenterTimeBuilder;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.toCounterpartyRoleEnum;

@SuppressWarnings("unused")
public class ResolutionTimeMappingProcessor extends MappingProcessor {

    public ResolutionTimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        System.out.println("hello " + synonymPath.toString() + " " + builder.getType().getSimpleName() + " " + parent.getType().getSimpleName());

        BusinessCenterTime.BusinessCenterTimeBuilder businessCenterTimeBuilder = (BusinessCenterTime.BusinessCenterTimeBuilder) builder;

        // hourMinuteTime
        setValueAndUpdateMappings(synonymPath.addElement("time"),
                value -> businessCenterTimeBuilder.setHourMinuteTime(LocalTime.parse(value)));

        // businessCenter
        setValueAndUpdateMappings(synonymPath.addElement("location"),
                value -> getSynonymToEnumMap().getEnumValueOptional(BusinessCenterEnum.class, value)
                        .map(Enum::name)
                        .ifPresent(businessCenterTimeBuilder::setBusinessCenterValue));
    }
}
