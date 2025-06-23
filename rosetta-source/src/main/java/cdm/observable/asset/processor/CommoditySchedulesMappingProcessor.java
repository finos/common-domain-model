package cdm.observable.asset.processor;

import cdm.base.datetime.PeriodEnum;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.updateMappingSuccess;

/**
 * FpML mapper.  Copied from DRR.
 */
@SuppressWarnings("unused")
public class CommoditySchedulesMappingProcessor extends MappingProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommoditySchedulesMappingProcessor.class);

    public CommoditySchedulesMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
        super(modelPath, synonymPaths, context);
    }

    @Override
    public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
        Path scheduleSynonymPath = synonymPath.getParent();

        // finds either calculationPeriodsScheduleReference or calculationPeriodsReference
        List<Mapping> calculationPeriodHrefMappings = findMappings(scheduleSynonymPath, "calculationPeriods", "href");
        if (calculationPeriodHrefMappings.isEmpty()) {
            return;
        }

        Path productSynonymPath = scheduleSynonymPath.getParent();
        LocalDate effectiveDate = extractLocalDate(productSynonymPath, ".effectiveDate.adjustableDate.unadjustedDate");
        LocalDate terminationDate = extractLocalDate(productSynonymPath, ".terminationDate.adjustableDate.unadjustedDate");

        if (effectiveDate == null || terminationDate == null) {
            return;
        }

        @SuppressWarnings("unchecked")
        List<DatedValue.DatedValueBuilder> datedValueBuilders = (List<DatedValue.DatedValueBuilder>) builders;

        for (Mapping calculationPeriodHrefMapping : calculationPeriodHrefMappings) {
            String id = (String) calculationPeriodHrefMapping.getXmlValue();

            // find corresponding calculationPeriodsSchedule path for the id
            Optional<Mapping> calculationPeriodsScheduleIdMapping = findMapping("calculationPeriodsSchedule.id", id);
            calculationPeriodsScheduleIdMapping.ifPresent(mapping -> {
                Path parentPath = mapping.getXmlPath().getParent();
                mapCalculationPeriodsSchedule(datedValueBuilders, effectiveDate, terminationDate, parentPath);
                // update mapping stats
                updateMappingSuccess(calculationPeriodHrefMapping, getModelPath());
                updateMappingSuccess(mapping, getModelPath());
            });

            // find corresponding calculationPeriods path for the id
            Optional<Mapping> calculationPeriodsIdMapping = findMapping("calculationPeriods.id", id);
            calculationPeriodsIdMapping.ifPresent(mapping -> {
                Path parentPath = mapping.getXmlPath().getParent();
                mapCalculationPeriods(datedValueBuilders, terminationDate, parentPath);
                // update mapping stats
                updateMappingSuccess(calculationPeriodHrefMapping, getModelPath());
                updateMappingSuccess(mapping, getModelPath());
            });
        }
    }

    private void mapCalculationPeriodsSchedule(List<DatedValue.DatedValueBuilder> datedValueBuilders, LocalDate effectiveDate, LocalDate terminationDate, Path calculationPeriodsSchedulePath) {
        // find all mappings within the calculationPeriodsSchedulePath
        List<Mapping> calculationPeriodsScheduleMappings = getMappings().stream()
                .filter(m -> calculationPeriodsSchedulePath.nameStartMatches(m.getXmlPath()))
                .filter(m -> m.getXmlValue() != null)
                .collect(Collectors.toList());

        Mapping periodMapping = filterMappings(calculationPeriodsScheduleMappings, "period");
        PeriodEnum period = extractPeriodEnum(periodMapping);

        Mapping periodMultiplierMapping = filterMappings(calculationPeriodsScheduleMappings, "periodMultiplier");
        Integer periodMultiplier = extractInteger(periodMultiplierMapping);

        if (period == null || periodMultiplier == null) {
            return;
        }

        Mapping balanceOfFirstPeriodMapping = filterMappings(calculationPeriodsScheduleMappings, "balanceOfFirstPeriod");
        boolean balanceOfFirstPeriod = extractBoolean(balanceOfFirstPeriodMapping);

        int index = 0;
        // update model
        setDatedValue(datedValueBuilders, index, effectiveDate);

        LocalDate currentDate = effectiveDate;
        while (currentDate.isBefore(terminationDate)) {
            index++;
            currentDate = getNextPeriodStartDate(period, currentDate, periodMultiplier, balanceOfFirstPeriod);

            if (currentDate.isBefore(terminationDate)) {
                // update model
                setDatedValue(datedValueBuilders, index, currentDate);
            }
        }
    }

    private LocalDate getNextPeriodStartDate(PeriodEnum period, LocalDate date, int periodMultiplier, boolean balanceOfFirstPeriod) {
        switch (period) {
            case M:
                if (balanceOfFirstPeriod) {
                    // Calculate the first day of the new month
                    return date.plusMonths(periodMultiplier).withDayOfMonth(1);
                } else {
                    // Calculate the new month with the current day of month
                    return date.plusMonths(periodMultiplier);
                }
            case D:
                return date.plusDays(periodMultiplier);
            case W:
                return date.plusWeeks(periodMultiplier);
            case Y:
                return date.plusYears(periodMultiplier);
            default:
                throw new IllegalArgumentException(String.format("Unknown period %s", period));
        }
    }

    private void mapCalculationPeriods(List<DatedValue.DatedValueBuilder> datedValueBuilders, LocalDate terminationDate, Path calculationPeriodsPath) {
        int index = 0;
        while (index < datedValueBuilders.size()) {
            Optional<Mapping> calculationPeriodDateMapping = findMapping(calculationPeriodsPath, "unadjustedDate", index);
            LocalDate calculationPeriodDate = calculationPeriodDateMapping.map(Mapping::getXmlValue).map(this::parseLocalDate).orElse(null);
            
            if (calculationPeriodDate == null || calculationPeriodDate.isAfter(terminationDate)) {
                break;
            }
            // update model
            setDatedValue(datedValueBuilders, index, calculationPeriodDate);
            // update mapping stats
            updateMappingSuccess(calculationPeriodDateMapping.get(), getModelPath());
            
            index++;
        }
    }

    private void setDatedValue(List<DatedValue.DatedValueBuilder> datedValues, int index, LocalDate date) {
        Optional.ofNullable(datedValues.get(index))
                .ifPresent(datedValueBuilder -> datedValueBuilder.setDate(Date.of(date)));
    }

    private PeriodEnum extractPeriodEnum(Mapping m) {
        return Optional.ofNullable(m)
                .map(Mapping::getXmlValue)
                .map(String.class::cast)
                .map(this::parsePeriodEnum)
                .orElse(null);
    }

    private PeriodEnum parsePeriodEnum(String s) {
        try {
            return PeriodEnum.valueOf(s);
        } catch (Exception e) {
            LOGGER.error("Failed to parse PeriodEnum {}", s, e);
            return null;
        }
    }

    private Integer extractInteger(Mapping m) {
        return Optional.ofNullable(m)
                .map(Mapping::getXmlValue)
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(null);
    }

    private Boolean extractBoolean(Mapping m) {
        return Optional.ofNullable(m)
                .map(Mapping::getXmlValue)
                .map(String.class::cast)
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    private LocalDate extractLocalDate(Path startsWithPath, String xmlPathContains) {
        return getMappings().stream()
                .filter(m -> startsWithPath.nameStartMatches(m.getXmlPath()))
                .filter(mapping -> mapping.getXmlPath().toString().contains(xmlPathContains))
                .map(Mapping::getXmlValue)
                .filter(Objects::nonNull)
                .map(this::parseLocalDate)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private LocalDate parseLocalDate(Object o) {
        try {
            return LocalDate.parse((String) o);
        } catch (Exception e) {
            LOGGER.error("Failed to parse local date {}", o, e);
            return null;
        }
    }

    /**
     * Find from all mappings
     */
    private List<Mapping> findMappings(Path startsWithPath, String pathContains, String pathEndsWith) {
        return getMappings().stream()
                .filter(m -> startsWithPath.nameStartMatches(m.getXmlPath()))
                .filter(m -> m.getXmlPath().toString().contains(pathContains))
                .filter(m -> m.getXmlPath().endsWith(pathEndsWith))
                .filter(m -> m.getXmlValue() != null)
                .collect(Collectors.toList());
    }

    /**
     * Filters mappings from a given subset of mappings 
     */
    private static Mapping filterMappings(List<Mapping> mappings, String pathEndsWith) {
        return mappings.stream()
                .filter(m -> m.getXmlPath().endsWith(pathEndsWith))
                .filter(m -> m.getXmlValue() != null)
                .findFirst()
                .orElse(null);
    }

    private Optional<Mapping> findMapping(String xmlPathContains, String xmlValue) {
        return getMappings().stream()
                .filter(m -> m.getXmlPath().toString().contains(xmlPathContains))
                .filter(m -> xmlValue.equals(m.getXmlValue()))
                .findFirst();
    }

    private Optional<Mapping> findMapping(Path pathStartsWith, String pathEndsWith, int pathIndex) {
        return getMappings().stream()
                .filter(m -> pathStartsWith.nameStartMatches(m.getXmlPath()))
                .filter(m -> m.getXmlPath().endsWith(pathEndsWith))
                .filter(m -> m.getXmlPath().getLastElement().forceGetIndex() == pathIndex)
                .filter(m -> m.getXmlValue() != null)
                .findFirst();
    }
}
