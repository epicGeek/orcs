kendo.culture("zh-CN");
$(function() {
	
	/*当前导航*/
	$("#topNavList .navListWrap:eq(2) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(2) ul li:eq(5)").addClass("active");
	
	//查询条件
	$("#startDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});
	$("#endDateTime").kendoDateTimePicker({
		format: "yyyy-MM-dd HH:MM"
	});

	
	$("#dataGrid").kendoGrid({
		
		dataSource: {
			data: dataList,
			pageSize:10
		},
		
		height: $(window).height()-$("#dataGrid").offset().top - 50,
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "neType",
			template: "<span  title='#:neType#'>#:neType#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "neName",
			template: "<span  title='#:neName#'>#:neName#</span>",
			title: "<span  title='网元名称'>网元名称</span>"
		}, {
			field: "unitType",
			template: "<span  title='#:unitType#'>#:unitType#</span>",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			field: "unitName",
			template: "<span  title='#:unitName#'>#:unitName#</span>",
			title: "<span  title='单元名称'>单元名称</span>"
		}, {
			field: "giveTime",
			template: "<span  title='#:giveTime#'>#:giveTime#</span>",
			title: "<span  title='获取时间'>获取时间</span>"
		}, {
			width:80,
			template: "<button class='btn btn-xs btn-danger'><i class='glyphicon glyphicon-download-alt'></i> 下载</button>",
			title: "<span  title='操作'>操作</span>"
		}]
	});

});

var dataList=[
    {
        "id": 1099,
        "neType": "NTHLRFE",
        "neName": "XIMHSS05FE12BNK",
        "unitType": "TIAMS",
        "unitName": "ximhss05nt2tia201",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05nt2tia201_20150701013015_command.log"
    },
    {
        "id": 1109,
        "neType": "NTHLRFE",
        "neName": "XIMHSS05FE12BNK",
        "unitType": "HLRFE",
        "unitName": "ximhss05nt2hlr210",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05nt2hlr210_20150701013015_command.log"
    },
    {
        "id": 1102,
        "neType": "NTHLRFE",
        "neName": "XIMHSS05FE12BNK",
        "unitType": "HLRFE",
        "unitName": "ximhss05nt2hlr208",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05nt2hlr208_20150701013015_command.log"
    },
    {
        "id": 1107,
        "neType": "NTHLRFE",
        "neName": "XIMHSS05FE12BNK",
        "unitType": "HLRFE",
        "unitName": "ximhss05nt2hlr207",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05nt2hlr207_20150701013015_command.log"
    },
    {
        "id": 1106,
        "neType": "NTHLRFE",
        "neName": "XIMHSS05FE12BNK",
        "unitType": "HLRFE",
        "unitName": "ximhss05nt2hlr205",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05nt2hlr205_20150701013015_command.log"
    },
    {
        "id": 1108,
        "neType": "HSSFE",
        "neName": "XIMHSS05FE02BNK",
        "unitType": "TIAMS",
        "unitName": "ximhss05hs2tia01",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05hs2tia01_20150701013015_command.log"
    },
    {
        "id": 1101,
        "neType": "HSSFE",
        "neName": "XIMHSS05FE02BNK",
        "unitType": "HSSFE",
        "unitName": "ximhss05hs2hss05",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05hs2hss05_20150701013015_command.log"
    },
    {
        "id": 1105,
        "neType": "HSSFE",
        "neName": "XIMHSS05FE02BNK",
        "unitType": "HSSFE",
        "unitName": "ximhss05hs2hss04",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05hs2hss04_20150701013015_command.log"
    },
    {
        "id": 1104,
        "neType": "HSSFE",
        "neName": "XIMHSS05FE02BNK",
        "unitType": "HSSFE",
        "unitName": "ximhss05hs2hss03",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05hs2hss03_20150701013015_command.log"
    },
    {
        "id": 1103,
        "neType": "HSSFE",
        "neName": "XIMHSS05FE02BNK",
        "unitType": "HSSFE",
        "unitName": "ximhss05hs2hss02",
        "giveTime": "2015-07-01 01:30:03",
        "path": "ximhss05hs2hss02_20150701013015_command.log"
    }
];
