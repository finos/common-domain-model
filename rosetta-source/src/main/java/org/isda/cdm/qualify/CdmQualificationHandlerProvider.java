package org.isda.cdm.qualify;

import cdm.event.common.BusinessEvent;
import cdm.product.template.NonTransferableProduct;
import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandlerProvider;

import java.util.Map;

public class CdmQualificationHandlerProvider implements QualificationHandlerProvider {

    private final Map<Class<?>, QualificationHandler<?, ?, ?>> handlerMap =
            ImmutableMap.<Class<?>, QualificationHandler<?, ?, ?>>builder()
                    .put(NonTransferableProduct.class, new EconomicTermsQualificationHandler())
                    .put(BusinessEvent.class, new BusinessEventQualificationHandler())
                    .build();

    @Override
    public Map<Class<?>, QualificationHandler<?, ?, ?>> getQualificationHandlerMap() {
        return handlerMap;
    }
}
