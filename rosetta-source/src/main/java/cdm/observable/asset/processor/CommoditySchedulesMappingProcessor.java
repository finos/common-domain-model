package cdm.observable.asset.processor;

import cdm.base.math.DatedValue;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.records.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * FpML mapper - copied from DRR.
 */
@SuppressWarnings("unused")
public class CommoditySchedulesMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommoditySchedulesMappingProcessor.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // You should use the appropriate date format

    public CommoditySchedulesMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        List<Path> calculationPeriodHref = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains("Schedule.calculationPeriods") && mapping.getXmlPath().toString().contains("href"))
                .map(Mapping::getXmlPath)
                .collect(Collectors.toList());
        List<Path> calculationPeriodsMultiplier = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains("calculationPeriodsSchedule.id") || mapping.getXmlPath().toString().contains("calculationPeriodsSchedule.period") || mapping.getXmlPath().toString().contains("calculationPeriodsSchedule.balanceOfFirstPeriod"))
                .map(Mapping::getXmlPath)
                .collect(Collectors.toList());
        List<Path> calculationPeriodsId = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains("calculationPeriods.id"))
                .map(Mapping::getXmlPath)
                .collect(Collectors.toList());
        List<Path> calculationPeriods = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains("calculationPeriods.unadjustedDate"))
                .map(Mapping::getXmlPath)
                .distinct()
                .collect(Collectors.toList());
        List<Path> effectiveDatePaths = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains(".effectiveDate.adjustableDate.unadjustedDate"))
                .map(Mapping::getXmlPath)
                .collect(Collectors.toList());
        List<Path> terminationDatePaths = getMappings().stream()
                .filter(mapping -> mapping.getXmlPath().toString().contains(".terminationDate.adjustableDate.unadjustedDate"))
                .map(Mapping::getXmlPath)
                .collect(Collectors.toList());

        List<DatedValue.DatedValueBuilder> datedValues =
                (List<DatedValue.DatedValueBuilder>) builders;

        for (int i = 0; i < calculationPeriodHref.size(); i++) {
            Optional<String> id = getValueAndUpdateMappings(calculationPeriodHref.get(i));

            // Find corresponding multiplier and date paths
            Optional<Path> multiplierPath = calculationPeriodsMultiplier.stream()
                    .filter(path -> getValueAndUpdateMappings(path).map(value -> value.equals(id.orElse(null))).orElse(false))
                    .findFirst();
            Optional<Path> cPeriodsPath = calculationPeriodsId.stream()
                    .filter(path -> getValueAndUpdateMappings(path).map(value -> value.equals(id.orElse(null))).orElse(false))
                    .findFirst();
            int index = 0;
            if (multiplierPath.isPresent()) {
                String effectiveDate = getOptionalValue(effectiveDatePaths, i);
                String idValue = getOptionalValue(calculationPeriodsMultiplier, i);
                String terminationDate = getOptionalValue(terminationDatePaths, i);
                String periodMultiplier = getOptionalValue(calculationPeriodsMultiplier, 1);
                String period = getOptionalValue(calculationPeriodsMultiplier, 2);
                String balanceOfFirstPeriodVal = getOptionalValue(calculationPeriodsMultiplier, 3);
                if (effectiveDate != null && terminationDate != null && period != null && periodMultiplier != null) {
                    try {
                        Date startDate = Date.parse(effectiveDate);
                        Date endDate = Date.parse(terminationDate);
                        Date currentDate = startDate;

                        int multiplier = Integer.parseInt(periodMultiplier);
                        // Assume there is no balance of the first period
                        boolean balanceOfFirstPeriod = false;
                        if (balanceOfFirstPeriodVal != null) {
                            balanceOfFirstPeriod = Boolean.parseBoolean(balanceOfFirstPeriodVal);
                        }
                        int currentYear, currentMonth, currentDay;
                        int newYear, newMonth, newDay;
                        // init populating the datedValues with current effectiveDate of the contract
                        datedValues.get(index).setDate(currentDate);
                        index++;
                        while (compareDates(currentDate, endDate) < 0) {
                            switch (period) {
                                case "M":
                                    if (balanceOfFirstPeriod) {
                                        // Calculate the first day of the next month

                                        currentYear = currentDate.getYear();
                                        currentMonth = currentDate.getMonth();
                                        newYear = currentYear;
                                        newMonth = currentMonth + multiplier;

                                        // Check if month exceeds 12, and adjust year and month accordingly
                                        if (newMonth > 12) {
                                            newYear += newMonth / 12;
                                            newMonth = newMonth % 12;
                                        }

                                        // Calculate the last day of the current month
                                        int maxDaysInMonth = getMaxDaysInMonth(currentYear, currentMonth);
                                        int lastDayOfMonth = (currentMonth == 2 && isLeapYear(currentYear)) ? 29 : maxDaysInMonth;

                                        // Create a new Date instance with the updated year and month
                                        currentDate = Date.of(newYear, newMonth, 1);
                                    } else {
                                        // Assuming currentDate represents the current date as a Date instance
                                        currentYear = currentDate.getYear();
                                        currentMonth = currentDate.getMonth();
                                        currentDay = currentDate.getDay();

                                        // Calculate the new year and month
                                        newYear = currentYear;
                                        newMonth = currentMonth + multiplier;

                                        // Check if month exceeds 12, and adjust year and month accordingly
                                        if (newMonth > 12) {
                                            newYear += newMonth / 12;
                                            newMonth = newMonth % 12;
                                        }
                                        // Create a new Date instance with the updated year and month
                                        currentDate = Date.of(newYear, newMonth, currentDay);
                                    }
                                    break;
                                case "D":
                                    currentYear = currentDate.getYear();
                                    currentMonth = currentDate.getMonth();
                                    currentDay = currentDate.getDay();

                                    // Calculate the new year, month, and day
                                    newYear = currentYear;
                                    newMonth = currentMonth;
                                    newDay = currentDay + multiplier;

                                    // Check if the day exceeds the maximum for the current month
                                    int maxDaysInMonth = getMaxDaysInMonth(newYear, newMonth);
                                    if (newDay > maxDaysInMonth) {
                                        newDay = 1;
                                        newMonth++;

                                        // Check if the month exceeds 12, and adjust year and month accordingly
                                        if (newMonth > 12) {
                                            newYear += newMonth / 12;
                                            newMonth = newMonth % 12;
                                        }

                                        // Create a new Date instance with the updated year, month, and day
                                        currentDate = Date.of(newYear, newMonth, newDay);
                                    }
                                    break;

                                case "W":
                                    // Assuming currentDate represents the current date as a Date instance
                                    currentYear = currentDate.getYear();
                                    currentMonth = currentDate.getMonth();
                                    currentDay = currentDate.getDay();

                                    // Calculate the new year, month, and day
                                    newYear = currentYear;
                                    newMonth = currentMonth;
                                    newDay = currentDay + (multiplier * 7);

                                    // Loop to handle month and year overflow
                                    while (newDay > getMaxDaysInMonth(newYear, newMonth)) {
                                        newDay -= getMaxDaysInMonth(newYear, newMonth);
                                        newMonth++;

                                        if (newMonth > 12) {
                                            newYear += 1;
                                            newMonth = 1;
                                        }
                                    }

                                    // Create a new Date instance with the updated year, month, and day
                                    currentDate = Date.of(newYear, newMonth, newDay);
                                    break;

                                case "Y":
                                    // Assuming currentDate represents the current date as a Date instance
                                    currentYear = currentDate.getYear();
                                    currentMonth = currentDate.getMonth();
                                    currentDay = currentDate.getDay();

                                    // Calculate the new year, month, and day
                                    newYear = currentYear + multiplier;

                                    // Create a new Date instance with the updated year, month, and day
                                    currentDate = Date.of(newYear, currentMonth, currentDay);
                                    break;

                                default:
                                    // Handle the case where 'period' is not one of M, D, W, or Y
                                    break;
                            }
                            if (compareDates(currentDate, endDate) < 0) {
                                datedValues.get(index).setDate(currentDate);
                            }
                            index++;

                        }
                    } catch (Exception e) {
                        LOGGER.error("Error parsing date: " + e.getMessage(), e);
                    }
                }
            } else if (cPeriodsPath.isPresent()) {
                String terminationDate = getOptionalValue(terminationDatePaths, i);
                Date endDate = Date.parse(terminationDate);
                while (index < datedValues.size() && compareDates(Date.parse(getValueAndUpdateMappings(calculationPeriods.get(index)).get()), endDate) < 0) {
                    datedValues.get(index).setDate(Date.parse(getValueAndUpdateMappings(calculationPeriods.get(index)).get()));
                    index++;
                }
            }
        }
    }

    private int getMaxDaysInMonth(int year, int month) {
        if (month == 2) {
            // Check if it's a leap year
            if (isLeapYear(year)) {
                return 29; // February in a leap year
            } else {
                return 28; // February in a non-leap year
            }
        } else {
            int[] daysInMonth = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            return daysInMonth[month];
        }
    }

    private boolean isLeapYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
    }


    private int compareDates(Date date1, Date date2) {
        int year1 = date1.getYear();
        int month1 = date1.getMonth();
        int day1 = date1.getDay();

        int year2 = date2.getYear();
        int month2 = date2.getMonth();
        int day2 = date2.getDay();

        if (year1 != year2) {
            return Integer.compare(year1, year2);
        } else if (month1 != month2) {
            return Integer.compare(month1, month2);
        } else {
            return Integer.compare(day1, day2);
        }
    }

    private String getOptionalValue(List<Path> paths, int index) {
        if (index >= 0 && index < paths.size()) {
            Optional<String> optionalValue = getValueAndUpdateMappings(paths.get(index));
            return optionalValue.orElse("");
        }
        return "";
    }
}
