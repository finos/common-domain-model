package com.regnosys.cdm.example.util;

import cdm.base.staticdata.asset.common.ProductIdentifier;

public class ProductIdentifiers {

    public static boolean areEqual(ProductIdentifier left, ProductIdentifier right) {
        return left.getIdentifier().getValue().equals(right.getIdentifier().getValue())
                && left.getSource().equals(right.getSource());
    }
}
