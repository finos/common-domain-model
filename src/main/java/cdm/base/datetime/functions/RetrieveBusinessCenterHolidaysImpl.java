package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateGroup;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;

import java.util.*;

public class RetrieveBusinessCenterHolidaysImpl extends RetrieveBusinessCenterHolidays {

	@Inject
	private BusinessCenterHolidaysDataProvider holidayData; // raw holiday lists

	// Is this cache necessary in the default implementation?
	private final Map<String, DateGroup.DateGroupBuilder> cache = new HashMap<>(); // cache of combined holiday data lists for lists of business centers

	protected DateGroup.DateGroupBuilder doEvaluate(BusinessCenters businessCenters) {
		String key = getKey(businessCenters);   // get a key based on the list of business centers

		// check if the combined list is cached and if so return it
		DateGroup cached = cache.get(key);
		if (cached != null)
			return cached.toBuilder();

		// otherwise generate the combined list, cache it, and return it
		DateGroup.DateGroupBuilder generated = generateHolidayList(businessCenters);
		cache.put(key, generated);
		return generated.toBuilder();
	}

	// generate a combined list of holidays for a list of business centers
	private DateGroup.DateGroupBuilder generateHolidayList(BusinessCenters bcs) {
		// retrieve the list of business centers as a list
		List<? extends FieldWithMetaBusinessCenterEnum> bcl = getBC(bcs);

		Set<Date> newHols = new TreeSet<>();    // initialize a sorted set to hold the dates
		// for each business center
		for (FieldWithMetaBusinessCenterEnum bc : bcl) {
			List<Date> holidays = holidayData.getHolidays(bc.getValue()); // get the list of holidays for the bc
			if (holidays != null)
				newHols.addAll(holidays);   // add it to the set of holidays to be return
		}
		// convert the sorted set to a list
		List<Date> result = new ArrayList<>(newHols.size());
		result.addAll(newHols);
		return DateGroup.builder().setDates(result);
	}

	// create a key by concatenating the BC codes
	private String getKey(BusinessCenters bcs) {
		List<? extends FieldWithMetaBusinessCenterEnum> bcl = getBC(bcs);
		StringBuilder key = new StringBuilder();
		for (FieldWithMetaBusinessCenterEnum bc : bcl) {
			if (bc == null || bc.getValue() == null)
				continue;
			key.append(bc.getValue().toString());
		}
		return key.toString();
	}

	// retrieve the list of business centers to a BusinessCenters structure, dereferencing if necessary
	private List<? extends FieldWithMetaBusinessCenterEnum> getBC(BusinessCenters bc) {
		if (bc == null)
			return new ArrayList<>();
		ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
		if (ref != null) {
			BusinessCenters refBC = ref.getValue();
			return getBC(refBC);
		}
		return bc.getBusinessCenter();
	}
}
