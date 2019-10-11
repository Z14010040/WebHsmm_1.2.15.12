<%@ page language="java" import="java.io.*" pageEncoding="UTF-8"%>
<%
	response.setContentType("text/html");
	request.setCharacterEncoding("utf-8");
	String fileName = "swhsmbak.dat";
	String Originalname = "/var/swhsm/download/swhsmbak.dat";
	response.setContentType("application/octet-stream");
	response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
	ServletOutputStream out1;
	try {
		out1 = response.getOutputStream();
		BufferedOutputStream bufferOut = new BufferedOutputStream(out1);
		FileInputStream inputStream = new FileInputStream(Originalname);
		BufferedInputStream bufferInput = new BufferedInputStream(inputStream);
		byte[] buffer = new byte[5 * 1024];
		int length = 0;
		while ((length = bufferInput.read(buffer)) > 0) {
			bufferOut.write(buffer, 0, length);
		}
		bufferInput.close();
		inputStream.close();
		bufferOut.close();
		out1.flush();
	} catch (IOException ex) {
	}
	out.clear();
	out = pageContext.popBody();
%>

