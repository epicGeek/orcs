<#include "../common/header.ftl">
<#include "../common/user-bar.ftl">
<#include "../common/navigate-bar.ftl">
<#include "../common/tailer.ftl">
<#include "../common/script.ftl">
<!DOCTYPE html>
<html> 
    <!-- 
    Smart developers always View Source. 
    
    This application was built using Adobe Flex, an open source framework
    for building rich Internet applications that get delivered via the
    Flash Player or to desktops via Adobe AIR. 
    
    Learn more about Flex at http://flex.org 
    // -->
    <head>
    <@header></@header>
        <title></title>
        <meta name="google" value="notranslate" />         
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <!-- Include CSS to eliminate any default margins/padding and set the height of the html element and 
             the body element to 100%, because Firefox, or any Gecko based browser, interprets percentage as 
             the percentage of the height of its parent container, which has to be set explicitly.  Fix for
             Firefox 3.6 focus border issues.  Initially, don't display flashContent div so it won't show 
             if JavaScript disabled.
        -->
        <style type="text/css" media="screen"> 
            html, body  { height:100%; }
            body { margin:0; padding:0; overflow:auto; text-align:center; 
                   background-color: #0066cc; }   
            object:focus { outline:none; }
            #flashContent { display:none; }
        </style>
        
        <!-- Enable Browser History by replacing useBrowserHistory tokens with two hyphens -->
        <!-- BEGIN Browser History required section -->
        <!-- <link rel="stylesheet" type="text/css" href="history/history.css" />
        <script type="text/javascript" src="history/history.js"></script> -->
        <!-- END Browser History required section -->  
            
        <script type="text/javascript" src="custom/app/topology/swfobject.js"></script>
        <script type="text/javascript">
            // For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), for no version detection. 
            var swfVersionStr = "10.2.0";
            // To use express install, set to playerProductInstall.swf, otherwise the empty string. 
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {};
		flashvars.MSISDN="8613635228176";
		flashvars.UTYPE="VIP";	// VIP,USER
            var params = {};
            params.quality = "high";
            params.bgcolor = "#0066cc";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            
            params.HSSNAME = "HSS51";
            var attributes = {};
            attributes.id = "nsn_DHSS_Topology_Shanghai";
            attributes.name = "nsn_DHSS_Topology_Shanghai";
            attributes.align = "middle";
            swfobject.embedSWF(
                "swf/nsn_DHSS_Topology_Shanghai.swf", "flashContent", 
                "100%", "100%", 
                
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
            // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
            swfobject.createCSS("#flashContent", "display:block;text-align:left;");
        </script>
        <script type="text/javascript">
		function goWebPage(inneid,innename,innetype,indate) {
			//window.location.href='p_nekpi.html?neid='+inneid+'&nename='+innename+'&netype='+innetype+'&date='+indate;
			window.open('p_nekpi.html?neid='+inneid+'&nename='+innename+'&netype='+innetype+'&date='+indate);
			return "successful";
		}
		function goWebPageGIS(inneid,innename,innetype) {
			window.open('http://10.209.13.15:8666/LteTacSelect.html?tac='+innename);
			return "successful";
		}
	function getUrlParams() { 
		var search = window.location.search; 
		if(!search){
			window.location.href="dhssTopology?HSSNAME=${HSSNAME}";
		}
		
		var tmparray = search.substr(1,search.length).split("&"); 
		var paramsArray = new Array; 
		if(tmparray) { 
			for(var i = 0;i<tmparray.length;i++) { 
				var reg = /[=|^==]/; //
				var set1 = tmparray[i].replace(reg,'&'); 
				var tmpStr2 = set1.split('&'); 
				var array = new Array ; 
				array[tmpStr2[0]] = tmpStr2[1] ; 
				paramsArray.push(array); 
			} 
		}
		return paramsArray ; 
	} 
	function getParamValue(name) { 
		console.log(name);
		var paramsArray = getUrlParams();
		console.log(paramsArray);
		if(paramsArray != null) { 
			for(var i = 0 ; i < paramsArray.length ; i ++ ){ 
				for(var j in paramsArray[i] ) { 
					if( j == name ) { 
						return paramsArray[i][j] ; 
					} 
				} 
			} 
		}else{
			return null ; 
		} 
	}
    </script>
    </head>
    <body>
  		  <!-- 用户信息 -->
		<@user_bar/>
		<!--顶部导航栏-->
		
		<input type="hidden" id = "dhssName" value="${HSSNAME}"/>
		
		
		<div class="n-nav n-nav-bg" style='margin-top: 40px;border-bottom: 1px solid lightblue;margin-bottom:5px;'>
			<div class="container-fluid">
				<div class="row noMarginTop noMarginBottom">
					<div class="col-md-2 col-sm-3 col-xs-10">
						<div class="clearfix">
							<div class="n-navbar-title">
									<a href="welcome" class="btn n-icon n-icon-32 n-icon-color3" role="button">
										<i class="glyphicon glyphicon-menu-left"></i>
									</a>
									&nbsp;<font id="parentMenuName"></font>
							</div>
						</div>
					</div>
					<div class="col-md-offset-1 col-md-9 col-sm-9 col-xs-14" style="float:right; _position:relative;">
						<div id="topNavListWrap" class="n-navbar-nav clearfix" >
							<div id="topNavList">
								<ul id='navList' class="nav navbar-nav">
								</ul>
							</div>
							<button id='overFlowBtn' class="btn btn-info">
								<span class="glyphicon glyphicon-align-justify"></span>
							</button>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--//顶部导航栏-->
		<script>
		$(function() {
		/*当前导航*/
			var pageURI = (window.location.href);
			console.log(pageURI);
			var aa = pageURI.match(/http?:\/\/[^/]+\/([^/]+)/);
			var menuPath = aa[1];
			if(menuPath.indexOf("?")!=-1)
				menuPath = menuPath.substring(0,menuPath.indexOf("?"));
			else
				menuPath = menuPath;
			$.ajax({
				url:"rest/system-menu/search/findByLink/"+menuPath,
				method:"GET",
				dataType : 'json',
				success:function(data){
					var linkTemplate = '<li><a href="{menuCode}">{menuName}</a></li>';
					var menuHtml = "";
					$.each(data,function(index,menuItem){
						
						if(menuItem.menuCode==menuPath){
							$("#parentMenuName").html(menuItem.parentMenuName);
						}
					});
					// var menuLink = linkTemplate.replace("{menuCode}",menuItem.menuCode).replace("{menuName}",menuItem.menuName);
					//menuHtml += menuLink;
					
					
					//$("#navList").html(menuHtml);
					//$("#navList a[href='"+menuPath+"']").addClass("active");
				}
			});
		});
		</script>
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
        <!-- SWFObject's dynamic embed method replaces this alternative HTML content with Flash content when enough 
             JavaScript and Flash plug-in support is available. The div is initially hidden so that it doesn't show
             when JavaScript is disabled.
        -->
        <div id="flashContent">
            <p>
                To view this page ensure that Adobe Flash Player version 
                10.2.0 or greater is installed. 
            </p>
            <script type="text/javascript"> 
                var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://"); 
                document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
                                + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
            </script> 
        </div>
        
        <noscript>
            <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="80%" id="nsn_DHSS_Topology_Shanghai">
                <param name="movie" value="swf/nsn_DHSS_Topology_Shanghai.swf" />
                <param name="quality" value="high" />
                <param name="bgcolor" value="#0066cc" />
                <param name="allowScriptAccess" value="sameDomain" />
                <param name="allowFullScreen" value="true" />
                <!--[if !IE]>-->
                <object type="application/x-shockwave-flash" data="swf/nsn_DHSS_Topology_Shanghai.swf" width="100%" height="80%">
                    <param name="quality" value="high" />
                    <param name="bgcolor" value="#0066cc" />
                    <param name="allowScriptAccess" value="sameDomain" />
                    <param name="allowFullScreen" value="true" />
            </object>
        </noscript>   
        <!-- abc -->  
   		<!--底部-->
			<@tailer></@tailer>
		<!--//底部-->
   <@script></@script>
   <script type="text/javascript" src="custom/app/topology/topology.js"></script>
   </body>
   
</html>