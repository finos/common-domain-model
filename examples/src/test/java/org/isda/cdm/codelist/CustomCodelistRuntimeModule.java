package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import com.google.inject.AbstractModule;
import org.finos.cdm.CdmRuntimeModule;

public class CustomCodelistRuntimeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ValidateFpMLCodingSchemeDomain.class).to(ValidateCodeListCustomImpl.class);
    }
}
