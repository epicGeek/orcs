
var tableListObjs;
var parameters={};

$(function() {

    $("[data-toggle='tooltip']").tooltip();//激活 tooltip
    
    initUserSelect();  //用户下拉

     tableListObjs = {
        tableObj: undefined,
        //生成表格
        initTable: function() {
            if (tableListObjs.tableObj) {
                tableListObjs.tableObj.destroy();
            }
            this.tableObj = $('#dataTables').dataTable({
               // responsive: true,
            	"bProcessing" : true,
    			"bServerSide" : true,
    			"bDestroy" : true,
    			"bSort" : false,// 排序
    			"bLengthChange" : false, // 改变每页显示数据数量
    			"bFilter" : false, // 过滤功能
    			"oLanguage" : { // 国际化配置
    				"sProcessing" : "正在获取数据，请耐心等待...",
    				"sLengthMenu" : "显示 _MENU_ 条",
    				"sZeroRecords" : "没有您要搜索的内容",
    				"sInfo" : "从 _START_ 到  _END_ 条记录 总记录数为 _TOTAL_ 条",
    				"sInfoEmpty" : "记录数为0",
    				"sInfoFiltered" : "(全部记录数 _MAX_ 条)",
    				"sInfoPostFix" : "",
    				"sSearch" : "搜索",
    				"sUrl" : "",
    				"oPaginate" : {
    					"sFirst" : "&lt;&lt;",
    					"sPrevious" : "&lt;",
    					"sNext" : "&gt;",
    					"sLast" : "&gt;&gt;"
    				}
    			},
                "ajax": {
                    "url": "query/userData",
                    "type": "POST",
                    "data": function(d) {
	       				$.extend(d, getParameters());
	       				return d;
                    }
                },
                "fnDrawCallback": function() {
                    //激活 tooltip
                      $('div[data-toggle="tooltip"]').tooltip();
                  },
                "columns": [{
                    title: "", //以防响应式（点击按钮）出现bug
                    "render": function(data, type, full, meta) {
                        return "";
                    }
                }, {
                	width:'10',
                    "title": "<input type='checkbox' class='checkedAll'/>",
                    "class": "text-right",
                    "render": function() {
                            return "<input type='checkbox' class='itemCheckbox'/>";
                    }
                }, {
                    "title": "操作",
                    "render": function() {
                            return   "<button class='deleteOneLogBtn btn btn-xs btn-default '>删除</button>&nbsp;&nbsp;" +
                                "<button class='editOneLogBtn btn btn-xs btn-default '>编辑</button>";
                    }
                }, {
                    "title": "登陆名称",
                    "data": "userName"
                },{
                    "title": "用户姓名",
                    "data": "realName"
                }, {
                    "title": "联系方式",
                    "data": "mobile"
                },  {
                    "title": "电子邮箱",
                    "data": "email"
                }],
                "oLanguage": {
                    "sProcessing": "正在加载中......",
                    "sLengthMenu": "",
                    "sInfo": "<button id='removeLogsBtn' type='button' class='btn btn-default btn-sm pull-left'>批量删除</button>",
                    "sInfoEmpty": "",
                    "sInfoFiltered": "",
                    "sInfoPostFix": "",
                    "sSearch": "",
                    "sUrl": "",
                    "oPaginate": {
                        "sFirst": "<<",
                        "sPrevious": "<",
                        "sNext": ">",
                        "sLast": ">>"
                    }
                }
            });
        },

        //行内编辑按钮
        editOneLog: function() {
            $('#tableWrapId').on('click', '.editOneLogBtn', function() {
            	$('#myModal').modal('show');
                formObj.currentDataItem = tableListObjs.tableObj.fnGetData($(this).parents("tr"));
                formObj.setItem();
            });
        },

        //批量删除按钮，需要判断是数据库中数据还是未添加到数据库里的数据
        deleteMultiLogs: function() {
        	$("#tableWrapId").delegate("#removeLogsBtn","click",function(){
                var postRemoveData = [];
                var checkedBox = $("#dataTables tbody .itemCheckbox:checked");
                var selectedTr, dataItem;

                if (checkedBox.length <= 0) {
                    layer.tips('请至少选择一条数据！', this);
                } else {
                    //询问框
                    layer.confirm('确认删除选中数据吗？', {
                        btn: ['确认', '取消'] //按钮
                    }, function() {
                        //确认事件
                    	var parameters=[];
                        for (var i = checkedBox.length - 1; i >= 0; i--) {
                            selectedTr = checkedBox.eq(i).closest('tr');
                            dataItem =  tableListObjs.tableObj.fnGetData(selectedTr);
                            parameters.push(dataItem.id);
                        }
                        if(parameters.length>0){
                        	$.when(delUserData(parameters.join(","))).done(function(data){
                            	if(data==0){//删除成功
                            		if(tableListObjs.tableObj){
                        				tableListObjs.tableObj.fnReloadAjax();
                        			}
                                	layer.msg('删除成功！', { icon: 1 });
                            	} else {
                            		layer.msg('删除失败！', { icon: 2 });
                            	}
                            })
                            .fail(function(){
                        		layer.msg('删除失败！', { icon: 2 });
                            });
                        }
                        $("#tableWrapId .checkedAll").prop("checked", false);
                    }, function() {});
                }

            });
        },

        //行内删除
        deleteSingleLog: function() {
        	
            $('#tableWrapId').on('click', '.deleteOneLogBtn', function() {

                    var that = $(this);
                    layer.confirm('确认删除数据么？', {  //询问框
                        btn: ['确认', '取消'] //按钮
                    }, function() {
                        //确认事件
                        var selectedTr = that.closest('tr');
                        var dataItem = tableListObjs.tableObj.fnGetData(selectedTr);
                        $.when(delUserData([dataItem.id])).done(function(data){
                        	if(data==0){//删除成功
                        		if(tableListObjs.tableObj){
                    				tableListObjs.tableObj.fnReloadAjax();
                    			}
                            	layer.msg('删除成功！', { icon: 1 });
                        	} else {
                        		layer.msg('删除失败！', { icon: 2 });
                        	}
                        })
                        .fail(function(){
                    		layer.msg('删除失败！', { icon: 2 });
                        });
                    }, function() {});
                });
        },

        //全选和取消全选
        checkboxSelect: function() {
            $('#tableWrapId').delegate('.checkedAll','click', function() {
                $('#tableWrapId tbody .itemCheckbox').prop("checked", $(this).prop("checked"));
            });
            $('#tableWrapId').delegate('.itemCheckbox','click', function() {
                if (!$(this).prop("checked")) {
                    $('#tableWrapId .checkedAll').prop("checked", false);
                }
            });
        },

        init: function() {
            this.initTable();
            this.checkboxSelect();
            this.editOneLog();
            this.deleteMultiLogs();
            this.deleteSingleLog();
        }
    };
     

     var formObj = {
         currentDataItem:undefined,
         //设置字段值
         setItem: function() {
             var dataItem = formObj.currentDataItem;
             $("#dataId").html(dataItem.id);
             $("#userNameEn").val(dataItem.userName);
             $("#realNameCn").val(dataItem.realName);
             $("#email").val(dataItem.email);
             $("#number").val(dataItem.mobile);
         },
         //获取字段值
         getItem:function(){
        	 var dataItem = formObj.currentDataItem;
             return {
            	 id:dataItem.id,
             	 userName:$("#userNameEn").val(),
             	 realName:$("#realNameCn").val(),
	             email: $("#email").val(),
	             mobile: $("#number").val(),
                 status: false
             };
         },
         
     	addUserObj:function() {
 	            userName: $("#userNameEn").val();
 	            realName: $("#realNameCn").val();
 	            email: $("#email").val();
 	            mobile: $("#number").val();
 	            status: false
     	},
         
         //保存按钮
         saveEdit: function() {
        	 
             $("#saveEditBtn").on("click", function() {
            	 
            	 var newObj = formObj.getItem();
            	 var otherBool = false, emailBool=false;
            	 var reg=/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;  //邮箱
            	 
          	 if(formObj.getItem() =="" || formObj.getItem() ==undefined){
            		 alert("添加");
                	 var addObj = formObj.addUserObj();
                     if ($("#userNameEn").val() == "" || $("#userNameEn").val()== 'null') {
                             $("#forDescTip").show();   //必填项
                             window.setTimeout(function() {  $("#forDescTip").hide();  }, 3000);
                             otherBool = false;
                     }else if($("#realNameCn").val() == "" || $("#realNameCn").val() == 'null'){
                    	  $("#forDesc").show();
                          window.setTimeout(function() { $("#forDesc").hide(); }, 3000);
                          otherBool = false;
                     } else {
                     	otherBool = true;
                     }
                     if($("#email").val() == "" || $("#email").val() =="null"){
                    	 emailBool = true;
                     }else{
                    	 if(reg.test($("#email").val())){  //验证邮箱
                    		 emailBool = true;
                    	 }else{
                    		 layer.confirm('请输入正确的邮箱格式', {
                    			 btn: ['确认', '取消'] //按钮
                    		 });
                    		 emailBool = false;
                    	 }
                     }
                     if(otherBool && emailBool){
                     	var edits = [];
                     	edits.push(addObj);
                         $.when(addOrEditLogData(edits)).done(function(data){ //ajax
                         	if(data == true || data == "true"){
                             	$("#tableWrapId .checkedAll").prop("checked", false);
                     			if(tableListObjs.tableObj){
                     				tableListObjs.tableObj.fnReloadAjax();
                     			//	tableListObjs.tableObj.fnGetData();
                     			}
                             	layer.msg('添加成功！', { icon: 1 });
                         	} else {
                         		layer.msg('添加失败！', { icon: 2 });
                         	}
                         })  .fail(function(){
                     		layer.msg('添加失败！', { icon: 2 });
                         });
                      }
                   
            		 
            	 }else {
            	 
                 if (newObj.userName == "" || newObj.userName == 'null') {
                         $("#forDescTip").show();   //必填项
                         window.setTimeout(function() {  $("#forDescTip").hide();  }, 3000);
                         otherBool = false;
                 }else if(newObj.realName == "" || newObj.realName == 'null'){
                	  $("#forDesc").show();
                      window.setTimeout(function() { $("#forDesc").hide(); }, 3000);
                      otherBool = false;
                 } else {
                 	otherBool = true;
                 }
                 if(newObj.email == "" || newObj.email =="null"){
                	 emailBool = true;
                 }else{
                	 if(reg.test(newObj.email)){  //验证邮箱
                		 emailBool = true;
                	 }else{
                		 layer.confirm('请输入正确的邮箱格式', {
                			 btn: ['确认', '取消'] //按钮
                		 });
                		 emailBool = false;
                	 }
                 }
                 if(otherBool && emailBool){
                 	var edits = [];
                 	edits.push(newObj);
                     $.when(addOrEditLogData(edits)).done(function(data){ //ajax
                     	if(data == true || data == "true"){
                         	$("#tableWrapId .checkedAll").prop("checked", false);
                 			if(tableListObjs.tableObj){
                 				tableListObjs.tableObj.fnReloadAjax();
                 			//	tableListObjs.tableObj.fnGetData();
                 			}
                         	layer.msg('修改成功！', { icon: 1 });
                     	} else {
                     		layer.msg('修改失败！', { icon: 2 });
                     	}
                     })  .fail(function(){
                 		layer.msg('修改失败！', { icon: 2 });
                     });
                  }
               }
             });
         },
         init: function(argument) {
         //    this.cancelEdit();
             this.saveEdit();
         }
     };

     $('#addBtn').on('click', function() {
    	 
      	$("#myModal").modal('show');
          /*formObj.currentDataItem = tableListObjs.tableObj.row($(this).closest("tr")).data();
          formObj.setItem();*/
    
      });
     
     //查询 按钮
     $("#queryBtn").on("click", function() {
         //根据ajax重新加载表格
         tableListObjs.tableObj.fnReloadAjax();
     });
     //重置 按钮
     $("#resetBtn").on("click", function() {
     	resetParameters();
     	//根据ajax重新加载表格
         tableListObjs.tableObj.fnReloadAjax();
     });
     
    tableListObjs.init();
    formObj.init();
  
});

//获取查询条件参数
function getParameters() {
	
	var userName =$("#userName").val()=='admin'?$("#selectUser").val():$("#realName").val();
	if(userName== "--全部人员--"){
		parameters.userName="";
	}else{
		 parameters.userName = userName;
	}
    return parameters;
}

//用户名下拉
function initUserSelect(){
	$.ajax({
		dataType : 'json',
	    type : "GET",
		url : "select/infoAll",
		data:{
			"type":"user"
		},
		success : function(data) {
			if(data){
				var html = "<option value='--全部人员--'>--全部人员--</option>";
				$.each(data,function(index,item){
					html+="<option value='"+item+"'>"+item+"</option>";
				});
				$('#selectUser').html(html);
				$('#selectUser').selectize({
					create: true,
					sortField:'text'
				});
			}
		},
		error:function(e){
		}
	});
}

//重置
function resetParameters() {
	var s1;
	var s=$('#selectUser').selectize();
	s1=s[0].selectize;
	s1.setValue("--全部人员--");
	
	if(parameters.userName != ""){
		return parameters.userName=$('#selectUser').val("");
	}
	
}

/*
 * 修改、添加
 */
function addOrEditLogData(userData){
	var dtd=$.Deferred();
	$.ajax({
		dataType : 'json',
	    type : "POST",
		url : "/saveOrEditUser",
		contentType : "application/json;charset=UTF-8",
		data:JSON.stringify(userData),
		success : function(data) {
			dtd.resolve(data);
		},
		fail:function(e){
			dtd.reject(e);
		}
	});
	return dtd.promise();
}

/**
 * 删除数据
 * @param id
 */
function delUserData(ids){
	var dtd=$.Deferred();
	$.ajax({
		dataType : 'json',
	    type : "post",
		url : "deleteUser/"+ids,
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			dtd.resolve(data);
		},
		fail:function(e){
			dtd.reject(e);
		}
	});
	return dtd.promise(); 
}
