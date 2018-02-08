
var searchparams = {page:1, size: 10,searchField:""};
var dataSource;
var userGrid;
var postParams = {};
var url = "";
var flag = false;//默认不重复

function initDataSource(){
	
	dataSource=new kendo.data.DataSource({
            transport: {
                read: {
                    type: "GET",
                    url:"platform/user/search",
                    dataType: "json",
                    contentType: "application/json;charset=UTF-8"
                },
                parameterMap: function(options, operation) {
                    if (operation == "read") {
                        searchparams.page = (options.page-1);
                        searchparams.size = options.pageSize;
                        searchparams.searchField = $("#inputKeyWord").val();
                        return searchparams;
                    }
                    
                }
        },
        schema: {
            data: function (data) {
            	 return data._embedded["system-user"];//返回页面数据
            },
            total: function (data) {
            	return data.page.totalElements; //总条数
            },
            pageSize: function(data) {
                return data.page.size; 
            }
        },
        requestEnd: function(e) {
            var response = e.response;

            if(response){
                var type = e.type;
                if(type !='read'){
                     this.read();                  
                }
            }else{
            	infoTip({content: "服务器异常，请重试！",color:"#088703"});
            }        
        },
        pageSize: 10,
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
      
    });
}

$(function($){
		
	//初始化数据
	initDataSource();
	
    //初始化 Grid
	userGrid =  $("#userGrid").kendoGrid({
	   dataSource: dataSource,
		reorderable: true,
		resizable: true,
		sortable: true,
		pageable: true,
		toolbar: kendo.template($("#template").html()),
		columns: [
		          { field: "userName", title:"登录名称"},
		          { field: "realName", title:"用户姓名"},
		          { field: "mobile", title:"联系方式"},
		          { field: "email", title:"电子邮箱"},
		          {
					template: "<a class='updateBtn btn btn-warning btn-xs'>修&nbsp;&nbsp;改</a> " +
							"<a class='deleteBtn btn btn-danger btn-xs'>删&nbsp;&nbsp;除</a> " +
							"<a class='resetPassword btn btn-success btn-xs'>重置密码</a>",
					encoded: false,
					title: "<span  title='操作'>操作</span>"
				  }
		      ]
		          
	}).data("kendoGrid");
	//查询
	$('#searchBtn').on('click', function() {
		
		searchparams.searchField = $('#inputKeyWord').val();
		if(!checkContentLength(searchparams.searchField,20,"名称")){return;};
		$("#userGrid").data("kendoGrid").pager.page(1);
		//条件查询重新加载 
		dataSource.read(searchparams);
	});
	//重置
	$('#resetBtn').on('click',function(){
		
		$('#inputKeyWord').val("");
		searchparams.searchField="";
		dataSource.read(searchparams);
	});
  
	//添加按钮
	$("#addBtn").on("click",function(){
		$('#myModal').modal('show');
		$("#pwd1").show();
		$("#pwd2").show();
		modalObj.clearModal();
	});
	
	//编辑按钮
	$('body').delegate(".updateBtn", 'click', function() {
		
		$('#myModal').modal('show');
		var data = userGrid.dataItem($(this).closest("tr"));
		//postParams.id= data.id;
		url = data._links.self.href;
		$("#pwd1").hide();
		$("#pwd2").hide();
		modalObj.setModal(data);
		
	});
	
	//删除按钮
	$('body').delegate(".deleteBtn", 'click', function() {
		var data = userGrid.dataItem($(this).closest("tr"));
		if(confirm("确认删除吗？")){
			deleteData(data._links.self.href);
		}
	});
	
	$('body').delegate(".resetPassword", 'click', function() {
		var data = userGrid.dataItem($(this).closest("tr"));
		resetPwd(data);
	});
	
	//弹窗
	modalObj = {
			
		//清空弹窗内的数据
		clearModal: function(){
			$("#userName").val("");         
			$("#realName").val("");       
			//$("#onePwd").val("");       
			//$("#twoPwd").val("");        
			$("#mobile").val("");     
			$("#email").val("");  
		},
		
		//告警级别 处理方式 扣除分数 是可以修改
		//修改设置弹窗内的数据
		setModal: function(data){
			$("#userName").val(data.userName);         
			$("#realName").val(data.realName);       
			$("#mobile").val(data.mobile);   
			$("#email").val(data.email);
			
		},
		//弹窗保存按钮
		saveBtn: function(){
			$("#saveBtn").on("click",function(){
				postParams.userName = $("#userName").val();         
				postParams.realName = $("#realName").val();
				postParams.mobile = $("#mobile").val();     
				postParams.email = $("#email").val();
				/*if(url==""){
					postParams.encryptedPassword = $("#onePwd").val();       
					if(checkIsNull(postParams.encryptedPassword,'密码')){return;} 
				}*/
                //add update 数据验证
				if(checkIsNull(postParams.userName,'登录名称')){return;};
				if(!checkContentLength(postParams.userName,20,'登录名称')){return;};
				if(checkIsNull(postParams.realName,'用户姓名')){return;};
				if(!checkContentLength(postParams.realName,20,'用户姓名')){return;};
				if(!numberAndLength(postParams.mobile,'联系方式')){return;};
				if(!CheckMail(postParams.email,'电子邮箱')){return;};
				
				//验证重复
				VerificationRepeat(postParams.userName);
				if(flag){return;};
				
				if(url!=""){
						postAddAndEdit('PATCH',url);
				}else{
						postAddAndEdit('POST','rest/system-user/');
				}
				
			});
		}
	};
	//添加 修改保存
	modalObj.saveBtn();
	
});


/**
 * 新增、修改请求函数
 * @param type
 * @param url
 */
function postAddAndEdit(type,url){
	console.log(url);
	$.ajax({
		url:url,
		type:type,
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
				$('#myModal').modal('hide');
				infoTip({content: "保存成功！",color:"#088703"});
				dataSource.read(searchparams);
		},
		complete: function(XMLHttpRequest, textStatus) {
				if(XMLHttpRequest.status==201){
					$('#myModal').modal('hide');
					infoTip({content: "保存成功！",color:"#088703"});
					dataSource.read(searchparams);
				}
		},
		fail:function(error){
			infoTip({content: "保存失败！",color:"#f60a0a"});
		},
	});
}


//删除
function deleteData(url){
	
	$.ajax({
		url:url,
		type:"delete",
		dataType:"json",
		contentType:"application/json;charset=utf-8",
		data:kendo.stringify(postParams),
		success:function(data){
			infoTip({content: "删除成功！",color:"#088703"});
			dataSource.read(searchparams);
		},
		fail:function(error){
			infoTip({content: "删除失败！",color:"#f60a0a"});
		},
		
	});
}


function resetPwd(data){
	
	var userUrl = data._links.self.href;
	var userId = userUrl.substring(userUrl.lastIndexOf("/")+1);
	var d = new Date();
	var str = "";
	  	str += d.getFullYear();
	  	str += d.getMonth() + 1 < 10 ? "0" + (d.getMonth() + 1) : (d.getMonth() + 1);
	  	str += d.getDate() < 10 ? "0" + d.getDate() : d.getDate();
	if(confirm('重置'+data.realName+'的密码\n重置后密码的格式为：登录名 + "_Cz" + 当前年月日\n例如："'+data.userName+'_Cz'+str+'"')){
		
		$.ajax({
			url:"system-user/reset-password/"+userId,
			type : "PATCH",
			//dataType:"json",
			contentType:"application/json;charset=utf-8",
			success : function(data) {
				infoTip({content: "重置成功！",color:"#088703"});
			},
			fail : function(data) {
				infoTip({content: "重置失败！",color:"#f60a0a"});
			}
		});
	}
};

//添加、修改，验证是否重复
function VerificationRepeat(valiDateField){
	
	$.ajax({
		url : "platform/VerificationRepeat",
		type : "POST",
		data:{
			"valiDateField":valiDateField,
			"type":"loginName"
		},
		async:false,
		success : function(data) {
			if(data>0 && data!=null){
					infoTip({content: "登陆名称不允许重复！",color:"#f60a0a"});
					flag = true;
			}
		},
		fail : function(data) {
			infoTip({content: "验证失败！",color:"#f60a0a"});
		}
	});
}
