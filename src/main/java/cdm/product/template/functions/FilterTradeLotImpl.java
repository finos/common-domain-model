package cdm.product.template.functions;

import cdm.base.staticdata.identifier.Identifier;
import cdm.product.template.TradeLot;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class FilterTradeLotImpl extends FilterTradeLot {

	@Override
	protected List<TradeLot.TradeLotBuilder> doEvaluate(List<? extends TradeLot> tradeLots, List<? extends Identifier> lotIdentifier) {
		return emptyIfNull(tradeLots).stream()
				.filter(tradeLot -> !Collections.disjoint(emptyIfNull(tradeLot.getLotIdentifier()), emptyIfNull(lotIdentifier)))
				.map(TradeLot::toBuilder)
				.collect(Collectors.toList());
	}
}
