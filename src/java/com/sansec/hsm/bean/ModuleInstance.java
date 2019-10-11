package com.sansec.hsm.bean;

import com.sansec.hsm.exception.DeviceException;
import com.sansec.hsm.lib.swsds;
import com.sansec.hsm.util.ErrorInfo;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

public class ModuleInstance {
	private volatile static ModuleInstance instance = null;
	private Pointer module = null;
    private Pointer session = null;

	private ModuleInstance() throws DeviceException {
		try {
			PointerByReference pointer = new PointerByReference();
			int rv = swsds.INSTANCE.SDF_OpenDevice(pointer);
			if (rv == 0) {		
				module = pointer.getValue();
                pointer = new PointerByReference();
                rv = swsds.INSTANCE.SDF_OpenSession(module, pointer);
                if(rv == 0) {
                    session = pointer.getValue();
                } else {
                    swsds.INSTANCE.SDF_CloseDevice(module);
                    throw new DeviceException("打开密码模块错误");
                }
			} else {
               throw new DeviceException("打开密码模块会话句柄错误");
            }
		} catch (DeviceException e) {
            throw e;
		}
	}

    /*
     * 单例模式获得密码机设备的句柄(即指针)module,session.
     * 如果持有指针,则返回指针,
     * 如果没有指针,则生成新指针.
     */
	public static ModuleInstance getInstance() throws DeviceException {
		if(instance == null) {
			synchronized(ModuleInstance.class) {
				if(instance == null) {
					instance = new ModuleInstance();
				}
			}
		}

		return instance;
	}

	public Pointer getModule() {
		return module;
	}

    public Pointer getSession() {
		return session;
    }

    public void close() {
        swsds.INSTANCE.SDF_CloseSession(session);
        swsds.INSTANCE.SDF_CloseDevice(module);
        instance = null;
    }

    public static void main(String[] args) throws DeviceException {
        ModuleInstance instance = ModuleInstance.getInstance();
        Pointer pointer = null;

        pointer = instance.getModule();
        System.out.println("module pointer: "+pointer);
        pointer = instance.getSession();
        System.out.println("session pointer: "+pointer);
        instance.close();
        System.out.println("finish...");
    }
}
