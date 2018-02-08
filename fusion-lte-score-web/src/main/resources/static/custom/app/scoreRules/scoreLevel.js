kendo.culture("zh-CN");

var levelGrid;

var modalObj; 

var postParams = {};

var searchParams = {scorefrom:'',scoreto:'',level:''};

var dataSource;

//分数等级js
$(function() {
	
		
		 dataSource = new kendo.data.DataSource({
	        transport: {
	            read: {
	                type: "GET",
	                dataType : "json",
					contentType : "application/json;charset=UTF-8",
	                url: "scorelevel/search"
	            },
	            parameterMap: function (options, operation) {
	                if (operation == "read") {
		                 searchParams.page = options.page;
		                 searchParams.pageSize = options.pageSize;
		                 return searchParams;
	                } else {
	                	return kendo.stringify(options);
	                }
	                
	            }
	        },
	        pageSize: 10,
	        schema: {
	            data: function (data) {
	            	 return data.content;//返回页面数据
	            },
	            total: function (data) {
	            	return data.totalElements; //总条数
	            },
	        },
	        serverPaging : true,
			serverFiltering : true,
			serverSorting : true
	    });
		
		//列表
		levelGrid = $("#levelList").kendoGrid({

			dataSource: dataSource,
			reorderable: true,
			resizable: true,
			sortable: true,
			pageable: true,
			toolbar: kendo.template($("#template").html()),
			columns: [
			          { field: "scorefrom", title:"最低分"},
	                  { field: "scoreto", title:"最高分"},
	                  { field: "level", title:"等级"},
	                  { field: "updatetime", title:"修改时间"},
					  {
					    template: "<a class='updateBtn btn btn-warning btn-xs'>修&nbsp;&nbsp;改</a><a class='deleteBtn btn btn-danger btn-xs'>删&nbsp;&nbsp;除</a>",
					    encoded: false,
					    title: "<span  title='操作'>操作</span>"
					  }],
		
		}).data("kendoGrid");
		
		//查询条件
		$('#searchBtn').on('click', function() {
			searchParams.scorefrom = $('#scorefromInput').val();
			searchParams.scoreto = $('#scoretoInput').val();
			searchParams.level = $('#levelInput').val();
			if(!validataNumber(searchParams.scorefrom)){return;};
			if(!validataNumber(searchParams.scoreto)){return;};
			if(!validataNumber(searchParams.level)){return;};
			$("#levelList").data("kendoGrid").pager.page(1);
			//条件查询重新加载 
			dataSource.read(searchParams);
		});
		$("#scorefromInput").on('keyup', function(){
			searchParams.scorefrom = $('#scorefromInput').val();
			dataSource.read(searchParams);
		});
		$("#scoretoInput").on('keyup', function(){
			searchParams.scoreto = $('#scoretoInput').val();
			dataSource.read(searchParams);
		});
		$("#levelInput").on('keyup', function(){
			searchParams.level = $('#levelInput').val();
			dataSource.read(searchParams);
		});
		
		// 重置
		$('#resetBtn').click(function(){
			 $('#scorefromInput').val("");
			 $('#scoretoInput').val("");
			 $('#levelInput').val("");
			 searchParams.scorefrom ="";
			 searchParams.scoreto ="";
			 searchParams.level ="";
			 dataSource.read(searchParams);
		});
		
		//添加按钮
		$("#addBtn").on("click",function(){
			$('#myModal').modal('show');
			modalObj.clearModal();
		});
		
		//编辑按钮
		$('body').delegate(".updateBtn", 'click', function() {
			
			$('#myModal').modal('show');
			var data = levelGrid.dataItem($(this).closest("tr"));
			postParams.id= data.id;
			modalObj.setModal(data);
			
		});
		
		$('body').delegate(".deleteBtn", 'click', function() {
			var data = levelGrid.dataItem($(this).closest("tr"));
			if(confirm("确认删除吗？")){
				deleteData(data.id);
			}
		});
		
	//弹窗
	modalObj = {

		//清空弹窗内的数据
		clearModal: function(){
			$("#lowScore").val("");
			$("#highScore").val("");
			$("#level").val("");
		},
		
		//设置弹窗内的数据
		setModal: function(data){
			$("#lowScore").val(data.scorefrom);
			$("#highScore").val(data.scoreto);
			$("#level").val(data.level);
		},
		
		//弹窗保存按钮(新增、修改)
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				postParams.scorefrom = $("#lowScore").val();
				postParams.scoreto = $("#highScore").val();
				postParams.level = $("#level").val();
				postParams.updatetime = new Date();
				
				if(!validataNumber(postParams.scorefrom)){return;};
				if(!validataNumber(postParams.scoreto)){return;};
				if(!validataNumber(postParams.level)){return;};
				
				if(postParams.id!="" && typeof(postParams.id)!="undefined"){
					postAddAndEdit('PATCH','rest/scorelevel/'+postParams.id);
				}else{
					postAddAndEdit('POST','rest/scorelevel/');
				}
			});
		}
	};
	//gridObj.init();
	//添加修改保存
	modalObj.saveBtn();
	
	//导出
    $("#btn-export").on("click",function(){
    	window.location.href = "scorelevel/exportFile?scorefrom=" +$('#scorefromInput').val()
    	+"&scoreto="+$('#scoretoInput').val()+"&level="+$('#levelInput').val();
	});
	
	//模板下载
    $("#downFile").on("click",function(){
    	downFile();
	});
	
	//上传
	$("#upload").kendoUpload({
		localization:{
			"select": "选择上传文件……",
			"dropFilesHere": "拖拽文件到此区域",
		},
		multiple: true,
		async: {
			saveUrl: "scorelevel/uploadFile",
			//removeUrl: "remove",
			autoUpload: true,
		},
		template: kendo.template($('#fileTemplate').html()),
		complete: function(){
			//console.log("complete");
			//window.location.href='paraProcessing-addUploadList.html';
		},
		success: function(){
			infoTip({content: "上传成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		error: function(){
			infoTip({content: "上传失败！",color:"#f60a0a"});
		}
	});
	
	
});

//模板下载
function downFile(){
	window.location.href="scorelevel/downFile";
}

/**
 * 新增、修改请求函数
 * @param type
 * @param url
 */
function postAddAndEdit(type,url){
	$.ajax({
		url:url,
		type:type,
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			$('#myModal').modal('hide');
			infoTip({content: "保存成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		complete: function(XMLHttpRequest, textStatus) {
			if(XMLHttpRequest.status==201){
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#088703"});
				dataSource.read(searchParams);
			}
		},
		fail:function(error){
			infoTip({content: "保存失败！",color:"#f60a0a"});
		},
		
	});
}
//删除
function deleteData(id){
	
	$.ajax({
		url:"rest/scorelevel/"+id,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			infoTip({content: "删除成功！",color:"#088703"});
			dataSource.read(searchParams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
	});
}

//添加判断
function validataNumber(value){
	
	if(!/^[0-9]*$/.test(value)){
		showNotify("请输入数字!","warning");
        return false;
    }else{
    	//输入正确判断大小
    	if(value>100 ||value<0){
    		showNotify("请输入0——100的数字!","warning");
		        return false;
    	}
    }
	return true;
}
