//查询条件
var result = "";
//列title
var columns  =null;

var pgwGrid;

//查询条件 pgwStartTime:'',pgwEndTime:'',executionResult:'', operationUser  errorCode unit identifier
var searchObj = {searchField:'',executionResult:'',pgwStartTime:'',pgwEndTime:''};


function initGridData(){
	
	 dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "get",
                url: "pgwLogSearch",
                dataType : "json",
 				contentType : "application/json;charset=UTF-8"
            },
            parameterMap: function (options, operation) {
                if (operation == "read") {
                	//动态组装条件参数
                	searchObj.page = options.page;
                	searchObj.pageSize = options.pageSize;
        			searchObj.executionResult =$('#resultType').val();
                	searchObj.searchField = $("#pgwSearch").val();
                    return searchObj;
                }
            }
        },
        batch: true,
        pageSize: 10,
        schema: {
        	
            data: function (data) {
                return data.content;//返回页面数据
            },
            total: function (data) {
              //  return data.iTotalRecords;//总条数
                return data.totalElements;
            	//return data;
            },
            pageSize: function(data) {
                return data.size; //总条数
            }
        },
        serverPaging : true,
		serverFiltering : true,
		serverSorting : true
    });
	pgwGrid  = $("#pgwList").kendoGrid({
		
		  dataSource:dataSource,
		  height: 401,
		  groupable: false,
	      sortable: true,
	      resizable: true,
	      columnMenu: false,
	      reorderable: true,
	      pageable: {
	    	  refresh: true,
	    	  pageSizes: true,
	    	  buttonCount: 5,
	    	  pageSizes: [10, 20, 30],
	    	  messages: {
	    	  display: "显示 {0}-{1} 共 {2} 项",
	    	  empty: "没有数据",
	    	  itemsPerPage: "每面显示数量",
	    	  first: "第一页",
	    	  last: "最后一页",
	    	  next: "下一页",
	    	  previous: "上一页"
	    	  }
	      },
	        columns:[
	                   { field: "requestId", title:"ID"},
	                   { field: "identifier", title:"用户号码" },
	                   { field: "unit", title:"单元名称"},
	                   { field: "instance", title:"实例"},
	                   { field: "operationUser", title:"执行用户"},
	                   { field: "operation", title:"执行内容"},
	                   { field: "executionResult", title:"执行结果"},
	                   { field: "errorCode", title:"错误代码"},
	                   { field: "errorMessage", title:"错误内容"},
	                   { field: "responseTime", title:"时间",width:170},
	                   { field: "performContent", title:"指令", hidden:true},
	                   { title:"操作",
	                   	 template:"<button type='button' class='btn btn-success btn-xs'"
	                   		    + "onclick='openCmd(this)'>查看指令</button>"
	                   }
	            ]
	    }).data("kendoGrid");
}

function resultType(){
	
	 $("#resultType").kendoDropDownList({
		    optionLabel:"--全部结果类型--",
	        dataTextField: "text",
	        dataValueField: "value",
	        dataSource: [
	            { text: "success", value: "success" },
	            { text: "failure", value: "failure" },
	        ],
	       // filter: "contains",
	        suggest: true,
	    });
}

//查看执行指令
function openCmd(d){
	
	var cmdText = pgwGrid.dataItem($(d).closest("tr")).performContent;
	//弹出窗口
	openWindow('dialog','416','815','查看指令');
	$('#cmdText').text(formatXml(cmdText));
}

function openWindow(id,h,w,titleName){
	
	$("#"+id).kendoWindow({
		  maxHeight: h+'px',
		  maxWidth:w+'px',
		  draggable: false,
		  title:titleName
	});
	dialogObj = $("#"+id).data("kendoWindow");
	dialogObj.open();dialogObj.center();
}

$(function($) {
	
	kendo.culture("zh-CN");
	
	//列表加载初始化
	initGridData();
	
	resultType();

	// 时间选择查询 
	 $("#startDateTime").kendoDateTimePicker({
	       value:new Date(),
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	   });
	 $("#endDateTime").kendoDateTimePicker({
	       value:new Date(),
           format: "yyyy-MM-dd HH:mm:ss",
           parseFormats: ["yyyy-MM-dd", "HH:mm:ss"]
	 });
	
	$('#searchBtn').on('click', function() {
			
			searchObj.pgwStartTime = $('#startDateTime').val();
			searchObj.pgwEndTime = $('#endDateTime').val();
			searchObj.executionResult =$('#resultType').val();
			searchObj.searchField = $('#pgwSearch').val();
			//条件查询重新加载 
			dataSource.read(searchObj);
		//	$('#pgwList').empty();
	});
	
	$("#pgwSearch").on('keyup', function(){
		searchObj.searchField = $('#pgwSearch').val();
		dataSource.read(searchObj);
	});
	
});

//格式化xml
function formatXml(text) {
    //去掉多余的空格  
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g, function($0, name, props) {
        return name + ' ' + props.replace(/\s+(\w+=)/g, " $1");  
    }).replace(/>\s*?</g, ">\n<");  
  
    //把注释编码  
    text = text.replace(/\n/g, '\r').replace(/<!--(.+?)-->/g,
            function($0, text) {  
                var ret = '<!--' + escape(text) + '-->';  
                //alert(ret);  
                return ret;  
            }).replace(/\r/g, '\n');  
  
    //调整格式  
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;  
    var nodeStack = [];  
    var output = text.replace(rgx, function($0, all, name, isBegin,  
            isCloseFull1, isCloseFull2, isFull1, isFull2) {  
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/')  
                || (isFull1 == '/') || (isFull2 == '/');  
        //alert([all,isClosed].join('='));  
        var prefix = '';  
        if (isBegin == '!') {  
            prefix = getPrefix(nodeStack.length);  
        } else {  
            if (isBegin != '/') {  
                prefix = getPrefix(nodeStack.length);  
                if (!isClosed) {  
                    nodeStack.push(name);  
                }  
            } else {  
                nodeStack.pop();  
                prefix = getPrefix(nodeStack.length);  
            }  
  
        }  
        var ret = '\n' + prefix + all;  
        return ret;  
    });  
  
    var prefixSpace = -1;  
    var outputText = output.substring(1);  
    //alert(outputText);  
  
    //把注释还原并解码，调格式  
    outputText = outputText.replace(/\n/g, '\r').replace(  
            /(\s*)<!--(.+?)-->/g,  
            function($0, prefix, text) {  
                //alert(['[',prefix,']=',prefix.length].join(''));  
                if (prefix.charAt(0) == '\r')  
                    prefix = prefix.substring(1);  
                text = unescape(text).replace(/\r/g, '\n');  
                var ret = '\n' + prefix + '<!--'  
                        + text.replace(/^\s*/mg, prefix) + '-->';  
                //alert(ret);  
                return ret;  
            });  
  
    return outputText.replace(/\s+$/g, '').replace(/\r/g, '\r\n');  
  
}    
function getPrefix(prefixIndex) {  
    var span = '    ';  
    var output = [];  
    for ( var i = 0; i < prefixIndex; ++i) {  
        output.push(span);  
    }  
  
    return output.join('');  
} 