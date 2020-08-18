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
					List<Mapping> partyRefMappings = getMappings().stream()
							.filter(m -> accountId.equals(m.getXmlValue()))
							.filter(this::filterAccountReferencePaths)
							.map(accPathMappings -> filterMappings(getMappings(),
									replacePath(accPathMappings.getXmlPath(), "AccountReference","PartyReference")))
							.flatMap(Collection::stream)
							.filter(this::filterNonNullXmlValues)
							.collect(Collectors.toList());
					List<String> partyRefs = partyRefMappings.stream()
							.map(Mapping::getXmlValue)
							.map(String::valueOf).distinct()
							.collect(Collectors.toList());
					if (partyRefs.size() == 1) {
						// Set value
						((ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder) parent).setExternalReference(partyRefs.get(0));
						// Update mapping report
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
