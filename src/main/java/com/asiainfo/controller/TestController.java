package com.asiainfo.controller;

import com.asiainfo.service.conf.RedisService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

	@Autowired
	private RedisService redisService;
	@GetMapping("/jwttest")
	public Object jwtTest(Authentication user) {
		return user;
	}

	@GetMapping("/testRedis")
	public void testRedis() {


		Map<String, String> map=new HashMap();






		map.put("0","TL_BPM_ERROR_LOG");
		map.put("1","TR_ROUTE_INFO");
		map.put("2","TI_F_USERTAG");
		map.put("3","TI_B_USER");
		map.put("4","TI_B_PCC_UPDATEUSERINFO");
		map.put("5","TI_B_PCC_DELETEUSERPOLICY");
		map.put("6","TI_B_PCC_ADDUSERPOLICY");
		map.put("7","TI_B_PCC_ADDUSERINFO");
		map.put("8","TI_B_OCC_INFO");
		map.put("9","TI_B_IOM");
		map.put("10","TI_BH_USER_SVC");
		map.put("11","TI_BH_USER_DISCNT");
		map.put("12","TI_BH_PCC_UPDATEUSERINFO");
		map.put("13","TI_BH_PCC_DELETEUSERPOLICY");
		map.put("14","TI_BH_PCC_ADDUSREINFO");
		map.put("15","TI_BH_PCC_ADDUSERPOLICY");
		map.put("16","TI_BH_PARTYCENTER_ABNORMALDATA");
		map.put("17","TF_SM_VIPCLUB_MEMBER");
		map.put("18","TF_R_TEMPOCCUPY");
		map.put("19","TF_R_SIMCARD_USE");
		map.put("20","TF_R_MPHONECODE_USE");
		map.put("21","TF_R_MPHONECODE_IDLE");
		map.put("22","TF_RES_TRADEFEE_PAYMODE");
		map.put("23","TF_F_USER_SVCSTATE");
		map.put("24","TF_F_USER_SVC_ITEM");
		map.put("25","TF_F_USER_SVC");
		map.put("26","TF_F_USER_SP_ITEM");
		map.put("27","TF_F_USER_SP");
		map.put("28","TF_F_USER_SCORE_DETAIL");
		map.put("29","TF_F_USER_SCORE");
		map.put("30","TF_F_USER_RES");
		map.put("31","TF_F_USER_PURCHASE_ITEM");
		map.put("32","TF_F_USER_PURCHASE");
		map.put("33","TF_F_USER_PSPT");
		map.put("34","TF_F_USER_PRODUCT_TYPE");
		map.put("35","TF_F_USER_PRODUCT");
		map.put("36","TF_F_USER_OTHER");
		map.put("37","TF_F_USER_MARKINFO");
		map.put("38","TF_F_USER_ITEM");
		map.put("39","TF_F_USER_INFOCHANGE");
		map.put("40","TF_F_USER_FOREGIFT");
		map.put("41","TF_F_USER_ELEMENT");
		map.put("42","TF_F_USER_DISCNT_ITEM");
		map.put("43","TF_F_USER_DISCNT");
		map.put("44","TF_F_USER_DEVELOP");
		map.put("45","TF_F_USER_DETAIL_ADDRESS");
		map.put("46","TF_F_USER_ASSURE");
		map.put("47","TF_F_USER");
		map.put("48","TF_F_REMOTE_CARD");
		map.put("49","TF_F_RELATION_UU_ITEM");
		map.put("50","TF_F_RELATION_UU");
		map.put("51","TF_F_PSPTINFO");
		map.put("52","TF_F_POSTINFO");
		map.put("53","TF_F_CUST_VIP");
		map.put("54","TF_F_CUST_PERSON_EXTEND");
		map.put("55","TF_F_CUST_PERSON");
		map.put("56","TF_F_CUSTOMER");
		map.put("57","TF_F_CUST_GROUPMEMBER");
		map.put("58","TF_F_CUST_GROUP_EXTEND");
		map.put("59","TF_F_CUST_GROUP");
		map.put("60","TF_F_CUST_CONTACT");
		map.put("61","TF_F_CLUBBER_SERV");
		map.put("62","TF_F_ACCT_ITEM");
		map.put("63","TF_F_ACCT_DISCNT_ITEM");
		map.put("64","TF_F_ACCT_DISCNT");
		map.put("65","TF_F_ACCOUNT_CONSIGN");
		map.put("66","TF_F_ACCOUNT");
		map.put("67","TF_CHL_DATBOOK");
		map.put("68","TF_CHL_ACCTACTION_LOG");
		map.put("69","TF_CHL_ACCT");
		map.put("70","TF_B_VALUECARD_SALE_DETAIL");
		map.put("71","TF_B_TRADE_USER_BAK");
		map.put("72","TF_B_TRADE_USER");
		map.put("73","TF_B_TRADE_SVCSTATE");
		map.put("74","TF_B_TRADE_SVC");
		map.put("75","TF_B_TRADE_SUB_ITEM");
		map.put("76","TF_B_TRADE_SP");
		map.put("77","TF_B_TRADE_SCORE_TRANS");
		map.put("78","TF_B_TRADE_SCORESUB_TRANS");
		map.put("79","TF_B_TRADE_SCORESUB");
		map.put("80","TF_B_TRADE_SCORE_CHANGE_TRANS");
		map.put("81","TF_B_TRADE_SCORE_CHANGE");
		map.put("82","TF_B_TRADE_SCORE");
		map.put("83","TF_B_TRADE_RES");
		map.put("84","TF_B_TRADE_RELATION");
		map.put("85","TF_B_TRADE_REL");
		map.put("86","TF_B_TRADE_PURCHASE");
		map.put("87","TF_B_TRADE_PSPT");
		map.put("88","TF_B_TRADE_PRODUCT_TYPE");
		map.put("89","TF_B_TRADE_PRODUCT");
		map.put("90","TF_B_TRADE_PERSON");
		map.put("91","TF_B_TRADE_PAYRELATION");
		map.put("92","TF_B_TRADE_OTHER");
		map.put("93","TF_B_TRADE_ITEM");
		map.put("94","TF_B_TRADEFEE_SUB");
		map.put("95","TF_B_TRADEFEE_PAYMONEY");
		map.put("96","TF_B_TRADEFEE_E_COUPON");
		map.put("97","TF_B_TRADEFEE_DEVICE");
		map.put("98","TF_B_TRADEFEE_CHECK");
		map.put("99","TF_B_TRADE_ELEMENT");
		map.put("100","TF_B_TRADE_DISCNT");
		map.put("101","TF_B_TRADE_DEVELOP");
		map.put("102","TF_B_TRADE_CUST_PERSON");
		map.put("103","TF_B_TRADE_CUSTOMER");
		map.put("104","TF_B_TRADE_CUST_BAK");
		map.put("105","TF_B_TRADE_BPS");
		map.put("106","TF_B_TRADE_BOOKING");
		map.put("107","TF_B_TRADE_BATDEAL");
		map.put("108","TF_B_TRADE_ASSURE");
		map.put("109","TF_B_TRADE_ACCT_CONSIGN");
		map.put("110","TF_B_TRADE_ACCT");
		map.put("111","TF_B_TRADE");
		map.put("112","TF_B_TERMINAL_SALE_DETAIL");
		map.put("113","TF_B_TELCARD_TRADE_LOG");
		map.put("114","TF_B_SIM_SALE_DETAIL");
		map.put("115","TF_B_RES_TRADE_LOG");
		map.put("116","TF_B_RES_TICKET");
		map.put("117","TF_B_RES_SALE_LOG");
		map.put("118","TF_B_RES_MISPOS");
		map.put("119","TF_B_RES_BADCARD_INFO");
		map.put("120","TF_B_IPHONE_TERMINAL_SALE");
		map.put("121","TF_B_INSURANCE_LOG");
		map.put("122","TF_BH_TRADE_USER");
		map.put("123","TF_BH_TRADE_TRANS");
		map.put("124","TF_BH_TRADE_SVCSTATE");
		map.put("125","TF_BH_TRADE_SVC");
		map.put("126","TF_BH_TRADE_SUB_ITEM");
		map.put("127","TF_BH_TRADE_SP");
		map.put("128","TF_BH_TRADE_RES");
		map.put("129","TF_BH_TRADE_PRODUCT_TYPE");
		map.put("130","TF_BH_TRADE_PRODUCT");
		map.put("131","TF_BH_TRADE_ITEM");
		map.put("132","TF_BH_TRADEFEE_SUB");
		map.put("133","TF_BH_TRADE_DISCNT");
		map.put("134","TF_BH_TRADE");
		map.put("135","TF_BHH_TRADE");
		map.put("136","TF_B_EXHIBITIONPREORDER");
		map.put("137","TF_A_PAYRELATION");
		map.put("138","TD_M_RESVALUE");
		map.put("139","HIS_TF_BH_TRADE");
		map.put("140","TF_F_CUST_PERSON");
		map.put("141","TF_M_STAFFFUNCRIGHT_NEW");
		map.put("142","TF_M_ROLEFUNCRIGHT");
		map.put("143","TF_M_ROLEDATARIGHT_NEW");
		map.put("144","TF_M_OBJECTDATARIGHT");
		map.put("145","TF_F_GUARANT_CONTRACT");
		map.put("146","TF_F_CUST_MANAGER_STAFF");
		map.put("147","TF_F_CONTRACTCUST");
		map.put("148","TF_F_COMPANY");
		map.put("149","TF_CHL_CHANNEL");
		map.put("150","TD_S_TRADETYPE_LIMIT");
		map.put("151","TD_S_TRADETYPE");
		map.put("152","TD_S_TRADE_SPECLIMIT");
		map.put("153","TD_S_TAG");
		map.put("154","TD_S_SVCSTATE_TRADE_LIMIT");
		map.put("155","TD_S_STATIC");
		map.put("156","TD_S_SERVICESTATE");
		map.put("157","TD_S_ROLE_REL");
		map.put("158","TD_S_REVENUE_LEVEL");
		map.put("159","TD_S_RELIGION");
		map.put("160","TD_S_RELATION_ROLE");
		map.put("161","TD_S_RELATION");
		map.put("162","TD_S_PRODUCT_TYPE_TRANS");
		map.put("163","TD_S_PRODUCT_TYPE_GD");
		map.put("164","TD_S_PRODUCT_TYPE");
		map.put("165","TD_S_PRODUCT_TRANS");
		map.put("166","TD_S_PRODUCTLIMIT");
		map.put("167","TD_S_PROD_SPEC_LIMIT");
		map.put("168","TD_S_PROD_RES_REL");
		map.put("169","TD_S_PAYMODE");
		map.put("170","TD_S_PASSPORTTYPE");
		map.put("171","TD_S_NETCODE");
		map.put("172","TD_S_NATIONALITY");
		map.put("173","TD_S_LOCAL_NATIVE");
		map.put("174","TD_S_JOBTYPE");
		map.put("175","TD_S_INTF_ELEMENT");
		map.put("176","TD_S_INTERFACE_CTRL");
		map.put("177","TD_S_INMODE");
		map.put("178","TD_S_FOREGIFT");
		map.put("179","TD_S_FEEITEM_REL");
		map.put("180","TD_S_FEECLASS");
		map.put("181","TD_S_ENUMERATE");
		map.put("182","TD_S_CUST_CLUSTER");
		map.put("183","TD_S_CPARAM");
		map.put("184","TD_S_COMMPARA");
		map.put("185","TD_S_CODE_STATIC");
		map.put("186","TD_S_CALLINGTYPE");
		map.put("187","TD_S_CALLINGSUBTYPE");
		map.put("188","TD_S_BRAND");
		map.put("189","TD_S_ATTRCODE_TRANS");
		map.put("190","TD_S_ASSURETYPE");
		map.put("191","TD_R_TERMINAL_MODEL");
		map.put("192","TD_R_TERMINAL_BRAND");
		map.put("193","TD_R_MACHINE_TYPE");
		map.put("194","TD_M_STAFF_DEPART_REL");
		map.put("195","TD_M_STAFF");
		map.put("196","TD_M_ROLE");
		map.put("197","TD_M_RESTRADE");
		map.put("198","TD_M_RES_PARA");
		map.put("199","TD_M_FUNCRIGHT");
		map.put("200","TD_M_DEVELOPER");
		map.put("201","TD_M_DEPARTKIND");
		map.put("202","TD_M_DEPART");
		map.put("203","TD_M_DATARIGHT");
		map.put("204","TD_M_AREA_ADDRESS");
		map.put("205","TD_M_AREA");
		map.put("206","TD_CHL_OBJTYPE");
		map.put("207","TD_CHL_KINDDEF");
		map.put("208","TD_B_USERTYPE");
		map.put("209","TD_B_USER_PARAM_DEF");
		map.put("210","TD_B_SYSTEMGUIMENU");
		map.put("211","TD_B_SPTYPE");
		map.put("212","TD_B_SERVICE");
		map.put("213","TD_B_SCHEME_LIMIT");
		map.put("214","TD_B_REMOVE_REASON");
		map.put("215","TD_B_PTYPE_PRODUCT");
		map.put("216","TD_B_PROMOTION_PRODUCT");
		map.put("217","TD_B_PROMOTION");
		map.put("218","TD_B_PRODUCT_TRADEFEE");
		map.put("219","TD_B_PRODUCT_RELEASE");
		map.put("220","TD_B_PRODUCT_PACKAGE");
		map.put("221","TD_B_PRODUCT_ITEM");
		map.put("222","TD_B_PRODUCT_GROUP");
		map.put("223","TD_B_PRODUCT");
		map.put("224","TD_B_PROD_PACKAGE_LIMIT");
		map.put("225","TD_B_PAYMENT");
		map.put("226","TD_B_PARTY_SERVICE");
		map.put("227","TD_B_PARTY_PRODUCT");
		map.put("228","TD_B_PARTY");
		map.put("229","TD_B_PACKAGE_LIMIT");
		map.put("230","TD_B_PACKAGE_ELEMENT_LIMIT");
		map.put("231","TD_B_PACKAGE_ELEMENT");
		map.put("232","TD_B_PACKAGE");
		map.put("233","TD_B_OPERFEE");
		map.put("234","TD_B_ITEMS");
		map.put("235","TD_B_GIFT_EXCHANGE");
		map.put("236","TD_B_FEEITEM");
		map.put("237","TD_B_ELEMENT_PACKAGE_LIMIT");
		map.put("238","TD_B_ELEMENT_LIMIT");
		map.put("239","TD_B_DISCNT_ITEM");
		map.put("240","TD_B_DISCNT");
		map.put("241","TD_B_DEFPRODUCT_PHONE");
		map.put("242","TD_B_COMPPRODUCT");
		map.put("243","TD_B_COMPPROD_MEMRULE");
		map.put("244","TD_B_COMPPROD_MEMPROD");
		map.put("245","TD_B_ACTION_RELYON");
		map.put("246","TD_B_ACTION_NEWREL");
		map.put("247","TF_F_CUST_GROUP");
		map.put("248","TB_OGG_TIME");
		map.put("249","TF_F_CYCLIENT");
		map.put("250","TD_B_RELATION_PRODUCT");


		System.out.println("!!!!!!!"+redisService.get("STREAM_ORACLE_9900_CRM02").get("250"));
		redisService.setInfo("STREAM_ORACLE_9900_CRM03",map);
		System.out.println("STREAM_ORACLE_9900_CRM01"+redisService.get("STREAM_ORACLE_9900_CRM01"));
		System.out.println("STREAM_ORACLE_9900_CRM02"+redisService.get("STREAM_ORACLE_9900_CRM02"));
		System.out.println("STREAM_ORACLE_9900_CRM01+0"+redisService.get("STREAM_ORACLE_9900_CRM01").get("0"));






	}
	public static void main(String[] args){
		SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:00:00" );
		Date d= new Date();
		String str = sdf.format(d);
		System.out.println(str);


	}
	
}
