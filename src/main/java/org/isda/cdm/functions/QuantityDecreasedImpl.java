package org.isda.cdm.functions;

import java.util.List;

import org.isda.cdm.QuantityChangePrimitive;
import org.isda.cdm.QuantityGroups;
import org.isda.cdm.functions.QuantityCompareUtil.CompareOp;

public class QuantityDecreasedImpl extends QuantityDecreased {

	@Override
	protected Boolean doEvaluate(List<QuantityChangePrimitive> quantityChangePrimitive) {
		List<QuantityGroups> beforeQuantity = beforeQuantity(quantityChangePrimitive).getMulti();
		List<QuantityGroups> afterQuantity = afterQuantity(quantityChangePrimitive).getMulti();
		
		return QuantityCompareUtil.compareQuantities(beforeQuantity, afterQuantity, CompareOp.GREATER);
	}

}
