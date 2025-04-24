package com.thesys.titan.aspect;

import com.thesys.titan.annotation.Page;
import com.thesys.titan.common.vo.CommonDto;
import com.thesys.titan.common.vo.PageInfo;
import com.thesys.titan.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.Field;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Aspect
public class ControllerAspect {

	@Pointcut("execution(* com.thesys.titan..*.*Controller.*(..))")
	public void commonPointcut() {
	}

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@SuppressWarnings("unchecked")
	@Before("commonPointcut()")
	public void callBeforeController(JoinPoint joinPoint) throws Throwable {
		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Page page = methodSignature.getMethod().getAnnotation(Page.class);

			for (Object obj : joinPoint.getArgs()) {
				if (obj instanceof HttpServletRequest) {
					handleHttpServletRequest(methodSignature);
				} else if (obj instanceof Map) {
					handleMapArgument((Map<String, Object>) obj, page);
				} else if (obj instanceof CommonDto commondto) {
					handleCommonDto(commondto);
				} else if (obj instanceof List) {
					handleListArgument((List<?>) obj);
				}
			}

			log.debug(methodSignature.getName() + " param:{}", joinPoint.getArgs());
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
		if (page != null && param.containsKey("pageNo") && param.containsKey("rowPerPage")) {
			int startRow = Integer.parseInt(String.valueOf(param.get("rowPerPage")))
					* (Integer.parseInt(String.valueOf(param.get("pageNo"))) - 1);
			param.put("startRow", startRow);
			PageInfo pageInfo = PageInfo.builder()
					.pageNo(Integer.parseInt(String.valueOf(param.get("pageNo"))))
					.rowPerPage(Integer.parseInt(String.valueOf(param.get("rowPerPage"))))
					.orderBy(page.sort().equals("ASC") ? "DESC" : "ASC").build();
			pageInfo.setStartRow(startRow);
			param.put("pageInfo", pageInfo);
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

	@SuppressWarnings("unchecked")
	@AfterReturning(pointcut = "commonPointcut()", returning = "returnValue")
	public void callAfterController(JoinPoint joinPoint, Object returnValue) throws BizException, Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		try {
			handleHttpServletRequestArgs(joinPoint);
			if (returnValue instanceof ResponseEntity) {
				handleResponseEntity(joinPoint, methodSignature, (ResponseEntity<Map<String, Object>>) returnValue);
			}
			if (returnValue instanceof ModelAndView modelandview) {
				handleModelAndView(modelandview);
			}
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
				if (obj instanceof Map<?, ?> paramMap && map != null) {
					if (paramMap instanceof Map<?, ?> safeParamMap) {
						updatePageInfo((Map<String, Object>) safeParamMap, map);
					}
				}
			}
		}
	}

	private void updatePageInfo(Map<String, Object> param, Map<String, Object> map) {
		if (param.containsKey("pageInfo")) {
			PageInfo pageInfo = (PageInfo) param.get("pageInfo");
			int totalPages = calculateTotalPages(pageInfo);
			pageInfo.setTotPage(totalPages);
			pageInfo.setIsFirstPage(pageInfo.getPageNo() == 1);
			pageInfo.setIsLastPage(pageInfo.getPageNo() == totalPages);
			pageInfo.setIsFirstPerPage(pageInfo.getPageNo() > 10);
			map.put("pageInfo", pageInfo);
		}
	}

	private int calculateTotalPages(PageInfo pageInfo) {
		return (pageInfo.getTotCount() % pageInfo.getRowPerPage() == 0)
				? (int) (pageInfo.getTotCount() / pageInfo.getRowPerPage())
				: (int) (pageInfo.getTotCount() / pageInfo.getRowPerPage()) + 1;
	}

	private void handleModelAndView(ModelAndView modelandview) {
		// Add logic if needed
	}

	@AfterThrowing(pointcut = "commonPointcut()", throwing = "ex")
	public void errorInterceptor(JoinPoint joinPoint, Throwable ex) throws Exception {
		log.debug("error:{}", (Object[]) ex.getStackTrace());
	}

}