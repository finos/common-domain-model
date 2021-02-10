# *CDM Model: Refactoring of Price and Quantity – Phase 2*

_What is Being Released_

The continuation of the refactoring of the price and quantity data types in the CDM.
 
**Background**

This phase of the price and quantity refactoring achieves multiple objectives: 

*	Creates revised atomic price and quantity data types that can be used anywhere in the model where a price and/or quantity is required.  
*	Inserts those new types into the ``TradableProduct`` data type and into specific locations in the ``Payout`` data type and other sections of the model.
* Allows for a product definition that is fully agnostic of the price and quantity for a specific trade.  
* Enables an efficient process for functions whose inputs require product definitions and related price and quantity. The previous version used an arbitrary set of attributes as a proxy to another set of attributes which is not appropriate, because that is the role of a reference – this new release introduces a new referencing mechanism consisting of an address and location pair that allows efficient referencing of ``price`` and ``quantity`` to the ``Payout`` data types for purposes of function inputs while upholding the product agnostic principle for the persisted objects.

**Primary Model Changes**  

* Added a new base data type, ``MeasureBase``, which represents two common attributes for prices and quantities:

    *  ``amount``, which is a number
    * ``unitOfAmount`` , which is of type ``UnitType``, a new data type that qualifies the unit by which the amount is measured (e.g. a specific currency, a specific financial unit such as shares, or a capacity unit such as barrels) 
    
* Added new enumerated sets to support ``UnitType``:
    * ``WeatherUnitEnum``
    * ``FinancialUnitEnum``
    * ``CapacityUnitEnum``, which replaces ``UnitEnum``
  
*	Revised the ``Price`` data type which 
    * extends ``MeasureBase`` with two attributes
      * the mandatory ``priceType`` attribute which is of type ``PriceTypeEnum``, a new enumerated set
      * ``perUnitOfAmount``, which is of type ``UnitType``, and provides an optional attribute to further define the price 
    * replaces ``PriceNotation`` and the encapsulated data types of ``ActualPrice``, ``CashPrice``, and ``Price``
    * and replaces the old ``Price`` data types and the number data type in selected locations within ``Payout``

* Revised the ``Quantity`` data type which 
    * extends ``MeasureBase`` with two optional attributes
      *   ``multiplier`` which is a number
      *   ``multiplierUnit`` which is of type ``UnitType``
    * replaces the ``QuantityNotation`` data type throughout the model
    
* Revised the ``Observable`` data type which
    * specifies the object to be observed for a price – the object could be an asset or a reference
    * replaces ``AssetIdentifier`` with a refined data structure, e.g. currency is not included because it is defined in the ``unitOfAmount`` where applicable
    
* Added a new data type, ``PriceQuantity`` which 
    * specifies the price, quantity, and optionally the observable for use in a trade or other purposes
    * comprises three attributes
      *	``price`` 
      * ``quantity``
      * ``observable``
   * includes a new metadata type, called address-location which
      * can be used as a special reference to point to payout data types or other locations
      * are assigned to the ``price`` and ``quantity`` attributes  
  
* Revised the ``TradableProduct``data type which 
    *  replaces ``priceNotation`` and ``quantityNotation``with the ``priceQuantity`` attribute which is of the ``PriceQuantity`` data type
    *  adds ``settlementTerms``, for which the data type is a revised ``SettlementTerms`` that replaces ``assetIdentifier`` with ``priceQuantity``  

* Removed ``PriceNotation``, ``QuantityNotation``, and ``AssetIdentifier`` from the model because they have been replaced by the data types described above

**Additional Related Model Changes** 

* The following attributes now use the revised ``Quantity`` data type and a metadata address that points to ``PriceQuantity``->``Quantity``:
    *	``ResolvableQuantity``-> ``resolvedQuantity``
    * ``ResolvableQuantity``-> ``quantitySchedule`` -> ``initialQuantity`` 
 
* The following attributes now use the modified ``Price`` data type and a metadata address that points to ``PriceQuantity``-> ``Price`` :
    * ``RateSchedule``-> ``initialPrice``
    *	``OptionExercise``-> ``strike`` -> ``StrikePrice``

* Other modified data types:
    *	``AllocationBreakdown`` -> ``quantity`` now uses the revised ``Quantity`` data type
    * ``ReferenceInformation`` -> ``price`` now uses the revised ``Price`` data type
    *	``InflationRateSpecification`` condition now refers to ``Observable`` in place of ``AssetIdentifier``
    *	``EquityValuation`` -> ``price`` now uses the revised ``Price`` data type

* Modified a set of creation functions to use the modified ``PriceQuantity``data type and/or its encapsulated types:
    * ``Create_Allocation`` : Change in Embedded Function
    *	``Create_ClearedTrade`` : Change in Alias
    * ``Create_Execution`` : Change in Input , Output
    * ``Create_ExecutionPrimitive`` : Change in Input , Output
    * ``Create_Exercise ``: Change in Condition
    * ``Create_TransferPrimitive``: Change in Condition

* Modified a set of qualification functions to use the modified ``PriceQuantity``data type and/or its encapsulated types:
    * ``Qualify_InterestRate_IRSwap_Basis_OIS`` : Change in Output
    * ``Qualify_InterestRate_IRSwap_FixedFloat`` : Change in Output
    * ``Qualify_InterestRate_IRSwap_FixedFloat_OIS`` : Change in Output
    * ``Qualify_InterestRate_IRSwap_FixedFloat_PlainVanilla`` : Change in Output
    * ``Qualify_StockSplit`` : Change in Alias

* Modified other functions to use the modified ``PriceQuantity``data type and/or its encapsulated types:
    * ``CurrencyAmount`` : Change in Input  
    * ``Equals`` : Change in Input, Output
    * ``EquityCashSettlementAmount`` : Change in Output
    * ``EquityPerformance`` : Change in Embedded Function
    * ``ExtractQuantityByAsset`` : Change in Input, Output
    * ``ExtractQuantityByCurrency`` : Change in Input , Output
    * ``FloatingAmount`` : Change in Embedded Function
    * ``FxMarkToMarket`` : Change in Alias
    * ``GreaterThan`` : Change in Input, Output
    * ``GreaterThanEquals`` : Change in Input, Output
    * ``NoOfUnits`` : Change in Input  
    * ``Plus`` : Change in Input, Output
    * ``PriceQuantityTriangulation`` : Change in Input  
    * ``ResolvablePayoutQuantity`` : Change in Output
    * ``ResolveEquityInitialPrice`` : Change in Input , Output
    * ``ResolveEquityPeriodEndPrice`` : Change in Output
    * ``ResolveEquityPeriodStartPrice`` : Change in Input, Output
    * ``StockSplit`` : Change in Alias
    * ``TerminateContract`` : Change in Output

* Removed one function that is no longer needed: 
    * Removed the function ``ExchangeRateQuantityTriangulation`` which was an embedded function the ``CashPriceQuantityNoOfUnitsTriangulation`` function because the new ``PriceQuantity`` structure enforces the logic that was defined in this function
    
* Modified a set of Reporting Rule changes to use the modified ``PriceQuantity``data type and/or its encapsulated types:
    * ``FixedRatePrice``
    * ``FloatingRatePrice``
    * ``SingleCurrencyBasisSwap``
    * ``CrossCurrencySwapBuyerSeller``
    * ``FixedFloatPrice``
    * ``BasisSwapPrice``
    * ``FixedFixedPrice``
    * ``CDSPrice``
    * ``Quantity``

* Added new synonym data types and synonym enums:
    * ``Price``
    * ``PriceQuantity``
    * ``PriceQuantity``
    * ``UnitType``
    * ``CapacityUnitEnum``
    * ``FinancialUnitEnum``
    * ``PriceTypeEnum``
    * ``WeatherUnitEnum``

* Modified a set of synonym data types to use the modified ``PriceQuantity``data type and/or its encapsulated types:
    * ``Price``
    * ``RateSpecificationBase``
    * ``ResolvablePayoutQuantity``
    * ``TradableProduct``
    * ``OptionStrike``

* Synonym data types replaced by the modified ``PriceQuantity`` data type and/or its encapsulated types:
    * ``CashPrice``
    * ``ActualPrice``
    * ``AssetIdentifier``
    * ``PriceNotation``
    * ``QuantityNotation``
 
**Review Directions**
* In the CDM Portal, use the Textual Browser or Graphical Navigator to review the data types, attributes, functions, enumerated sets, and synonyms referenced in this note. 
* Review ingestion examples, such as ``ird ex29 non deliverable settlement swap uti``.
 

