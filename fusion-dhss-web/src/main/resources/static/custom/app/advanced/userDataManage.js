//查询条件
var category = null;
var itemChecked = [], paramsChecked =[],paramas_cmd =[],numberData = [];
var contextvalue ="",cmdType = "",ratext = "";
var userDataGrid,dataSource,dialogObj,commandGrid;
var needRefresh = true;
var retryTime = 5;
var searchResult = {page:1,pageSize:10,searchField:''};

$(document).ready(function() {
	
	$("#startTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	$("#endTime").kendoDateTimePicker({
		format:"yyyy-MM-dd HH:mm:ss"
	});
	
	//加载命令类型
	getSubtoolCmdType();
	
	//向上按钮
	$(document).on("scroll",function(){
		if($(window).scrollTop() > 280){
			$("#toTop").fadeIn();
		} else {
			$("#toTop").fadeOut();
		}
	});
	$("#toTop").on("click",function(){
		$("html,body").animate({"scrollTop": "0"},300);
	});
 	
 	checkItemDataSource();
	
 	checkItemGrid();
	
	//加载结果列表
	resultGridData();
	
	//筛选命令
	$('#cmdName').on('keyup',function(){
		var itemName = $('#cmdName').val();
		var cmdType = $('#subtoolType').val();
		filtersData(itemName,cmdType);
	});
	
	//筛选用户号码
	$("#inputBtn").on('keyup', function(){
		searchResult.searchField = $('#inputBtn').val();
		dataSource.read(searchResult);
		
	});
	
	$("#startTime,#endTime").on('change', function(){
		searchResult.searchField = $('#inputBtn').val();
		searchResult.exeResults =$('#exeResult').val();
		dataSource.read(searchResult);
		
	});
	
	$("#startTime,#endTime").on('keyup', function(){
		searchResult.searchField = $('#inputBtn').val();
		searchResult.exeResults =$('#exeResult').val();
		dataSource.read(searchResult);
		
	});
	
	
	 //执行结果下拉
	$("#exeResult").kendoDropDownList({
			autoBind: true,
		 	optionLabel:"--请选择结果类型--",
		 	dataTextField: "text",
	        dataValueField: "value",
			dataSource: [{text:'成功',value:0},{text:'失败',value:1}],
			change: function(){
		    	searchResult.exeResults =$('#exeResult').val();
		    	//条件查询重新加载 
		    	dataSource.read(searchResult);
		    }
		
	});
	
	//是否批量选择
	$("#selectorButton").click(function(){
		//关闭选择窗口
		dialogObj.close();
		var type = $('input:radio[name=radio2]:checked').val();
		singleUser(type);//选择单用户操作
		
	});
	
	// 绑定提交按钮事件
	$('#submit').on('click', function() {
		//提交执行前清空数组
		clear();
		
		var dataItem = $("#commandGrid .k-grid-content input[type='radio']:checked");
		var data = commandGrid.dataItem($(dataItem).closest("tr"));
		if(data!=null){
			var i = {};
			i.name = data.name;
			i.command = data.command;
			i.params = data.params;
			i.account = data.account;
			i.defaultValue =  data.defaultParamValues;
			itemChecked.push(i);
			//打开窗口
			openWindow('selectorModal','100','450',itemChecked[0].name);
		}else{
			showNotify("未选择命令集","warning");
		}
	});

	$("#executeCommandButton").on("click",function(){
		$("#executeCommandButton").attr({"disabled":"disabled"});
		execution();
	});
});

//checkItem Grid
function checkItemGrid(){
	commandGrid = $("#commandGrid").kendoGrid({
		dataSource : commandGridDataSource,
		height : $(window).height() - $("#commandGrid").offset().top - 115,
		reorderable : true,
		resizable : true,
		sortable : true,
		pageable : false,
		columns: [{
			width: 30,
			template: "<input type='radio' name='radio1' />",
			attributes:{"class": "text-center"}
		},{
			field: "name",
			title: "<span  title='指令名称'>指令名称</span>"
		},{
			field: "cmdType",
			title: "<span  title='命令类型'>命令类型</span>"
		}]
	}).data("kendoGrid");
}

//命令集DataSource
function checkItemDataSource(){
	
	commandGridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "rest/check-item/search/findListByCategory?q=USER_DATA",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
        },
        schema: {
            data: function (data) {
            	if(data._embedded){
        			 return data._embedded["check-item"];
            	}
            	return [];
            },
            total: function(data) {
            	if(data._embedded){
        			return data._embedded["check-item"].length;
            	}
            	return 0;
            }
        },
        serverPaging : false,
		serverFiltering : false,
		serverSorting : false,
	});
}

//窗口初始化
function openWindow(id,h,w,titleName){
	
	$("#"+id).kendoWindow({
		  height: h+'px',
		  width:w+'px',
		  title:titleName
	});
	dialogObj = $("#"+id).data("kendoWindow");
	dialogObj.open();dialogObj.center();
	dialogObj.title(titleName);
}

//模板下载
function downFile(){
	window.location.href="subtool/downTemplateNumber?"
		   +"params="+itemChecked[0].params
		   +"&defaultValue="+itemChecked[0].defaultValue
	       +"&checkName="+itemChecked[0].name;
}

//参数导入
function uploadFile(){
	$('#fileName').click();
}

//导入excel数据
function uploadExcelFile(obj){
	
	var file = obj.value;
	if(file.length!=0){
		var extend = file.substring(file.lastIndexOf(".")+1); 
		if(!(extend=="xls")){ 
			showNotify("请上传后缀名为xls的文件","warning");
			return false; 
		}else{
			ajaxFileUpload();
		}
	}
}

//文件上传
function ajaxFileUpload() {
    // 开始上传文件时显示一个图片
   /* $("#wait_loading").ajaxStart(function() {
        $(this).show();
    // 文件上传完成将图片隐藏起来
    }).ajaxComplete(function() {
        $(this).hide();
    });*/
	
    //var elementIds=["commandName","command"]; //flag为id、name属性名
    $.ajaxFileUpload({
        url: 'subtool/setParamMap',
        type: 'post',
        dataType : "json",
		//contentType: "application/json;charset=UTF-8",
        secureuri: false, //一般设置为false
        fileElementId: 'fileName', // 上传文件的id、name属性名
        data:{
        	"params":itemChecked[0].params,
        	"checkName":itemChecked[0].name
        },
       // elementIds: elementIds, //传递参数到服务器
        success: function(data,status){
        	showNotify("上传文件成功","success");
        	getNumberMap();
        },
        error: function(data){ 
        	showNotify("上传文件失败","warning");
        }
    });
}

/**
 * 根据命令类型获取excel动态值
 */
function getNumberMap(){
	
	$.ajax({
		type : "post",
		dataType : "json",
		url : "subtool/getParamMap",
		data:{
			"checkName":itemChecked[0].name
		},
		success : function(data) {
			if(data!=null){
				numberData = data;
			}
		},
		error : function(data) {
			var error = JSON.parse(data.responseJSON);
			showNotify(error.message,"error");
		}
	});
}


/**
 * 用户操作选择
 * @param type 1：批量操作 0：单个操作
 */
function singleUser(type){
	
	//帮助信息处理
	if(itemChecked[0].command.indexOf(' ')!=-1){
		var cmd = itemChecked[0].command.split(" ");
		var code = "";
		$.each(cmd,function(index,item){
			
			if(index==1){
				code = item;
			}
			if(index==3 ){
				if(item.indexOf('$')<0){
					code +=" "+item;
				}
			}
		});
		
		getSubtoolHelpData(code);
	}
	
	
	
	var params = itemChecked[0].params;
	
	
	var defaultValue = itemChecked[0].defaultValue;
	
	if('null'==params || ''==params || params == null){//直接下发执行命令，无弹出窗口
		confirmCommand(type);
	}else{//用户需填写所需命令参数值，弹出窗口
		
		var h = "";  //高度
		var w = ""; //宽地
		var html ='<div  id="passwdModal" >'
		         +'<div class="modal-body">'
			     +'<div class="form-horizontal" role="form">';
		
		if(type==1){//批量操作
			h = "350";
			w = "720";
			html +='<div class="form-group" id="batchNumber" style="margin-left: -63px;"> '
				//+'<label for="inputAccount" class="col-sm-2 control-label">imsi / msisdn</label>'
				+'<div class="col-sm-offset-3 col-sm-8">'
				+'<button onclick="downFile()"  style="margin-left: -3px;width: 155px;" type="button" class="btn btn-default btn btn-primary" >模板下载</button>'
				+'<button onclick="uploadFile()" style="margin-left: 15px;width: 155px;" type="button" class="btn btn-default btn btn-primary">参数导入</button>'
				+'</div></div>';
		}else{
			h = "555";
			w = "670";
			var default_str = "";
			if(defaultValue){
				default_str = defaultValue.split(",");//动态参数多个用逗号分隔	
			}
			//设置默认参数值
			var str = params.split(",");//动态参数多个用逗号分隔
			var va_value = "";
			$.each(str,function(index,item){
				var itemval = item.replace(/[ ]/g,"");
				if(default_str!=""){
					va_value = default_str[index];
				}
				
				html +='<div class="form-group"> '
					+'<label  class="col-sm-2 control-label">'+itemval+'</label>'
					+'<div class="col-sm-5">'
					+'<input class="form-control" style="width:380px" type="text" value="'+va_value+'" id="input_value_'+itemval+'" placeholder="'+itemval+'">'
					+'</div></div>';
				
			});
		}
		
		html+='<div class="form-group">'
			+'<label class="col-sm-2 control-label"></label>'
			+'<div class="col-sm-6">'
			+'<button onclick="confirmCommand('+type+')" type="button" style="width:353px" class="btn btn-default btn btn-success" >'
			+'<span class="glyphicon glyphicon-ok"></span>提交执行</button>'
			+'</div></div>';
		
		//帮助信息显示
		html +='<div class="form-group"> '
			+'<label  class="col-sm-2 control-label">帮助信息</label>'
			+'<div class="col-sm-6">'
			+'<textarea style="width:380px;height:160px;" class="form-control" id="subtoolHelp"></textarea></div></div></div>';
		
		$('#modal_dialg').html(html);
		//打开窗口
		openWindow('modal_dialg',h,w,itemChecked[0].name);
		
	}
}


//根据code查询帮助信息
function getSubtoolHelpData(code){
	$.ajax({
		type : "get",
		dataType : "json",
		contentType: "application/json;charset=UTF-8",
		url : "subtool/getSubtoolHelpData/"+code+"/all",
		success : function(data) {
			if(data!=null){
				if(typeof(data[0])!='undefined'){
					$('#subtoolHelp').text(data[0].helpContext);
				}
			}
		},
		error : function(data) {
			var error = JSON.parse(data.responseJSON);
			showNotify(error.message,"error");
		}
	});
}

//执行命令
function execution(){
	
	$.ajax({
		type : "post",
		url : "subtool/sendCommand",
		data:{
			"operationType":$('input:radio[name=radio2]:checked').val(),
			"checkedName":itemChecked[0].name,
			"command":paramas_cmd.join(",")
		},
		success : function(data) {
			
			if(null!=data){
				if(data.ok){
					showNotify("命令下发成功","warning");
					setInterval(function(){
						if(needRefresh && retryTime > 0){
							dataSource.read(searchResult);
						}
						retryTime--;
					},5000); //指定5秒刷新一次
				}else if(data.zero || data.no){
					if(data.zero){
						showNotify("命令未匹配到ip","warning");
					}
					if(data.no){
						showNotify("命令未包含imsi or msisdn","warning");
					}
					
				}else{
					showNotify("执行命令失败","warning");
				}
				//关闭窗口
				dialogObj.close();
				$("#unitChecked:checked").prop("checked", false);
				$('input:radio[name=checkItem]').prop('checked',false);
				//$('input:radio[name=radio1]').prop('checked',false);
				//默认选择单用户数操作
				$("input:radio[name='radio1']").eq(1).prop("checked",false);
				$("input:radio[name='radio1']").eq(0).prop("checked",true);
			}
		},
		error : function(data) {
			//var error = JSON.parse(data.responseJSON);
			showNotify("执行命令失败","error");
		}
	});
	
}



function setParameValue(){
	
	paramas_cmd = [];
	var cmdData = [];
	var command  = itemChecked[0].command;
	var cmds = command.split(";");
	$.each(cmds,function(index,command){
		//按照顺序替换动态值
		if(command!=""){
			$.each(numberData,function(k,itemParems){
				var cmd = command;
				if(itemParems.length>0){
					$.each(itemParems,function(x,value){
						if(cmd.indexOf("$"+(x + 1))!=-1){
							if(null!=value){
								cmd = cmd.replace("$" + (x + 1),value);
							}
						}
					});
					paramas_cmd.push(cmd+";"); 
				}
			});
		}
	});
	$('#form-confirm-command').val(paramas_cmd.join('\n'));
}

function confirmCommand(type){
	if(type==1 && numberData.length==0){
		showNotify("请上传后缀名为xls的文件","warning");
		return false;
	}
	
	var params = itemChecked[0].params;
	var flag = true;
	if('null'!=params && params != null && params != ''){
		var str = params.split(",");
		
		//只限单用户操作
		if(type==0){
			var cmdValue = [];
			$.each(str,function(index,item){
				var itemval = item.replace(/[ ]/g,"");
				var value = $("#input_value_"+itemval).val();
				if(null==value ||""==value){
					showNotify(itemval+"项不能为空","warning");
					flag = false;
					return false;
				}else{
					if(value.length==11 && (itemval=="msisdn" || itemval=="number" || itemval=="MSISDN")){
						value = "86"+value;
					}
					cmdValue.push(value);
				}
			});
			numberData.push(cmdValue);
			$('#batchNumber').hide();
		}else{
			$('#batchNumber').show();
		}
	}
	if(flag){
		//$参数替换
		setParameValue();
		//关闭上个窗口
		dialogObj.close();
		$("#executeCommandButton").removeAttr("disabled");
		$('#commandName').val(itemChecked[0].name);
		//打开窗口
		openWindow('confirmCommandModal','400','700','确认命令下发');
		
	}

}


function filtersData(name,cmdType){
	
    var filters = [];
	if(cmdType)
    	filters.push({field: "cmdType", operator: "eq", value: cmdType});
	if(name)
    	filters.push({field: "name", operator: "contains", value: name});
	
 	commandGridDataSource.filter(filters);
 	commandGridDataSource.fetch();
	
}

//获取subtool命令类型
function getSubtoolCmdType(){
	
	var subToolDataSource= new kendo.data.DataSource({
		transport: {
			read: {
				type : "GET",
				url: "subtool/category/search",
				dataType: "json"
			}
		}
	});
	
	$("#subtoolType").kendoDropDownList({
		optionLabel:"--全部命令类型--",
	    dataSource: subToolDataSource,
	    filter: "contains",
	    autoBind: false,
	    //suggest: true
	    change: function(){
        	//数据过滤
	    	var itemName = $('#cmdName').val();
			var cmdType = $('#subtoolType').val();
			filtersData(itemName,cmdType);
        }
	});

}

//执行结果
function resultGridData(){
	
	 dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "get",
                url: "subtool/userDataCheck/search"
            },
            parameterMap: function (options, operation) {
            	if (operation == "read") {
                	//动态组装条件参数
            		searchResult.page = options.page;
            		searchResult.pageSize = options.pageSize;
            		searchResult.exeResults = $('#exeResult').val();
            		searchResult.startTime = $('#startTime').val();
            		searchResult.endTime = $('#endTime').val();
                    return searchResult;
                }
            }
        },
        batch: true,
        pageSize: 10,
        schema: {
            data: function (data) {
               return data.content;
            },
        total: function (data) {
            return data.totalElements;//总条数
        	}
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	 userDataGrid = $("#resultData").kendoGrid({
		
		  dataSource:dataSource,
		  height: 375,
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
	      columns: [
		              {field:"id",hidden:true},
	                  { width:135,field: "createTime", title:"时间"},
	                  { width:130,field: "userNumber", title:"用户号码" },
	                  { width:130,field: "checkName", title:"检查项"},
	                  { width:100,field: "createName", title:"操作人"},
	                  { width:100,field: "exeResults", title:"结果",
	                	template: "#var resultText = exeResults=='0'?'成功':'失败';"
	                		     + "var color = exeResults=='0'?'green':'red';#"
	                	 		 + "<font color='#:color#'>#:resultText#</font>"
	                  },
	                  { width:40,field: "filePath", title:"原始报文",
	                	/*template: "<button type='button' class='btn btn-success btn-xs'"
	                		    + "onclick='showLog(this)'>显&nbsp;&nbsp;示</button>"*/
	                	template: "#if(exeResults==0){#"
	                		    + "<button type='button' class='btn btn-success btn-xs'"
	                		    + "onclick='openUser(this)'>显&nbsp;&nbsp;示</button>"
	                		    + "#}else{#"
	                		    + "<button type='button' class='btn btn-danger btn-xs'"
	                		    + "onclick='showLog(this)'>显&nbsp;&nbsp;示</button>"
	                		    //+ "&nbsp;&nbsp;<button type='button' class='btn btn-success btn-xs'"
	                		   // + "onclick='downLoadLog(this)'>下&nbsp;&nbsp;载</button>"
	                		    +"#}#"
	                  }
	             ]
	    }).data("kendoGrid");
	
}

//打开用户数据查询
function openUser(d){
	
	var userNumber = userDataGrid.dataItem($(d).closest("tr")).userNumber;
	window.location.href="userDataQuery?number="+userNumber;
}

//下载
function downLoadLog(d){
	var filePath = userDataGrid.dataItem($(d).closest("tr")).filePath;
	window.location.href="subtool/downUserData?filePath="+filePath;
}
//查询日志
function showLog(d){
	
	var context = userDataGrid.dataItem($(d).closest("tr")).errorMessage;
	//弹出窗口
	openWindow('textModal','416','815',' 查看内容');
	if("null"!=context){
		$('#logContext').html(context);
	}else{
		$('#logContext').html("");
	}
	
}

function clear(){
	
	var file = $("#fileName");
	file.after(file.clone().val(""));
	file.remove();
	
	itemChecked = [];

	paramsChecked =[];

	contextvalue ="";

	type = "";

	ratext = "";

	numberData = [];

	paramas_cmd = [];
}