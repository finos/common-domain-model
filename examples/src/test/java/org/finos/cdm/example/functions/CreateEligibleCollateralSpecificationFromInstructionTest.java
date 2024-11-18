package org.finos.cdm.example.functions;

import cdm.base.datetime.Period;
import cdm.base.datetime.PeriodBound;
import cdm.base.datetime.PeriodEnum;
import cdm.base.datetime.PeriodRange;
import cdm.base.staticdata.asset.common.*;
import cdm.product.collateral.*;
import cdm.product.collateral.functions.Create_EligibleCollateralSpecificationFromInstruction;
import com.google.inject.Inject;
import org.finos.cdm.example.AbstractExampleTest;
import org.finos.cdm.example.util.ResourcesUtils;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateEligibleCollateralSpecificationFromInstructionTest extends AbstractExampleTest {

    @Inject
    Create_EligibleCollateralSpecificationFromInstruction func;

    @Test
    void shouldMergeCommonAndVariableEligibleCollateralCriteria() throws IOException {
        // Common criteria - GILTS
        EligibleCollateralCriteria common = getCommonCriteria();

        // Variable criteria - Valuation percentages for each maturity range
        List<EligibleCollateralCriteria> variable = Arrays.asList(
                getVariableCriteria(0.97, getMaturityRange(0, 1)),
                getVariableCriteria(0.96, getMaturityRange(1, 5)),
                getVariableCriteria(0.95, getMaturityRange(5, 10)),
                getVariableCriteria(0.93, getMaturityRange(10, 30)),
                getVariableCriteria(0.9, getMaturityRange(30)));

        // Create instruction
        EligibleCollateralSpecificationInstruction instruction = EligibleCollateralSpecificationInstruction.builder()
                .setCommon(common)
                .setVariable(variable)
                .build();

        // Call function to merge common and variable criteria
        EligibleCollateralSpecification merged = func.evaluate(instruction);

        // Assert
        String expectedJson = ResourcesUtils.getJson("merge-eligible-collateral-expected.json");
        String mergedJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(merged);
        assertEquals(expectedJson, mergedJson);
    }

    private static EligibleCollateralCriteria getCommonCriteria() {
        return EligibleCollateralCriteria.builder()
                .addAsset(AssetCriteria.builder()
                        .addCollateralAssetType(AssetType.builder()
                                .setAssetType(AssetTypeEnum.SECURITY)
                                .setSecurityType(InstrumentTypeEnum.DEBT)))
                .addIssuer(IssuerCriteria.builder()
                        .addIssuerType(CollateralIssuerType.builder()
                                .setIssuerType(IssuerTypeEnum.SOVEREIGN_CENTRAL_BANK))
                        .addIssuerCountryOfOrigin(ISOCountryCodeEnum.GB))
                .build();
    }

    private static PeriodRange getMaturityRange(int lowerBound, int upperBound) {
        return PeriodRange.builder()
                .setLowerBound(getMaturityBound(lowerBound, true))
                .setUpperBound(getMaturityBound(upperBound, false))
                .build();
    }

    private static PeriodRange getMaturityRange(int lowerBound) {
        return PeriodRange.builder()
                .setLowerBound(getMaturityBound(lowerBound, true))
                .build();
    }

    private static PeriodBound.PeriodBoundBuilder getMaturityBound(int years, boolean inclusive) {
        return PeriodBound.builder()
                .setInclusive(inclusive)
                .setPeriod(Period.builder()
                        .setPeriodMultiplier(years)
                        .setPeriod(PeriodEnum.Y));
    }
    private static EligibleCollateralCriteria getVariableCriteria(double haircutPercentage, PeriodRange maturityRange) {
        return EligibleCollateralCriteria.builder()
                .setTreatment(CollateralTreatment.builder()
                        .setIsIncluded(true)
                        .setValuationTreatment(CollateralValuationTreatment.builder()
                                .setHaircutPercentage(BigDecimal.valueOf(haircutPercentage))))
                .addAsset(AssetCriteria.builder()
                        .setMaturityType(MaturityTypeEnum.REMAINING_MATURITY)
                        .setMaturityRange(maturityRange))
                .build();
    }
}
