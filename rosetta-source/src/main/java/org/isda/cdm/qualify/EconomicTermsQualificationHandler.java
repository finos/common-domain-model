package org.isda.cdm.qualify;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.product.template.EconomicTerms;
import cdm.product.template.ProductBase;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Qualification handler for EconomicTerms
 */
public class EconomicTermsQualificationHandler implements QualificationHandler<EconomicTerms, ProductBase, ProductBase.ProductBaseBuilder> {

    @Override
    public Class<EconomicTerms> getQualifiableClass() {
        return EconomicTerms.class;
    }

    @Override
    public EconomicTerms getQualifiableObject(ProductBase product) {
        return Optional.ofNullable(product)
                .map(ProductBase::getEconomicTerms)
                .orElse(null);
    }

    @Override
    public String getQualifier(ProductBase productBase) {
        return Optional.ofNullable(productBase)
                .map(ProductBase::getTaxonomy)
                .orElse(Collections.emptyList())
                .stream()
                .map(ProductTaxonomy::getProductQualifier)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setQualifier(ProductBase.ProductBaseBuilder productBuilder, String qualifier) {
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
