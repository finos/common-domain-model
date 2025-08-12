package cdm.csv.functions;

import java.math.BigDecimal;
import java.util.List;

public class GetElementImpl extends GetElement {
    @Override
    protected String doEvaluate(List<String> inputList, BigDecimal index) {
        return inputList.get(index.intValue());
    }
}
