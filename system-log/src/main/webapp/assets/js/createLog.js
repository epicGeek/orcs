moment.locale('zh', {
    days: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
    daysShort: ["日", "一", "二", "三", "四", "五", "六", "日"],
    daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
    months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthsShort: ["一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"],
    meridiem: ["上午", "下午"],
    today: "今天"
});

$(function() {
	
    //激活 tooltip
    $("[data-toggle='tooltip']").tooltip();

    //时间控件
    $('#datepicker').datetimepicker({
        format: 'YYYY-MM-DD',
        minDate: moment(new Date()).subtract(7, 'days'),
        maxDate: moment(new Date()).add(7, 'days'), 
        keepOpen: true,
        inline: true,
        locale: 'zh',
        viewMode:"days"
    });

    //循环日志数据，标示表格上哪些日期已经填写过日志
    initDateTimeLogs();
    $("#datepicker").on("dp.update",function(){
    	//循环日志数据，标示表格上哪些日期已经填写过日志
        initDateTimeLogs();
        dateTimeClick();
    });
    
  //点击前后两周时间事件（选择时间后，会重新加载时间，因此也需要从新绑定事件）
    var dateTimeClick = function(){
        //点击 某个时间，显示form列表和table，并加载table数据
        $("#datepicker tbody tr td.canClick").on("click", function(e) {
            e.stopPropagation();
            e.preventDefault();
            $(".loading").show();
            //选择某个时间,隐藏时间框，显示form和表格
            hideDatePanel();

            //获取当前点击时间和是否已有日志数据记录
            var dateStr = $(this).attr("data-day").split("/");
            var dataTime = dateStr[2] + "-" + dateStr[0] + "-" + dateStr[1];
            $("#selectedDateId").html(dataTime);
            tableListObj.parameter = dataTime;
            
            function setDefaultInput(){
            	$("#moduleInput").val("");
            	$("#workTimeInput").val("0.5");
            	$("#descInput").val("");
            }
            setDefaultInput();
            
            //重新加载表格数据
            if (tableListObj.tableObj) {
                tableListObj.tableObj.destroy();
            }
            $('#dataListTable').empty(); 
            queryLogData(dataTime,"","",function(data){
            	 if(data == undefined || data == null || typeof data=='string'){
            		 data = [];
                 }
                 tableListObj.dataSource = data;
                 tableListObj.initTable();
                 $(".loading").hide();
                 setTotalTimes(tableListObj.tableObj);
            });
            return false;
        });
    };
    dateTimeClick();


    //初始化 下拉框 控件
    initCondition();

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

    //表格
    var tableListObj = {
    		
        tableObj: undefined,
        dataSource: [],
        parameter:undefined,
        //生成表格
        initTable: function() {
            this.tableObj = $('#dataListTable').DataTable({
                responsive: true,
                paging: false,
                searching: false,
                ordering: false,
                "scrollX": "100%",
                "scrollY":300,
                "scrollCollapse": false,
                "destroy": true,//如果需要重新加载的时候请加上这个
                "lengthChange": false,
                "data": tableListObj.dataSource,
                "columns": [{
                    title: "", //以防响应式（点击按钮）出现bug
                    "render": function(data, type, full, meta) {
                        return "";
                    }
                }, {
                    title: "<input type='checkbox' class='checkedAll'/>",
                    "class": "text-right",
                    "render": function(data, type, full, meta) {
                        var exec = "<input type='checkbox' class='itemCheckbox'/>";
                        return exec;
                    }
                }, {
                    "data": "status",
                    title: "状态",
                    "class": "text-center",
                    "render": function(data, type, full, meta) {
                        var exec = "";
                        if (data) {
                            exec = "<i class='addedIcon glyphicon glyphicon-ok-circle' title='已保存'></i>";
                        } else {
                            exec = "<i class='unAddIcon glyphicon glyphicon-ban-circle' title='未保存' ></i>";
                        }
                        return exec;
                    }
                }, {
                    "title": "时间",
                    "data": "logDate"
                }, {
                    "title": "产品",
                    "data": "product",
                    "render": function(data, type, full, meta) {
                    	if(typeof data == 'object') {
                    		return data.product;
	                    	} else if(typeof data=='string') {
	                    		return data;
	                    	}
                    }
                }, {
                    "title": "模块",
                    "data": "modular"
                }, {
                    "title": "项目",
                    "data": "projectName",
                    "render": function(data, type, full, meta) {
                    	if(typeof data == 'object') {
                    		return data.projectName;
	                    	} else if(typeof data=='string') {
	                    		return data;
	                    	}
                    }
                }, {
                    "title": "产品阶段",
                    "data": "stage",
                    "render": function(data, type, full, meta) {
                    	if(typeof data == 'object') {
                    		return data.stage;
	                    	} else if(typeof data=='string') {
	                    		return data;
	                    	}
                    }
                }, {
                    "title": "工作包类型",
                    "data": "workPackageType",
                    "render": function(data, type, full, meta) {
                    	if(typeof data == 'object') {
                    		return data.workType;
	                    	} else if(typeof data=='string') {
	                    		return data;
	                    	}
                    }
                }, {
                    "title": "工作包",
                    "data": "workPackage",
                    "render": function(data, type, full, meta) {
                    	if(typeof data == 'object') {
                    		return data.workPackage;
	                    	} else if(typeof data=='string') {
	                    		return data;
	                    	}
                    }
                }, {
                    "title": "工时数",
                    "data": "time",
                    "render": function(data, type, full, meta) {
                        return data + " <i>h</i>";
                    }
                }, {
                    "title": "备注",
                    "data": "jobOperator",
                    "render": function(data, type, full, meta) {

                        return "<div style='white-space: normal;display:inline-block;width:80%;'>" + data + "</div>";
                    }
                }],
                "oLanguage": {
                    "sProcessing": "正在加载中......",
                    "sLengthMenu": "",
                    "sZeroRecords": "<i></i>您还没有添加任何日志哦！请从表单添加！",
                    "sInfo": "",
                    "sInfoEmpty": "",
                    "sInfoFiltered": "",
                    "sInfoPostFix": "",
                    "sSearch": "",
                    "sUrl": "",
                    "oPaginate": {
                        "sFirst": "",
                        "sPrevious": "",
                        "sNext": "",
                        "sLast": ""
                    }
                }
            });
        },
        
        //批量删除按钮，需要判断是数据库中数据还是未添加到数据库里的数据
        deleteSelectedUser: function() {
        	
            $('#removeLogsBtn').on('click', function() {

                var parameters = [];
                var allCheckBox = $("#dataListTable tbody .itemCheckbox");
                var checkedBox = $("#dataListTable tbody .itemCheckbox:checked");
                var selectedTr, dataItem, selectedDBArr=[];//是否是数据库中数据

                if (allCheckBox.length <= 0) { //如果没有数据
                    layer.msg('您还没有添加任何日志哦！', { icon: 5 });
                } else if (checkedBox.length <= 0) {
                    layer.tips('请至少选择一条数据！', this);
                } else {
                    //询问框
                    layer.confirm('确认删除选中数据吗？', {
                        btn: ['确认', '取消'] //按钮
                    }, function() {
                        //确认事件
                        for (var i = checkedBox.length - 1; i >= 0; i--) {
                            selectedTr = checkedBox.eq(i).closest('tr');
                            dataItem = tableListObj.tableObj.row(selectedTr).data();
                            if(dataItem.status){ //如果删除的是数据库中的数据
                            	parameters.push(dataItem.id);
                            	selectedDBArr.push(selectedTr);
                            } else{
                            	tableListObj.tableObj.row(selectedTr).remove();
                            }
                        }
                        tableListObj.tableObj.draw(false);
                        
                    	//ajax
                        if(parameters.length>0){
                        	$.when(delLogData(parameters.join(",")))
                            .done(function(data){
                            	if(data==0){//删除成功
                            		for (var i = selectedDBArr.length - 1; i >= 0; i--) {
                                    	tableListObj.tableObj.row(selectedDBArr[i]).remove();
                                    }
                            		tableListObj.tableObj.draw(false);
                                	setTotalTimes(tableListObj.tableObj);
                                	layer.msg('删除成功！', { icon: 1 });
                                	
                            	} else {
                            		layer.msg('已保存数据删除失败！', { icon: 2 });
                            	}
                            })
                            .fail(function(){
                        		layer.msg('已保存数据删除失败！', { icon: 2 });
                            });
                        } else {
                        	layer.msg('删除成功！', { icon: 1 });
                        }
                    	setTotalTimes(tableListObj.tableObj);
                    }, function() {});
                    
                }
                $("#tableWrap .checkedAll").prop("checked", false);

            });
        },

        //全选和取消全选
        checkboxSelect: function() {
            $('#tableWrap').delegate('.checkedAll', 'click', function() {
                $('#tableWrap tbody .itemCheckbox').prop("checked", $(this).prop("checked"));
            });
            $('#tableWrap').delegate('.itemCheckbox', 'click', function() {
                if (!$(this).prop("checked")) {
                    $('#tableWrap .checkedAll').prop("checked", false);
                }
            });
        },

        init: function() {
            this.initTable();
            this.deleteSelectedUser();
            this.checkboxSelect();
        }
    };
    tableListObj.init();

    //添加一条记录按钮
    $("#addOneLogBtn").on("click", function() {
    	 
    	var logsObj = {
    	            logDate: $("#selectedDateId").html(),
    	            product:{product:$("#productInput").text(),id:$("#productInput").val()},
    	            projectName: {projectName:$("#projectInput").text(),id:$("#projectInput").val()},
    	            stage: {stage:$("#productPeriodInput").text(),id:$("#productPeriodInput").val()},
    	            workPackageType: {workType:$("#workTypeInput").text(),id:$("#workTypeInput").val()},
    	            workPackage: {workPackage:$("#workContentInput").text(),id:$("#workContentInput").val()},
    	            modular: $("#moduleInput").val(),
    	            time: parseFloat($("#workTimeInput").val()),
    	            jobOperator: $("#descInput").val(),
    	            userName:$("#realName").val(),
    	            status: false
    	};
    	 
    	var totalTimes=parseFloat($("#totalTimes").html());//记录总工时
        var addOneRow = false; //标示是否满足添加一行数据的条件
        //判断输入的工时数是否正确(0.5<= 时间 <=24)
        var reg=/^(2[0-3](.[05])?)$|^(1?[1-9](.[05])?)$|^(0.5)$|^(24)$/g;
       
        totalTimes+=logsObj.time;
        
        if(totalTimes>24){
        	layer.confirm('您输入的当天总工时已超过24小时，请重新输入工时！', {
                btn: ['确认', '取消'] //按钮
            });
        	return;
        }
        
        if(reg.test(logsObj.time)){
        	addOneRow = true;
        }else{
        	 layer.confirm('请输入正确的时间，时间范围：0.5-24小时，粒度0.5小时', {
                 btn: ['确认', '取消'] //按钮
             });
        	return;
        }
        
        //如果产品 或项目有一个是【其他】或者【与项目无关】选项，备注都不能为空
        if (logsObj.product.product == "其他" ||  logsObj.projectName.projectName == '其他'  || logsObj.product.product == "与产品无关"   || logsObj.projectName.projectName == "与项目无关") {
            if (logsObj.jobOperator == "") {
                $("#forDescTip").show();
                $("#descInput").focus();
                window.setTimeout(function() {
                    $("#forDescTip").hide();
                }, 3000);
                addOneRow = false;
            } else {
                addOneRow = true;
            }
        } else {
            addOneRow = true;
        }
        
        if (addOneRow) {
            tableListObj.dataSource.push(logsObj);
            tableListObj.tableObj.row.add(logsObj).draw(false);
            $("#totalTimes").html(totalTimes);
        }
        $("#tableWrap .checkedAll").prop("checked", false);
    });

    //保存已添加日志
    $("#saveLogBtn").on("click", function() {
        var postAddData = [];
        var allCheckBox = $("#dataListTable tbody .itemCheckbox");
        var unAddIcon = $("#dataListTable tbody .unAddIcon");
        var currentIcon, selectedTr, dataItem;

        if (allCheckBox.length <= 0 || unAddIcon.length <= 0) { //如果没有(新)数据
            layer.msg('您还没有添加任何新的日志哦！', { icon: 6 });
        } else {
            //询问框
            layer.confirm('确认添加新日志么？', {
                btn: ['确认', '取消'] //按钮
            }, function() {
            	 //确认事件,批量添加数据
            	var parameters =[];
            	//获取数据
            	for (var i=unAddIcon.length - 1; i >= 0; i--) {
                	currentIcon = unAddIcon.eq(i);
                    selectedTr = currentIcon.closest('tr');
                    dataItem = tableListObj.tableObj.row(selectedTr).data();
                    parameters.push(dataItem);
                }
            	
            	//ajax
                $.when(addOrEditLogData(parameters))
                .done(function(data){
                	if(data == true || data == "true"){
                		unAddIcon.addClass("addedIcon glyphicon-ok-circle").removeClass("unAddIcon glyphicon-ban-circle");
                    	layer.msg('添加成功！', { icon: 1 });
                	} else {
                		layer.msg('添加失败！', { icon: 2 });
                	}
                })
                .fail(function(){
            		layer.msg('添加失败！', { icon: 2 });
                });
            });
        }
    });

    //取消保存日志按钮 / 返回
    $("#cancelSaveLogBtn,#returnBtn").on("click", function() {

        var unAddIcon = $("#dataListTable tbody .unAddIcon");

        if (unAddIcon.length > 0) {
            //询问框
            layer.confirm('您还有未保存的新日志，确认放弃么？', {
                btn: ['确认', '取消'] //按钮
            }, function(index) {
                showDatePanel();
                initDateTimeLogs();
                layer.close(index);
            });
        } else {
            showDatePanel();
            initDateTimeLogs();
        }
    });

});

//根据日期查询数据
/*
 * @dateTime : 查询某一天的数据
 * @startDate -- @endDate 查询某时间段内的数据
 * */
function queryLogData(dateTime,startDate,endDate,callback){
	$.ajax({
		dataType : 'json',
	    type : "POST",
	    "url": "systemLog/query/condition",
        "data":{
       	 "dateTime":dateTime,
       	 "startDate":startDate,
       	 "endDate":endDate,
        "userName":$("#userName").val()=='admin'?"":$("#realName").val()
        //"userName":$("#realName").val()
        },
		success : function(data) {
			if(callback){
				callback(data);
			}
		},
		fail:function(e){
			layer.msg('查询数据失败！', { icon: 1 });
		}
	});
}


//显示时间面板,隐藏form和table面板 
function showDatePanel() {
    $("#dataTimePanel").show();
    $("#splitePanel").hide();
}
//隐藏时间面板，显示form和table面板
function hideDatePanel() {
    $("#dataTimePanel").hide();
    $("#splitePanel").show();
    $("#forDescTip").hide();
}

//在时间控件显示日期已经被添加过几个日志，模拟数据。
function initDateTimeLogs() {
    var nowDate = new Date(),date1,date2;
    
 	var startDate = moment(nowDate).subtract(7, 'days').format('YYYY-MM-DD');
	var endDate= moment(nowDate).add(7, 'days').format('YYYY-MM-DD');
	
	//标示可点击的td
	$("#datePanel td.day.canClick").removeClass("canClick");
	for(var i=0;i<=7;i++){
		date1 = moment(nowDate).subtract(i, 'days').format('MM/DD/YYYY');
		date2 = moment(nowDate).add(i, 'days').format('MM/DD/YYYY');
		$(".day[data-day='" + date1 + "']").addClass('canClick');
		$(".day[data-day='" + date2 + "']").addClass('canClick');
	}
	
	//查询前后两周的数据
    queryLogData("",startDate,endDate,function(data){
    	$("#datePanel td.day.added").removeClass("added");
		$.each(data,function(index,item){
			date = moment(new Date(item.logDate)).format('MM/DD/YYYY');
			var $date = $(".day[data-day='" + date + "']");
	        $date.addClass('added');
		});
    });
    
}

function setTotalTimes(dataTable){
	var totalTimes=0;
	if(dataTable && dataTable.data()){
		dataTable.data().each(function(d) {
			totalTimes+=d.time;
		});
	}
	$("#totalTimes").html(totalTimes);
}