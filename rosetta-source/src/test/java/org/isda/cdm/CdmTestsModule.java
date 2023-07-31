package org.isda.cdm;

import cdm.base.datetime.functions.BusinessCenterHolidays;
import cdm.base.datetime.functions.BusinessCenterHolidaysTestDataProvider;
import cdm.product.asset.functions.ResolveRateIndex;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.ResolveRateIndexImpl;
import cdm.product.common.schedule.functions.TestableCalculationPeriod;
import org.finos.cdm.CdmRuntimeModule;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
	}

	@Override
	protected Class<? extends CalculationPeriod> bindCalculationPeriod() {
		return TestableCalculationPeriod.class;
	}

	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return ResolveRateIndexImpl.class;
	}

	@Override
	protected Class<? extends BusinessCenterHolidays> bindBusinessCenterHolidays() {
		return BusinessCenterHolidaysTestDataProvider.class;
	}
}
