package com.uxun.pos.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Administrator 格式化工具
 */
public class FormatUtils {

	// IP的正则表达式
	public static final Pattern IP_PATTERN = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	// 默认时间格式
	public static final String DATA_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	//
	public static final SimpleDateFormat DATA_TIME_FORMAT = new SimpleDateFormat(DATA_TIME_PATTERN, Locale.CHINA);

	// 默认数值格式化器
	public static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("0.00");

	public static String formatDecimal(BigDecimal decimal) {
		if (decimal != null) {
			return DEFAULT_DECIMAL_FORMAT.format(decimal);
		}
		return "";
	}

}
