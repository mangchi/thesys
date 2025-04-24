package com.thesys.titan.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

	// Private constructor to prevent instantiation
	private StringUtil() {
		throw new UnsupportedOperationException("Utility class");
	}

	private static final SecureRandom rand = new SecureRandom();

	/**
	 * 문자열 오른쪽으로 빈값(스페이스)채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param srcStr String 대상 문자열
	 * @param padLen int 요청 문자열 길이
	 * @return String padResult 결과 문자열
	 */
	public static String rpad(String srcStr, int padLen) {
		return rpad(srcStr, padLen, " ");
	}

	/**
	 * 문자열 오른쪽으로 요청 문자로 채워 요청 길이 만큼의 문자열을 리턴한다.
	 *
	 * @param srcStr  String 대상 문자열
	 * @param padLen  int 요청 문자열 길이
	 * @param trgtStr String 요청 문자
	 * @return String padResult 결과 문자열
	 */
	public static String rpad(String srcStr, int padLen, String trgtStr) {
		if (srcStr == null) {
			return srcStr;
		}

		if (trgtStr == null) {
			trgtStr = " ";
		}

		if (srcStr.length() >= padLen) {
			return srcStr.substring(0, padLen);
		}

		return padString(srcStr, padLen, trgtStr);
	}

	private static String padString(String srcStr, int padLen, String trgtStr) {
		StringBuilder padResult = new StringBuilder(srcStr);
		while (padResult.length() < padLen) {
			padResult.append(trgtStr);
		}
		return padResult.substring(0, padLen);
	}

	/**
	 * 정수형 문자열 세자리 마다 콤마를 찍어 String으로 리턴한다.
	 *
	 * @param str String 정수형 문자열
	 * @return String 콤마가 추가된 문자열
	 */
	public static String stringToNumFormat(String str) {
		if (str == null || str.equals("")) {
			return "0";
		}

		int inValues = Integer.parseInt(str);
		DecimalFormat commas = new DecimalFormat("#,###");
		return commas.format(inValues);
	}

	/**
	 * 숫자 세자리 마다 콤마를 찍어 String으로 리턴한다.
	 *
	 */
	public static String intToNumFormat(int num) {
		DecimalFormat commas = new DecimalFormat("#,###");
		return commas.format(num);
	}

	/**
	 *
	 * @Method Name : lengthLimit
	 * @author : kdh
	 * @Date : 2019. 12. 26.
	 * @Description : 한글 포함 문자열, 특정 길이만큼 자르기
	 * @History :
	 * @param inputStr
	 * @param limit
	 * @param fixStr
	 * @return
	 */
	public static String lengthLimit(String inputStr, int limit, String fixStr) {
		if (inputStr == null) {
			return "";
		}
		if (limit <= 0) {
			return inputStr;
		}

		byte[] strbyte = null;
		strbyte = inputStr.getBytes();

		if (strbyte.length <= limit) {
			return inputStr;
		}
		char[] charArray = inputStr.toCharArray();
		int checkLimit = limit;
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] < 256) {
				checkLimit -= 1;
			} else {
				checkLimit -= 2;
			}
			if (checkLimit <= 0) {
				break;
			}
		}
		// 대상 문자열 마지막 자리가 2바이트의 중간일 경우 제거함
		byte[] newByte = new byte[limit + checkLimit];
		for (int i = 0; i < newByte.length; i++) {
			if (i < strbyte.length) {
				newByte[i] = strbyte[i];
			}

		}
		if (fixStr == null) {
			return new String(newByte);
		} else {
			return new String(newByte) + fixStr;
		}
	}

	public static String subStringBytes(String str, int byteLength, int sizePerLetter) {
		int retLength = 0;
		int tempSize = 0;
		int asc;
		if (str == null || "".equals(str) || "null".equals(str)) {
			str = "";
		}

		int length = str.length();

		for (int i = 1; i <= length; i++) {
			asc = str.charAt(i - 1);
			if (asc > 127) {
				if (byteLength >= tempSize + sizePerLetter) {
					tempSize += sizePerLetter;
					retLength++;
				}
			} else {
				if (byteLength > tempSize) {
					tempSize++;
					retLength++;
				}
			}
		}

		return str.substring(0, retLength);
	}

	public static String byteCuter(String inputStr, int cutLeng, String append) {
		if (inputStr == null) {
			return "";
		}
		if (inputStr.getBytes().length > cutLeng) {
			StringBuilder stringBuilder = new StringBuilder(cutLeng);
			int nCnt = 0;
			for (char ch : inputStr.toCharArray()) {
				nCnt += String.valueOf(ch).getBytes().length;
				if (nCnt > cutLeng)
					break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString() + append;
		} else {
			return inputStr;
		}

	}

	/**
	 *
	 * @Method Name : getRandomStr
	 * @author : kdh
	 * @Date : 2019. 10. 30.
	 * @Description : 무작위 문자열 생성
	 * @History :
	 * @param len : 문자열 사이즈
	 * @return
	 */
	public static String getRandomStr(int len) {
		String capitalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String smallChars = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		String symbols = "!@%~*_=+-/.<>)";

		String values = capitalChars + smallChars + numbers + symbols;

		rand.setSeed(System.currentTimeMillis());
		char[] randomStr = new char[len];

		for (int i = 0; i < len; i++) {
			randomStr[i] = values.charAt(rand.nextInt(values.length()));
		}
		return String.valueOf(randomStr);
	}

	public static String convertUnderScore(String camelStr) {

		StringBuilder result = new StringBuilder();
		char c = camelStr.charAt(0);
		result.append(Character.toLowerCase(c));

		for (int i = 1; i < camelStr.length(); i++) {

			char ch = camelStr.charAt(i);

			if (Character.isUpperCase(ch)) {
				result.append('_');
				result.append(Character.toLowerCase(ch));
			}

			else {
				result.append(ch);
			}
		}

		return result.toString().toUpperCase();
	}

	public static String convertCamelCase(String underScore) {
		if (underScore.indexOf('_') < 0 && Character.isLowerCase(underScore.charAt(0))) {
			return underScore;
		}
		StringBuilder result = new StringBuilder();
		boolean nextUpper = false;
		int len = underScore.length();
		for (int i = 0; i < len; i++) {
			char currentChar = underScore.charAt(i);
			if (currentChar == '_') {
				nextUpper = true;
			} else {
				if (nextUpper) {
					result.append(Character.toUpperCase(currentChar));
					nextUpper = false;
				} else {
					result.append(Character.toLowerCase(currentChar));
				}

			}

		}
		return result.toString();

	}

	/**
	 *
	 * @Method Name : convertPrivacyStr
	 * @author : mangchi
	 * @Date : 2022. 2. 4.
	 * @History :
	 * @param privacyStr 주민번호 포함 문자열
	 * @param idx        변경할려는 문자열의 시작 인덱스
	 * @return
	 */
	public static String convertPrivacyStr(String privacyStr, int idx) {
		if (privacyStr == null || privacyStr.isEmpty()) {
			return "";
		}
		String switchStr = "******-*******".substring(idx, 14);

		privacyStr = processPattern(privacyStr, idx, switchStr,
				"\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\-[1-8][0-9]{6}");
		privacyStr = processPattern(privacyStr, idx, switchStr,
				"\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\-[1-8][0-9][*]{6}");

		return privacyStr;
	}

	private static String processPattern(String privacyStr, int idx, String switchStr, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(privacyStr);
		while (matcher.find()) {
			String matchedGroup = matcher.group();
			if (isValidPrivacyString(matchedGroup)) {
				privacyStr = privacyStr.replace(matchedGroup, matchedGroup.substring(0, idx) + switchStr);
			}
		}
		return privacyStr;
	}

	private static boolean isValidPrivacyString(String str) {
		int[] validInt = { 11, 12, 13, 14, 15, 21, 22, 31 };
		int[] milInt = { 3, 4, 7, 8 };
		int[] nonMilInt = { 1, 2, 5, 6 };

		String modifiedStr = str.replace("-", "");
		int strInt = 0;
		for (int i = 0; i < 12; i++) {
			strInt += Integer.parseInt(modifiedStr.substring(i, i + 1)) * (i % 2 + 1);
		}
		int chkInt = 10 - (strInt % 10);
		if (chkInt == 10) {
			chkInt = 0;
		}
		return chkInt == Integer.parseInt(modifiedStr.substring(12, 13))
				&& IntStream.of(validInt).anyMatch(x -> x == Integer.parseInt(modifiedStr.substring(4, 6)))
				&& ((!modifiedStr.startsWith("0")
						&& IntStream.of(nonMilInt).anyMatch(x -> x == Integer.parseInt(modifiedStr.substring(6, 7))))
						|| (modifiedStr.startsWith("0") && IntStream.of(milInt)
								.anyMatch(x -> x == Integer.parseInt(modifiedStr.substring(6, 7)))));
	}

	/**
	 *
	 * @Method Name : checkOrdStr
	 * @author : mangchi
	 * @Date : 2022. 4. 26.
	 * @Description : 연속적인 오름,내림차순 체크 ex)연속 세자리 오름/내림 차순 checkOrdStr("123egw,3)
	 * @History :
	 * @param str
	 * @param limit
	 * @return
	 */
	public static boolean checkOrdStr(String str, Integer limit) {
		int l = limit == null ? 3 : limit;
		if (l > 3) {
			return hasLongSequentialOrder(str, l);
		} else {
			return hasShortSequentialOrder(str);
		}
	}

	private static boolean hasLongSequentialOrder(String str, int limit) {
		int o = 0;
		int d = 0;
		int p = 0;
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			char tmpVal = str.charAt(i);
			if (i > 0 && (p = o - tmpVal) > -2 && (n = p == d ? n + 1 : 0) > limit - 3) {
				return true;
			}
			d = p;
			o = tmpVal;
		}
		return false;
	}

	private static boolean hasShortSequentialOrder(String str) {
		String tmpStr = str.toUpperCase();
		int strLen = tmpStr.length();
		int[] tmpArray = new int[strLen];

		for (int i = 0; i < strLen; i++) {
			tmpArray[i] = tmpStr.charAt(i);
		}
		for (int i = 0; i < strLen - 2; i++) {
			if (isSequential(tmpArray, i)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSequential(int[] tmpArray, int i) {
		return ((tmpArray[i] > 47 && tmpArray[i + 2] < 58) || (tmpArray[i] > 64 && tmpArray[i + 2] < 91))
				&& Math.abs(tmpArray[i + 2] - tmpArray[i + 1]) == 1
				&& Math.abs(tmpArray[i + 2] - tmpArray[i]) == 2;
	}

	/**
	 *
	 * @Method Name : checkSerialDupStr
	 * @author : mangchi
	 * @Date : 2022. 4. 26.
	 * @Description : 동일한 문자가 연속으로 있는 경우
	 * @History :
	 * @param str
	 * @param limit
	 * @return
	 */
	public static boolean checkSerialDupStr(String str, Integer limit) {
		try {
			log.debug("str:{}", str);
			log.debug("limit:{}", limit);
			// Removed unused local variable 'l'
			String patternStr = "(\\w)";
			//
			// for(int i=0;i<l-1;i++) {
			// patternStr += "\\1";
			// }

			Pattern passPattern = Pattern.compile(patternStr);
			Matcher passMatcher = passPattern.matcher(str);
			if (passMatcher.find()) {
				return true;
			}
		} catch (PatternSyntaxException e) {
			log.error("error:", e);
		}
		return false;
	}

	public static boolean checkPwdStr(String str) {
		String patternStr = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{9,20}";
		Pattern passPattern = Pattern.compile(patternStr);
		Matcher passMatcher = passPattern.matcher(str);
		return passMatcher.find();
	}

	protected static final long[] IP_RANGE = { 16777216L, 65536L, 256L };

	public static String longToIp(long addr) {
		StringBuilder ip = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			if (i < 3) {
				ip.append((addr / IP_RANGE[i]) % 256).append(".");
			} else {
				ip.append(addr % 256);
			}
		}
		return ip.toString();
	}

	public static long ipToLong(String[] addr) {
		if (addr == null || addr.length < 4) {
			return 0L;
		}
		long ip = 0L;
		for (int i = 0; i < 4; i++) {
			// if(addr[i]) {
			if (i < 3) {
				ip += IP_RANGE[i] * Long.parseLong(addr[i]);
			} else {
				ip += Long.parseLong(addr[i]);
			}
			// else {
			// return 0L;

			// }
		}
		return ip;
	}

	public static long ipToLong(String ip) {
		return ipToLong(getBytesByInetAddress(ip));
	}

	public static long ipToLong(byte[] address) {
		if (address == null || address.length < 4) {
			return 0L;
		}
		long ip = 0L;
		for (int i = 0; i < 4; ++i) {
			long y = address[i];
			if (y < 0) {
				y += 256;
			}
			ip += y << ((3 - i) * 8);
		}
		return ip;

	}

	public static byte[] getBytesByInetAddress(String ip) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			return new byte[0];
		}
		return addr.getAddress();
	}

	public static long byteArrayToLong(byte[] bytes) {
		// Removed unnecessary assignment to byteBuf
		final byte[] change = new byte[8];

		for (int i = 0; i < 8; i++) {
			change[i] = (byte) 0x00;
		}
		for (int i = 0; i < bytes.length; i++) {
			change[7 - i] = bytes[bytes.length - 1 - i];
		}

		ByteBuffer byteBuf = ByteBuffer.wrap(change);
		byteBuf.order(ByteOrder.BIG_ENDIAN);

		return byteBuf.getLong();
	}

	public static boolean isOnlyNumber(String s) {
		return s != null && s.matches("\\d*");
	}

	public static boolean isTenIpNumber(String s) {
		if (isOnlyNumber(s)) {
			return s.length() == 10;
		} else {
			return false;
		}
	}

	public static String getBoolean(String boolStr) {
		String rBoolStr = "false";
		if (boolStr == null || boolStr.trim().equals("")) {
			return rBoolStr;
		}
		if (boolStr.trim().equals("1")) {
			rBoolStr = "true";
		}
		return rBoolStr;
	}

}
