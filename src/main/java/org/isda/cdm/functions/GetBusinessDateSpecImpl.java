package org.isda.cdm.functions;

import java.time.LocalDate;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

public class GetBusinessDateSpecImpl extends GetBusinessDateSpec {

	public static final String BUSINESS_DATE = "businessDate";
	private final LocalDate businessDate;

	@Inject
	public GetBusinessDateSpecImpl(@Named(BUSINESS_DATE) LocalDate businessDate) {
		this.businessDate = businessDate;
	}

	@Override
	protected Date doEvaluate() {
		return new DateImpl(businessDate);
	}
}
