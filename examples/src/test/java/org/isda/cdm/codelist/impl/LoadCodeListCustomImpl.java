package org.isda.cdm.codelist.impl;

import cdm.base.staticdata.codelist.CodeList;
import cdm.base.staticdata.codelist.CodeValue;
import cdm.base.staticdata.codelist.functions.LoadCodeList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LoadCodeListCustomImpl extends LoadCodeList {

    private static final Logger logger = LoggerFactory.getLogger(LoadCodeListCustomImpl.class);

    public static Map<String, Set<String>> myCodeListSubsets;

    /**
     * Custom definition of the supported code lists as Set<String>.
     */
    static {
        myCodeListSubsets = new HashMap<>();
        //My allowed business-center codes
        myCodeListSubsets.put("business-center", Set.of("EUTA", "GBLO"));
        myCodeListSubsets.put("floating-rate-index", Set.of("LIBOR", "EURIBOR"));

    }

    @Override
    protected CodeList.CodeListBuilder doEvaluate(String domain) {
        CodeList.CodeListBuilder result = CodeList.builder();
        myCodeListSubsets.get(domain).forEach(
                codeString -> result.addCodes(CodeValue.builder().setValue(codeString).build())
        );

        logger.debug("Loaded codes for domain {}: [{}]", domain, result.getCodes().stream()
                .map(codeStr -> "'"+codeStr+"'").collect(Collectors.joining(", ")));

        return result;
    }
}
