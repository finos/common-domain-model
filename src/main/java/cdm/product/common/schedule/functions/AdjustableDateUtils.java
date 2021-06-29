package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import com.opengamma.strata.basics.ReferenceData;
import com.opengamma.strata.basics.date.BusinessDayAdjustment;
import com.opengamma.strata.basics.date.BusinessDayConvention;
import com.opengamma.strata.basics.date.HolidayCalendarIds;

import java.time.LocalDate;
import java.util.Optional;

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
    public static LocalDate adjustDate(AdjustableOrRelativeDate adjustableOrRelativeDate) {
        if (adjustableOrRelativeDate.getAdjustableDate() != null) {
            AdjustableDate adjustableDate = adjustableOrRelativeDate.getAdjustableDate();
            if (adjustableDate.getAdjustedDate() != null) {
                return adjustableDate.getAdjustedDate().getValue().toLocalDate();
            }
            if (adjustableDate.getUnadjustedDate() != null) {
                BusinessDayConventionEnum businessDayConvention = Optional
                        .ofNullable(adjustableDate.getDateAdjustments())
                        .map(BusinessDayAdjustments::getBusinessDayConvention)
                        .orElse(BusinessDayConventionEnum.NONE);

                LocalDate unadjustedDate = adjustableDate.getUnadjustedDate().toLocalDate();
                return BusinessDayAdjustment.builder()
                        .convention(toBusinessDayConvention(businessDayConvention))
                        .calendar(HolidayCalendarIds.SAT_SUN).build()
                        .adjust(unadjustedDate, ReferenceData.minimal());
            }
        }

        if (adjustableOrRelativeDate.getRelativeDate() != null) {
            AdjustedRelativeDateOffset relativeDate = adjustableOrRelativeDate.getRelativeDate();
            if (relativeDate.getAdjustedDate() != null) {
                return relativeDate.getAdjustedDate().toLocalDate();
            }
            // adjusting relative dates are not supported yet
        }

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