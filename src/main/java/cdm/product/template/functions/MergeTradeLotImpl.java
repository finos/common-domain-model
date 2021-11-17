package cdm.product.template.functions;

import cdm.base.staticdata.identifier.Identifier;
import cdm.product.template.TradeLot;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class MergeTradeLotImpl extends MergeTradeLot {

	@Override
	protected List<TradeLot.TradeLotBuilder> doEvaluate(List<? extends TradeLot> tradeLots, TradeLot newTradeLot) {
		return emptyIfNull(tradeLots).stream()
				.map(TradeLot::toBuilder)
				.map(builder -> {
					return matches(emptyIfNull(newTradeLot.getLotIdentifier()), emptyIfNull(builder.getLotIdentifier())) ?
							builder.setLotIdentifier(newTradeLot.getLotIdentifier()).setPriceQuantity(newTradeLot.getPriceQuantity()) :
							builder;
				})
				.collect(Collectors.toList());
	}

	private boolean matches(List<? extends Identifier> l1, List<? extends Identifier> l2) {
		return !Collections.disjoint(l1, l2) || (l1.isEmpty() && l2.isEmpty());
	}
}
