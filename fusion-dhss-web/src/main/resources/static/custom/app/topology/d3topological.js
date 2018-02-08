/**
 * Created by john on 2016/8/2.
 */
//位置参数



$(function () {
    var nename = "HSS50";
    if(getQueryString("NENAME")){
        nename = getQueryString("NENAME");
        nodePanelObj.init();
    }

    var margin = {top: 20, right: 120, bottom: 20, left: 120},
        width = 960 - margin.right - margin.left,
        height = 800 - margin.top - margin.bottom;

    var i = 0,
        duration = 750,
        root;
// 声明树布局
    var tree = d3.layout.tree()
        .size([height, width]);
// 指定为径向布局
    var diagonal = d3.svg.diagonal()
        .projection(function(d) { return [d.y, d.x]; });

    var svg = d3.select("#topologicalmap").append("svg")
        .attr("width", width + margin.right + margin.left)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    d3.json("new-topology?HSS="+encodeURI($("#dhssName").val()), function(error, flare) {
        root = flare.result[0];
        root.x0 = height / 2;
        root.y0 = 0;
        function collapse(d) {
            if (d.children) {
                console.log(d);
                d._children = d.children;
                d._children.forEach(collapse);
                d.children = null;
            }
        }
        function collapseroot(d) {
            if (d.children) {
                d.children.forEach(collapse);
            }
        }
        root.children.forEach(collapseroot);
        update(root);
    });

    d3.select(self.frameElement).style("height", "800px");
    function update(source){
        var nodes = tree.nodes(root).reverse(),
            links = tree.links(nodes);

        nodes.forEach(function(d) {
            d.y = d.depth * 180;
        });

        var node = svg.selectAll("g.node")
            .data(nodes, function(d) {
                return d.id
                    || (d.id = ++i);
            });

        var nodeEnter = node.enter().append("g")
            .attr("class", "node")
            .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; });
        nodeEnter.append("circle")
            .attr("r", 1e-6)
            .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; })
            .on("click", click);
        nodeEnter.append("svg:image")
            .attr("xlink:href", function (d) {
                if(getClassLevel(d.netype)==0){
                    return "../assets/images/iconALLHSS.png";
                }else if(getClassLevel(d.netype)==1){
                    return "../assets/images/iconSite.png";
                }else if(getClassLevel(d.netype)==2){
                    return "../assets/images/iconHSS.png";
                }else if(getClassLevel(d.netype)==3){
                    return "../assets/images/iconGWF.png";
                }else if(getClassLevel(d.netype)==4){
                    return "../assets/images/iconHSSUNIT.png";
                }else{
                    return "../assets/images/iconWIFI.png";
                }

            })
            .attr("x", "-36px")
            .attr("y", "-16px")
            .attr("width", "32px")
            .attr("height", "32px").on("click", click2);;



        nodeEnter.append("text")
            .attr("x", function(d) { return d.children || d._children ? -36 : 10; })
            .attr("dy", ".35em")
            .attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
            .text(function(d) { return d.nename; })
            .style("fill-opacity", 1e-6);

        //(2-5)过渡到新位置
        var nodeUpdate = node.transition()
            .duration(duration)
            .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });

        nodeUpdate.select("circle")
            .attr("r", 4.5)
            .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });

        nodeUpdate.select("text")
            .style("fill-opacity", 1);

        var nodeExit = node.exit().transition()
            .duration(duration)
            .attr("transform", function(d) {
                return "translate(" + source.y + "," + source.x + ")";
            })
            .remove();

        nodeExit.select("circle")
            .attr("r", 1e-6);

        nodeExit.select("text")
            .style("fill-opacity", 1e-6);

        var link = svg.selectAll("path.link")
            .data(links, function(d) { return d.target.id; });

        link.enter().insert("path", "g")
            .attr("class", "link")
            .attr("d", function(d) {
                var o = {x: source.x0, y: source.y0};
                return diagonal({source: o, target: o});
            });

        link.transition()
            .duration(duration)
            .attr("d", diagonal);

        link.exit().transition()
            .duration(duration)
            .attr("d", function(d) {
                var o = {x: source.x, y: source.y};
                return diagonal({source: o, target: o});
            })
            .remove();

        nodes.forEach(function(d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }


    function click(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        update(d);
    }
    function click2(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        update(d);//要更新
        nodePanelObj.eConsole(d);
    }

});


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
            type:"POST",
            success: function(data){
//                var mydatas = JSON.stringify(data);
                nodePanelObj.updateTopoData(data.result);
                //alert(mydatas);
                //JSON.parse()
            }
        });
    },
    getNodeKPIData: function(inNodeInfo) {
        console.log(inNodeInfo);
        $.ajax({
            url: "findKpiInfoByEquipmentName",
            data:{"NENAME" :inNodeInfo.neid},
            cache: false,
            success: function(data){
//                var mydatas = JSON.stringify(data);
                nodePanelObj.initResultList(data);
            }
        });
    },
    getNodeAlarmData: function(inNodeInfo) {
        console.log(inNodeInfo);
        $.ajax({
            url: "new-topology-alarm",
            data:{"NENAME" :inNodeInfo.cnum},
            cache: false,
            success: function(data){
                var mydatas = JSON.stringify(data);
                nodePanelObj.initResultList2(data.result);
            }
        });
    },
    getNodeAhubData: function(inNodeInfo) {
        $.ajax({
            url: "new-topology-ahub",
            data:{"NENAME" :inNodeInfo.neid},
            cache: false,
            success: function(data){
//                var mydatas = JSON.stringify(data);
                nodePanelObj.resultAhubData(/*data.result*/data.result);
            }
        });
    },
    updateTopoData: function(indata) {
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
        var tmpNeType = param.netype;
        if(tmpNeType=="AHUB" || tmpNeType=="AHUB"){
            nodePanelObj.showAhubPanel(param);
            nodePanelObj.getNodeAhubData(param);
        }else if(tmpNeType=="ALLHSS"){

        }else{
            nodePanelObj.showPanel(param);
            nodePanelObj.getNodeKPIData(param);
            nodePanelObj.getNodeAlarmData(param);
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
        $("#result3Wrapa table tbody").html(liHtml);
    },
    showPanel:function(inItem){
        $("#nodePanel").show();
        $("#nodePanel .eNodeBName").html(inItem.nename);

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
        $("#nodeAhubPanel .eNodeBName").html(inItem.nename);

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
            cur.symbolSize = [48, 32];
            cur.symbol = "image://../assets/images/iconSite.png";
        }else if(getClassLevel(tmp.netype)==2){
            cur.symbolSize = [48, 48];
            cur.symbol = "image://../assets/images/iconHSS.png";
        }else if(getClassLevel(tmp.netype)==3){
            cur.symbolSize = [32, 32];
            cur.symbol = "image://../assets/images/iconGWF.png";
        }else if(getClassLevel(tmp.netype)==4){
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
	                ["HSSTYPE"],
	                ["SGW","NTHLRFE","HSSFE","ONE_NDS"],
	                ["SOAP_GW","HLR-FE","PCC","HLR-TIAMS","HLR-AHUB","HSSFE","HSS-PCC","HSS-TIAMS","AHUB","ROUTING-DSA",
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
			    		var url = encodeURI("dhssTopology?HSSNAME=" + item.dhssName );
			    		var flag = item.dhssName == $("#dhssName").val() ? "active" : "";
			    		htmltemp += "<li><a href='" + url + "'  class= '"+ flag +"'>" + item.dhssName + "</a></li>";
//			    		$("#navList a[href='"+url+"']").addClass("active");
			    	}
			    })
			}
			$("#navList").html(htmltemp);
//			var url = encodeURI("dhssTopology?HSSNAME=" + $("#dhssName").val() );
			
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

    if(getQueryString("NENAME")){
        nodePanelObj.init();
        //nodePanelObj.getTopoData(getQueryString("NENAME"));
    }else{
        nodePanelObj.init();
        //nodePanelObj.getTopoData("HSS50");
    }

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