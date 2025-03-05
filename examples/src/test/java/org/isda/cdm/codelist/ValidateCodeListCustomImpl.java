package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateCodeListCustomImpl extends ValidateFpMLCodingSchemeDomain {

    private static final Logger logger = LoggerFactory.getLogger(ValidateCodeListCustomImpl.class);

    @Override
    protected Boolean doEvaluate(String code, String domain) {
        logger.info("Triggered custom validation implementation with code `{}` and domain `{}`", code, domain);
        return true;
    }
}
