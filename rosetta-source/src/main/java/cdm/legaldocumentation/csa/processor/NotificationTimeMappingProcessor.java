package cdm.legaldocumentation.csa.processor;

import cdm.base.datetime.BusinessCenterEnum;
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
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.*;

@SuppressWarnings("unused")
public class NotificationTimeMappingProcessor extends MappingProcessor {

    public NotificationTimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
        NotificationTime.NotificationTimeBuilder notificationTimeBuilder = (NotificationTime.NotificationTimeBuilder) builder;
        PARTIES.forEach(party -> getNotificationTimeElection(synonymPath, party).ifPresent(notificationTimeBuilder::addPartyElections));
    }

    public Optional<NotificationTimeElection> getNotificationTimeElection(Path synonymPath, String party) {
        NotificationTimeElection.NotificationTimeElectionBuilder notificationTimeElectionBuilder = NotificationTimeElection.builder();
        
        // party
        setValueAndUpdateMappings(synonymPath.addElement(party + "_" + synonymPath.getLastElement().getPathName()),
                (value) -> notificationTimeElectionBuilder
                        .setParty(toCounterpartyRoleEnum(party)).build());

        BusinessCenterTimeBuilder businessCenterTimeBuilder = notificationTimeElectionBuilder.getOrCreateNotificationTime();

        // hourMinuteTime
        setValueAndUpdateMappings(synonymPath.addElement(party + "_time"),
                value -> businessCenterTimeBuilder.setHourMinuteTime(LocalTime.parse(value)));

        // businessCenter
        setValueAndUpdateMappings(synonymPath.addElement(party + "_location"),
                value -> getSynonymToEnumMap().getEnumValueOptional(BusinessCenterEnum.class, value)
                        .map(Enum::name)
                        .ifPresent(businessCenterTimeBuilder::setBusinessCenterValue));
        
        // customNotification
        setValueAndUpdateMappings(synonymPath.addElement(party + "_specify"),
                value -> notificationTimeElectionBuilder.setCustomNotification(removeHtml(value)));

        return notificationTimeElectionBuilder.hasData() ? Optional.of(notificationTimeElectionBuilder.build()) : Optional.empty();
    }
}
