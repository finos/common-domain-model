package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;

import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterPartyRoleImpl extends FilterPartyRole {

	@Override
	protected List<PartyRole.PartyRoleBuilder> doEvaluate(List<? extends PartyRole> partyRoles, PartyRoleEnum partyRoleEnum) {
		return emptyIfNull(partyRoles).stream()
				.filter(role -> role.getRole() == partyRoleEnum)
				.map(PartyRole::toBuilder)
				.collect(Collectors.toList());
	}
}
