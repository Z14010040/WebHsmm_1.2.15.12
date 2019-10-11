<%@ page language="java" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">

        <title>Key Recovery Wizard - Import Key Component</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <link rel="stylesheet" type="text/css" href="css/styles.css" />
        <script language="JavaScript" type="text/javascript">
            function check(){
                var sPin = document.getElementById("pin1");
                var len = sPin.value.length;
                if(sPin.value == ""){
                    alert($.i18n.prop('ICPINCheckNull'));
                    return false;
                }else if(len > 8){
                    alert($.i18n.prop('ICPINCheckLength'));
                    return false;
                }else{
                    return true;
                }
            }
        </script>
		<script type="text/javascript" src="js/jquery3.3.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="js/language.js"></script>
		<script type="text/javascript">
			function display(){
				$('#label_KeyRestoreWizard').html($.i18n.prop('KeyRestoreWizard'));
				$('#label_KeyRestoreStepImportPart').html($.i18n.prop('KeyRestoreStepImportPart'));
				$('#label_KeyRestoreStepImportPartTips1').html($.i18n.prop('KeyRestoreStepImportPartTips1'));
				$('#label_KeyRestoreStepImportPartTips2').html($.i18n.prop('KeyRestoreStepImportPartTips2'));
				$('#label_KeyRestoreStepImportPartInputPIN').html($.i18n.prop('KeyRestoreStepImportPartInputPIN'));
				$('#label_KeyRestoreStepImportPartPrevious1').html($.i18n.prop('KeyRestoreStepImportPartPrevious1'));
				$('#label_KeyRestoreStepImportPartPrevious').html($.i18n.prop('KeyRestoreStepImportPartPrevious'));
				$('#label_KeyRestoreStepImportPartPrevious2').html($.i18n.prop('KeyRestoreStepImportPartPrevious2'));
				document.getElementById("button_confirm").value=$.i18n.prop('ButtonConfirm');
			}

			function load(){
				//alert(window.languageCallback);
				getLanguage(display);
				getLanguage(showGuide);
			}
			window.onload = load;
		</script>
    </head>
    <body>
        <%
            int part = 1;
            String strPart = "一";
            try {
                part = (Integer)request.getAttribute("part");
                if(part == 3) {
                    strPart = "3";
                } else if(part == 2){
                    strPart = "2";
                } else {
                    strPart = "1";
                }
            } catch(Exception ex) {
            }
        %>
        <div class="totalInfo">
            <div class="guideInfo">
                <jsp:include page="/guide/restore.jsp">
                    <jsp:param name="step" value="3" />
                </jsp:include>
            </div>
            <div class="blank"></div>
            <div class="Info">
                <div class="toheader"><label id="label_KeyRestoreWizard"></label></div>
                <div class="tobody">
                    3、<label id="label_KeyRestoreStepImportPart"></label>[<%= part %>].<br>
                    <br>
                    <label id="label_KeyRestoreStepImportPartTips1"></label>&nbsp;<%= strPart %>&nbsp;<label id="label_KeyRestoreStepImportPartTips2"></label>.<br>
                    <br>
                    <form id="form3" name="form3" onsubmit="return check()" method="post" action="/handle/maintainKeyRestore.jsp?action=importPart">
                        <div>
                           <label id="label_KeyRestoreStepImportPartInputPIN"></label>:<input type="password" name="pin" id="pin" value="" autocomplete="off"/>
                            <input type="submit" id="button_confirm" name="Submit" value="submit" />
                        </div>
                        <input type="hidden" value="<%= part %>" name="part">
                    </form>
                    <script>
                        $("form").submit(function() {
                          $("#button_confirm").attr("disabled", "disabled");
                        });
                    </script>
                    <br>
                    <label id="label_KeyRestoreStepImportPartPrevious1"></label><a href="/handle/maintainKeyRestore.jsp?action=showUploadFile">&lt;
						<label id="label_KeyRestoreStepImportPartPrevious"></label>&gt;</a><label id="label_KeyRestoreStepImportPartPrevious2"></label>.
                </div>
            </div>
        </div>
    </body>
</html>
