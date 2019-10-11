package com.sansec.hsm.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface MyCDll extends Library {
	MyCDll INSTANCE = (MyCDll)Native.loadLibrary("MyCDll", MyCDll.class);
    public int checkU(byte[] user, int len1, byte[] password, int len2);
}
