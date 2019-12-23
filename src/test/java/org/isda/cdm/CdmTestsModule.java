package org.isda.cdm;

import org.isda.cdm.calculation.functions.GetRateScheduleImpl;
import org.isda.cdm.calculation.functions.ResolveRateIndexImpl;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.GetRateSchedule;
import org.isda.cdm.functions.NoQuantityChange;
import org.isda.cdm.functions.NoQuantityChangeImpl;
import org.isda.cdm.functions.QuantityDecreased;
import org.isda.cdm.functions.QuantityDecreasedImpl;
import org.isda.cdm.functions.QuantityDecreasedToZero;
import org.isda.cdm.functions.QuantityDecreasedToZeroImpl;
import org.isda.cdm.functions.QuantityIncreased;
import org.isda.cdm.functions.QuantityIncreasedImpl;
import org.isda.cdm.functions.ResolveRateIndex;
import org.isda.cdm.functions.TestableCalculationPeriod;

import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
		bind(GetRateSchedule.class).to(bindGetRateSchedule());
		
		bind(NoQuantityChange.class).to(NoQuantityChangeImpl.class);
		bind(QuantityIncreased.class).to(QuantityIncreasedImpl.class);
		bind(QuantityDecreased.class).to(QuantityDecreasedImpl.class);
		bind(QuantityDecreasedToZero.class).to(QuantityDecreasedToZeroImpl.class);
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
