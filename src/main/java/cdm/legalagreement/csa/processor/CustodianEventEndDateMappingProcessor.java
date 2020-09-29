package cdm.legalagreement.csa.processor;

import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;

/**
 * "Days after Custodian Event" ( "days_after_custodian_event" / "days_after_collateral_manager_event" / "days_after_euroclear_event")
 *   when "days_after_custodian_event" = "days"
 *     No. of days: "after_days" (number)
 *     Type of day: "after_days_type":
 *       "Days" ("days_after_days"),
 *       "Calendar days" ("calendar_days_after_days"),
 *       "other" ("specify_after_days")
 *   when "days_after_custodian_event" = "other"
 *     Specify Text: "after_specify"
 *
 * "Release Date"
 *   when "release_date" = "days"
 *     No. of days: "release_days" (number)
 *     Type of day: "release_days_type"
 *       "Days" ("day_release_days"),
 *       "Calendar days" ("calendar_day_release_days"),
 *       "other" ("specify_release_days")
 *   when "release_date" = "other"
 *     Specify Text: "release_specify"
 *
 * "Release Date (I) (CMSA termination)"
 *   when "release_date_i" = "days"
 *     No. of days: "release_i_days" (number)
 *     Type of day: "release_date_i_type"
 *       "Days" ("day_release_date_i"),
 *       "Calendar days" ("calendar_day_release_date_i"),
 *       "other" ("specify_release_date_i")
 *   when "release_date_i" = "other"
 *     Specify Text: "release_i_specify"
 *
 * "Date of Timely Statement"
 *   when "date_of_timely_statement" = "days"
 *
 *     No. of days prior to Release Date: "days_prior_to_release_date" (number)
 *     Type of day: "days_prior_to_release_date_type"
 *       "Days" ("days_days_prior_to_release_date"),
 *       "Calendar days" ("calendar_days_days_prior_to_release_date"),
 *       "other" ("specify_days_prior_to_release_date")
 *
 *     No. of days prior to Release Date: "timely_days" (number)
 *     Type of day: "timely_days_type"
 *       "Days" ("days_timely_days"),
 *       "Calendar days" ("calendar_days_timely_days"),
 *       "other" ("specify_timely_days")
 *
 *     No. of days after timely statement is effective: "days_after_timely_statement" (number)
 *     Type of day: "days_after_timely_statement_type":
 *       "Day" ("days_days_after_timely_statement"),
 *       "Local Business Day" ("local_business_days_days_after_timely_statement"),
 *       "other" ("specify_days_after_timely_statement")
 *
 *   when "date_of_timely_statement" = "other"
 *     Specify Text: "timely_specify"
 */
@SuppressWarnings("unused")
public class CustodianEventEndDateMappingProcessor extends org.isda.cdm.processor.CustodianEventEndDateMappingProcessor {

	public CustodianEventEndDateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}
}