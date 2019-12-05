package org.isda.cdm;

import org.isda.cdm.functions.Abs;
import org.isda.cdm.functions.AbsImpl;
import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.ListsCompare;
import org.isda.cdm.functions.ListsCompareImpl;
import org.isda.cdm.functions.Sum;
import org.isda.cdm.functions.SumImpl;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.qualify.QualifyFunctionFactory;
import com.rosetta.model.lib.validation.ModelObjectValidator;

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
	protected Class<? extends ListsCompare> bindListsCompare() {
		return ListsCompareImpl.class;
	}
}
