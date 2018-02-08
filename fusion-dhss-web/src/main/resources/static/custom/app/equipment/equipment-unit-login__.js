$(function(){
	/**
	 * 进入页面时，先重置所有的选项和GRID。
	 */
	
	$.ajax({
		url:"/equipment-unit-login/get-options",
		type:"get",
		success:function(data){
			console.log(data.test);
		}
	});
});