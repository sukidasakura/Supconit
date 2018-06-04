<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>

<body>
	<table>
		<!--pageContext.request.contextPath：jsp取得绝对路径的方法，取出部署的应用程序名或当前的项目名称， 如“/SpringHDFS”-->
		<td><a href="${pageContext.request.contextPath}/hdfs/ls.do" style="color:#666666;text-decoration:none;">
		               查看目录</a></td>
		<td><a href=/HDFSWEB/upload.jsp style="color:#666666;text-decoration:none;">上传文件</a></td>
		
	</table>
</body>
</html>