Ext.Ajax.on('requestcomplete',checkUserSessionStatus, this);   

function checkUserSessionStatus(conn,response,options) {  
  if (typeof response.getResponseHeader('sessionstatus') != 'undefined') {
        	
    if(parent!=null){
      parent.window.location= 'login.html';
    }
        	
    window.location = "login.html";   
  }
  
}