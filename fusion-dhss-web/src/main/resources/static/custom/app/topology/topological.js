/**
 * Created by john on 2016/8/2.
 */
var myChart;
var option;
require(
    [
        'echarts',
        'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
        'echarts/chart/tree'
    ],
    function (ec) {
        myChart = ec.init(document.getElementById('topologicalmap'));
        option = {
            title : {
                text: '123'
            },
            toolbox: {
                show : false,
                feature : {
                    mark : {show: false},
                    dataView : {show: false, readOnly: false},
                    restore : {show: false},
                    saveAsImage : {show: false}
                }
            },
            series : [
                {
                    name:$("#dhssName").val(),
                    type:'tree',
                    orient: 'horizontal',  // vertical horizontal
                    rootLocation: {x: 100,y: 230}, // 根节点位置  {x: 100, y: 'center'}
                    nodePadding: 12,
                    layerPadding: 200,
                    hoverable: false,
                    roam: true,
                    symbolSize: 6,
                    itemStyle: {
                        normal: {
                            color: '#4883b4',
                            label: {
                                show: true,
                                position: 'right',
                                formatter: "{b}",
                                textStyle: {
                                    color: '#000',
                                    fontSize: 5
                                }
                            },
                            lineStyle: {
                                color: '#ccc',
                                type: 'curve' // 'curve'|'broken'|'solid'|'dotted'|'dashed'

                            }
                        },
                        emphasis: {
                            color: '#4883b4',
                            label: {
                                show: false
                            },
                            borderWidth: 0
                        }
                    },

                    data: [
                        {name:$("#dhssName").val(),
                            symbolSize: [48, 48],
                            symbol: 'image://../assets/images/iconALLHSS.png',
                            children:[{
                                name:"SHHSS50FE12BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                children:[{name:"BNK024",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                    children:[
                                        {name:"SHHSS50FE11BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                        {name:"SHHSS50FE11BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                        {name:"SHHSS50FE11BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]},
                                    {name:"BNK025",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png'},
                                    {name:"BNK026",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                        children:[
                                            {name:"SHHSS50FE14BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                            {name:"SHHSS50FE14BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]},
                                {name:"SHHSS50FE13BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[
                                        {name:"BNK027",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                            children:[
                                                {name:"SHHSS50FE13BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]},
                                {name:"SHHSS50FE14BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[
                                        {name:"BNK028",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png'},
                                        {name:"BNK029",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                            children:[
                                                {name:"SHHSS50FE13BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]},
                                {name:"SHHSS50FE15BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[{name:"BNK031",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                        children:[
                                            {name:"SHHSS50FE13BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                            {name:"SHHSS50FE13BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                            {name:"SHHSS50FE13BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]},
                                {name:"SHHSS50FE16BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[
                                        {name:"BNK032",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                            children:[
                                                {name:"SHHSS50FE13BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]},
                                {name:"SHHSS50FE17BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[
                                        {name:"BNK087",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png'},
                                        {name:"BNK088",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png'}]},
                                {name:"SHHSS50FE18BNK",symbolSize: [48, 48],symbol: 'image://../assets/images/iconHSS.png',
                                    children:[
                                        {name:"BNK089",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png'},
                                        {name:"BNK090",symbolSize: [32, 32],symbol: 'image://../assets/images/iconGWF.png',
                                            children:[
                                                {name:"SHHSS50FE13BNKB1",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB2",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'},
                                                {name:"SHHSS50FE13BNKB3",symbolSize: [24, 24],symbol: 'image://../assets/images/iconHSSUNIT.png'}]}]}
                            ]}
                    ]
                }
            ]
        };
        // myChart.setOption(option);
        myChart.on(ecConfig.EVENT.CLICK, nodePanelObj.eConsole);
        nodePanelObj.getTopoData($("#dhssName").val());
       /* if(getQueryString("NENAME")){
            nodePanelObj.init();
            nodePanelObj.getTopoData(getQueryString("NENAME"));
        }else{
            nodePanelObj.init();
            nodePanelObj.getTopoData($("#dhssName").val());
        }*/
    }
);
var ecConfig = require('echarts/config');
//var ecConfig = echarts.config;  


//【DHSS基本信息】
var nodePanelObj = {
    hssName:"",
    currentNode:null,
    //更新拓扑图数据
    getTopoData: function(inhss) {
        nodePanelObj.hssName = inhss;
        $.ajax({
            url: /*"../assets/nehss.json"*/"new-topology",
            data:{"HSS" :inhss},
            cache: false,
            success: function(data){
//                var mydatas = JSON.parse(data);
                nodePanelObj.updateTopoData(data.result);
                //alert(mydatas);
                //JSON.parse()
            }
        });
    },
    getNodeKPIData: function(inNodeInfo) {
        console.log(inNodeInfo);
        $.ajax({
            url: /*"../assets/nekpilist.json"*/"findKpiInfoByEquipmentName",
            data:{"NENAME" :inNodeInfo.neid},
            cache: false,
            success: function(data){
//                var mydatas = JSON.parse(data);
                nodePanelObj.initResultList(/*mydatas.result*/data);
            }
        });
    },
    getNodeAlarmData: function(inNodeInfo) {
        console.log(inNodeInfo);
        $.ajax({
            url: /*"../assets/nealarmlist.json"*/"new-topology-kpi",
            data:{"NENAME" :inNodeInfo.neid},
            cache: false,
            success: function(data){
//                var mydatas = JSON.parse(data);
                nodePanelObj.initResultList2(data.result);
            }
        });
    },
    getNodeAhubData: function(inNodeInfo) {
        console.log(inNodeInfo);
        $.ajax({
            url: /*"../assets/neahubdata.json"*/"new-topology-ahub",
            data:{"NENAME" :inNodeInfo.neid},
            cache: false,
            success: function(data){
//            	var mydatas = JSON.parse(data);
                nodePanelObj.resultAhubData(data.result);
            }
        });
    },
    updateTopoData: function(indata) {
    	console.log(indata);
        var tmpdata = formatTopoData(indata);
        var tmp = option.series[0].data;
        option.title.text = nodePanelObj.hssName;
        option.series[0].data = tmpdata;
        myChart.setOption(option, true);
        // myChart.addData(tmpdata);
    },
    // 点击拓扑图上面的节点之后
    eConsole:function (param) {
        //console.log(param);
        nodePanelObj.currentNode = param;
        var tmpNeType = param.data.netype;
        if(tmpNeType=="AHUB" || tmpNeType=="AHUB"){
            nodePanelObj.showAhubPanel(param.data);
            nodePanelObj.getNodeAhubData(param.data);
        }else if(tmpNeType=="ALLHSS"){

        }else{
            nodePanelObj.showPanel(param.data);
            nodePanelObj.getNodeKPIData(param.data);
            nodePanelObj.getNodeAlarmData(param.data);
        }

    },
    //指标滚动条样式
    initScrollStyle: function() {
        $("#resultWrap").height(265);
        $("#resultWrap").mCustomScrollbar({
            theme: "minimal-dark",
            scrollbarPosition: "inside",
            autoHideScrollbar: true
        });
    },
    //指标滚动条内添加数据
    initResultList: function(inData) {
        var liHtml = "";
        for (var i = 0; i < inData.length; i++) {
            var item = inData[i];
            liHtml += '<li class="liList" >'+item.kpiname+'<br>'+item.date+'<span style="float:right;font-size: 18px">'+ item.kpivalue + ''+ item.kpiunit +'</span></li>';
        }
        $("#resultWrap ul").html(liHtml);
    },
    //指标-绑定事件
    resultListClick: function() {
        $("#result2Wrap").delegate("li.liList", "click", function() {
            $("#result2Wrap li.active").removeClass("active");
            var that = $(this);
            that.addClass("active");
        });
    },
    //告警滚动条样式
    initScrollStyle2: function() {
        $("#result2Wrap").height(265);
        $("#result2Wrap").mCustomScrollbar({
            theme: "minimal-dark",
            scrollbarPosition: "inside",
            autoHideScrollbar: true
        });
    },
    //告警滚动条内添加数据
    initResultList2: function(inData) {
        var liHtml = "";
        for (var i = 0; i < inData.length; i++) {
            var item = inData[i];
            liHtml += '<li class="liList">'+item.alarm_content+'<br>'+item.date+'<span style="float:right;font-size: 18px">'+ item.alarmlevel +'</span></li>';
        }
        $("#result2Wrap ul").html(liHtml);
    },
    //告警-绑定事件
    resultListClick2: function() {
        $("#result2Wrap").delegate("li.liList", "click", function() {
            $("#result2Wrap li.active").removeClass("active");
            var that = $(this);
            that.addClass("active");
        });
    },
    initScrollStyle3: function() {
        $("#result3Wrap").height(430);
        $("#result3Wrap").mCustomScrollbar({
            horizontalScroll:true,
            theme: "minimal-dark",
            scrollbarPosition: "inside",
            autoHideScrollbar: true
        });
    },
    resultAhubData: function(inData) {
        var liHtml = "";
        for (var i = 0; i < inData.length; i++) {
            var item = inData[i];
            liHtml += '<tr><td>'+ item.selfPort+'</td><td>'+item.targetMode+'</td><td>'+item.targetLan+'</td><td>'+item.vlanId+'</td><td>'+item.ipAddress+'</td><td>'+item.targetEquipment+'</td><td>'+item.targetPort+'</td><td>'+item.upLinkIpAddress+'</td></tr>';
        }
        $("#result3Wrap TABLE tbody").html(liHtml);
    },
    showPanel:function(inItem){
        $("#nodePanel").show();
        $("#nodePanel .eNodeBName").html(inItem.name);

        $("#topKpi .location").html(inItem.location);
        $("#topKpi .idsVersion").html(inItem.idsVersion);
        $("#topKpi .swVersion").html(inItem.swVersion);
        $("#topKpi .neState").html(inItem.neState);
        $("#topKpi .remarks").html(inItem.remarks);

        $("#nodePanel .closePanel").on("click",function(){
            nodePanelObj.hidePanel();
        });
        nodePanelObj.hideAhubPanel();
    },
    hidePanel:function(){
        $("#nodePanel").hide();
    },
    showAhubPanel:function(inItem){
        $("#nodeAhubPanel").show();
        $("#nodeAhubPanel .eNodeBName").html(inItem.name);

        $("#nodeAhubPanel .closePanel").on("click",function(){
            nodePanelObj.hideAhubPanel();
        });
        nodePanelObj.hidePanel();
    },
    hideAhubPanel:function(){
        $("#nodeAhubPanel").hide();
    },
    init: function() {
        nodePanelObj.initScrollStyle();
        nodePanelObj.resultListClick();
        nodePanelObj.initScrollStyle2();
        nodePanelObj.resultListClick2();
        nodePanelObj.initScrollStyle3();

    }
};



function formatTopoData(indata) {
    var returnArr = [];
    for(var i=0;i<indata.length;i++){
        var tmp = indata[i];
        var cur = tmp;
        cur.name = tmp.nename;
        if(getClassLevel(tmp.netype)==0){
            cur.symbolSize = [48, 48];
            cur.symbol = "image://../assets/images/iconALLHSS.png";
        }else if(getClassLevel(tmp.netype)==1){
            cur.symbolSize = [48, 48];
            cur.symbol = "image://../assets/images/iconHSS.png";
        }else if(getClassLevel(tmp.netype)==2){
            cur.symbolSize = [32, 32];
            cur.symbol = "image://../assets/images/iconGWF.png";
        }else if(getClassLevel(tmp.netype)==3){
            cur.symbolSize = [32, 32];
            cur.symbol = "image://../assets/images/iconHSSUNIT.png";
        }else{
            cur.symbolSize = [32, 32];
            cur.symbol = "image://../assets/images/iconALLHSS.png";
        }
        if(tmp.children){
            cur.children = arguments.callee(tmp.children)
        }
        returnArr.push(cur);
    }
    return returnArr;
}

function getClassLevel(param) {
    var classArr = [
        ["ALLHSS"],
        ["SGW","NTHLRFE","HSSFE","ONE_NDS"],
        ["HSSTYPE"],
        ["SOAP_GW","HLR-FE","PCC","HLR-TIAMS","HLR-AHUB","HSSFE","HSS-PCC","HSS-TIAMS","HSS-AHUB","ROUTING-DSA",
         "BE-DSA","PGW","PGW_DSA","ADM","INS","DRA","OMU","TIAMS","SWITCH","NTHLRFE","VC","R_DSA","ADM_SERVER",
         "INSTALL_SERVER","CISCO_SWITCH","BE_DSA","OA","HSS_DRA","SGW","CE", "SLH","ILON"]
    ];
    for (var i = 0; i < classArr.length; i++) {
        var tmpClassArr = classArr[i];
        if(tmpClassArr.indexOf(param)>= 0){
            return i;
        }
    }
    return 0;
}

//接收url参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


$(function () {
	
	$.ajax({
		url : "rest/equipment-ne",
		type : "GET",
		dataType: "json",
//        contentType: "application/json;charset=UTF-8",
		success : function(data) {	
			var htmltemp = "";
			if(data._embedded){
				$.each(data._embedded["equipment-ne"],function(index,item){
			    	if(htmltemp.indexOf(item.dhssName) == -1){
			    		htmltemp += "<li><a href='dhssTopology?HSSNAME=" + item.dhssName + "'>" + item.dhssName + "</a></li>";
			    	}
			    })
			}
			$("#navList").html(htmltemp);
			$("#navList a[href='dhssTopology?HSSNAME="+$("#dhssName").val()+"']").addClass("active");
		}
	});		
	
	
	
	
    $(window).on("resize",function(){
        //窗口调整时，更新拓扑图的展示区域
        $("#topologicalmap").height($(window).height()-120);

        if ($("#nodePanel").is(":hidden") == false) {
            var tmpgridHeight = $(window).height()-360;
            $("#resultWrap").height(tmpgridHeight<260?260:tmpgridHeight);
            $("#result2Wrap").height(tmpgridHeight<260?260:tmpgridHeight);
            $("#result2Wrap").height(tmpgridHeight<260?260:tmpgridHeight);
        }
        if ($("#nodeAhubPanel").is(":hidden") == false) {
            var tmpgridHeight = $(window).height()-220;
            $("#result3Wrap").height(tmpgridHeight<430?430:tmpgridHeight);
        }
    });

    // 高亮显示当前模块导航
    // getQueryString("NENAME")
    var urlstr = location.href;//获取浏览器的url
    var urlstatus=false;//标记
    //遍历导航div
    $("#navList a").each(function () {
        //判断导航里面的rel和url地址是否相等
        if ((urlstr + '/').indexOf($(this).attr('href')) > -1&&$(this).attr('href')!='') {
            $(this).addClass('active');
            urlstatus = true;
        } else {
            $(this).removeClass('active');
        }
    });
    if (!urlstatus) {$("#navList a").eq(0).addClass('active'); }
});

var alarmData = [
    {
        objName: "SGW1",
        time1: "2016/04/28 12:34",
        time2: "2016/05/03 14:32",
        acknowledge: "已确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏",
        alarmLevel: 5,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "SGW2",
        time1: "2016/04/27 11:45",
        time2: "2016/05/02 14:43",
        acknowledge: "已确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏",
        alarmLevel: 4,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "PGW1",
        time1: "2016/04/26 09:34",
        time2: "2016/05/02 08:32",
        acknowledge: "未确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏",
        alarmLevel: 2,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "PGW2",
        time1: "2016/04/25 12:43",
        time2: "2016/05/03 14:07",
        acknowledge: "未确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏",
        alarmLevel: 3,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "MME1",
        time1: "2016/04/24 10:05",
        time2: "2016/05/03 17:15",
        acknowledge: "未确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏",
        alarmLevel: 1,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "MME2",
        time1: "2016/04/23 12:34",
        time2: "2016/05/03 16:09",
        acknowledge: "已确认",
        alarmName: "驻波比重要告警",
        alarmDesc: "VSWR值超规定门限，天线不匹配或损坏不匹配或损坏",
        alarmLevel: 1,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "SGW1",
        time1: "2016/04/22 17:32",
        time2: "2016/05/03 12:08",
        acknowledge: "已确认",
        alarmName: "到某模块的连接丢失",
        alarmDesc: "到基站所属某模块的连接丢失",
        alarmLevel: 1,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "SGW1",
        time1: "2016/04/21 23:38",
        time2: "2016/05/05 09:44",
        acknowledge: "已确认",
        alarmName: "光接口发射故障",
        alarmDesc: "SFP发生故障或SFP模块不存在（无电气连接）。SFP为光纤的一部分  ",
        alarmLevel: 4,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "MME1",
        time1: "2016/04/20 12:34",
        time2: "2016/05/03 45:25",
        acknowledge: "未确认",
        alarmName: "网元操作维护连接失败",
        alarmDesc: "基站与iOMS的操作维护连接中断，可能是传输网络或基站传输板件出现故障",
        alarmLevel: 5,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }, {
        objName: "MME2",
        time1: "2016/04/20 09:45",
        time2: "2016/05/06 14:32",
        acknowledge: "已确认",
        alarmName: "网元操作维护连接失败",
        alarmDesc: "基站与iOMS的操作维护连接中断，可能是传输网络或基站传输板件出现故障",
        alarmLevel: 3,
        alarmExpression: "attach_rate.last( 0 ) <  95%",
    }];