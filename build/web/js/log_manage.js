$(document).ready(function() {
    var auditUrl = 'admin/audit-log';
 //   var deleteUrl = 'admin/delete-log';
    var listRefresh = 'log/log_list.jsp';
    function setPageSize() {
        var pageSizeInCookie = $.cookie('pageSize');
        if (pageSizeInCookie) {            
            $('#pagesize input[value='+pageSizeInCookie+']').addClass('selected').siblings().removeClass('selected');
        } else {
            $('#pagesize input').eq(0).addClass('selected').siblings().removeClass('selected');
        }
        $('.selectAll').attr('checked',false);
        $('.selectInvert').attr('checked',false);
    }
    setPageSize();
    
    function isAllSelected() {//判断当前页是否全部选中
        if ($('.selectIndex').not(':checked').length == 0) {
            $('.selectAll').attr('checked',true);
        } else {
            $('.selectAll').attr('checked',false);
        }
    }
    function getSelectedList() {//返回选中的列表
        var list = '';
        $('.selectIndex:checked').each(function(i,obj) {
            list += $(this).next('span').text()+',';
        });
        return list;
    }
    function getPageNow() {
        return $('.pageArea td').eq(0).children().eq(0).children().filter('.pageNow').text();
    }
    function getPageSize() {
        return $('.pageArea td').eq(0).children().eq(1).children().filter('.selected').val();
    }
    function showAuditResult(ids,data) {
        if (data.indexOf('error') != -1) {
            alert(data.substr(6));
        } else if (data.indexOf('ok') != -1) {
            var idArray = ids.split(',');
            for(var i=0,len=idArray.length;i<len;i++) {
                var idNow = idArray[i];
                if (idNow) {
                    $('#logList tr[logid='+idNow+']').removeClass('notRead');
                    $('#logList tr[logid='+idNow+'] .toReadOne').parent().empty().text('Audited');
                }
            }
        } else {
            alert('服务器异常');
        }
    }
    function showDeleteResult(data) {
        if (data.indexOf('error') != -1) {
            alert(data.substr(6));
        } else if (data.indexOf('ok') != -1) {
            var pageSizeNow = getPageSize();
            var pageNowStr = getPageNow();
            var url = listRefresh +'?pageNow=' + pageNowStr+ '&pageSize='+pageSizeNow + '&rand=' + Math.random();
            $('#logList').fadeOut().load(url,function() {
                $(this).fadeIn();
                setPageSize();
            });
        } else {
            alert('服务器异常');
        }
    }
    $('.selectAll').click(function() {        
        $('.selectIndex').attr('checked',!!$(this).attr('checked'));
        $('.selectAll').not(this).attr('checked',!!$(this).attr('checked'));
    });
    $('.selectInvert').click(function() {        
        $('.selectIndex').each(function(i,obj) {
            $(this).attr('checked',!$(this).attr('checked'));
        });
        isAllSelected();
        $('.selectInvert').not(this).attr('checked',!!$(this).attr('checked'));
    });  
    $('.selectIndex').live('click',function() {
        isAllSelected();
    });
    
    $('.toAudit').click(function() {//批量审核
       var list =  getSelectedList();
       if (list) {
           $.post(auditUrl,{'ids':list},function(data) {
               showAuditResult(list,data);
           });
       } else {
           alert('您尚未选择要操作的日志');
       }
    });
    $('.toDelete').click(function() {//批量删除
        if (!confirm('删除后不可恢复，确定要删除吗？')) {
            return;
        }
        var list =  getSelectedList();
        if (list) {
 //           $.post(deleteUrl,{'ids':list},function(data) {
 //               showDeleteResult(data);
  //          });
        } else {
            alert('您尚未选择要操作的日志');
        }
    });
    
    $('.toReadOne').live('click',function() {//审计一个元素
        var id = $(this).parent().parent().attr('logid');
        $.post(auditUrl,{'ids':id},function(data) {
            showAuditResult(id,data);
        });
    });
    
    $('.toDeleteOne').live('click',function() {//删除一个元素
        if (!confirm('删除后不可恢复，确定要删除吗？')) {
            return;
        }
        var id = $(this).parent().parent().attr('logid');
 //       $.post(deleteUrl,{'ids':id},function(data) {
//            showDeleteResult(data);
//        });
    });
    $('.pageArea #pagesize input').live('click',function() {//每页显示数目
        var pageSizeNow = $(this).val();
        var pageNowNO = getPageNow();
        $(this).addClass('selected').siblings().remove('selected');
        $.cookie('pageSize',pageSizeNow);
        $('#logList').fadeOut().load(listRefresh+'?rand='+Math.random()+'&pageSize='+pageSizeNow+'&pageNow='+pageNowNO,function() {
            $(this).fadeIn();
            setPageSize();
        });
    });
    $('.pageArea a').live('click',function() {
        var pageSizeNow = getPageSize();
        var url = $(this).attr('href')+'&pageSize='+pageSizeNow+'&rand='+Math.random();
        $('#logList').fadeOut().load(url,function() {
            $(this).fadeIn();
            setPageSize();
        });
        return false;
    });
    
    $('.pageArea a span').live('mouseover', function(){
    	  $(this).css("background-color","#6b9B9B");
    	  $(this).css("color","#FFFFFF");
    }).live('mouseout', function(){
    	$(this).css("background-color","#FFFFFF");
  	  	$(this).css("color","#50c0f0");
    });
    
    $('#jumpto').live('click', function(){
    	$('#jumpa').attr('href', 'log/log_list.jsp?pageNow='+$('#jumpnumber').val());
    	$('#jumpa').click();
    });
    
    $('.toExportAll').click(function() {//导出所有
 //   	window.location.href="/admin/export-log";
    });

});