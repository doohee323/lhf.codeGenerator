package ${class.packageName}.controller;

import java.util.List;
import java.util.Map;
import lhf.basis.data.GridData;
import lhf.extend.core.mvc.LhfRequest;
import lhf.extend.core.mvc.LhfResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ${class.packageName}.domain.${class.name?cap_first};
import ${class.packageName}.service.${class.programId?upper_case}Service;

<#include "./_JavaTemplates.ftl">

@Controller
@RequestMapping("${urlMapping}")
public class ${class.programId?upper_case}Controller {

	/**
	 * 로그처리를 위한 logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(${class.programId?upper_case}Controller.class);
	/**
	 * getLogger
	 */
	public Logger getLogger(){
		return logger;
	}
	/**
	 * Service 클래스 이용하기위한 변수
	 */
	@Autowired
	private ${class.programId?upper_case}Service service;

	/**
	 * <pre>
	 *  코드를 조회한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveCodeList.*")
	public void retrieveCodeList(LhfRequest request, LhfResponse response){
		//TODO 각화면 load 시 필요한 코드(공통코드(CO_COMM_CD) 테이블제외) 리스트 조회 로직
	}

	/**
	 * <pre>
	 *  ${class.tableDesc} 한건을 조회한다.
	 * </pre>
	 * @param request
	 * @param
	 */
	@RequestMapping("retrieve${class.name?cap_first}.*")
	public void retrieve${class.name?cap_first}(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		${class.name?cap_first} ${class.name?uncap_first} = service.retrieve${class.name?cap_first}(requestMap);

		response.set("ds_${class.name?cap_first}", ${class.name?uncap_first}, ${class.name?cap_first}.class);
	}

	/**
	 * <pre>
	 *  ${class.tableDesc} 조건에 의한 자료를  조회한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieve${class.name?cap_first}List.*")
	public void retrieve${class.name?cap_first}List(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		List<${class.name?cap_first}> ${class.name?uncap_first}List = service.retrieve${class.name?cap_first}List(requestMap);

		response.setList("ds_${class.name?cap_first}", ${class.name?uncap_first}List, ${class.name?cap_first}.class);
	}

	/**
	 * <pre>
	 *  ${class.tableDesc} GRID DATA 추가/삭제/수정 한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("save${class.name?cap_first}List.*")
	public void save${class.name?cap_first}List(LhfRequest request, LhfResponse response){
		GridData<${class.name?cap_first}> gridData  = request.getGridData("ds_${class.name?cap_first}", ${class.name?cap_first}.class);
		service.save${class.name?cap_first}List(gridData);
	}

	/**
	 * <pre>
	 *  ${class.tableDesc} 조건에 의한 자료를 삭제한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("remove${class.name?cap_first}List.*")
	public void remove${class.name?cap_first}List(LhfRequest request, LhfResponse response){
		Map<String, Object> requestMap = request.getParam();

		service.remove${class.name?cap_first}List(requestMap);
	}
}
