package org.finos.cdm.qualify;

import cdm.event.common.BusinessEvent;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;

import java.util.Optional;

/**
 * Qualification handler for BusinessEvent
 */
public class BusinessEventQualificationHandler implements QualificationHandler<BusinessEvent, BusinessEvent, BusinessEvent.BusinessEventBuilder> {

    @Override
    public Class<BusinessEvent> getQualifiableClass() {
        return BusinessEvent.class;
    }

    @Override
    public BusinessEvent getQualifiableObject(BusinessEvent businessEvent) {
        return businessEvent;
    }

    @Override
    public String getQualifier(BusinessEvent businessEvent) {
        return Optional.ofNullable(businessEvent)
                .map(BusinessEvent::getEventQualifier)
                .orElse(null);
    }

    @Override
    public void setQualifier(BusinessEvent.BusinessEventBuilder businessEventBuilder, String qualifier) {
        businessEventBuilder.setEventQualifier(qualifier);
    }
}
