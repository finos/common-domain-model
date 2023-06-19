package com.icma.cdm.example;

import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantityChangeDirectionEnum;
import cdm.base.math.UnitType;
import cdm.base.math.functions.Create_NonNegativeQuantitySchedule;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.event.common.PrimitiveInstruction;
import cdm.event.common.PrimitiveInstruction.PrimitiveInstructionBuilder;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.common.settlement.functions.Create_PriceQuantity;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.rosetta.model.lib.functions.ModelObjectValidator;
import com.rosetta.model.lib.functions.RosettaFunction;
import com.rosetta.model.lib.functions.*;
import com.rosetta.model.lib.mapper.Mapper;
import com.rosetta.model.lib.mapper.MapperC;
import com.rosetta.model.lib.mapper.MapperS;
import java.math.BigDecimal;
import java.util.Optional;

import static com.rosetta.model.lib.expression.ExpressionOperators.*;

@ImplementedBy(Create_Validation.Create_ValidationDefault.class)
public abstract class Create_Validation implements RosettaFunction {
    @Inject
    protected ModelObjectValidator objectValidator;
    @Inject protected ConditionValidator conditionValidator;
    public PrimitiveInstruction evaluate(TradeState tradeState) {

        PrimitiveInstruction.PrimitiveInstructionBuilder instruction = doEvaluate(tradeState);

        if (instruction != null) {
            //instruction = null;
            objectValidator.validate(PrimitiveInstruction.class, instruction);
       }
        return instruction;
    }

    protected abstract PrimitiveInstruction.PrimitiveInstructionBuilder doEvaluate(TradeState tradeState);

    public static class Create_ValidationDefault extends Create_Validation {

        @Override
        protected PrimitiveInstruction.PrimitiveInstructionBuilder doEvaluate(TradeState tradeState) {
            PrimitiveInstruction.PrimitiveInstructionBuilder instruction = PrimitiveInstruction.builder();
            return assignOutput(instruction, tradeState);
        }

        protected PrimitiveInstruction.PrimitiveInstructionBuilder assignOutput(PrimitiveInstruction.PrimitiveInstructionBuilder instruction, TradeState tradeState) {

            return Optional.ofNullable(instruction)
                    .map(o -> o.prune())
                    .orElse(null);
        }

    }
}

