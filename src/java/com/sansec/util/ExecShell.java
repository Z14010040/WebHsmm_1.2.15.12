package com.sansec.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecShell {

	/**
	 * 运行shell脚本
	 * 
	 * @param shell
	 *            需要运行的shell脚本
	 */
	public static void execShell(String shell) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 脚本执行
	 * @param command
	 * @return int
	 * @author wangtao
	 * @date 2017年1月19日 上午10:55:09
	 */
	public static int execShellFile(String command) {
		int success = 0;
		StringBuffer sbf = new StringBuffer();
		BufferedReader bufferedReader = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		sbf.append(dateFormat.format(new Date())).append("执行命令").append(command).append("\r\n");
		String[] cmd = { "/bin/sh", "-c", command };
		Process pid = null;
		try {
			pid = Runtime.getRuntime().exec(cmd);
			if (pid != null) {
				sbf.append("进程号:").append(pid.toString()).append("\r\n");
				bufferedReader = new BufferedReader(new InputStreamReader(pid.getInputStream()));
			} else {
				sbf.append("没有pid").append("\r\n");
			}
			String line = null;
			while (bufferedReader != null && ((line = bufferedReader.readLine()) != null)) {
				sbf.append(line).append("\r\n");

			}

			//System.out.println("命令执行" + sbf.toString());
			success = 1;
		} catch (IOException ioe) {
			sbf.append("脚本执行异常" + ioe.getMessage());

		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		}
		return success;

	}
	
	public static Process getExecShellProcess(String command) {
		Process pid = null;
		String[] cmd = { "/bin/sh", "-c", command };
		try {
			//System.out.println("命令执行" + command);
			pid = Runtime.getRuntime().exec(cmd);
		} catch (IOException ioe) {
			ioe.printStackTrace();
	}
	return pid;
	}
}
