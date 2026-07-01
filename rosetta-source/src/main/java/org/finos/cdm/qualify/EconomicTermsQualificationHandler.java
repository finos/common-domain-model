package org.finos.cdm.qualify;

import cdm.base.staticdata.asset.common.ProductTaxonomy;
import cdm.base.staticdata.asset.common.TaxonomySourceEnum;
import cdm.base.staticdata.asset.common.TaxonomyValue;
import cdm.product.template.EconomicTerms;
import cdm.product.template.NonTransferableProduct;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationHandler;
import com.rosetta.model.metafields.FieldWithMetaString;

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
                .filter(t -> Optional.ofNullable(t.getCalculated()).orElse(false))
                .map(ProductTaxonomy::getValue)
                .filter(Objects::nonNull)
                .map(TaxonomyValue::getName)
                .filter(Objects::nonNull)
                .map(FieldWithMetaString::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void setQualifier(NonTransferableProduct.NonTransferableProductBuilder productBuilder, String qualifier) {
        // Find any existing ProductTaxonomy
        ProductTaxonomy.ProductTaxonomyBuilder productTaxonomyBuilder =
                emptyIfNull(productBuilder.getTaxonomy())
                        .stream()
                        .filter(t -> Optional.ofNullable(t.getCalculated()).orElse(false))
                        .findFirst()
                        .orElse(null);
        // Update existing ProductTaxonomy
        if (productTaxonomyBuilder != null) {
            productTaxonomyBuilder
                    .setValue(TaxonomyValue.builder().setNameValue(qualifier));
        } else {
            // Or add new ProductTaxonomy
            productBuilder.addTaxonomy(ProductTaxonomy.builder()
                    .setValue(TaxonomyValue.builder().setNameValue(qualifier))
                    .setSource(TaxonomySourceEnum.ISDA)
                    .setCalculated(true));
        }
    }
}
