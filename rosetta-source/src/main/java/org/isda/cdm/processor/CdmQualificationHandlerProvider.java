package org.isda.cdm.processor;

import cdm.event.common.BusinessEvent;
import cdm.product.common.ProductIdentification;
import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandlerProvider;

import java.util.Map;
import java.util.Optional;

public class CdmQualificationHandlerProvider implements QualificationHandlerProvider {

    private final Map<Class<?>, QualificationHandler<?, ?, ?>> handlerMap =
            ImmutableMap.<Class<?>, QualificationHandler<?, ?, ?>>builder()
                    .put(ContractualProduct.class, new EconomicTermsQualificationHandler())
                    .put(BusinessEvent.class, new BusinessEventQualificationHandler())
                    .build();

    @Override
    public Map<Class<?>, QualificationHandler<?, ?, ?>> getQualificationHandlerMap() {
        return handlerMap;
    }

    /**
     * Qualification handler for EconomicTerms
     */
    private static class EconomicTermsQualificationHandler implements QualificationHandler<EconomicTerms, ContractualProduct, ContractualProduct.ContractualProductBuilder> {

        @Override
        public Class<EconomicTerms> getQualifiableClass() {
            return EconomicTerms.class;
        }

        @Override
        public EconomicTerms getQualifiableObject(ContractualProduct contractualProduct) {
            return Optional.ofNullable(contractualProduct)
                    .map(ContractualProduct::getEconomicTerms)
                    .orElse(null);
        }

        @Override
        public String getQualifier(ContractualProduct contractualProduct) {
            return Optional.ofNullable(contractualProduct)
                    .map(ContractualProduct::getProductIdentification)
                    .map(ProductIdentification::getProductQualifier)
                    .orElse(null);
        }

        @Override
        public void setQualifier(ContractualProduct.ContractualProductBuilder contractualProductBuilder, String qualifier) {
            contractualProductBuilder
                    .getOrCreateProductIdentification()
                    .setProductQualifier(qualifier);
        }
    }

    /**
     * Qualification handler for BusinessEvent
     */
    private static class BusinessEventQualificationHandler implements QualificationHandler<BusinessEvent, BusinessEvent, BusinessEvent.BusinessEventBuilder> {

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
}
