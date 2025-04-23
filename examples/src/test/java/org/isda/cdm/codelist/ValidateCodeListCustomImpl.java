package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidateCodeListCustomImpl extends ValidateFpMLCodingSchemeDomain {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeListCustomImpl.class);

    public static Map<String, Set<String>> myCodeListSubsets;

    static {
        myCodeListSubsets = new HashMap<>();
        //My allowed business-center codes
        myCodeListSubsets.put("business-center", Set.of("EUTA", "GBLO"));
        myCodeListSubsets.put("floating-rate-index", Set.of("LIBOR", "EURIBOR"));

    }

    @Override
    protected Boolean doEvaluate(String code, String domain) {
        logger.info("Triggered custom validation implementation with code `{}` and domain `{}`", code, domain);

        if (myCodeListSubsets.containsKey(domain))
            return myCodeListSubsets.get(domain).stream().anyMatch(code::equalsIgnoreCase);

        return true;
    }
}
