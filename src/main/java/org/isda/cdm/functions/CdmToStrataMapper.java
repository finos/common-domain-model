package org.isda.cdm.functions;

import java.util.Arrays;

import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.RollConventionEnum;

import com.opengamma.strata.basics.schedule.Frequency;
import com.opengamma.strata.basics.schedule.RollConvention;
import com.rosetta.model.lib.annotations.RosettaSynonym;

class CdmToStrataMapper {

    static Frequency getFrequency(CalculationPeriodDates calculationPeriodDates) {
        return Frequency.parse(calculationPeriodDates.getCalculationPeriodFrequency().getPeriodMultiplier().toString() + calculationPeriodDates.getCalculationPeriodFrequency().getPeriod().toString());
    }

    static RollConvention getRollConvention(CalculationPeriodDates calculationPeriodDates) {
        try {
            String field = calculationPeriodDates.getCalculationPeriodFrequency().getRollConvention().name();
            RosettaSynonym[] synonyms = RollConventionEnum.class.getField(field).getAnnotationsByType(RosettaSynonym.class);
            String rollConventionName = Arrays.stream(synonyms)
                    .filter(synonym -> synonym.source().equals("FpML_5_10"))
                    .findFirst()
                    .map(RosettaSynonym::value)
                    .orElseThrow(() -> new CdmToStrataMapperException("No FpML synonym found for [" + field + "]"));

            return RollConvention.extendedEnum().externalNames("FpML").lookup(rollConventionName);

        } catch (NoSuchFieldException e) {
            throw new CdmToStrataMapperException(e);
        }
    }

    private static class CdmToStrataMapperException extends RuntimeException {
        CdmToStrataMapperException(Throwable cause) {
            super(cause);
        }

        CdmToStrataMapperException(String message) {
            super(message);
        }
    }
}
