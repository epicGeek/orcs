var count  = 0,tabCount =0,req_count = 0;//请求次数
var $row = "",level_2_zhName = "",level_3_zhName = "",level_2_html ="",$leftPanel,$rightPanel;
var level_1_path = "",unitName = "",name = "",mobile = "";
var put = [],put_3 = [],putobj = [];
var json =null;
var map = {};
var flag = true;
var area=1;


//下载日志
function downloadLog(path){
	//var tr = $(e.target).closest("tr"); 
	//var data = this.dataItem(tr);
	window.location.href="userData/downxml?filePath="+path;
}

//打开xml
function openUserData(filePath,numberSection,name,type){
	if(type==0){
		dialogObj.close();
	}
	//var tr = $(e.target).closest("tr");
	//unitName =  this.dataItem(tr).unitName;
	unitName = name;
	//重复请求控制
	if(req_count==0){
		var r = numberSection.match("^86.*");
		if(null!=r){
			$('input[name="radio1"][value="1"]').attr('checked', true);
		}else{
			$('input[name="radio1"][value="2"]').attr('checked', true);
		}
		$('#contextvalue').val(numberSection);
		$('#liList').empty();
		$('#tabContent').empty();
		//获取当前的numberSection数据
		queryData(numberSection,filePath);
		req_count++;
	}
	//重置
	req_count= 0;
}

//查询用户数据
function queryData(type,contextvalue){
	
	var url_code = $('#isAdvanced').val();
	console.log(url_code);
	var code = typeof(url_code)!='undefined'?url_code:'false';
	$.ajax({
		url : "userData/getUserData/"+type+"/"+contextvalue+"/all/"+code,
		type : "get",
		dataType : "json",
		success : function(data) {
			if(data!=null && data.keyAll!=null && null!=data.rulePars){
				if(data.pattern=="N"){
					showNotify('建议在网元管理中绑定号段!',"warning");
				}
				if(data.unitName){
					unitName = data.unitName;
				}
				keyAll = data.keyAll;
				var rulePars = data.rulePars;
				iterator(rulePars);
			}else{ 
				showNotify('没有查到用户数据!',"warning");
			}
		},
		fail : function(data) {
			showNotify(data.message,"error");
		}
	});
}

function iterator(rulePars){
	
	htmlClear();
	
	//update 2015-08-03 增加tab排序
	//循环tab
	$.each(rulePars, function(index,value) {
		
		var hrefStr = value.tabName.replace(/\s/g, "");
		//添加tab标题
		var $li = $("<li role='presentation'></li>").appendTo($("#liList"));
		$("<a href='#"+hrefStr+"' aria-controls='"+hrefStr+"' role='tab' data-toggle='tab'>"+hrefStr+"</a>").appendTo($li);
		$li.siblings("li.active").removeClass("active");
		$li.addClass("active");
		
		//添加tabpanel
		var $tabPanel = $("<div role='tabpanel' class='tab-pane' id='"+hrefStr+"'></div>").appendTo($("#tabContent"));
		$tabPanel.siblings(".tab-pane.active").removeClass("active");
		$tabPanel.addClass("active");
		
		$row = $("<div class='row'></div>").appendTo($tabPanel);
		
		$leftPanel = $("<div class='col-md-6'></div>").appendTo($row);
		$rightPanel = $("<div class='col-md-6'></div>").appendTo($row);
		
		//遍历一级，一级对象为：first_level_json
		
			$.each(value, function(first_level_index,first_level_json) {
				
				//跳出当前循环，继续下次循环
				if(first_level_index == 'tabName'||first_level_index == 'parentName'){
					//tabCount = keyAll[first_level_json]
					return true;
				}
				
				if(typeof first_level_json == "object"){ //如果是一级对象
					
					if(first_level_json.showType){ //如果没有子级（二级）：只有一个字段
						
						level_1(first_level_json);
						
					} else {//如果有子级
							
						//遍历二级
						$.each(first_level_json,function(second_level_index,second_level_json){
								
								//跳出当前循环，继续下次循环
								if(second_level_index == 'zhName' ||second_level_index == 'parentName'){
									return true;
								}
								mobile = second_level_index;
								//console.log(second_level_json);
								if(second_level_json.showType){ //如果没有子级（三级）：只有一个字段
									if(tabCount>1){
										return true;//表示多个值一级处理、跳出循环
									}
									getLevel_2(2,second_level_json,first_level_json);
								} else {//如果有子级（三级）
									//遍历三级
									$.each(second_level_json, function(third_level_index,third_level_json) {    
										if(third_level_json.showType){
											getLevel_2(3,third_level_json,second_level_json);
										}
										
									});
									//清空数据
									//put_3 = [];
									//put = [];
								}
							
							
						});
						//put = [];//清空数据
					}
				} 
				tabCount = 0;
			});
	});
	
	$("#liList > li:eq(0)").addClass("active").siblings('.active').removeClass("active");
	$("#tabContent > div:eq(0)").addClass("active").siblings('.active').removeClass("active");
	if(unitName){
		if(area==0){
			hlrOrhssBtn(unitName);
		}else{
//			var unitHtml ="&nbsp;&nbsp;<label id='labelUnit'>单元名称："+unitName+"</label>";
//			$("#isUnit").html(unitHtml);
			$("#labelUnit").html("单元名称："+unitName);
		}
		$('#isUnit').show();
	}else{
		$('#isUnit').hide();
	}
}

function webFunction(text){
	if(text == ""){
		infoTip({
			content : "路径为空!"
		});
	}else{
		window.open(text,'newwindow'+new Date());
	}
}

function hlrOrhssBtn(unitName){
	
	 $.get("userData/getUnit",{"unitName":unitName}, function(unit){
		 $.ajax({
				url: "rest/equipment-unit/"+unit.id+"/webInterface",
				dataType:"json",
				async: false,
				success:function(data){
					var text = "";
					if(data._embedded){
						if(data._embedded){
							var arrs = data._embedded["equipment-webinterface"];
							$.each(arrs,function(index,item){
							    var r = item.interfaceType.split("_");
							   var btnName = r[r.length-1];
								text += "<button name='web' onclick = 'webFunction(\""+item.url+"\")' "
								     +"class='btn btn-info'> "+
									btnName+" </button><span name='web'>&nbsp;&nbsp;</span>";
							
						    })
							
						}
						
//						var unitHtml ="&nbsp;&nbsp;<label style='font-size: 20px;color: red;'>单元名称："+unitName+"</label>";
//						$("#isUnit").html(text+unitHtml);
						$("#labelUnit").html("单元名称："+unitName);
					}
				}
		 
		});
	 });
}


function getValueSize(showKey){
	
	var size = "";
	var keyObj = keyAll[showKey];
	if(null!=keyObj){
		size = keyObj.length;
	}
	return size;
	
}
/**
 * 一级对象处理
 * @param showKey 标签key
 * @param level_json 节点数据
 */
function level_1(second_level_json){
	
	var $col = "";
	var $first_forge_heading = "";
	var $first_form_horizontal = "";
	if(typeof(second_level_json.showType)!='undefined'){
		var $currentPanel = $leftPanel.height()<=$rightPanel.height()?$leftPanel:$rightPanel;
		$col = $("<div class='col-md-12'></div>").appendTo($currentPanel);
		$first_forge_heading = $("<div class='forge-heading'></div>").appendTo($col);
		$first_form_horizontal = $("<div class='form-horizontal'></div>").appendTo($first_forge_heading);
		if(second_level_json.showType=='text'){
			var value = keyAll[second_level_json.path];
			showType('is',value,second_level_json, $first_form_horizontal);
		}else{
			showType('is',null,second_level_json, $first_form_horizontal);
			
		}
	}
}

/**
 * 多个标签值处理
 * @param index 第几个值
 * @param level 级别
 * @param second_level_json
 * @param first_level_json
 */
function getLevel2_many(index,level,second_level_json,first_level_json){
	if(second_level_json.showType=='text'){
		var _vType = keyAll[second_level_json.path];
		var value = "";
		if(_vType instanceof Array){
			value = _vType[index];
		}else{
			value = _vType;
		}
		getNotText(index,level,value,second_level_json,first_level_json);
	}else{
		//其他类型处理 second_level_json对象
		getNotText(index,level,null,second_level_json,first_level_json);
	}
}

/**
 * 二级节点数据处理
 * @param second_level_json
 * @param first_level_json
 */
function getLevel_2(level,second_level_json,first_level_json){
	
if(second_level_json.showType=='text'){
		
		var _vType = keyAll[second_level_json.path];
		var size = 0;
		if(_vType instanceof Array){
			size = _vType.length;
		}
		//mobile 处理不节点属性显示个数不统一规则过滤处理
		if(size>1 && tabCount==0 && mobile!='featuresNotSupportedBySGSN'){
			tabCount++;
			for(var i=0;i<size;i++){
				$.each(first_level_json,function(level_2_index,level_2_json){
					if(level_2_index == 'zhName' ||level_2_index == 'parentName'){
						return true;
					}
					if(level_2_json.showType){
						getLevel2_many(i,level,level_2_json,first_level_json);
					}else{
						$.each(level_2_json, function(third_level3_index,third_level3_json) {    
							if(third_level3_json.showType){
								getLevel2_many(i,3,third_level3_json,level_2_json);
							}
							
						});
					}
				});
				level_3_zhName = "";//重置为空
			}
			
		}else if(tabCount==0){
			var value = "";
			if(size>1){
				for(var k=0;k<size;k++){
					value = keyAll[second_level_json.path][k];
					getNotText('is',level,value,second_level_json,first_level_json);
				}
			}else{
				value = keyAll[second_level_json.path];
				getNotText('is',level,value,second_level_json,first_level_json);
			}
		}
	}else if(tabCount==0){
		//其他类型处理 second_level_json对象
		getNotText('is',level,null,second_level_json,first_level_json);
	}
}

/**
 * 二级级对象处理 Text 
 * @param showKey 标签key
 * @param level_json 节点数据
 */
function levelIsText(level,second_level_json,first_level_json){
	if(typeof(second_level_json.showType)!='undefined'){
		var keyObj = keyAll[second_level_json.path];
		getNotText(level,keyObj,second_level_json,first_level_json);
	}
}

/**
 * 多个相同标签写入同一个col-md-6里面
 * @param level 级别
 * @param keyObj 根据key获取的[]值
 * @param second_level_json 当前json对象
 * @param first_level_json 上层json对象
 */
function getNotText(index,level,value,second_level_json,first_level_json){
	
	var $col = "";
	var $first_forge_heading = "";
	var $first_form_horizontal = "";
	if(level==3){
		
		if(level_3_zhName!=first_level_json.zhName){
			$secondNav = $("<div class='row secondNav'></div>").appendTo(put[count]);
			$("<h4>"+first_level_json.zhName+"</h4>").prependTo($secondNav);
			$first_form_horizontal = $secondNav;
			level_3_zhName = first_level_json.zhName;
			put_3[count] = $first_form_horizontal;
		}else{
			$first_form_horizontal = put_3[count];
		}
		
	}else if(level==2){
		
		if(level_2_zhName!=first_level_json.zhName){
			if(first_level_json.zhName){//二级节点zhName 中文描述
				var $currentPanel = $leftPanel.height()<=$rightPanel.height()?$leftPanel:$rightPanel;
				$col = $("<div class='col-md-12'></div>").appendTo($currentPanel);
				$first_forge_heading = $("<div class='forge-heading'></div>").appendTo($col);
				$first_form_horizontal = $("<div class='form-horizontal'></div>").appendTo($first_forge_heading);
				$("<h3>"+first_level_json.zhName+"</h3>").prependTo($first_forge_heading);
				level_2_zhName = first_level_json.zhName;
				put[count] = $first_form_horizontal;//存放当前标签位置，以便下次使用
			}
		}else{
			$first_form_horizontal = put[count];
		}
	}
	
	showType(index,value,second_level_json, $first_form_horizontal);
		
   }
/**
 * index 0：表示获取多个值时、只显示第一个的title，is:表示非多个值为空显示title
 * keyValue:lebel显示的值
 * dataJson 表示当前对象，
 * $form_horizontal html动态添加内容
 */
function showType(index,keyValue,dataJson, $form_horizontal) {
	var $form_group = "";
	if(keyValue==""){
		if(index==0 || index=='is'){
			$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
			if(typeof(dataJson.zhName)!='undefined'){
				$("<label class='col-sm-4 label-title' title='"+dataJson.zhName+"'>"+dataJson.zhName+"</label>").appendTo($form_group);
			}
		}
		return;//取值为空只显示zhName
	}else{
		$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
		dataJson.zhName = dataJson.zhName?dataJson.zhName:"";
		//if(typeof(dataJson.zhName)!='undefined'){
		$("<label class='col-sm-4 label-title' title='"+dataJson.zhName+"'>"+dataJson.zhName+"</label>").appendTo($form_group);
		//}
	}
/*//	var $inputWrap = $("<label class='col-sm-6 font-normal'></label>").appendTo($form_group);
*/	var xx = 0;//值为空不显示select
	var divc = 0;
	
	if(keyValue!="" && typeof(keyValue)!='undefined'){
		$("<label class='col-sm-7 label-value'>"+keyValue+"</label>").appendTo($form_group);
	}
	
	/*switch (dataJson.showType) {
		case "text":
			if(keyValue!="" && typeof(keyValue)!='undefined'){
//				$("<input class='form-control' type='text' placeholder='' value='"+keyValue+"'>").appendTo($inputWrap);
				$("<span>"+keyValue+"</span>").appendTo($inputWrap);
			}
			break;

		case "select":
			var $select = "";
			$.each(dataJson,function(index_name,value){
				if(index_name == "zhName" || index_name == "showType"){
					return true;
				}
				var sValue = "";
				if(index=='is'){
					 sValue = keyAll[value.path];
				}else{
					sValue = keyAll[value.path][index];
				}
				if(typeof(value.value)!='undefined' && typeof(sValue)!='undefined'){
					if(xx==0){
						$select = $("<select class='form-control'></select>").appendTo($inputWrap);
					}
					if(sValue==value.value){
						$("<option value='"+keyValue+"'  selected = 'selected'>"+value.valueDes+"</option>").appendTo($select);
					}else{
						$("<option value='"+keyValue+"'>"+value.valueDes+"</option>").appendTo($select);
					}
					xx++;
				}else{
					if(divc==0){
						$("<div style='height: 35px;'></div>").appendTo($inputWrap);
					}
					divc++;
				}
			});
			
			break;

		case "checkbox":
			
			$.each(dataJson,function(index_name,value){
				if(index_name == "zhName" || index_name == "showType"){
					return true;
				}
				var sValue = "";
				if(index=='is'){
					 sValue = keyAll[value.path];
				}else{
					if(keyAll[value.path] instanceof Array){
						sValue = keyAll[value.path][index];
					}else{}
					
					
				}
				if(typeof(value.value)!='undefined' && typeof(sValue)!='undefined'){
					if(sValue==value.value){
						var $label = $("<label class='checkboxText'></label><br>").appendTo($inputWrap);
						$label.html("<input type='checkbox' checked='checked' />"+value.valueDes);
					}else{
						var $label = $("<label class='checkboxText'></label><br>").appendTo($inputWrap);
						$label.html("<input type='checkbox' />"+value.valueDes);
					}
				}else{
					if(divc==0){
						$("<div style='height: 35px;'></div>").appendTo($inputWrap);
					}
					divc++;
				}
			});
			break;

		case "radio":
			
			$.each(dataJson,function(index_name,value){
				if(index_name == "zhName" || index_name == "showType"){
					return true;
				}
				var sValue = "";
				if(index=='is'){
					 sValue = keyAll[value.path];
				}else{
					sValue = keyAll[value.path][index];
				}
				if(typeof(value.value)!='undefined' && typeof(sValue)!='undefined'){
					if(sValue==value.value){
						var $label = $("<label class='radioText'></label>").appendTo($inputWrap);
						$label.html("<input type='radio' checked='checked' name='"+dataJson.zhName+"'/>"+value.valueDes);
					}else{
						var $label = $("<label class='radioText'></label>").appendTo($inputWrap);
						$label.html("<input type='radio'  name='"+dataJson.zhName+"'/>"+value.valueDes);
					}
				}else{
					if(divc==0){
						$("<div style='height: 35px;'></div>").appendTo($inputWrap);
					}
					divc++;
				}
				
			});
			break;
	}*/
}

function htmlClear(){
	$('#liList').empty();
	$('#tabContent').empty();
	count  = 0;
	$row = "";
	flag = true;
	put = [];
	put_3 = [];
	level_2_zhName = "";
	level_3_zhName = "";
	level_2_html ="";
	level_1_path = "";
}





