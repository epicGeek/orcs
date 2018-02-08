//验证密码,
//长度在6~18之间，只能包含字符、数字和下划线
function checkPassword(str) {
    var reg = /^\w{6,18}$/;
    return reg.test(str);
}

//初始化 下拉框 控件
function initCondition() {
	
	//产品
	initSelect("productInput","product");
	//项目
	initSelect("projectInput","project");
    //阶段
	initSelect("productPeriodInput","stage");
	//工作包类型
	initWorkType("workTypeInput","workType");
	//工作包
	initWorkPackage("workContentInput",[]);
};

/**
 * 删除数据
 * @param id
 */
function delLogData(ids){
	var dtd=$.Deferred();
	$.ajax({
		dataType : 'json',
	    type : "post",
		url : "systemLog/deleteLog/"+ids,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
            //$("#tableWrap .checkedAll").prop("checked", false);
			/*if(tableObj){
				tableObj.fnReloadAjax();
			}*/
			dtd.resolve(data);
			
		},
		fail:function(e){
			dtd.reject(e);
		}
	});
	return dtd.promise(); 
}

/**
 * 修改、添加
 * @param logData
 */
function addOrEditLogData(logData){
	var dtd=$.Deferred();
	$.ajax({
		dataType : 'json',
	    type : "POST",
		url : "systemLog/saveOrEditLog",
		contentType : "application/json;charset=UTF-8",
		data:JSON.stringify(logData),
		success : function(data) {
			dtd.resolve(data);
		},
		fail:function(e){
			dtd.reject(e);
		}
	});
	return dtd.promise();
}

//初始化工作包类型
function initWorkType(id,type){
	
	$.ajax({
		dataType : 'json',
	    type : "GET",
		url : "select/infoAll",
		data:{
			"type":type
		},
		success : function(data) {
			if(data){
				 var $select = $('#'+id).selectize({
				        options:data,
				        valueField: 'id',
				        labelField: type,
				        onChange: function(e) {
				        	//工作包
				        	initWorkPackage("workContentInput",data[e-1].items);
				        }
				 });
				 
				 //选中工作包第一项
				 var selectize = $select[0].selectize;
				 selectize.setValue(data[0].id);

					//生成工作包类型
					 initWorkPackage("workContentInput",data[0].items);
			}
		},
		error:function(e){
			
		}
	});
	
}



//根据工作包类型获取工作包
function initWorkPackage(id,data){
	if(data.length>0){
		var workContentInput = $("#workContentInput").data("selectize");
		workContentInput.clearOptions();
		workContentInput.addOption(data);
		workContentInput.setValue(data[0].id);
	}else{
		var $select = $('#'+id).selectize({
			options:data,
			valueField: 'id',
			labelField: 'workPackage'
		});

		 var selectize = $select[0].selectize;
		 if(data.length > 0){
			 selectize.setValue(data[0].id);
		 }
		 
	}
	
}

/**
 * 产品、项目、阶段等下拉公用方法
 * @param id
 * @param type
 */
function initSelect(id,type){
	
	$.ajax({
		dataType : 'json',
	    type : "GET",
		url : "select/infoAll",
		data:{
			"type":type
		},
		success : function(data) {
			if(data){
				if('project'==type){
					type='projectName';
				}
				var $select = $('#'+id).selectize({
				        options:data,
				        valueField: 'id',
				        labelField: type
				 });
				 var selectize = $select[0].selectize;
				 selectize.setValue(data[0].id);
			}
		},
		error:function(e){
			
		}
	});
	
}


