package com.lhf.sy.sya.sya01.service;

import java.util.List;
import java.util.Map;

import lhf.basis.data.GridData;
import lhf.extend.query.CommonDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lhf.sy.sya.sya01.domain.CoUser;

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

@Service
public class SYA0101Service {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(SYA0101Service.class);
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
	public CoUser retrieveCoUser(Map<String, Object> requestMap) {
		return commonDao.queryForObject("SYA0101.retrieveCoUser", requestMap,CoUser.class);
	}

	/**
	 * <pre>
	 * 를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return 리스트
	 */
	public List<CoUser> retrieveCoUserList(Map<String, Object> requestMap) {
		return commonDao.queryForList("SYA0101.retrieveCoUserList", requestMap,CoUser.class);
	}
		
	/**
	 * <pre>
	 * 를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> retrieveCoUserList(Map<String, Object> requestMap) {
		return commonDao.queryForMapList("SYA0101.retrieveCoUserList");
	}

	/**
	 * <pre>
	 *  LIST를 저장
	 * </pre>
	 * @param gridData
	 */
	public void saveCoUserList(GridData<CoUser> gridData) {

		gridData.forEachRow(new lhf.extend.query.callback.AbstractRowStatusCallback<CoUser>() {
            //필요한경우만 아래 주석을 풀어서 사용하세요

			///**
			// * normal 처리인경우
			// */
			//@Override
			//public void normal(CoUser record, int rowNum) {
			//}

			///**
			// * 추가
			// */
			//@Override
			//public void insert(CoUser record, int rowNum) {
			//	record.setFstRegUserId(UserInfo.getUserId());// 최초등록자
			//	record.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("SYA0101.insertCoUser", record);
			//}

			///**
			// * 수정
			// */
			//@Override
			//public void update(CoUser updatedRecord, CoUser oldRecord, int rowNum) {
			//  updatedRecord.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("SYA0101.updateCoUser", updatedRecord);
			//}

			///**
			// * 삭제
			// */
			//@Override
			//public void delete(CoUser record, int rowNum) {
			//	commonDao.update("SYA0101.deleteCoUser", record);
			//}
		});

	}

	/**
	 * <pre>
	 * 을 조건에 의한 삭제
	 * </pre>
	 * @param requestMap
	 */
	public void removeCoUserList(Map<String, Object> requestMap) {
		commonDao.update("SYA0101.deleteCoUserList", requestMap);
	}
}
