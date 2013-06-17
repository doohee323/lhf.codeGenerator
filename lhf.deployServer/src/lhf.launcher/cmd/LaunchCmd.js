var argList = process.argv.splice(2);
main(argList);

function main(argList) {
	var Launch = require('../service/Launch.js');
	// 메인 처리
	var stage = '';
	var cmd = '';

	if (argList.length > 0) {
		stage = argList[0];
	}

	if (argList.length > 1) {
		cmd = argList[1];
	}
	// 1) 본사 호출 테스트
//	 stage = 'execRemote';
//	 cmd = 'all';
	// 2) 원격지 호출 테스트
//	stage = 'win';
//	cmd = 'deploy';
	var service = new Launch();
	service.runMe(stage, cmd);
}
