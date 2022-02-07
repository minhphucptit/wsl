package com.ceti.wholesale.common.util;

import org.springframework.util.MultiValueMap;

import java.util.*;

public class SqlProcess {

	public static String getWhereSql(MultiValueMap<String, String> where, List<String> allow, String tableName,
                                     String... likeFields) {
		// list các điều kiện hợp lệ
		List<String> conditionAlloweds = new ArrayList<>();
		String conditionAllowed = "";
		
		// list các điều kiện so sánh like trong số các đk hợp lệ
		Set<String> likes = new HashSet<>(Arrays.asList(likeFields));
		
		// kiểm tra request query param có hợp lệ không và có phải là điều kiện like không
		for (String condition : where.keySet()) {
			if (likes.contains(condition)) {
				conditionAllowed = tableName + "." + condition + " like concat('%',:" + condition + ",'%')";
				conditionAlloweds.add(conditionAllowed);
			} else {
				if (allow.contains(condition)) {
					conditionAllowed = tableName + "." + condition + " = :" + condition;
					conditionAlloweds.add(conditionAllowed);
				}
			}
		}
		if (conditionAlloweds.size() == 0) {
			return "";
		}
		return " " + String.join(" and ", conditionAlloweds);
	}

	public static String getWhenSql(String where_, String columnName, Long from, Long to) {
		if (from != null) {
			if (where_ != "") {
				where_ += " and";
			}
			where_ += " " + columnName + ">=:" + columnName + "_from";
		}
		if (to != null) {
			if (where_ != "") {
				where_ += " and";
			}
			where_ += " " + columnName + "<=:" + columnName + "_to";
		}
		return where_;
	}
	
	public static String getWhereLikeSqlWithDelimiterOr(MultiValueMap<String, String> where, String tableName,
                                                        String... likeFields) {
		// list các điều kiện hợp lệ
		List<String> conditionAlloweds = new ArrayList<>();
		String conditionAllowed = "";
		
		// list các điều kiện so sánh like trong số các đk hợp lệ
		Set<String> likes = new HashSet<>(Arrays.asList(likeFields));
		
		// kiểm tra request query param có hợp lệ không và có phải là điều kiện like không
		for (String condition : where.keySet()) {
			if (likes.contains(condition)) {
				conditionAllowed = tableName + "." + condition + " like concat('%',:" + condition + ",'%')";
				conditionAlloweds.add(conditionAllowed);
			}
		}
		if (conditionAlloweds.isEmpty()) {
			return "";
		}
		return " " + String.join(" or ", conditionAlloweds);
	}

	public static String getWhenSqlWithTableName(String where_, String columnName, Long from, Long to, String tableName) {
		if (from != null) {
			if (where_ != "") {
				where_ += " and";
			}
			where_ += " " + tableName + "." + columnName + ">=:" + columnName + "_from";
		}
		if (to != null) {
			if (where_ != "") {
				where_ += " and";
			}
			where_ += " " + tableName + "." + columnName + "<=:" + columnName + "_to";
		}
		return where_;
	}

}
