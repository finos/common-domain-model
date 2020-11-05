package org.isda.cdm.processor;

import cdm.base.staticdata.party.CounterpartyEnum;

public class IsdaCreateMappingProcessorUtils {

	public static CounterpartyEnum toCounterpartyEnum(String party) {
		if ("partyA".equalsIgnoreCase(party))
			return CounterpartyEnum.PARTY_1;
		else if ("partyB".equalsIgnoreCase(party))
			return CounterpartyEnum.PARTY_2;
		else
			throw new IllegalArgumentException(String.format("Unknown ISDA Create party %s", party));
	}
}
