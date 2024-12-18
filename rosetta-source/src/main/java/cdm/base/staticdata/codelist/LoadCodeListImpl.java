package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;

import java.io.IOException;

public class LoadCodeListImpl extends LoadCodeList {
    @Override
    protected CodeList.CodeListBuilder doEvaluate(String domain) {

        JsonCodeLoader loader = JsonCodeLoader.getInstance();
        try {
            CodeList codeList = loader.loadCodelist(domain);
            return codeList.toBuilder();
        } catch (IOException e) {
            return null;
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
