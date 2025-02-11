package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Deprecated
class LoadCodeListTest extends AbstractFunctionTest {
    @Inject
    private LoadCodeList LoadCodeList;

    //@Test
    void loadCodeList() {
        String domain = "business-center";

        CodeList codeList = LoadCodeList.evaluate(domain);
        int numCodes = codeList.getCodes().size();
        String firstCode = codeList.getCodes().get(0).getValue();
        assertEquals(firstCode, "AEAB");
        String lastCode = codeList.getCodes().get(numCodes-1).getValue();
        assertEquals(lastCode, "ZWHA" );
        assertEquals(207, numCodes);
    }

}

