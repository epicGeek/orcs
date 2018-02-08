<#assign basePath=requestContext.contextPath>
<#macro header>  
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="shortcut icon" href="${basePath}/assets/n-images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" href="${basePath}/assets/plugins/bootstrap-3.3.2-dist/css/bootstrap.css" />

		<link rel="stylesheet" href="${basePath}/assets/plugins/kendoui2015.1.318/styles/kendo.common.min.css" />
		<link rel="stylesheet" href="${basePath}/assets/plugins/kendoui2015.1.318/styles/kendo.silver.min.css" />

		<!--easing-->
		<link rel="stylesheet" href="${basePath}/assets/plugins/cplugin/cStyle.css" />

		<link rel="stylesheet" href="${basePath}/assets/css/nsn-home-style.css">

		<title>DHSS 集中运维平台</title>
		
		<script type="text/javascript"  src="${basePath}/assets/plugins/jquery/jquery-1.11.1.min.js"></script>

		<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
		<!--[if lt IE 9]>
		<script type="text/javascript" src="${basePath}/assets/plugins/jquery/html5shiv.min.js"></script>
		<script type="text/javascript" src="${basePath}/assets/plugins/jquery/respond.min.js"></script>
		<![endif]-->
		<!--easing-->
		<script type="text/javascript" src="${basePath}/assets/plugins/cplugin/jquery.easing.1.3.js"></script>
		<script type="text/javascript" src="${basePath}/assets/plugins/cplugin/cjs.js"></script>
</#macro>  