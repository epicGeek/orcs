
var quotaScene = [];
var quotaKpiList =[];
var allNeNameArray = [];
function allbscname(){
	
	var dataSource = new kendo.data.DataSource({
		transport : { 
			read : {
				type : "GET",
				url : "rest/kpi-item",
				dataType : "json",
				contentType : "application/json;charset=UTF-8"
			}
		},
		schema : {
			data : function(d) {
				if(d._embedded){
					var array = [];
					$.each(d._embedded["kpi-item"],function(index,item){
						if(item.outPutField=="success_rate"){
							item.kpiName=item.kpiName+"";
						}
						if(item.outPutField=="success_count"){
							item.kpiName=item.kpiName+"";
						}
						if(item.outPutField=="fail_count"){
							item.kpiName=item.kpiName+"";
						}
						if(item.outPutField=="fail_rate"){
							item.kpiName=item.kpiName+"";
						}
						if(item.outPutField=="total_count"){
							item.kpiName=item.kpiName+"";
						}
						array[array.length] = item;
						if($.inArray(item.kpiCategory,quotaScene) == -1 ){
							quotaScene[quotaScene.length] = item.kpiCategory;
						}
					})
					quotaKpiList = array;
					return array;  //响应到页面的数据
			     }else{
					return new Array();
				}
			}
		}
	});
	
	$("#inputBusinessQuota").kendoDropDownList({
		optionLabel:"请选择指标",
        dataTextField: "kpiName",
        dataValueField: "kpiCode",
        dataSource: dataSource,
        filter: "contains",
        suggest: true,
    });
								$.ajax({
									url:"allQuotaControll/queryBscName",
									datatype : 'json',
									timeout:5000,
									cache:false,
									success:function(msg){
										
										var datas = msg.rows;
										var allDhss = [];
										var allLocation = [];
										var allNe = [];

						  			  	allLocation = ["全部局址"];
						  			  	allNe = [{neName : "全部网元" , id : -1}];
						  			  	
										$.each(datas,function(index,item){
											if($.inArray(item.dhssName,allDhss) == -1){
												allDhss[allDhss.length] = item.dhssName;
											}
											if($.inArray(item.location,allLocation) == -1){
												allLocation[allLocation.length] = item.location;
											}
											if($.inArray(item.neName,allNeNameArray) == -1){
												allNeNameArray.push({neName:item.neName});
											}
										})		
										$("#inputDhssQuota").kendoDropDownList({        //DHHS组、局址、网元、指标名称的下拉菜单在此
											  optionLabel:"请选择DHSS组",
									  		  dataSource:allDhss,
									  		  filter: "contains",
									  		  suggest: true,
									  		  change:function(event){
									  			  	allLocation = ["全部局址"];
									  			  	allNe = [];
										  			var dataItem = (this.dataItem(event.item));
										  			if($.inArray($("#inputDhssQuota").data("kendoDropDownList").text(),allDhss) == -1){
										  				$('#inputNeNameQuota').data("kendoDropDownList").text("请选择网元");
										  				$('#inputLocationQuota').data("kendoDropDownList").text("请选择局址");
										  				return false;
										  			}
										  			$.each(datas,function(index,item){
														if(dataItem == item.dhssName && $.inArray(item.location,allLocation) == -1){
															allLocation[allLocation.length] = item.location;
														}
													})
													$("#inputLocationQuota").kendoDropDownList({
												  		  dataSource:allLocation,
												  		  filter: "contains",
												  		  suggest: true,
												  		  change:function(event){
												  			  	allNe = [];
													  			var dataItem = (this.dataItem(event.item));
													  			$.each(datas,function(index,item){
																	if( dataItem == item.location && $.inArray(item.neName,allNe) == -1){
																		allNe[allNe.length] = { neName : item.neName , id : item.id };
																	}
																})
													  			if(allNe.length==0){
													  				allNe = allNeNameArray;
													  			}
																$("#inputNeNameQuota").kendoDropDownList({
																	  dataTextField: "neName",
																      dataValueField: "neName",
															  		  dataSource: allNe,
															  		  filter: "contains",
															  		  suggest: true
															    });
													  			$('#inputNeNameQuota').data("kendoDropDownList").text(allNe[0].neName);
																
												  		  }
												    });
													$('#inputLocationQuota').data("kendoDropDownList").text(allLocation[0]);
													$('#inputLocationQuota').val(allLocation[0]);
													$.each(datas,function(index,item){
														if( allLocation[0] == item.location && $.inArray(item.neName,allNe) == -1){
															allNe[allNe.length] = { neName : item.neName , id : item.id };
														}
													})
										  			if(allNe.length==0){
										  				allNe = allNeNameArray;
										  			}
													$("#inputNeNameQuota").kendoDropDownList({
														  dataTextField: "neName",
													      dataValueField: "neName",
												  		  dataSource: allNe,
												  		  filter: "contains",
												  		  suggest: true
												    });
													$('#inputNeNameQuota').data("kendoDropDownList").text(allNe[0].neName);
									  		  }
									    });
										$("#inputLocationQuota").kendoDropDownList({
											  optionLabel:"请选择局址",
									  		  dataSource:allLocation,
									  		  filter: "contains",
									  		  suggest: true,
									  		  change:function(event){
									  			  	allNe = [];
										  			var dataItem = (this.dataItem(event.item));
										  			$.each(datas,function(index,item){
														if( dataItem == item.location && $.inArray(item.neName,allNe) == -1){
															allNe[allNe.length] = { neName : item.neName , id : item.id };
														}
													});
										  			if(allNe.length==0){
										  				allNe = allNeNameArray;
										  			}
													$("#inputNeNameQuota").kendoDropDownList({
														  dataTextField: "neName",
													      dataValueField: "neName",
												  		  dataSource: allNe,
												  		  filter: "contains",
												  		  suggest: true
												    });
													$('#inputNeNameQuota').data("kendoDropDownList").text(allNe[0].neName);
										  			
													
									  		  }
									    });
										$("#inputNeNameQuota").kendoDropDownList({
											  optionLabel:"请选择网元",
											  dataTextField: "neName",
										      dataValueField: "neName",
									  		  dataSource: msg.rows,
									  		  filter: "contains",
									  		  suggest: true
									    });
										handleSiteData(msg);
									}
								});
								
								
								
															}