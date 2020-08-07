package com.mobicule.android.msales.mgl.meterreading.view;

import com.mobicule.android.component.logging.MobiculeLogger;

public class MeterReaderMsg
{
	private final String TAG = "MeterReaderMsg";

	private final String other = "Other(Write)";

	private String[] msgList_0_PremisesLocked = { "Select", "Reason unknown", "Customer rarely visits the premise",
			"Customer available on weekends only", "Investor's flat (non occupied)",
			"Customer gone out of station/country for few days", "Customer living out of station/country",
			"Customer normally available but gone outside today",
			"This BP has abandoned for redevelopment.Few others are living"};//other

	private String[] msgList_1_TempDisconnection = { "Select", "Using LPG cylinder,Meter read",
			"Not using LPG cylinder,Meter read"};

	private String[] msgList_2_CustShifted = { "Select", "Flat Sold, New owner refused", other };

	private String[] msgList_3_CustNotStaying = { "Select", "NA", other };

	/*private String[] msgList_4_MeterDisconnected = { "Select", "No meter,customer doesn't know about that",
			"No meter.As per BP,meter was removed by MGL representative",other};
*/
	private String[] msgList_4_MeterDisconnected = { "Select", "No meter,customer doesn't know about that",
			"No meter.As per BP,meter was removed by MGL representative","Removed meter available with customer but unable to show for reading",
			"Flats combined.This meter removed and not available"};

	/*private String[] msgList_5_BuildingDemolished = { "Select", "Demolition in progress", "Confirmed with Security",
			"Confirmed with Secretary/BP by calling" };//other
*/
	private String[] msgList_5_BuildingDemolished = { "Select", "Demolition in progress", "Building abandoned Confirmed with Security",
	"Building abandoned Confirmed with Secretary/BP by calling","Building abandoned. Nobody available for confirmation","This BP has abandoned for redevelopment.Few others are living" };//other
	
	/*private String[] msgList_6_BuildingUnderConstruction = { "Select", "Building abandoned Confirmed with Security",
			"Building abandoned Confirmed with Secretary/BP by calling",
			"Building abandoned.Nobody available for confirmation",
			"This BP has abandoned for redevelopment.Few others are living" };*/
	
	private String[] msgList_6_BuildingUnderConstruction = { "Select","Address correct,building under construction" };

	private String[] msgList_7_MeterAddressNotFound = { "Select", "Building & Wing correct but customer not found here",
			"Building/wing not available on given address",
			"Building redeveloped with new name. Presently no PNG connection",
			"Building redeveloped with new name and new connections"};//other
	
	private String[] msgList_8_CounterDefective = { "Select", "Tested.Checked Meter/counter defective,Replace the Meter"};//other

	private String[] msgList_10_HarmedByConsumer = { "Select", "Replace the Meter"};//other

	/*private String[] msgList_13_MeterInaccessible = {
			"Select",
			"Meter not read.Stool/ladder not provided by customer meter reading",
			"Meter read. Customer provided stool/ladder. Site situation prevented from capturing clear photo.Refer to random  meter reading",
			other };*/
	
	private String[] msgList_13_MeterInaccessible = {
			"Select",
			"Meter not read.Stool/ladder not provided by customer meter reading",
			"Meter read. Customer provided stool/ladder. Site situation prevented from capturing clear photo.Refer to random  meter reading",
			"Meter not read.Use of stool ladder not possible"};
/*
	private String[] msgList_14_MeterFittedAtAwkwardPlace = {
			"Select",
			"Meter could be reached but failed to read & capture clear photo",
			"Meter read but site situation prevented from capturing clear photo.Refer to random meter reading.Customer is interested in relocation",
			"Meter read but site situation prevented from capturing clear photo.Refer to random  meter reading.Customer is not intrested in relocation",
			other };*/
	
	private String[] msgList_14_MeterFittedAtAwkwardPlace = {
			"Select",
			"Meter could be reached but failed to read & capture clear photo",
			"Meter read but site situation prevented from capturing clear photo.Refer to random meter reading.Customer is interested in relocation",
			"Meter read but site situation prevented from capturing clear photo.Refer to random meter reading.Customer is not interested in relocation"};

	/*private String[] msgList_15_RoomUnderRepair = { "Select",
			"No meter in premise, labourers don't know about the meter",
			"Meter removed by MGL and not found in premise", "Meter removed by other and not found in premise",other };*/
	
	
	private String[] msgList_15_RoomUnderRepair = { "Select",
			"No meter in premise, labourers don't know about the meter",
			"Meter removed by MGL and not found in premise", "Meter removed by other and not found in premise",
			"Removed meter available with the customer but unable to show for reading"};

	private String[] msgList_16_MeterGlassSmoky = { "Select",
			"Attempt made to clean the meter but still not readable.Replace the meter"};//other

	/*private String[] msgList_17_CustRefusedMR = { "Select", "Servant refused since customer not present in Flat",
			"Servant refused since Kids present in Flat", "MGL should give letter to each society Member",
			"Not right time to visit", "Senior citizen alone and refused entry in premises",
			"Reading normally sent via other modes", "Meter reader visited recently", other };*/
	
	
	private String[] msgList_17_CustRefusedMR = { "Select", "Servant refused since customer not present in Flat",
			"Servant refused since kids present in Flat", "MGL should give letter/SMS",
			"Not right time to visit", "Senior citizen alone and refused entry in premises",
			"Reading normally sent via other modes", "Meter reader visited recently",
			"No meter/meter lost","Repair/renovation done inside","PNG connection not taken","PNG permanently disconnected",
			"Temporary disconnection by MGL","Not using PNG","Informed MGL for minimum charge bill","MGL has disconnected from outside",
			"Compliant given to MGL but there is no action","ID card details insufficient"};
	
	

	/*private String[] msgList_52_NotUsingGas = { "Select", "Using LPG cylinder",
			"Not using LPG cylinder,checked Meter running ok", other };*/
	public static  String[] msgList_52_NotUsingGas = { "Select","Customer using gas in other premise.","Customer rarely stays in this premise",
			"Nobody living here.Investor's flat or customer living in other  city/country","Customer returned after long time and gas usage not restarted",
			"TD or Renovation earlier.Now yet to restart using PNG","Customer using electric appliance for cooking","Customer uses gas very rarely for tea-coffee etc",
			"Customer eats outside or gets tiffin"};

/*	private String[] msgList_55_PermissionRefusedBySecurity = { "Select", "Meter reader visited this Month only",
			"Not right time to visit", "Refused due to security concern", "Privacy reason",
			"Reading normally sent via other modes" };//other
*/
	private String[] msgList_55_PermissionRefusedBySecurity = { "Select", "Meter reader/MGL representative visited this Month only",
			"Not right time to visit", "Refused due to security concern", "Privacy reason",
			"Reading normally sent via other modes", "MGL should give letter/SMS","NO PNG Connection"
			,"No BP(s) by the name(s) shown","ID card details insufficient"};
	
	
	
	private String[] msgList_58_MeterDamaged = { "Select", "Can't read. Replace the Meter"};//other

	/*private String[] msgList_66_PermissionRefusedBySecretary = { "Select", "Meter reader visited this Month only",
			"Not right time to visit", "Refused due to security concern",
			"MGL should give letter to each society Member", "Reading normally sent via other modes", other };*/
	
	private String[] msgList_66_PermissionRefusedBySecretary = { "Select", "Meter reader/MGL representative visited this month only",
			"Not right time to visit", "Refused due to security concern","MGL should give letter/SMS",
			"Reading normally sent via other modes", "NO PNG Connection"
			,"No BP(s) by the name(s) shown","ID card details insufficient"};

	private String[] msgList_99_MeterNormal = { "Select", "Meter number and BP details matched with MGL record",
			"Meter number different from MGL record, BP details matching at given address",
			"Bp details matching at given address but meter Sr.no. not visible for confirmation", "Meter no. matching at given address but BP premise at other location", "BP premise at other location and meter Sr.no. not visible for confirmation"};//other

	private String[] zeroConsumption = { " Counter Defective    8", " Not Using Gas    52" };

/*	private String[] msgList_68_NoPNG = { "Select", "No PNG connection exists in entire building",
			"No PNG connection exists for this BP. Other BPs in the building have PNG connection",
			"Meter not available since present person occupied the premise"};//other
*/
	
	private String[] msgList_68_NoPNG = { "Select", "No PNG connection exists in entire building",
			"PNG connection not taken by this BP.Other BP's in the house have PNG connection",
			"Meter not available since present person occupied the premise","PNG permanently disconnected",
			"Flats combined and this connection removed"};
	/*,
			"Customer uses gas only rarely and only for tea coffee etc","Customer eats outside or gets tiffin",
			"Not using LPG Cylinder.New PNG connection and usage not started"};*/ // changed
	
	private String[] msgList_9_PermissionRefusedByCustomer = { "Select",
			"Servant refused since customer not present in Flat", "Servant refused since kids present in Flat",
			"MGL should give letter to each society Member", "Not right time to visit",
			"Senior citizen alone and refused entry in premises", "Reading normally sent via other modes",
			"Meter reader visited recently"};//other
	private String[] msgList_63_UsingForCommercialPurpose={"Select","Using PNG for office/pantry","Using PNG for clinic/shop","Using PNG for tiffin service"};
	
	private String[] msgList_20_LetterToConsumer = { "Select","Customer requested meter reader’s visit once per year"};//other
	
	private String[] msgList_22_NoPNGUsed = {"No PNG hose - Burner connection"};
	
/*	public static String[] msgListForNotUsingGas = {"No PNG hose - burner connection"};*/
	public static String[] msgListForNotUsingGas = {"Select","Using LPG cylinder,Meter Read","Not using LPG cylinder.No PNG hose-stove connection", "Not using LPG cylinder. PNG Stove/burner not usable",
		"Not using LPG cylinder.PNG Stove/Burner inaccessible","Not using LPG cylinder.Copper/GI connection removed","Not using LPG cylinder.New PNG connection and usage not started",
		"Not using LPG cylinder.PNG disconnected from outside"};
	
	public static String[] msglist_69_MeterTestingNotPossible={"Select","Meter outside premise and door lock. Code 52, 18 selection not possible. Meter read.",
			"Customer refused for Meter testing. Code 52, 18 selection not possible. Meter read"};
	
	
	public static String[] msglist_105_implausibleYNY={"Select","Using gas here rarely now,started using gas in other premise","Customer shifted to other premises","Customer rarely staying in this premise",
			"Customer returned after long time and started gas re-use","TD/Renovation was being done.Re-started using PNG recently",
			"Customer uses gas only rarely and only for tea coffee etc","Recently stopped using PNG.Not using LPG or electrical appliance"
			,"Recently stopped using PNG.Now using LPG","Recently stopped using PNG,now using electrical appliance",
			"Customer started eating outside or getting tiffin","Using LPG earlier,started using PNG recently",
			"Number of family members reduced","Meter replaced recently","Outside disconnection recently done","Last actual reading obtained few days ago"};

	public static String[] msglist_106_implausibleYYY={"Select","Joint family with common kitchen,not using gas geyser","Joint family with common kitchen,also using gas geyser"
			,"Number of family members increased","Normal size family,also using gas geyser","Actual meter reading after long time",
			"Meter replaced after previous actual meter reading","Earlier using gas rarely,now using regularly "};
	public String[] getMessageList(String strCode)
	{
		try
		{
			int code = Integer.parseInt(strCode);

			switch (code)
			{
				case 0:
					return msgList_0_PremisesLocked;
				case 1:
					return msgList_1_TempDisconnection;
				case 2:
					return msgList_2_CustShifted;
				case 3:
					return msgList_3_CustNotStaying;
				case 4:
					return msgList_4_MeterDisconnected;
				case 5:
					return msgList_5_BuildingDemolished;
				case 6:
					return msgList_6_BuildingUnderConstruction;
				case 7:
					return msgList_7_MeterAddressNotFound;
				case 8:
					return msgList_8_CounterDefective;
				case 9:
					return msgList_9_PermissionRefusedByCustomer;
				case 10:
					return msgList_10_HarmedByConsumer;
				case 13:
					return msgList_13_MeterInaccessible;
				case 14:
					return msgList_14_MeterFittedAtAwkwardPlace;
				case 15:
					return msgList_15_RoomUnderRepair;
				case 16:
					return msgList_16_MeterGlassSmoky;
				case 17:
					return msgList_17_CustRefusedMR;
				case 18:
					return msgList_8_CounterDefective;
				case 20:
					return msgList_20_LetterToConsumer;
				case 52:
					return msgList_52_NotUsingGas;
				case 55:
					return msgList_55_PermissionRefusedBySecurity;
				case 58:
					return msgList_58_MeterDamaged;
				case 66:
					return msgList_66_PermissionRefusedBySecretary;
				case 99:
					return msgList_99_MeterNormal;
				case 68:
					return msgList_68_NoPNG;
				case 60: // 52 + 8 = 60    zeroConsumtion
					return zeroConsumption;
				case 22: 
					return msgList_22_NoPNGUsed;
				case 69:
					return msglist_69_MeterTestingNotPossible;
	
				case 63:
					return  msgList_63_UsingForCommercialPurpose;
				case 105:
					return msglist_105_implausibleYNY;
				case 106: 
					return msglist_106_implausibleYYY;
				
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

}
