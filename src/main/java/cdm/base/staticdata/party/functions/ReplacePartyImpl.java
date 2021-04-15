package cdm.base.staticdata.party.functions;

import cdm.base.staticdata.party.Party;
import com.rosetta.model.lib.RosettaModelObject;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class ReplacePartyImpl extends ReplaceParty {

	@Override
	protected List<Party.PartyBuilder> doEvaluate(List<? extends Party> parties, Party oldParty, Party newParty) {
		List<Party.PartyBuilder> updatedParties = emptyIfNull(parties)
				.stream()
				.map(Party::toBuilder)
				.filter(p -> Optional.ofNullable(oldParty)
						.map(Party::toBuilder)
						.map(op -> !p.prune().equals(op.prune()))
						.orElse(true)) // don't filter if null
				.collect(Collectors.toList());
		updatedParties.add(newParty.toBuilder().prune());
		return updatedParties;
	}
}
