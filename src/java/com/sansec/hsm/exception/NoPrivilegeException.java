package com.sansec.hsm.exception;

import com.sansec.hsm.bean.Language;

public class NoPrivilegeException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NoPrivilegeException() {
        super();
    }

    public NoPrivilegeException(String message) {
        super(Language.get("PrivilegeCheckWarning") + " " + message);
    }
}
