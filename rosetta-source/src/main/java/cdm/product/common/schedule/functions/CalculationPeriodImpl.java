package cdm.product.common.schedule.functions;

import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodData.CalculationPeriodDataBuilder;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.common.collect.Iterables;
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
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import static cdm.product.common.schedule.functions.CdmToStrataMapper.getFrequency;
import static cdm.product.common.schedule.functions.CdmToStrataMapper.getRollConvention;

/**
 * TODO - Move this to the CDM
 */
public class CalculationPeriodImpl extends CalculationPeriod {

    private static Logger LOGGER = LoggerFactory.getLogger(CalculationPeriod.class);


    @Override
    protected CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
        CalculationPeriodDataBuilder builder = CalculationPeriodData.builder();


        return builder;
    }

}
