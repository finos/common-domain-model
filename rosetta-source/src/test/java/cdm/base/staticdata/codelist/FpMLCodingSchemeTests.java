package cdm.base.staticdata.codelist;

import cdm.base.datetime.BusinessCenterTime;
import cdm.event.common.TradeState;
import cdm.product.template.EconomicTerms;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.validation.ValidationResult;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class FpMLCodingSchemeTests {

    private static final Logger logger = LoggerFactory.getLogger(FpMLCodingSchemeTests.class);
    private static Injector injector;
    @Inject
    RosettaTypeValidator validator;

    private static ObjectMapper mapper;

    @BeforeAll
    public static void setUpOnce() {
        mapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        injector = Guice.createInjector(new CdmRuntimeModule());
    }

    @BeforeEach
    public void setUp() {
        injector.injectMembers(this);
    }

    @Test
    void test1() {
        //TODO: FieldWithMetaBusinessCenter is not being generated
        BusinessCenterTime bct = BusinessCenterTime.builder()
                .setBusinessCenter(FieldWithMetaString.builder()
                        .setValue("TEST")
                        .build())
                .build();
        ValidationReport report = validator.runProcessStep(BusinessCenterTime.class, bct);
        report.getValidationResults().stream().map(ValidationResult::toString).forEach(logger::info);
    }

    @Test
    void test2() throws IOException {
        final URL source = FpMLCodingSchemeTests.class.getClassLoader().getResource("result-json-files/fpml-5-13/products/interest-rate-derivatives/ird-ex01-vanilla-swap.json");
        assertNotNull(source);

        TradeState tradeState = mapper.readValue(source, TradeState.class);
        ValidationReport report = validator.runProcessStep(EconomicTerms.class, tradeState.getTrade().getProduct().getEconomicTerms());
        report.getValidationResults().stream().filter(it -> !it.isSuccess()).map(ValidationResult::toString).forEach(logger::info);
        assertNotNull(tradeState);
    }
}
