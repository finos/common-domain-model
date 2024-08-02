package org.isda.cdm.qualify;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Qualification handler for EconomicTerms
 */
public class EconomicTermsQualificationHandler implements QualificationHandler<EconomicTerms, ContractualProduct, ContractualProduct.ContractualProductBuilder> {

    @Override
    public Class<EconomicTerms> getQualifiableClass() {
        return EconomicTerms.class;
    }

    @Override
    public EconomicTerms getQualifiableObject(ContractualProduct contractualProduct) {
        return Optional.ofNullable(contractualProduct)
                .map(ContractualProduct::getEconomicTerms)
                .orElse(null);
    }

    @Override
    public String getQualifier(ContractualProduct contractualProduct) {
        return Optional.ofNullable(contractualProduct)
                .map(ContractualProduct::getProductTaxonomy)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductTaxonomy::getProductQualifier)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setQualifier(ContractualProduct.ContractualProductBuilder contractualProductBuilder, String qualifier) {
        // Find any existing ProductTaxonomy
        ProductTaxonomy.ProductTaxonomyBuilder productTaxonomyBuilder =
                emptyIfNull(contractualProductBuilder.getProductTaxonomy())
                        .stream()
                        .filter(t -> t.getProductQualifier() != null)
                        .findFirst()
                        .orElse(null);
        // Update existing ProductTaxonomy
        if (productTaxonomyBuilder != null) {
            productTaxonomyBuilder
                    .setProductQualifier(qualifier)
                    .setSource(TaxonomySourceEnum.ISDA);
        } else {
            // Or add new ProductTaxonomy
            contractualProductBuilder.addProductTaxonomy(ProductTaxonomy.builder()
                    .setProductQualifier(qualifier)
                    .setSource(TaxonomySourceEnum.ISDA));
        }
    }
}
