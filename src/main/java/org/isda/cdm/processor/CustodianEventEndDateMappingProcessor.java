package org.isda.cdm.processor;

import cdm.base.datetime.DayTypeEnum;
import cdm.base.datetime.Offset;
import cdm.base.datetime.PeriodEnum;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.CustodianEventEndDate;
import org.isda.cdm.CustomisableOffset;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.isda.cdm.processor.MappingProcessorUtils.*;

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
public class CustodianEventEndDateMappingProcessor extends MappingProcessor {

	private final Map<String, DayTypeEnum> synonymToDayTypeEnumMap;

	public CustodianEventEndDateMappingProcessor(RosettaPath rosettaPath, List<String> synonymValues, List<Mapping> mappings) {
		super(rosettaPath, synonymValues, mappings);
		this.synonymToDayTypeEnumMap = synonymToEnumValueMap(DayTypeEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
	}

	@Override
	protected <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		getSynonymValues().forEach(v -> {
			CustodianEventEndDate.CustodianEventEndDateBuilder endDateBuilder = (CustodianEventEndDate.CustodianEventEndDateBuilder) builder;
			// DaysAfterCustodianEvent
			getCustomisableOffset(v, "days_after_custodian_event",
					"after_days",
					true,
					"after_days_type",
					"after_specify")
					.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
			getCustomisableOffset(v, "days_after_collateral_manager_event",
					"after_days",
					true,
					"after_days_type",
					"after_specify")
					.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
			getCustomisableOffset(v, "days_after_euroclear_event",
					"after_days",
					true,
					"after_days_type",
					"after_specify")
					.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
			getCustomisableOffset(v, "days_after_clearstream_event",
					"after_days",
					true,
					"after_days_type",
					"after_specify")
					.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
			// ReleaseDate
			getCustomisableOffset(v, "release_date",
					"release_days",
					true,
					"release_days_type",
					"release_specify")
					.ifPresent(endDateBuilder::setReleaseDate);
			getCustomisableOffset(v, "release_date_i",
					"release_i_days",
					true,
					"release_date_i_type",
					"release_i_specify")
					.ifPresent(endDateBuilder::setReleaseDate);
			// DateOfTimelyStatement
			getCustomisableOffset(v, "date_of_timely_statement",
					"days_prior_to_release_date",
					false,
					"days_prior_to_release_date_type",
					"timely_specify")
					.ifPresent(endDateBuilder::setDateOfTimelyStatement);
			getCustomisableOffset(v, "date_of_timely_statement",
					"timely_days",
					false,
					"timely_days_type",
					"timely_specify")
					.ifPresent(endDateBuilder::setDateOfTimelyStatement);
			getCustomisableOffset(v, "date_of_timely_statement",
					"days_after_timely_statement",
					true,
					"days_after_timely_statement_type",
					"timely_specify")
					.ifPresent(endDateBuilder::setDateOfTimelyStatement);
		});
	}

	private Optional<CustomisableOffset> getCustomisableOffset(String synonym,
			String endDateTypeSynonym,
			String numberOfDaysSynonym,
			boolean after,
			String dayTypeSynonym,
			String customEndDateSynonym) {

		CustomisableOffset.CustomisableOffsetBuilder customisableOffsetBuilder = CustomisableOffset.builder();

		Path basePath = Path.parse(String.format("answers.partyA.%s", synonym));

		setValueAndUpdateMappings(getSynonymPath(basePath, endDateTypeSynonym),
				(type) -> {
					switch (type) {
					case "days":
						getOffset(basePath, numberOfDaysSynonym, after, dayTypeSynonym).ifPresent(customisableOffsetBuilder::setOffset);
						break;
					case "other":
						setValueAndUpdateMappings(getSynonymPath(basePath, customEndDateSynonym),
								(value) -> customisableOffsetBuilder.setCustomProvision(value));
						break;
					}
				});

		return customisableOffsetBuilder.hasData() ? Optional.of(customisableOffsetBuilder.build()) : Optional.empty();
	}

	@NotNull
	private Optional<Offset> getOffset(Path basePath, String numberOfDaysSynonym, boolean after, String dayTypeSynonym) {
		Offset.OffsetBuilder offsetBuilder = Offset.builder();

		setValueAndUpdateMappings(getSynonymPath(basePath, numberOfDaysSynonym),
				(value) -> {
					int numberOfDays = Integer.valueOf(value);
					offsetBuilder.setPeriodMultiplier(after ? numberOfDays : -numberOfDays);
					offsetBuilder.setPeriod(PeriodEnum.D);
				});

		setValueAndOptionallyUpdateMappings(getSynonymPath(basePath, dayTypeSynonym),
				(value) -> {
					Optional<DayTypeEnum> dayType = getEnumValue(synonymToDayTypeEnumMap, value, DayTypeEnum.class);
					dayType.ifPresent(offsetBuilder::setDayType);
					return dayType.isPresent();
				}, getMappings(), getPath());

		return offsetBuilder.hasData() ? Optional.of(offsetBuilder.build()) : Optional.empty();
	}
}