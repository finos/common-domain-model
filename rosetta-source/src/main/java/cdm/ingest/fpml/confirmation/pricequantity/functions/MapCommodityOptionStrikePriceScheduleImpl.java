package cdm.ingest.fpml.confirmation.pricequantity.functions;

import cdm.base.math.DatedValue;
import com.rosetta.model.lib.records.Date;
import fpml.consolidated.shared.NonNegativeMoney;

import java.util.ArrayList;
import java.util.List;

public class MapCommodityOptionStrikePriceScheduleImpl extends MapCommodityOptionStrikePriceSchedule {

    @Override
    protected List<DatedValue.DatedValueBuilder> doEvaluate(List<? extends NonNegativeMoney> fpmlStrikePricePerUnitStep, List<Date> fpmlCommodityStrikePriceDates) {
        List<DatedValue.DatedValueBuilder> datedValueBuilders = new ArrayList<>();

        for (int i = 0; i < fpmlStrikePricePerUnitStep.size(); i++) {
            Date date = fpmlCommodityStrikePriceDates.size() > i ? fpmlCommodityStrikePriceDates.get(i) : null;
            datedValueBuilders.add(DatedValue.builder()
                    .setValue(fpmlStrikePricePerUnitStep.get(i).getAmount())
                    .setDate(date));
        }
        return datedValueBuilders;
    }
}
