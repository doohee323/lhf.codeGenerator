package com.lhf.md.mdmm.mdmm10.service;

import java.util.List;
import java.util.Map;

import lhf.basis.data.GridData;
import lhf.extend.query.CommonDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lhf.md.mdmm.mdmm10.domain.Mdmm;

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

@Service
public class MDMM1010Service {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(MDMM1010Service.class);
	/**
	 * getLogger
	 */
	public Logger getLogger(){
		return logger;
	}
	/**
	 * DB처리를 위한  공통 dao
	 */
	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	/**
	 * <pre>
	 * 를 단건을 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return 
	 */
	public Mdmm retrieveMdmm(Map<String, Object> requestMap) {
		return commonDao.queryForObject("MDMM1010.retrieveMdmm", requestMap,Mdmm.class);
	}

	/**
	 * <pre>
	 * 를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return 리스트
	 */
	public List<Mdmm> retrieveMdmmList(Map<String, Object> requestMap) {
		return commonDao.queryForList("MDMM1010.retrieveMdmmList", requestMap,Mdmm.class);
	}
		
	/**
	 * <pre>
	 * 를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> retrieveMdmmList(Map<String, Object> requestMap) {
		return commonDao.queryForMapList("MDMM1010.retrieveMdmmList");
	}

	/**
	 * <pre>
	 *  LIST를 저장
	 * </pre>
	 * @param gridData
	 */
	public void saveMdmmList(GridData<Mdmm> gridData) {

		gridData.forEachRow(new lhf.extend.query.callback.AbstractRowStatusCallback<Mdmm>() {
            //필요한경우만 아래 주석을 풀어서 사용하세요

			///**
			// * normal 처리인경우
			// */
			//@Override
			//public void normal(Mdmm record, int rowNum) {
			//}

			///**
			// * 추가
			// */
			//@Override
			//public void insert(Mdmm record, int rowNum) {
			//	record.setFstRegUserId(UserInfo.getUserId());// 최초등록자
			//	record.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("MDMM1010.insertMdmm", record);
			//}

			///**
			// * 수정
			// */
			//@Override
			//public void update(Mdmm updatedRecord, Mdmm oldRecord, int rowNum) {
			//  updatedRecord.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("MDMM1010.updateMdmm", updatedRecord);
			//}

			///**
			// * 삭제
			// */
			//@Override
			//public void delete(Mdmm record, int rowNum) {
			//	commonDao.update("MDMM1010.deleteMdmm", record);
			//}
		});

	}

	/**
	 * <pre>
	 * 을 조건에 의한 삭제
	 * </pre>
	 * @param requestMap
	 */
	public void removeMdmmList(Map<String, Object> requestMap) {
		commonDao.update("MDMM1010.deleteMdmmList", requestMap);
	}
}
