package org.isda.cdm.processor;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.LegalEntity;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.Custodian;
import org.isda.cdm.CustodianElection;

import java.util.List;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CustodianMappingProcessor  extends MappingProcessor {

	public CustodianMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
	}

	@Override
	public void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			Custodian.CustodianBuilder custodianBuilder = (Custodian.CustodianBuilder) builder;
			getCustodianElection(v,"partyA").ifPresent(custodianBuilder::addPartyElection);
			getCustodianElection(v,"partyB").ifPresent(custodianBuilder::addPartyElection);
		});
	}

	@Override
	protected void map(List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Do nothing
	}

	private Optional<CustodianElection> getCustodianElection(String synonymValue, String party) {
		CustodianElection.CustodianElectionBuilder custodianElectionBuilder = CustodianElection.builder();

		String suffix = "collateral_manager".equals(synonymValue) ? "specify" : "custodian_name";
		List<Mapping> custodianNameMappings = findMappings(getMappings(),
				Path.parse(String.format("answers.partyA.%s.%s_%s", synonymValue, party, suffix)));
		Optional<String> custodianName = findMappedValue(custodianNameMappings);
		custodianName.ifPresent(xmlValue -> {
			custodianElectionBuilder.setParty(party);
			custodianElectionBuilder.setCustodian(LegalEntity.builder()
					.setName(FieldWithMetaString.builder()
							.setValue(xmlValue).build()).build());
			custodianNameMappings.forEach(m -> updateMapping(m, getPath()));
		});

		if ("custodian_and_segregated_account_details".equals(synonymValue)) {
			List<Mapping> segregatedCashAccountMappings = findMappings(getMappings(),
					Path.parse(String.format("answers.partyA.custodian_and_segregated_account_details.%s_cash", party)));
			findMappedValue(segregatedCashAccountMappings).ifPresent(xmlValue -> {
				custodianElectionBuilder.setSegregatedCashAccount(Account.builder()
						.setAccountName(FieldWithMetaString.builder().setValue(xmlValue).build())
						.build());
				segregatedCashAccountMappings.forEach(m -> updateMapping(m, getPath()));
			});

			List<Mapping> segregatedSecurityAccountMappings = findMappings(getMappings(),
					Path.parse(String.format("answers.partyA.custodian_and_segregated_account_details.%s_securities", party)));
			findMappedValue(segregatedSecurityAccountMappings).ifPresent(xmlValue -> {
				custodianElectionBuilder.setSegregatedSecurityAccount(Account.builder()
						.setAccountName(FieldWithMetaString.builder().setValue(xmlValue).build())
						.build());
				segregatedSecurityAccountMappings.forEach(m -> updateMapping(m, getPath()));
			});
		}

		return custodianElectionBuilder.hasData() ? Optional.of(custodianElectionBuilder.build()) : Optional.empty();
	}
}