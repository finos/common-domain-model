package org.isda.cdm.processor;

import com.google.inject.Inject;
import com.regnosys.rosetta.common.hashing.SimpleBuilderProcessor;
import com.rosetta.lib.postprocess.PostProcessorReport;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
import com.rosetta.model.lib.process.PostProcessStep;

import cdm.product.common.functions.ResolveContractualProduct;
import cdm.product.template.ContractualProduct;
import cdm.product.template.TradableProduct;

public class ResolveContractualProductProcessStep implements PostProcessStep {

    @Inject
    private ResolveContractualProduct resolveFunc;

    @Override
    public Integer getPriority() {
        return 3;
    }

    @Override
    public String getName() {
        return "Resolve Contractual Product Quantity";
    }

    @Override
    public <T extends RosettaModelObject> PostProcessorReport runProcessStep(Class<T> topClass, RosettaModelObjectBuilder builder) {

        RosettaPath path = RosettaPath.valueOf(topClass.getSimpleName());

        builder.process(path, new SimpleBuilderProcessor() {
            @Override
            public <R extends RosettaModelObject> boolean processRosetta(RosettaPath path,
                                                                         Class<? extends R> rosettaType,
                                                                         RosettaModelObjectBuilder builder,
                                                                         RosettaModelObjectBuilder parent,
                                                                         AttributeMeta... metas) {
                if (builder == null || !builder.hasData()) return false;
                if (TradableProduct.class.isAssignableFrom(rosettaType)) {
                    TradableProduct.TradableProductBuilder tradableProductBuilder = (TradableProduct.TradableProductBuilder) builder;
                    TradableProduct tradableProduct = tradableProductBuilder.build();
                    ContractualProduct contractualProduct = resolveFunc.evaluate(tradableProduct.getProduct().getContractualProduct(), tradableProduct.getQuantityNotation());
                    tradableProductBuilder.getProduct().setContractualProduct(contractualProduct);
                }
                return true;
            }

            @Override
            public <T> void processBasic(RosettaPath rosettaPath, Class<T> aClass, T t, RosettaModelObjectBuilder rosettaModelObjectBuilder, AttributeMeta... attributeMetas) {
            }

            @Override
            public Report report() {
                return new Report() {
                };
            }
        });

        return () -> builder;
    }


}
