function RegexName(obj,obj1,obj2,str) 
{  
	obj1.text("");
	obj2.text("");
	var regex = /^\w+$/;
	if(!regex.test(str.trim())){
		obj.text("密码必须是字母、数字或下划线的组合！");
		return false;
	}
	return true;
}  
$(function() {
	$("#saveBtn").on("click",function(){
		
	   var oldPwd = $("#oldPwd").val();
	   var newPwd1= $("#newPwd1").val();
	   var newPwd2= $("#newPwd2").val();
	   if(oldPwd.trim() == null || oldPwd.trim() == ''){
		   $("#newPwdLabel1").text("");
		   $("#newPwdLabel2").text("");
		   $("#oldPwdLabel").text("旧密码不能为空！");
		   return false;
	   }
	   if(!RegexName($("#oldPwdLabel"),$("#newPwdLabel1"),$("#newPwdLabel2"),oldPwd)){
		   return false;
	   };
	   if(newPwd1.trim() == null || newPwd1.trim() == ''){
		   $("#oldPwdLabel").text("");
		   $("#newPwdLabel2").text("");
		   $("#newPwdLabel1").text("新密码不能为空！");
		   return false;
	   }
	   if(!RegexName($("#newPwdLabel1"), $("#oldPwdLabel"),$("#newPwdLabel1"),newPwd1)){
		   return false;
	   };
	   if(newPwd2.trim() == null || newPwd2.trim() == ''){
		   $("#oldPwdLabel").text("");
		   $("#newPwdLabel1").text("");
		   $("#newPwdLabel2").text("确认密码不能为空！");
		   return false;
	   }
	   if(!RegexName($("#newPwdLabel2"),$("#oldPwdLabel"),$("#newPwdLabel1"),newPwd2)){
		   return false;
	   };
	   if(newPwd1 != newPwd2){
		   $("#oldPwdLabel").text("");
		   $("#newPwdLabel1").text("");
		   $("#newPwdLabel2").text("确认密码与新密码不符！");
		   return false;
	   }
	   
	   $("#oldPwdLabel").text("");
	   $("#newPwdLabel1").text("");
	   $("#newPwdLabel2").text("");
	   var param = [];
	   param.passwordNew = newPwd1;
	   param.passwordOld = oldPwd;
	   //更新密码
	   changePassword(param);
			
		 
	}); 
    //重置
	$('#resetBtn').click(function(){		
		$('#oldPwd').val("");
		$('#newPwd1').val("");
		$('#newPwd2').val("");
		$("#oldPwdLabel").text("");
		$("#newPwdLabel1").text("");
		$("#newPwdLabel2").text("");
	});
	
	function changePassword(param){
		$.ajax({
		    url:"platform/changeOwnerPassword",
			//url:"my-profile/change-password",
			type:"POST",
			//dataType:"json",
			//contentType:"application/json;charset=utf-8",
			data:{
				"passwordNew":param.passwordNew,
				"passwordOld":param.passwordOld
			},
			success:function(data){
				if(data != ''){
					infoTip({content:data ,color:"#f60a0a"});
				}else{
					infoTip({content: "修改密码成功！正在为您跳转到登陆页...",color:"#088703"});
					setTimeout(function(){window.location="logout"},2000);
				}
				 
			},
			fail:function(error){
				infoTip({content: "修改密码失败！"+error,color:"#f60a0a"});
			}
		});
	}
});	
