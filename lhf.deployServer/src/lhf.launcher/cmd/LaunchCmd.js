var argList = process.argv.splice(2);
main(argList);

function main(argList) {
	var Launch = require('../service/Launch.js');
	// ���� ó��
	var stage = '';
	var cmd = '';

	if (argList.length > 0) {
		stage = argList[0];
	}

	if (argList.length > 1) {
		cmd = argList[1];
	}
	// 1) ���� ȣ�� �׽�Ʈ
//	 stage = 'execRemote';
//	 cmd = 'all';
	// 2) ������ ȣ�� �׽�Ʈ
//	stage = 'win';
//	cmd = 'deploy';
	var service = new Launch();
	service.runMe(stage, cmd);
}
