package com.regnosys.cdm.example;

import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.meta.InterestRatePayoutMeta;
import cdm.product.template.Payout;
import cdm.product.template.validation.datarule.PayoutDayCountFraction;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.lib.validation.ValidatorFactory;

import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Illustration of how to invoke validations on a CDM object as well as
 * individual validations
 */
public class Validation extends AbstractExample {

	@Inject
	private ValidatorFactory.Default validatorFactory;

	@Inject
	private RosettaTypeValidator validator;

	@Inject
	private PayoutDayCountFraction condition;

	@Override
	public void example() {
		var fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout(BigDecimal.valueOf(0.05));

		// Recursively run all validators for an object
		//
		validator.runProcessStep(InterestRatePayout.class, fixedRatePayout.toBuilder())
				.getValidationResults().forEach(System.out::println);

		// The validators for a single class can be accessed via it's meta class
		//
		var fixedRatePayoutMeta = (InterestRatePayoutMeta) fixedRatePayout.metaData();

		// the meta class offers granularity over which types of Validators to extract
		//
		var validators = fixedRatePayoutMeta.dataRules(validatorFactory);

		// fixedRatePayoutMeta.validator() returns the cardinality validator for fixedRatePayout
		//
		validators.add(fixedRatePayoutMeta.validator());

		// Run Validators
		//
		validators.stream()
				.map(validator -> validator.validate(RosettaPath.valueOf("InterestRatePayout"), fixedRatePayout))
				.sorted(Comparator.comparing(ValidationResult::isSuccess, Boolean::compareTo)) // failures first
				.forEach(System.out::println);

		// Individual Validators can be invoked for further debugging
		//
		var validationResult = condition
				.validate(RosettaPath.valueOf("InterestRatePayout"), Payout.builder().addInterestRatePayout(fixedRatePayout).build());

		System.out.println("\nSingle validation result:\n" + validationResult);
	}

	public static void main(String[] args) {
		new Validation().run();
	}
}
