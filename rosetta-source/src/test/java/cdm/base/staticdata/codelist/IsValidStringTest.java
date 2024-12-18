package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.IsValidString;
import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class IsValidStringTest extends AbstractFunctionTest {
    @Inject
    private IsValidString isValidString;

    @Test
    void IsValidString() {
        String domain = "business-center";
        String valid = "USNY";
        String invalid = "USXX";

        test("", domain, valid);
        test("Code USXX not found in "+domain, domain, invalid);
        test("Codelist XXX not found", "XXX", invalid);

        String result = isValidString.evaluate("XXX", "XXX", "XXX");
        assertEquals("Unsupported validation rule XXX", result);
    }

    void test(String expected, String domain, String value) {

        String result = isValidString.evaluate("CodeListValidation", domain, value);
        assertEquals(expected,result);
    }
}
