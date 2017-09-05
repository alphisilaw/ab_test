/**
 *  @author liurunbao
 *  @file register.js
 *  @brief register js
 */
$(function(){
	var init = function(){
		$('#register').click(function(){
			var email = $('#email').val().trim();
			var pwd = $('#pwd').val().trim();
			var pwd2 = $('#pwd').val().trim();
			var name = $('#name').val().trim();
			var phone = $('#phone').val().trim();

			if(name == ''){
				$.Dialog.showPopTip('请输入用户名');
				return false;
			}

			if(email == ''){
				$.Dialog.showPopTip('请输入邮箱');
				return false;
			}

			if(!/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/i.test(email)){
				$.Dialog.showPopTip('邮箱格式有误');
				return false;
			}

			if(pwd == ''){
				$.Dialog.showPopTip('请输入密码');
				return false;
			}

			if(pwd2 == ''){
				$.Dialog.showPopTip('请再次输入密码');
				return false;
			}

			if(pwd != pwd2){
				$.Dialog.showPopTip('两次密码不一致');
				return false;
			}

			if(phone == ''){
				$.Dialog.showPopTip('请输入手机');
				return false;
			}

			if(!/^\d+$/.test(phone)){
				$.Dialog.showPopTip('手机格式有误');
				return false;
			}

			var params = {
				userName:name,
				email : email,
				password : pwd,
				phone:phone
			}

			$.postEx(MAIN_URL, '/submitUserRegister', params, function(data){
				 if(data.result == 'success'){
				 	showSuccess(email);
				 }else{
				 	$.Dialog.showPopTip(data.errMsg);
				 }
			});

			return false;
		})
	}

	var showSuccess = function(email){
		var $login_success = $('.login_success');

		$login_success.find('.sub').html('激活邮件已发送到您邮箱<span>'+email+'</span>，请查收并激活');

		$login_success.show();

		$('.s_form').hide();

	}

	init();
})