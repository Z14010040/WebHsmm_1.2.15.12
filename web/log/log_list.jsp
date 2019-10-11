<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="com.sansec.hsm.bean.log.LogBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="debug.log.LogUtil.Log"%>
<%@page import="com.sansec.hsm.bean.log.LogManage"%>
<%@page import="debug.log.LogUtil"%>
<%@ page pageEncoding="UTF-8" %>
<%@ include file="/common.jsp" %>
<%
	//language
	String strAudit= Language.get("LogManagementAudit");
	String strAudited = Language.get("LogManagementAudited");
	String strNextPage= Language.get("LogManagementListNextPage");
	String strTotal = Language.get("LogManagementListTotal");
	String strPage= Language.get("LogManagementListPage");
	String strPageGo = Language.get("LogManagementListGo");
	String strConfirm = Language.get("ButtonConfirm");
%>
<%
    int count = 0;
    int pageSize = 10;
    int pageNow = 1;
    int pageCount = 1;
    Log logger = LogUtil.getLog("log_manage.jsp");
    try {
        count = LogManage.getListCount();
    } catch (Exception e) {
        logger.error("获取日志列表长度时发生异常", e);
    }
    String pageNowStr = request.getParameter("pageNow");
    if (pageNowStr != null && !"".equals(pageNowStr)) {
        try {
            pageNow = Integer.parseInt(pageNowStr);
        } catch (Exception e) {
        }
    }
    
    if (count > 0) {
        
        String pageSizeStr = request.getParameter("pageSize");
        if (pageSizeStr != null && !"".equals(pageSizeStr)) {
            try {
                pageSize = Integer.parseInt(pageSizeStr);
                logger.debug("pageSize in parameter:"+pageSize);
            } catch (Exception e) {
                logger.debug("",e);
            }
        } else {
            pageSizeStr = getCookie("pageSize", request);
            if (pageSizeStr != null && !"".equals(pageSizeStr)) {
                try {
                    pageSize = Integer.parseInt(pageSizeStr);
                    logger.debug("pageSize in cookie:"+pageSize);
                } catch (Exception e) {
                }
            } else {
                logger.debug("none pageSize in cookie");
            }           
        }
        
        logger.debug("count:"+count);
        int c = (count/pageSize);
        int l = count%pageSize;
        if (l > 0) {
            c++;
        }
        pageCount = c;
        if (pageNow > pageCount) {
            pageNow = pageCount;
        }
        
        int offset = (pageNow - 1)*pageSize;
        logger.debug("offset:"+offset);
        ArrayList<LogBean> list = LogManage.getList(pageSize, offset);
        if (list != null && list.size() > 0) {
            for (LogBean bean:list) {
                boolean hasRead = bean.isAudited();
                String trClass="";
                if (!hasRead) {
                    trClass = "notRead";
                }
                //<th>id</th><th>创建时间</th><th>操作者IP</th><th>操作类型</th>
                //<th>操作结果</th><th>详细信息</th><th>状态</th><th>操作</th>

	
 %>
                <tr class="<%=trClass%>" logid="<%=bean.getId()%>">
                    <td>
                        <input type="checkbox" class="selectIndex" />
                        <span><%=bean.getId()%></span>
                    </td>
                
                    <td><%=bean.getCreateTimeS()%></td>
                    <td><%=bean.getOperator()%></td>
                 
                    <td><%=bean.getMsg()%></td>
                    <%if(hasRead){%>
                    <td><%=strAudited%></td>
                    <%} else {%>
                    <td><input type="button" title="点击审计当前日志" value=<%=strAudit%> class="toReadOne" /></td>
                    <%}%>
                
                </tr>
<%               
            }//end of for
%>
            <tr class="pageArea">
                <td colspan="8" >
                    <div style="float:left;">
                    <%
                    //preview page and first page
                    if(pageNow != 1)
                    {
                    	%>
                        <a href="log/log_list.jsp?pageNow=<%=pageNow-1%>"><span>&larr;</span></a>
                        <a href="log/log_list.jsp?pageNow=1"><span>1</span></a>
                        <%
                    }
                    else
                    {
                    	%>
                        <span class="pageNow">1</span>
                        <%
                    }
                    
                    if(pageCount > 1)
                    {
                    	if(pageNow <= 6){
                    		for(int i=2;i<=6;i++) {
                    			if(i >= pageCount){
                    				break;
                    			}

                                if(i != pageCount) {
                        			if (i==pageNow) {
                                        %>
                                        <span class="pageNow"><%=i%></span>
                                        <%
                                    } else {
                                        %>
                                        <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                        <%
                                    }
                                }
                            }
                    	} else {
                    		%>
                            <span style="border:0px">...</span>
                            <%
                            for(int i=pageNow-4;i<=pageNow;i++) {
                                if(i != pageCount) {
                                	if (i==pageNow) {
                                        %>
                                        <span class="pageNow"><%=i%></span>
                                        <%
                                    } else {
                                        %>
                                        <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                        <%
                                    }
                                }
                    		}
                    	}
                    	
                    	if(pageCount-pageNow <= 6) {
                    		if(pageNow <= 6){
                    			for(int i=7;i<pageCount;i++) {
                    				if(i!=pageCount) {
                    					%>
                                        <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                        <%
                    				}
                    			}
                    		}
                    		else
                    		{
                    			for(int i=pageNow+1;i<pageCount;i++) {
                                    %>
                                    <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                    <%
                                }
                    		}
                    		
                    	} else {
                    		if(pageNow <= 6) {
                        		for(int i=7;i<=10;i++) {
                        			if(i >= pageCount){
                        				break;
                        			}
                                    %>
                                    <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                    <%
                        		}
                    		}
                    		else {
                    			for(int i=pageNow+1;i<pageNow+5;i++) {
                                    %>
                                    <a href="log/log_list.jsp?pageNow=<%=i%>"><span><%=i%></span></a>
                                    <%
                        		}
                    		}
                    		%>
                            <span style="border:0px">...</span>
                            <%
                    	}
                    	
                        if(pageNow != pageCount) {
                        	%>
                        	<a href="log/log_list.jsp?pageNow=<%=pageCount%>"><span><%=pageCount%></span></a>
                            <a href="log/log_list.jsp?pageNow=<%=pageNow+1%>"><span><%=strNextPage%>&rarr;</span></a>
                            <%
                        }
                        else {
                        	%>
                            <span class="pageNow"><%=pageCount%></span>
                            <%
                        }
                        
                        //跳转
                        %>
                        <%=strTotal%>&nbsp;<%=pageCount%>&nbsp;<%=strPage%>&nbsp;&nbsp;<%=strPageGo%><input type="text" value="<%=pageNow%>" id="jumpnumber" style="width:30px"/><%=strPage%>
                        <input type="button" id="jumpto" value="<%=strConfirm%>" />
                        <a id="jumpa" href=""></a>
                        <%
                    }
                   
                    %>
                    </div>
                    <div id="pagesize" style="float:right;">                    
                        <input type="button" class="selected" value="10" />
                        <input type="button" value="20" />
                        <input type="button" value="50" />
                    </div>
                </td>
            </tr>
<%
        } else {// if (list != null && list.size() > 0) {
%>
<tr><td colspan="8">获取数据失败</td></tr>                
<%           
        }
    } else if (pageNow > 1) {//
%>
<tr><td colspan="8">获取数据失败.</td></tr>
<%        
    } else {//
%>
<tr><td colspan="8">数据不存在</td></tr>
<%        
    }   
%>


