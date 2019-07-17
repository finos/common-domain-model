package org.isda.cdm.functions;

import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.EquityPayout;
import org.isda.cdm.PriceReturnTerms;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class EquityCalculationPeriodImpl implements EquityCalculationPeriod {

	@Override
	public CalculationResult execute(EquityPayout equityPayout, Date businessDate) {
		PriceReturnTerms terms = equityPayout.getPriceReturnTerms();

		List<LocalDate> unadjustedDate = terms.getValuationPriceInterim().getValuationDates().getAdjustableDates().getUnadjustedDate();
		unadjustedDate.sort(LocalDate::compareTo);

		LocalDate date = LocalDate.of(businessDate.getYear(), businessDate.getMonth(), businessDate.getDay());
		LocalDate terminationDate = terms.getValuationPriceFinal().getValuationDate().getAdjustableDate().getUnadjustedDate();
		LocalDate effectiveDate = equityPayout.getCalculationPeriodDates().getEffectiveDate().getAdjustableDate().getUnadjustedDate();

		LocalDate endDate = findEndDate(unadjustedDate, date).orElse(terminationDate);
		LocalDate startDate = findStartDate(unadjustedDate, date).orElse(effectiveDate);

		return new CalculationResult()
				.setStartDate(new DateImpl(startDate))
				.setEndDate(new DateImpl(endDate))
				.setIsFirstPeriod(startDate.equals(effectiveDate))
				.setIsLastPeriod(endDate.equals(terminationDate));
	}

	private Optional<LocalDate> findStartDate(List<LocalDate> dates, LocalDate date) {
		return findDate(dates, date, LocalDate::isAfter);
	}

	private Optional<LocalDate> findEndDate(List<LocalDate> dates, LocalDate date) {
		return findDate(dates, date, LocalDate::isBefore);
	}

	private Optional<LocalDate> findDate(List<LocalDate> dates, LocalDate date, BiPredicate<LocalDate, LocalDate> predicate) {
		if (!dates.isEmpty()) {
			LocalDate head = dates.get(0);
			if (predicate.test(date, head)) {
				return Optional.of(head);
			} else {
				return findDate(dates.subList(1, dates.size()), date, predicate);
			}
		} else {
			return Optional.empty();
		}
	}
}
