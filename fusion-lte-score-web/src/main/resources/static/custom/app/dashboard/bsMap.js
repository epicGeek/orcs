var detailsData = [];
//var yunnanJson = [];
var searchData = [];
var today = new Date();
var parameters={
		areaCode:'',cityCode:'',searchIndex:'',
		startDate:new Date(today.getFullYear(),today.getMonth(),today.getDate()-1).Format("yyyy-MM-dd"),
		endDate:new Date(today).Format("yyyy-MM-dd"),grade:'1'
};
var mapUrl = "";
function initDetailsData(){
	var dtd = $.Deferred(); // 新建一个Deferred对象
	$.ajax({
		dataType : 'json',
		type : "post",
		contentType : "application/json;charset=utf-8",
		url : "dashboard/searchBtsMap",
		data : kendo.stringify(parameters),
		async:false,
		success : function(data) {
			detailsData = data;
			//yunnanJson = data.yunnanJson;
			dtd.resolve(); // 改变Deferred对象的执行状态
		}
	});
	
	return dtd;
}


/**
 * 查询函数
 */
function search() {
	$("#resultWrap ul").empty();
	var liHtml = "";	
	parameters.areaCode=$('#areaCode').val(); //地区
	parameters.cityCode=$('#cityCode').val(); 
	parameters.startDate=$('#startDate').val(); 
	parameters.endDate=$('#endDate').val(); 
	parameters.searchIndex =$('#searchIndex').val();//关键字
	var g = [];
	$("#mapControl ul li input[type=checkbox]").each(function(){
	    if(this.checked){
	      g.push($(this).val());
	    }
	}); 
	parameters.grade=g.join(',');
	//执行查询动作
	initDetailsData();
	
	$.each(detailsData, function(i, item) {
		if(detailsData[i].latlng!=null){
			if(searchIndex!="" && searchIndex!=null){
				var proxyX = detailsData[i].latlng[0]+"";
				var proxyY = detailsData[i].latlng[1]+"";
				var bsNo = detailsData[i].bsNo+"";
				if(proxyX.indexOf(searchIndex) !=-1 || proxyY.indexOf(searchIndex) !=-1){
						liHtml += "<li class='liList' lat='" + detailsData[i].latlng[0] + "' lng='" + detailsData[i].latlng[1] + "'>" + detailsData[i].locateName + "</li>";
				}else{
					liHtml += "<li class='liList' lat='" + detailsData[i].latlng[0] + "' lng='" + detailsData[i].latlng[1] + "'>" + detailsData[i].locateName + "</li>";
				}
			}else{
				if(parameters.areaCode!="0"){
					liHtml += "<li class='liList' lat='" + detailsData[i].latlng[0] + "' lng='" + detailsData[i].latlng[1] + "'>" + detailsData[i].locateName + "</li>";						
				}
			}
		}
	});
	
	$("#resultWrap ul").html(liHtml);
}

$(function() {
	$("#startDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: parameters.startDate
	});
	$("#endDate").kendoDatePicker({
		format: "yyyy-MM-dd",
		value: parameters.endDate
	});

	// 【基站查询】
	var queryBSObj = {

		// 【基站查询】结果列表设置滚动条样式
		initScrollStyle: function() {
			$("#resultWrap").mCustomScrollbar({
				theme: "minimal-dark",
				scrollbarPosition: "inside",
				autoHideScrollbar: true
			});
		},
		// 生成【基站查询】结果列表，detailsData：见mapDataSource--结果数据
		initResultList: function() {		
		},
		// 点击【基站查询】结果列表
		resultListClick: function() {
			$("#resultWrap").delegate("li.liList", "click", function() {
				$("#resultWrap li.active").removeClass("active");
				var that = $(this);
				that.addClass("active");
				var latlng = [parseFloat(that.attr("lat")), parseFloat(that.attr("lng"))];
				
				// 显示所有的星级图标
				//$(".star1,.star2,.star3,.star4,.star5").show();
				$(".star1").show();
				$("#mapControl input[type='checkbox']").prop("checked", true);
				$.each(mapObj.starChecked, function(index, value) {
					mapObj.starChecked[index] = true;
				});

				// 地图定位
				mapObj.locationAndZoom(latlng);
			});
		},
		init: function() {
			this.initScrollStyle();
			this.initResultList();
			this.resultListClick();
		}
	}; // 【基站查询】结束

	// class='star1 star2 star3 star4 star5'：与星级对应
	var mapObj = {
		mapId: "mapBox",
		minZoom: 6, // 地图最小缩放倍数
		maxZoom: 16, // 地图最大缩放倍数
		mapUrl: "/map/YunNanMap", // 加载地图的路径

		map: undefined, // 地图对象

		starChecked: { // 记录哪个星级选中，未选中：隐藏false -- 选中：显示true，默认所有图标都显示
			"star1": true,
			"star2": false,
			"star3": false,
			"star4": false,
			"star5": false
		},

		// 将云南范围高亮
		highligherArea: function() {

			var styles = {
				color: "#0000FF",
				weight: 2,
				opacity: 0.6,
				fill: true,
				fillColor: "#0000FF",
				fillOpacity: 0.06
			};
			var polygon = L.polygon(yunnanJson, styles).addTo(mapObj.map);
			mapObj.map.fitBounds(polygon.getBounds());
		},

		// 将div生成地图，并将地图图层添加到地图上
		initMap: function() {
			var mapLayer = new L.TileLayer.TileLoad(mapObj.mapUrl, {
				maxZoom: mapObj.maxZoom,
				minZoom: mapObj.minZoom,
				continuousWorld: true
			});

			var map = L.map(mapObj.mapId, {
				attributionControl: false
			}).setView([25.04, 102.42], 7);

			mapLayer.addTo(map);

			return map;
		},

		// 生成图标图层
		initMarkersLayer: function() {
			mapObj.markersLayer = L.markerClusterGroup({
				disableClusteringAtZoom: 16,
				zoomToBoundsOnClick: false, // 阻止点击汇聚点的时候，放大地图
				polygonOptions: {
					weight: 3,
					color: '#ff0000',
					opacity: 0.6
				},
				// 汇聚的icon图标
				iconCreateFunction: function(cluster) {
					var icon = L.divIcon({
						html: "<img class='starGroup' src='custom/images/bsGroup.gif' />",
						iconAnchor: [0, 20]
					});
					return icon;
				}
			});

			mapObj.group1 = L.featureGroup.subGroup(mapObj.markersLayer);
			mapObj.group2 = L.featureGroup.subGroup(mapObj.markersLayer);
			mapObj.group3 = L.featureGroup.subGroup(mapObj.markersLayer);
			mapObj.group4 = L.featureGroup.subGroup(mapObj.markersLayer);
			mapObj.group5 = L.featureGroup.subGroup(mapObj.markersLayer);

			mapObj.markersLayer.addTo(mapObj.map);
		},
		// 生成散列marker
		initMarkers: function(markersListData, markersLayer) {
			for (var i = 0; i < markersListData.length; i++) {
				var item = markersListData[i],
					imgUrl, className;
				
				if(item.latlng ==null || item.latlng == "null"){
					return ;
				}
				switch (item.star) {
					case 1:
						imgUrl = "custom/images/bsRed.gif";
						className = "star1";
						break;
					case 2:
						imgUrl = "custom/images/bsOrange.gif";
						className = "star2";
						break;
					case 3:
						imgUrl = "custom/images/bsYellow.gif";
						className = "star3";
						break;
					case 4:
						imgUrl = "custom/images/bsBlue.gif";
						className = "star4";
						break;
					case 5:
						imgUrl = "custom/images/bsGreen.gif";
						className = "star5";
						break;
				}
				// 生成icon
				var icon = L.divIcon({
					//
					html:  "<a href='bsCurrentScore?areaCode="+item.areaCode+"&cityCode="+item.cityCode+"&type=2"+"&tjType=2"+
					"&neCode="+item.bsNo+"&startDate="+parameters.startDate+"&endDate="+parameters.endDate+"'>" 
					+"<img class='" + className + "' src='" + imgUrl + "' />"
					+"</a>"
				});
				
				// 生成icon标示对象
				var marker = L.marker(item.latlng, {
						icon: icon,
						iconAnchor: [0, 0],
						iconSize: [42, 42],
						shadowAnchor: [0, 0],
						riseOnHover: true,
						title: item.locateName,
						star: item.star, // 附加属性，星级
						score: item.score, // 附加属性，得分
						bsNo: item.bsNo, // 附加属性，站号
					});
				/*if(marker==null || marker=="null"){
					return "";
				}else{
					return marker;
				}*/
				//markersLayer.addLayer(marker);
				//marker.addTo(item.star==1 ? mapObj.group1 : item.star==2 ? mapObj.group2 : item.star==3 ? mapObj.group3  : item.star==4 ? mapObj.group4:item.star==5 ? mapObj.group5);
				switch (item.star) {
					case 1: marker.addTo(mapObj.group1);break;
					case 2: marker.addTo(mapObj.group2);break;
					case 3: marker.addTo(mapObj.group3);break;
					case 4: marker.addTo(mapObj.group4);break;
					case 5: marker.addTo(mapObj.group5);break;
				}
			}
		},

		// 根据选择的地址，在地图上定位且地图缩放到最大
		locationAndZoom: function(latlng) {

			// 地图上定位到当前点，
			mapObj.map.setView(latlng, mapObj.map.getMaxZoom());

			var icon = L.divIcon({
				className: 'highlightIcon'
			});

			var markerLay = L.marker(latlng, {
				icon: icon,
				iconAnchor: [0, 0],
				iconSize: [52, 52],
				shadowAnchor: [0, 0],
				riseOnHover: true
			});
			mapObj.map.addLayer(markerLay);

			window.setTimeout(function() {
				mapObj.map.removeLayer(markerLay);
			}, 3000);

		},

		// 图层checkbox选择
		hideOrShowMarkers: function() {
			$("#mapControl li input[type='checkbox']").on("change", function(e) {
				var imgClass = $(this).attr("imgClass");
				var checked = $(this).prop("checked");
				var star = imgClass.charAt(imgClass.length - 1);

				mapObj.starChecked[imgClass] = checked;

				if (checked) {
					$("." + imgClass).show();
					switch(imgClass){
						case "star1": mapObj.markersLayer.addLayer(mapObj.group1);break;
						case "star2": mapObj.markersLayer.addLayer(mapObj.group2);break;
						case "star3": mapObj.markersLayer.addLayer(mapObj.group3);break;
						case "star4": mapObj.markersLayer.addLayer(mapObj.group4);break;
						case "star5": mapObj.markersLayer.addLayer(mapObj.group5);break;
					}
				} else {
					$("." + imgClass).hide();
					
					switch(imgClass){
						case "star1": mapObj.markersLayer.removeLayer(mapObj.group1);break;
						case "star2": mapObj.markersLayer.removeLayer(mapObj.group2);break;
						case "star3": mapObj.markersLayer.removeLayer(mapObj.group3);break;
						case "star4": mapObj.markersLayer.removeLayer(mapObj.group4);break;
						case "star5": mapObj.markersLayer.removeLayer(mapObj.group5);break;
					}
				}
				//mapObj.markersLayer.refreshClusters();
			});
		},

		// 在缩放的时候，根据checkbox 选中状态，显示图标
		mapMoveend: function() {
			this.map.on("moveend", function() {
				$.each(mapObj.starChecked, function(index, value) {
					if (value) {
						$("." + index).show();
					} else {
						$("." + index).hide();
					}
				});
			});
		},

		init: function() {
			this.map = this.initMap();
			this.highligherArea();
			this.initMarkersLayer(); //汇聚图层
			
			//this.initMarkers(detailsData, this.markersLayer); //根据数据添加icons ----- 在此设置读取数据
			
			//分步加载marker数据
			function initAllMarkers(){
				var dtd = $.Deferred();
				var loopNum = 100;
				var num = Math.ceil(detailsData.length/loopNum);
				var currentLoopNum = 1;
				var interval = window.setInterval(function(){
					if (currentLoopNum <= loopNum) {
							var startIndex = (currentLoopNum - 1) * num;
							var endIndex = currentLoopNum * num;
							if (endIndex > detailsData.length) {
									endIndex = detailsData.length;
							}
							var dataList = detailsData.slice(startIndex,endIndex);
							mapObj.initMarkers(dataList, mapObj.markersLayer);
							currentLoopNum += 1;
					} else {
						window.clearInterval(interval);
						dtd.resolve();
					}
				},100);
				return dtd.promise();
			};
			//将每一级的基站图层添加到地图上
			$.when(initAllMarkers())
			.done(function(){
				mapObj.group1.addTo(mapObj.map);
				mapObj.group2.addTo(mapObj.map);
				mapObj.group3.addTo(mapObj.map);
				mapObj.group4.addTo(mapObj.map);
				mapObj.group5.addTo(mapObj.map);
				$("#loading").hide();
			});

			//this.map.addLayer(this.markersLayer); //添加图标图层

			this.markersLayer.on('clusterclick', function(a) { // 点击汇聚图标事件

				var childMarkers = a.layer.getAllChildMarkers();

				var gridList = []; // gridList--汇聚点下的所有marker数据
				for (var i = 0, len = childMarkers.length; i < len; i++) {
					var item = childMarkers[i];
					var latlng = item._latlng;
					var options = item.options; 
					
					if (mapObj.starChecked["star" + options.star]) { // 如果该星级现在在地图上，弹框列表中则有该数据，否则，列表中不显示
						gridList.push({
							latlng: latlng,
							bsName: options.title,
							star: options.star,
							score: options.score,
							bsNo: options.bsNo
						});
					}
				}

				// 按星级排序
				gridList.sort(function(a, b) {
					return a.star - b.star;
				});
				
				$('#detailsModal').modal("show");
				
				var browser=navigator.appName;
				var b_version=navigator.appVersion;
				var version=b_version.split(";");
				var trim_Version = undefined;
				if(version[1]){
					trim_Version = version[1].replace(/[ ]/g,""); 
				}
				if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE8.0"){
					modalObj.resetGrid(gridList);
				} else {
					// 生成弹窗表格
					$('#detailsModal').on('shown.bs.modal', function() {
						modalObj.resetGrid(gridList);
					});
				}
			});

			// 显示或隐藏图标
			this.hideOrShowMarkers();
			// 在缩放的时候，根据checked,隐藏显示图标
			this.mapMoveend();
		}
	};

	/* 以下为弹窗 */
	var modalObj = {
		initGrid: function(gridList) {
			modalObj.dataGridObj = $("#detailsGrid").kendoGrid({
				dataSource: {
					data: gridList,
					pageSize: 10,
					serverPaging: false
				},
				
				height: 280,
				reorderable: true,
				resizable: true,
				sortable: true,
				columnMenu: true,
				pageable: true,
				columns: [{
					width: 84,
					field: "star",
					template: "#if(star==1){# <img src='custom/images/bsRed.gif' /> #} else if(star==2){#" +
						"<img src='custom/images/bsOrange.gif' /> #} else if(star==3){#" +
						"<img src='custom/images/bsYellow.gif' /> #} else if(star==4){#" +
						"<img src='custom/images/bsBlue.gif' /> #} else if(star==5){#" +
						"<img src='custom/images/bsGreen.gif' /> #}#",
					encoded: false,
					title: "<span  title='星级'>星级</span>"
				}, {
					field: "bsName",
					template: "<a class='bsNameBtn' title='#:bsName#'>#:bsName#</a>",
					encoded: false,
					title: "<span  title='站名'>站名</span>"
				}, {
					field: "score",
					template: "<span  title='#:score#'>#:score#</span>",
					encoded: false,
					title: "<span  title='得分'>得分</span>"
				}, {
					field: "bsNo",
					template: "<span title='#:bsNo#'>#:bsNo#</span>",
					encoded: false,
					title: "<span  title='站号'>站号</span>"
				}, {
					template: "<span  title='#:latlng.lng#'>#:latlng.lng#</span>",
					encoded: false,
					title: "<span  title='经度'>经度</span>"
				}, {
					field: "lat",
					template: "<span  title='#:latlng.lat#'>#:latlng.lat#</span>",
					encoded: false,
					title: "<span  title='纬度'>纬度</span>"
				}],
				dataBound: function() {
					// 点击基站名称
					$(".bsNameBtn").on("click", function() {
						var dataItem = modalObj.dataGridObj.dataItem($(this).closest("tr"));
						// 关闭弹窗
						$('#detailsModal').modal('hide');
						// 地图定位
						mapObj.locationAndZoom([dataItem.latlng.lat, dataItem.latlng.lng]);
					});
					 
				}
			}).data("kendoGrid");
		},
		resetGrid: function(gridList){
			if (modalObj.dataGridObj) {
						var dataSource = new kendo.data.DataSource({
							data: gridList,
						});
						modalObj.dataGridObj.setDataSource(dataSource);
					} else {
						modalObj.initGrid(gridList);
					}
		}
		
	};
	
	//依序执行，得到的是同步执行的效果
	$.when(initDetailsData()).done(function(){
		modalObj.initGrid([]); //弹框
		mapObj.init(); //地图
		queryBSObj.init();  //基站查询
		initAreaList(); //地市下拉
		initCityList(); //区县
		$('#searchBtn').on('click', function() {  //查询按钮
			search();
		});
		
	});
	
});
function initAreaList(){
	var defaultData = [{ areaCode: "0" , areaCn: "全部" }];
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "city/search",
		"success" : function(data) {
			
			$.each(data,function(index,item){
				defaultData.push({areaCode: item.areaCode, areaCn: item.areaCn });
			});
			$("#areaCode").kendoDropDownList({
				//optionLabel:"--请选择地市--",
	            dataTextField: "areaCn",
	            dataValueField: "areaCode",
	            dataSource: defaultData,
	            filter: "contains",
	            index: 0, // 当前默认选中项，索引从0开始。
	            change: function(){
	            	var areaCode = $('#areaCode').val();
	            	getCityData(areaCode);
	            }
	        });
			
		}
	});
}

function getCityData(areaCode){
	var defaultData ="";
   
	defaultData = [{ cityCode: "0" , cityCn: "全部" }];
	 
	
	$.ajax({
		"dataType" : 'json',
		"type" : "GET",
		"url" : "area/search",
		data:{
			"cityCode":areaCode
		},
		index: 0, // 当前默认选中项，索引从0开始。
		"success" : function(data) {
			
			$.each(data,function(index,item){
				defaultData.push({cityCode: item.cityCode, cityCn: item.cityCn });
			});
			$("#cityCode").data("kendoDropDownList").setDataSource(defaultData);
			//$("#cityCode").data("kendoDropDownList").refresh();
		}
	});
}

function initCityList(){
	var defaultData = [{ cityCode: "0" , cityCn: "全部" }];
	 
	$("#cityCode").kendoDropDownList({
		//optionLabel:"--请选择区县--",
        dataTextField: "cityCn",
        dataValueField: "cityCode",
        dataSource: defaultData,
        filter: "contains",
        index: 0, // 当前默认选中项，索引从0开始。
        
    });
}

/** **********以下为地图公用方法*********** */
/**
 * 加载本地arcgis切片类
 * 
 * 继承自TileLayer
 * 
 * @param {Object}
 *            tomcat中映射该切片目录url
 * @param {Object}
 *            options
 */
L.TileLayer.TileLoad = L.TileLayer.extend({
	initialize: function(url, options) {
		options = L.setOptions(this, options);
		this.url = url + "/{z}/{x}/{y}.png";
		L.TileLayer.prototype.initialize.call(this, this.url, options);
	}
});

/**
 * 重写TileLayer中获取切片url方法
 * 
 * @param {Object}
 *            tilePoint
 */
L.TileLayer.prototype.getTileUrl = function(tilePoint) {

	return L.Util.template(this._url, L.extend({
		s: this._getSubdomain(tilePoint),
		z: function() {
			var value = (tilePoint.z).toString().toUpperCase();
			return "L" + pad(value, 2);
		},
		x: function() {
			var value = (tilePoint.y).toString(16).toUpperCase();
			return "R" + pad(value, 8);
		},
		y: function() {
			var value = (tilePoint.x).toString(16).toUpperCase();
			return "C" + pad(value, 8);
		}
	}));
};
L.tileLayer.tileLoad = function(url, options) {
	return new L.TileLayer.TileLoad(url, options);
};
/**
 * 高位补全方法
 * 
 * @param {Object}
 *            数字类型字符串
 * @param {Object}
 *            总位数，不足则高位补0
 */
var pad = function(numStr, n) {
	var len = numStr.length;
	while (len < n) {
		numStr = "0" + numStr;
		len++;
	}
	return numStr;
};

/** *******Array map******* */
if (!Array.prototype.map) {
	Array.prototype.map = function(callback, thisArg) {

		var T, A, k;

		if (this == null) {
			throw new TypeError(" this is null or not defined");
		}

		// 1. 将O赋值为调用map方法的数组.
		var O = Object(this);

		// 2.将len赋值为数组O的长度.
		var len = O.length >>> 0;

		// 4.如果callback不是函数,则抛出TypeError异常.
		if ({}.toString.call(callback) != "[object Function]") {
			throw new TypeError(callback + " is not a function");
		}

		// 5. 如果参数thisArg有值,则将T赋值为thisArg;否则T为undefined.
		if (thisArg) {
			T = thisArg;
		}

		// 6. 创建新数组A,长度为原数组O长度len
		A = new Array(len);

		// 7. 将k赋值为0
		k = 0;

		// 8. 当 k < len 时,执行循环.
		while (k < len) {

			var kValue, mappedValue;

			// 遍历O,k为原数组索引
			if (k in O) {

				// kValue为索引k对应的值.
				kValue = O[k];

				// 执行callback,this指向T,参数有三个.分别是kValue:值,k:索引,O:原数组.
				mappedValue = callback.call(T, kValue, k, O);

				// 返回值添加到新数组A中.
				A[k] = mappedValue;
			}
			// k自增1
			k++;
		}

		// 9. 返回新数组A
		return A;
	};
	
}