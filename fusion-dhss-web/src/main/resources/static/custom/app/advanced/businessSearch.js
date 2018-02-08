//var type = {LTE:'LTE',VoLTE:'VoLTE',GPRS:'GPRS',VOICE:'语音',NETWORK:'智能网',OVERHEAD:'开销户',STOPRESET:'停复机',GPRS_VOICE:'GPRS_语音',CARD:'补卡',BQUERY:'业务查询'};
var type = {LTE:'LTE',GPRS:'GPRS',VOICE:'语音',NETWORK:'智能网',OVERHEAD:'开销户',STOPRESET:'停复机',GPRS_VOICE:'GPRS_语音'};
var bossGrid;
var dataSource;
var isOk = "ok";
var errorFlag= '其他';
var neName = "";
var message="";
//默认移动
var isType = "all";
//查询条件
var searchObj = {page:1,pageSize:10,startTime:'',endTime:'',imsiMsisdn:'',result:'',businessType:'',cmdType:'',errorCode:'',hlrsn:''};
var businessTypeAndCnDescMap = [];
var businessTypeArray = [];
var businessTypeCnDescArray = [];
function getBusinessType(businessTypeCnDesc){
	var businessType = "";
	$.each(businessTypeAndCnDescMap,function(index,item){
		if(businessTypeCnDesc == item.business_type_cn){
			businessType = item.business_type;
		}
	});
	return businessType;
}
function initGridData(){
	 dataSource = new kendo.data.DataSource({
		
        transport: {
        	read : {
 				type : "GET",
 				url : "boss/search-item",
 				dataType : "json",
 				contentType : "application/json;charset=UTF-8"
 			},
            parameterMap: function (options, operation) {
                if (operation == "read") { 
                	//动态组装条件参数
                	//searchObj.page = options.page;
                	//searchObj.pageSize = options.pageSize;
                    return searchObj;
                }
            },
        },
        batch: true,
        pageSize: 10,
        schema: {
            data: function (data) {
                return data.content;//返回页面数据
            },
            total: function (data) {
            	getTotal();
                return data.totalElements;//总条数
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false
    });
	bossGrid  = $("#bossList").kendoGrid({
		
		  dataSource:dataSource,
		  height: 410,
		  groupable: false,
	      sortable: true,
	      resizable: true,
	      columnMenu: true,
	      reorderable: true,
	      pageable: {
	    	  refresh: true,
	    	  pageSizes: true,
	    	  buttonCount: 5,
	    	  pageSizes: [10, 20, 30],
	    	  messages: {
	    	  display: "显示 {0}-{1} 共 {2} 项",
	    	  empty: "没有数据",
	    	  itemsPerPage: "每面显示数量",
	    	  first: "第一页",
	    	  last: "最后一页",
	    	  next: "下一页",
	    	  previous: "上一页"
	    	  }
	      },
	        columns:[
                     { field: "re_id" ,title:"请求ID" , width: 300},
	                 { field: "imsi" ,title:"IMSI" , width: 160},
	                 { field: "isdn" ,title:"MSISDN" , width: 160},
	                 { field: "operationname",title:"指令类型", width: 110},
	                 { field: "b_class",title:"业务类型", width: 80,
	                   template:"#if(type[b_class]){##:type[b_class]##}else{}#"
	                 },
	                 { field: "hlrsn", title:"上级网元", width: 80,
	                   template:"#:neName##:hlrsn#"
	                 },
	                 { field: "callback_result", title:"执行结果", width: 80},
	                 { field: "errorcode",title:"错误代码" , width: 80},
	                 { field: "re_time",title:"时间", width: 150},
	                 { field: "errormessage",title:"结果内容", hidden:true},
	                 { field: "context",title:"内容详情", hidden:true},
	                 { title:"操作",
	                   width: 160,
	                   template:"#if(message!='' && message!=null){# "
	                	   + "<button type='button' class='btn btn-success btn-xs' onclick='lookMessage(this)'>查看结果详情</button>#}#"
	                	   + "&nbsp;&nbsp;<button type='button' class='btn btn-success btn-xs' onclick='lookText(this)'>查看内容</button>"
	                 }
	            ]
	    }).data("kendoGrid");
}

//查看错误详情
function lookMessage(d){
	$('#context').empty();
	var message = bossGrid.dataItem($(d).closest("tr")).errormessage;
	//弹出窗口
	openWindow('errorModal','416','815','查看错误详情'); 
	$('#context').val(message);
}

//查看内容
function lookText(d){
	//清空
	$('#bossContext').empty();
	var context = bossGrid.dataItem($(d).closest("tr")).context;
	//弹出窗口
	openWindow('textModal','416','815','查看内容');
	$('#bossContext').val(formatXml(context));
}

function openWindow(id,h,w,titleName){
	
	$("#"+id).kendoWindow({
		  maxHeight: h+'px',
		  maxWidth:w+'px',
		  draggable: false,
		  title:titleName
	});
	dialogObj = $("#"+id).data("kendoWindow");
	dialogObj.open();dialogObj.center();
}
function cmdTypeAndBusinessMapping(){
	$.ajax({
		type : "GET",
		url :"/boss/cmdAndBusinessTypeMap",
		async:false,
		success : function(data) {
		}
	});
}
$(function($) {
	cmdTypeAndBusinessMapping();
	if(isType==true){
		$('#bustype').show();
		$('#cmdtype').hide();
	}else if(isType == false){
		$('#cmdtype').show();
		$('#bustype').hide();
		//指令类型
		cmdType();
	}
	else if(isType == "all"){
		$('#cmdtype').show();
		$('#bustype').show();
		cmdType();
	}
	//getTotal();
	
	kendo.culture("zh-CN");
	//上级网元
	inputNeName();
	
	//列表加载初始化
	initGridData();
	
	//业务类型
	businessType();
	
	//错误类型
	errorType();

	//执行结果下拉
	resultType();
	
	//页面开始时间为当前时间往后退7天
    var startTime = new Date(new Date().getTime() - 24*7*60*60*1000);
	// 时间选择查询  
	 $("#startDateTime").kendoDateTimePicker({
	        value:startTime,
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	   });
	 $("#endDateTime").kendoDateTimePicker({
	       value:new Date(),
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	 });

	
	$('#btn-search').on('click', function() {
		searchObj.startTime = $("#startDateTime").val();
		searchObj.endTime = $("#endDateTime").val();
		searchObj.hlrsn =  $("#inputNeName").val();
		searchObj.imsiMsisdn = $("#inputValue").val();
		searchObj.result = $("#resultType").val();
		searchObj.businessType = $("#businessType").val();
		searchObj.cmdType = $("#cmdTypeId").val();
		//searchObj.errorCode = $("#errorType").val();
		var errorCode = $("#errorType").val().split("-")[0];// may contains "-"
		searchObj.errorCode = errorCode; 
		dataSource.read(searchObj);
		$("#bossList").data("kendoGrid").pager.page(1);
		console.log(searchObj);
		/*validataTime(searchObj.startTime,searchObj.endTime);
		if(isOk=="ok"){
		}else{
			showNotify(isOk,"warning");
		}*/
	});
	
	$('#btn-export').on('click', function() {

		window.location.href="userBoss/exportFile?" +
		 "startTime="+ $('#startDateTime').val()+
		 "&endTime="+$('#endDateTime').val()+
		 "&imsiMsisdn="+$('#inputValue').val()+
		 "&result="+$("#resultType").val()+
		 "&businessType="+$("#businessType").val()+ //
		 "&cmdType="+$("#cmdTypeId").val()+ // has more
		 "&errorCode="+$("#errorType").val()+ // has more
		 "&hlrsn="+$("#inputNeName").val();
	});
	
	
});


function getTotal(){
	var val = $('#errorType').val().split('-')[0];
	$.ajax({
		type : "POST",
		url : "boss/total",
		data:{
			"startTime":$('#startDateTime').val(),
			 "endTime":$('#endDateTime').val(),
			 "imsiMsisdn":$('#inputValue').val(),
			 "result":$("#resultType").val(),
			 "businessType":$("#businessType").val(),
			 "cmdType":$("#cmdTypeId").val(),
			 "errorCode":val,
			 "hlrsn":$("#inputNeName").val()
		},
		//async:false,
		success : function(data) {
			if(data>0){
				$("<span> , 总条数  ["+data+"]</span>").appendTo($("#bossList .k-pager-wrap .k-pager-info"));
			}
		}
	});
	
}

function inputNeName(){
	var neData = [];
	$.ajax({
		type : "GET",
		url : "getNePatentName",
		async:false,
		success : function(data) {
			$.each(data, function(i,item){
        		if(i==0){neName=item.replace(/[^a-zA-Z]/g,"");};
        		var _value = item.replace(/[^0-9]/ig,"")==""?"hlrsn":item.replace(/[^0-9]/ig,"");
        		neData.push({text:item,value:_value});
        	});
		}
	});
	$("#inputNeName").kendoDropDownList({
		    optionLabel:"--全部网元类型--",
	        dataTextField: "text",
	        dataValueField: "value",
	        dataSource:neData,
	       // filter: "contains",
	        suggest: true,
	});
}

function resultType(){
	
	 $("#resultType").kendoDropDownList({
		    optionLabel:"--全部结果类型--",
	        dataTextField: "text",
	        dataValueField: "value",
	        dataSource: [
	            { text: "success", value: "success" },
	            { text: "failure", value: "failure" },
	        ],
	       // filter: "contains",
	        suggest: true,
	    });
}
function unique2(array) {
	var n = {}, r = [], len = array.length, val, type;
	for (var i = 0; i < array.length; i++) {
		val = array[i];
		type = typeof val;
		if (!n[val]) {
			n[val] = [ type ];
			r.push(val);
		} else if (n[val].indexOf(type) < 0) {
			n[val].push(type);
			r.push(val);
		}
	}
	return r;
} 

//获取业务类型
function businessType(){
	$.ajax({
		url:"queryMonitorType/all",
		type:"GET",
		async:false,
		success:function(data){
			$.each(data,function(index,item){
				var businessTypeAndCnDesc = new Object();
				businessTypeAndCnDesc.business_type = item.business_type;
				businessTypeArray.push(item.business_type);
				businessTypeAndCnDesc.business_type_cn = item.business_type_cn;
				businessTypeCnDescArray.push(item.business_type_cn);
				businessTypeAndCnDescMap.push(businessTypeAndCnDesc);
			});
		}
	});
	$("#businessType").kendoComboBox({
		dataSource : unique2(businessTypeCnDescArray),
		filter : "contains",
		suggest : true

	// index:1
	});
}
//指令类型
function cmdType(){
	
	$("#cmdTypeId").kendoComboBox({
		dataSource: new kendo.data.DataSource({
		    transport: {
		        read: {
		            url: "getCmdType",
		            datatype: "json"
		        }
		    }
		}),
		filter: "contains",
		suggest: true
	});
}

function validataTime(startTime,endTime){
	$.ajax({
		type : "POST",
		url : "validataTime",
		data:{
			"startTime":startTime,
			"endTime":endTime
		},
		async:false,
		success : function(data) {
			isOk = data;
		}
	});
}

function errorType() {
	$.ajax({
		url : "/getErrorCode",
		type : "GET",
		async:false,
		success : function(data) {
			var errorCodeDescArray = [];
			$.each(data,function(index,item){
				errorCodeDescArray.push(item.error_code); 
			});
			$("#errorType").kendoComboBox({
				dataSource : errorCodeDescArray,
				filter : "contains",
				suggest : true
			});
		}

	})

}
//格式化xml
function formatXml(text) {  
	text = text.replace("~","@");
    //去掉多余的空格  
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g, function($0, name, props) {  
        return name + ' ' + props.replace(/\s+(\w+=)/g, " $1");  
    }).replace(/>\s*?</g, ">\n<");  
  
    //把注释编码  
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g,  
            function($0, text) {  
                var ret = '<!--' + escape(text) + '-->';  
                //alert(ret);  
                return ret;  
            }).replace(/\r/g, '\n');  
  
    //调整格式  
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;  
    var nodeStack = [];  
    var output = text.replace(rgx, function($0, all, name, isBegin,  
            isCloseFull1, isCloseFull2, isFull1, isFull2) {  
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/')  
                || (isFull1 == '/') || (isFull2 == '/');  
        //alert([all,isClosed].join('='));  
        var prefix = '';  
        if (isBegin == '!') {  
            prefix = getPrefix(nodeStack.length);  
        } else {  
            if (isBegin != '/') {  
                prefix = getPrefix(nodeStack.length);  
                if (!isClosed) {  
                    nodeStack.push(name);  
                }  
            } else {  
                nodeStack.pop();  
                prefix = getPrefix(nodeStack.length);  
            }  
  
        }  
        var ret = '\n' + prefix + all;  
        return ret;  
    });  
  
    var prefixSpace = -1;  
    var outputText = output.substring(1);  
    //alert(outputText);  
  
    //把注释还原并解码，调格式  
    outputText = outputText.replace(/\n/g, '\r').replace(  
            /(\s*)<!--(.+?)-->/g,  
            function($0, prefix, text) {  
                //alert(['[',prefix,']=',prefix.length].join(''));  
                if (prefix.charAt(0) == '\r')  
                    prefix = prefix.substring(1);  
                text = unescape(text).replace(/\r/g, '\n');  
                var ret = '\n' + prefix + '<!--'  
                        + text.replace(/^\s*/mg, prefix) + '-->';  
                //alert(ret);  
                return ret;  
            });  
  
    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n');  
  
}    
function getPrefix(prefixIndex) {  
    var span = '    ';  
    var output = [];  
    for ( var i = 0; i < prefixIndex; ++i) {  
        output.push(span);  
    }  
  
    return output.join('');  
} 

