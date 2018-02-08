$(function() {

	/*当前导航*/
	$("#topNavList .navListWrap:eq(0) .parentList").addClass("active");
	$("#topNavList .navListWrap:eq(0) ul li:eq(0)").addClass("active");
	
	$("#dataGrid").kendoGrid({
		
		dataSource: {
			data: loginDS,
			pageSize: 10
		},
		
		reorderable: true,

		resizable: true,

		sortable: true,

		columnMenu: true,

		pageable: true,

		columns: [{
			field: "locations",
			template: "<span  title='#:locations#'>#:locations#</span>",
			title: "<span  title='局址'>局址</span>"
		}, {
			field: "parentNe",
			template: "<span  title='#:parentNe#'>#:parentNe#</span>",
			title: "<span  title='DHSS'>DHSS</span>"
		}, {
			field: "neTypeName",
			template: "<span  title='#:neTypeName#'>#:neTypeName#</span>",
			title: "<span  title='网元类型'>网元类型</span>"
		}, {
			field: "ne",
			template: "<span  title='#:ne#'>#:ne#</span>",
			title: "<span  title='网元'>网元</span>"
		}, {
			field: "unitType",
			template: "<span  title='#:unitType#'>#:unitType#</span>",
			title: "<span  title='单元类型'>单元类型</span>"
		}, {
			field: "unit",
			title: "单元"
		}, {
			field: "ip",
			template: "<span  title='#:ip#'>#:ip#</span>",
			title: "<span  title='IP'>IP</span>"
		}, {
			title: "登录入口",
			template: "<button class='loginBtns btn btn-xs btn-danger'> 终端登录 </button>"
		}],
		dataBound: function(){
			
			$(".loginBtns").on("click", function(){
				alert("连接超时 5 秒");
			});
		}
	});

});

var loginDS=[
	{
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"HSSFE",
		ne:"FZHSS05FE03BNK",
		unitType:"DRA",
		unit:"fzhss05hs3dra02",
		ip:"10.210.28.6"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"HSSFE",
		ne:"FZHSS05FE03BNK",
		unitType:"AHUB",
		unit:"FZHSS05NT3HUB302",
		ip:"10.210.28.67"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"HSSFE",
		ne:"FZHSS05FE03BNK",
		unitType:"AHUB",
		unit:"FZHSS05NT3HUB301",
		ip:"10.210.28.66"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"HSSFE",
		ne:"FZHSS05FE03BNK",
		unitType:"AHUB",
		unit:"FZHSS05HS3HUB02",
		ip:"10.210.28.3"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"HSSFE",
		ne:"FZHSS05FE03BNK",
		unitType:"AHUB",
		unit:"FZHSS05HS3HUB01",
		ip:"10.210.28.2"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"ONE-NDS",
		ne:"FZHSS05BE03BNK",
		unitType:"SWITCH",
		unit:"FZHSS5SG3_OM_ESA2",
		ip:"10.210.28.99"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"ONE-NDS",
		ne:"FZHSS05BE03BNK",
		unitType:"SWITCH",
		unit:"FZHSS5SG3_OM_ESA1",
		ip:"10.210.28.98"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"ONE-NDS",
		ne:"FZHSS05BE01BNK",
		unitType:"SWITCH",
		unit:"FZHSS5SG1_OM_ESA2",
		ip:"10.209.74.227"
	}, {
		locations:"",		
		parentNe:"FZHSS05",
		neTypeName:"ONE-NDS",
		ne:"FZHSS05BE01BNK",
		unitType:"SWITCH",
		unit:"FZHSS5SG1_OM_ESA1",
		ip:"10.209.74.226"
	}, {
		locations:"",	
		parentNe:"XIMHSS05",
		neTypeName:"ONE-NDS",
		ne:"XIMHSS05BE02BNK",
		unitType:"SWITCH",
		unit:"XIMHSS5SG2_OM_ESA2",
		ip:"10.210.98.227"
	}
];