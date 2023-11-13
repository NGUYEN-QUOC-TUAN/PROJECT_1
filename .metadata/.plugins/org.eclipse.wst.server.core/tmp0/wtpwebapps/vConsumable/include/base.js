Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this);   

function checkUserSessionStatus(conn,response,options) {  
  if (typeof response.getResponseHeader('sessionstatus') != 'undefined') {
        	
    if(parent!=null){
      parent.window.location= 'login.html';
    }
        	
    window.location = "login.html";   
  }
  
}

function getProxy(url){
	return {
 		type : 'rest',
 		getMethod: function(){ return 'POST'; },
 		url : url,
 		reader : {
 			type : 'json',
 			root : 'rows',
 			totalProperty : 'total'// 数据的总数
 		}
 	};
}

getSyncStore = function(storeUrl , fields){
	var data;
	sendAjax({
		params: {},
		async : false,
        url: storeUrl,
		callback : function(resData){
			if(resData.success){
                data=resData.rows;
			}else{
				Ext.MessageBox.alert("CONSUMABLE NOTE", resData.text);
			}
		}
	});
	
	return Ext.create('Ext.data.Store', {
	    fields: fields,
	    data :data
	});
}

//封装、简化AJAX
var sendAjax = function(config) { // 封装、简化AJAX
	Ext.Ajax.request({
		url : config.url, // 请求地址
		async : config.async,
		params : config.params, // 请求参数
		method : 'post', // 方法
		callback : function(options, success, response) {
			if(Ext.isDefined(config.callback)){
				var text=response.responseText;
				if(Ext.isObject(text)){
					config.callback(text);
				}else{
					var callbackData ;
					try{
						callbackData = Ext.JSON.decode(text);
					} catch (e) {
						callbackData = { success : false,text:"Server error, operation failed! Please contact MIS"};
					}
					config.callback(callbackData);
				}
			}else{
				//alert('没有回调函数');
			}
		}
	});
	return false;
};