package org.isda.cdm.codelist;

import cdm.base.staticdata.codelist.CodeList;
import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LoadCodeListTest {

    private static final Logger logger = LoggerFactory.getLogger(LoadCodeListTest.class);
    private static Injector injector;

    private static final ObjectMapper mapper = RosettaObjectMapper.getNewRosettaObjectMapper();

    @Inject
    LoadCodeList loadCodeListFunc;

    @Inject
    RosettaTypeValidator validator;

    @BeforeAll
    static void setUpOnce() {
        injector = Guice.createInjector(new CdmRuntimeModule());
    }

    @BeforeEach
    public void setUp() {
        injector.injectMembers(this);
    }

    @Test
    void mustResolveBusinessCenterCodeList() throws JsonProcessingException {
        shoudlResolveCodeListByDomainAsExpected("business-center");
    }

    @Test
    void mustResolveFloatingRateIndexCodeList() throws JsonProcessingException {
        shoudlResolveCodeListByDomainAsExpected("floating-rate-index");
    }

    private void shoudlResolveCodeListByDomainAsExpected( String domain) throws JsonProcessingException {
        //Resolve and log
        CodeList supportedCodes = loadCodeListFunc.evaluate(domain);
        logger.info("CDM CodeList for '{}' domain", domain);
        logger.info(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(supportedCodes));

        //Validate
        ValidationReport report = validator.runProcessStep(CodeList.class, supportedCodes);
        report.logReport();

        //Assert
        assertTrue(report.success(), "Must resolve a valid CodeList");
    }
}
