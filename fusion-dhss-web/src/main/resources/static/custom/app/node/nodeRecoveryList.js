kendo.culture("zh-CN");

var neTypes = "";

var unit;
var neType;
var neState;
var startdatetime;
var enddatetime;

var searchparams = {
	page : 0,
	size : 10
};
var dataSource;
var dataGridObj;
$(function() {

	$("#clearsearch").on("click", function() {
		$("#startdatetime").val("");
		$("#enddatetime").val("");
		dataSource.read();
	});

	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "emergencySecurity/querySecurityState",
				dataType : "json",
				contentType : "application/json"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchparams.page = options.page - 1;
					searchparams.size = options.pageSize;
					searchparams.unit = $("#inputUnit").val();
					searchparams.ne = $("#inputNeType").val();
					searchparams.startdatetime = $("#startdatetime").val();
					searchparams.enddatetime = $("#enddatetime").val();
					searchparams.operate_state = 2;
					return searchparams;
				}
			}
		},
		batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(d) {
				if (d._embedded) {
					/*
					 * var temparray = [];
					 * $.each(d._embedded["emergency-security-state"],function(index,item){
					 * if(item.unitEntity == null){ item.unitEntity =
					 * {unitName:""}; } temparray[temparray.length] = item;
					 * console.log(item); }); console.log(temparray);
					 */
					return d._embedded["emergency-security-state"];
				}
				return []; // 响应到页面的数据
			},
			total : function(d) {
				return d.page.totalElements; // 总条数
			}
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true

	});
	dataGridObj = {
		init : function() {
			this.dataGrid = $("#gridNodeRecovery")
					.kendoGrid(
							{
								dataSource : dataSource,
								height : $(window).height()
										- $("#gridNodeRecovery").offset().top
										- 50,
								reorderable : true,
								resizable : true,
								sortable : true,
								columnMenu : true,
								pageable : true,
								columns : [
										{
											field : "_links.self.href",
											title : "_links.self.href",
											hidden : true
										},
										{
											field : "operateDate",
											template : "#= operateDate ? kendo.toString(new Date(operateDate), \'yyyy-MM-dd HH:mm\') : \'\' #",
											title : "<span  title='时间'>时间</span>"
										},
										{
											field : "unitEntity.unitName",
											// template: "#if(imsi!=null){#<span
											// title='#:imsi#'>#:imsi#</span>#}#",
											title : "<span  title='单元名称'>单元名称</span>"
										},
										{
											field : "neEntity.neName",
											// template:
											// "#if(areaName!=null){#<span
											// title='#=areaName#'>#=areaName#</span>#}#",
											title : "<span  title='网元名称'>网元名称</span>"
										},
										{
											field : "neEntity.location",
											// template:
											// "#if(areaName!=null){#<span
											// title='#=areaName#'>#=areaName#</span>#}#",
											title : "<span  title='站点'>站点</span>"
										},
										{
											field : "operateState",
											template : "#if(operateState==1){#节点隔离#}if(operateState==2){#节点恢复#}if(operateState==4){#网元隔离#}if(operateState==5){#网元恢复#}#",
											title : "<span  title='状态'>状态</span>"
										},
										{
											field : "operator",
											// template:
											// "#if(areaName!=null){#<span
											// title='#=areaName#'>#=areaName#</span>#}#",
											title : "<span  title='操作人'>操作人</span>"
										},
										{
											width : 130,
											template : "<button class='btn btn-xs btn-info' onclick='showDetails(this)' ><i class='glyphicon glyphicon-edit'></i>查看详情</button>&nbsp;&nbsp;",
											title : "<span  title='操作'>操作</span>"
										} ],
								dataBound : function() {
								}
							}).data("kendoGrid");
		}
	};
	dataGridObj.init();

	// 获取网元列表
	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "queryNeType",
		success : function(data) {
			$("#inputNeType").kendoDropDownList({
				optionLabel : "请选择网元类型",
				dataSource : data,
				filter : "contains",
				suggest : true,
				change : function() {

				}
			})
		}
	});

	$.ajax({
		dataType : 'json',
		type : "GET",
		url : "rest/equipment-unit",
		success : function(data) {
			$("#inputUnit").kendoDropDownList({
				optionLabel : "请选择单元类型",
				dataTextField : "unitName",
				dataValueField : "unitId",
				dataSource : data._embedded["equipment-unit"],
				filter : "contains",
				suggest : true
			})
		}
	});
	$("#startdatetime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss"
	});
	$("#enddatetime").kendoDateTimePicker({
		format : "yyyy-MM-dd HH:mm:ss"
	});
});
// 查询
$("#searchButton").click(function() {
	dataSource.read();
});

function showDetails(e) {
	// location.href = 'emergencySecurity/nodeRecovery';

	var tr = dataGridObj.dataGrid.dataItem($(e).closest("tr"));
	// alert(data.ne);
	var unitId;
	if (tr.unitEntity.unitId == undefined) {
		unitId = 0;
	} else {
		unitId = tr.unitEntity.unitId;
	}
	location.href = 'nodeRecoveryList?state=' + tr.operateState + '&webSite='
			+ tr.neEntity.neId + '&neTypeId=' + tr.neEntity.neType + '&unitId='
			+ unitId;
}