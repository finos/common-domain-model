package org.isda.cdm.functions;

import java.util.List;

import org.isda.cdm.QuantityNotation;
import org.isda.cdm.QuantityNotation.QuantityNotationBuilder;
import org.isda.cdm.QuantityNotationEnum;

public class ExtractQuantityByNotationImpl extends ExtractQuantityByNotation {

	@Override
	protected QuantityNotationBuilder doEvaluate(List<QuantityNotation> quantities, QuantityNotationEnum notation) {
		return quantities.stream()
				.filter(qty -> notation.equals(qty.getNotationTag())).findAny()
				.map(QuantityNotation::toBuilder)
				.orElse(QuantityNotation.builder());
	}

}
