package org.isda.cdm.processor;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.PARTIES;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.toFieldWithMetaString;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.Custodian;
import org.isda.cdm.CustodianElection;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.LegalEntity;

/**
 * ISDA Create mapping processor.
 */
@SuppressWarnings("unused")
public class CustodianMappingProcessor extends MappingProcessor {

	public CustodianMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		Custodian.CustodianBuilder custodianBuilder = (Custodian.CustodianBuilder) builder;
		PARTIES.forEach(party -> getCustodianElection(synonymPath, party).ifPresent(custodianBuilder::addPartyElection));
	}

	private Optional<CustodianElection> getCustodianElection(Path synonymPath, String party) {
		CustodianElection.CustodianElectionBuilder custodianElectionBuilder = CustodianElection.builder();

		String suffix = synonymPath.endsWith("collateral_manager") ? "_specify" : "_custodian_name";
		setValueAndUpdateMappings(synonymPath.addElement(party + suffix),
				(value) -> {
					custodianElectionBuilder.setParty(party);
					custodianElectionBuilder.setCustodian(LegalEntity.builder()
							.setName(toFieldWithMetaString(value))
							.build());
				});

		if (synonymPath.endsWith("custodian_and_segregated_account_details")) {
			setValueAndUpdateMappings(synonymPath.addElement(party + "_cash"),
					(value) -> custodianElectionBuilder.setSegregatedCashAccount(Account.builder()
							.setAccountName(toFieldWithMetaString(value))
							.build()));

			setValueAndUpdateMappings(synonymPath.addElement(party + "_securities"),
					(value) -> custodianElectionBuilder.setSegregatedSecurityAccount(Account.builder()
							.setAccountName(toFieldWithMetaString(value))
							.build()));
		}

		return custodianElectionBuilder.hasData() ? Optional.of(custodianElectionBuilder.build()) : Optional.empty();
	}
}