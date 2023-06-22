package cdm.event.common.processor;

import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.product.template.TradableProduct.TradableProductBuilder;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.metafields.FieldWithMetaString;

import java.util.List;

public class ORECounterpartyMappingProcessor extends MappingProcessor {

	public ORECounterpartyMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext context) {
		super(modelPath, synonymPaths, context);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		TradableProductBuilder tradable = (TradableProductBuilder) builder;
		if (tradable.getCounterparty().size()==1) {
			tradable.addCounterparty(Counterparty.builder().setPartyReference(
					ReferenceWithMetaParty.builder().setValue(
						Party.builder().setName(
							FieldWithMetaString.builder().setValue("ME").build())
						.build())
					.build())
					.setRole(CounterpartyRoleEnum.PARTY_2)
				.build());
		}
	}
}
