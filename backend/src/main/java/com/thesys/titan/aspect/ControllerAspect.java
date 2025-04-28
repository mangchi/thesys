package com.thesys.titan.aspect;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.thesys.titan.annotation.Page;
import com.thesys.titan.common.vo.CommonDto;
import com.thesys.titan.common.vo.PageInfo;
import com.thesys.titan.constants.TitanConstants;
import com.thesys.titan.exception.BizException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Aspect
public class ControllerAspect {

	@Pointcut("execution(* com.thesys.titan..*.*Controller.*(..))")
	public void commonPointcut() {
	}

	@Before("commonPointcut()")
	public void callBeforeController(JoinPoint joinPoint) throws Throwable {
		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Page page = methodSignature.getMethod().getAnnotation(Page.class);

			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof HttpServletRequest) {
					handleHttpServletRequest(methodSignature);
				} else if (obj instanceof Map<?, ?>) {
					@SuppressWarnings("unchecked")
					Map<String, Object> paramMap = (Map<String, Object>) obj;
					handleMapArgument(paramMap, page);
				} else if (obj instanceof CommonDto commondto) {
					handleCommonDto(commondto);
				} else if (obj instanceof List) {
					handleListArgument((List<?>) obj);
				}
			}
			if (null != joinPoint.getArgs() && joinPoint.getArgs().length > 0) {
				log.debug("ControllerAspect param:{}", joinPoint.getArgs()[0]);
			}
		} catch (Exception e) {
			log.error("ControllerAspect error:", e);
		}
	}

	private void handleHttpServletRequest(MethodSignature methodSignature) {
		if (!methodSignature.getName().equals("logout")) {
			// Add logic if needed
		}
	}

	private void handleMapArgument(Map<String, Object> param, Page page) {
		if (page != null && param.containsKey(TitanConstants.PAGE_NO)
				&& param.containsKey(TitanConstants.ROW_PER_PAGE)) {
			int startRow = Integer.parseInt(String.valueOf(param.get(TitanConstants.ROW_PER_PAGE)))
					* (Integer.parseInt(String.valueOf(param.get(TitanConstants.PAGE_NO))) - 1);
			param.put("startRow", startRow);
			PageInfo pageInfo = PageInfo.builder()
					.pageNo(Integer.parseInt(String.valueOf(param.get(TitanConstants.PAGE_NO))))
					.rowPerPage(Integer.parseInt(String.valueOf(param.get(TitanConstants.ROW_PER_PAGE))))
					.orderBy(page.sort().equals("ASC") ? "DESC" : "ASC").build();
			pageInfo.setStartRow(startRow);
			param.put(TitanConstants.PAGE_INFO, pageInfo);
		}
	}

	private void handleCommonDto(CommonDto obj) {
		Class<?> superClass = obj.getClass().getSuperclass();
		Field[] fields = superClass.getDeclaredFields();
		for (Field f : fields) {
			if ((f.getName().equals("createdId") || f.getName().equals("updateId"))
					&& obj instanceof CommonDto commonDto) {
				if (f.getName().equals("createdId")) {
					commonDto.setCreatedId("admin");
				} else if (f.getName().equals("updateId")) {
					commonDto.setUpdateId("admin");
				}
			}
		}
	}

	private void handleListArgument(List<?> list) {
		for (Object o : list) {
			if (o instanceof CommonDto commondto) {
				handleCommonDto(commondto);
			}
		}
	}

	@AfterReturning(pointcut = "commonPointcut()", returning = "returnValue")
	public void callAfterController(JoinPoint joinPoint, Object returnValue) throws BizException, Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		try {
			handleHttpServletRequestArgs(joinPoint);
			if (returnValue instanceof ResponseEntity<?> responseEntity
					&& responseEntity.getBody() instanceof Map<?, ?>) {
				@SuppressWarnings("unchecked")
				ResponseEntity<Map<String, Object>> safeResponseEntity = (ResponseEntity<Map<String, Object>>) responseEntity;
				handleResponseEntity(joinPoint, methodSignature, safeResponseEntity);
			}
			// if (returnValue instanceof ModelAndView modelandview) {
			// handleModelAndView(modelandview);
			// }
		} catch (Exception e) {
			log.debug("error:{}", (Object[]) e.getStackTrace());
		} finally {
			log.debug("return:{}", returnValue);
		}
	}

	private void handleHttpServletRequestArgs(JoinPoint joinPoint) {
		for (Object obj : joinPoint.getArgs()) {
			if (obj instanceof HttpServletRequest) {
				// Add logic if needed
			}
		}
	}

	private void handleResponseEntity(JoinPoint joinPoint, MethodSignature methodSignature,
			ResponseEntity<Map<String, Object>> res) {
		Page page = methodSignature.getMethod().getAnnotation(Page.class);
		Map<String, Object> map = res.getBody();
		if (page != null) {
			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof Map<?, ?> paramMap && map != null && paramMap instanceof Map<?, ?> safeParamMap &&
						safeParamMap.keySet().stream().allMatch(String.class::isInstance) &&
						safeParamMap.values().stream().allMatch(Object.class::isInstance)) {
					@SuppressWarnings("unchecked")
					Map<String, Object> castedParamMap = (Map<String, Object>) safeParamMap;
					updatePageInfo(castedParamMap, map);
				}
			}
		}
	}

	private void updatePageInfo(Map<String, Object> param, Map<String, Object> map) {
		if (param.containsKey(TitanConstants.PAGE_INFO)) {
			PageInfo pageInfo = (PageInfo) param.get(TitanConstants.PAGE_INFO);
			int totalPages = calculateTotalPages(pageInfo);
			pageInfo.setTotPage(totalPages);
			pageInfo.setIsFirstPage(pageInfo.getPageNo() == 1);
			pageInfo.setIsLastPage(pageInfo.getPageNo() == totalPages);
			pageInfo.setIsFirstPerPage(pageInfo.getPageNo() > 10);
			map.put(TitanConstants.PAGE_INFO, pageInfo);
		}
	}

	private int calculateTotalPages(PageInfo pageInfo) {
		return (pageInfo.getTotCount() % pageInfo.getRowPerPage() == 0)
				? (int) (pageInfo.getTotCount() / pageInfo.getRowPerPage())
				: (int) (pageInfo.getTotCount() / pageInfo.getRowPerPage()) + 1;
	}

	// private void handleModelAndView(ModelAndView modelandview) {
	// // Add logic if needed
	// }

	@AfterThrowing(pointcut = "commonPointcut()", throwing = "ex")
	public void errorInterceptor(JoinPoint joinPoint, Throwable ex) throws Exception {
		log.debug("error:{}", (Object[]) ex.getStackTrace());
	}

}