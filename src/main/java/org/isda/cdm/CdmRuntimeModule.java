package org.isda.cdm;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.functions.*;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(QualifyFunctionFactory.class).to(bindQualifyFunctionFactory());

		// functions
		bind(Abs.class).to(bindAbs());
		bind(org.isda.cdm.functions.CalculationPeriod.class).to(bindCalculationPeriod());
		bind(Sum.class).to(bindSum());
		bind(ListsCompare.class).to(bindListsCompare());
		bind(ResolvePayoutQuantity.class).to(bindResolvePayoutQuantity());
		bind(ResolveContractualProduct.class).to(bindResolveContractualProduct());
		bind(ResolveEquityInitialPrice.class).to(bindResolveEquityInitialPrice());
		bind(NoOfUnits.class).to(bindNoOfUnits());
		bind(Notional.class).to(bindNotional());
		bind(PartyByRole.class).to(PartyByRoleImpl.class);
		bind(FloatingInterestRatePayout.class).to(FloatingInterestRatePayoutImpl.class);
	}

	protected Class<? extends ListsCompare> bindListsCompare() {
		return ListsCompareImpl.class;
	}

	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return RosettaTypeValidator.class;
	}

	protected Class<? extends QualifyFunctionFactory> bindQualifyFunctionFactory() {
		return QualifyFunctionFactory.Default.class;
	}

	// Functions
	
	protected Class<? extends Abs> bindAbs() {
		return AbsImpl.class;
	}

	protected Class<? extends org.isda.cdm.functions.CalculationPeriod> bindCalculationPeriod() {
		return CalculationPeriodImpl.class;
	}

	protected Class<? extends Sum> bindSum() {
		return SumImpl.class;
	}

	protected Class<? extends ResolvePayoutQuantity> bindResolvePayoutQuantity() {
		return ResolvePayoutQuantityImpl.class;
	}

	protected Class<? extends ResolveContractualProduct> bindResolveContractualProduct() {
		return ResolveContractualProductImpl.class;
	}

	protected Class<? extends ResolveEquityInitialPrice> bindResolveEquityInitialPrice() {
		return ResolveEquityInitialPriceImpl.class;
	}
	
	protected Class<? extends NoOfUnits> bindNoOfUnits() {
		return NoOfUnitsImpl.class;
	}
	
	protected Class<? extends Notional> bindNotional() {
		return NotionalImpl.class;
	}
}
