<%-- 
    Document   : doRsaGen
    Created on : 2011-9-24, 21:02:31
    Author     : root
--%>
<%@page import="com.sansec.hsm.util.IPUtil"%>
<%@page import="com.sansec.hsm.util.OperationLogUtil"%>
<%@page import="com.sansec.hsm.bean.keymgr.SymmetricKeyStatus"%>
<%@page import="com.sansec.hsm.util.KeyUtil"%>
<%@include file="/common.jsp" %>
<%@page import="com.sansec.hsm.bean.keymgr.AsymmetricKeyStatus"%>
<%@page import="com.sansec.hsm.exception.DeviceException"%>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException"%>
<%@page import="com.sansec.hsm.lib.kmapi"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.sansec.hsm.bean.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!	String url = "";
	String result = "";

	/* 密码机初始化 */
	boolean init_key(HttpServletRequest req, boolean isGuide) {
    
		HttpSession session = req.getSession();
		String username = (String)session.getAttribute("user");
		String userIp = IPUtil.parseIpAddr(req);
		OperationLogUtil.setUserIP(userIp);
		OperationLogUtil.setUser(username);
		try {
			Privilege rights = new Privilege();
			rights.init();

			result = Language.get("MaintainKeyInitializeSuccess");
			Backup.deleteFile();
			req.setAttribute("result", result);
			if (isGuide) {
				url = "/user/managerAdd.jsp?isGuide=true";
			} else {
				url = "/handle/maintainUser.jsp?action=showUserManagement&isGuide=false";
			}

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 产生密钥对
	boolean generate_keypair(HttpServletRequest req, boolean isGuide, String algrithm) {
    
                HttpSession session = req.getSession();
                String username = (String)session.getAttribute("user");
                String userIp = IPUtil.parseIpAddr(req);
                OperationLogUtil.setUserIP(userIp);
                OperationLogUtil.setUser(username);
		String index = null;
		int length = -1;
		int usage = -1;
		try {
			LogUtil.println("index: " + req.getParameter("index"));
			LogUtil.println("length: " + req.getParameter("length"));
			LogUtil.println("usage: " + req.getParameter("usage"));
			index = req.getParameter("index");
			length = Integer.parseInt(req.getParameter("length"));
			usage = Integer.parseInt(req.getParameter("usage"));
		} catch (Exception ex) {
			result = "Parameter error: " + ex;
			result = urlEncode(result);

			url = "/error.jsp?error=" + result;

			return false;
		}

		int encKeyLen = -1;
		int signKeyLen = -1;
		if (usage == 3) {        // 签名和加密密钥对
			encKeyLen = length;
			signKeyLen = length;
		} else if (usage == 2) { // 加密密钥对
			encKeyLen = length;
		} else if (usage == 1) { // 签名密钥对
			signKeyLen = length;
		}

		List<Integer> list = KeyUtil.analyzeIndexString(index, kmapi.MAX_RSA_KEY_COUNT / 2);
		if ((list == null) || (list.size() == 0)) {
			req.setAttribute("result", "Enter key index parameter error");
			return show_management(req, isGuide, algrithm);
		}

		try {
			for (int i = 0; i < list.size(); i++) {
				AsymmetricKeyStatus RSAKey = new AsymmetricKeyStatus(list.get(i), signKeyLen, encKeyLen, algrithm);
				RSAKey.generate();
			}

			result = Language.get("MaintainKeyGenerate")  +" "+ algrithm.toUpperCase();
			if (usage == 3) {        // 签名和加密密钥对
				//result = "产生"+algrithm.toUpperCase()+"密钥对"+list.toString()+"成功";
				//result += Language.get("MaintainKeyPair") ;
			} else if (usage == 2) { // 加密密钥对
				result += " "+Language.get("MaintainKeyUsageEncrypt");
			} else if (usage == 1) { // 签名密钥对
				result += " "+Language.get("MaintainKeyUsageSign");
			}
			result += " "+Language.get("MaintainKeyPair") + " [" + index + "] " + Language.get("MaintainKeySuccess");

			req.setAttribute("result", result);

			return show_management(req, isGuide, algrithm);
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 删除密钥对
	boolean delete_keypair(HttpServletRequest req, boolean isGuide, String algrithm) {
                HttpSession session = req.getSession();
                String username = (String)session.getAttribute("user");
                String userIp = IPUtil.parseIpAddr(req);
                OperationLogUtil.setUserIP(userIp);
                OperationLogUtil.setUser(username);
		try {
			int index = Integer.parseInt(req.getParameter("index"));
			int nSignKeyLen = -1;
			int nEncKeyLen = -1;
			if (index % 2 == 0) {
				nSignKeyLen = 1;
			} else {
				nEncKeyLen = 1;
			}

			AsymmetricKeyStatus status = new AsymmetricKeyStatus(index / 2 + 1, nSignKeyLen, nEncKeyLen, algrithm);
			LogUtil.println("maintainKey.jsp -> delete() -> status: \n" + status);
			status.delete();
			result = Language.get("ButtonDelete") + " "+algrithm;
			if (nSignKeyLen > 0) {
				result += " "+Language.get("MaintainKeyUsageSign")+" ";
			} else {
				result += " "+Language.get("MaintainKeyUsageEncrypt")+" ";
			}
			result += Language.get("MaintainKeyPair") +" ["+ ((index) / 2 + 1) + "]" + " "+Language.get("MaintainKeySuccess");
			req.setAttribute("result", result);

			return show_management(req, isGuide, algrithm);
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 产生密钥对
	boolean show_management(HttpServletRequest req, boolean isGuide, String algrithm) {
                HttpSession session = req.getSession();
                String username = (String)session.getAttribute("user");
                String userIp = IPUtil.parseIpAddr(req);
                OperationLogUtil.setUserIP(userIp);
                OperationLogUtil.setUser(username);
		try {
			AsymmetricKeyStatus RSAStatus = new AsymmetricKeyStatus(algrithm);
			List<AsymmetricKeyStatus> list = RSAStatus.getKeyPairStatus();

			req.setAttribute(algrithm + "Status", list);

			url = "/key/" + algrithm + "Management.jsp?isGuide=" + isGuide;

			return true;
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示设置私钥访问控制码页面
	boolean show_set_PAP(HttpServletRequest req, boolean isGuide, String algrithm) {
		try {
			int keyIndex = Integer.parseInt(req.getParameter("keyIndex"));
			req.setAttribute("keyIndex", keyIndex);
			req.setAttribute("algrithm", algrithm);

			url = "/key/setPrivateKeyAccessPassword.jsp?isGuide=" + isGuide;

			return true;
		} catch (Exception e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 修改私钥访问控制码
	boolean modify_PAP(HttpServletRequest req, boolean isGuide) {
		try {
			int keyIndex = Integer.parseInt(req.getParameter("keyIndex"));
			String password = req.getParameter("password");
			String password2 = req.getParameter("password2");
			String algrithm = req.getParameter("algrithm");
			LogUtil.println("modify_PAP -> algrithm: " + algrithm);
			LogUtil.println("modify_PAP -> keyIndex: " + keyIndex);
			req.setAttribute("keyIndex", keyIndex);
			url = "/key/setPrivateKeyAccessPassword.jsp?isGuide=" + isGuide;
			if (password == null || password2 == null) {
				req.setAttribute("result", Language.get("InputCheckNull"));
			} else if (password.length() != 8 || password2.length() != 8) {
				req.setAttribute("result", Language.get("ICPINCheckLength"));
			} else if (!password.equals(password2)) {
				req.setAttribute("result", Language.get("PasswordCheckConsistent"));
			} else {
				AsymmetricKeyStatus key = new AsymmetricKeyStatus(keyIndex);
				key.modifyPAP(password);
				//req.setAttribute("result", "[" + keyIndex + "]号密钥的访问控制码修改成功.");
				req.setAttribute("result", "[" + keyIndex + "]" + Language.get("MaintainKey") + "  "
						+ Language.get("KeyStatusTablePrivateAccess")+ "  "
						+ Language.get("ConfigSaveSuccess"));
				return show_management(req, isGuide, algrithm);
			}

			return show_set_PAP(req, isGuide, algrithm);
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		} catch (Exception e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示kek管理页面
	boolean show_kekManagement(HttpServletRequest req, boolean isGuide) {
		try {
			List list = new SymmetricKeyStatus().getKekStatus();

			url = "/key/kekManagement.jsp";
			req.setAttribute("status", list);

			return true;
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 产生密钥对
	boolean generate_kek(HttpServletRequest req, boolean isGuide) {
                HttpSession session = req.getSession();
                String username = (String)session.getAttribute("user");
                String userIp = IPUtil.parseIpAddr(req);
                OperationLogUtil.setUserIP(userIp);
                OperationLogUtil.setUser(username);
		String index = null;
		int length = -1;
		try {
			LogUtil.println("index: " + req.getParameter("index"));
			LogUtil.println("length: " + req.getParameter("length"));
			index = req.getParameter("index");
			length = Integer.parseInt(req.getParameter("length"));
		} catch (Exception ex) {
		}

		List<Integer> list = KeyUtil.analyzeIndexString(index, kmapi.MAX_KEK_COUNT);
		if ((list == null) || (list.size() == 0)) {
			req.setAttribute("result", "Enter key index parameter error");
			return show_kekManagement(req, isGuide);
		}

		try {
			for (int i = 0; i < list.size(); i++) {
				SymmetricKeyStatus key = new SymmetricKeyStatus(list.get(i), length);
				key.generate();
			}

			//result = "产生对称密钥"+list.toString()+"成功";
			result = Language.get("MaintainKeyGenerate")+" "+Language.get("MaintainKey")+" "+"[" + index + "]"+" "+Language.get("MaintainKeySuccess");

			req.setAttribute("result", result);

			return show_kekManagement(req, isGuide);
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 删除kek
	boolean delete_kek(HttpServletRequest req, boolean isGuide) {
		try {
			int index = Integer.parseInt(req.getParameter("index"));

			SymmetricKeyStatus status = new SymmetricKeyStatus(index, 0);
			LogUtil.println("maintainKey.jsp -> delete() -> status: \n" + status);
			status.delete();
			result = Language.get("ButtonDelete")+" "+Language.get("MaintainKey")+" "+"[" + index + "]"+" "+Language.get("MaintainKeySuccess");
			req.setAttribute("result", result);

			return show_kekManagement(req, isGuide);
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException e) {
			result = urlEncode(e.getMessage());
			LogUtil.println("failed result: " + e);
			url = "/error.jsp?error=" + result;

			return false;
		}
	}
%>


<%
	// 获取执行动作参数
	String action = "";
	boolean isGuide = false;
	try {
		action = request.getParameter("action");
		isGuide = Boolean.parseBoolean(request.getParameter("isGuide"));
	} catch (Exception ex) {
	}

	LogUtil.println("maintainKey -> action: " + action);
	// 执行
	boolean forTransport = false;   // 重定向标志
	if ("init".equals(action)) {
		// 初始化密码机
		forTransport = init_key(request, isGuide);
	} else if ("generate_RSA".equals(action)) {
		// 产生RSA密钥对
		forTransport = generate_keypair(request, isGuide, "RSA");
	} else if ("generate_ECC".equals(action)) {
		// 产生ECC密钥对
		forTransport = generate_keypair(request, isGuide, "ECC");
	} else if ("RSAManagement".equals(action)) {
		// 产生ECC密钥对
		forTransport = show_management(request, isGuide, "RSA");
	} else if ("ECCManagement".equals(action)) {
		// 产生ECC密钥对
		forTransport = show_management(request, isGuide, "ECC");
	} else if ("delete_RSA".equals(action)) {
		// 删除RSA密钥对
		forTransport = delete_keypair(request, isGuide, "RSA");
	} else if ("delete_ECC".equals(action)) {
		// 删除ECC密钥对
		forTransport = delete_keypair(request, isGuide, "ECC");
	} else if ("RSASetPrivateKeyAccessPassword".equals(action)) {
		// 显示私钥访问控制码修改页面
		forTransport = show_set_PAP(request, isGuide, "RSA");
	} else if ("ECCSetPrivateKeyAccessPassword".equals(action)) {
		// 显示私钥访问控制码修改页面
		forTransport = show_set_PAP(request, isGuide, "ECC");
	} else if ("modifyPrivateKeyAccessPassword".equals(action)) {
		// 修改私钥访问控制码修改页面
		forTransport = modify_PAP(request, isGuide);
	} else if ("kekManagement".equals(action)) {
		// 修改私钥访问控制码修改页面
		forTransport = show_kekManagement(request, isGuide);
	} else if ("generate_kek".equals(action)) {
		// 产生kek
		forTransport = generate_kek(request, isGuide);
	} else if ("delete_kek".equals(action)) {
		// 删除kek
		forTransport = delete_kek(request, isGuide);
	} else {
		forTransport = false;
		result = urlEncode("action parameter error");
		url = "/error.jsp?error=" + result;
	}

	// showUpdatePrivateAccessPassword
	LogUtil.println("maintainKey -> url: " + url);
	LogUtil.println("maintainKey -> forTransport: " + forTransport);
%>

<%
	// 跳转
	if (forTransport) {
%>
<jsp:forward page="<%= url%>" />
<%
	} else {
		response.sendRedirect(url);
	}
%>