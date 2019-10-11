/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sansec.hsm.bean.config;

import com.sansec.hsm.bean.GlobalData;
import com.sansec.hsm.bean.Language;
import com.sansec.hsm.bean.Privilege;
import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.exception.NoPrivilegeException;
import com.sansec.hsm.util.ErrorInfo;
import com.sansec.hsm.util.OperationLogUtil;
import debug.log.LogUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author root
 */
public class ServiceRunInfo {

	public static final String operationname = "SERVICE MANAGEMENT";
	public static final String SERVICE_NAME = GlobalData.DEF_SERVICE_NAME;
	private int currentCount;               // 当前并发数
	private int isRun;
	private String usedMemoryPercent;       // 使用内存的百分比
	private String serviceStatus;           // 服务状态

	public ServiceRunInfo() {
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public String getUsedMemoryPercent() {
		return usedMemoryPercent;
	}

	public String getServiceStatus() {
		return serviceStatus;
	}

	public void load() throws DeviceException, NoPrivilegeException {
		// 检查权限
		Privilege rights = new Privilege();
		if (!rights.check(Privilege.PRIVILEGE_SHOW_RUN_STATUS)) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_SHOW_RUN_STATUS));
		}

		double memoryRatio = 0.0;
		long freeMemory = 0;
		long usedMemory = 0;
		/*
		 free cmd result:
		 total       used       free     shared    buffers     cached
		 Mem:       2051800     914168    1137632          0     118620     540820
		 -/+ buffers/cache:     254728    1797072
		 Swap:            0          0          0
		 */
		try {
			Process process = Runtime.getRuntime().exec("free");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			str = br.readLine(); //1rd line:             total       used       free     shared    buffers     cached
			//LogUtil.println("free_line1:" + str);
			str = br.readLine(); //2rd line:Mem:       2051800     914168    1137632          0     118620     540820
			//LogUtil.println("free_line2:" + str);
			str = br.readLine(); //3rd line:-/+ buffers/cache:     254728    1797072
			//LogUtil.println("free_line3:" + str);
			String ss[] = str.split(" ");
			for (int i = 0; i < ss.length; i++) {
				if ((ss[i].length() < 1) || (ss[i].charAt(0) > '9') || (ss[i].charAt(0) < '0')) {
					continue;
				}
				if (usedMemory == 0) {
					usedMemory = Integer.parseInt(ss[i]);
				} else {
					freeMemory = Integer.parseInt(ss[i]);
					if (freeMemory > 0) {
						break;
					}
				}
			}

			//LogUtil.println("usedMemory:" + usedMemory);
			//LogUtil.println("freeMemory:" + freeMemory);
			br.close();

			memoryRatio = usedMemory * 1.0 / (usedMemory + freeMemory) * 100;

			DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();
			format.applyLocalizedPattern("0.00");
			usedMemoryPercent = format.format(memoryRatio);
			usedMemoryPercent += "%";
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		try {
			Process process = Runtime.getRuntime().exec("ps -A");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;
			while ((str = br.readLine()) != null) {
				if (str.contains(SERVICE_NAME)) {
					currentCount++;
				}
			}
			br.close();

			//run status
			isRun = 0;
			serviceStatus = Language.get("RunStatusStop");
			ServerInfo server = new ServerInfo();
			server.load();
			//String strRunStatusShell = "netstat -an |grep " + server.getPort() + "  |wc -l";
			String strRunStatusShell = "netstat -an ";
			System.out.println(strRunStatusShell);
			Process process2 = Runtime.getRuntime().exec(strRunStatusShell);
			BufferedReader br2 = new BufferedReader(new InputStreamReader(process2.getInputStream()));
			String str2 = null;
			String strGrep = String.valueOf(server.getPort());
			System.out.println(strGrep);
			while ((str2 = br2.readLine()) != null) {
				if (str2.contains(strGrep)) {
					System.out.println(str2);
					serviceStatus = Language.get("RunStatusRun");
					isRun = 1;
				}
			}
			br2.close();
		} catch (Exception e) {
		}

		if (currentCount > 0) {
			currentCount--; //排除主进程
		}
	}

	/* 起动服务 */
	public void start() throws DeviceException, NoPrivilegeException {
		// 检查权限
		Privilege rights = new Privilege();
		if (!rights.strictCheck(Privilege.PRIVILEGE_START_SERVICE)) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_START_SERVICE));
		}

		try {
			Process process = Runtime.getRuntime().exec("/bin/" + SERVICE_NAME + " -s &");
			process.waitFor();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			LogUtil.println("/bin/" + SERVICE_NAME + " -s & exec error: " + ex);
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException("服务启动失败-1!");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		load();
		if (!isRun()) {
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务启动失败-2!");
		}
		OperationLogUtil.genLogMsg("INFO","Start Service" ,"success",null,null,null);


	}

	/* 停止服务 */
	public void stop() throws DeviceException, NoPrivilegeException {
		// 检查权限
		Privilege rights = new Privilege();
		if (!rights.strictCheck(Privilege.PRIVILEGE_STOP_SERVICE)) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_STOP_SERVICE));
		}

		try {
			Process process = Runtime.getRuntime().exec("sudo /bin/killall " + SERVICE_NAME);
			process.waitFor();
		} catch (Exception ex) {
			LogUtil.println("sudo killall swserver exec error: " + ex);
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务停止失败-3!");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		if (isRun()) {
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务停止失败-4!");
		}
		OperationLogUtil.genLogMsg("INFO","Stop Service" ,"success",null,null,null);
	}

	/* 重新启动服务 */
	public void restart() throws DeviceException, NoPrivilegeException {
		// 检查权限
		Privilege rights = new Privilege();
		if (!rights.strictCheck(Privilege.PRIVILEGE_RESTART_SERVICE)) {
			throw new NoPrivilegeException(Privilege.getPrivilegeInfo(Privilege.PRIVILEGE_RESTART_SERVICE));
		}

		try {
			Process process = Runtime.getRuntime().exec("sudo /bin/killall " + SERVICE_NAME);
			process.waitFor();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			LogUtil.println("sudo killall swserver exec error: " + ex);
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务停止失败-5!");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		if (isRun()) {
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务停止失败-6!");
		}

		try {
			Process process = Runtime.getRuntime().exec("/bin/" + SERVICE_NAME + " -s &");
			process.waitFor();
		} catch (Exception ex) {
			System.out.println(ex.toString());
			LogUtil.println("/bin/swserver -s & exec error: " + ex);
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));

			throw new DeviceException("服务启动失败-7!");
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {
		}

		load();
		if (!isRun()) {
			OperationLogUtil.genLogMsg("ERROR",operationname ,"Failed","Start Service Failed.",ErrorInfo.returnErrorInfo().get(0),ErrorInfo.returnErrorInfo().get(1));
			throw new DeviceException("服务启动失败-8!");
		}
		OperationLogUtil.genLogMsg("INFO","Restart Service" ,"success",null,null,null);
	}

	/* 是否在运行 */
	public boolean isRun() {
		/*
		String buf = "";
		try {
			Process process = Runtime.getRuntime().exec("ps -A");

			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String str = null;

			while ((str = br.readLine()) != null) {
				buf += str;
			}
		} catch (Exception ex) {
		}

		return buf.contains(SERVICE_NAME);
		* */
		return (this.isRun == 1);
	}

	@Override
	public String toString() {
		return "CurrentCount      = " + currentCount + "\n"
				+ "UsedMemoryPercent = " + usedMemoryPercent + "\n"
				+ "ServiceStatus     = " + serviceStatus + "\n";
	}

	public static void main(String[] args) throws DeviceException, NoPrivilegeException, Exception {
		ServiceRunInfo info = new ServiceRunInfo();
		info.load();
		System.out.println(info);

		System.out.println("current: " + info.isRun());
		info.start();
		Thread.sleep(10);
		System.out.println("start: " + info.isRun());
		info.restart();
		Thread.sleep(10);
		System.out.println("restart: " + info.isRun());
		info.stop();
		Thread.sleep(10);
		System.out.println("stop: " + info.isRun());
		/*int n = -1;
		 Process process = Runtime.getRuntime().exec("ps -A");
		 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		 //n = process.exitValue();
		 String str = null;
		 while( (str = br.readLine()) != null ) {
		 System.out.println(str);
		 }*/

	}
}
