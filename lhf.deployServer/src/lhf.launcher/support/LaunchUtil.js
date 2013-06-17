var config = null;
var LaunchUtil = function() {
	this.setConfig = setConfig;
	this.runZip = runZip;
	this.runFtp = runFtp;
	this.runExec = runExec;
	this.sleep = sleep;
	this.checkHttp = checkHttp;
};

function setConfig(aconfig) {
	config = aconfig;
}

// 拘绵 颇老 贸府
function runZip(actionType, callback) {
	try {
		var fs = require('fs');
		var fse = require('fs-extra');
		var execFile = require('child_process');

		var fileName = config.zip.fileName;
		var command = null;
		if (actionType == 'zip') {
			var targetDir = config.zip.targetDir;
			console.log("[server] basedir: ${targetDir}");
			console.log("[server] destfile: ${fileName}");

			fse.remove(fileName, function(err) {
				if (err) {
					console.log(err);
				} else {
					console.log("remove success!");
					if (config.os == 'win') {
						command = '"C:\\Program Files (x86)\\7-Zip\\7z.exe" a ' + fileName + ' ' + targetDir;
					} else {
						command = 'unzip cvf ' + fileName + ' ' + targetDir;
					}
					console.log('command: ' + command);
					execFile.exec(command, function(error, stdout, stderr) {
						if (error) {
							console.log(error.stack);
							console.log('Error code: ' + error.code);
							console.log('Signal received: ' + error.signal);
						}
						callback();
					});
				}
			});
		} else {
			var workingDir = config.zip.workingDir;
			fs.exists(fileName, function(exists) {
				if (!exists) {
					console.log("fileName not exist!!! : ${config.zip.fileName}");
				} else {
					console.log("[agent] fileName: ${fileName}");
					console.log("[agent] destDir: ${workingDir}");
					var AdmZip = require('adm-zip');

					var zip = new AdmZip(fileName);
					// var zipEntries = zip.getEntries();
					//
					// zipEntries.forEach(function(zipEntry) {
					// console.log(zipEntry.toString());
					// });
					zip.extractAllTo(workingDir, true);

					console.log("[agent] finished unzip");
					callback();
				}
			});
		}
	} catch (e) {
		console.log(e);
	}
}

// FTP 颇老 贸府
function runFtp(actionType, callback) {
	try {
		var fse = require('fs-extra');
		var FTPClient = require('ftp');
		var fs = require('fs');

		var hostIp = config.ftp.hostIp;
		var userId = config.ftp.userId;
		var password = config.ftp.password;
		var localDir = config.ftp.localDir;
		var remoteDir = config.ftp.remoteDir;

		console.log("hostIp: " + hostIp);
		console.log("userId: " + userId);
		console.log("password: " + password);
		console.log("localDir: " + localDir);
		console.log("remoteDir: " + remoteDir);

		var conn = new FTPClient({
			host : hostIp
		});
		if (actionType == 'put') {
			console.log("[server] put ftp file from : " + localDir);
			console.log("[server] put ftp file to : " + remoteDir);

			conn.on('connect', function() {
				conn.auth(userId, password, function(e) {
					if (e)
						throw e;
					conn.put(localDir, remoteDir, function(e) {
						console.log("[server] put ftp file from : " + remoteDir);
						console.log("[server] put ftp file to : " + localDir);
						conn.end();
					});
				});
			});
		} else { // get
			fse.remove(localDir, function(err) {
				if (err) {
					console.error(err);
					throw err;
				}
				console.log("remove success!");

				var mkdirp = require('mkdirp');
				var tmp = localDir.substring(0, localDir.lastIndexOf('/'));
				mkdirp(tmp, function(err) {
					if (err) {
						console.error(err);
						throw err;
					}
					conn.on('ready', function(e) {
						if (e)
							throw e;
						conn.get(remoteDir, function(err, stream) {
							if (err)
								throw err;
							stream.once('close', function() {
								conn.end();
								console.log("[agent] get ftp file from : " + remoteDir);
								console.log("[agent] get ftp file to : " + localDir);
								callback();
							});
							stream.pipe(fs.createWriteStream(localDir));
						});
					});
				});
			});
		}
		conn.connect({
			"host" : hostIp,
			"port" : 21,
			"user" : userId,
			"password" : password
		});
	} catch (e) {
		console.log(e);
	}
}

// command 角青
function runExec(command, callback) {
	var execFile = require('child_process');

	execFile.exec(command, function(error, stdout, stderr) {
		if (error) {
			console.log(error.stack);
			console.log('Error code: ' + error.code);
			console.log('Signal received: ' + error.signal);
		}
		if (callback)
			callback();
	});
}

// was 辑滚 live check
function checkHttp(callback) {
	var errCode = 0;
	try {
		console.log("[agent] check out 1 : " + config.service.url);

		var http = require("http");
		var url = require('url');
		url = url.parse(config.service.url);

		var options = {
			hostname : url.hostname,
			port : url.port,
			path : url.path,
			method : 'GET'
		};

		var req = http.request(options, function(res) {
			res.setEncoding('utf8');
			console.log("statusCode: ", res.statusCode);
			console.log("headers: ", res.headers);

			res.on('data', function(d) {
//				console.log('BODY: ' + d);
				callback(res.statusCode);
				// errCode = res.statusCode;
			});
		});

		req.on('error', function(e) {
			console.error(e);
			callback(errCode);
		});

		req.end();
	} catch (e) {
		console.log(e);
		errCode = 0;
		callback(errCode);
	}
}

function sleep(second) {
	setTimeout((function() {
		console.log('sleep ~ ' + second);
	}), second);
}

module.exports = LaunchUtil;
