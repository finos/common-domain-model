package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Arrays;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

public class BuyerSellerPartyHelper {

    public static boolean isBuyerAsReceiver(Path synonymPath, RosettaPath modelPath) {
        return !(isCreditFundingLeg(modelPath, synonymPath) || isFra(modelPath, synonymPath));
    }

    public static boolean isSellerAsPayer(Path synonymPath, RosettaPath modelPath) {
        return !(isCreditFundingLeg(modelPath, synonymPath) || isFra(modelPath, synonymPath));
    }
    
    private static boolean isCreditFundingLeg(RosettaPath modelPath, Path synonymPath) {
        boolean interestRatePayout = subPath("InterestRatePayout", toPath(modelPath)).isPresent();
        boolean creditDefaultSwapGeneralTerms = subPath("generalTerms", synonymPath)
                .map(path -> path.endsWith("creditDefaultSwap", "generalTerms"))
                .orElse(false);
        return interestRatePayout && creditDefaultSwapGeneralTerms;
    }

    private static boolean isFra(RosettaPath modelPath, Path synonymPath) {
        boolean interestRatePayout = subPath("InterestRatePayout", toPath(modelPath)).isPresent();
        boolean fra = Arrays.stream(synonymPath.getPathNames()).anyMatch("fra"::equals);
        return interestRatePayout && fra;
    }
}
