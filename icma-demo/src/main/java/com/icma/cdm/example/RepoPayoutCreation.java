/*******************************************************************************
 * Copyright (c) icmagroup.org  All rights reserved.
 *
 * This file is part of the International Capital Market Association (ICMA)
 * CDM for Repo and Bonds Demo
 *
 * This file is intended for demo purposes only and may not be distributed
 * or used in any commercial capacity other then its intended purpose.
 *
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING
 * THE WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE.
 *
 *
 * Contact International Capital Market Association (ICMA),110 Cannon Street,
 * London EC4N 6EU, ph. +44 20 7213 0310, if you have any questions.
 *
 ******************************************************************************/
package com.icma.cdm.example;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceExpression;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.asset.FixedRateSpecification;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

/**
 * Examples of how to create ISDA CDM(TM) Java objects
 */
public class RepoPayoutCreation {

    public static final String CURRENCY_EUR = "EUR";

    public static InterestRatePayout getFloatingRatePayout() {
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("quantity-1"))))
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2018, 1, 3))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2020, 1, 3))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                .setBusinessCenters(BusinessCenters.builder()
                                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                .setExternalReference("primaryBusinessCenters")
                                                                .build()))))
                        )
                        .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                                .setRollConvention(RollConventionEnum._3)
                                .setPeriodMultiplier(6)
                                .setPeriod(PeriodExtendedEnum.M))
                        .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                .setBusinessCenters(BusinessCenters.builder()
                                        .setBusinessCentersReference(
                                                ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))

                .setPaymentDates(PaymentDates.builder()
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M)))

                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(FloatingRateSpecification.builder()
                                .setRateOptionValue(FloatingRateOption.builder()
                                        .setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_LIBOR_BBA)
                                        .setIndexTenor(Period.builder()
                                                .setPeriod(PeriodEnum.M)
                                                .setPeriodMultiplier(6)))))

                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_1)
                        .setReceiver(CounterpartyRoleEnum.PARTY_2))
                .build();
    }

    public static InterestRatePayout getFixedRatePayout(BigDecimal fixedRate) {
        return InterestRatePayout.builder()
                .setPriceQuantity(ResolvablePriceQuantity.builder()
                        .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                .setReference(Reference.builder()
                                        .setScope("DOCUMENT")
                                        .setReference("quantity-2"))))
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2018, 1, 3))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2020, 1, 3))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                .setBusinessCenters(BusinessCenters.builder()
                                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                .setExternalReference("primaryBusinessCenters"))
                                                        .addBusinessCenter(
                                                                FieldWithMetaBusinessCenterEnum.builder().setValue(BusinessCenterEnum.EUTA).build())))))
                        .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                                .setRollConvention(RollConventionEnum._3)
                                .setPeriodMultiplier(3)
                                .setPeriod(PeriodExtendedEnum.M))
                        .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                                .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                .setBusinessCenters(BusinessCenters.builder()
                                        .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                .setExternalReference("primaryBusinessCenters")))))
                .setPaymentDates(PaymentDates.builder()
                        .setPayRelativeTo(PayRelativeToEnum.CALCULATION_PERIOD_END_DATE)
                        .setPaymentFrequency(Frequency.builder()
                                .setPeriodMultiplier(1)
                                .setPeriod(PeriodExtendedEnum.Y)
                                .build())
                        .build())
                .setRateSpecification(RateSpecification.builder()
                        .setFixedRate(FixedRateSpecification.builder()
                                .setRateSchedule(RateSchedule.builder()
                                        .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                .setReference(Reference.builder()
                                                        .setScope("DOCUMENT")
                                                        .setReference("price-1"))
                                                .setValue(Price.builder()
                                                        .setValue(fixedRate)
                                                        .setUnit(UnitType.builder().setCurrencyValue(CURRENCY_EUR))
                                                        .setPerUnitOf(UnitType.builder().setCurrencyValue(CURRENCY_EUR))
                                                        .setPriceExpression(PriceExpression.builder()
                                                                .setPriceType(PriceTypeEnum.INTEREST_RATE)))))))
                .setPayerReceiver(PayerReceiver.builder()
                        .setPayer(CounterpartyRoleEnum.PARTY_2)
                        .setReceiver(CounterpartyRoleEnum.PARTY_1))
                .build();
    }
}
