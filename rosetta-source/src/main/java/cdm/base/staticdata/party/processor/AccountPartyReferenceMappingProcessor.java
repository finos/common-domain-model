package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.*;
import static com.regnosys.rosetta.common.translation.Path.PathElement;

/**
 * FpML mapping processor.
 *
 * In FpML, account references are positioned in PayerReceiver and BuyerSeller, both inside the Product.
 * In CDM, the Product is agnostic to parties and accounts so the account mappings from removed from the Product, and the accounts are
 * associated to the party by mapping attribute Trade.accounts.partyReference.
 */
@SuppressWarnings("unused")
public class AccountPartyReferenceMappingProcessor extends MappingProcessor {

	public AccountPartyReferenceMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getNonNullMappedValue(filterMappings(getMappings(), synonymPath))
				.ifPresent(accountId -> {
					// Find mappings based on the accountId and account reference path, then find the associated party references by replacing account/party in the path.
					List<Mapping> partyRefMappings = getMappings().stream()
							// Find mappings with accountId
							.filter(m -> accountId.equals(m.getXmlValue()))
							// Filter to paths ending in *AccountReference.href
							.filter(this::filterAccountReferencePaths)
							// Replace AccountReference for PartyReference, and get those party reference mappings
							.map(accPathMappings -> filterMappings(getMappings(),
									replacePath(accPathMappings.getXmlPath(), "AccountReference","PartyReference")))
							.flatMap(Collection::stream)
							.filter(this::filterNonNullXmlValues)
							.collect(Collectors.toList());
					// Extract the party references from the mappings
					List<String> partyRefs = partyRefMappings.stream()
							.map(Mapping::getXmlValue)
							.map(String::valueOf).distinct()
							.collect(Collectors.toList());
					if (partyRefs.size() == 1) {
						// Set value
						((ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder) builder).setExternalReference(partyRefs.get(0));
						// Update mapping report using the AccountReference mappings
						partyRefMappings.stream()
								.map(Mapping::getXmlPath)
								.map(partyRefPath -> replacePath(partyRefPath, "PartyReference", "AccountReference"))
								.forEach(accountRefPath -> updateMappings(accountRefPath.getParent(), getMappings(), getModelPath()));
					}
				});
	}

	private boolean filterAccountReferencePaths(Mapping accIdMappings) {
		return accIdMappings.getXmlPath().endsWith("href") && accIdMappings.getXmlPath().getParent()
				.getLastElement()
				.getPathName()
				.endsWith("AccountReference");
	}

	private boolean filterNonNullXmlValues(Mapping partyIdMappings) {
		return Objects.nonNull(partyIdMappings.getXmlValue());
	}

	private Path replacePath(Path accountReferencePath, String target, String replacement) {
		Path parent = accountReferencePath.getParent();
		PathElement partyReferenceElement = PathElement.parse(parent.getLastElement().getPathName().replace(target, replacement));
		return parent.getParent()
				.addElement(partyReferenceElement)
				.addElement("href");
	}
}
