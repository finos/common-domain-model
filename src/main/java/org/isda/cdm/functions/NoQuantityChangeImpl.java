package org.isda.cdm.functions;

import org.isda.cdm.QuantityChangePrimitive;
import org.isda.cdm.QuantityGroups;
import org.isda.cdm.functions.QuantityCompareUtil.CompareOp;

import java.util.List;

public class NoQuantityChangeImpl extends NoQuantityChange {

	@Override
	protected Boolean doEvaluate(List<QuantityChangePrimitive> quantityChangePrimitive) {
		List<QuantityGroups> beforeQuantity = beforeQuantity(quantityChangePrimitive).getMulti();
		List<QuantityGroups> afterQuantity = afterQuantity(quantityChangePrimitive).getMulti();
		
		return QuantityCompareUtil.compareQuantities(afterQuantity, beforeQuantity, CompareOp.GREATER);
	}
}
