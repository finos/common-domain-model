package cdm.product.template.processor;

import cdm.product.template.OptionPayout;
import cdm.product.template.OptionTypeEnum;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static cdm.base.staticdata.party.processor.BuyerSellerPartyHelper.isBuyerReceiver;

@SuppressWarnings("unused")
public class SwaptionOptionTypeMappingProcessor extends MappingProcessor {
    
    public SwaptionOptionTypeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public <T> void mapBasic(Path synonymPath, Optional<T> instance, RosettaModelObjectBuilder parent) {
        ((OptionPayout.OptionPayoutBuilder) parent).setOptionType(getOptionType(synonymPath));
    }

    private OptionTypeEnum getOptionType(Path synonymPath) {
        return isBuyerReceiver(synonymPath, getModelPath()) ? OptionTypeEnum.CALL : OptionTypeEnum.PUT;
    }
}
