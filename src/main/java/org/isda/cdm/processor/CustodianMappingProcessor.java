package org.isda.cdm.processor;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.LegalEntity;
import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.Custodian;
import org.isda.cdm.CustodianElection;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.toFieldWithMetaString;
import static org.isda.cdm.processor.RegimeMappingHelper.PARTIES;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CustodianMappingProcessor extends MappingProcessor {

	public CustodianMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			Custodian.CustodianBuilder custodianBuilder = (Custodian.CustodianBuilder) builder;
			PARTIES.forEach(party -> getCustodianElection(v, party).ifPresent(custodianBuilder::addPartyElection));
		});
	}

	private Optional<CustodianElection> getCustodianElection(String synonymValue, String party) {
		CustodianElection.CustodianElectionBuilder custodianElectionBuilder = CustodianElection.builder();

		String suffix = "collateral_manager".equals(synonymValue) ? "specify" : "custodian_name";
		setValueFromMappings(String.format("answers.partyA.%s.%s_%s", synonymValue, party, suffix),
				(value) -> {
					custodianElectionBuilder.setParty(party);
					custodianElectionBuilder.setCustodian(LegalEntity.builder()
							.setName(toFieldWithMetaString(value))
							.build());
				});

		if ("custodian_and_segregated_account_details".equals(synonymValue)) {
			setValueFromMappings(String.format("answers.partyA.custodian_and_segregated_account_details.%s_cash", party),
					(value) -> custodianElectionBuilder.setSegregatedCashAccount(Account.builder()
							.setAccountName(toFieldWithMetaString(value))
							.build()));

			setValueFromMappings(String.format("answers.partyA.custodian_and_segregated_account_details.%s_securities", party),
					(value) -> custodianElectionBuilder.setSegregatedSecurityAccount(Account.builder()
							.setAccountName(toFieldWithMetaString(value))
							.build()));
		}

		return custodianElectionBuilder.hasData() ? Optional.of(custodianElectionBuilder.build()) : Optional.empty();
	}
}