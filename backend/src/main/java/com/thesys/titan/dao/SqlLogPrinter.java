package com.thesys.titan.dao;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlLogPrinter {

	public static void logPrint(BoundSql boundSql, String sql, String queryId)
			throws NoSuchFieldException, IllegalAccessException {
		log.debug("Query Id : {}", queryId);
		if (!"".equals(queryId)) {
			// return;
		}
		Object param = boundSql.getParameterObject();
		if (param == null) {
			sql = sql.replaceFirst("\\?", "''");
		} else {
			if (param instanceof Integer || param instanceof Long || param instanceof Float
					|| param instanceof Double) {
				sql = sql.replaceFirst("\\?", String.valueOf(param));
			} else if (param instanceof String) { // 해당 파라미터의 클래스가 String 일 경우(이 경우는 앞뒤에 '(홑따옴표)를 붙여야해서 별도 처리
				sql = sql.replaceFirst("\\?", "'" + param + "'");
			} else if (param instanceof Map) {
				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
				for (ParameterMapping mapping : paramMapping) {
					String propValue = mapping.getProperty(); // 파라미터로 넘긴 Map의 key 값이 들어오게 된다
					Object value = ((Map<?, ?>) param).get(propValue); // 넘겨받은 key 값을 이용해 실제 값을 꺼낸다
					if (value == null) {
						// __frch_ PREFIX에 대한 처리
						if (boundSql.hasAdditionalParameter(propValue)) { // myBatis가 추가 동적인수를 생성했는지 체크하고
							value = boundSql.getAdditionalParameter(propValue); // 해당 추가 동적인수의 Value를 가져옴
							if (value instanceof String) { // SQL의 ? 대신에 실제 값을 넣는다. 이때 String 일 경우는 '를 붙여야 하기땜에 별도 처리
								sql = sql.replaceFirst("\\?", "'" + value + "'");
							} else {
								sql = sql.replaceFirst("\\?", String.valueOf(value));
							}
						}
						if (value == null) {
							continue;
						}
					} else if (value instanceof String) { // SQL의 ? 대신에 실제 값을 넣는다. 이때 String 일 경우는 '를 붙여야 하기땜에 별도 처리
						sql = sql.replaceFirst("\\?", "'" + value + "'");
					} else {
						// log.debug("value:{}",value);
						sql = sql.replaceFirst("\\?", String.valueOf(value));
					}
				}

			} else {// 해당 파라미터가 사용자 정의 클래스일 경우
				/*
				 * 쿼리의 ?와 매핑되는 실제 값들이 List 객체로 return이 된다. 이때 List 객체의 0번째 순서에 있는
				 * ParameterMapping 객체가 쿼리의 첫번째 ?와 매핑이 된다 이런 식으로 쿼리의 ?과 ParameterMapping 객체들을
				 * Mapping 한다
				 */
				List<ParameterMapping> paramMapping = boundSql.getParameterMappings();
				Class<? extends Object> paramClass = param.getClass();
				for (ParameterMapping mapping : paramMapping) {
					String propValue = mapping.getProperty(); // 해당 파라미터로 넘겨받은 사용자 정의 클래스 객체의 멤버변수명
					Field field = paramClass.getDeclaredField(propValue); // 관련 멤버변수 Field 객체 얻어옴
					field.setAccessible(true); // 멤버변수의 접근자가 private일 경우 reflection을 이용하여 값을 해당 멤버변수의 값을 가져오기 위해 별도로 셋팅
					Class<?> javaType = mapping.getJavaType(); // 해당 파라미터로 넘겨받은 사용자 정의 클래스 객체의 멤버변수의 타입

					if (String.class == javaType) { // SQL의 ? 대신에 실제 값을 넣는다. 이때 String 일 경우는 '를 붙여야 하기땜에 별도 처리
						sql = sql.replaceFirst("\\?", "'" + field.get(param) + "'");
					} else {
						sql = sql.replaceFirst("\\?", String.valueOf(field.get(param)));
					}

				}
			}
		}

		log.debug("Query String : {}", sql);
	}

}
