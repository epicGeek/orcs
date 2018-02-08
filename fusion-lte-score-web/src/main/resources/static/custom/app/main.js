
$(function($){
	
	/**
	 * 登录时，加载当前用户地区权限
	 */
	$.get("city/search");
	
	/**
	 * 根据当前登录用户获取菜单权限
	 */
	$.ajax({
		url : "platform/authority/search",
		type : "get",
		success : function(data) {
			var keyMenu = [];
			$.each(data,function(key,menu){
				$.each(menu,function(index,item){
					if($.inArray(item.menuCode,keyMenu)!=-1){
						return true;
					}else{
						keyMenu.push(item.menuCode);
						$("#"+item.menuCode).attr("href",item.menuCode);
					}
					
				});
			});
		}
	});
	
});