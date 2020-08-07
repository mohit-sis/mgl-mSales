package com.mobicule.android.msales.mgl.meterreading.view;

import com.mahanagar.R;
import com.mobicule.android.component.logging.MobiculeLogger;
import android.content.Context;

public class MeterReadingNotesListsConstants
{

	//Static Lists for Group and Code

	private final static String TAG = "MeterReadingNotesListConstants";

	public static String[] groupList = { "Meter/Address Not Found", "Building Demolished",
			"Building Under Construction", "Permission Not Obtained", "Premises Locked",
			"Reached PNG Installation But Reading/Photo Not Possible", "Meter Reading Possible", "Letter To Customer" };

	public static String[] groupCodeList = { "1", "2", "3", "4", "5", "6", "7", "8" };

	/*	public static String[] groupList = { "Meter Reading Possible",
		"Reached PNG Installation But Reading/Photo Not Possible", "Premisses Locked", "Permission Not Obtained",
		"Building Under Construction", "Building Demolished", "Meter/Address Not Found", "Letter To Customer" };*/

	//public static String[] meterNormalReasonsList = { "Meter Normal" };

	//public static String[] meterNormalCodeList = { "99" };

	public static String[] meterPossibleReasonList = { "Meter Normal", "Using for commercial purpose" };//changed

	public static String[] meterPossibleCodeList = { "99", "63" };//changed

	public static String[] meternormalcode = { "99" };

	public static String[] meternormalreason = { "Meter Normal" };

	public static String[] meterNormalReasonsList = { "Meter Normal", "Temporary Disconnection",
			"Meter/Counter Defective", "Not Using Gas", "Meter Testing Not Possible" };//changed

	public static String[] yesYesReasonList = { "Not Using Gas" };

	public static String[] yesYesCodeList = { "52" };

	public static String[] noCodeList = { "1", "52", "69" };

	public static String[] noReasonList = { "Temporary Disconnection", "Not Using Gas", "Meter Testing Not Possible" };//changed

	public static String[] yesNoReasonList = { "Meter/Counter Defective" };

	public static String[] yesNoCodeList = { "18" };

	public static String[] meterNormalCodeList = { "99", "1", "18", "52", "69" };//changed

	public static String[] readingNotPossibleReasonsList = { "No PNG Connection",
			"Meter Fitted At Awkward Place/Level", "Meter Inaccessible", "Meter Glass Smoky/Opaque",
			"Meter Rusty/Damaged", "Harmed By Consumer", "Meter Disconnected", "Room Under Repair/Renovation" };

	public static String[] readingNotPossibleCodeList = { "68", "14", "13", "16", "58", "10", "4", "15" };

	public static String[] premisesLockedReasonList = { "Premises Locked" };

	public static String[] premisesLockedCodeList = { "0" };

	public static String[] permissionNotObtainedReasonList = { "Permission Refused By Security",
			"Permission Refused By Secretary", "Permission Refused By Customer" };

	public static String[] permissionNotObtainedCodeList = { "55", "66", "17" };

	public static String[] buildingUnderConstructionReasonList = { "Building Under Construction" };

	public static String[] buildingUnderConstructionCodeList = { "6" };

	public static String[] buildingDemolishedReasonList = { "Building Demolished" };

	public static String[] buildingDemolishedCodeList = { "5" };

	public static String[] meterAddnotFoundReasonList = { "Meter/Address Not Found" };

	public static String[] meterAddNotFoundCodeList = { "7" };

	public static String[] letterToCustomerReasonList = { "Letter given for visit once per  year " };

	public static String[] letterToCustomerCodeList = { "20" };

	//All Reasons List and Codes List For Mapping

	public static String[] allReasonsList = { "Select", "Meter Normal", "Premises Locked", "Temporary Disconnection",
			"Customer Shifted", "Customer Not Staying", "Meter Disconnected", "Building Demolished",
			"Building Under Construction", "Meter/Address Not Found", "Meter/Counter Defective", "Harmed By Consumer",
			"Meter Inaccessible", "Meter Fitted At Awkward Place/Level", "Room Under Repair/Renovation",
			"Meter Glass Smoky/Opaque", "Permission Refused By Customer", "Not Using Gas",
			"Permission Refused By Security", "Meter Rusty/Damaged", "Permission Refused By Secretary",
			"No PNG Connection", "Failed To Complete Work Given", "Meter Testing Not Possible",
			"Using for commercial purpose" };

	public static String[] allCodesList = { "", "99", "0", "1", "2", "3", "4", "5", "6", "7", "18", "10", "13", "14",
			"15", "16", "17", "52", "55", "58", "66", "68", "49", "69", "63" };

	// To get texts in hindi

	public static String[] getGroupInHindi(Context context)
	{
		/*String[] hindiGroup = { context.getString(R.string.meter_reading_possible_hindi),
				context.getString(R.string.reached_png_installation_but_reading_or_photo_not_possible_hindi),
				context.getString(R.string.premises_locked_hindi),
				context.getString(R.string.permission_not_obtained_hindi),
				context.getString(R.string.building_under_construction_hindi),
				context.getString(R.string.building_demolished_hindi),
				context.getString(R.string.address_not_found_hindi),
				context.getString(R.string.letter_to_customer_hindi) };*/

		String[] hindiGroup = { context.getString(R.string.address_not_found_hindi),
				context.getString(R.string.building_demolished_hindi),
				context.getString(R.string.building_under_construction_hindi),
				context.getString(R.string.permission_not_obtained_hindi),
				context.getString(R.string.premises_locked_hindi),
				context.getString(R.string.reached_png_installation_but_reading_or_photo_not_possible_hindi),
				context.getString(R.string.meter_reading_possible_hindi),
				context.getString(R.string.letter_to_customer_hindi) };

		return hindiGroup;
	}

	//Code-------------------------------------------------------------

	public static String[] getMeterNormalCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.meter_normal_hindi),
				context.getString(R.string.temporary_disconnection_hindi),
				context.getString(R.string.meter_or_counter_defective_hindi),
				context.getString(R.string.not_using_gas_hindi), };

		return hindiCode;
	}

	public static String[] getMeterPossibleCodeInHindi(Context context)
	{
		String[] hindiCode = { context.getString(R.string.meter_normal_hindi),
				context.getString(R.string.using_for_commercial_purpose) };
		return hindiCode;
	}

	public static String[] getYesYesCodeInHindi(Context context)
	{
		String[] hindiCode = { context.getString(R.string.not_using_gas_hindi) };
		return hindiCode;
	}

	public static String[] getNoCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.temporary_disconnection_hindi),
				context.getString(R.string.not_using_gas_hindi), context.getString(R.string.Meter_testing_not_possible) };
		return hindiCode;

	}

	public static String[] getYesNoInHindi(Context context)
	{
		String[] hindiCode = { context.getString(R.string.meter_or_counter_defective_hindi) };
		return hindiCode;
	}

	public static String[] getReadingNotPossibleCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.no_png_connection_hindi),
				context.getString(R.string.meter_fitted_at_awkward_place_or_level_hindi),
				context.getString(R.string.meter_inaccessible_hindi),
				context.getString(R.string.meter_glass_smoky_or_opaque_hindi),
				context.getString(R.string.meter_rusty_or_damaged_hindi),
				context.getString(R.string.harmed_by_consumer_hindi),
				context.getString(R.string.meter_disconnected_hindi),
				context.getString(R.string.room_under_repair_or_renovation_hindi) };

		return hindiCode;
	}

	public static String[] getPremisesLockedCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.premises_locked_hindi) };

		return hindiCode;
	}

	public static String[] getPermissionNotObtainedCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.permission_refused_by_security_hindi),
				context.getString(R.string.permission_refused_by_secretary_hindi),
				context.getString(R.string.permission_refused_by_customer_hindi) };

		return hindiCode;
	}

	public static String[] getbuildingUnderConstructionCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.building_under_construction_hindi) };

		return hindiCode;
	}

	public static String[] getBuildingDemolishedCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.building_demolished_hindi) };

		return hindiCode;
	}

	public static String[] getAdderessNotFoundCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.address_not_found_hindi) };

		return hindiCode;
	}

	public static String[] getMeterNormalcodeImplausible(Context context)
	{

		String[] hindiCode = { context.getString(R.string.meter_normal_hindi) };

		return hindiCode;
	}

	public static String[] getLetterGivenCodeInHindi(Context context)
	{

		String[] hindiCode = { context.getString(R.string.letter_given_for_visit_once_per_year_hindi) };

		return hindiCode;
	}

	//Messages------------------------------------------------

	public static String[] getAdderessNotFoundMessageInHindi(Context context)
	{

		String[] hindiCode = { "",
				context.getString(R.string.building_and_wing_correct_but_customer_not_found_here_hindi),
				context.getString(R.string.building_or_wing_not_available_on_given_address_hindi),
				context.getString(R.string.building_redeveloped_with_new_name_presently_no_png_connection_hindi),
				context.getString(R.string.building_redeveloped_with_new_name_and_new_connections_hindi),
				context.getString(R.string.other_hindi) };

		return hindiCode;
	}

	public static String[] getBuildingDemolishedMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.demolition_in_progress_hindi),
				context.getString(R.string.confirmed_with_security_hindi),
				context.getString(R.string.confirmed_with_secretary_or_bp_by_calling_hindi),
				context.getString(R.string.building_abandoned_nobody_available_for_confirmation_hindi),
				context.getString(R.string.this_bp_has_abandoned_for_redevelopment_few_others_are_living_hindi) };

		return hindiCode;
	}

	public static String[] getBuildingUnderConstructionMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.address_correct_building_under_construction) };

		return hindiCode;
	}

	public static String[] getRefusedBySecurityMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.meter_reader_visited_this_month_only_hindi),
				context.getString(R.string.not_right_time_to_visit_hindi),
				context.getString(R.string.refused_due_to_security_concern_hindi),
				context.getString(R.string.privacy_reason_hindi),
				context.getString(R.string.reading_normally_sent_via_other_modes_hindi),
				context.getString(R.string.mgl_should_send_letter_sms), context.getString(R.string.no_png_connection),
				context.getString(R.string.no_bp_by_name_shown),
				context.getString(R.string.id_card_details_insufficient) };

		return hindiCode;
	}

	public static String[] getRefusedBySecretaryMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.meter_reader_visited_this_month_only_hindi),
				context.getString(R.string.not_right_time_to_visit_hindi),
				context.getString(R.string.refused_due_to_security_concern_hindi),
				context.getString(R.string.reading_normally_sent_via_other_modes_hindi),
				context.getString(R.string.mgl_should_give_letter_before_reading),
				context.getString(R.string.no_png_connection), context.getString(R.string.no_bp_by_name_shown),
				context.getString(R.string.id_card_details_insufficient) };

		return hindiCode;
	}

	public static String[] getRefusedByCustomerMessageInHindi(Context context)
	{

		String[] hindiCode = { "",
				context.getString(R.string.servant_refused_since_customer_not_present_in_flat_hindi),
				context.getString(R.string.servant_refused_since_kids_present_in_flat_hindi),
				context.getString(R.string.mgl_should_give_letter_before_reading),
				context.getString(R.string.not_right_time_to_visit_hindi),
				context.getString(R.string.senior_citizen_alone_and_refused_entry_in_premises_hindi),
				context.getString(R.string.reading_normally_sent_via_other_modes_hindi),
				context.getString(R.string.meter_reader_visited_recently_hindi),
				context.getString(R.string.no_meter_or_meter_lost),
				context.getString(R.string.repair_renovation_done_inside),
				context.getString(R.string.png_connection_not_taken),
				context.getString(R.string.png_permanently_disconnected),
				context.getString(R.string.temporary_disconnection_by_mgl), context.getString(R.string.not_using_png),
				context.getString(R.string.informed_mgl_minimum_bill),
				context.getString(R.string.mgl_has_disconnected_from_outside),
				context.getString(R.string.complaint_given_mgl_no_action),
				context.getString(R.string.id_card_details_insufficient), };

		return hindiCode;
	}

	public static String[] getPremisesLockedMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.reason_unknown_hindi),
				context.getString(R.string.customer_rarely_visits_the_premise_hindi),
				context.getString(R.string.customer_available_on_weekends_only_hindi),
				context.getString(R.string.investors_flat_non_occupied_hindi),
				context.getString(R.string.customer_gone_out_of_station_or_country_for_few_days_hindi),
				context.getString(R.string.customer_living_out_of_station_or_country_hindi),
				context.getString(R.string.customer_normally_available_but_gone_outside_today_hindi),
				context.getString(R.string.this_bp_has_abandoned_for_redevelopment_few_others_are_living_hindi) };

		return hindiCode;
	}

	public static String[] getNoPNGConnectionMessageInHindi(Context context)
	{

		String[] hindiCode = {
				"",
				context.getString(R.string.no_png_connection_exists_in_entire_building_hindi),
				context.getString(R.string.no_png_connection_exists_for_this_bp_other_bps_in_the_building_have_png_connection_hindi),
				context.getString(R.string.meter_not_available_since_present_person_occupied_the_premise_hindi),
				context.getString(R.string.png_permanently_disconnected),
				context.getString(R.string.falt_combined_and_this_connection_removed) };

		return hindiCode;
	}

	public static String[] getMeterAtAwkwardPlaceMessageInHindi(Context context)
	{

		String[] hindiCode = {
				"",
				context.getString(R.string.meter_could_be_reached_but_failed_to_read_and_capture_clear_photo_hindi),
				context.getString(R.string.meter_read_but_site_situation_prevented_from_capturing_clear_photo_refer_to_random_meter_reading_customer_is_interested_in_relocation_hindi),
				context.getString(R.string.meter_read_but_site_situation_prevented_from_capturing_clear_photo_refer_to_random_meter_reading_customer_is_not_intrested_in_relocation_hindi) };

		return hindiCode;
	}

	public static String[] getMeterInaccessiblMessageInHindi(Context context)
	{

		String[] hindiCode = {
				"",
				context.getString(R.string.meter_not_read_stool_or_ladder_not_provided_by_customer_meter_reading_hindi),
				context.getString(R.string.meter_read_customer_provided_stool_or_ladder_site_situation_prevented_from_capturing_clear_photo_refer_to_random_meter_reading_hindi),
				context.getString(R.string.meter_not_read_use_of_customer_or_ladder_not_possible) };

		return hindiCode;
	}

	public static String[] getMeterGlassSmokeyOpaqueMessageInHindi(Context context)
	{

		String[] hindiCode = {
				"",
				context.getString(R.string.attempt_made_to_clean_the_meter_but_still_not_readable_replace_the_meter_hindi),
				context.getString(R.string.other_hindi) };

		return hindiCode;
	}

	public static String[] getMeterRustyDamagedMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.cant_read_replace_the_meter_hindi) };

		return hindiCode;
	}

	public static String[] getHarmedByConsumerMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.replace_the_meter_hindi) };

		return hindiCode;
	}

	public static String[] getOnMHindiText(Context context)//
	{

		String[] hindiCode = { "", context.getString(R.string.Using_LPG_MGL_gas_hose_not_available),
				context.getString(R.string.Not_using_LPG_MGL_gas_hose_also_not_available) };

		return hindiCode;
	}

	public static String[] getMeterDisconnectedMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.no_meter_customer_doesnt_know_about_that_hindi),
				context.getString(R.string.no_meter_as_per_bp_meter_was_removed_by_mgl_representative_hindi),
				context.getString(R.string.removed_meter_available_with_unable_to_show_reading),
				context.getString(R.string.falt_combined_and_this_meter_removed_and_not_available) };

		return hindiCode;
	}

	public static String[] getNotusingGasYY(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.Customer_using_gas_in_other_premise),
				context.getString(R.string.Customer_rarely_stays_in_this_premise),
				context.getString(R.string.Nobody_living_here_Investor_flat_or_customer_living_in_other_citycountry),
				context.getString(R.string.Customer_returned_after_long_time_and_gas_usage_not_restarted),
				context.getString(R.string.TD_or_renovation_earlier_Now_yet_to_restart_using_PNG),
				context.getString(R.string.Customer_using_electric_appliance_for_cooking),
				context.getString(R.string.Customer_uses_gas_very_rarely_for_tea_coffee_etc),
				context.getString(R.string.Customer_eats_outside_or_gets_tiffin),

		};

		return hindiCode;
	}

	public static String[] getRoomUnderRepairMessageInHindi(Context context)
	{

		String[] hindiCode = { "",
				context.getString(R.string.no_meter_in_premise_labourers_dont_know_about_the_meter_hindi),
				context.getString(R.string.meter_removed_by_mgl_and_not_found_in_premise_hindi),
				context.getString(R.string.meter_removed_by_other_and_not_found_in_premise_hindi) };

		return hindiCode;
	}

	public static String[] getMeterNormalMessageInHindi(Context context)
	{
		MobiculeLogger.verbose("getMeterNormalMessageInHindi called");

		String[] hindiCode = {
				"",
				context.getString(R.string.meter_number_and_bp_details_matched_with_mgl_record_hindi),
				context.getString(R.string.meter_number_different_from_mgl_record_bp_details_matching_at_given_address),
				context.getString(R.string.bp_details_matching_at_given_address_but_meter_sr_no_not_visible_for_confirmation_hindi),
				context.getString(R.string.meter_matching_given_address_but_bp_premise_at_other_location),
				context.getString(R.string.bp_premise_at_other_location_and_serial_no_not_visible_for_confirmation) };

		return hindiCode;
	}

	public static String[] getTemporaryDisconnectionMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.using_lpg_cylinder_meter_read_hindi),
				context.getString(R.string.not_using_lpg_cylinder_meter_read_hindi),
				context.getString(R.string.other_hindi) };

		return hindiCode;
	}

	public static String[] getMeterCounterDefectiveMessageInHindi(Context context)
	{

		String[] hindiCode = { "",
				context.getString(R.string.checked_meter_or_counter_defective_replace_the_meter_hindi),
				context.getString(R.string.other_hindi) };

		return hindiCode;
	}

	public static String[] getNotUsingGasMessageInHindi(Context context)
	{

		String[] hindiCode = { "",/* context.getString(R.string.using_lpg_cylinder_checked_meter_running_ok_hindi),
									context.getString(R.string.not_using_lpg_cylinder_checked_meter_running_ok_hindi),*/
				context.getString(R.string.using_lpg_cylinder_meter_read_hindi),
				context.getString(R.string.Not_using_LPG_cylinder_No_PNG_hose_stove_connection),
				context.getString(R.string.Not_using_LPG_cylinder_PNG_Stove_burner_not_usable),
				context.getString(R.string.Not_using_LPG_cylinder_PNG_Stove_burner_inaccessible),
				context.getString(R.string.Not_using_LPG_cylinder_Copper_GI_connection_removed),
				context.getString(R.string.Not_using_LPG_cylinder_New_PNG_connection_and_usage_not_started),
				context.getString(R.string.Not_using_LPG_cylinder_PNG_disconnected_from_outside),
				context.getString(R.string.other_hindi) };

		/*String[] hindiCode = { "", context.getString(R.string.no_PNG_hose_burner_connection),
								context.getString(R.string.other_hindi) };*/

		return hindiCode;
	}

	public static String[] getNoPNGHoseBurnerMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.no_PNG_hose_burner_connection),
				context.getString(R.string.other_hindi) };

		return hindiCode;

	}

	public static String[] getMeterTestingNotPossible(Context context)

	{
		String[] hindiCode = {
				"",
				context.getString(R.string.Meter_outside_premise_and_door_lock_Code_52_18_selection_not_possible_Meter_read),
				context.getString(R.string.Customer_refused_for_Meter_testing_Code_52_18_selection_not_possible_Meter_read),
				context.getString(R.string.other_hindi) };

		return hindiCode;

	}

	public static String[] getLetterToCustomerMessageInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.customer_requested_meter_readers_visit_once_per_year),
				context.getString(R.string.other_hindi) };

		return hindiCode;
	}

	public static String[] getUsedForCommercialPurposeInHindi(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.Using_PNG_for_office_pantry),
				context.getString(R.string.Using_PNG_for_clinic_shop),
				context.getString(R.string.Using_PNG_for_tiffin_service) };

		return hindiCode;
	}

	public static String[] getImplausibleHigh(Context context)
	{

		String[] hindiCode = { "", context.getString(R.string.Joint_family_with_common_kitchen_not_using_gas_geyser),
				context.getString(R.string.Joint_family_with_common_kitchen_also_using_gas_geyser),
				context.getString(R.string.Number_of_family_members_increased),
				context.getString(R.string.Normal_size_family_also_using_gas_geyser),
				context.getString(R.string.Actual_meter_reading_after_long_time),
				context.getString(R.string.Meter_replaced_after_previous_actual_reading),
				context.getString(R.string.Earlier_using_gas_rarely_now_using_regularly),
				context.getString(R.string.other_hindi) };
		return hindiCode;
	}

	public static String[] getImplausibleLow(Context context)
	{

		String[] hindiCode = { "",
				context.getString(R.string.Using_gas_here_rarely_now_started_using_gas_in_other_premise),
				context.getString(R.string.Customer_shifted_to_other_premise),
				context.getString(R.string.Customer_rarely_staying_in_this_premise),
				context.getString(R.string.Customer_returned_after_long_time_and_gas_usage_not_restarted),
				context.getString(R.string.TD_renovation_was_being_done_Re_started_using_PNG_recently),
				context.getString(R.string.Customer_uses_gas_very_rarely_and_only_for_tea_coffee_etc),
				context.getString(R.string.Recently_stopped_using_PNG_Not_using_LPG_or_electrical_appliance),
				context.getString(R.string.Recently_stopped_using_PNG_now_using_LPG),
				context.getString(R.string.Recently_stopped_using_PNG_now_using_electrical_appliance),
				context.getString(R.string.Customer_started_eating_outside_or_getting_tiffin),
				context.getString(R.string.Using_LPG_earlier_started_using_PNG_recently),
				context.getString(R.string.Number_of_family_members_reduced_members_increased),
				context.getString(R.string.Meter_replaced_recently),
				context.getString(R.string.Outside_disconnection_recently_done),
				context.getString(R.string.Last_actual_reading_obtained_few_days_ago),
				context.getString(R.string.other_hindi) };
		return hindiCode;
	}

	public static String[] getMessageInHindiList(Context context, String strCode)
	{
		MobiculeLogger.verbose("strCode : " + strCode);

		try
		{
			int code = Integer.parseInt(strCode);
			MobiculeLogger.verbose("code ::: " + code);

			switch (code)
			{
				case 0:
					return getPremisesLockedMessageInHindi(context);
				case 1:
					return getTemporaryDisconnectionMessageInHindi(context);
				case 2:
					return null;
				case 3:
					return null;
				case 4:
					return getMeterDisconnectedMessageInHindi(context);
				case 5:
					return getBuildingDemolishedMessageInHindi(context);
				case 6:
					return getBuildingUnderConstructionMessageInHindi(context);
				case 7:
					return getAdderessNotFoundMessageInHindi(context);
				case 8:
					return getMeterCounterDefectiveMessageInHindi(context);
				case 9:
					return getRefusedByCustomerMessageInHindi(context);
				case 10:
					return getHarmedByConsumerMessageInHindi(context);
				case 13:
					return getMeterInaccessiblMessageInHindi(context);
				case 14:
					return getMeterAtAwkwardPlaceMessageInHindi(context);
				case 15:
					return getRoomUnderRepairMessageInHindi(context);
				case 16:
					return getMeterGlassSmokeyOpaqueMessageInHindi(context);
				case 17:
					return getRefusedByCustomerMessageInHindi(context);
				case 18:
					return getMeterCounterDefectiveMessageInHindi(context);
				case 20:
					return getLetterToCustomerMessageInHindi(context);
				case 52:
					return getNotUsingGasMessageInHindi(context);
				case 55:
					return getRefusedBySecurityMessageInHindi(context);
				case 58:
					return getMeterRustyDamagedMessageInHindi(context);
				case 63:
					return getUsedForCommercialPurposeInHindi(context);
				case 66:
					return getRefusedBySecretaryMessageInHindi(context);
				case 99:
					return getMeterNormalMessageInHindi(context);
				case 68:
					return getNoPNGConnectionMessageInHindi(context);

				case 69:
					return getMeterTestingNotPossible(context);
				case 60: // 52 + 8 = 60    zeroConsumtion
					return null;
				case 71:
					return getImplausibleHigh(context);

				case 72:
					return getImplausibleLow(context);
				case 73:
					return getMeterNormalcodeImplausible(context);

				case 74:

					return getOnMHindiText(context);
				default:
					return null;

			}
		}
		catch (Exception e)
		{
			MobiculeLogger.verbose("getMessageList() - " + e.toString());
			return null;
		}

	}

	public static String[] getConditionsInHindiList(Context context, String strCondition)
	{

		if (strCondition.equals("All details verified. Actual consumption abnormal."))
		{
			String[] hindiCode = { context.getString(R.string.all_details_verified_actual_consumption_abnormal) };
			return hindiCode;
		}
		if (strCondition.equals("All details verified."))
		{
			String[] hindiCode = { context.getString(R.string.all_details_verified) };
			return hindiCode;
		}
		if (strCondition.equals("PNG not being used. Burner operated, meter checked, OK"))
		{
			String[] hindiCode = { context.getString(R.string.png_not_being_used_burner_operated_meter_checked_ok) };
			return hindiCode;
		}
		if (strCondition.equals("PNG not being used. Meter checking not possible."))
		{
			String[] hindiCode = { context.getString(R.string.png_not_being_used_meter_checking_not_possible) };
			return hindiCode;
		}
		if (strCondition.equals("Burner Operated, Meter not running, Replace Meter."))
		{
			String[] hindiCode = { context.getString(R.string.burner_operated_meter_not_running_replace_meter) };
			return hindiCode;
		}

		return null;
	}
}
