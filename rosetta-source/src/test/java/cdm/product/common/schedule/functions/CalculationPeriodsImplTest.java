package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculationPeriodsImplTest extends AbstractFunctionTest {

    @Inject CalculationPeriods calculationPeriods;

    private final CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
            .setEffectiveDate((AdjustableOrRelativeDate.builder()
                    .setAdjustableDate(AdjustableDate.builder()
                            .setUnadjustedDate(Date.of(2018, 1, 3))
                            .setDateAdjustments(BusinessDayAdjustments.builder()
                                    .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                    .build())
                            .build())
                    .build()))
            .setTerminationDate(AdjustableOrRelativeDate.builder()
                    .setAdjustableDate(AdjustableDate.builder()
                            .setUnadjustedDate(Date.of(2020, 1, 3))
                            .setDateAdjustments(BusinessDayAdjustments.builder()
                                    .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                    .setBusinessCenters(BusinessCenters.builder()
                                            .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                    .setExternalReference("primaryBusinessCenters")
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                    .setRollConvention(RollConventionEnum._3)
                    .setPeriodMultiplier(3)
                    .setPeriod(PeriodExtendedEnum.M)
                    .build())
            .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                    .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                    .setBusinessCenters(BusinessCenters.builder()
                            .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                    .setExternalReference("primaryBusinessCenters")
                                    .build())
                            .build())
                    .build())
            .build();

    private final CalculationPeriodDates calculationPeriodDates2 = CalculationPeriodDates.builder()
            .setEffectiveDate((AdjustableOrRelativeDate.builder()
                    .setAdjustableDate(AdjustableDate.builder()
                            .setUnadjustedDate(Date.of(2020, 4, 27))
                            .setDateAdjustments(BusinessDayAdjustments.builder()
                                    .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                    .build())
                            .build())
                    .build()))
            .setTerminationDate(AdjustableOrRelativeDate.builder()
                    .setAdjustableDate(AdjustableDate.builder()
                            .setUnadjustedDate(Date.of(2022, 4, 27))
                            .setDateAdjustments(BusinessDayAdjustments.builder()
                                    .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                    .setBusinessCenters(BusinessCenters.builder()
                                            .addBusinessCenter(FieldWithMetaString.builder()
                                                    .setValue("EUTA")
                                                    .build())
                                            .build())
                                    .build())
                            .build())
                    .build())
            .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                    .setRollConvention(RollConventionEnum._27)
                    .setPeriod(PeriodExtendedEnum.M)
                    .setPeriodMultiplier(2)
                    .build())
            .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                    .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                    .setBusinessCenters(BusinessCenters.builder()
                            .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                    .setExternalReference("primaryBusinessCenters")
                                    .build())
                            .build())
                    .build())
            .build();

    @Test
    void shouldReturnStartAndEndDateOfFirstPeriod() {
        List<? extends CalculationPeriodData> periods = calculationPeriods.evaluate(calculationPeriodDates);

        CalculationPeriodData usingStartDate = periods.get(0);
        assertThat(usingStartDate.getStartDate(), is(Date.of(2018, 1, 3)));
        assertThat(usingStartDate.getEndDate(), is(Date.of(2018, 4, 3)));
        assertEquals(periods.size(), 8);
    }
}

