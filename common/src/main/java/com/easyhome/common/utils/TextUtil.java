package com.easyhome.common.utils;

import android.text.TextUtils;

/**
 * 文本工具类
 * 
 * @author wangzengyang 2012-11-19
 * 
 */
public class TextUtil {
	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param str
	 *            the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		str = str.trim();
		return str.length() == 0 || str.equals("null");
	}

	/**
	 * 获取字符串的长度，中文占一个字符,英文数字占半个字符
	 *
	 * @param value  指定的字符串
	 * @return 字符串的长度
	 */
	public static double chineseLength(String value) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 1;
			} else {
				// 其他字符长度为0.5
				valueLength += 0.5;
			}
		}
		//进位取整
		return  Math.ceil(valueLength);
	}

	/**
	 * 获得最大长度的汉字字串
	 * @param source
	 * @param maxLength
	 * @return
	 */
	public static String getChineseStringByMaxLength(String source, int maxLength) {
		String cutString = "";
		double tempLength = 0;
		for (int i = 0; i < source.length(); i++) {
			String temp = source.substring(i, i + 1);
			String chinese = "[\u4e00-\u9fa5]";
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				tempLength += 1;
			} else {
				// 其他字符长度为0.5
				tempLength += 0.5;
			}
			cutString += temp;
			if (Math.ceil(tempLength) == maxLength) {
				break;
			}
		}
		return cutString;
	}

    /**
	 * 去掉文件名称中的非法字符
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeFileName(String str) {
		if (str == null) {
			return null;
		}
		/** 非法字符包括：/\:*?"<>| */
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c == '/' || c == '\\' || c == ':' || c == '*' || c == '?' || c == '"' || c == '<'
					|| c == '>' || c == '|') {
				continue;
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	/**
	 * 从url获取当前图片的id，如果url以ignoreTag开头则直接返回该url；如果ignoreTag为空，则不会判断ignoreTag
	 * 
	 * @param ignoreTag
	 * @param url
	 * @return
	 */
	public static String getIdFromUrl(String url, String ignoreTag) {
		if (TextUtils.isEmpty(url) || (!TextUtils.isEmpty(ignoreTag)) && url.startsWith(ignoreTag)) {
			return url;
		}
		int lastIndex = url.lastIndexOf(".jpg");
		if (lastIndex < 0) {
			lastIndex = url.length() - 1;
		}
		int beginIndex = url.lastIndexOf("/") + 1;
		int slashIndex = url.lastIndexOf("%2F") + 3;
		int finalSlashIndex = url.lastIndexOf("%252F") + 5;
		beginIndex = Math.max(Math.max(beginIndex, slashIndex), finalSlashIndex);

		return url.substring(beginIndex, lastIndex);
	}

	public static String getIdFromUrl(String url) {
		return getIdFromUrl(url, null);
	}

	public static String trim(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str.trim();
	}

	/**
	 * [A-z0-9]{2,} 任意字母和数字组合，并长度大于等于2，必选
	 * <p>
	 * [@] 必选
	 * <p>
	 * [a-z0-9]{2,} 任意小写字母和数字组合，并长度大于等于2，必选
	 * <p>
	 * [.] 必选
	 * <p>
	 * \p{Lower}{2,} 任意小写字母，并长度大于等于2，必选
	 * <p>
	 * @param emailString
	 * @return
	 */
	public static boolean validateEmailAddress(String emailString) {
		String format = "[A-z0-9]{2,}[@][a-z0-9]{2,}[.]\\p{Lower}{2,}";
		if (emailString.matches(format)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 匹配格式： 11位手机号码 3-4位区号，7-8位直播号码，1－4位分机号 如：12345678901、1234-12345678-1234
	 * 
	 * @param phoneString
	 * @return
	 */
	public static boolean validatePhoneNumber(String phoneString) {
		String format = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
		if (phoneString.matches(format)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 5-12位的数字
	 * @param qqString
	 * @return
	 */
	public static boolean validateQQNumber(String qqString) {
		String format = "^\\d{5,12}$";
		if (qqString.matches(format)) {
			return true;
		} else {
			return false;
		}
	}
}
