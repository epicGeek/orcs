moment.locale('zh', {
    days: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    daysShort: ["日", "一", "二", "三", "四", "五", "六", "日"],
    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthsShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    meridiem: ["上午", "下午"],
    today: "今天"
});
var tableListObj;
var parameters={};

$(function() {
	
	if($("#userName").val()=='admin'){  //只有管理员才显示 导出和人员下拉查询
		$("#exportBtn").show();
		$("#hideSelect").show();
	}else{
		$("#exportBtn").hide();
		$("#hideSelect").hide();
	}
	
	initUserSelect();  //用户下拉

    //激活 tooltip
    $("[data-toggle='tooltip']").tooltip();

    //时间控件
    $('#startTime,#endTime').datetimepicker({
        format: 'YYYY-MM-DD',
        keepOpen: false,
        inline: false,
        locale: 'zh'
    });
    
  //查询 按钮
    $("#queryBtn").on("click", function() {
        //根据ajax重新加载表格
        tableListObj.tableObj.fnReloadAjax();
    });
    //重置 按钮
    $("#resetBtn").on("click", function() {
    	resetParameters();
    	//根据ajax重新加载表格
        tableListObj.tableObj.fnReloadAjax();
    });
    

    //工时
    var worktimes = {
        $input: $(".numControl input[type='text']"),
        $addBtn: $(".numControl .addNumBtn"),
        $reduceBtn: $(".numControl .reduceNumBtn"),
        interval: undefined,
        addFun: function() {
            var num = parseFloat(worktimes.$input.val()) + 0.5;
            if (num > 24) {
                worktimes.$addBtn.addClass('disabled');
            } else {
                worktimes.$input.val(num);
                if (num > 0 && worktimes.$reduceBtn.hasClass("disabled")) {
                    worktimes.$reduceBtn.removeClass('disabled');
                }
            }
        },
        reduceFun: function() {
            var num = parseFloat(worktimes.$input.val()) - 0.5;
            if (num < 0) {
                worktimes.$reduceBtn.addClass('disabled');
            } else {
                worktimes.$input.val(num);
                if (num < 24 && worktimes.$addBtn.hasClass("disabled")) {
                    worktimes.$addBtn.removeClass('disabled');
                }
            }
        },
        init: function() {
            worktimes.$addBtn.on("click", function() {
                worktimes.addFun();
            }).on("mousedown", function(argument) {
                worktimes.interval = window.setInterval(function() {
                    worktimes.addFun();
                }, 200);

            }).on("mouseup", function(argument) {
                window.clearInterval(worktimes.interval);
            });
            worktimes.$reduceBtn.on("click", function() {
                worktimes.reduceFun();
            }).on("mousedown", function(argument) {
                worktimes.interval = window.setInterval(function() {
                    worktimes.reduceFun();
                }, 200);
            }).on("mouseup", function(argument) {
                window.clearInterval(worktimes.interval);
            });
        }
    };
    worktimes.init();

    //表格,修改、删除控制在前后一周，其他时间段不允许修改删除
    tableListObj = {
        tableObj: undefined,
        //生成表格
        initTable: function() {
            if (tableListObj.tableObj) {
                tableListObj.tableObj.destroy();
            }
            this.tableObj = $('#dataTables').dataTable({
             //	responsive: true, 自适应列表
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
                     "url": "systemLog/query/historyData",
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
                    "data": "logDate",
                    "title": "<input type='checkbox' class='checkedAll'/>",
                    "class": "text-right",
                    "render": function(data, type, full, meta) {
                        var str = moment(data).fromNow(true);
                        var num = parseInt(str.split(" ")[0]);
                        if (str.search(/year/) > 0 || str.search(/month/) > 0 || (str.search(/day/) > 0 && !isNaN(num) && num > 8)) {
                            return "";
                        } else {
                            return "<input type='checkbox' class='itemCheckbox'/>";
                        }
                    }
                }, {
                	width:'100px',
                    "data": "logDate",
                    "title": "操作",
                    "render": function(data, type, full, meta) {
                        var str = moment(data).fromNow(true);
                        var num = parseInt(str.split(" ")[0]);
                        if (str.search(/year/) > 0 || str.search(/month/) > 0 || (str.search(/day/) > 0 && !isNaN(num) && num > 8)) {
                            //return "";
                        	return "<button class='deleteOneLogBtn btn btn-xs btn-default ' disabled>删除</button>&nbsp;&nbsp;" +
                            "<button class='editOneLogBtn btn btn-xs btn-default ' disabled>编辑</button>";
                        } else {
                            return "<button class='deleteOneLogBtn btn btn-xs btn-default '>删除</button>&nbsp;&nbsp;" +
                                "<button class='editOneLogBtn btn btn-xs btn-default '>编辑</button>";
                        }
                    }
                }, {
                	width:'10%',
                    "title": "姓名",
                    "data": "userName"
                }, {
                	width:'15%',
                    "title": "时间",
                    "data": "logDate"
                }, {
                	width:'15%',
                    "title": "产品",
                    "data": "product",
                    "render": function(data, type, full, meta) {
                    	return data.product;
                    }
                }, {
                	width:'10%',
                    "title": "模块",
                    "data": "modular",
                    "render": function(data, type, full, meta) {
                        return "<div class='ellipsis'  title='"+data+"' data-toggle='tooltip' >" + data + "</div>";
                    }
                }, {
                	width:'10%',
                    "title": "项目",
                    "data": "projectName",
                    "render": function(data, type, full, meta) {
                    	return data.projectName;
                    }
                }, {
                	width:'10%',
                    "title": "产品阶段",
                    "data": "stage",
                    "render": function(data, type, full, meta) {
                    	return data.stage;
                    }
                }, {
                	width:'10%',
                    "title": "工作包类型",
                    "data": "workPackageType",
                    "render": function(data, type, full, meta) {
                    	return data.workType;
                    }
                }, {
                	width:'10%',
                    "title": "工作包",
                    "data": "workPackage",
                    "render": function(data, type, full, meta) {
                    	return data.workPackage;
                    }
                }, {
                	width:'10%',
                    "title": "工时数(h)",
                    "data": "time"
                }, {
                	width:'10%',
                    "title": "备注",
                    "data": "jobOperator",
                    "render": function(data, type, full, meta) {

                        return "<div class='ellipsis'  title='"+data+"'  data-toggle='tooltip' >" + data + "</div>";
                    }
                }],
                "oLanguage": {
                    "sProcessing": "正在加载中......",
                    "sLengthMenu": "",
                    "sZeroRecords": "<i></i>您还没有添加任何日志哦！",
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
            $('#tableWrap').on('click', '.editOneLogBtn', function() {
                $("#listTab").hide();
                $("#formTab").show();
                $("#forDescTip").hide();

               // formObj.currentDataItem = tableListObj.tableObj.row($(this).closest("tr")).data();
                formObj.currentDataItem = tableListObj.tableObj.fnGetData($(this).parents("tr"));
                formObj.setItem();
            });
        },

        //批量删除按钮，需要判断是数据库中数据还是未添加到数据库里的数据
        deleteMultiLogs: function() {
        	
        	$("#tableWrap").delegate("#removeLogsBtn","click",function(){
                var postRemoveData = [];
                var checkedBox = $("#dataTables tbody .itemCheckbox:checked");
                var selectedTr, dataItem;

                if (checkedBox.length <= 0) {
                    layer.tips('请至少选择一条数据！', this);
                } else {
                    //询问框
                    layer.confirm('确认删除选中数据么？', {
                        btn: ['确认', '取消'] //按钮
                    }, function() {
                        //确认事件
                    	var parameters=[];
                        for (var i = checkedBox.length - 1; i >= 0; i--) {
                            selectedTr = checkedBox.eq(i).closest('tr');
                            dataItem =  tableListObj.tableObj.fnGetData(selectedTr);
                            if(dataItem.status){
                            	parameters.push(dataItem.id);
                            }
                        }
                        if(parameters.length>0){
                        	$.when(delLogData(parameters.join(",")))
                            .done(function(data){
                            	if(data==0){//删除成功
                            		if(tableListObj.tableObj){
                        				tableListObj.tableObj.fnReloadAjax();
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
                        $("#tableWrap .checkedAll").prop("checked", false);
                    }, function() {});
                }
        	});
        },

        //行内删除
        deleteSingleLog: function() {
            $('#tableWrap').on('click', '.deleteOneLogBtn', function() {
                var that = $(this);
                //询问框
                layer.confirm('确认删除数据么？', {
                    btn: ['确认', '取消'] //按钮
                }, function() {
                    //确认事件
                    var selectedTr = that.closest('tr');
                    var dataItem = tableListObj.tableObj.fnGetData(selectedTr);
                    $.when(delLogData([dataItem.id]))
                    .done(function(data){
                    	if(data==0){//删除成功
                    		if(tableListObj.tableObj){
                				tableListObj.tableObj.fnReloadAjax();
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
            $('#tableWrap').delegate('.checkedAll','click', function() {
                $('#tableWrap tbody .itemCheckbox').prop("checked", $(this).prop("checked"));
            });
            $('#tableWrap').delegate('.itemCheckbox','click', function() {
                if (!$(this).prop("checked")) {
                    $('#tableWrap .checkedAll').prop("checked", false);
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
           // $("#selectedUserName").html(dataItem.userName);
            $("#selectedDateId").html(dataItem.id);
            $("#productInput").data("selectize").setValue(dataItem.product.id);
            $("#projectInput").data("selectize").setValue(dataItem.projectName.id);
            $("#productPeriodInput").data("selectize").setValue(dataItem.stage.id);
            $("#workTypeInput").data("selectize").setValue(dataItem.workPackageType.id);
            $("#workContentInput").data("selectize").setValue(dataItem.workPackage.id);
            $("#moduleInput").val(dataItem.modular);
            $("#descInput").val(dataItem.jobOperator);
            $("#workTimeInput").val(dataItem.time);
        },
        //获取字段值
        getItem:function(){
            var dataItem = formObj.currentDataItem;
            return {
            	 id:dataItem.id,
            	 logDate: dataItem.logDate,
            	 userName:$("#realName").val(),
                 product:{product:$("#productInput").text(),id:$("#productInput").val()},
                 projectName: {projectName:$("#projectInput").text(),id:$("#projectInput").val()},
                 stage: {stage:$("#productPeriodInput").text(),id:$("#productPeriodInput").val()},
                 workPackageType: {workType:$("#workTypeInput").text(),id:$("#workTypeInput").val()},
                 workPackage: {workPackage:$("#workContentInput").text(),id:$("#workContentInput").val()},
                 modular: $("#moduleInput").val(),
                 time: $("#workTimeInput").val(),
                 jobOperator: $("#descInput").val(),
                 status: false
            };
        },
        //取消按钮
        cancelEdit: function() {
            $("#cancelEditBtn").on("click", function() {
                $("#listTab").show();
                $("#formTab").hide();
            });
        },
        
        //保存按钮
        saveEdit: function() {
            $("#saveEditBtn").on("click", function() {
                var newObj = formObj.getItem();
                var product=newObj.product.product;
                var proName=newObj.projectName.projectName;
                var otherBool = false, timeBool=false;
                //判断输入的工时数是否正确(小数 时间大小<=24)
                var reg=/^(2[0-3](.[05])?)$|^(1?[1-9](.[05])?)$|^(0.5)$|^(24)$/g;
                
                //如果产品 或项目有一个是【其他】选项，备注都不能为空
                if (product == "其他" || proName == '其他' || product == "与产品无关" || proName == '与项目无关') {
                	if (newObj.jobOperator == "") {
                        $("#forDescTip").show();
                        $("#descInput").focus();
                        window.setTimeout(function() {
                            $("#forDescTip").hide();
                        }, 3000);
                        otherBool = false;
                    } else {
                    	otherBool = true;
                    }
                }else {
                	otherBool = true;
                }
                
                if(reg.test(newObj.time)){
                	timeBool = true;
                }else{
                	 layer.confirm('请输入正确的时间，时间范围：0.5-24小时，粒度0.5小时', {
                         btn: ['确认', '取消'] //按钮
                     });
                	 timeBool = false;
                }
                
                if(otherBool && timeBool){
                	var edits = [];
                	edits.push(newObj);
                	
                	//ajax
                    $.when(addOrEditLogData(edits))
                    .done(function(data){
                    	if(data == true || data == "true"){
                        	$("#tableWrap .checkedAll").prop("checked", false);
                			if(tableListObj.tableObj){
                				tableListObj.tableObj.fnReloadAjax();
                			}
                        	layer.msg('修改成功！', { icon: 1 });
                    	} else {
                    		layer.msg('修改失败！', { icon: 2 });
                    	}
                    })
                    .fail(function(){
                		layer.msg('修改失败！', { icon: 2 });
                    });
                    //显示表格，隐藏编辑面板
                    $("#listTab").show();
                    $("#formTab").hide();
                }
                
            });
        },
        init: function(argument) {
            this.cancelEdit();
            this.saveEdit();
        }
    };
    tableListObj.init();
    formObj.init();
    initCondition();
    
  //导出
    $("#exportBtn").on("click",function(){
    	
    	var nameSearch= $("#selectUser").val();
    	if(nameSearch=="--全部人员--"){
    		nameSearch='';
    	}else if(nameSearch == 'null' || nameSearch == null){
    		nameSearch='';
    	}
    	window.location.href = "systemLog/exportFile?startDate="+$('#startTime > input[type="text"]').val()+"&endDate="+
    	$('#endTime > input[type="text"]').val()+"&userName="+nameSearch;
    	
	});
    
});

//获取查询条件参数
function getParameters() {
	
	
	var userName =$("#userName").val()=='admin'?$("#selectUser").val():$("#realName").val();
	
	if(userName== "--全部人员--"){
		parameters.userName="";
	}else{
		 parameters.userName = userName;
	}
    parameters.startDate =$('#startTime > input[type="text"]').val();
    parameters.endDate= $('#endTime > input[type="text"]').val();
    
    return parameters;
}
//重置
function resetParameters() {
	$('#startTime > input[type="text"]').val("");
	$('#endTime > input[type="text"]').val("");
	var s1;
	var s=$('#selectUser').selectize();
	s1=s[0].selectize;
	s1.setValue("--全部人员--");
	
	if(parameters.userName != ""){
		return parameters.userName=$('#selectUser').val("");
	}
	
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
