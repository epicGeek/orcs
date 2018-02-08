var categoryId = $("#category_id").val();
var category_cn = $("#category_cn").val();
var category_code = $("#category_code").val();
console.log("categoryId="+categoryId+";category_cn="+category_cn+";category_code="+category_code);

if(category_code == "network"){
	$("#navList a.active").removeClass("active");
	$("#navList li:eq(3) a").addClass("active");
}else if(category_code == "remote"){
	$("#navList a.active").removeClass("active");
	$("#navList li:eq(2) a").addClass("active");
}else if(category_code == "environment"){
	$("#navList a.active").removeClass("active");
	$("#navList li:eq(1) a").addClass("active");
}

var searchparams = {
		categoryId:categoryId,
		neTypeId:'',
		keyWord : '',
	    neId:'',
	    unitTypeId:'',
	    cmdtypeId:''
};
var resultparamrs ={
		page: 0,
		size: 10,
		categoryId:categoryId,
		neTypeId:'',
		unitTypeId:'',
		sort:"requestTime,desc"
};
var windowObj;
var unitChecked = [];
var paramsChecked =[];
var itemChecked = [];


//弹窗
function openWindow(id,h,w,titleName){
	if (!$("#"+id+"").data("kendoWindow")) {
		$("#"+id+"").kendoWindow({
			  height: h+'px',
			  width:w+'px',
			  title:titleName,
			  modal: true
		});
     }
     windowObj = $("#"+id+"").data("kendoWindow");
     windowObj.center().open();
}
var LogInfoWindow;
//点击下载日志
function downloadLog(id){
//	window.location.href = "/downloadLog?id=" + id;
	$.ajax({
		dataType : "json",
		url : "maintainOperateion/showloadLog",
		data : {id : id},
		success : function(data) {
			LogInfoWindow.obj.setOptions({"title":"日志"});
			LogInfoWindow.initContent({dataLog:data});
		}
	});
}

function confirmCommand(k){
	var params = itemChecked[0].params;
	var flag = true;
	if('null'!=params && params != null && params != ''){
		var str = params.split(",");
		$.each(str,function(index,item){
			 valueStr = str[index];
			var value = $("#input_value_"+valueStr).val();
			if(null==value ||""==value){
				showNotify("表单填写项不能为空","warning");
				flag = false;
				return false;
			}else{
				paramsChecked.push(value);
				//console.log(paramsChecked);
			}
			
		});
	}
	if(flag){
		if(k!=1){
			//关闭上个窗口
			windowObj.close();
		}
		
		//动态参数值获取回填
		var unitStr = "";
		if(unitChecked){
			$.each(unitChecked,function(index,unit){
				unitStr += unit.unit+",";
			});
		}
		
		var itemStr = "";
		if(itemChecked){
			$.each(itemChecked,function(index_checkItem,checkItem){
				itemStr += checkItem.command+",";
			});
		}

		var paramStr = "";
		if(paramsChecked){
			$.each(paramsChecked,function(index_params,paramIte){
				paramStr += paramIte+",";
			});
		}
		//console.log(paramStr);
		itemStr = itemStr.substring(0, itemStr.length-1);
		unitStr = unitStr.substring(0, unitStr.length-1);
		paramStr = paramStr.substring(0, paramStr.length-1);
		$('#form-confirm-command').val(itemStr);
		$('#form-confirm-unit').val(unitStr);
		$('#form-confirm-param').val(paramStr);
		
		//弹出窗口
		openWindow('submitWindow','350','520','确认下发指令');
	}
 }
$(function() {
	

	LogInfoWindow = {
			
			obj: undefined,
			
			template: undefined,
			
			id: $("#windowTemplate"),
			//取消
			cancelClick: function(){
				$("#cancelBtn").on("click",function(){
					LogInfoWindow.obj.close();
				});
			},
			
			initContent: function(dataItem){
				
				//填充弹窗内容
				LogInfoWindow.obj.content(LogInfoWindow.template(dataItem));
				
				LogInfoWindow.cancelClick();
				
				LogInfoWindow.obj.center().open();
			},
			
			init: function(){
				
				this.template = kendo.template($("#windowTemplate").html());
				
				if (!LogInfoWindow.id.data("kendoWindow")) {
					LogInfoWindow.id.kendoWindow({
						width: "700px",
						actions: ["Close"],
						modal:true,
						title: "号段管理"
					});
				}
				LogInfoWindow.obj = LogInfoWindow.id.data("kendoWindow");
			}
		};
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(1) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(1) ul li:eq(1)").addClass("active");
	
	//选择指令-高度
	$("#instructionWrap").height($(window).height() - $("#instructionWrap").offset().top - 130);		
	var unitTypeMap = {};
	var neMap = {};
	$.ajax({
		dataType : 'json',
		type : "GET",
		async: false,
		url : "rest/equipment-neType",
		success : function(data) {
			var html = "";		
			var defaultneTypeselect = 0;
			var defaultunitTypeselect = 0;
			if(data._embedded){
				  $.each(data._embedded["equipment-neType"],function(index,item){
					  if(index == 0){
						  defaultneTypeselect = item.id;
						  searchparams.neTypeId = item.id;
					  }
					  html += '<option value="' + item.id + '">' + item.neTypeName + '</option>';
					  if(item._embedded){
						  console.log(item._embedded);
						  unitTypeMap[item.id] = item._embedded["unitType"];
						  neMap[item.id] = item._embedded["Ne"];						 
					  }			
				  });
			}			
			$('#select-ne-type').html(html);
			$("#select-ne-type option[value='"+defaultneTypeselect+"']").attr("selected", "selected");
			
			 var ne = neMap[defaultneTypeselect];
			 var unitType = unitTypeMap[defaultneTypeselect];
			 var unitType_html = '';
			 var ne_html = '';
			 ne_html += '<option  value ="">全部</option>';
			 if(unitType.length > 0 ){			 
				 $.each(unitType,function(index,item){
					 if(index == 0){
           			      defaultunitTypeselect = item.id;
						  searchparams.unitTypeId = item.id;
					  }
					 unitType_html += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
				 });  
			 } 
             if(ne.length > 0 ){           	 
            	 $.each(ne,function(index,item){
            		
            		 ne_html += '<option value="' + item.id + '">' + item.neName + '</option>';
 				 }); 
			 }
             $('#select-unit-type').html(unitType_html);
             $("#select-unit-type option[value='"+defaultunitTypeselect+"']").attr("selected", "selected");
             $('#select-ne').html(ne_html);  
             refushItem();
		}
	});
	
	$('#select-ne-type').on('change',function (event){		
		 var neType_id= $(this).children('option:selected').prop("value");	
		 var ne = neMap[neType_id];
		 var unitType = unitTypeMap[neType_id];
		 var html = "";
		 if(unitType){
			 $.each(unitType,function(index,item){
					html += '<option value="' + item.id + '">' + item.unitTypeName + '</option>';
				});  
		 } else{
			 html += '<option  value ="">请选择网元类型</option>';
		 }
		 var html1 = "";
		 if(ne){
			 $.each(ne,function(index,item){
					html1 += '<option value="' + item.id + '">' + item.neName + '</option>';
				});  
		 } else{
			 html1 += '<option  value ="">请选择网元类型</option>';
		 }
        $('#select-ne').html(html1);
        $('#select-unit-type').html(html);
	 });	
	
	var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: "/equipment-ne/search/list",
                dataType: "json",
                contentType: "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                    return searchparams;                   
                }
            }
        }
    });
	function refushItem(){
	   $.ajax({
		  dataType : 'json',
		  type : "GET",
		  data : searchparams,
		  url : "check-item/list/search",
		  success : function(data) {
			  var html = "";
			  $.each(data,function(index,item){
				  html += "<tr id='check-item-" + item.id + "'><td><input type='radio' value='" + item.id + "'"
			       +" name=\"checkItem\" params=\"" + item.params + "\" account=\""+item.account+"\" command=\"" + item.command + "\" itemName=\"" + item.name + "\"/></td><td>" + item.name + "</td></tr>";
			  });
			  $("#operationsTable").html(html);
		   }
	    });	
	}
	//单元列表
	$("#queryGrid").kendoGrid({		
		dataSource: dataSource,		
		height:$(window).height() - $("#queryGrid").offset().top - 80,		
		reorderable: true,
		resizable: true,
		sortable: true,
		columnMenu: true,
		pageable: false,
		columns: [{
			width: 30,
			template: "<input type='checkbox' />",
			attributes:{
				"class": "text-center"
			},
			title: "<input type='checkbox' id='selectAllCheckbox'/>"
		}, {
			template: "<span  title='#:neName#'>#:neName#</span>",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "unitName",
			template: "<span  title='#:unitName#'>#:unitName#</span>",
			title: "<span  title='单元'>单元</span>"
		}, {
			field: "ip",
			template: "<span  title='#:serverIp#'>#:serverIp#</span>",
			title: "<span  title='地址'>地址</span>"
		}, {
			field: "unitType",
			template: "<span  title='#:unitType#'>#:unitType#</span>",
			title: "<span  title='单元类型'>单元类型</span>"
		}],
		dataBound: function(){
			//全选按钮
			$("#selectAllCheckbox").on("click",function(){
				$("#queryGrid .k-grid-content input[type='checkbox']").prop("checked",$(this).prop("checked"));
			});
			
			//取消全选
			$("#queryGrid .k-grid-content input[type='checkbox']").on("click",function(){
				if(!$(this).prop("checked")){
					$("#selectAllCheckbox").prop("checked",false);
				}
			});
		}
	});
	
      $('#searchNetWork').on('click',function (event){
		
		searchparams.neId = $("#select-ne").val();
    	searchparams.unitTypeId = $("#select-unit-type").val();
		searchparams.keyWord = $("#input-search").val();
		searchparams.neTypeId = $("#select-ne-type").val();
		dataSource.read(searchparams);
		refushItem();
	  });

      var resultdataSource = new kendo.data.DataSource({
          transport: {
              read: {
                  type: "GET",
                  url: "maintain-result/search/page",
                  dataType: "json",
                  contentType: "application/json;charset=UTF-8"
              },
              parameterMap: function (options, operation) {
                  if (operation == "read") {
                	  resultparamrs.page = options.page -1 ;
                      return resultparamrs;                   
                  }
              }
          },
          batch: true,
          pageSize: 10,
          requestEnd: function(e) {
              var response = e.response;
              if(response){
                  var type = e.type;
                  if(type !='read'){
                       this.read();                  
                  }
              }else{
                  alert("服务器异常，请重试！");
              }
              
          },
          schema: {
              data: function (data) {
              	var rdata = [];
              	if(data._embedded){
              		$.each(data._embedded["maintain-operation"],function(index,item){
                  		/*item.ne = item._embedded["ne"];
      					item.unit = item._embedded["unit"];
      					item.checkName = item._embedded["operation"].commandCheckItem.name;*/
                  		rdata.push(item);
                  	});
              	}
                  return rdata;//返回页面数据
              },
              total: function(d) {
                  return d.page.totalElements; 
              },
              pageSize: function(d) {
                  return d.page.size; 
              }
          },
         serverPaging : true,
  		serverFiltering : true,
  		serverSorting : true
      });
	//执行结果
	var resultGridObj = $("#resultGrid").kendoGrid({
		
		dataSource: resultdataSource,
		
		height:$(window).height() - $("#resultGrid").offset().top - 75,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		columns: [{
			field: "requestTime",
			template: "#if(requestTime != null ){#<span  title='#:requestTime#'>#:requestTime#</span> #}#",
			title: "<span  title='时间'>时间</span>"
		}, {
			field: "checkName",
			template: "<span  title='#:checkName#'>#:checkName#</span>",
			title: "<span  title='检查项'>检查项</span>"
		}, {
			field: "isDone",
			template: "#if(isDone == 0){#"+
					  "<div class='progress'><div class='progress-bar progress-bar-warning progress-bar-striped active' style='width: 70%;'>执行中……</div></div>"+
					  "#} else {#"+
					  "完成，<a class='btn btn-link'  onclick='downloadLog(\"#:id#\")'><i class='glyphicon glyphicon-download-alt'></i>下载</a>"+
					  "#}#",
			title: "<span  title='结果'>结果</span>"
		}],
		dataBound: function(){
			$(".loginBtns").on("click", function(){
				alert("连接超时 5 秒");
			});
		}
	}).data("kendoGrid");
	

	
	
	
	
	
	//提交执行
	$("#submitBtn").on("click",function(){
		unitChecked = [];
		itemChecked = [];
		paramsChecked =[];
		if($("#queryGrid .k-grid-content input[type='checkbox']:checked").length <=0){
			infoTip({"content":"请选择单元列表！"});
		} else if($("#operationsTable input[type='radio']:checked").length <=0){
			infoTip({"content":"请选择指令！"});
		} else {
			//windowObj.center().open();
			var checkedUnitInput = $("#queryGrid .k-grid-content tbody tr input[type='checkbox']:checked");
			var queryGrid= $("#queryGrid").data("kendoGrid");
		    var unitStr = "";
			
			$.each(checkedUnitInput,function(index,value){
				var data = queryGrid.dataItem($(value).closest("tr"));				
				var u = {};
				u.neId =data.ne.id;
				u.neName =data.ne.neName;
				u.id = data.id;
				u.unit = data.unitName;
				unitChecked.push(u);			
				
			});
			var checkedRedio = $("#operationsTable input[type='radio']:checked");
			var itemName = checkedRedio.attr("itemName");
			var params = checkedRedio.attr("params") == "null" ? "":checkedRedio.attr("params");
			alert(params);
			var i = {};
			i.id  =  checkedRedio.attr("value");
			i.name = checkedRedio.attr("itemName");
			i.command = checkedRedio.attr("command");
			i.params = params;
			i.account = checkedRedio.attr("account");			
			itemChecked.push(i);					
		    if('null'==params || ''==params || params == null){//直接下发执行命令，无弹出窗口
		    	//$('#modal_dialg').hide();
				confirmCommand(1);
			}else{//用户需填写所需命令参数值，弹出窗口
				var html = '<div class="modal-body"><div class="form-horizontal">';
				var valueStr = "";
				var str = params.split(",");//动态参数多个用逗号分隔		
				$.each(str,function(index,item){
					
					   valueStr = str[index];
					   html+='<div class="form-group">'
						   +'<label class="col-sm-2 control-label">'+valueStr+'</label>'
						   + '<div class="col-sm-7">'
					       +'<input type="text" class="form-control"  id="input_value_'+valueStr+'" placeholder="'+valueStr+'"></div></div>';
					   //console.log(valueStr);
				});
				html+='<div class="form-group">'
					+'<label class="col-sm-2 control-label"></label>'
		            +'<div class="col-sm-7">'
		            +'<div><button onclick="confirmCommand(0)" type="button" class="btn btn-success"'
		            +'style="width: 100%;padding: 10px 0;"><i class="glyphicon glyphicon-ok"></i>提交执行</button></div>'
		            +'</div></div>'
		            +'</div></div>';
					console.log(html);
					$('#modal_dialg').html(html); 
					openWindow('modal_dialg','250','500',itemName);
					
			}
			
		}
	});
	
	//弹窗-确定按钮
	$("#dialogOKBtn").on("click",function(){
		
		var postData = {
				"unitList" : unitChecked,
				"itemList" : itemChecked,
				"category" : categoryId,
				"paramsList" : paramsChecked
			};
		
		$.ajax({
			type : "POST",
			dataType : "json",
			contentType: "application/json;charset=UTF-8",
			url : "maintain-operation/sendCommand",
			data : JSON.stringify(postData),
			success : function(data) {
				infoTip({"content":"命令下发成功！"});
				$("#queryGrid .k-grid-content tbody tr input[type='checkbox']:checked").prop("checked", false);
				$("#operationsTable input[type='radio']").prop('checked',false);
				resultdataSource.read(resultparamrs);
				windowObj.close();
			}
		});
		
		/*result.push(data);
		var dataSource = new kendo.data.DataSource({
			data: result
		});
		windowObj.close();
		resultGridObj.setDataSource(dataSource);*/
	});
});

