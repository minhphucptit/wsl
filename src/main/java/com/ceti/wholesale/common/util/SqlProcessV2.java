package com.ceti.wholesale.common.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.LongType;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

import com.ceti.wholesale.common.constant.ResultPage;

public class SqlProcessV2 {
	// Hiện tại getWhereSql đã nhận được param dạng table.field từ front end
	@SuppressWarnings("unchecked")
	public static String getWhereSql(String defaultTableName, MultiValueMap<String, String> where,
			Map<String, Object> queryTypes) {
		List<String> equalType = (List<String>) queryTypes.get("equalType");
		List<String> likeType = (List<String>) queryTypes.get("likeType");
		List<String> dateType = (List<String>) queryTypes.get("dateType");
		List<String> inRangeType = (List<String>) queryTypes.get("inRangeType");
		List<String> inType = (List<String>) queryTypes.get("inType");
		Map<String, List<String>> searchType = (Map<String, List<String>>) queryTypes.get("searchType");
		if (equalType == null) {
			equalType = new ArrayList<String>();
		}
		if (likeType == null) {
			likeType = new ArrayList<String>();
		}
		if (dateType == null) {
			dateType = new ArrayList<String>();
		}
		if (inRangeType == null) {
			inRangeType = new ArrayList<String>();
		}
		if (searchType == null) {
			searchType = new HashMap<String, List<String>>();
		}
		if (inType == null) {
			inType = new ArrayList<String>();
		}

		Pattern patternFrom = Pattern.compile("^(.+)_from$");
		Pattern patternTo = Pattern.compile("^(.+)_to$");

		// list condition allowed
		List<String> conditionAlloweds = new ArrayList<>();
		// check if request query param is allowed
		for (String condition : where.keySet()) {
			String fieldWithTable = "";
			if (condition.contains(".")) {
				fieldWithTable = condition;
			} else {
				fieldWithTable = defaultTableName + "." + condition;
			}

			String conditionAllowed = "";
			Matcher matcherFrom = patternFrom.matcher(condition);
			Matcher matcherTo = patternTo.matcher(condition);
			if (likeType.contains(condition)) {
				conditionAllowed = fieldWithTable + " like concat('%',:like_type" + likeType.indexOf(condition)
						+ ",'%')";
				conditionAlloweds.add(conditionAllowed);
			} else if (equalType.contains(condition)) {
				if (where.get(condition).size() == 1) {
					conditionAllowed = fieldWithTable + " = :equal_type" + equalType.indexOf(condition);
				} else {
					conditionAllowed = fieldWithTable + " in :equal_type" + equalType.indexOf(condition);
				}
				conditionAlloweds.add(conditionAllowed);
			}else if (inType.contains(condition)) {
				fieldWithTable = fieldWithTable.replace("_in",""); // từ key lấy tên cột
				conditionAllowed = fieldWithTable + " in :in_type" + inType.indexOf(condition);
				conditionAlloweds.add(conditionAllowed);
			}
			else if (searchType.keySet().contains("search") && condition.equals("search")) {
				List<String> sqlSearch = new ArrayList<>();
				for (String field : searchType.get("search")) {
					if (field.contains(".")) {
						sqlSearch.add(field + " like concat('%',:" + condition + ",'%')");
					} else {
						sqlSearch.add(defaultTableName + "." + field + " like concat('%',:" + condition + ",'%')");
					}
				}
				conditionAlloweds.add("(" + String.join(" or ", sqlSearch) + ")");
			} else if (matcherFrom.matches() && dateType.contains(matcherFrom.group(1))) {
				if (matcherFrom.group(1).contains(".")) {
					conditionAllowed = matcherFrom.group(1) + " >= :date_type_from";
				} else {
					conditionAllowed = defaultTableName + "." + matcherFrom.group(1) + " >= :date_type_from";
				}
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherTo.matches() && dateType.contains(matcherTo.group(1))) {
				if (matcherTo.group(1).contains(".")) {
					conditionAllowed = matcherTo.group(1) + " <= :date_type_to";
				} else {
					conditionAllowed = defaultTableName + "." + matcherTo.group(1) + " <= :date_type_to";
				}
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherFrom.matches() && inRangeType.contains(matcherFrom.group(1))) {

				if (matcherFrom.group(1).contains(".")) {
					conditionAllowed = matcherFrom.group(1) + " >= :in_range_type_from";
				} else {
					conditionAllowed = defaultTableName + "." + matcherFrom.group(1) + " >= :in_range_type_from";
				}
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherTo.matches() && inRangeType.contains(matcherTo.group(1))) {
				if (matcherTo.group(1).contains(".")) {
					conditionAllowed = matcherTo.group(1) + " <= :in_range_type_to";
				} else {
					conditionAllowed = defaultTableName + "." + matcherTo.group(1) + " <= :in_range_type_to";
				}
				conditionAlloweds.add(conditionAllowed);
			}
		}
		if (conditionAlloweds.size() == 0) {
			return "";
		}
		return " where " + String.join(" and ", conditionAlloweds);
	}

	// Hàm này nhằm mục đích lấy where sql chỉ chứa tên column
	@SuppressWarnings("unchecked")
	public static String getWhereSqlOnlyColumnName(MultiValueMap<String, String> where,
			Map<String, Object> queryTypes) {
		List<String> equalType = (List<String>) queryTypes.get("equalType");
		List<String> likeType = (List<String>) queryTypes.get("likeType");
		List<String> dateType = (List<String>) queryTypes.get("dateType");
		List<String> inRangeType = (List<String>) queryTypes.get("inRangeType");
		Map<String, List<String>> searchType = (Map<String, List<String>>) queryTypes.get("searchType");
		if (equalType == null) {
			equalType = new ArrayList<String>();
		}
		if (likeType == null) {
			likeType = new ArrayList<String>();
		}
		if (dateType == null) {
			dateType = new ArrayList<String>();
		}
		if (inRangeType == null) {
			inRangeType = new ArrayList<String>();
		}
		if (searchType == null) {
			searchType = new HashMap<String, List<String>>();
		}
		Pattern patternFrom = Pattern.compile("^(.+)_from$");
		Pattern patternTo = Pattern.compile("^(.+)_to$");

		// list condition allowed
		List<String> conditionAlloweds = new ArrayList<>();
		// check if request query param is allowed
		for (String condition : where.keySet()) {
			String conditionAllowed = "";
			Matcher matcherFrom = patternFrom.matcher(condition);
			Matcher matcherTo = patternTo.matcher(condition);
			if (likeType.contains(condition)) {
				conditionAllowed = condition + " like concat('%',:like_type" + likeType.indexOf(condition) + ",'%')";
				conditionAlloweds.add(conditionAllowed);
			} else if (equalType.contains(condition)) {
				if (where.get(condition).size() == 1) {
					conditionAllowed = condition + " = :equal_type" + equalType.indexOf(condition);
				} else {
					conditionAllowed = condition + " in :equal_type" + equalType.indexOf(condition);
				}
				conditionAlloweds.add(conditionAllowed);
			} else if (searchType.keySet().contains("search") && condition.equals("search")) {
				List<String> sqlSearch = new ArrayList<>();
				for (String field : searchType.get("search")) {
					sqlSearch.add(field + " like concat('%',:" + condition + ",'%')");
				}
				conditionAlloweds.add("(" + String.join(" or ", sqlSearch) + ")");
			} else if (matcherFrom.matches() && dateType.contains(matcherFrom.group(1))) {
				conditionAllowed = matcherFrom.group(1) + " >= :date_type_from";
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherTo.matches() && dateType.contains(matcherTo.group(1))) {
				conditionAllowed = matcherTo.group(1) + " <= :date_type_to";
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherFrom.matches() && inRangeType.contains(matcherFrom.group(1))) {
				conditionAllowed = matcherFrom.group(1) + " >= :in_range_type_from";
				conditionAlloweds.add(conditionAllowed);
			} else if (matcherTo.matches() && inRangeType.contains(matcherTo.group(1))) {
				conditionAllowed = matcherTo.group(1) + " <= :in_range_type_to";
				conditionAlloweds.add(conditionAllowed);
			}
		}
		if (conditionAlloweds.size() == 0) {
			return "";
		}
		return " where " + String.join(" and ", conditionAlloweds);
	}

	@SuppressWarnings("unchecked")
	public static ResultPage<Object[]> getResultPageWithEmbed(String selectQuery, String fromQuery, Session session,
			String tableName, HashMap<String, Class<?>> allowEntities, Pageable pageable, List<String> embedTables,
			MultiValueMap<String, String> where, Map<String, Object> queryTypes) {

		ResultPage<Object[]> rs = new ResultPage<Object[]>();

		// Get select, from, page sql
		String page_ = PageableProcess.PageToSqlQuery(pageable, tableName);
		String where_ = SqlProcessV2.getWhereSql(tableName, where, queryTypes);

		NativeQuery<Object[]> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_ + page_);
		SqlProcessV2.setParams(query, where, queryTypes);

		query.addEntity(tableName, allowEntities.get(tableName));
		for (String embedTable : embedTables) {
			if (allowEntities.containsKey(embedTable)) {
				query.addEntity(embedTable, allowEntities.get(embedTable));
			}
		}

		rs.setPageList(query.getResultList());

		NativeQuery<Long> queryCount = session.createSQLQuery("select count (*) as total_items " + fromQuery + where_);
		SqlProcessV2.setParams(queryCount, where, queryTypes);

		queryCount.addScalar("total_items", LongType.INSTANCE);
		rs.setTotalItems(queryCount.getResultList().get(0));

		return rs;
	}

	@SuppressWarnings("unchecked")
	public static ResultPage<Object[]> getResultPageColumnWithEmbed(String selectQuery, String fromQuery, Session session,
															  String tableName, HashMap<String, Class<?>> allowEntities, Pageable pageable, List<String> embedTables,
															  MultiValueMap<String, String> where, Map<String, Object> queryTypes, String columnName) {

		ResultPage<Object[]> rs = new ResultPage<Object[]>();

		// Get select, from, page sql
		String page_ = PageableProcess.PageToSqlQueryOrderBy(pageable, tableName);
		String where_ = SqlProcessV2.getWhereSql(tableName, where, queryTypes);

		NativeQuery<Object[]> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_ + page_);
		SqlProcessV2.setParams(query, where, queryTypes);

		query.addEntity(tableName, allowEntities.get(tableName));
		for (String embedTable : embedTables) {
			if (allowEntities.containsKey(embedTable)) {
				query.addEntity(embedTable, allowEntities.get(embedTable));
			}
		}

		rs.setPageList(query.getResultList());

		NativeQuery<Long> queryCount = session.createSQLQuery("select count (distinct "+tableName+"." +columnName+") as total_items " + fromQuery + where_);
		SqlProcessV2.setParams(queryCount, where, queryTypes);

		queryCount.addScalar("total_items", LongType.INSTANCE);
		rs.setTotalItems(queryCount.getResultList().get(0));

		return rs;
	}

	@SuppressWarnings("unchecked")
	public static List<Object[]> getResultListWithEmbed(String selectQuery, String fromQuery, Session session,
			String tableName, HashMap<String, Class<?>> allowEntities, List<String> embedTables,
			MultiValueMap<String, String> where, Map<String, Object> queryTypes) {

		// Get select, from, page sql
		String where_ = SqlProcessV2.getWhereSql(tableName, where, queryTypes);

		NativeQuery<Object[]> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_);
		SqlProcessV2.setParams(query, where, queryTypes);

		query.addEntity(tableName, allowEntities.get(tableName));
		for (String embedTable : embedTables) {
			if (allowEntities.containsKey(embedTable)) {
				query.addEntity(embedTable, allowEntities.get(embedTable));
			}
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public static <T> ResultPage<T> getResultPage(String selectQuery, String fromQuery, Session session,
			String tableName, Class<T> tableEntity, Pageable pageable,
			MultiValueMap<String, String> where, Map<String, Object> queryTypes) {

		ResultPage<T> rs = new ResultPage<T>();

		// Get select, from, page sql
		String page_ = PageableProcess.PageToSqlQuery(pageable, tableName);
		String where_ = SqlProcessV2.getWhereSql(tableName, where, queryTypes);

		NativeQuery<T> query = session.createSQLQuery(selectQuery + " " + fromQuery + where_ + page_);
		SqlProcessV2.setParams(query, where, queryTypes);
		query.addEntity(tableName, tableEntity);
		rs.setPageList(query.getResultList());

		NativeQuery<Long> queryCount = session.createSQLQuery("select count (*) as total_items " + fromQuery + where_);
		SqlProcessV2.setParams(queryCount, where, queryTypes);

		queryCount.addScalar("total_items", LongType.INSTANCE);
		rs.setTotalItems(queryCount.getResultList().get(0));

		return rs;

	}
	
	@SuppressWarnings("unchecked")
	public static <T> ResultPage<T> getResultPage(Session session, String tableName, Class<T> tableEntity,
			Pageable pageable, MultiValueMap<String, String> where, Map<String, Object> queryTypes) {

		ResultPage<T> rs = new ResultPage<T>();

		// Get select, from, page sql
		String select_ = "select {" + tableName + ".*}";
		String from_ = " from " + tableName;
		String page_ = PageableProcess.PageToSqlQuery(pageable, tableName);
		String where_ = SqlProcessV2.getWhereSql(tableName, where, queryTypes);

		NativeQuery<T> query = session.createSQLQuery(select_ + from_ + where_ + page_);
		SqlProcessV2.setParams(query, where, queryTypes);
		query.addEntity(tableName, tableEntity);
		rs.setPageList(query.getResultList());

		NativeQuery<Long> queryCount = session.createSQLQuery("select count (*) as total_items" + from_ + where_);
		SqlProcessV2.setParams(queryCount, where, queryTypes);

		queryCount.addScalar("total_items", LongType.INSTANCE);
		rs.setTotalItems(queryCount.getResultList().get(0));

		return rs;
	}

	@SuppressWarnings("unchecked")
	public static <T> void setParams(NativeQuery<T> query, MultiValueMap<String, String> where,
			Map<String, Object> queryTypes) {
		List<String> equalType = (List<String>) queryTypes.get("equalType");
		List<String> likeType = (List<String>) queryTypes.get("likeType");
		List<String> dateType = (List<String>) queryTypes.get("dateType");
		List<String> inRangeType = (List<String>) queryTypes.get("inRangeType");
		List<String> inType = (List<String>) queryTypes.get("inType");
		Map<String, List<String>> searchType = (Map<String, List<String>>) queryTypes.get("searchType");
		if (equalType == null) {
			equalType = new ArrayList<String>();
		}
		if (likeType == null) {
			likeType = new ArrayList<String>();
		}
		if (dateType == null) {
			dateType = new ArrayList<String>();
		}
		if (inRangeType == null) {
			inRangeType = new ArrayList<String>();
		}
		if (inType == null){
			inType = new ArrayList<>();
		}

		List<String> dateFromFields = new ArrayList<String>();
		List<String> dateToFields = new ArrayList<String>();

		for (String dateKey : dateType) {
			dateFromFields.add(dateKey + "_from");
			dateToFields.add(dateKey + "_to");
		}

		List<String> rangeFromFields = new ArrayList<String>();
		List<String> rangeToFields = new ArrayList<String>();

		for (String field : inRangeType) {
			rangeFromFields.add(field + "_from");
			rangeToFields.add(field + "_to");
		}

		for (String key : where.keySet()) {
			// type like
			if (likeType.contains(key)) {
				query.setParameter("like_type" + likeType.indexOf(key), where.get(key).get(0));
			}

			// type equal
			if (equalType.contains(key)) {
				if (where.get(key).size() == 1) {
					query.setParameter("equal_type" + equalType.indexOf(key), where.get(key).get(0));
				} else {
					query.setParameterList("equal_type" + equalType.indexOf(key), where.get(key));
				}
			}

			if (inType.contains(key)){
				query.setParameterList("in_type" + inType.indexOf(key), where.get(key).get(0).split(","));
			}

			// type date from to
			if (dateFromFields.contains(key)) {
				query.setParameter("date_type_from", Instant.ofEpochSecond(Long.parseLong(where.get(key).get(0))));
			}
			if (dateToFields.contains(key)) {
				query.setParameter("date_type_to", Instant.ofEpochSecond(Long.parseLong(where.get(key).get(0))));
			}
			// type value from to
			if (rangeFromFields.contains(key)) {
				query.setParameter("in_range_type_from", where.get(key).get(0));
			}
			if (rangeToFields.contains(key)) {
				query.setParameter("in_range_type_to", where.get(key).get(0));
			}
			if (searchType != null) {
				if (searchType.keySet().contains("search") && key.equals("search")) {
					query.setParameter(key, where.get(key).get(0));
				}
			}
		}
	}
}
