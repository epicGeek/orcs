kendo.culture("zh-CN");
$(function() {
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(2)").addClass("active");
	
	
	
	//查询条件
	$("#startDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	$("#endDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});

	var dataGridObj = $("#dataGrid").kendoGrid({

		dataSource: {
			data: ds,
			pageSize: 10
		},

		height: $(window).height() - $("#dataGrid").offset().top - 50,

		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "requestId",
			template: "<span  title='#:requestId#'>#:requestId#</span>",
			title: "<span  title='ID'>ID</span>"
		}, {
			field: "identifier",
			template: "<span  title='#:identifier#'>#:identifier#</span>",
			title: "<span  title='用户号码'>用户号码</span>"
		}, {
			field: "unit",
			template: "<span  title='#:unit#'>#:unit#</span>",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			field: "instance",
			template: "<span  title='#:instance#'>#:instance#</span>",
			title: "<span  title='实例'>实例</span>"
		}, {
			field: "operationUser",
			template: "<span  title='#:operationUser#'>#:operationUser#</span>",
			title: "<span  title='执行用户'>执行用户</span>"
		}, {
			field: "operation",
			template: "<span  title='#:operation#'>#:operation#</span>",
			title: "<span  title='执行内容'>执行内容</span>"
		}, {
			field: "executionResult",
			template: "<span  title='#:executionResult#'>#:executionResult#</span>",
			title: "<span  title='执行结果'>执行结果</span>"
		}, {
			field: "errorCode",
			template: "<span  title='#:errorCode#'>#:errorCode#</span>",
			title: "<span  title='错误代码'>错误代码</span>"
		}, {
			field: "errorMessage",
			template: "<span  title='#:errorMessage#'>#:errorMessage#</span>",
			title: "<span  title='错误内容'>错误内容</span>"
		}, {
			field: "responseTime",
			template: "<span  title='#:responseTime#'>#:responseTime#</span>",
			title: "<span  title='时间'>时间</span>"
		}, {
			template: "<button class='queryBtn btn btn-xs btn-danger'>查看执行指令</button>",
			title: "<span  title='操作'>操作</span>"
		}],
		dataBound: function(){
			//查看执行指令
			$(".queryBtn").on("click",function(){
				var dataItem = dataGridObj.dataItem($(this).closest("tr"));
				$("#content").val(dataItem.performContent);
				winObj.center().open();
			});
		}
	}).data("kendoGrid");


	//弹窗
	if (!$("#infoWindow").data("kendoWindow")) {
		$("#infoWindow").kendoWindow({
			width: "700px",
			actions: ["Close"],
			modal: true,
			title: "查看执行指令"
		});
	}
	var winObj = $("#infoWindow").data("kendoWindow");

});

var ds = [{
	"id": 18991,
	"requestId": "-38389d67:14e0d2fb9b8:-5ffb",
	"identifier": "460023594478478",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 07:07:33",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-5ffb\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023594478478</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 19021,
	"requestId": "6c21eb3a:14dbf1bd9a1:7b85",
	"identifier": "460023594400947",
	"unit": "fzhss05be1pg2",
	"instance": "instance3",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:58:36",
	"performContent": "<spml:modifyRequest requestID=\"6c21eb3a:14dbf1bd9a1:7b85\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023594400947</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 18992,
	"requestId": "-38389d67:14e0d2fb9b8:-5ffc",
	"identifier": "460023804712016",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:57:42",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-5ffc\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804712016</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 19022,
	"requestId": "6c21eb3a:14dbf1bd9a1:7b84",
	"identifier": "460023594409567",
	"unit": "fzhss05be1pg2",
	"instance": "instance3",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:57:41",
	"performContent": "<spml:modifyRequest requestID=\"6c21eb3a:14dbf1bd9a1:7b84\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023594409567</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 18993,
	"requestId": "-38389d67:14e0d2fb9b8:-5ffd",
	"identifier": "460023804658146",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:55:37",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-5ffd\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804658146</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 19023,
	"requestId": "6c21eb3a:14dbf1bd9a1:7b83",
	"identifier": "460023804685874",
	"unit": "fzhss05be1pg2",
	"instance": "instance3",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:55:34",
	"performContent": "<spml:modifyRequest requestID=\"6c21eb3a:14dbf1bd9a1:7b83\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804685874</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 18994,
	"requestId": "-38389d67:14e0d2fb9b8:-5ffe",
	"identifier": "460023804644133",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:53:39",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-5ffe\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804644133</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbgprs>2</odbgprs><gprs/></valueObject></modification><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:EPS\"><odbPOAccessEps>ALLPOS</odbPOAccessEps></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 19024,
	"requestId": "6c21eb3a:14dbf1bd9a1:7b82",
	"identifier": "460023804609518",
	"unit": "fzhss05be1pg2",
	"instance": "instance3",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:49:39",
	"performContent": "<spml:modifyRequest requestID=\"6c21eb3a:14dbf1bd9a1:7b82\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804609518</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbgprs>2</odbgprs><gprs/></valueObject></modification><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:EPS\"><odbPOAccessEps>ALLPOS</odbPOAccessEps></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 18995,
	"requestId": "-38389d67:14e0d2fb9b8:-5fff",
	"identifier": "460023594089968",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 06:46:38",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-5fff\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023594089968</identifier><modification operation=\"addorset\"><valueObject xsi:type=\"subscriber:HLR\"><odbssm>1</odbssm></valueObject></modification></spml:modifyRequest>",
	"searchField": null
}, {
	"id": 18996,
	"requestId": "-38389d67:14e0d2fb9b8:-6000",
	"identifier": "460023804693300",
	"unit": "fzhss05be1pg2",
	"instance": "instance1",
	"operationUser": "soapUser",
	"operation": "DEFAULT",
	"executionResult": "success",
	"errorCode": "-",
	"errorMessage": "-",
	"responseTime": "2015-06-28 01:10:38",
	"performContent": "<spml:modifyRequest requestID=\"-38389d67:14e0d2fb9b8:-6000\" xmlns:spml=\"urn:siemens:names:prov:gw:SPML:2:0\" xmlns:subscriber=\"urn:siemens:names:prov:gw:SUBSCRIBER:1:0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><version>SUBSCRIBER_v10</version><objectclass>Subscriber</objectclass><identifier>460023804693300</identifier><modification name=\"hlr\" operation=\"remove\"/></spml:modifyRequest>",
	"searchField": null
}];