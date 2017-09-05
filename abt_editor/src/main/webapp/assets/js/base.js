/**
 *  @author liurunbao
 *  @file base.js
 *  @brief base js
 */

$.ajaxSettings.cache = false;

var Class = function(option){ if(option.autoInit){ option.init && option.init(); }; return option; };

$.cookie = function(key,value,options){
	var doc = document;
	// key and value given, set cookie...
	if (arguments.length > 1 && (value === null || typeof value !== "object")) {
		options = options || {};
		if (value === null) {
			value = '';
			options.expires = new Date(0);
		}

		if (typeof options.expires === 'number') {
			var days = options.expires, t = options.expires = new Date();
			t.setDate(t.getDate() + days);
		} else if(options.expires == 'forever') {
			options.expires = new Date(0xfffffffffff);
		}

		return (doc.cookie = [
			encodeURIComponent(key), '=',
			encodeURIComponent(String(value)),
			options.expires ? '; expires=' + options.expires.toGMTString() : '', // use expires attribute, max-age is not supported by IE
			options.path ? '; path=' + options.path : '; path=/',
			// options.domain ? ('; domain=' + options.domain) : ('; domain='+COOKIE_DOMAIN),
			options.secure ? '; secure' : ''
		].join('')); 
	}
	// key and possibly options given, get cookie...
	options = value || {};
	var ret,result, decode = decodeURIComponent;
	
	if(result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(doc.cookie)) {
		try {
			ret = decode(result[1]);
		} catch(e) {
			ret = result[1];
		}
	} else {
		ret = '';
	}
	
	return ret;
};

String.prototype.trim = function(){
	return this.replace(/^[\s　]+/gm,"").replace(/[\s ]+$/gm,"");
};

$.jsonToStr = function(oJson){
	if( oJson == null )
		return "null";
	if( typeof(oJson) == typeof(0) )
		return oJson.toString();
	if( typeof(oJson) == typeof('') ||
		oJson instanceof String )
		{
		oJson = oJson.toString();
		oJson = oJson.replace( /\r\n/, '\\r\\n');
		oJson = oJson.replace( /\n/, '\\n');
		oJson = oJson.replace( /'/, '\'');
		return '"' + oJson + '"';
	}
	if( oJson instanceof Array )
	{
		var strRet = "[";
		for( var i = 0; i < oJson.length; i++)
		{
			if( strRet.length > 1 )
				strRet += ",";
			strRet += arguments.callee(oJson[i]);
		}
		strRet += "]";
		return strRet;
	}
	if( typeof(oJson) == typeof({}) )
	{
		var strRet = "{";
		for( var p in oJson )
		{
			if( strRet.length > 1 )
				strRet += ",";
			strRet += '"'+p.toString() + '":' + arguments.callee(oJson[p]);
		}
		strRet += "}";
		return strRet;
	}

}

$.getUrlParam = function(name){
	if(!arguments.callee.obj){
		var str = location.href,index = str.indexOf('?'),n,arr,obj={};
		if(index!=-1){
			str = str.substring(index+1);
			arr = str.split('#')[0].split('&');
		}else{
			arr=[];
		}
		for(n=0;n<arr.length;++n){
			arr[n] = arr[n].split('=');
			obj[arr[n][0]] = obj[arr[n][0]]= decodeURIComponent(arr[n][1]);
		}
		arguments.callee.obj = obj;
	}

	if(name){
		return arguments.callee.obj[name] || '';
	}else{
		return arguments.callee.obj;
	}
}

$.getEx = function(host,path,params,callback,cache,timeout){
	if(timeout == null){
		timeout = 20000;
	}
	cache = cache || false;
	$.ajax({
		url: host+path,
		type: 'get',
		dataType: 'json',
		data: params,
		timeout: timeout,
		cache:cache
	})
	.done(function(data) {
		callback && callback(data);
	})
	.fail(function(req, msg) {
		var data = {rtn:1, data:{msg:'服务器异常，请稍后再试'}};
		if(msg == 'timeout'){
			data.t = 1;
		}
		callback && callback(data);
	});

};

$.postEx = function(host,path,params,callback,cache,timeout){
	if(timeout == null){
		timeout = 20000;
	}
	cache = cache || false;
	$.ajax({
		url: host+path,
		type: 'post',
		dataType: 'json',
		data: params,
		timeout: timeout,
		cache:cache
	})
	.done(function(data) {
		if(data && data.rtn == 4){
			$.Dialog.alert({
				content:data.data.msg,
				callback:function(){
					location.reload();
				}
			});
		}else{
			callback && callback(data);
		}
	})
	.fail(function(req, msg) {
		var data = {rtn:1, data:{msg:'服务器异常，请稍后再试'}};
		if(msg == 'timeout'){
			data.t = 1;
		}
		callback && callback(data);
	});
};

$.fn.outerHtml = function() {
	return $("<div>").append(this.eq(0).clone()).html()
}

$.html_encode = function(str) {
	var s = "";
	if (str==null || typeof(str)=='undefined' || str.length == 0) return "";
	s = str.replace(/&/g, "&amp;");
	s = s.replace(/</g, "&lt;");
	s = s.replace(/>/g, "&gt;");
	s = s.replace(/ /g, "&nbsp;");
	s = s.replace(/\'/g, "'");
	s = s.replace(/\"/g, "&quot;");
	s = s.replace(/\n/g, "<br>");
	return s;
}

$.html_decode = function(str){
	var s = "";   
	if (str.length == 0) return "";   
	s = str.replace(/&amp;/g, "&");   
	s = s.replace(/&lt;/g, "<");   
	s = s.replace(/&gt;/g, ">");   
	s = s.replace(/&nbsp;/g, " ");   
	s = s.replace(/'/g, "\'");   
	s = s.replace(/&quot;/g, "\"");   
	s = s.replace(/<br>/g, String.fromCharCode(10));   
	return s;
}

$.Dialog = (function(){
 	var $dom;
  	var $popbox;
 	var $overlay;
 	var _callback;

 	var _isIE6 = /MSIE\s+6/gi.test(navigator.userAgent);

 	var init = function(){
	    if(!$overlay){
	        $overlay = $('<div style="display:none;" class="dialog_overlay"></div>');
	        $('body').append($overlay);
	    }
    
 		if($dom){
 			return false;
 		}

 		$dom = $('<div style="display:none;min-width:300px;" class="dialog">\
 						<div class="dialog_title">温馨提示<span class="_zq-closeme"></span></div>\
 						<div class="content"><div class="txt_con"></div></div>\
 						<div class="ctl_wrap">\
 							<a href="#" class="btn ok_btn">确定</a>\
 							<a href="#" class="btn cancel_btn">取消</a>\
 						</div>\
 				  </div>');

 		

 		$('body').append($dom);
 		
 		bindEvents();
 	}

 	var bindEvents = function(){
 		$dom.delegate('.ok_btn', 'click', function(){
 			_callback && _callback(1);
 			hide();
 			return false;

 		}).delegate('.cancel_btn', 'click', function(){
 			_callback && _callback(0);
 			hide();
 			return false;

 		});
 		$(document).on('keyup',function(e){
 			if( parseInt(e.which)===27){
 				hide();
 			}
 			return false;
 		});
 	}

 	var hide = function(){
    	var $box = $popbox || $dom;
 		if($box){
 			$box.fadeOut('fast');
 			$overlay.hide();
 		}
	    if($popbox){
	      $popbox.hide();
	      $popbox = null;
	    }
 	}

  var fixWin = function(){
      var $box = $popbox || $dom;
      
      var $win = $(window);

      var left = ($win.width() - $box.width()) >> 1;
      var top = Math.max(0,($win.height() - $box.height()) >> 1);
      
      if(_isIE6){
          top += $win.scrollTop();
          $box.css({position:'absolute',top:top,left:left});
      }else{
          $box.css({position:'fixed',top:top,left:left});
      }
  }

 	var autoFix = function (){
		 fixWin();
		 $(window).unbind('resize',fixWin).bind('resize',fixWin);
	}

  var clear = function(){
     $dom && $dom.hide();
     $overlay && $overlay.hide();
  }
  
  var dragable=function(){
	  	var DRAGABLE={
	  		mousedown:false,
	  		etargOffsetX:-1,
	  		etargOffsetY:-1
	  	};
		document.onmousedown=function(e){
			   if(e.target==$dom.get(0)){
			      DRAGABLE.mousedown=true;
			      DRAGABLE.etargOffsetX=e.offsetX-$dom.offset().left;
			      DRAGABLE.etargOffsetY=e.offsetY-$dom.offset().top;
			   }
		};
		document.onmousemove=function(e){
			  if(e.target==$dom.get(0)){
			     $dom.offset({top:e.offsetY-DRAGABLE.etargOffsetY,left:e.offsetX-DRAGABLE.etargOffsetX});
			  }
		}
		document.onmouseup=function(e){
			  if(e.target==$dom.get(0)){
			      DRAGABLE.mousedown=false;
			      DRAGABLE.etargOffsetX=e.offsetX-$dom.offset().left;
			      DRAGABLE.etargOffsetY=e.offsetY-$dom.offset().top;
			   }
		}
  }

 	var show = function(id){
      clear();
      if($popbox){
	      $popbox.hide();
	      $popbox = null;
	  }

      if(id){
        init();
        $popbox = $(id);
      }else{
        
      }

      var $box = $popbox || $dom;
      
      if( $('.dialog_title ._zq-closeme',$box).length==0 ){
    	  $('.dialog_title',$box).append('<span class="_zq-closeme"></span>');
      }
      $('.dialog_title ._zq-closeme',$box).on('click',function(){
		  hide();
	  });
      
      autoFix();
   		$box.fadeIn('fast');
   		$overlay.fadeIn('fast');
 	}

 	var alert = function(content, title, callback){
 		init();

	    var ok_txt = '确定';
	    var cancel_txt = '取消';

 		if(typeof content == 'object'){
 			title = content.title;
 			callback = content.callback;
		      ok_txt = content.ok_txt || ok_txt;
		      cancel_txt = content.cancel_txt || cancel_txt;

 			content = content.content;
 		}


	    $dom.find('.ok_btn').html(ok_txt).show();
	    $dom.find('.cancel_btn').html(cancel_txt).hide();

 		$dom.find('.content .txt_con').html(content);
 		$dom.find('.dialog_title').html(title || '温馨提示');
 		_callback = callback;

 		show();
 	}

	  var confirm = function(content, title, callback){
		    init();

		    var ok_txt = '确定';
		    var cancel_txt = '取消';

		    if(typeof content == 'object'){
		      title = content.title;
		      callback = content.callback;
		      ok_txt = content.ok_txt || ok_txt;
		      cancel_txt = content.cancel_txt || cancel_txt;

		      content = content.content;
		      
		    }

		    $dom.find('.ok_btn').html(ok_txt).show();
		    $dom.find('.cancel_btn').html(cancel_txt).show();

		    $dom.find('.dialog_title').html(title || '温馨提示');
		    $dom.find('.content .txt_con').html(content);
		    _callback = callback;

		    show();
	  }

	  var $popTip;
	  var initPopTip = function(){
	    if($popTip){
	       return false;
	    }

	    $popTip = $('<div class="dialog_pop_tip"></div>');
	    $('body').append($popTip);

	  }

	  var pop_timer;
	  var showPopTip = function(tip, time){
	      initPopTip();
	      clearTimeout(pop_timer);

	      $popTip.html(tip);

	      var $win = $(window);

	      var left = ($win.width() - $popTip.outerWidth()) >> 1;
	      var top = 20;//Math.max(0, (($win.height() - $popTip.outerHeight()) >> 1) - 40);
	      $popTip.css({position:'fixed',top:top,left:left});
	      
	      $popTip.fadeIn('fast');

	      pop_timer = setTimeout(function(){
	          $popTip.hide();
	      }, time || 2000);

	  }

	  
	  var conTip = (function(){
	  	  var conTip;
	  	  var init = function(){
	  	  		if(!conTip){
			  	  	 conTip = $('<div class="pop_con_tip" style="display:none;"></div>');
			  	  	 $('body').append(conTip);
			  	  }
	  	  }

	  	  var fixPos = function($dom){
	  	  	  var offset = $dom.offset();
	  	  	  var left = offset.left + $dom.outerWidth()/2;
	  	  	  left = left - conTip.outerWidth()/2;

	  	  	  var top = offset.top - conTip.outerHeight() - 4;

	  	  	  conTip.css('left', left).css('top', top);
	  	  }

	  	  var show = function($dom, tip){
	  	  		init();

	  	  		conTip.html(tip);

	  	  		fixPos($dom);

	  	  		conTip.fadeIn(200);
	  	  }

	  	  var hide = function(){
	  	  		conTip.fadeOut(100);
	  	  }

	  	  return {show:show, hide:hide};

	  })();

	  var $loadingDom;
	  var $loadingLay;
	  var showLoadingTip = function(tip){
		  	if(!$loadingDom){
		  		$loadingDom = $('<div class="loading_tip"><div class="loading"></div><div class="txt"></div></div>');
		  		$loadingLay = $('<div class="loading_tip_lay"></div>');
		  		$('body').append($loadingDom).append($loadingLay);
		  	}

		  	$loadingDom.find('.txt').html(tip || '正在加载...');

		  	$loadingDom.css('margin-left', -$loadingDom.outerWidth()/2);
		  	$loadingDom.css('margin-top', -$loadingDom.outerHeight()/2);

		  	$loadingDom.fadeIn('fast');
		  	$loadingLay.show();
	  }

	  var hideLoadingTip = function(){
	  		if($loadingDom){
	  			$loadingDom.hide();
	  			$loadingLay.hide();
	  		}
	  }

 	return { show:show, hide:hide, alert:alert, conTip:conTip, autoFix:autoFix, showPopTip:showPopTip, confirm:confirm, loader:{ show:showLoadingTip, hide:hideLoadingTip }}

})();

$.fn.setSelect = function(a,b){
	var inp = $(this).get(0);

	$(document).focus();
	inp.focus();
	inp.setSelectionRange(a,b);
	inp.focus();
}

var Dialog = function(option){
	var $dom;

	this.init = function(){
		if($dom){
			return false;
		}

		$dom = $(option.html);
		this.dom = $dom;
		option.onInit && option.onInit(this);
		this.bindEvent();
		$('body').append($dom);
	}

	this.bindEvent = function(){
		var self = this;
		$dom.delegate('.cancel_btn, .dialog_cls','click',function(){
			self.hide();
			option.onClose && option.onClose(self);
			return false;
		});
	}

	this.show = function(info){
		this.init();
		this.info = info;
		option.onShow && option.onShow(this);
		$.Dialog.show($dom);
	}

	this.hide = function(){
		$.Dialog.hide();
	}

	return this;
}

$.TypeHelper = new Class({
	autoInit:true,
	buizTypeList:[{id:1, name:'自助'}],
	kpiGoalTypes:[],
	normalGoalTypes:[],
	init:function(cb){
		var self = this;
		if(self.isInit){
			cb && cb();
			return false;
		}
		self.isInit = true;
		self.buizMap = {};
		var list = self.buizTypeList;
		for(var i = 0; i < list.length; i++){
			self.buizMap[list[i].id] = list[i];
		}

		$.getEx(MAIN_URL, '/getEditPageInitAttrs', {}, function(data){
			if(data.result == 'success'){
				self.normalGoalTypes = data.normalGoalTypes;
				self.kpiGoalTypes = data.kpiGoalTypes;

				self.normalGoalTypesMap = self.arrayToMap(self.normalGoalTypes, 'key');
				self.kpiGoalTypesMap = self.arrayToMap(self.kpiGoalTypes, 'key');

			}
		});

	},
	arrayToMap:function(arr, key){
		var map = {};
		for(var i = 0, item; i < arr.length; i++){
			item = arr[i];
			map[item[key]] = item;
		}
		return map;
	},
	getBuizTypes:function(id){
		if(id){
			return this.buizMap[id];
		}else{
			return this.buizTypeList;
		}
	},
	getKpiGoalTypes:function(id){
		if(id){
			return this.kpiGoalTypesMap[id];
		}else{
			return this.kpiGoalTypes;
		}
	},
	getNormalGoalTypes:function(id){
		if(id){
			return this.normalGoalTypesMap[id];
		}else{
			return this.normalGoalTypes;
		}
	}
});


$.Login = new Class({
	isLogin:function(){
		return $.cookie('loginName') && $.cookie('loginUid');
	},
	getUserName:function(){
		return $.cookie('loginName');
	},
	check:function(){
		if(this.isLogin()){
			$('#top_user_info').html('<a href="'+MAIN_URL+'/v/experiment">'+this.getUserName()+'</a><a href="#" id="logout">退出</a>');
		}
	}
});

$.IsURL = function (url){
    return /^(https|http|ftp|rtsp|mms):\/\/(.)+$/.test(url) && /\.(.*)[^\.]$/.test(url);
}

$(function(){
	$('#top_user_info').delegate('#logout', 'click', function(){
		$.cookie('loginUid', null);
		$.cookie('loginName', null);
		location.href = EDITOR_URL;
	});

	$.Login.check();

})
$.ZqUI=(function(){
	var select=function($selector){
		if( $selector.data('_ZqUI') ){
			return false;
		}
		var $wrapper=$('<div></div>').addClass('_zqui-select-wrapper');
		var $con=$('<div></div>').addClass('_zqui-select').appendTo($wrapper);
		var $selectedcon=$('<div></div>').addClass('_zqui-selected-wrapper').appendTo($con);
		var $selected = $('<div></div>').addClass('_zqui-selected').appendTo($selectedcon);
		$selectedcon.on('click',function(){
			if( $listcon.data('status')==='hide' ){
				$listcon.show().trigger('show');
			}else{
				$listcon.hide().trigger('hide');
			}
		});
		
		var $listcon = $('<ul></ul>').addClass('_zqui-select-list').appendTo($con).hide().data('status','hide');
		
		$listcon.on('hide',function(){$(this).data('status','hide')}).on('show',function(){$(this).data('status','show')});
		
		$listcon.delegate('._zqui-select-option','click',function(){
			//$('option[value='+$(this).attr('_value')+']',$selector).click();
			$('._zqui-select-option',$listcon).removeClass('_zqui-option-selected');
			$(this).addClass('_zqui-option-selected');
			$selected.html($(this).html());
			$listcon.hide().trigger('hide');;
			
			$selector.val($(this).attr('_value'));
			$selector.change();
			
		});
		$('option',$selector).each(function(){
			var $this = $(this);
			var $option=$('<li></li>').addClass('_zqui-select-option').attr({
				_value:$this.attr('value'),
			})
			.html($this.html())
			.appendTo($listcon);
			if( $this.is(':selected') ){
				$option.addClass('_zqui-option-selected');
				$selected.html($this.html());
			}
		});
		$wrapper.insertBefore($selector);
		$selector.hide().data('_ZqUI',1);
	};
	var checkbox = function($checkbox){
		if( $checkbox.data('_ZqUI') ){
			return false;
		}
		$checkbox.hide();
		var $wrapper=$('<div></div>').addClass('_zqui-checkbox-wrapper');
		var $checkboxUI=$('<div></div>').addClass('_zqui-checkbox').html("√").appendTo($wrapper);
		$checkboxUI.data('checked',$checkbox.is(':checked'));
		$checkboxUI.data('checked') && $checkboxUI.addClass('_zqui-checked');
		$checkboxUI.click(function(){
			$checkboxUI.data('checked',!$checkboxUI.data('checked'));
			$checkboxUI.toggleClass('_zqui-checked');
			$checkbox.click();
		});
		$wrapper.insertBefore($checkbox);
		$checkbox.data('_ZqUI',1);
	};
	var radios =function($radios){
		if( $radios.data('_ZqUI') ){
			return false;
		}
		var $wrapper=$('<div></div>').addClass('_zqui-radios-wrapper');
		var $con=$('<div></div>').addClass('_zqui-radios').appendTo($wrapper);
		
		var $listcon = $('<ul></ul>').addClass('_zqui-radio-list clrfix').appendTo($con);
		
		$listcon.delegate('._zqui-radio-option','click',function(){
			$(':radio:checked[value!="'+$(this).attr('_value')+'"]',$radios).removeAttr('checked');
			$(':radio[value="'+$(this).attr('_value')+'"]',$radios).click();
			$('._zqui-radio-option',$listcon).removeClass('_zqui-option-rochecked');
			$(this).addClass('_zqui-option-rochecked');
		});
		$(':radio',$radios).each(function(){
			var $this = $(this);
			var $option=$('<li></li>').addClass('_zqui-radio-option').attr({
				_value:$this.attr('value'),
			})
			.html($this.attr('label'))
			.appendTo($listcon);
			if( $this.is(':checked') ){
				$option.addClass('_zqui-option-rochecked');
			}
		});
		$wrapper.appendTo($radios);
		$radios.data('_ZqUI',1);
		$(':radio',$radios).hide();
		
	};
	return {
		select:select,
		checkbox:checkbox,
		radios:radios
	};
})();

/*
 * Make the dialog draggable in a simple way
 * ----------------------------------------------------
 * 
 * Dialog HTML Format:
 * --------------------------------------
 * <div>
 * 		<div class='dialog_title'></div>
 * 		<div></div>
 * </div>
 * --------------------------------------
 * 
 * Browser compatibility: chrome
 * 
 */
function _zqDragable(dom){
		var DRAGABLE={
	  		mousedown:false,
	  		etargOffsetX:-1,
	  		etargOffsetY:-1
	  	};
		document.onmousedown=function(e){
			   if(e.target.parentNode==dom && e.target.classList.contains('dialog_title')){
				  e.target.style.cursor='move';
			      DRAGABLE.mousedown=true;
			      DRAGABLE.etargOffsetX=e.screenX-dom.offsetLeft;
			      DRAGABLE.etargOffsetY=e.screenY-dom.offsetTop;
			      e.preventDefault();
			   }
		};
		document.onmousemove=function(e){
			  if(e.target.parentNode==dom && e.target.classList.contains('dialog_title') && DRAGABLE.mousedown){
				  dom.style.left=(e.screenX-DRAGABLE.etargOffsetX).toString()+'px';
				  dom.style.top=(e.screenY-DRAGABLE.etargOffsetY).toString()+'px';
			  }
		}
		document.onmouseup=function(e){
			DRAGABLE.mousedown=false;
		}
}
