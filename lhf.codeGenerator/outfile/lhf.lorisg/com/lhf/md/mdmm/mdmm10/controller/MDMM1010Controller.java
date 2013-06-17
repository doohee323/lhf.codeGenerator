package com.lhf.md.mdmm.mdmm10.controller;

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

import com.lhf.md.mdmm.mdmm10.domain.Mdmm;
import com.lhf.md.mdmm.mdmm10.service.MDMM1010Service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MDMM1010
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
@RequestMapping("/mdmm/mdmm10/mdmm1010/*")
public class MDMM1010Controller {

	/**
	 * 로그처리를 위한 logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(MDMM1010Controller.class);
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
	private MDMM1010Service service;

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
	@RequestMapping("retrieveMdmm.*")
	public void retrieveMdmm(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		Mdmm mdmm = service.retrieveMdmm(requestMap);

		response.set("ds_Mdmm", mdmm, Mdmm.class);
	}

	/**
	 * <pre>
	 *   조건에 의한 자료를  조회한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveMdmmList.*")
	public void retrieveMdmmList(LhfRequest request, LhfResponse response){
	    Map<String, Object> requestMap = request.getParam();

		List<Mdmm> mdmmList = service.retrieveMdmmList(requestMap);

		response.setList("ds_Mdmm", mdmmList, Mdmm.class);
	}

	/**
	 * <pre>
	 *   GRID DATA 추가/삭제/수정 한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("saveMdmmList.*")
	public void saveMdmmList(LhfRequest request, LhfResponse response){
		GridData<Mdmm> gridData  = request.getGridData("ds_Mdmm", Mdmm.class);
		service.saveMdmmList(gridData);
	}

	/**
	 * <pre>
	 *   조건에 의한 자료를 삭제한다.
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("removeMdmmList.*")
	public void removeMdmmList(LhfRequest request, LhfResponse response){
		Map<String, Object> requestMap = request.getParam();

		service.removeMdmmList(requestMap);
	}
}
