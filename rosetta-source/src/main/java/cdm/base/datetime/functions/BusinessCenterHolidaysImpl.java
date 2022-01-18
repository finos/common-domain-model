package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BusinessCenterHolidaysImpl extends BusinessCenterHolidays {

	@Inject
	private BusinessCenterHolidaysDataProvider dataProvider; // raw holiday lists

	protected List<Date> doEvaluate(BusinessCenters businessCenters) {
		return getBusinessCenters(businessCenters).stream()
				.filter(Objects::nonNull)
				.map(FieldWithMetaBusinessCenterEnum::getValue)
				.filter(Objects::nonNull)
				.map(dataProvider::getHolidays)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.sorted()
				.distinct()
				.collect(Collectors.toList());
	}

	// retrieve the list of business centers to a BusinessCenters structure, de-referencing if necessary
	private List<? extends FieldWithMetaBusinessCenterEnum> getBusinessCenters(BusinessCenters bc) {
		if (bc == null) {
			return null;
		}
		ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
		if (ref != null) {
			return getBusinessCenters(ref.getValue());
		}
		return bc.getBusinessCenter();
	}
}
