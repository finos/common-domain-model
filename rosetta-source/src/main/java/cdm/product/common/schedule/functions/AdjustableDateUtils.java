package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.HolidayCalendarIds;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO - Move this to the CDM
 */
public class AdjustableDateUtils {

    /**
     * If the input date is already adjusted, then return it for both adjustable and relative dates
     * If the input date is unadjusted, then adjust it using the BusinessDayConvention and basic weekdays holiday calendar
     * If the input date is relative, then it will not be adjusted. Not supported yet.
     *
     * @param adjustableOrRelativeDate
     * @return the adjusted date
     */
    public static Date adjustDate(AdjustableOrRelativeDate adjustableOrRelativeDate) {
        if (adjustableOrRelativeDate == null) {
            return null;
        }
        if (adjustableOrRelativeDate.getAdjustableDate() != null) {
            return adjustDate(adjustableOrRelativeDate.getAdjustableDate());
        }
        if (adjustableOrRelativeDate.getRelativeDate() != null) {
            return adjustDate(adjustableOrRelativeDate.getRelativeDate());
        }
        return null;
    }

    public static Date adjustDate(AdjustableDate adjustableDate) {
        if (adjustableDate == null) {
            return null;
        }
        if (adjustableDate.getAdjustedDate() != null) {
            return adjustableDate.getAdjustedDate().getValue();
        }
        if (adjustableDate.getUnadjustedDate() != null) {
            return adjustDate(adjustableDate.getUnadjustedDate(), adjustableDate.getDateAdjustments());
        }
        return null;
    }

    public static Date adjustDate(Date unadjustedDate, BusinessDayAdjustments businessDayAdjustments) {
        if (unadjustedDate != null) {
            BusinessDayConventionEnum businessDayConvention = Optional.ofNullable(businessDayAdjustments)
                    .map(BusinessDayAdjustments::getBusinessDayConvention)
                    .orElse(BusinessDayConventionEnum.NONE);

            return Date.of(BusinessDayAdjustment.builder()
                    .convention(toBusinessDayConvention(businessDayConvention))
                    .calendar(HolidayCalendarIds.SAT_SUN).build()
                    .adjust(unadjustedDate.toLocalDate(), ReferenceData.minimal()));
        }
        return null;
    }

    public static Date adjustDate(AdjustedRelativeDateOffset relativeDate) {
        if (relativeDate == null) {
            return null;
        }
        if (relativeDate.getAdjustedDate() != null) {
            return relativeDate.getAdjustedDate();
        }
        // adjusting relative dates are not supported yet
        return null;
    }

    public static List<Date> adjustDates(AdjustableRelativeOrPeriodicDates adjustableRelativeOrPeriodicDates) {
        if (adjustableRelativeOrPeriodicDates == null) {
            return null;
        }
        if (adjustableRelativeOrPeriodicDates.getAdjustableDates() != null) {
            AdjustableDates adjustableDates = adjustableRelativeOrPeriodicDates.getAdjustableDates();
            if (adjustableDates.getAdjustedDate() != null) {
                return adjustableDates.getAdjustedDate().stream()
                        .map(FieldWithMetaDate::getValue)
                        .collect(Collectors.toList());
            }
            if (adjustableDates.getUnadjustedDate() != null) {
                return adjustableDates.getUnadjustedDate().stream()
                        .map(unadjustedDate -> adjustDate(unadjustedDate, adjustableDates.getDateAdjustments()))
                        .collect(Collectors.toList());
            }
        }
        // relative and periodic dates not supported yet
        return null;
    }

    private static BusinessDayConvention toBusinessDayConvention(BusinessDayConventionEnum businessDayConvention) {
        switch (businessDayConvention) {
            case FOLLOWING:
                return BusinessDayConvention.of("FOLLOWING");
            case MODFOLLOWING:
                return BusinessDayConvention.of("MODIFIEDFOLLOWING");
            case PRECEDING:
                return BusinessDayConvention.of("PRECEDING");
            case MODPRECEDING:
                return BusinessDayConvention.of("MODIFIEDPRECEDING");
            case NEAREST:
                return BusinessDayConvention.of("NEAREST");
            default:
                return BusinessDayConvention.of("NOADJUST");
        }
    }
}