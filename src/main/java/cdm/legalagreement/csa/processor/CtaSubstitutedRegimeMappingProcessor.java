package cdm.legalagreement.csa.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

import static cdm.legalagreement.csa.CollateralTransferAgreementElections.CollateralTransferAgreementElectionsBuilder;

@SuppressWarnings("unused")
public class CtaSubstitutedRegimeMappingProcessor extends MappingProcessor {

	private final SubstitutedRegimeHelper helper;

	public CtaSubstitutedRegimeMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
		this.helper = new SubstitutedRegimeHelper(modelPath, mappingContext.getMappings());
	}

	@Override
	public void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builders, RosettaModelObjectBuilder parent) {
		((CollateralTransferAgreementElectionsBuilder) parent)
				.clearSubstitutedRegime()
				.addSubstitutedRegime(helper.getSubstitutedRegimes(synonymPath));
	}
}