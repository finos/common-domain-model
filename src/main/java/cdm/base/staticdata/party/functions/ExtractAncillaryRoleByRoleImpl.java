package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.AncillaryRoleEnum;
import cdm.base.staticdata.party.AncillaryRole;

import java.util.List;

public class ExtractAncillaryRoleByRoleImpl extends ExtractAncillaryRoleByRole {

	@Override
	protected AncillaryRole.AncillaryRoleBuilder doEvaluate(List<AncillaryRole> roles, AncillaryRoleEnum rolesEnumToExtract) {
		return roles.stream()
				.filter(cp -> cp.getRole() == rolesEnumToExtract)
				.map(AncillaryRole::toBuilder)
				.findFirst()
				.orElse(AncillaryRole.builder());
	}
}