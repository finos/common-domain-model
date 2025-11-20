package cdm.ingest.fpml.confirmation.common.functions;

import java.util.Optional;

public class StringContainsImpl extends StringContains {

    @Override
    protected Boolean doEvaluate(String input, String str) {
        return Optional.ofNullable(input)
                .map(s -> s.contains(str))
                .orElse(false);
    }
}