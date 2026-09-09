package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.StubPeriodTypeEnum;
import com.opengamma.strata.basics.schedule.Frequency;
import com.opengamma.strata.basics.schedule.RollConvention;
import com.opengamma.strata.basics.schedule.StubConvention;
import org.checkerframework.checker.units.qual.C;

import java.util.List;

class CdmToStrataMapper {

    static Frequency getFrequency(CalculationPeriodDates calculationPeriodDates) {
        return Frequency.parse(calculationPeriodDates.getCalculationPeriodFrequency().getPeriodMultiplier().toString() + calculationPeriodDates.getCalculationPeriodFrequency().getPeriod().toString());
    }

    static RollConvention getRollConvention(CalculationPeriodDates calculationPeriodDates) {
            String rollConventionName = calculationPeriodDates.getCalculationPeriodFrequency().getRollConvention().toString();
            // The display name of the match RollConvention using FpML
            return RollConvention.extendedEnum().externalNames("FpML").lookup(rollConventionName);
    }

    static StubConvention getStubConvention(List<StubPeriodTypeEnum> stubTypes) {

        if (stubTypes == null || stubTypes.isEmpty()) {
            return null;
        }
        if (stubTypes.size() > 2) {
            throw new IllegalArgumentException("Stub period types list should not contain more than 2 elements.");
        }
        if (stubTypes.size() == 2) {
            return StubConvention.BOTH;
        }
        switch (stubTypes.get(0)) {
            case SHORT_INITIAL: return StubConvention.SHORT_INITIAL;
            case LONG_INITIAL:  return StubConvention.LONG_INITIAL;
            case SHORT_FINAL:   return StubConvention.SHORT_FINAL;
            case LONG_FINAL:    return StubConvention.LONG_FINAL;
            default: throw new IllegalArgumentException("Unknown stub period type: " + stubTypes.get(0));
        }
    }

}
