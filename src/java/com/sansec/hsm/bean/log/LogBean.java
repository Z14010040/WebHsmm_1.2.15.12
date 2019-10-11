package com.sansec.hsm.bean.log;

import java.util.Date;

/**
 * @author root
 */
public class LogBean {
    private int id;
    private Date createTime;
    private String operator;
    private boolean success;
    private String operation;
    private String serial;
    private String msg;
    private String parameters;
    private String createTimeS;
    private boolean audited;
    private Date audittime;

    public LogBean() {
        this.createTime = new Date();
        this.audited = false;
    }

    public LogBean(String subject, boolean result, String operation, String object, String parameters) {
        this.createTime = new Date();
        this.operator = subject;
        this.success = result;
        this.operation = operation;
        this.msg = object;
        this.parameters = parameters;
        this.audited = false;
    }

    public LogBean(Date createTime, String subject, boolean result, String operation, String object, String parameters, boolean auditalg, Date audittime) {
        this.createTime = createTime;
        this.operator = subject;
        this.success = result;
        this.operation = operation;
        this.msg = object;
        this.parameters = parameters;
        this.audited = auditalg;
        this.audittime = audittime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCreateTimeS() {
        return createTimeS;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public void setCreateTimeS(String screateTime) {
        this.createTimeS = screateTime;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public boolean isAudited() {
        return audited;
    }

    public void setAudited(boolean auditalg) {
        this.audited = auditalg;
    }

    public Date getAudittime() {
        return audittime;
    }

    public void setAudittime(Date audittime) {
        this.audittime = audittime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "id=" + id + ", createTime=" + createTime + ", operator=" + operator + ", success=" + success + ", operation=" + operation + ", msg=" + msg + ", parameters=" + parameters + ", audited=" + audited + ", audittime=" + audittime + "\r\n";
    }
}
