package com.thesys.titan.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import com.thesys.titan.common.vo.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
/*
 * @Intercepts({ @Signature(type = Executor.class, method = "query", args = {
 * MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
 * })
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class QueryInterceptor implements Interceptor {

	private static String countIdSuffix = "TotalCnt";

	/**
	 * @Method : createCountResultMaps
	 * @CreateDate : 2021. 4. 19.
	 * @param ms
	 * @return
	 * @Description : COUNT QUERY 결과를 받기위한 ResultMaps 생성
	 */
	private List<ResultMap> createCountResultMaps(MappedStatement ms) {
		List<ResultMap> countResultMaps = new ArrayList<>();

		ResultMap countResultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId() + countIdSuffix,
				Long.class, new ArrayList<>()).build();
		countResultMaps.add(countResultMap);

		return countResultMaps;
	}

	/**
	 * @Method : createCountMappedStatement
	 * @CreateDate : 2021. 4. 19.
	 * @param ms
	 * @return
	 * @Description : COUNT QUERY 결과를 받기위한 MappedStatement 생성 속도문제로 개선필요 시 간단히 Map으로
	 *              캐싱처리해도 될듯
	 */
	private MappedStatement createCountMappedStatement(MappedStatement ms) {
		List<ResultMap> countResultMaps = createCountResultMaps(ms);
		return new MappedStatement.Builder(ms.getConfiguration(), ms.getId() + countIdSuffix, ms.getSqlSource(),
				ms.getSqlCommandType()).resource(ms.getResource()).parameterMap(ms.getParameterMap())
				.resultMaps(countResultMaps).fetchSize(ms.getFetchSize()).timeout(ms.getTimeout())
				.statementType(ms.getStatementType()).resultSetType(ms.getResultSetType()).cache(ms.getCache())
				.flushCacheRequired(ms.isFlushCacheRequired()).useCache(true)
				.resultOrdered(ms.isResultOrdered()).keyGenerator(ms.getKeyGenerator())
				.keyColumn(ms.getKeyColumns() != null ? String.join(",", ms.getKeyColumns()) : null)
				.keyProperty(ms.getKeyProperties() != null ? String.join(",", ms.getKeyProperties()) : null)
				.databaseId(ms.getDatabaseId()).lang(ms.getLang())
				.resultSets(ms.getResultSets() != null ? String.join(",", ms.getResultSets()) : null).build();
	}

	/**
	 *
	 * @param invocation Invocation
	 * @return Object
	 * @throws Throwable
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object intercept(Invocation invocation) throws Throwable {
		try {
			if (invocation.getArgs()[2] instanceof PageInfo pageInfo) {

				MappedStatement originalMappedStatement = (MappedStatement) invocation.getArgs()[0];
				MappedStatement countMappedStatement = createCountMappedStatement(originalMappedStatement);
				// COUNT 구하기
				invocation.getArgs()[0] = countMappedStatement;
				List<Long> totalCount = (List<Long>) invocation.proceed();
				pageInfo.setTotCount(totalCount.get(0));
				if (pageInfo.getTotCount() % pageInfo.getRowPerPage() == 0) {
					pageInfo.setTotPage(
							Integer.parseInt(String.valueOf(pageInfo.getTotCount() / pageInfo.getRowPerPage())));
				} else {
					pageInfo.setTotPage(
							Integer.parseInt(String.valueOf(pageInfo.getTotCount() / pageInfo.getRowPerPage())) + 1);
				}

				pageInfo.setIsFirstPage(pageInfo.getPageNo() == 1);
				pageInfo.setIsLastPage(pageInfo.getPageNo().equals(pageInfo.getTotPage()));

				// LIST 구하기
				List<Object> list = new ArrayList<>();
				if (pageInfo.getTotCount() == 0) {
					return list;
				}
				invocation.getArgs()[0] = originalMappedStatement;
				list = (List<Object>) invocation.proceed();
				return list;
			}
		} catch (ClassCastException e) {
			log.error("error:", e);
		}

		return invocation.proceed();
	}

}
