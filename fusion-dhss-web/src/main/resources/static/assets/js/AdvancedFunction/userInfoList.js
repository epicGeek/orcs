$(function(){
	
	var rulePars = window.results.rulePars;
	
	//循环tab
	$.each(rulePars, function(index,value) {  
		//添加tab标题
		var $li = $("<li role='presentation'></li>").appendTo($("#liList"));
		$("<a href='#"+(value.tabName).replace(/ /g,"")+"' aria-controls='"+value.tabName+"' role='tab' data-toggle='tab'>"+value.tabName+"</a>").appendTo($li);
		
		//添加tabpanel
		var $tabPanel = $("<div role='tabpanel' class='tab-pane' id='"+(value.tabName).replace(/ /g,"")+"'></div>").appendTo($("#tabContent"));
		
		var $row = $("<div class='row'></div>").appendTo($tabPanel);
		
		//遍历一级，一级对象为：first_level_json
		$.each(value, function(first_level_index,first_level_json) { 
			
			//跳出当前循环，继续下次循环
			if(first_level_index == 'tabName'){
				return true;
			}
			
			var $col = $("<div class='col-md-6'></div>").appendTo($row);
			var $first_forge_heading = $("<div class='forge-heading'></div>").appendTo($col);
			
			var $first_form_horizontal = $("<div class='form-horizontal'></div>").appendTo($first_forge_heading);
			
			if(typeof first_level_json == "object"){ //如果是一级对象
				
				if(first_level_json.showType){ //如果没有子级（二级）：只有一个字段
					
					showType(first_level_json.path, $first_form_horizontal);
					
				} else {//如果有子级
					
					//判断一级是否有标题
					if(first_level_json.zhName){
						$("<h3>"+first_level_json.zhName+"</h3>").prependTo($first_forge_heading);
					}
					//遍历二级
					$.each(first_level_json,function(second_level_index,second_level_json){
						
						//跳出当前循环，继续下次循环
						if(second_level_index == 'zhName'){
							return true;
						}
						
						if(second_level_json.showType){ //如果没有子级（三级）：只有一个字段
					
							showType(second_level_json.path,$first_form_horizontal);
							
						} else {//如果有子级（三级）
							
							$secondNav = $("<div class='row secondNav'></div>").appendTo($first_form_horizontal);
							
							//判断二级是否有标题
							if(second_level_json.zhName){
								$("<h4>"+second_level_json.zhName+"</h4>").prependTo($secondNav);
							}
							
							//遍历三级
							$.each(second_level_json, function(third_level_index,third_level_json) {    
								
								//跳出当前循环，继续下次循环
								if(typeof third_level_json == "object"){ //如果是三级对象    
									showType(third_level_json.path, $secondNav);
								}
							});
						}
						
					});
				}
				
			} 
		});
	});
	
	$("#liList > li:eq(0)").addClass("active");
	$("#tabContent > div:eq(0)").addClass("active");
	
	//将没有字段的内容从页面删除
	var $colmd6 = $(".tab-pane > .row > .col-md-6");
	for(var i=0; i<$colmd6.length; i++){
		var md = $colmd6.eq(i);
		var notnull = md.find("input[type='text'],input[type='checkbox'],input[type='radio'],select option");
		if(notnull.length == 0){
			md.detach();
		}
	}
});


function showType(path, $form_horizontal) {
	
	var keyAll = window.results.keyAll;
	
	var dataKey = keyAll[path];
	
	var i;
	
	if(dataKey){
		
		for(i=0; i<dataKey.length; i++){
			
			var dataJson = dataKey[i];
			
			switch (dataJson.showType) {
				
				case "text":
					
					 if(dataJson.path){
							
							$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
							
							$("<label class='col-sm-4 control-label'>"+dataJson.zhName+"</label>").appendTo($form_group);
							
							var $inputWrap = $("<div class='col-sm-6'></div>").appendTo($form_group);
							
							$("<input class='form-control' type='text' placeholder='' value='"+dataJson.path+"'>").appendTo($inputWrap);
					}
					
					break;
		
				/*case "select":
				
					var pathArr = new Array();
					
					var secondIndex = 0;
					$.each(dataJson,function(index,value){
						if(index == "zhName" || index == "showType"){
							return true;
						}
						if(value.path){
							for(var i=0; i < value.path.length; i++){
								if(!pathArr[i]){
									pathArr[i] = new Array();
								}
								pathArr[i][secondIndex] = value.path[i];
							}
							secondIndex = secondIndex+1;
						}
					});
					
					for(var j=0;j<pathArr.length;j++){
						$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
						$("<label class='col-sm-4 control-label'>"+dataJson.zhName+"</label>").appendTo($form_group);
						var $inputWrap = $("<div class='col-sm-6'></div>").appendTo($form_group);
						var $select = $("<select></select>").appendTo($inputWrap);
						
						for(var k=0;k<pathArr[j].length; k++){
							var val = pathArr[j][k];
							$("<option>"+val+"</option>").appendTo($select);
						}
					}
					
					break;
		
				case "checkbox":
					$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
					$("<label class='col-sm-4 control-label'>"+dataJson.zhName+"</label>").appendTo($form_group);
					var $inputWrap = $("<div class='col-sm-6'></div>").appendTo($form_group);
					$.each(dataJson,function(index,value){
						if(index == "zhName" || index == "showType"){
							return true;
						}
						
						var val;
						if(value.valueDes){
							val = value.valueDes;
						} else if(value.path){
							val = value.path;
						}
						var $label = $("<label class='checkboxText'></label>").appendTo($inputWrap);
						$label.html("<input type='checkbox' />"+val);
					});
					
					var pathArr = new Array();
					
					var secondIndex = 0;
					$.each(dataJson,function(index,value){
						if(index == "zhName" || index == "showType"){
							return true;
						}
						if(value.path){
							for(var i=0; i < value.path.length; i++){
								if(!pathArr[i]){
									pathArr[i] = new Array();
								}
								pathArr[i][secondIndex] = value.path[i];
							}
							secondIndex = secondIndex+1;
						}
					});
					
					for(var j=0;j<pathArr.length;j++){
						$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
						$("<label class='col-sm-4 control-label'>"+dataJson.zhName+"</label>").appendTo($form_group);
						var $inputWrap = $("<div class='col-sm-6'></div>").appendTo($form_group);
						var $select = $("<select></select>").appendTo($inputWrap);
						
						for(var k=0;k<pathArr[j].length; k++){
							var val = pathArr[j][k];
							$("<option>"+val+"</option>").appendTo($select);
						}
					}
					break;
		
				case "radio":
					$form_group = $("<div class='form-group'></div>").appendTo($form_horizontal);
					$("<label class='col-sm-4 control-label'>"+dataJson.zhName+"</label>").appendTo($form_group);
					var $inputWrap = $("<div class='col-sm-6'></div>").appendTo($form_group);
					$.each(dataJson,function(index,value){
						if(index == "zhName" || index == "showType"){
							return true;
						}
						
						var val;
						if(value.valueDes){
							val = value.valueDes;
						} else if(value.path){
							val = value.path;
						}
						var $label = $("<label class='radioText'></label>").appendTo($inputWrap);
						$label.html("<input type='radio' name='"+dataJson.zhName+"'/>"+val);
					});
					break;*/
			}	
		}
	}
}

