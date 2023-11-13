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

<link rel="stylesheet" type="text/css" href="${ctx}/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/grid.css" />

<script type="text/javascript" src="${ctx}/extjs/ext-all.js"></script>
<script type="text/javascript" src="${ctx}/include/base.js"></script>
<script type="text/javascript" src="${ctx}/extjs/ext-locale-en.js"></script>
