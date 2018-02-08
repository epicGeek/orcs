
var nodeName;
var neType;
var scene;
var kpiName;
var searchParams = {
	page : 0,
	size : 20,
	
};
function getDataM() {
	loadGridList();
}
$(function() {
	allbscname();
	getDataM();
});

	$('#exportb').on('click', function() {        // 导出按钮
		var scene = $('#inputNeState').val();
		var neId = $('#inputNeNameQuota').val();
		var kpiCode = $('#inputBusinessQuota').val();
		var url1 = "exportFileQuotaMonitor?";
		var url2 = "scene=" + scene;
		var url4 = "&kpiName=" + kpiCode;
		var url3 = "&neId=" + neId;
		
		var url = url1 + url2 + url3 + url4;
		window.location.href = url;
	});

function loadGridList() {
	dataSource = new kendo.data.DataSource({
		transport : {
			read : {
				type : "GET",
				url : "quota-monitor/search/findByFilter",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			},
			parameterMap : function(options, operation) {
				if (operation == "read") {
					searchParams.scene = $('#inputNeState').val();
					if($('#inputNeNameQuota').val()!="全部网元"){
						searchParams.neId = $('#inputNeNameQuota').val();
					}else{
						delete searchParams.neId;
					}
					if($('#inputLocationQuota').val()!="全部局址"){
						searchParams.locationName = $('#inputLocationQuota').val();
					}else{
						delete searchParams.locationName;
					}
					searchParams.kpiName = $('#inputBusinessQuota').val();
					searchParams.page = options.page - 1;
					searchParams.size = options.pageSize;
					console.log(searchParams);
					return searchParams;
				}
			},
		},
		batch : true,
		pageSize : 20, // 每页显示个数
		schema : {
			data : function(d) {
				// console.log(d.content);
				if (d._embedded) {
					return d._embedded["quota-monitor"]; // 响应到页面的数据
				} else {
					return new Array();
				}
			},
			total : function(d) {
				console.log(d.page.totalElements);
				return d.page.totalElements; // 总条数
			},
		},
		serverPaging : true,
		serverFiltering : true,
		serverSorting : true

	});
	grid = $("#gridNodeRecovery").kendoGrid({

		dataSource : dataSource,
		height : $(window).height() - $("#gridNodeRecovery").offset().top - 50,
		width : "100%",
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
		}, {
			field : "neName",
			template : "<span  title='#:neName#'>#:neName#</span>",
			title : "<span  title='所在网元'>所在网元</span>"
		}, {
			field : "neType",
			template : "<span  title='#:neType#'>#:neType#</span>",
			title : "<span title='网元类型'>网元类型</span>"
		}, {
			field : "unitName",
			template : "<span  title='#:unitName#'>#:unitName#</span>",
			title : "<span title='单元名称'>单元名称</span>"
		}, {
			field : "nodeName",
			template : "<span  title='#:nodeName#'>#:nodeName#</span>",
			title : "<span  title='节点名称'>节点名称</span>"
		}, {
			field : "kpiName",
			template : "<span  title='#:kpiName#'>#:kpiName#</span>",
			title : "<span  title='指标名称'>指标名称</span>"
		}, {
			field : "kpiValue",
			template : "<span  title='#:kpiValue#'>#:kpiValue#</span>",
			title : "<span  title='指标值'>指标值</span>"
		}, {
			field : "kpiUnit",
			template : "<span  title='#:kpiUnit#'>#:kpiUnit#</span>",
			title : "<span  title='单位'>单位</span>"
		}, {
			field : "periodStartTime",
			template : "<span  title='#:periodStartTime#'>#:periodStartTime#</span>",
			title : "<span  title='指标周期'>指标周期</span>"
		}],
		dataBound : function() {
		}
	}).data("kendoGrid");
}
function onChangeSite() {
	var array = [];
	var scene = $("#inputNeState").val();
	$.each(quotaKpiList,function(index,item){
		if(item.kpiCategory == scene ){
			array[array.length] = item;
		}
	});
	$("#inputBusinessQuota").kendoDropDownList({
        dataTextField: "kpiName",
        dataValueField: "kpiCode",
        dataSource: array,
        filter: "contains",
        suggest: true,
    });
	$('#inputBusinessQuota').data("kendoDropDownList").text(array[0].kpiName);
	$('#inputBusinessQuota').val(array[0].kpiCode);
}
function handleSiteData(data) {
	$("#inputNeState").kendoDropDownList({      //场景
		optionLabel : "请选择指标种类",
		dataSource : quotaScene,
		filter : "contains",
		change : onChangeSite,
		suggest : true
		
	});

}


