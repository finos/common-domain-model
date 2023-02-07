package cdm.legaldocumentation.csa.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.legaldocumentation.csa.CreditSupportAgreementElections.CreditSupportAgreementElectionsBuilder;

@SuppressWarnings("unused")
public class CsaSubstitutedRegimeMappingProcessor extends MappingProcessor {

	private final SubstitutedRegimeHelper helper;

	public CsaSubstitutedRegimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new SubstitutedRegimeHelper(modelPath, mappingContext.getMappings(), mappingContext.getSynonymToEnumMap());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		((CreditSupportAgreementElectionsBuilder) parent)
				.setSubstitutedRegime(helper.getSubstitutedRegimes(synonymPath));
	}
}