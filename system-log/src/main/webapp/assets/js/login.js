$(function() {
	$('#pwInput').bind('keypress', function(event) {
		if (event.keyCode == "13") {
			checkLogin();
		}
	});

	$("#loginBtn").click(function() {
		checkLogin();
	});
	function checkLogin() {
		var account = $("#accountInput").val();
		var password = $("#pwInput").val();
		if (account.length <= 0) {
			$("#error").html("请输入登录名！")
		} else if (password.length <= 0) {
			checkPwd(account,0);
		} else {
			if('admin'==account){
				$("form").submit();
			}else{
				checkPwd(account,1);
			}
			 
		}
	}

});

/**
 * 验证是否第一次登陆
 * @param userName
 * @param type
 */
function checkPwd(userName,type){
	$.ajax({
	    type : "GET",
	    dataType : 'json',
		url : "checkPwd",
		data:{
			"userName":userName
		},
		success : function(data) {
			if('no'==data){
				if('0'==type){
					$("#error").html("请输入密码！");
				}else{
					$("form").submit();
				}
			}else{
				window.location.href="changePassword?userName="+userName;
			}
		},
		error:function(e){
			if(e.status=='200' && e.responseText=='yes'){
				window.location.href="resetPwd?userName="+userName;
			}else{
				if('0'==type){
					$("#error").html("请输入密码！");
				}else{
					$("form").submit();
				}
			}
		}
	});
}