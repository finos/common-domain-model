package org.isda.cdm.qualify;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.product.template.EconomicTerms;
import cdm.product.template.NonTransferableProduct;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Qualification handler for EconomicTerms
 */
public class EconomicTermsQualificationHandler implements QualificationHandler<EconomicTerms, NonTransferableProduct, NonTransferableProduct.NonTransferableProductBuilder> {

    @Override
    public Class<EconomicTerms> getQualifiableClass() {
        return EconomicTerms.class;
    }

    @Override
    public EconomicTerms getQualifiableObject(NonTransferableProduct product) {
        return Optional.ofNullable(product)
                .map(NonTransferableProduct::getEconomicTerms)
                .orElse(null);
    }

    @Override
    public String getQualifier(NonTransferableProduct productBase) {
        return Optional.ofNullable(productBase)
                .map(NonTransferableProduct::getTaxonomy)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductTaxonomy::getProductQualifier)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setQualifier(NonTransferableProduct.NonTransferableProductBuilder productBuilder, String qualifier) {
        // Find any existing ProductTaxonomy
        ProductTaxonomy.ProductTaxonomyBuilder productTaxonomyBuilder =
                emptyIfNull(productBuilder.getTaxonomy())
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
            productBuilder.addTaxonomy(ProductTaxonomy.builder()
                    .setProductQualifier(qualifier)
                    .setSource(TaxonomySourceEnum.ISDA));
        }
    }
}
