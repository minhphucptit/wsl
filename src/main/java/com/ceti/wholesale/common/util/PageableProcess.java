package com.ceti.wholesale.common.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import java.util.List;

public class PageableProcess {
	public static String PageToSqlQuery(Pageable pageable, String defaultTable) {
		String page_ = "";
		Sort sort = pageable.getSort();
		List<Order> orders = sort.toList();
		for (Order order : orders) {
			String prop = order.getProperty();
			if (prop.split("\\.").length == 1) {
				prop = defaultTable + "." + prop;
			}
			if (page_ != "") {
				page_ += " ," + prop;
			} else {
				page_ = " order by " + prop;
			}
			Direction direction = order.getDirection();
			if (direction.isAscending()) {
				page_ += " asc";
			} else {
				page_ += " desc";
			}
		}
		long offSet = pageable.getOffset();
		long pageSize = pageable.getPageSize();
		if (page_ == "") {
			page_ = " order by (select null)";
		}
		page_ += " OFFSET " + offSet + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
		return page_;
	}

	public static String PageToSqlQueryOrderBy(Pageable pageable, String defaultTable) {
		String page_ = "";
		Sort sort = pageable.getSort();
		List<Order> orders = sort.toList();
		for (Order order : orders) {
			String prop = order.getProperty();
			if (prop.split("\\.").length == 1) {
				prop = defaultTable + "." + prop;
			}
			if (page_ != "") {
				page_ += " ," + prop;
			} else {
				page_ = " order by " + prop;
			}
			Direction direction = order.getDirection();
			if (direction.isAscending()) {
				page_ += " asc";
			} else {
				page_ += " desc";
			}
		}
		long offSet = pageable.getOffset();
		long pageSize = pageable.getPageSize();
		if (page_ == "") {
			page_ = " order by 1";
		}
		page_ += " OFFSET " + offSet + " ROWS FETCH NEXT " + pageSize + " ROWS ONLY";
		return page_;
	}
}
