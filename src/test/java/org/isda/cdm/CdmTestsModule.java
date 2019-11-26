package org.isda.cdm;

import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.calculation.functions.GetRateScheduleImpl;
import org.isda.cdm.calculation.functions.ResolveRateIndexImpl;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.GetRateSchedule;
import org.isda.cdm.functions.ResolveRateIndex;
import org.isda.cdm.functions.TestableCalculationPeriod;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
		bind(GetRateSchedule.class).to(bindGetRateSchedule());
	}

	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}

	@Override
	protected Class<? extends CalculationPeriod> bindCalculationPeriod() {
		return TestableCalculationPeriod.class;
	}

	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return ResolveRateIndexImpl.class;
	}

	protected Class<? extends GetRateSchedule> bindGetRateSchedule() {
		return GetRateScheduleImpl.class;
	}
}
