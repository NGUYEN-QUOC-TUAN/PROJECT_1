<%@ page contentType="text/html;charset=UTF-8"%>

<%
String ctx = request.getContextPath();
String extLibPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/ext4";
pageContext.setAttribute("extLibPath", extLibPath);
pageContext.setAttribute("ctx", ctx);
%>
<script type="text/javascript">
var ctx = "${ctx}";
</script>

<script type="text/javascript" src="${ctx}/include/jquery-2.1.3.min.js"></script>
<script type="text/javascript" src="${ctx}/include/jquery.message.min.js"></script>
<script type="text/javascript" src="${ctx}/include/jquery-validate.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/include/jquery.message.css"/>

<script type="text/javascript" src="${ctx}/include/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctx}/include/cookieUtils.js"></script>
<script type="text/javascript">
//全局的ajax访问，处理ajax清求时sesion超时  
$.ajaxSetup({   
	contentType:"application/x-www-form-urlencoded;charset=utf-8",   
	complete:function(XMLHttpRequest,textStatus){
		var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
		if(sessionstatus != 'undefined' &&　sessionstatus　!= null　){
			console.log(sessionstatus);
			if(parent!=null){
				parent.window.location= 'login.html';
			}
			window.location = ctx + "/login.html";
		}
	}
});  
</script>
