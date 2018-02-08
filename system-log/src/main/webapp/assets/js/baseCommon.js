
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
/*
*//**
 * 修改、添加
 * @param logData
 *//*
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
*/
//初始化工作包类型
function initWorkType(id,type){
	
	$.ajax({
		dataType : 'json',
	    type : "GET",
		url : "/queryAll",
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
 * 产品、项目、阶段等公用方法
 * @param type
 */
function initTable(type){
	
	$.ajax({
		dataType : 'json',
	    type : "GET",
		url : "/queryAll",
		data:{
			"type":type
		},
		success : function(data) {},
		error:function(e){}
	});
	
}


