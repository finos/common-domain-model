package cdm.observable.asset.processor;

import cdm.base.math.DatedValue;
import cdm.observable.asset.PriceSchedule;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unused")
public class CommoditySchedulesMappingProcessorTest {
    public static final Path SYNONYM_PATH = Path.parse("nonpublicExecutionReport.trade.commodityOption.strikePricePerUnitSchedule.strikePricePerUnitStep");
    public static final RosettaPath MODEL_PATH = RosettaPath.valueOf("ReportableEvent.originatingWorkflowStep.proposedEvent.instruction(0).before.value.trade.product.economicTerms.payout.optionPayout(0).exerciseTerms.strike.strikePrice.datedValue");


    @Test
    void shouldMapDatedValue() {
        // Set up
        Path calculationPeriodHref = Path.parse("nonpublicExecutionReport.trade.commodityOption.strikePricePerUnitSchedule.calculationPeriodsScheduleReference.href");
        Path calculationPeriodMultiplierId = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.id");
        Path calculationPeriodMultiplierValue = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.periodMultiplier");
        Path calculationPeriodMultiplierPeriod = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.period");
        Path calculationBalanceOfFirstPeriod = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.balanceOfFirstPeriod");

        Path effectiveDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.effectiveDate.adjustableDate.unadjustedDate");
        Path terminationDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.terminationDate.adjustableDate.unadjustedDate");

        List<Mapping> mappings = new ArrayList<>();
        mappings.add(getErrorMapping(calculationPeriodHref, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierId, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierValue, "1"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierPeriod, "M"));
        //mappings.add(getErrorMapping(calculationBalanceOfFirstPeriod, "false"));
        mappings.add(getErrorMapping(effectiveDatePaths, "2022-07-01"));
        mappings.add(getErrorMapping(terminationDatePaths, "2022-08-31"));

        // Create context
        MappingContext context = new MappingContext(mappings, null, null, null);

        // Create the parent object with a PriceSchedule parent
        PriceSchedule.PriceScheduleBuilder parent = PriceSchedule.builder()
                .setDatedValue(
                        Arrays.asList(
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(2000))
                                        .build()
                                ,
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(3000))
                                        .build()
                        )
                );

        // Create the parent object with DatedValue
        DatedValue.DatedValueBuilder datedValue1 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(2000));
        DatedValue.DatedValueBuilder datedValue2 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(3000));

        // Create the processor
        CommoditySchedulesMappingProcessor commoditySchedulesMappingProcessor = new CommoditySchedulesMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);

        List<RosettaModelObjectBuilder> buildersList = new ArrayList<>();
        buildersList.add(datedValue1);
        buildersList.add(datedValue2);


        // Invoke the map method
        commoditySchedulesMappingProcessor.map(SYNONYM_PATH, buildersList, parent);

        // Assert the result based on your expected outcome
        assertEquals("2022-07-01", datedValue1.getDate().toString());
        assertEquals("2022-08-01", datedValue2.getDate().toString());

    }

    @Test
    void shouldMapDatedValue2() {
        // Set up
        Path calculationPeriodHref = Path.parse("nonpublicExecutionReport.trade.commodityOption.strikePricePerUnitSchedule.calculationPeriodsScheduleReference.href");
        Path calculationPeriodMultiplierId = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.id");
        Path calculationPeriodMultiplierValue = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.periodMultiplier");
        Path calculationPeriodMultiplierPeriod = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.period");
        Path calculationBalanceOfFirstPeriod = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriodsSchedule.balanceOfFirstPeriod");

        Path effectiveDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.effectiveDate.adjustableDate.unadjustedDate");
        Path terminationDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.terminationDate.adjustableDate.unadjustedDate");

        List<Mapping> mappings = new ArrayList<>();
        mappings.add(getErrorMapping(calculationPeriodHref, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierId, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierValue, "1"));
        mappings.add(getErrorMapping(calculationPeriodMultiplierPeriod, "M"));
        mappings.add(getErrorMapping(calculationBalanceOfFirstPeriod, "true"));
        mappings.add(getErrorMapping(effectiveDatePaths, "2022-07-04"));
        mappings.add(getErrorMapping(terminationDatePaths, "2022-08-31"));

        // Create context
        MappingContext context = new MappingContext(mappings, null, null, null);

        // Create the parent object with a PriceSchedule parent
        PriceSchedule.PriceScheduleBuilder parent = PriceSchedule.builder()
                .setDatedValue(
                        Arrays.asList(
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(2000))
                                        .build()
                                ,
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(3000))
                                        .build()
                        )
                );

        // Create the parent object with DatedValue
        DatedValue.DatedValueBuilder datedValue1 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(2000));
        DatedValue.DatedValueBuilder datedValue2 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(3000));

        // Create the processor
        CommoditySchedulesMappingProcessor commoditySchedulesMappingProcessor = new CommoditySchedulesMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);

        List<RosettaModelObjectBuilder> buildersList = new ArrayList<>();
        buildersList.add(datedValue1);
        buildersList.add(datedValue2);

        commoditySchedulesMappingProcessor.map(SYNONYM_PATH, buildersList, parent);

        // Assert the result based on your expected outcome
        assertEquals("2022-07-04", datedValue1.getDate().toString());
        assertEquals("2022-08-01", datedValue2.getDate().toString());
    }

    @Test
    void shouldMapDatedValue3() {
        // Set up
        Path calculationPeriodHref = Path.parse("nonpublicExecutionReport.trade.commodityOption.strikePricePerUnitSchedule.calculationPeriodsScheduleReference.href");
        Path calculationPeriodId = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriods.id");
        Path calculationPeriodValue1 = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriods.unadjustedDate[0]");
        Path calculationPeriodValue2 = Path.parse("nonpublicExecutionReport.trade.commodityOption.calculationPeriods.unadjustedDate[1]");

        Path effectiveDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.effectiveDate.adjustableDate.unadjustedDate");
        Path terminationDatePaths = Path.parse("nonpublicExecutionReport.trade.commodityOption.terminationDate.adjustableDate.unadjustedDate");

        List<Mapping> mappings = new ArrayList<>();
        mappings.add(getErrorMapping(calculationPeriodHref, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodId, "CalculationPeriods"));
        mappings.add(getErrorMapping(calculationPeriodValue1, "2022-07-01"));
        mappings.add(getErrorMapping(calculationPeriodValue2, "2022-08-01"));
        mappings.add(getErrorMapping(effectiveDatePaths, "2022-07-01"));
        mappings.add(getErrorMapping(terminationDatePaths, "2022-08-31"));

        // Create context
        MappingContext context = new MappingContext(mappings, null, null, null);

        // Create the parent object with a PriceSchedule parent
        PriceSchedule.PriceScheduleBuilder parent = PriceSchedule.builder()
                .setDatedValue(
                        Arrays.asList(
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(2000))
                                        .build()
                                ,
                                DatedValue.builder()
                                        .setDate(null)  // You should set the date appropriately
                                        .setValue(new BigDecimal(3000))
                                        .build()
                        )
                );

        // Create the parent object with DatedValue
        DatedValue.DatedValueBuilder datedValue1 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(2000));
        DatedValue.DatedValueBuilder datedValue2 =
                DatedValue.builder()
                        .setDate(null)  // You should set the date appropriately
                        .setValue(new BigDecimal(3000));


        // Create the processor
        CommoditySchedulesMappingProcessor commoditySchedulesMappingProcessor = new CommoditySchedulesMappingProcessor(MODEL_PATH, Arrays.asList(SYNONYM_PATH), context);

        List<RosettaModelObjectBuilder> buildersList = new ArrayList<>();
        buildersList.add(datedValue1);
        buildersList.add(datedValue2);

        // Invoke the map method
        commoditySchedulesMappingProcessor.map(SYNONYM_PATH, buildersList, parent);

        // Assert the result based on your expected outcome
        DatedValue dV = parent.getDatedValue().get(0);
        DatedValue dV2 = parent.getDatedValue().get(1);
        assertEquals("2022-07-01", datedValue1.getDate().toString());
        assertEquals("2022-08-01", datedValue2.getDate().toString());
    }

    @NotNull
    private static Mapping getErrorMapping(Path xmlPath, String xmlValue) {
        return new Mapping(xmlPath, xmlValue, null, null, "Not found", false, false, false);
    }
}
