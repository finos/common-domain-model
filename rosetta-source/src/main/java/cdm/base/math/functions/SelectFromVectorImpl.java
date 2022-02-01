package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.List;

public class SelectFromVectorImpl extends SelectFromVector {

	// select a value from a vector by index.  return null if not in range
	@Override
	protected BigDecimal doEvaluate(List<BigDecimal> vector, Integer index) {
		if (vector == null || index == null)
			return null;
		if (vector == null)
			return null;
		return (index >= 0 && index < vector.size()) ? vector.get(index) : null;
	}
}
