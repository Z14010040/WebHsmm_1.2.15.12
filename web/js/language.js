window.language = '';
//保存主页面的回调函数
window.languageCallback;


function createXMLHttpRequest() {
	var request;

	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	}
	else if (window.ActiveXObject) {
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e) {
				return null;
			}
		}
	}

	return request;
}

function sendGetRequest(url, callback, loadCenter){
	var getRequest = createXMLHttpRequest();
	getRequest.open("GET", url, true);
	getRequest.send(null);
	getRequest.onreadystatechange = function() {
		//如果执行状态成功，那么就把返回信息写到指定的层里
		if (getRequest.readyState == 4 && getRequest.status == 200) {
			var res = getRequest.responseXML.getElementsByTagName("command");
			var sCommandStr = res[0].firstChild.data;
			if(sCommandStr == "GETLANGUAGE"){
				var res = getRequest.responseXML.getElementsByTagName("result");
				window.language = res[0].firstChild.data;
				//alert(window.language);
				jQuery.i18n.properties({
					name : 'strings', //资源文件名称
					path : '/i18n/', //资源文件路径
					mode : 'map', //用Map的方式使用资源文件中的值
					language : window.language,
					callback : callback
				});
				//是否加载center frame
				if(loadCenter){
					//frame center
					var center = top.frames["panel"];
					if(typeof(center) == "undefined"){
						return;
					}
					center.load();
				}
			} else {
				alert("您所请求的页面有异常.");
			}
		}
	}
}

function sendPostRequest(url, para){
	var postRequest = createXMLHttpRequest();
	postRequest.open("POST", url, true);
	//定义传输的文件HTTP头信息
	postRequest.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	//发送POST数据
	var postPara = "LANGUAGE=" + para;
	postRequest.send(postPara);
	//获取执行状态
	postRequest.onreadystatechange = function() {
		//如果执行状态成功，那么就把返回信息写到指定的层里
		if (postRequest.readyState == 4 && postRequest.status == 200) {
			var res = postRequest.responseXML.getElementsByTagName("result");
			//get language after set.
			//getLanguage();
			sendGetRequest("getLanguage.jsp", window.languageCallback, true);
		}
	}
}

function getLanguage(callback){
	sendGetRequest("getLanguage.jsp", callback, false);
}

function setLanguage(){
	var para;
	if(window.language == 'zh'){
		para = 'en';
	}else{
		para = 'zh';
	}
	//alert(para);
	sendPostRequest("setLanguage.jsp", para);
}




