package com.uxun.pos.utils;

/**
 * 
 * 安全工具
 * 
 * @author Administrator
 * 
 */
public class SecurityUtils {

	/**
	 * 加密
	 * 
	 * @param src
	 * @return
	 */
	public static String encode(String src) {
		String s_rtn;
		String s_asc;
		String s_tmp;
		char s_char;
		int i_asc;
		int i_rem;
		int i_len;
		int i;
		if (src == null || "".equals(src)) {
			return "";
		}
		s_tmp = "";
		i_len = src.length();
		i = 1;
		while (i <= i_len) {
			i_rem = i - 1 - ((i - 1) / 10) * 10;
			s_char = src.charAt(i - 1);
			i_asc = s_char + i_rem;
			s_asc = ("000" + i_asc).substring(((("000" + i_asc)).length() - 3));
			s_tmp = s_tmp + s_asc;
			i++;
		}
		s_rtn = "";
		i_len = s_tmp.length();
		i = 1;
		while (i <= i_len) {
			s_char = s_tmp.charAt(i - 1);
			i_asc = Integer.parseInt(s_char + "") + 1;
			switch (i_asc) {
			case 1:
				s_char = 'A';
				break;
			case 2:
				s_char = 'B';
				break;
			case 3:
				s_char = 'C';
				break;
			case 4:
				s_char = 'D';
				break;
			case 5:
				s_char = 'E';
				break;
			case 6:
				s_char = 'F';
				break;
			case 7:
				s_char = '0';
				break;
			case 8:
				s_char = '1';
				break;
			case 9:
				s_char = '2';
				break;
			case 10:
				s_char = '3';
				break;
			}
			s_rtn = s_rtn + s_char;
			i++;
		}
		return s_rtn;
	}

}
