package cdm.legaldocumentation.csa.processor;

import cdm.base.datetime.CustomisableOffset;
import cdm.base.datetime.DayTypeEnum;
import cdm.base.datetime.Offset;
import cdm.base.datetime.PeriodEnum;
import cdm.legaldocumentation.csa.CustodianEventEndDate;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.MappingProcessor;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;

import java.util.List;
import java.util.Optional;

import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.getNonNullMappedValue;
import static com.regnosys.rosetta.common.translation.MappingProcessorUtils.setValueAndOptionallyUpdateMappings;
import static org.isda.cdm.processor.CreateiQMappingProcessorUtils.removeHtml;

/**
 * "Days after Custodian Event" ( "days_after_custodian_event" / "days_after_collateral_manager_event" / "days_after_euroclear_event")
 * when "days_after_custodian_event" = "days"
 * No. of days: "after_days" (number)
 * Type of day: "after_days_type":
 * "Days" ("days_after_days"),
 * "Calendar days" ("calendar_days_after_days"),
 * "other" ("specify_after_days")
 * when "days_after_custodian_event" = "other"
 * Specify Text: "after_specify"
 * <p>
 * "Release Date"
 * when "release_date" = "days"
 * No. of days: "release_days" (number)
 * Type of day: "release_days_type"
 * "Days" ("day_release_days"),
 * "Calendar days" ("calendar_day_release_days"),
 * "other" ("specify_release_days")
 * when "release_date" = "other"
 * Specify Text: "release_specify"
 * <p>
 * "Release Date (I) (CMSA termination)"
 * when "release_date_i" = "days"
 * No. of days: "release_i_days" (number)
 * Type of day: "release_date_i_type"
 * "Days" ("day_release_date_i"),
 * "Calendar days" ("calendar_day_release_date_i"),
 * "other" ("specify_release_date_i")
 * when "release_date_i" = "other"
 * Specify Text: "release_i_specify"
 * <p>
 * "Date of Timely Statement"
 * when "date_of_timely_statement" = "days"
 * <p>
 * No. of days prior to Release Date: "days_prior_to_release_date" (number)
 * Type of day: "days_prior_to_release_date_type"
 * "Days" ("days_days_prior_to_release_date"),
 * "Calendar days" ("calendar_days_days_prior_to_release_date"),
 * "other" ("specify_days_prior_to_release_date")
 * <p>
 * No. of days prior to Release Date: "timely_days" (number)
 * Type of day: "timely_days_type"
 * "Days" ("days_timely_days"),
 * "Calendar days" ("calendar_days_timely_days"),
 * "other" ("specify_timely_days")
 * <p>
 * No. of days after timely statement is effective: "days_after_timely_statement" (number)
 * Type of day: "days_after_timely_statement_type":
 * "Day" ("days_days_after_timely_statement"),
 * "Local Business Day" ("local_business_days_days_after_timely_statement"),
 * "other" ("specify_days_after_timely_statement")
 * <p>
 * when "date_of_timely_statement" = "other"
 * Specify Text: "timely_specify"
 */
@SuppressWarnings("unused")
public class CustodianEventEndDateMappingProcessor extends MappingProcessor {

	public CustodianEventEndDateMappingProcessor(RosettaPath modelPath, List<Path> synonymPaths, MappingContext mappingContext) {
		super(modelPath, synonymPaths, mappingContext);
	}

	@Override
	public void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		CustodianEventEndDate.CustodianEventEndDateBuilder endDateBuilder = (CustodianEventEndDate.CustodianEventEndDateBuilder) builder;
		// DaysAfterCustodianEvent
		getCustomisableOffset(synonymPath, "days_after_custodian_event",
				"after_days",
				true,
				"after_days_type",
				"after_specify")
				.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
		getCustomisableOffset(synonymPath, "days_after_collateral_manager_event",
				"after_days",
				true,
				"after_days_type",
				"after_specify")
				.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
		getCustomisableOffset(synonymPath, "days_after_euroclear_event",
				"after_days",
				true,
				"after_days_type",
				"after_specify")
				.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
		getCustomisableOffset(synonymPath, "days_after_clearstream_event",
				"after_days",
				true,
				"after_days_type",
				"after_specify")
				.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
		getCustomisableOffset(synonymPath, "days_after_clearstream_event",
				"after_days",
				true,
				"after_days_type",
				"specify_after_days")
				.ifPresent(endDateBuilder::setDaysAfterCustodianEvent);
		// ReleaseDate
		getCustomisableOffset(synonymPath, "release_date",
				"release_days",
				true,
				"release_days_type",
				"release_specify")
				.ifPresent(endDateBuilder::setReleaseDate);
		getCustomisableOffset(synonymPath, "release_date_i",
				"release_i_days",
				true,
				"release_date_i_type",
				"release_i_specify")
				.ifPresent(endDateBuilder::setReleaseDate);
		// Safekeeping Period Expiry
		getCustomisableOffset(synonymPath, "release_date_ii",
				"release_date_ii_days",
				true,
				"release_date_ii_type",
				"release_date_ii_specify")
				.ifPresent(endDateBuilder::setSafekeepingPeriodExpiry);
		getCustomisableOffset(synonymPath, "release_date_ii",
				"release_ii_days",
				true,
				"release_date_ii_type",
				"specify_release_date_ii")
				.ifPresent(endDateBuilder::setSafekeepingPeriodExpiry);
		// DateOfTimelyStatement
		getCustomisableOffset(synonymPath, "date_of_timely_statement",
				"days_prior_to_release_date",
				false,
				"days_prior_to_release_date_type",
				"timely_specify")
				.ifPresent(endDateBuilder::setDateOfTimelyStatement);
		getCustomisableOffset(synonymPath, "date_of_timely_statement",
				"timely_days",
				false,
				"timely_days_type",
				"timely_specify")
				.ifPresent(endDateBuilder::setDateOfTimelyStatement);
		getCustomisableOffset(synonymPath, "date_of_timely_statement",
				"days_after_timely_statement",
				true,
				"days_after_timely_statement_type",
				"timely_specify")
				.ifPresent(endDateBuilder::setDateOfTimelyStatement);
	}

	private Optional<CustomisableOffset> getCustomisableOffset(Path synonymPath,
			String endDateTypeSynonym,
			String numberOfDaysSynonym,
			boolean after,
			String dayTypeSynonym,
			String customEndDateSynonym) {

		CustomisableOffset.CustomisableOffsetBuilder customisableOffsetBuilder = CustomisableOffset.builder();

		setValueAndUpdateMappings(synonymPath.addElement(endDateTypeSynonym),
				(type) -> {
					switch (type) {
					case "days":
						getOffset(synonymPath, numberOfDaysSynonym, after, dayTypeSynonym, customEndDateSynonym, customisableOffsetBuilder);
						break;
					case "other":
						setValueAndUpdateMappings(synonymPath.addElement(customEndDateSynonym),
								value -> customisableOffsetBuilder.setCustomProvision(removeHtml(value)));
						break;
					}
				});

		return customisableOffsetBuilder.hasData() ? Optional.of(customisableOffsetBuilder.build()) : Optional.empty();
	}

	private void getOffset(Path basePath, String numberOfDaysSynonym, boolean after, String dayTypeSynonym,  String customEndDateSynonym,
						   CustomisableOffset.CustomisableOffsetBuilder customisableOffsetBuilder) {
		Offset.OffsetBuilder offsetBuilder = Offset.builder();

		Path numberOfDaysPath = basePath.addElement(numberOfDaysSynonym);
		Optional<String> numberOfDaysValue = getNonNullMappedValue(numberOfDaysPath, getMappings());
		if (!numberOfDaysValue.isPresent()) {
			return;
		}

		setValueAndUpdateMappings(numberOfDaysPath,
				(value) -> {
					int numberOfDays = Integer.parseInt(value);
					offsetBuilder.setPeriodMultiplier(after ? numberOfDays : -numberOfDays);
					offsetBuilder.setPeriod(PeriodEnum.D);
				});

		setValueAndOptionallyUpdateMappings(basePath.addElement(dayTypeSynonym),
				(value) -> {
					if ("other".equals(value)) {
						setValueAndUpdateMappings(basePath.addElement(customEndDateSynonym),
								customEndDate -> customisableOffsetBuilder.setCustomProvision(removeHtml(customEndDate)));
						return true;
					} else {
						Optional<DayTypeEnum> dayType = getSynonymToEnumMap().getEnumValueOptional(DayTypeEnum.class, value);
						dayType.ifPresent(offsetBuilder::setDayType);
						return dayType.isPresent();
					}

				}, getMappings(), getModelPath());


		if (offsetBuilder.hasData()) {
			customisableOffsetBuilder.setOffset(offsetBuilder);
		}
	}
}