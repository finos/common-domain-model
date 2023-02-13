package cdm.base.staticdata.party.processor;

import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.subPath;
import static com.regnosys.rosetta.common.util.PathUtils.toPath;

public class CreditPartyMappingHelper {

    public static boolean isCreditFundingLeg(RosettaPath modelPath, Path synonymPath) {
        boolean interestRatePayout = subPath("interestRatePayout", toPath(modelPath)).isPresent();
        boolean creditDefaultSwapGeneralTerms = synonymPath.getParent().endsWith("creditDefaultSwap", "generalTerms");
        return interestRatePayout && creditDefaultSwapGeneralTerms;
    }

    public static boolean isFra(RosettaPath modelPath, Path synonymPath) {
        boolean interestRatePayout = subPath("interestRatePayout", toPath(modelPath)).isPresent();
        boolean fra = synonymPath.getParent().endsWith("fra");
        return interestRatePayout && fra;
    }
}
