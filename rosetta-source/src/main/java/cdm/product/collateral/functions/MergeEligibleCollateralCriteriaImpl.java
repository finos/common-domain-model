package cdm.product.collateral.functions;

import cdm.product.collateral.EligibleCollateralCriteria;
import com.regnosys.rosetta.common.merging.SimpleMerger;

public class MergeEligibleCollateralCriteriaImpl extends MergeEligibleCollateralCriteria {

    @Override
    protected EligibleCollateralCriteria.EligibleCollateralCriteriaBuilder doEvaluate(EligibleCollateralCriteria criteria1, EligibleCollateralCriteria criteria2) {
        EligibleCollateralCriteria.EligibleCollateralCriteriaBuilder mergedCriteria = criteria1.build().toBuilder();
        // merge criteria2 onto mergedCriteria
        new SimpleMerger().run(mergedCriteria, criteria2.toBuilder());
        return mergedCriteria;
    }
}
