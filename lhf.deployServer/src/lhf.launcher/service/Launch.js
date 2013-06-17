var config = null;
var util = null;
var os = null;

var Launch = function() {
	this.runMe = runMe;

	var osm = require('os');
	os = osm.platform();

	if (os.indexOf('win') > -1) {
		os = "win";
	} else {
		os = "linux";
	}

	var Config = require('../support/Config.js');
	var service = new Config();
	config = service.get();
};

function runMe(stage, cmd) {
	try {
		var fse = require('fs-extra');

		if (stage === null) {
			stage = 'execRemote';
		}
		if (cmd === null) {
			cmd = 'all';
		}
		console.log("os : " + os);
		console.log("param stage : " + stage);
		console.log("param cmd : " + cmd);

		config = eval("config." + stage);
		config.os = os;

		var LaunchUtil = require('../support/LaunchUtil.js');
		util = new LaunchUtil();
		util.setConfig(config);

		if (stage == 'execRemote') {
			console.log("[server] workingDir delete : " + config.zip.workingDir);
			fse.remove(config.zip.workingDir, function(err) {
				if (err) {
					console.log(err);
					throw err;
				}
				util.runZip('zip', function() {
					console.log("[server] execRemote call : " + cmd);
					if (cmd == "all") {
						var remoteList = config.all.remoteList;
						var lists = remoteList.split(",");
						for ( var i = 0; i < lists.length; i++) {
							execRemote(lists[i], util.sleep(10000));
						}
					} else {
						execRemote(null, util.sleep(10000));
					}
				});
			});
		} else if (cmd == "deploy") {
			util.runFtp('get', function() {
				console.log("[server] runFtp call : " + cmd);
				util.runZip('unzip', function() {
					console.log("[server] runZip call : " + cmd);
					serverExec("shutdown", function() {
						console.log("[agent] ${config.replace.targetDir} replaced by ${config.replace.sourceDir}");
						fse.copy(config.replace.sourceDir, config.replace.targetDir, function(err) {
							if (err) {
								console.log(err);
								throw err;
							}
							console.log("[agent] deleteDir : " + config.zip.targetDir);
							console.log("[agent] " + config.zip.workingDir + " renames to " + config.zip.targetDir);

							fse.remove(config.zip.targetDir, function(err) {
								if (err) {
									console.log(err);
									return;
								}
								fse.rename(config.zip.workingDir, config.zip.targetDir, function(err) {
									if (err) {
										console.log(err);
										return;
									}
									serverExec('startup');
									util.sleep(10000);
								});
							});
						});
					});
				});
			});
		} else if (cmd == "shutdown") {
			serverExec('shutdown');
		} else if (cmd == "startup") {
			serverExec('startup');
		}
	} catch (e) {
		console.log(e);
	}
}

// was ���� �⵿ ����
function serverExec(actionType, callback) {
	console.log("serverExec actionType : " + actionType);
	try {
		var command;
		var lists;
		if (actionType == 'startup') {
			command = config.exec.startup;
			lists = command.split(",");
			for ( var i = 0; i < lists.length; i++) {
				util.runExec(lists[i], util.sleep(10000));
			}
		} else if (actionType == 'shutdown') {
			command = config.exec.shutdown;
			console.log("[agent] shutdown : " + command);
			
			util.checkHttp(function(errCode) {
				console.log("[agent] return errCode : " + errCode);
				if (errCode > 0) {
					lists = command.split(",");
					for ( var i = 0; i < lists.length; i++) {
						util.runExec(lists[i], util.sleep(10000));
					}
				}
			});
		} else {
			console.log("[agent] actionType is empty!");
			return false;
		}
	} catch (e) {
		console.log(e);
	}
}

// ������ ������ ���� ����
function execRemote(remote, callback) {
	var serverIp = eval("config." + remote + ".serverIp");
	var userId = eval("config." + remote + ".userId");
	var password = eval("config." + remote + ".password");
	var remoteShell = eval("config." + remote + ".remoteShell");
	console.log("[server] serverIp : " + serverIp);
	console.log("[server] userId : " + userId);
	console.log("[server] password : " + password);
	console.log("[server] remoteShell : " + remoteShell);

	try {
		var net = require('net');
		var conn = net.createConnection(23, serverIp);
		conn.on("connection", function(socket) {
			socket.on("data", function(c) {
				var data = c + '';
				switch (data) {
				case 'login:':
					socket.write(userId);
					break;
				case 'assword:':
					socket.write(password);
					break;
				case '> ':
					var lists = remoteShell.split(",");
					for ( var i = 0; i < lists.length; i++) {
						socket.write(lists[i]);
						util.sleep(10000);
					}
					break;
				case 'Invalid Username!':
					break;
				default:
					break;
				}
			});
			socket.on("end", function() {
				callback();
			});
		});
	} catch (e) {
		console.log(e);
	}

	// 1) �������� ������ ���� ȣ��
	// net start "lorisg.deploy" // ������ ������ deploy ��ü ó��
	// net start "lorisg.start" // was �⵿
	// net start "lorisg.shutdown" // was ����

	// ���� �������� ������ ������ �������� ȣ���� �� ��� (���缭�� -> ������ ����)
	// C:\LHF_IDE\workspace\lhf.launcher\bat\laucher.bat

	// 2) bat������ ���񽺷� ����ϱ�
	// 2-1) resource toolkit ��ġ
	// http://www.microsoft.com/en-us/download/details.aspx?id=17657
	// 2-2) srvany.exe, instsrv.exe �� c:\windows\systems �� ����
	// 2-3) lorisg.deploy ���� ���
	// instsrv.exe lorisg.deploy "C:\Program Files (x86)\Windows Resource
	// Kits\Tools\srvany.exe"
	// instsrv.exe lorisg.start "C:\Program Files (x86)\Windows Resource
	// Kits\Tools\srvany.exe"
	// instsrv.exe lorisg.shutdown "C:\Program Files (x86)\Windows Resource
	// Kits\Tools\srvany.exe"
	// 2-4) ������Ʈ�� ����
	// - HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\lorisg.deploy
	// - Parameters Ű ����
	// - String �߰�
	// Application : C:/LHF_IDE/workspace/lhf.launcher/bat/laucher.bat
	// AppDirectory :
	// AppParameters : win deploy
	// - HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\lorisg.start
	// - Parameters Ű ����
	// - String �߰�
	// Application : C:\LHF_IDE\was\apache-tomcat-6.0.35\bin\startup.bat
	// AppDirectory :
	// AppParameters :
	// - HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\lorisg.shutdown
	// - Parameters Ű ����
	// - String �߰�
	// Application : C:\LHF_IDE\was\apache-tomcat-6.0.35\bin\shutdown.bat
	// AppDirectory :
	// AppParameters :

	// 3) ������ ���� ����
	// sc delete "lorisg.start"
	// sc delete "lorisg.shutdown"

	// * ����
	// http://www.ischo.net/?mid=board_windows&listStyle=webzine&document_srl=4288
	// http://publib.boulder.ibm.com/tividd/td/BSM/SC32-9130-00/en_US/HTML/bsmp112.htm
}

module.exports = Launch;
