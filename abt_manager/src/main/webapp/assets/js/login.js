/**
 *  @author liurunbao
 *  @file login.js
 *  @brief login js
 */
$(function () {
    var show_msg = function (msg) {
        $(".err-msg").text(msg);
        $(".login-tx .email").addClass("err-bor");
        $(".err-msg").show();
    }
    var init = function () {
        if (window.tip && tip != 'null') {
//			$.Dialog.showPopTip($.html_encode(tip), 4000);
        }

        $('#pwd').keydown(function (evt) {
            if (evt.keyCode == 13) {
                $('#login').click();
            }
        });

        $('#login').click(function () {
            var email = $('#email').val().trim();
            var pwd = $('#pwd').val().trim();

            if (email == '') {
//				$.Dialog.showPopTip('请输入邮箱');
                show_msg('请输入邮箱');
                return false;
            }

            if (!/\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/i.test(email)) {
                show_msg('邮箱格式有误');
//				$.Dialog.showPopTip('邮箱格式有误');
                return false;
            }

            if (pwd == '') {
                show_msg('请输入密码');
//				$.Dialog.showPopTip('请输入密码');
                return false;
            }

            var params = {
                email: email,
                password: pwd
            }

            $.postEx(MAIN_URL, '/doLogin', params, function (data) {
                if (data.result == "success") {
//					$.Dialog.showPopTip('登录成功');
                    setTimeout(function () {
                        location.href = CHANNEL_URL;
                    }, 300);
                } else {
                    show_msg(data.errMsg);
//					$.Dialog.showPopTip(data.errMsg);
                }
            });

            return false;
        })
    }

    init();

})