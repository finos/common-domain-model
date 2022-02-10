package cdm.product.template.processor;

import com.regnosys.rosetta.common.translation.Path;

public class FraHelper {

	/**
	 * Create new dummy synonym path so the reference code can differentiate it from the fixed leg path.
	 */
	static Path getDummyFloatingLegPath(Path synonymPath) {
		return Path.parse("dummyFloatingLeg").append(synonymPath);
	}
}
