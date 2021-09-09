package cdm.base.math.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class AppendToVectorImpl extends AppendToVector {

	// append a value to a vector
	@Override
	protected List<BigDecimal> doEvaluate(List<BigDecimal> vector, BigDecimal value) {
		List<BigDecimal> values = new ArrayList<>(emptyIfNull(vector));
		if (value != null)
			values.add(value);
		return values;
	}
}
