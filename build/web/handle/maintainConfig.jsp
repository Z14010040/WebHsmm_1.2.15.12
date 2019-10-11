<%-- 
    Document   : maintainConfig.jsp
    Created on : 2011-9-25, 21:22:56
    Author     : root
--%>

<%@page import="com.sansec.hsm.bean.Language"%>
<%@page import="com.sansec.hsm.bean.config.NetworkInfo"%>
<%@page import="com.sansec.hsm.bean.config.ServiceRunInfo"%>
<%@page import="com.sansec.hsm.bean.config.MaintainInfo"%>
<%@page import="com.sansec.hsm.bean.config.DeviceInfo"%>
<%@page import="com.sansec.hsm.bean.config.WhiteHostInfo"%>
<%@page import="com.sansec.hsm.bean.config.ServerInfo"%>
<%@page import="com.sansec.hsm.exception.DeviceException"%>
<%@page import="com.sansec.hsm.exception.NoPrivilegeException"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@include file="/common.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!	 String result = "";
	String url = "";
	// 修改网络

	boolean modify_network_info(HttpServletRequest req, boolean isGuide) {
		List<NetworkInfo> list = new ArrayList<NetworkInfo>(2);
		try {
			// 获取请求数据
			int netnums = Integer.parseInt(req.getParameter("netnums"));
			for (int i = 0; i < netnums; i++) {
				String dev = req.getParameter("net-" + i);
				String ip = req.getParameter("ip-" + i);
				String mask = req.getParameter("mask-" + i);
				String gateway = req.getParameter("gateway-" + i);
				String bond = req.getParameter("bond");
				NetworkInfo net = new NetworkInfo(i, dev, ip, mask, gateway);
				if (bond != null) {
					net.setBond(true);
				}
				list.add(net);
				if (net.getBond()) {
					break;
				}
			}

			// 检查请求数据
			for (int i = 0; i < list.size(); i++) {
				if (!ipCheck(list.get(i).getIp())) {
					throw new Exception(list.get(i).getName() + " -> ip invalid");
				}

				if (!ipCheck(list.get(i).getMask())) {
					throw new Exception(list.get(i).getName() + " -> mask invalid");
				}

				if (!ipCheck(list.get(i).getGateway())) {
					throw new Exception(list.get(i).getName() + " -> gateway invalid");
				}
			}

			// 保存
			for (int i = 0; i < list.size(); i++) {
				NetworkInfo net = list.get(i);
				net.store();
			}

			req.setAttribute("networks", list);
			req.setAttribute("result", Language.get("ConfigSaveSuccess"));

			url = "/config/network.jsp?isGuide=" + isGuide;

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
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}


	}

	// 显示当前网络
	boolean show_network_info(HttpServletRequest req, boolean isGuide) {
		List<NetworkInfo> list = null;
		try {
			NetworkInfo net = new NetworkInfo();
			list = net.getNetworks();
			req.setAttribute("networks", list);

			url = "/config/network.jsp?isGuide=" + isGuide;

			return true;

		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 重新启动网络
	boolean restart_network(HttpServletRequest req, boolean isGuide) {
		List<NetworkInfo> list = null;
		try {
			NetworkInfo net = new NetworkInfo();
			list = net.getNetworks();
			net.restart();

			if ((list != null) && (list.size() > 0)) {
				String ip1 = req.getServerName();
				NetworkInfo firstnet = list.get(0);
				NetworkInfo lastnet = list.get(list.size() - 1);
				String ip3 = firstnet.getIp();
				String ip2 = lastnet.getIp();
				if (!ip1.equalsIgnoreCase(ip2) && !ip1.equalsIgnoreCase(ip3)) {
					url = "http://" + ip2;
					return false;
				}
			}

			req.setAttribute("result", Language.get("ConfigNetworkRestartSuccess"));
			req.setAttribute("networks", list);
			url = "/config/network.jsp?isGuide=" + isGuide;
			return true;
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 修改服务配置
	boolean modify_serivce_info(HttpServletRequest req, boolean isGuide) {
		ServerInfo server = null;
		try {
			int port = Integer.parseInt(req.getParameter("port"));
			int onBoot = Integer.parseInt(req.getParameter("onBoot"));
			int maxThread = Integer.parseInt(req.getParameter("maxThread"));
			int sessionTimeout = Integer.parseInt(req.getParameter("sessionTimeout"));
			String connectPwd = req.getParameter("connectPwd");
			String serviceStartPwd = req.getParameter("serviceStartPwd");
			LogUtil.println("port: " + port);
			LogUtil.println("onBoot: " + onBoot);
			LogUtil.println("maxThread: " + maxThread);
			LogUtil.println("sessionTimeout: " + sessionTimeout);
			LogUtil.println("connectPwd: " + connectPwd);
			LogUtil.println("serviceStartPwd: " + serviceStartPwd);
			server = new ServerInfo(port, onBoot, sessionTimeout, maxThread, connectPwd, serviceStartPwd);

			// 保存
			server.store();

			req.setAttribute("server", server);
			req.setAttribute("result", Language.get("ConfigSaveSuccess"));

			url = "/config/service.jsp?isGuide=" + isGuide;


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
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示当前服务配置
	boolean show_service_info(HttpServletRequest req, boolean isGuide) {
		ServerInfo server = new ServerInfo();
		String result = null;
		try {
			server.load();
			//LogUtil.println("show -> maintainServiceInfo: \n"+server.toString());
			req.setAttribute("server", server);

			url = "/config/service.jsp?isGuide=" + isGuide;

			return true;
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 添加白名单
	boolean del_white_host(HttpServletRequest req, boolean isGuide) {
		ServerInfo server = null;
		try {
			// 获取请求数据
			String name = req.getParameter("name");
			LogUtil.println("name: " + name);

			WhiteHostInfo host = new WhiteHostInfo(name, null);

			// 删除保存
			LogUtil.println("jsp -> del");
			host.delete();

			// 显示
			return show_white_host_info(req, isGuide);
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
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 添加白名单
	boolean add_white_host(HttpServletRequest req, boolean isGuide) {
		List<WhiteHostInfo> list = null;
		try {
			// 获取请求数据
			String ip = req.getParameter("newIP");
			LogUtil.println("ip: " + ip);
			// 检查请求数据
			if (ip == null || !ipCheck(ip)) {
				throw new Exception("IP[ " + ip + " ]" + Language.get("FormatError"));
			}
			WhiteHostInfo host = new WhiteHostInfo(null, ip);

			// 添加保存
			LogUtil.println("jsp -> add");
			host.add();

			// 显示
			return show_white_host_info(req, isGuide);
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
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示当前白名单
	boolean show_white_host_info(HttpServletRequest req, boolean isGuide) {
		List<WhiteHostInfo> list = null;
		String result = null;
		try {
			WhiteHostInfo host = new WhiteHostInfo();
			list = host.load();
			LogUtil.println("show_white_host_info() -> white hosts: " + list.toString());
			req.setAttribute("whitehosts", list);

			url = "/config/whiteHost.jsp?isGuide=" + isGuide;

			return true;
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示设备信息
	boolean show_device_info(HttpServletRequest req, boolean isGuide) {
		try {
			DeviceInfo info = new DeviceInfo();
			info.load();

			req.setAttribute("device", info);
			url = "/config/device.jsp?";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示维护信息
	boolean show_maintain_info(HttpServletRequest req, boolean isGuide) {
		try {
			MaintainInfo info = new MaintainInfo();

			info.load();

			req.setAttribute("maintain", info);
			url = "/config/maintain.jsp?isGuide=" + isGuide;

			return true;
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 修改维护信息
	boolean modify_maintain_info(HttpServletRequest req, boolean isGuide) {
		try {
			//req.setCharacterEncoding("utf-8");
			String systemName = filter(req.getParameter("systemName"));
			String company = filter(req.getParameter("company"));
			String department = filter(req.getParameter("department"));
			String contact = filter(req.getParameter("contact"));
			String telephone = filter(req.getParameter("telephone"));
			String mobile = filter(req.getParameter("mobile"));
			String mail = filter(req.getParameter("mail"));
			LogUtil.println("modify_maintain_info() -> contact: " + contact);
			MaintainInfo maintain = new MaintainInfo(systemName, company, department, contact, telephone, mobile, mail);
			// 保存
			maintain.store();

			req.setAttribute("maintain", maintain);
			//req.setAttribute("result", "修改成功");
			req.setAttribute("result", Language.get("ConfigSaveSuccess"));

			url = "/config/maintain.jsp?isGuide=" + isGuide;


			return true;
		} catch (NoPrivilegeException e) {
			req.setAttribute("result", e.getMessage());
			LogUtil.println("NoPrivilegeException: " + e);
			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (Exception ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示服务状态信息
	boolean show_service_run_info(HttpServletRequest req, boolean isGuide) {
		try {
			ServiceRunInfo info = new ServiceRunInfo();
			info.load();

			req.setAttribute("status", info);
			url = "/config/runStatus.jsp?";

			return true;
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 显示服务运行状态
	boolean show_service_start_stop(HttpServletRequest req, boolean isGuide) {
		try {
			ServiceRunInfo info = new ServiceRunInfo();
			info.load();

			boolean bRun = info.isRun();
			if (!bRun) {
				url = "/config/serviceStart.jsp";
				result = Language.get("ServiceStopSuccess");
			} else {
				url = "/config/serviceStop.jsp";
				result = Language.get("ServiceStartSuccess");
			}

			if (req.getAttribute("result") == null) {
				req.setAttribute("result", result);
			}

			return true;
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 服务启动
	boolean service_start(HttpServletRequest req, boolean isGuide) {

		try {
			ServiceRunInfo info = new ServiceRunInfo();
			info.start();
			return show_service_start_stop(req, isGuide);
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 服务停止
	boolean service_stop(HttpServletRequest req, boolean isGuide) {

		try {
			ServiceRunInfo info = new ServiceRunInfo();
			info.stop();

			return show_service_start_stop(req, isGuide);
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

			url = "/error.jsp?error=" + result;

			return false;
		}
	}

	// 服务重起
	boolean service_restart(HttpServletRequest req, boolean isGuide) {

		try {
			ServiceRunInfo info = new ServiceRunInfo();
			info.restart();
			req.setAttribute("result", Language.get("ServiceRestartSuccess"));

			return show_service_start_stop(req, isGuide);
		} catch (NoPrivilegeException ex) {
			req.setAttribute("result", ex.getMessage());
			LogUtil.println("NoPrivilegeException: " + ex);

			url = "/handle/maintainUser.jsp?action=showUserLogin&isGuide=false";

			return true;
		} catch (DeviceException ex) {
			result = urlEncode(ex.getMessage());
			LogUtil.println("failed result: " + ex);

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

	LogUtil.println("maintainConfig.jsp -> action: " + action);
	LogUtil.println("maintainConfig.jsp -> isGuide: " + isGuide);

	// 执行
	boolean forTransport = false;   // 重定向标志
	if ("modifyNetworkInfo".equals(action)) {
		forTransport = modify_network_info(request, isGuide);
	} else if ("showNetworkInfo".equals(action)) {
		forTransport = show_network_info(request, isGuide);
	} else if ("restartNetwork".equals(action)) {
		forTransport = restart_network(request, isGuide);
	} else if ("modifyServiceInfo".equals(action)) {
		forTransport = modify_serivce_info(request, isGuide);
	} else if ("showServiceInfo".equals(action)) {
		forTransport = show_service_info(request, isGuide);
	} else if ("showWhiteHostInfo".equals(action)) {
		forTransport = show_white_host_info(request, isGuide);
	} else if ("addWhiteHostInfo".equals(action)) {
		forTransport = add_white_host(request, isGuide);
	} else if ("delWhiteHostInfo".equals(action)) {
		forTransport = del_white_host(request, isGuide);
	} else if ("showDeviceInfo".equals(action)) {
		forTransport = show_device_info(request, isGuide);
	} else if ("showMaintainInfo".equals(action)) {
		forTransport = show_maintain_info(request, isGuide);
	} else if ("modifyMaintainInfo".equals(action)) {
		forTransport = modify_maintain_info(request, isGuide);
	} else if ("showServiceRunStatus".equals(action)) {
		forTransport = show_service_run_info(request, isGuide);
	} else if ("showServiceStartOrStop".equals(action)) {
		forTransport = show_service_start_stop(request, isGuide);
	} else if ("serviceStart".equals(action)) {
		forTransport = service_start(request, isGuide);
	} else if ("serviceStop".equals(action)) {
		forTransport = service_stop(request, isGuide);
	} else if ("serviceRestart".equals(action)) {
		forTransport = service_restart(request, isGuide);
	} else {
		forTransport = false;
		result = urlEncode("action parameter error");
		url = "/error.jsp?error=" + result;
	}

	LogUtil.println("maintainConfig.jsp -> forTransport: " + forTransport);
	LogUtil.println("maintainConfig.jsp -> url: " + url);
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
