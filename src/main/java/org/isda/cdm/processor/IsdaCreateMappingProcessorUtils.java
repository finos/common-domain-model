package org.isda.cdm.processor;

import cdm.base.staticdata.party.CounterpartyRoleEnum;

import java.util.Arrays;
import java.util.List;

public class IsdaCreateMappingProcessorUtils {

	public static final String PARTY_A = "partyA";
	public static final String PARTY_B = "partyB";
	public static final List<String> PARTIES = Arrays.asList(PARTY_A, PARTY_B);
	public static final String ISDA_CREATE_SYNONYM_SOURCE = "ISDA_Create_1_0";

	public static CounterpartyRoleEnum toCounterpartyRoleEnum(String party) {
		if (PARTY_A.equalsIgnoreCase(party))
			return CounterpartyRoleEnum.PARTY_1;
		else if (PARTY_B.equalsIgnoreCase(party))
			return CounterpartyRoleEnum.PARTY_2;
		else
			throw new IllegalArgumentException(String.format("Unknown ISDA Create party %s", party));
	}
}
