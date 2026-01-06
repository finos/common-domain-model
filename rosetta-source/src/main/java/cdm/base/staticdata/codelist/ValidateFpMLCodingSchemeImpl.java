package cdm.base.staticdata.codelist;

import cdm.base.staticdata.codelist.functions.LoadCodeList;
import cdm.base.staticdata.codelist.functions.ValidateFpMLCodingSchemeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Implementation of FpML coding scheme validation.
 *
 * This class is responsible for:
 * - Loading CodeList JSON files from resources using LoadCodeList.
 * - Caching loaded CodeLists for efficient validation.
 * - Evaluating whether a given code exists in the relevant CodeList.
 */
public class ValidateFpMLCodingSchemeImpl extends ValidateFpMLCodingSchemeDomain {

    private final Logger logger = LoggerFactory.getLogger(ValidateFpMLCodingSchemeImpl.class);

    @Inject
    LoadCodeList loadCodeListFunc;

    /**
     * Validates whether the provided code exists in the specified domain's CodeList.
     *
     * @param code   The code to validate.
     * @param domain The domain that determines which CodeList should be used.
     * @return True if the code exists in the corresponding CodeList; otherwise, false.
     */
    @Override
    protected Boolean doEvaluate(String code, String domain) {
        logger.debug("Triggered with code={} and domain={}", code, domain);

        // Load the CodeList from resources and cache it
        CodeList codeList = loadCodeListFunc.evaluate(domain);
        if (codeList.getCodes().isEmpty()) {
            logger.error("Loaded empty CodeList for domain '{}'", domain);
        }

        // Check if the provided code exists in the CodeList
        return codeList.getCodes().stream()
                .map(CodeValue::getValue)
                .anyMatch(code::equalsIgnoreCase);
    }
}