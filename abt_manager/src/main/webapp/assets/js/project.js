/**
 *  @author liurunbao
 *  @file project.js
 *  @brief project js
 */
 $(function(){

 	$.Login.needLogin();
 	
 	var Page = new Class({
 		autoInit:true,
 		currentTab:'',
 		projectId:'',
 		caseId:$('#ld_lab_list').val(),
 		versionColors:['#FEA525','#5EBF70','#59BBEA','#FEA525','#5EBF70','#59BBEA'],
 		init:function(){
 			$.Dialog.loader.show();
 			self=this;
 			self.currentTab=location.hash;
 			self.initEvents();
 		},
 		initEvents:function(){
 			$('#tabs_nav').delegate('.item','click',function(e){
 				location.hash=$(this).attr('t');
 				self.currentTab=location.hash.substr(1);
 			});
 		},
 		dataTransfer:{
 			
 		}
 	});

 	var Project = new Class({

 		projData:{},

 		curProj:null,

 		init:function(){
 			this.initProj();
 		},

 		initProj:function(){
 			var self = this;
 			//展开项目列表
 			$('.proj_info .more').click(function(){
 				$('.proj_list_wrap').show();
 				setTimeout(function(){
 					$('.proj_list_wrap').addClass('show');
 				}, 1);
 				return false;
 			});

 			$('.proj_info .name').click(function(){
 				AddProj.show(self.curProj);
 				return false;
 			});
 			
 			
 			var hideProjList = function(){
 				$('.proj_list_wrap').removeClass('show');
 			}

 			//项目列表事件
 			$('#_zq-leftcol').delegate('.bot .btn', 'click', function(){
 				var t = $(this).attr('t');
 				if(t=='add'){
 					AddProj.show();
 				}else{
 					location.href = MAIN_URL +'/v/project_list';
 				}
 				
 				return false;
 			});
 			$('.proj_list_wrap').delegate('.cls, .bg', 'click', function(){
 				//hideProjList();
 				return false;
 			}).delegate('.plist .item', 'click', function(){
 				var $this = $(this);
 				var pid = $this.attr('pid');
 				Page.projectId=pid;
 				if(!self.curProj){ //直接URL打开
 					self.curProj = self.projData[pid];
 					var headTitle=$('head title').html();
 					$('head title').html( headTitle.replace('**',self.curProj.projectName));

	 				$('.proj_list_wrap').find('.plist .item').removeClass('on');

	 				$this.addClass('on');

	 				hideProjList();

	 				$('<a></a>').attr('href',MAIN_URL+'/v/experiment?pid='+pid)
	 					.appendTo($('#_zq-top_proj_name').empty()).text(self.curProj.projectName);
	 				$('#_zq-top_proj_dscrp').text(self.curProj.description);
	 				$('#_zq-top_proj_crtime').text('创建于'+new Date(self.curProj.createTime).toLocaleString());
	 				
	 				$.postEx(MAIN_URL, '/isEmptyProject', {pid:pid}, function(data){
	 					if( data.toString()=="1" ){
	 						$('#_zq-proj-ctrl :button[t=delproject]').show();
	 					}
	 				});
	 				
	 				var _tab=$('#tabs_nav').find('.item[t='+Page.currentTab+']');
	 				if( ! _tab.length ){
	 					_tab=$('#tabs_nav').find('.item.on');
	 				}
	 				_tab.click();
	 				
	 				$('#_zq-proj-ctrl :button[t=edit]').on('click',function(){
	 					AddProj.show(self.curProj);
	 	 			});
	 				$('#_zq-proj-ctrl :button[t=proj-code]').on('click',function(){
	 					$.Dialog.dialog(
	 							 '<p style="text-align:left;font-size:0.8em;margin-bottom:1em;">请将以下代码拷贝到您网页的&lt;head&gt;&lt;/head&gt;之间或紧挨&lt;body&gt;之后</p>'
	 							+'<textarea readonly="readonly" id="proj_code-2" class="inp" style="width:700px;height:40px;">'
	 								+'<script src="'+DATA_HOST+'/abtest/code/'+self.curProj.projectId+'/stat.js"></script>'
	 							+'</textarea>'
	 							+'<input type="button" value="复制" id="_zq-btn-copy-proj_code-2" class="_zq-docopy">'
	 							+'<div class="_zq-copy-success-tip-2" style="display:none;">内容已经复制到剪贴板</div>'
	 							+'<p class="orange" style="text-align:left;font-size:0.8em;">注：控制版本及URL分离的版本需要嵌入代码，在线编辑的版本不需要嵌入代码</p>', 
	 							'项目代码');
	 					$('#_zq-btn-copy-proj_code-2').data('zclipId') || $('#_zq-btn-copy-proj_code-2').zclip({
	 						path:MAIN_URL+'/assets/js/ZeroClipboard.swf',
	 						copy:function(){return $('#proj_code-2').val();},
	 						afterCopy:function(){
	 							$('._zq-copy-success-tip-2').show();
	 							setTimeout(function(){$('._zq-copy-success-tip-2').hide(1000);}, 2000)
	 						}
	 					});
	 				});
	 				$('#_zq-proj-ctrl :button[t=delproject]').on('click',function(){
	 					$.postEx(MAIN_URL, '/doDeleteProject', {pid:pid}, function(data){
	 						if(data.result == 'success'){
		 						$.Dialog.showPopTip('删除项目成功');
		 						location.href = MAIN_URL+'/v/experiment';
		 					}else{
		 						$.Dialog.showPopTip(data.errMsg);
		 					}
	 					});
	 				})
	 				if( ! $.getUrlParam('cid') ){
	 					LabList.init();
	 				}
 				}else{ //从当前Project转移到另一个或同一个Project
 					location.href = MAIN_URL+'/v/experiment?pid='+pid;
 				}

 				return false;
 			});
 			//导航事件
 			$('#tabs_nav').delegate('.item', 'click', function(){
 				var $this = $(this);
 				var t = $this.attr('t');

 				self.showTab(t);

 				$('#tabs_nav').find('.item').removeClass('on');

 				$this.addClass('on');

 				return false;
 			});


 			this.getProjList();

 		},

 		getProjList:function(){
 			var self = this;

 			var params = {
 				pageSize:20,
 				pageNum:1
 			}

 			var tab = location.hash;
 			tab = tab.replace(/^#/g, '');

 			$('#tabs_nav').find('.item').removeClass('on');
 			
 			var _tab=$('#tabs_nav').find('.item[t='+tab+']');
 			if( _tab.length ){
 				_tab.addClass('on').click();
 			}else{
 				$('#tabs_nav').find('.item').eq(0).addClass('on').click();
 			}

 			$.getEx(MAIN_URL, '/doSearchProjectsByUid', params, function(data){
 				 if(data.result == 'success'){
 				 	var list = data.data;
 				 	if(list && list.length > 0){
 				 		var item;
 				 		var html = [];
 				 		for(var i = 0;  i < list.length; i++){
 				 			item = list[i];
 				 			self.projData[item.projectId] = item;
 				 			html.push('<div class="item clrfix" pid="'+item.projectId+'">\
											<div class="name">'+item.projectName+'</div>\
											<!--<div class="sub">'+item.description+'</div>-->\
										</div>');
 				 		}
 				 		$('.proj_list .plist').html(html.join(''));
 				 	}
			 		$('#_zq-fullpagemask').hide();
			 		$.Dialog.loader.hide();

			 		var pid = $.getUrlParam('pid');
			 		if(pid){
			 			$('.proj_list .plist').find('.item[pid="'+pid+'"]').eq(0).click();
			 		}else{
			 			$('.proj_list .plist').find('.item').eq(0).click();
			 		}

 				 	self.isInit = true;

 				 	$('.page_loading').hide();
 				 }
 			})
 		},

 		showTab:function(type){
 			location.hash = type;
 			switch(type){
				case 'valid':
					ValidExperiment.show();
					break;
				case 'archive':
					ArchiveExperiment.show();
					break;
				default:
					break;
			}
 		}

 	});
 	
 	var ValidExperiment=(function(){
 		return {
 			show:function(){
 				$('._zq-tab_main .tab').hide();
 				$('#_zq-expri-valid').show();
 			}
 		};
 	})();
 	var ArchiveExperiment=(function(){
 		return {
	 		show:function(){
	 			$('._zq-tab_main .tab').hide();
 				$('#_zq-expri-archive').show();
	 		}
 		};
 	})();

 	var AddProj = new Dialog({
 		html:'<div style="display:none;min-width:300px;" class="dialog">\
 						<div class="dialog_title">添加项目</div>\
 						<div class="content">\
 							<div class="s_form">\
 								<div class="item">\
 									<div class="l">项目名称：</div>\
 									<div class="r"><input id="name" type="text" style="width:250px;" /></div>\
 								</div>\
 								<div class="item">\
 									<div class="l">项目描述：</div>\
 									<div class="r"><textarea id="desc" style="height:60px; width:250px;" name="" id="" cols="30" rows="10"></textarea></div>\
 								</div>\
 							</div>\
 						</div>\
 						<div class="ctl_wrap">\
 							<a href="#" class="btn ok_btn">确定</a>\
 							<a href="#" class="btn cancel_btn">取消</a>\
 						</div>\
 				  </div>',

 		onInit:function(dialog){
 			var $dom = dialog.dom;

 			$dom.delegate('.ok_btn', 'click', function(){
 				var name = $dom.find('#name').val().trim();
 				var desc = $dom.find('#desc').val().trim();

 				if(name == ''){
 					$.Dialog.showPopTip('请输入项目名称');
 					return false;
 				}else if( name.length>10 || name.length<4 ){
 					$.Dialog.showPopTip('项目名的长度应该在4~10个字之间');
 					return false;
 				}

 				if(desc == ''){
 					$.Dialog.showPopTip('请输入项目描述');
 					return false;
 				}

 				var params = {
 					pName:name,
 					pDesc:desc
 				}

 				if(dialog.info){
 					params.pid = dialog.info.projectId;
 					
 					$.postEx(MAIN_URL, '/doEditProjectsByProjectId', params, function(data){
	 					if(data.result == 'success'){
	 						dialog.hide();
	 						$.Dialog.showPopTip('修改项目成功');
	 						Project.getProjList();
	 					}else{
	 						$.Dialog.showPopTip(data.errMsg);
	 					}
	 				})
 				}else{
 					$.postEx(MAIN_URL, '/doAddProjectsByUid', params, function(data){
	 					if(data.result == 'success'){
	 						dialog.hide();
	 						$.Dialog.showPopTip('添加项目成功');
	 						Project.getProjList();
	 					}else{
	 						$.Dialog.showPopTip(data.errMsg);
	 					}
	 				})
 				}
 				return false;
 			});
 		},

 		onShow:function(dialog){
 			var $dom = dialog.dom;

 			var info = dialog.info;

 			if(info){
 				$dom.find('#desc').val(info.description);
 				$dom.find('#name').val(info.projectName);

 				$dom.find('.dialog_title').html('修改项目');
 				$dom.find('.ok_btn').html('修改');

 			}else{
 				$dom.find('#desc').val('');
 				$dom.find('#name').val('').focus();

 				$dom.find('.dialog_title').html('添加项目');
 				$dom.find('.ok_btn').html('添加');

 			}

 		}

 	});

	var AddLab = new Dialog({
		html:'<div style="display:none;min-width:300px;" class="dialog">\
 						<div class="dialog_title">添加实验</div>\
 						<div class="content">\
 							<div class="s_form">\
 								<div class="item">\
 									<div class="l">实验名称：</div>\
 									<div class="r"><input id="name" type="text" style="width:350px;" /></div>\
 								</div>\
 								<div class="item">\
 									<div class="l">实验网址：</div>\
 									<div class="r"><input id="link" type="text" style="width:350px;"/></div>\
 								</div>\
 								<div class="item">\
 									<div class="l">所属业务：</div>\
 									<div class="r"><select name="" id="buizType"></select></div>\
 								</div>\
								<div class="item">\
									<div class="l">移动版：</div>\
						            <div class="r"><select name="" id="isMobile"><option value="0">否</option><option value="1">是</option></select></div>\
								</div>\
 								<div class="item" style="display:none;">\
 									<div class="l">&nbsp;</div>\
 									<div class="r"><input type="checkbox" id="add_lab_chk" class="chk" /><label for="add_lab_chk">添加后打开编辑器</label></div>\
 								</div>\
 							</div>\
 						</div>\
 						<div class="ctl_wrap">\
 							<a href="#" class="btn ok_btn">确定</a>\
 							<a href="#" class="btn cancel_btn">取消</a>\
 						</div>\
 				  </div>',
 		onInit:function(dialog){
 			var $dom = dialog.dom;

 			var buizList = $.TypeHelper.getBuizTypes();
 			var html = [], item;
 			for(var i = 0; i < buizList.length; i++){
 				item = buizList[i];
 				html.push('<option value="'+item.id+'">'+item.name+'</option>');
 			}
 			$dom.find('#buizType').html(html.join(''));

 			$dom.delegate('.ok_btn', 'click', function(){
 				var name = $dom.find('#name').val().trim();
 				var link = $dom.find('#link').val().trim();

 				var buizType = $dom.find('#buizType').val();
 				var isMobile = $dom.find('#isMobile').val();

 				if(!$.IsURL(link)){
 					$.Dialog.showPopTip('请输入正确的网址');
 				}

 				var params = {
 					projectId   :  Project.curProj.projectId,
 					cName		:  name,
 					cUrl		:  link,
 					buizType    :  buizType,
 					isMobile    :  isMobile
 				}
 				$.Dialog.loader.show("正在创建实验，请稍候~");
 				$.postEx(MAIN_URL, '/doAddCase', params, function(data){
 					if(data.result == 'success'){
 						dialog.hide();
 						$.Dialog.showPopTip('添加成功');
 						LabList.getList();
 						$('#_zq-proj-ctrl :button[t=delproject]').hide();
 					}else{
 						$.Dialog.showPopTip(data.errMsg);
 					}
 				});
 				$.Dialog.loader.hide();

 				return false;
 			})

 		},
 		onShow:function(dialog){
 			var $dom = dialog.dom;

 			$dom.find('#name').val('');

 			$dom.find('#link').focus().val('http://');

 			$dom.find('#add_lab_chk').attr('checked', true);

 		}
	})
	
	var LabList = new Class({
		dom:$('#lab_list'),
		listDom:$('._zq-expri-table tbody'),
		noneDom:$('#lab_list_none'),
		dataInfo:{},
		init:function(){
			if(this.isInit){
				return false;
			}

			this.isInit = true;

			this.initEvents();
			
			this.getList(null,{expriType:'valid'});
			this.getList(null,{expriType:'archive'});

		},

		initEvents:function(){
			var self = this;

			self.noneDom.delegate('.add', 'click', function(){

				AddLab.show();

				return false;
			});

			$('#add_lab').click(function(){
				AddLab.show();
				return false;
			});
			$('#query_lab').click(function(){
				self.getList();
			});
			$('th._zq-field-status').on('click',function(){
				self.getList(null, {orderBy:'status',expriType:$(this).eq(0).parents('.tab').attr('t')});
			});
			$('th._zq-field-crtime').on('click',function(){
				self.getList(null, {orderBy:'crtime',expriType:$(this).eq(0).parents('.tab').attr('t')});
			});
			
			self.listDom.delegate('.ctl', 'click', function(){
				var $this = $(this);

				var t = $this.attr('t');

				var data = self.dataInfo[$this.parent().attr('cid')];

				switch(t){
					case 'edit':
						var proj = Project.curProj;
						var params = {
							caseId    : data.caseId,
							isMobile  : data.isMobile,
							pName     : proj.projectName
						}
						window.open($.handleUrl(EDITOR_URL, params));
						break;
					case 'preview':
						var proj = Project.curProj;
						LabPreview.show({
							caseId    : data.caseId,
							projectId : proj.projectId
						})
						break;
					case 'del':
						$.Dialog.confirm({
							content:'注意，归档后的实验无法再次运行，确认要归档实验“'+data.caseName+'”?',
							callback:function(flag){
								if(flag){
									var params = {
										caseId:data.caseId
									}
									$.postEx(MAIN_URL, '/doDeleteCase', params, function(data){

										if(data.result == 'success'){
											$.Dialog.showPopTip('归档成功');
											self.getList(null, {expriType:'archive'});
											self.getList(null, {expriType:'valid'});
										}else{
											$.Dialog.showPopTip(data.errMsg);
										}

									})
								}
							}
						});
						break;
					case 'realdel':
						$.Dialog.confirm({
							content:'注意，删除后的实验将不能恢复和查看，确认要删除实验“'+data.caseName+'”?',
							callback:function(flag){
								if(flag){
									var params = {
										caseId:data.caseId,
										real:true
									}
									$.postEx(MAIN_URL, '/doDeleteCase', params, function(data){

										if(data.result == 'success'){
											$.Dialog.showPopTip('删除成功');
											self.getList(null, {expriType:'valid'});
											$.postEx(MAIN_URL, '/isEmptyProject', {pid:Page.projectId}, function(data){
							 					if( data.toString()=="1" ){
							 						$('#_zq-proj-ctrl :button[t=delproject]').show();
							 					}
							 				});
										}else{
											$.Dialog.showPopTip(data.errMsg);
										}
									})
								}
							}
						});
						break;
					case 'data':
						document.location = MAIN_URL+'/v/exprmdata-simple?pid='+data.projectId+'&cid='+data.caseId;
						break;
					case 'copy':
						$.Dialog.confirm({
							content : '确认要拷贝实验“'+data.caseName+'”?',
							callback : function(flag){
								if(flag){
									var params = {
										caseId:data.caseId
									}
									$.Dialog.loader.show("正在拷贝实验，请稍候~");
									$.postEx(MAIN_URL,'/doCopyCase', params, function(data){
										$.Dialog.loader.hide();
										if(data.result == 'success'){
											$.Dialog.showPopTip('拷贝实验成功');
											self.getList();
										}else{
											$.Dialog.showPopTip(data.errMsg);
										}
									})
								}
							}
						})
						break;
				}

				return false;
			}).delegate('.start .btn, ._zq-pause-ctrl', 'click', function(){
				var $this = $(this);
				if($this.hasClass('doing')){
					return false;
				}

				var caseInfo = self.dataInfo[$this.parent().attr('cid')];

				$.Dialog.confirm({
					content:'确认要暂停实验“'+caseInfo.caseName+'”？',
					callback:function(flag){
						if(flag){
							var params ={
								projectId : caseInfo.projectId,
								caseId : caseInfo.caseId
							}

							$this.addClass('doing');
							$.postEx(MAIN_URL, '/stopCase', params, function(data){
								if(data.result == 'success'){
									$.Dialog.showPopTip('暂停试验成功');
									caseInfo.caseStatus = 1;
									$this.parents('.stat').replaceWith(self.getLabStatus(caseInfo));
								}else{
									$.Dialog.showPopTip(data.errMsg);
								}
								$this.removeClass('doing');
							})
						}
					}
				})

				return false;
			}).delegate('.pause .btn, ._zq-start-ctrl', 'click', function(){
				var $this = $(this);
				if($this.hasClass('doing')){
					return false;
				}
				var caseInfo = self.dataInfo[$this.parent().attr('cid')];
				var runCase=function(){
					
					var params ={
						projectId : caseInfo.projectId,
						caseId : caseInfo.caseId
					}
					
					var $ctl3=$this.parents('td._zq-field-status').parent().find('td.ctls .ctl.ctl3');
					$this.addClass('doing');
					$.postEx(MAIN_URL, '/runCase', params, function(data){
						if(data.result == 'success'){
							$.Dialog.showPopTip('启动试验成功');
							caseInfo.caseStatus = 2;
							$this.parents('.stat').replaceWith(self.getLabStatus(caseInfo));
							$ctl3.replaceWith('<a href="#" t="data" class="ctl ctl2">查看数据</a>');
						}else{
							$.Dialog.showPopTip(data.errMsg);
						}
						$this.removeClass('doing');
					})

				}
				if( caseInfo.caseStatus >= 0 ){
					runCase();
				}else{
					Page.dataTransfer.selectedCaseId=caseInfo.caseId;
				}
				
				
				return false;
			}).delegate('.stat', 'mouseover', function(){
				var $this = $(this);
				$('._zq-bubble-ctrls',$this).show();
			}).delegate('.stat', 'mouseout', function(){
				var $this = $(this);
				$('._zq-bubble-ctrls',$this).hide();
			})



		},
		isValidStartAction:function (caseMeta,callback){
			if( caseMeta.versions.length<=1 ){
				$.Dialog.alert('请先添加对比版本。');
				return false;
			}
			if( caseMeta.goals.length<1 ){
				$.Dialog.alert('请先添加追踪目标。');
				return false;
			}
			var hasMasterVersion=false;
			for( var goalId in caseMeta.meta.goal_meta ){
				if( caseMeta.meta.goal_meta[goalId]['isMaster'].toString()=='1' ){
					hasMasterVersion=true;break;
				}
			}
			if( ! hasMasterVersion ){
				$.Dialog.alert('<div style="text-align:left;line-height:24px;"><p>请先添加主追踪目标</p><p>&nbsp;</p><p>1.管理目标 -&gt; 页面类目标 -&gt; 设为主追踪目标</p><p style="text-align:center;">或</p><p>2.管理目标 -&gt; KPI类目标 -&gt; 设为主追踪目标</p></div>');
				return false;
			}
			callback && callback();
			return true;
		},

		getList:function(page,customParams){
			var self = this;

			var proj = Project.curProj;
			if(!proj){

				return false;
			}

			page = page || 1;

			var params = {
				projectId : proj.projectId,
				pageSize  : 20,
				pageNum   : page,
				expriType : 'valid',
				orderBy	  : 'crtime',
				url       : $('#query_txt').val()
			}
			
			if( typeof(customParams)==='object' ){
				$.extend(params,customParams);
			}
			
			$('[t='+params.expriType+']').find(self.listDom).empty();
			self.noneDom.html('加载中...').show();

			$.getEx(MAIN_URL, '/doSearchAvailCaseByProjectId', params , function(data){
				if(data.result == 'success'){
					var list = data.data;

					if(list && list.length > 0){
						var html = [];
						for(var i = 0; i < list.length; i++){
							html.push(self.renderItem(list[i], i+ 1 + 20*(page-1) ));
						}
						self.noneDom.hide();
						self.renderPager(data.pageNum, data.pageTotal);
						$('#tabs_nav [t='+params.expriType+'] i').html(data.dataTotal);
						$('[t='+params.expriType+']').find(self.listDom).html(html.join(''));
						$.getEx(MAIN_URL, '/getCasePersonViewByProjectId', {pid:params.projectId}, function(data){
							if( data ){
								$.each(data,function(caseId,pCount){
									$('._zq-field-pcount[data-caseid='+caseId+']',self.listDom).html(pCount);
								});
							}
						});
					}else{
						self.renderPager(0,0);
						self.noneDom.html('还没实验，<a href="#" class="btn add">立即添加</a>');
						$('#lab_total').html('共'+0+'个实验');
					}

				}else{
					self.noneDom.html('加载失败')
				}

			})
		},

		getLabStatus:function(data){
			switch(data.caseStatus){
				case -1 :
					return '<div class="stat archive" cid="'+data.caseId+'">'
								+'<span class="icon"></span><span class="txt">已归档</span>'
							+'</div>';
				case 0 :
					return '<div class="stat draft" cid="'+data.caseId+'">'
								+'<span class="icon"></span><span class="txt">草稿</span>'
								+'<span class="_zq-bubble-wrapper clrfix">'
									+'<div class="_zq-bubble-ctrls" cid="'+data.caseId+'">'
									+	'<span class="_zq-bubble-ctrl _zq-start-ctrl">启动</span>'
									+	'<span class="_zq-hline"></span>'
									+	'<span class="_zq-bubble-ctrl _zq-archive-ctrl ctl" t="del">归档</span>'
									+'</div>'
								+'</span>'
							+'</div>';
				case 2 :
					return '<div class="stat start" cid="'+data.caseId+'">'
								+'<span class="icon"></span><span class="txt">运行中</span>'
								+'<span class="_zq-bubble-wrapper">'
									+'<div class="_zq-bubble-ctrls clrfix" cid="'+data.caseId+'">'
									+	'<span class="_zq-bubble-ctrl _zq-pause-ctrl">暂停</span>'
									+'</div>'
								+'</span>'
							+'</div>';
				case 1 :
					return '<div class="stat pause" cid="'+data.caseId+'">'
								+'<span class="icon"></span><span class="txt">已暂停</span>'
								+'<span class="_zq-bubble-wrapper">'
									+'<div class="_zq-bubble-ctrls clrfix" cid="'+data.caseId+'">'
									+	'<span class="_zq-bubble-ctrl _zq-start-ctrl">启动</span>'
									+	'<span class="_zq-hline"></span>'
									+	'<span class="_zq-bubble-ctrl _zq-archive-ctrl ctl" t="del">归档</span>'
									+'</div>'
								+'</span>'
							+'</div>';
			}
			return '';
		},

		renderItem:function(data, index){
			var self = this;

			self.dataInfo[data.caseId] = data;

			var html = '<tr>\
							<td class="_zq-field-status">'+self.getLabStatus(data)+'</td>\
							<td>'+data.caseName+'</td>\
							<td>'+$.TypeHelper.getBuizTypes(data.buizType).name+'</td>\
							<td class="_zq-field-pcount" data-caseid="'+data.caseId+'">0</td>\
							<td>'+data.verCount+'</td>\
							<td>'+data.curGoalCount+'/'+data.maxGoalCount+'</td>\
							<td>'+$.formatDate(data.createTime, 'yyyy-mm-dd <br />H:i:s')+'</td>\
							<td class="ctls" cid="'+data.caseId+'">\
								<a href="#" t="edit" class="ctl">配置</a>\
								<a href="#" t="copy" class="ctl">拷贝</a>'
								+(data.caseStatus == 0 
										? '<a href="#" t="realdel" class="ctl ctl3">删除</a>' 
										: '<a href="#" t="data" class="ctl ctl2">查看数据</a>')+
							'</td>\
						</tr>';

			return html;
		},

		renderPager:function(page,totalPage){
			var self = this;
			$.renderPager('#lab_list_pager',totalPage,page,10,function(num){
				self.getList(num);
			});
		},

		show:function(){
			this.init();
			$('.project_tabs .tab,._zq-tab_main .tab').hide();
			this.dom.show();
			this.getList();
		}

	});
	
	var ProjectCode = new Class({
		dom:$('#lab_setting'),
		init:function(){
			if(this.isInit){
				return false;
			}

			this.isInit = true;

			$('#proj_code').click(function(){
				$(this).setSelect(0,1000);
			})

		},
		show:function(){
			this.init();

			var proj = Project.curProj;

			var code = '<script src="'+DATA_HOST+'/abtest/code/'+proj.projectId+'/stat.js"></script>';

			$('#proj_code').val(code);

			$('.project_tabs .tab').hide();
			$('._zq-tab_main .tab').hide();
			this.dom.show();

		}
	});

	var LabPreview = new Dialog({
		html : '<div style="display:none;min-width:600px;" class="dialog _zq-large-viewer _zq-flat">\
 						<div class="dialog_title">配置信息<span class="_zq-closeme"></span></div>\
 						<div class="content">\
 							<div class="lab_preview">\
 								<div class="tab_tl">基本信息</div>\
 								<div class="tab_con">\
 									<div class="tab_form" id="case_info">\
 										<div class="item clearfix clrfix">\
 											<div class="l">实验名称：</div>\
 											<div class="r2">测试11</div>\
 											<div class="l">所属业务：</div>\
 											<div class="r2">自助</div>\
											<div class="l">移动版：</div>\
											<div class="r2">是</div>\
 										</div>\
 										<div class="item clearfix clrfix">\
 											<div class="l">实验网址：</div>\
 											<div class="r">ssss</div>\
 										</div>\
 									</div>\
 								</div>\
								<div class="tab_tl">版本信息</div>\
								<div class="tab_con">\
									<div class="tab_form">\
										<div class="item">\
											<div class="r" id="version" style="height:120px;"></div>\
										</div>\
									</div>\
								</div>\
								<div class="tab_tl">追踪目标</div>\
								<div class="tab_con" id="_zq-view-goal-list">\
									<div class="tab_form">\
									<div class="item clearfix">\
										<div class="l">页面类目标：</div>\
										<div id="page_goal" class="r"></div>\
									</div>\
									<div class="item clearfix">\
										<div class="l">KPI类目标：</div>\
										<div id="kpi_goal" class="r"></div>\
									</div>\
									</div>\
								</div>\
 								<div class="tab_tl">实验代码</div>\
 								<div class="tab_con" id="_zq-view-code">\
 									<div class="setting_wrap" style="padding-top:0px;">\
										<div class="top_tl">请将以下代码拷贝到您网页的&lt;head&gt;&lt;/head&gt;之间或紧挨&lt;body&gt;</div>\
										<div style="width:900px;">\
											<textarea readonly class="inp" style="width:700px;height:40px;" id="proj_code"></textarea>\
											<input type="button" class="_zq-docopy" id="_zq-btn-copy-proj_code" value="复制"/>\
										</div>\
										<div class="_zq-copy-success-tip" style="display:none;">内容已经复制到剪贴板</div>\
										<div class="orange">注：控制版本及URL分离的版本需要嵌入代码，在线编辑的版本不需要嵌入代码</div>\
									</div>\
 								</div>\
 							</div>\
 							<div class="none_txt">正在加载...</div>\
 						</div>\
 				  </div>',
 		onInit : function(dialog){
 			var $dom = dialog.dom;

 			$dom.delegate('.ok_btn', 'click', function(){
 				dialog.hide();
 				return false;
 			});
 			$('._zq-closeme',$dom).on('click',function(){
 				dialog.hide();
 			});
 			
 			
 		},

 		onShow : function(dialog){
 			var $dom = dialog.dom;

 			var showInfo = function(data){
 				var caseInfo = data.caseInfo;
				$dom.find('#case_info').html('<div class="item clearfix">\
 											<div class="l">实验名称：</div>\
 											<div class="r2">'+caseInfo.caseName+'</div>\
 											<div class="l">所属业务：</div>\
 											<div class="r2">'+$.TypeHelper.getBuizTypes(caseInfo.buizType).name+'</div>\
 											<div class="l">移动版：</div>\<div class="r2">'+(caseInfo.isMobile==1?'是':'否')+'</div>\
 										</div>\
 										<div class="item clearfix">\
 											<div class="l">实验网址：</div>\
 											<div class="r"><a href="'+caseInfo.url+'" target="_blank">'+caseInfo.url+'</a></div>\
 										</div>');

				var goalInfo = data.pageGoal;
				var html = [];
				for(var i = 0, item; i < goalInfo.length; i++){
					item = goalInfo[i];
					html.push('<span class="tag">'+item.goalName+(item.isMaster==1?'&nbsp;<span class="red">*</span>':'')+'</span>')
				}
				$dom.find('#page_goal').html(html.join('') || '无');

				goalInfo = data.kpiGoal;
				html = [];
				for(var i = 0, item; i < goalInfo.length; i++){
					item = goalInfo[i];
					html.push('<span class="tag">'+item.goalName+(item.isMaster==1?'&nbsp;<span class="red">*主追踪</span>':'')+'</span>')
				}
				$dom.find('#kpi_goal').html(html.join('') || '无');

				var code = '<script src="'+DATA_HOST+'/abtest/code/'+dialog.info.projectId+'/stat.js"></script>';
				$dom.find('#proj_code').val(code);

				var pieData = [];
				var list = data.percents;
				for(var i = 0, item; i < list.length; i++){
					item = list[i];
					var _oneData={
						name:item.versionName,y:item.percent,color:Page.versionColors[i]
					};
					pieData.push(_oneData);
				}

				showPie(pieData);
				
				$dom.find('.lab_preview').show();
				$.Dialog.autoFix();
				$('#_zq-btn-copy-proj_code').data('zclipId') || $('#_zq-btn-copy-proj_code').zclip({
					path:MAIN_URL+'/assets/js/ZeroClipboard.swf',
					copy:function(){return $('#proj_code').val();},
					afterCopy:function(){
						$('._zq-copy-success-tip',$dom).show();
						setTimeout(function(){$('._zq-copy-success-tip',$dom).hide(1000);}, 2000)
					}
				});
 			}

 			var showPie = function(data){
 				$dom.find('#version').highcharts({
			        chart: {
			            plotBackgroundColor: null,
			            plotBorderWidth: null,
			            plotShadow: false,
			            marginLeft: 0,
			            width:300,
			            spacingLeft: -200
			        },
			        xAxis:{
			        	tickLength:0
			        },
			        credits: {
			            enabled: false
			        },
			        title: {
			            text: ''
			        },
			        tooltip: {
			    	    pointFormat: '<b>{point.percentage}%</b>'
			        },
			         legend: {
			            align: 'right',
			            layout: 'vertical',
			            verticalAlign: 'top',
			            labelFormat:'{name}',
			            itemMarginBottom:5,
			            x: 0,
			            y: 10,
			            symbolHeight:6,
			            symbolWidth:15
			        },
			        plotOptions: {
			            pie: {
			                allowPointSelect: true,
			                cursor: 'pointer',
			                dataLabels: {
			                    enabled: true,
			                    color: '#666',
			                    connectorColor: '#000000',
			                    format: '{point.percentage}%',
			                    connectorWidth:0,
			                    connectorPadding:-25
			                },
			                showInLegend: true
			            }
			        },
			        series: [{
			            type: 'pie',
			            name: 'Browser share',
			            data: data
			        }]
			    });
 			}


 			var params = {
 				projectId : dialog.info.projectId,
 				caseId : dialog.info.caseId
 			}

 			$dom.find('.none_txt').show();
 			$dom.find('.lab_preview').hide();

 			$.getEx(MAIN_URL, '/doSearchConfigInfoByCaseId', params, function(data){
 				if(data.result == 'success'){
 					showInfo(data);
 					$dom.find('.none_txt').hide();
 				}else{
 					$.Dialog.showPopTip(data.errMsg);
 				}
 			})
 		}

	});

 	Project.init();
 	

 })