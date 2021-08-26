package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.List;

public class LastInVectorImpl extends LastInVector {

	@Override
	protected BigDecimal doEvaluate(List<BigDecimal> vector) {
		// return last in the list if the list is not empty
		if (vector != null && vector.size() > 0) {
			return vector.get(vector.size() - 1);
		}
		return null;
	}
}
