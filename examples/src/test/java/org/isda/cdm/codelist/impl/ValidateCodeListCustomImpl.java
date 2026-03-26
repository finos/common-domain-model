package org.isda.cdm.codelist.impl;

import cdm.base.staticdata.codelist.CodeList;
import cdm.base.staticdata.codelist.CodeValue;
import cdm.base.staticdata.codelist.functions.LoadCodeList;
import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;

public class ValidateCodeListCustomImpl extends ValidateFpMLCodingSchemeDomain {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeListCustomImpl.class);

    @Inject
    protected LoadCodeList loadCodeListFunc;

    @Override
    protected Boolean doEvaluate(String code, String domain) {
        logger.info("Triggered custom validation implementation with code `{}` and domain `{}`", code, domain);

        CodeList codeList = loadCodeListFunc.evaluate(domain);

        if (!codeList.getCodes().isEmpty())
            return codeList.getCodes().stream().map(CodeValue::getValue).anyMatch(code::equalsIgnoreCase);
        
        return true;
    }
}
