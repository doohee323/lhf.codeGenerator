var config = null;
var Config = function(){
	this.get = get;
};

function get() {
	if(config) {
		return config;
	} else {
		var nconf = require('nconf');
		nconf.use('file', { file: '../support/config.json' });
//		nconf.use('file', { file: './lhf.launcher/support/config.json' });
		nconf.load();
		config = nconf.get('lhfConf');
		console.log('~~~' + config);
//		var lhfConf = nconf.get('lhfConf');
//		console.log(lhfConf);
	}
	
	return config;
}

//nconf.set('name', 'Avian');
//nconf.set('dessert:name', 'Ice Cream');
//nconf.set('dessert:flavor', 'chocolate');

//console.log(nconf.get('dessert'));
//var dessert = nconf.get('dessert');
//console.log(dessert);
//console.log(dessert.name);
//
//nconf.save(function (err) {
//    if (err) {
//        console.error(err.message);
//        return;
//    }
//    console.log('Configuration saved successfully.');
//});

module.exports = Config;