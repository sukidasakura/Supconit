<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false" %> 
<%String ref = request.getHeader("REFERER");%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<table>
	<tr>
		<td>文件名</td>
		<td>文件大小  </td>
		<td>拥有者 </td>
		<td>权限  </td>
		<td>时间  </td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;操作</td>
	</tr>
     <c:forEach var="file" items="${file}">
     <tr>
			 <td><a href="${pageContext.request.contextPath}/hdfs/ls.do?path=${file.fileName}" style="color:#666666;text-decoration:none;">
			      ${file.fileName}</a></td>
			 <td> ${file.fileSize}</td>	
			 <td> ${file.owner}</td>	
			 <td> ${file.permission}</td>	
			 <td> ${file.modificationTime}</td>	
			 
			 <td><a href="${pageContext.request.contextPath}/hdfs/delete.do?path=${file.fileName}" style="color:#666666;text-decoration:none;"
			  class="button border-dot button-little" onclick="return confirm('确认删除?')" >
			                   删除  </a>
			 &nbsp;&nbsp;
			 <a href="${pageContext.request.contextPath}/hdfs/download.do?path=${file.fileName}" style="color:#666666;text-decoration:none;">
			 下载</a></td>
    </tr>
	</c:forEach>
 <input type="button" value="返回"   onclick="javascript:window.location='<%=ref%>'" >
</table>
</body>
</html>