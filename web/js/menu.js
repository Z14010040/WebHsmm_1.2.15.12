function hasClassName(classNameNow,searchName) {
    return new RegExp("( ?|^)" + searchName + "\\b").test(classNameNow);
}	

function addClass(element,value){
    if (!element.className) {
            element.className=value;
    } else {
            newClassName=element.className;
            newClassName+=" ";
            newClassName+=value;
            element.className=newClassName;
    }
    return element;
}

function removeClass(element,searchName) {			
    var classNameNow = element.className;
    element.className = classNameNow.replace(new RegExp("( ?|^)" + searchName + "\\b"), "");
    return element;
}
function stopEventPropagation (evt) { 

    var e=(evt)?evt:window.event; 
    if (window.event) { 
        e.cancelBubble=true; 
    } else { 
        //e.preventDefault(); 
        e.stopPropagation(); 
    } 
}
function menuFix() {
    var showClassName = "sfhover";
    var sfEls = document.getElementById("nav").childNodes;
    for (var i=0; i<sfEls.length; i++) {
        if (sfEls[i].nodeType == 1) {
            var sublis = sfEls[i].getElementsByTagName("li");
            var lens = sublis.length;
            if (lens > 0) {//当前菜单含有子菜单
                sfEls[i].onclick = function() {
                    if (hasClassName(this.className,showClassName)) {//已打开
                        this.className = "tree";//关闭之
                    } else {//已关闭
                        this.className = "untree sfhover";//打开之
                    }
                    return false;
                }//

                for (var j=0;j<lens;j++) {
                    sublis[j].onclick = function(e) {                    
                        stopEventPropagation(e);
                    }
                }
            }
        }                
    }
}

//window.onload=menuFix;