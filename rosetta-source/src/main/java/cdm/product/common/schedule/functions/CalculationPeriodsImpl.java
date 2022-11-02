package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.common.collect.ImmutableList;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.schedule.PeriodicSchedule;
import com.opengamma.strata.basics.schedule.Schedule;
import com.opengamma.strata.basics.schedule.SchedulePeriod;
import com.opengamma.strata.basics.schedule.StubConvention;
import com.rosetta.model.lib.records.Date;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static cdm.product.common.schedule.functions.AdjustableDateUtils.adjustDate;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

public class CalculationPeriodsImpl extends CalculationPeriods {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);

    @Override
    protected List<CalculationPeriodData.CalculationPeriodDataBuilder> doEvaluate(CalculationPeriodDates calculationPeriodDates) {

        List<CalculationPeriodData.CalculationPeriodDataBuilder> returnVal = new ArrayList<>();

        return returnVal;
    }

}
