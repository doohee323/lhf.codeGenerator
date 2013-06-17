package com.lhf.sy.sya.sya01.controller;

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

import com.lhf.sy.sya.sya01.domain.CoUser;
import com.lhf.sy.sya.sya01.service.SYA0101Service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : SYA0101
 * 설    명 :  처리를 위한 클래스
 * 작 성 자 :
 * 작성일자 : 2013/06/14
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013/06/14             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */

@Controller
@RequestMapping("/sya/sya01/sya0101/*")
public class SYA0101Controller {

	/**
	 * 로그처리를 위한 logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(SYA0101Controller.class);
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
	private SYA0101Service service;

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
	 *   한건을 조회한다.
	 * </pre>
	 * @param request
	 * @param
	 */
	@RequestMapping("retrieveCoUser.*")
	public void retrieveCoUser(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		CoUser coUser = service.retrieveCoUser(requestMap);

		response.set("ds_CoUser", coUser, CoUser.class);
	}

	/**
	 * <pre>
	 *   조건에 의한 자료를  조회한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveCoUserList.*")
	public void retrieveCoUserList(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		List<CoUser> coUserList = service.retrieveCoUserList(requestMap);

		response.setList("ds_CoUser", coUserList, CoUser.class);
	}

	/**
	 * <pre>
	 *   GRID DATA 추가/삭제/수정 한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("saveCoUserList.*")
	public void saveCoUserList(LhfRequest request, LhfResponse response){
		GridData<CoUser> gridData  = request.getGridData("ds_CoUser", CoUser.class);
		service.saveCoUserList(gridData);
	}

	/**
	 * <pre>
	 *   조건에 의한 자료를 삭제한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("removeCoUserList.*")
	public void removeCoUserList(LhfRequest request, LhfResponse response){
		Map<String, Object> requestMap = request.getParam();

		service.removeCoUserList(requestMap);
	}
}
