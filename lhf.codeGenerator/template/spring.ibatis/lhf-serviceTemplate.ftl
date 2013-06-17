package ${class.packageName}.service;

import java.util.List;
import java.util.Map;

import lhf.basis.data.GridData;
import lhf.extend.query.CommonDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ${class.packageName}.domain.${class.name?cap_first};

<#include "./_JavaTemplates.ftl">

@Service
public class ${class.programId?upper_case}Service {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(${class.programId?upper_case}Service.class);
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
	 * ${class.tableDesc}를 단건을 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return ${class.tableDesc}
	 */
	public ${class.name?cap_first} retrieve${class.name?cap_first}(Map<String, Object> requestMap) {
		return commonDao.queryForObject("${class.programId?upper_case}.retrieve${class.name?cap_first}", requestMap,${class.name?cap_first}.class);
	}

	/**
	 * <pre>
	 * ${class.tableDesc}를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return ${class.tableDesc}리스트
	 */
	public List<${class.name?cap_first}> retrieve${class.name?cap_first}List(Map<String, Object> requestMap) {
		return commonDao.queryForList("${class.programId?upper_case}.retrieve${class.name?cap_first}List", requestMap,${class.name?cap_first}.class);
	}
		
	/**
	 * <pre>
	 * ${class.tableDesc}를 조건에 의한 자료 조회한다.
	 * </pre>
	 * @param requestMap
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> retrieve${class.name?cap_first}List(Map<String, Object> requestMap) {
		return commonDao.queryForMapList("${class.programId?upper_case}.retrieve${class.name?cap_first}List");
	}

	/**
	 * <pre>
	 * ${class.tableDesc} LIST를 저장
	 * </pre>
	 * @param gridData
	 */
	public void save${class.name?cap_first}List(GridData<${class.name?cap_first}> gridData) {

		gridData.forEachRow(new lhf.extend.query.callback.AbstractRowStatusCallback<${class.name?cap_first}>() {
            //필요한경우만 아래 주석을 풀어서 사용하세요

			///**
			// * normal 처리인경우
			// */
			//@Override
			//public void normal(${class.name?cap_first} record, int rowNum) {
			//}

			///**
			// * 추가
			// */
			//@Override
			//public void insert(${class.name?cap_first} record, int rowNum) {
			//	record.setFstRegUserId(UserInfo.getUserId());// 최초등록자
			//	record.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("${class.programId?upper_case}.insert${class.name?cap_first}", record);
			//}

			///**
			// * 수정
			// */
			//@Override
			//public void update(${class.name?cap_first} updatedRecord, ${class.name?cap_first} oldRecord, int rowNum) {
			//  updatedRecord.setFnlEditUserId(UserInfo.getUserId());// 최종수정자
			//	commonDao.update("${class.programId?upper_case}.update${class.name?cap_first}", updatedRecord);
			//}

			///**
			// * 삭제
			// */
			//@Override
			//public void delete(${class.name?cap_first} record, int rowNum) {
			//	commonDao.update("${class.programId?upper_case}.delete${class.name?cap_first}", record);
			//}
		});

	}

	/**
	 * <pre>
	 * ${class.tableDesc}을 조건에 의한 삭제
	 * </pre>
	 * @param requestMap
	 */
	public void remove${class.name?cap_first}List(Map<String, Object> requestMap) {
		commonDao.update("${class.programId?upper_case}.delete${class.name?cap_first}List", requestMap);
	}
}
