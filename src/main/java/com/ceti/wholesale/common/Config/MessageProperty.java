package com.ceti.wholesale.common.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MessageProperty {

	@Autowired
	private MessageSource messageSource;

	// hàm lây message trong file messages
	public String getMessage(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}

	// hàm lây message trong file messages với message mặc định khi không tìm thấy
	public String getMessage(String key, String defaultValue) {
		return messageSource.getMessage(key, null, defaultValue, LocaleContextHolder.getLocale());
	}
}
