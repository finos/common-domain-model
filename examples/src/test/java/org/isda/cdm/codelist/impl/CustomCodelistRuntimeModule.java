package org.isda.cdm.codelist.impl;

import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import com.google.inject.AbstractModule;

public class CustomCodelistRuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ValidateFpMLCodingSchemeDomain.class).to(ValidateCodeListCustomImpl.class);
    }
}
