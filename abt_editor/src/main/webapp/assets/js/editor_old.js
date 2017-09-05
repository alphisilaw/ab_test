/**
 *  @author liurunbao
 *  @file editor.js
 *  @brief editor js
 */
(function($) {
    //十六进制颜色值的正则表达式
    var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
    /*RGB颜色转换为16进制*/
    String.prototype.colorHex = function(){
        var that = this;
        if(/^(rgb|RGB)/.test(that)){
            var aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g,"").split(",");
            var strHex = "#";
            var len = aColor.length;

            if( aColor[0] == 'a0' && aColor[len-1] == 0 ) {
                return 'transparent';
            }
            for(var i=0; i<len; i++){
                var hex = Number(aColor[i]).toString(16);
                if(hex === "0"){
                    hex += hex;
                }
                strHex += hex;
            }
            if(strHex.length !== 7){
                strHex = that;
            }
            return strHex;
        }else if(reg.test(that)){
            var aNum = that.replace(/#/,"").split("");
            if(aNum.length === 6){
                return that;
            }else if(aNum.length === 3){
                var numHex = "#";
                for(var i=0; i<aNum.length; i+=1){
                    numHex += (aNum[i]+aNum[i]);
                }
                return numHex;
            }
        }else{
            return that;
        }
    };

    /*16进制颜色转为RGB格式*/
    String.prototype.colorRgb = function( opacity ){
        var sColor = this.toLowerCase();
        if(sColor && reg.test(sColor)){
            if(sColor.length === 4){
                var sColorNew = "#";
                for(var i=1; i<4; i+=1){
                    sColorNew += sColor.slice(i,i+1).concat(sColor.slice(i,i+1));
                }
                sColor = sColorNew;
            }
            //处理六位的颜色值
            var sColorChange = [];
            for(var i=1; i<7; i+=2){
                sColorChange.push(parseInt("0x"+sColor.slice(i,i+2)));
            }
            return "RGB(" + sColorChange.join(",") + ")";
        }else{
            return sColor;
        }
    };
})(jQuery);
$(function () {
    var Page = new Class({
        autoInit: true,
        versionColors: ['#FEA525', '#5EBF70', '#59BBEA', '#FEA525', '#5EBF70', '#59BBEA'],
        init: function () {
            self = this;
            self.initEvents();
        },
        initEvents: function () {},
        dataTransfer: {},
        versionNames: {}
    });
    editorConf = {
        caseData: null,
        goalList: [],
        url: '',
        mode: 'edit',
        baseEditFrame: $('.html_loader').eq(0),
        editing: false,
        isdropmenu: false,
        dropMenu: $('#drop_menu'),
        selectEle: null,
        fixTop: 130,
        doc: null,
        firstLoad: true,
        curDoc: null
    }

    //初始化
    var initPage = function () {
        var url = $.getUrlParam('url');
        var caseId = $.getUrlParam('caseId');
        if (caseId != '') {
            dataCtl.getCaseData(caseId);
        } else {
            if (url != '') {
                editorConf.url = url;
                loadEditPage(url);
            } else {
                location.href = MAIN_URL;
            }
        }
 
        $('.goal_ctl_chk input').prop('checked', true);
        //$.ZqUI.checkbox($('.goal_ctl_chk input:checkbox'));
 
        $(window).bind("beforeunload", function () {
            if (dataCtl.checkChange()) {
                return '您将离开配置页面，离开前请确定您已经保存好您需要的配置信息。';
            }
        });
        // $(document).bind("contextmenu",function(e){  
        //           return false;  
        //       });
    };
    /*
     * ===========================================================================
     * 版本工作逻辑
     * ---------------------------------------------------------------------------
     * 新增版本：(curVersion|addNewVersion)-->触发版本选项卡点击事件-->resetCurrent
     *        -->根据curVersion渲染版本编辑器中显示的内容（不同版本类型有不同渲染方式）-->setMode
     * 
     * ---------------------------------------------------------------------------
     * Url分离版本渲染方式：UrlVersionCtl在新的iframe打开新页面
     * 在线编辑方式：通过执行渲染代码在原iframe执行
     * **浏览模式打开的新页面（实际上也可以抽象为版本方式，行为类似于Url分离版本），但是触发条件不同，监听对象页面链接
     * **点击事件和表单的提交事件来触发UrlVersionCtl，在原iframe打开新页面
     * =========================================================================== 
     */
 
 
    //编辑数据管理
    var dataCtl = (function () {
        var cacheDoc = {};
        var currentEl;
        var hasChange = false;
        var versionList = [];
        var versionInfo = {};
        var curVersion;
        var $edit_ctls = $('.edit_ctls');
        var $versionDom = $('.version_wrap');
        var init = function () {
            initVersionCtl();
            initEditCtl();
            // setCtlStatus();
            initLabSetting();
            getVersionList();
        }
 
        var saveVersion = function (version, cb) {
            // if(version.type == 1){ //控制版不能修改
            //  cb && cb();
            //  return ;
            // }
            if (version.isDel) {
                if (!version.unsave) {
                    var params = {
                        versionId: version.id,
                        caseId: editorConf.caseData.caseId
                    }
 
                    $.postEx(MAIN_URL, '/doDeleteVersion', params, function (data) {
                        if (data.result == 'success') {
                            version.unsave = 1;
                            version.undo = [];
                            version.redo = [];
                            setCtlStatus();
                            // version
                        } else {
 
                        }
                        cb && cb();
                    })
                } else {
                    cb && cb();
                }
 
            } else if (version.unsave == 1) {
                var params = {
                    versionName: version.name,
                    caseId: editorConf.caseData.caseId,
                    projectId: editorConf.caseData.projectId,
                    jsCode: version.code.join(';'),
                    percent: version.percent,
                    versionType: version.type,
                    forwardUrl: version.url
                }
                $.postEx(MAIN_URL, '/doSaveVersion', params, function (data) {
                    if (data.result == 'success') {
                        version.unsave = 0;
                        version.isChange = 0;
                        version.undo = [];
                        version.redo = [];
                        setCtlStatus();
                        // version
                    } else {
                        $.Dialog.alert(data.errMsg);
                    }
                    cb && cb();
                })
            } else {
                if (version.isChange == 1) {
                    var params = {
                        versionName: version.name,
                        caseId: editorConf.caseData.caseId,
                        versionId: version.id,
                        jsCode: version.code.join(';'),
                        percent: version.percent,
                        versionType: version.type,
                        forwardUrl: version.url
                    }
 
                    $.postEx(MAIN_URL, '/doSaveVersion', params, function (data) {
                        if (data.result == 'success') {
                            version.isChange = 0;
                            version.undo = [];
                            version.redo = [];
                            setCtlStatus();
                        } else {
                            $.Dialog.alert(data.errMsg);
                        }
                        cb && cb();
                    })
                } else {
                    cb && cb();
                }
            }
        }
 
        var saveVersionSequence = function (list, n, cb) {
            if (n < list.length) {
                saveVersion(list[n], function () {
                    n++;
                    saveVersionSequence(list, n, cb);
                });
            } else {
                cb && cb();
            }
        }
 
        var doSaveVersion = function (cb) {
            if (!hasChange) {
                cb && cb();
                return false;
            }
 
            $.Dialog.loader.show('正在提交配置...');
            saveVersionSequence(versionList, 0, function () {
                $.Dialog.loader.hide();
                cb && cb();
            });
            hasChange = false;
            setCtlStatus();
        }
 
        var initEditCtl = function () {
            $edit_ctls.delegate('.btn', 'click', function () {
                var $this = $(this);
                if ($this.hasClass('disabled')) {
                    return false;
                }
                var type = $this.attr('t');
                switch (type) {
                case 'save':
                    if (!hasChange) {
                        return false;
                    }
 
                    if (!checkCanSave()) {
                        return false;
                    }
 
                    doSaveVersion(function () {
                        // alert(1);
                    })
                    // for(var i = 0; i < versionList.length; i++){
                    //  saveVersion(versionList[i]);
                    // }
 
                    // hasChange = false;
                    // setCtlStatus();
                    break;
                case 'undo':
                    if (curVersion.undo.length > 0) {
                        var code = curVersion.undo.pop();
                        curVersion.redo.push(copyCode(curVersion.code));
                        curVersion.code = code;
                        resetDoc(1);
                        console.log( getCode(code) );
                        execCode(getCode(code));
                    }
                    break;
                case 'redo':
                    if (curVersion.redo.length > 0) {
                        var code = curVersion.redo.pop();
                        curVersion.undo.push(copyCode(curVersion.code));
                        curVersion.code = code;
                        resetDoc(1);
                        execCode(getCode(code));
                    }
                    break;
                }
                setCtlStatus();
                return false;
            });
        }
 
        var getCaseData = function (caseId) {
            $.getEx(MAIN_URL, '/getCaseInfo', {
                caseId: caseId
            }, function (data) {
                if (data.result == 'success') {
                    editorConf.caseData = data.info;
                    editorConf.url = data.info.url;
                    if (editorConf.caseData.caseStatus != 0) {
                        showCantEditTip(editorConf.caseData.caseStatus);
                    }
                    dealStatus(editorConf.caseData.caseStatus.toString());
                    dealStatus();
                    $('#proj_nav_name').attr('href', MAIN_URL + '/v/experiment?pid=' + editorConf.caseData.projectId).text(
                        $.getUrlParam('pName') || '项目名称');
                    $('.proj_info .lname').find('.read span').text(editorConf.caseData.caseName);
                    $('#_zq-view-mode [t=current-url]').attr('current-url', data.info.url);
                    getGoalList();
                    loadEditPage(data.info.url);
                } else {
                    location.href = MAIN_URL;
                }
            })
        }
 
        var getGoalList = function (cb) {
            var caseData = editorConf.caseData;
            if (!caseData) {
                return false;
            }
 
            var params = {
                caseId: caseData.caseId,
                projectId: caseData.projectId
            }
 
            $.getEx(MAIN_URL, '/doSearchGoalByCaseId', params, function (data) {
 
                if (data.result == 'success') {
                    var list = data.normalGoalData;
                    editorConf.goalList = data;
                    editorConf.goalData = {};
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        editorConf.goalData[item.goalId] = item;
                    }
 
                    list = data.kpiGoalData;
                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        editorConf.goalData[item.goalId] = item;
                    }
                    $('#_zq-goals-count').html(data.normalGoalData.length + data.kpiGoalData.length);
                }
 
                if ($('.goal_ctl_chk input').attr('checked')) {
                    showGoals();
                }
 
                cb && cb();
            })
 
        }
 
        var showGoals = function (hide) {
            var $doc = editorConf.doc;
            if (!$doc) {
                return;
            }
            if (hide) {
                $doc.find('.medialab-abtest-goal').remove();
            } else {
                var list = editorConf.goalList.normalGoalData;
                $doc.find('.medialab-abtest-goal').remove();
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var $el = $doc.find(item.goalSelector);
                    if ($el.length > 0) {
                        $el.each(function () {
                            var $el = $(this);
                            if (!$el.hasClass('medialab-elem')) {
                                var $goal = $('<div class="medialab-abtest-goal medialab-elem"></div>');
                                $goal.css({
                                    left: $el.offset().left,
                                    top: $el.offset().top - 2,
                                    width: $el.outerWidth(),
                                    height: $el.outerHeight()
                                }).appendTo($doc.find('body'));
                            }
                        });
                    }
                }
            }
 
            if (curVersion && curVersion.ctl) {
                curVersion.ctl.showGoals(hide);
            }
 
        }
 
        var getVersionList = function () {
            var caseData = editorConf.caseData;
            if (caseData) {
                var params = {
                    caseId: caseData.caseId,
                    projectId: caseData.projectId
                }
 
                $.getEx(MAIN_URL, '/doSearchVersionByCaseId', params, function (data) {
                    if (data.result == 'success') {
                        var list = data.data;
                        if (list && list.length > 0) {
                            for (var i = 0; i < list.length; i++) {
                                var item = list[i];
                                addNewVersion({
                                    id: item.versionId,
                                    name: item.versionName,
                                    type: item.versionType,
                                    url: item.forwardUrl,
                                    code: [item.jsCode],
                                    percent: item.percent
                                });
                                Page.versionNames[item.versionName] = 1;
                            }
                            setMode('edit');
                            setTimeout(function () {
                                $versionDom.find('.btn:first').click();
                            }, 100);
                        } else {
                            addNewVersion();
                            setMode('edit');
                            setTimeout(function () {
                                $versionDom.find('.btn:first').click();
                            }, 100);
                        }
                    } else {
                        addNewVersion();
                        setMode('edit');
                        setTimeout(function () {
                            $versionDom.find('.btn:first').click();
                        }, 100);
                    }
                })
            } else {
 
                addNewVersion();
                setMode('edit');
                setTimeout(function () {
                    $versionDom.find('.btn:last').click();
                }, 100);
            }
 
        }
 
        var initVersionCtl = function () {
 
            // addNewVersion();
 
            $versionDom.delegate('.btn', 'click', function () {
                var $this = $(this);
                if (!$this.hasClass('on')) {
                    var v = $this.attr('v');
                    var data = versionInfo[v];
                    curVersion = data;
                    // hasChange = false;
                    resetCurrent();
                    setCtlStatus();
                    $versionDom.find('.btn').removeClass('on');
                    $this.addClass('on');
                } else {
                    $this.addClass('show_drop');
                }
 
                return false;
            }).delegate('.btn', 'mouseenter', function () {
                // var $this = $(this);
                // if($this.hasClass('on')){
 
                // }
            }).delegate('.btn', 'mouseleave', function () {
                $(this).removeClass('show_drop');
            }).delegate('.btn .item', 'click', function () {
                var $this = $(this);
                var t = $this.attr('t');
                var v = $this.parents('.btn').eq(0).attr('v');
                var version = versionInfo[v];
                switch (t) {
                case 'rename':
                    EditVersion.show(version);
                    version.copies = undefined;
                    break;
                case 'copy':
                    var id = versionList.length + 1;
                    version.copies = version.copies || [];
                    version.copies.push('local_' + id);
                    var seq = version.copies.length;
                    var copyVersionName = version.name + '_拷贝' + parseInt(seq.toString() + id.toString()).toString(36).toUpperCase() +
                        '';
                    while (Page.versionNames[copyVersionName]) {
                        copyVersionName += (parseInt(Math.random() * 999999) % 36).toString(36).toUpperCase();
                    }
                    Page.versionNames[copyVersionName] = 1;
                    addNewVersion({
                        id: 'local_' + id,
                        name: copyVersionName,
                        unsave: 1,
                        code: version.code,
                        type: version.type,
                        url: version.url,
                        prototypeId: version.id,
                        prototypeName: version.name,
                        copySeq: seq
                    });
                    $versionDom.find('.btn:last').click();
                    Page.versionNames['copy of ' + version.name] = 1;
                    hasChange = true;
                    setCtlStatus();
                    break;
                case 'del':
                    $.Dialog.confirm({
                        content: '确认要删除“' + version.name + '”版本？',
                        callback: function (flag) {
                            if (flag) {
                                $versionDom.find('.btn[v=' + version.id + ']').remove();
                                version.isDel = 1;
                                hasChange = true;
                                setCtlStatus();
                                if ($versionDom.find('.btn').length == 0) {
                                    addNewVersion();
                                }
                                if ($versionDom.find('.btn.on').length == 0) {
                                    $versionDom.find('.btn:last').click();
                                }
                                Page.versionNames[version.name] = 0;
                                resetVersionPercent();
                            }
                        }
                    })
                    break;
                }
                return false;
            });
            $('.version_add').click(function () {
                // addNewVersion();
                // $versionDom.find('.btn:last').click();
                // hasChange = true;
                // setCtlStatus();
                return false;
            }).delegate('.drop_box .item', 'click', function () {
                var t = $(this).attr('t');
                switch (t) {
                case 'edit':
                    addNewVersion();
                    $versionDom.find('.btn:last').click();
                    hasChange = true;
                    setCtlStatus();
                    break;
                case 'url':
                    addUrlVersion.show({
                        name: '版本＃' + versionList.length,
                        cb: function (data) {
                            addNewVersion(null, {
                                url: data.url,
                                type: 3,
                                name: data.name
                            });
                            $versionDom.find('.btn:last').click();
                            hasChange = true;
                            setCtlStatus();
                        }
                    });
                    break;
                }
 
 
 
                return false;
            })
 
 
 
            // $versionDom.find('.btn:last').click();
 
        }
 
        var initLabSetting = function () {
            $('#lab_setting').click(function () {
                $(this).addClass('show_drop');
                return false;
            }).mouseleave(function () {
                $(this).removeClass('show_drop');
            });
            $('#lab_setting').delegate('.item', 'click', function () {
                var $this = $(this);
                var t = $this.attr('t');
                switch (t) {
                case 'code':
                    ProjCode.show();
                    break;
                case 'assign':
                    Assign.show();
                    break;
                case 'limit':
                    LabLimit.show();
                    break;
                case 'preview':
                    LabPreview.show();
                    break;
                default:
                    break;
                }
                return false;
            })
            $('#lab_people_setting').click(function () {
                LabLimit.show();
            });
            $('#lab_uvflow_setting').click(function () {
                Assign.show();
            });
            $('#lab_goal_setting').click(function () {
                // $(this).addClass('show_drop');
                GoalManager.show();
                return false;
            }).mouseleave(function () {
                $(this).removeClass('show_drop');
            });
        }
 
        var copyCode = function (code) {
            var new_code = [];
            for (var i = 0; i < code.length; i++) {
                new_code.push(code[i]);
            }
 
            return new_code;
        }
 
        var resetVersionPercent = function () {
            var list = [];
            for (var i = 0; i < versionList.length; i++) {
                var item = versionList[i];
                if (!item.isDel) {
                    list.push(item);
                }
            }
 
            var total = list.length;
            var p = Math.floor(100 / total);
            var lp = 100 - p * (total - 1);
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                item.percent = p;
                item.isChange = 1;
            }
 
            list[list.length - 1].percent = lp;
        }
 
        var addNewVersion = function (version, info) {
            var reset = false;
            if (!version) {
                var id = versionList.length;
                version = {
                    id: 'local_' + id,
                    name: '版本＃' + id,
                    type: 2,
                    url: '',
                    unsave: 1,
                    code: []
                }
 
                if (info) {
                    version = $.extend(version, info);
                }
 
                reset = true;
            }
            if (version.prototypeId) {
                reset = true;
            }
            version.undo = [];
            version.redo = [];
            //
            if (version.type == 3) {
                version.editFrameId = 'ifr-url-' + parseInt(Math.random().toString().replace('.', '')).toString(36);
            } else {
                version.editFrameId = editorConf.baseEditFrame.attr('id');
            }
 
            versionList.push(version);
            versionInfo[version.id] = version;
            if (reset) {
                resetVersionPercent();
            }
 
            var tmp = '<div class="btn" v="' + version.id + '">\
                            <div class="con">' + version.name;
            if (version.type != 1) {
                tmp += '<i></i>';
            }
 
            tmp += '</div>';
            var typeTip = '';
            switch (version.type) {
            case 1:
                typeTip = '控制版本';
                break;
            case 2:
                typeTip = '在线编辑版本';
                break;
            case 3:
                typeTip = 'URL分离版本';
                break
            }
 
            tmp += '<div class="top_tip"><span>' + typeTip + '</span><em class="barr"></em></div>';
            if (version.type != 1) {
                tmp +=
                    '<div class="drop_box">\
                                <div class="item" t="rename">重命名</div>\
                                <div class="item" t="copy">拷贝</div>\
                                <div class="item" t="del">删除</div>\
                            </div>';
            }
 
            tmp += '</div>';
            $versionDom.append(tmp);
        }
 
        var saveDoc = function ($doc) {
            $doc.find('body').find('script').remove();
            dataCtl.cacheDoc = {};
            var _f = false;
            for (var vi in versionInfo) {
                var version = versionInfo[vi];
                if (!dataCtl.cacheDoc[version.editFrameId]) {
                    dataCtl.cacheDoc[version.editFrameId] = $($('#' + version.editFrameId).get(0).contentWindow.document
                        .body).children();
                }
                _f = true;
            }
            if (!_f) {
                dataCtl.cacheDoc[editorConf.baseEditFrame.attr('id')] = $doc.find('body').children();
            }
            //cacheDoc = $doc.find('body').children();
 
        }
        /*
         * 使用场景：
         * --------------------------------------------------
         * 1）撤销
         * 2）重做
         * 3）拷贝
         * 4）新建在线编辑版本
         */
        var resetDoc = function (clone, cb) {
            // var $body = editorConf.curDoc.find('body');
            var $body = editorConf.curDoc.find('body');
            console.log(dataCtl.cacheDoc);
            $body.children().remove();
            if (clone) {
                var d = dataCtl.cacheDoc[curVersion.editFrameId].clone();
                $body.append(d);
                //$body.append('<style>html,body,*{ cursor:default !important;}</style>');
 
            } else {
                $body.append(dataCtl.cacheDoc[curVersion.editFrameId]);
                //$body.find('a').attr('target', '_blank');
            }
 
            /*if( ! $('.cursour-default',editorConf.curDoc).length ){
             $('head',editorConf.curDoc)
             .append('<style class="cursour-default">html,body,*{ cursor:default !important;}</style>');
             }*/
 
            editorConf.selector = $body.find('.medialab-abtest-selector');
            setTimeout(function () {
                cb && cb();
            }, 500);
        }
 
        var execCode = function (code, cb) {
            setTimeout(function () {
                cb && cb();
            }, 300)
            try {
                code = code.replace(/\r|\n/gi, '');
                // console.log( code );
                editorConf.win['eval'](";(function($){try{" + code +
                    ";}catch(e){throw e;}})(window.__medialab_abtest_jQuery)")
            } catch (e) {
                throw e;
            }
        }
 
        var addCode = function (code) {
            curVersion.undo.push(copyCode(curVersion.code));
            curVersion.redo = [];
            curVersion.code.push(code.code);
            curVersion.isChange = 1;
            resetDoc(1, function () {});
            execCode(getCode(curVersion.code), function () {
                hasChange = true;
                setCtlStatus();
                if ($('.goal_ctl_chk input').attr('checked')) {
                    showGoals();
                }
            });
        }
 
        var setCtlStatus = function () {
            if (hasChange) {
                $edit_ctls.find('.save').removeClass('disabled');
            } else {
                $edit_ctls.find('.save').addClass('disabled');
            }
 
            if (curVersion.undo.length > 0) {
                $edit_ctls.find('.undo').removeClass('disabled');
            } else {
                $edit_ctls.find('.undo').addClass('disabled');
            }
 
            if (curVersion.redo.length > 0) {
                $edit_ctls.find('.redo').removeClass('disabled');
            } else {
                $edit_ctls.find('.redo').addClass('disabled');
            }
 
        }
 
        var resetCurrent = function (forceUrl) {
            if (forceUrl) {
                if (!curVersion.ctl) {
                    curVersion.ctl = new UrlVersionCtl($.extend(curVersion, {
                        url: forceUrl
                    }));
                }
                curVersion.ctl.show();
            } else if (curVersion && curVersion.type == 3) { //?如果同时又是通过浏览模式打开的新页面怎么办？
                //对于每个version只获取一次UrlVersionCtl的实例。
                //UrlVersionCtl实例包含isload属性，保证页面只真正载入一次，后面再次切换到该版本时只是简单的show一下
                if (!curVersion.ctl) {
                    curVersion.ctl = new UrlVersionCtl(curVersion);
                }
                curVersion.ctl.show();
            } else {
                $('#base_editor').show().siblings('.page_editor').hide();
                editorConf.curDoc = editorConf.doc;
                resetDoc(1);
                setMode(editorConf.mode);
                execCode(getCode());
                if ($('.goal_ctl_chk input').attr('checked')) {
                    showGoals();
                }
            }
            if (editorConf.mode === 'edit') {
                if (!$('.cursour-default', editorConf.curDoc).length) {
                    $('head', editorConf.doc).append(
                        '<style class="cursour-default">html,body,*{ cursor:default;}</style>');
                }
            } else if (editorConf.mode === 'navigate') {
                $('.cursour-default', editorConf.curDoc).remove();
            }
 
        }
 
        var cacheEl = function ($el) {
            currentEl = {
                el: $el,
                txt: $el.text(),
                html: $el.outerHtml()
            }
        }
 
        var changeElTemp = function (type, value) {
            var tmp = currentEl.el;
            if (type == 'text') {
                tmp.text(value);
            } else if (type == 'html') {
                var e = $(value);
                tmp.replaceWith(e);
                tmp = e;
            } else if (type == 'img') {
                tmp.src(value);
            } else if (type == 'url') {
                tmp.html(value[0]);
                tmp.attr('href', value[1]);
                if (value[2] == 1) {
                    tmp.attr('target', "_blank");
                }
            } else if (type == 'sty') {
                var e = eval(value);
                tmp.css(e);
            } else if (type == 'move') {
                tmp.offset(value);
            } else if (type == 'drag') {
                tmp.width(value.width);
                tmp.height(value.height);
            }
            currentEl.el = tmp;
        }
 
        var getCode = function (code) {
            if (!code && !curVersion) {
                return '';
            }
 
            code = code || curVersion.code;
            return code.join(';');
        }
 
        var setChange = function (change) {
            hasChange = change;
            setCtlStatus();
        }
 
        var checkChange = function () {
            return hasChange;
        }
 
        var getVersions = function () {
            return versionList;
        }
 
        var getVersionsInfo = function () {
            return versionInfo;
        }
 
        var getCurVersion = function () {
            return curVersion;
        }
 
        var self = {
            init: init,
            saveDoc: saveDoc,
            resetDoc: resetDoc,
            resetCurrent: resetCurrent,
            execCode: execCode,
            cacheEl: cacheEl,
            changeElTemp: changeElTemp,
            getCode: getCode,
            getCaseData: getCaseData,
            addCode: addCode,
            setChange: setChange,
            checkChange: checkChange,
            getGoalList: getGoalList,
            showGoals: showGoals,
            getVersions: getVersions,
            getVersionsInfo: getVersionsInfo,
            getCurVersion: getCurVersion,
            doSaveVersion: doSaveVersion,
            cacheDoc: cacheDoc
        }
 
        return self;
    })();
    var EditVersion = new Dialog({
        html: '<div style="display:none;min-width:300px;" class="dialog">\
                        <div class="dialog_title">修改版本名称</div>\
                        <div class="content">\
                            <div class="s_form">\
                                <div class="item">\
                                    <div class="l">版本名称：</div>\
                                    <div class="r"><input id="name" type="text" style="width:350px;" /></div>\
                                </div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">确定</a>\
                            <a href="#" class="btn cancel_btn">取消</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.delegate('.ok_btn', 'click', function () {
                var info = dialog.info;
                if ($dom.find('#name').val() == info.name) {
                    dialog.hide();
                    return false;
                }
 
                var name = $dom.find('#name').val().trim();
                if (name == '') {
                    $.Dialog.showPopTip('请输入名称');
                    return false;
                }
                if (Page.versionNames[name]) {
                    $.Dialog.showPopTip('本实验中已经存在同名的版本，请重新指定');
                    return false;
                }
 
                Page.versionNames[info.name] = 0;
                info.name = name;
                info.isChange = 1;
                $('.version_wrap').find('.btn[v=' + info.id + ']').find('.con').html(name + '<i></i>');
                Page.versionNames[name] = 1;
                dataCtl.setChange(true);
                dialog.hide();
                return false;
            });
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var info = dialog.info;
            $dom.find('#name').focus().val(info.name).setSelect(0, 1000);
        }
    })
 
    //加载要编辑的页面
    var loadEditPage = function (url) {
 
        url = parseEditUrl(url);
        var iframe = editorConf.baseEditFrame;
        if (editorConf.caseData.isMobile == 1) {
        	iframe.css('width', 375);
        }
        iframe.bind('load', onEditPageLoad);
        iframe.attr('src', url);
    }
 
    var parseEditUrl = function (url, isMain) {
        if (!isMain) {
            isMain = 'true';
        }
        if (!/^http/.test(url)) {
            url = url.replace(/^\/+/, '', url);
            url = 'http://' + url;
        }
 
        var params = '';
        if (url.indexOf('?') > 0) {
            var tmp = url.split('?');
            params = '&' + tmp[1];
        }
 
        //var pre = url.replace(/^(http|https):\/\/[\w\.:\-]+\/?/i, '');
        //pre = pre.replace(/\?.*$/i, '');
        //pre = pre.replace(/\#.*$/i, '');
        url = SPIDER_URL + '?url=' + encodeURIComponent(url) + '&isMain=' + isMain + '&isMobile=' + editorConf.caseData
            .isMobile + params;
        return url;
    }
    var proxyUrl = function (url, isMain) {
        if (!url) {
            return '';
        }
        if (!isMain) {
            isMain = 'true';
        }
        var params = '';
        if (url.indexOf('?') > 0) {
            var tmp = url.split('?');
            params = '&' + tmp[1];
        }
 
        var match;
        var pre = '';
        var host = '';
        if (match = /^(?:https?:)?\/{2}([^\/]+)\/?([^\?]*)[^\/]*$/.exec(url)) {
            //match = /^(?:https?:)?\/{2}([^\/]+)\/?([^\?]*)[^\/]*$/.exec(url);
            host = match[1];
            pre = match[2];
            pre.replace(/\/$/, '');
            url = SPIDER_URL + '/' + pre + '?host=' + host + '&isMain=' + isMain + '&url=' + encodeURIComponent(url) +
                params;
        } else if (/^\//.test(url)) {
            var path = url.split('?')[0];
            path = path.replace(/^\//, '');
            url = SPIDER_URL + '/' + path + '?isMain=' + isMain + '&url=' + encodeURIComponent(url) + params;
        } else {
            //Use the url relative to the main url
        }
        return url;
    }
 
    //设置操作模式
    var setMode = function (type, $doc) {
        type = type || 'edit';
        editorConf.mode = type;
        $doc = $doc || editorConf.doc;
        switch (type) {
        case 'edit':
            $doc.find("html, body").css({
                "user-select": "none",
                "-o-user-select": "none",
                "-moz-user-select": "none",
                "-khtml-user-select": "none",
                "-webkit-user-select": "none"
            });
            //dataCtl.resetCurrent();
            $doc.bind("contextmenu", editorConf.contextMenuHandler);
            $('.goal_ctl_chk :checkbox:not(:checked)').click();
            $('a[href].proxied', $doc).each(function () {
                if ($(this).attr('_url')) {
                    $(this).attr('href', $(this).attr('_url'));
                    $(this).attr('target', $(this).attr('_target') || "");
                    $(this).removeClass('proxied');
                }
            });
            $('form[action].proxied', $doc).each(function () {
                if ($(this).attr('_url')) {
                    $(this).attr('action', $(this).attr('_url'));
                    $(this).attr('target', $(this).attr('_target') || "");
                    $(this).removeClass('proxied');
                }
            });
            /*if( ! $('.cursour-default').length ){
                 $('head',$doc).append('<style class="cursour-default">html,body,*{ cursor:default !important;}</style>');
                 }*/
            break;
        case 'navigate':
            //dataCtl.resetCurrent();
            //dataCtl.resetDoc();
            $doc.find("html, body").css({
                "user-select": "",
                "-o-user-select": "",
                "-moz-user-select": "",
                "-khtml-user-select": "",
                "-webkit-user-select": ""
            });
            $doc.unbind("contextmenu", editorConf.contextMenuHandler);
            $('.medialab-abtest-selector', $doc).hide();
            $('.goal_ctl_chk :checkbox:checked').click();
            var curVersion = dataCtl.getCurVersion(); //
            $('a[href]:not(.proxied)', $doc).each(function () {
                $(this).attr('_url', $(this).attr('href'));
                $(this).attr('_target', $(this).attr('target') || "");
                $(this).attr('target', curVersion.editFrameId);
                $(this).attr('href', proxyUrl($(this).attr('href')));
                if (!$(this).attr('prx')) {
                    $(this).attr('prx', '1');
                }
                $(this).on('click', function () {
                    $('#_zq-ifr-page-editor').data('newSrc', $(this).attr('href'));
                    dataCtl.resetCurrent($(this).attr('href'));
                });
                $(this).addClass('proxied');
            });
            $('form[action]:not(.proxied)', $doc).each(function () {
                $(this).attr('_url', $(this).attr('action'));
                $(this).attr('_target', $(this).attr('target') || "");
                $(this).attr('target', curVersion.editFrameId);
                $(this).attr('action', proxyUrl($(this).attr('action')));
                if (!$(this).attr('prx')) {
                    $(this).attr('prx', '1');
                }
                var $this = $(this);
                $this.on('submit', function () {
                    $('#_zq-ifr-page-editor').data('newSrc', $this.attr('action'));
                    dataCtl.resetCurrent($this.attr('action'));
                });
                $this.addClass('proxied');
            });
            //editorIframeLoadEvent();
            //$('.cursour-default',$doc).remove();
            break;
        }
    }
 
    var isEditMode = function () {
        return editorConf.mode == 'edit';
    }
 
    var isEditing = function () {
        return editorConf.editing || editorConf.isdropmenu;
    }
 
    //目标页面加载完成
    var onEditPageLoad = function (e) {
 
        var iframe = editorConf.baseEditFrame;
        iframe.unbind('load');
        $('.page_loading').hide();
        editorConf.win = iframe.get(0).contentWindow;
        editorConf.curDoc = editorConf.doc = $(editorConf.win.document);
        //iframe.css('height', editorConf.curDoc.height());
        //iframe.css('width', editorConf.curDoc.width());
        editorConf.contextMenuHandler = function (e) {
            return false;
        };
        editorConf.doc.bind("contextmenu", editorConf.contextMenuHandler);
        //editorConf.doc.unbind("contextmenu",contextMenuHandler);
 
        insertEditCode(editorConf.doc, function ($doc) {
            editorConf.selector = $doc.find('.medialab-abtest-selector');
            dataCtl.saveDoc($doc);
        });
        bindEditEvents();
        dataCtl.init();
        // setMode('edit');
    }
 
 
    //添加html代码到目标页面
    var insertEditCode = function ($doc, cb) {
        $doc = $doc || editorConf.doc;
 
        $(
            '<style type="text/css">\
            .medialab-abtest-selector{\
                background:none;\
                pointer-events:none;\
                border:1px solid #0F0;\
                border-radius:2px;\
                background:rgba(0,115,0,0.3);\
                display:none;\
                position:absolute;\
                z-index:2000002 !important;\
                box-sizing: border-box;\
            }\
            .medialab-abtest-selector-on{\
                background:rgba(0,115,0,0.5);\
                pointer-events:auto;\
            }\
            .medialab-abtest-goal{\
                background:none;\
                border:2px solid red;\
                display:block;\
                position:absolute;\
                pointer-events:none;\
                padding:2px 0;\
                box-shadow: 0 1px 10px rgba(255,0,0,0.4);\
                z-index:2000000 !important;\
            }\
            .medialab-abtest-selector-fullmask{\
                display:none;\
                position:fixed;\
                height:100%;\
                width:100%;\
                left:0;\
                top:0;\
                background:rgba(115,115,115,0.8);\
                z-index:1999999;\
            }\
            .medialab-abtest-selector-fullmask.on{\
                display:block;\
            }\
           ._abt_hidden {\
              visibility: visible !important;\
              opacity: 0 !important;\
              -moz-opacity: 0 !important;\
              filter: alpha(opacity=0) !important;\
              -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=($value*100))" !important;\
            }\
            ._abt_hidden_show {\
              visibility: visible !importan;\
              opacity: 0.5 !important;\
              -moz-opacity: 0.5 !important;\
              filter: alpha(opacity=0.5) !important;\
              -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=($value*50))" !important;\
            }\
             </style>')
            .appendTo($doc.find('head'));
 
 
        $('<div class="medialab-abtest-selector medialab-elem"></div>').appendTo($doc.find('body'));
        $('<div class="medialab-abtest-selector-fullmask medialab-elem"></div>').appendTo($doc.find('body'));
        var script = document.createElement('script');
        script.src = EDITOR_URL + "/assets/js/jquery-1.7.1-abtest.js?v=";
        $doc.find('head').get(0).appendChild(script);
        cb && cb($doc);
    }
 
 
        function editorIframeLoadEvent() {
            var editFrameId = (dataCtl.getCurVersion() && dataCtl.getCurVersion().editFrameId) ? dataCtl.getCurVersion()
                .editFrameId : '_zq-ifr-page-editor';
            /*
             * 在浏览模式中，iframe中页面载入完成事件必需在全局定义，所以需要放置在这里
             */
            $('#' + editFrameId).on('load', function () {
                if (editorConf.firstLoad) {
                    return;
                }
                dataCtl.resetCurrent($(this).data('newSrc'));
            });
        }
    $('.ctl_bar').on('mouseover', function (event) {
        editorConf.selector.hide();
    });
    //处理事件
    var bindEditEvents = function () {
        var $doc = editorConf.curDoc || editorConf.doc;
        $('a[href].proxied,form[action].proxied', $doc).on('click', function () {
            //注意这里有Bug，如果页面发生重定向，则这里捕捉不到，所以最合理的方式是由服务器端代理将url写入返回的页面
            $('#_zq-view-mode [t=current-url]').attr('current-url', $(this).attr('_url'));
        });
        $doc.bind('mouseover', function (evt) {
            var $this = $(this);
            if (isEditMode()) {
                var $el = $(evt.target);
                var tag = $el.prop('tagName').toLowerCase();
                if (tag != 'html') {//tag != 'body' && 
                    //隐藏的改变类
                    if ($el.hasClass("_abt_hidden")) {
                        $el.removeClass("_abt_hidden");
                        $el.addClass("_abt_hidden_show");
                    }
                    if (!$el.hasClass('medialab-elem') && isEditMode() && !isEditing()) {
                        var offset = $el.offset();
                        editorConf.selectEle = $el;
                        editorConf.selector.css({
                            left: offset.left,
                            top: offset.top,
                            width: $el.outerWidth(),
                            height: $el.outerHeight()
                        }).removeClass('medialab-abtest-selector-on').show();
                    }
                }
            }
 
        }).bind('click', function (evt) {
            var $this = $(this);
            if (isEditMode()) {
                editorConf.selector.addClass('medialab-abtest-selector-on');
                //$('.medialab-abtest-selector-fullmask',$this).show();
                //console.log( $(editorConf.selector) );//Debug
                //showDropMenu();
                return false;
            }
        }).bind('mousedown', function (evt) {
            if (isEditMode()) {
                var $el = $(evt.target);
                var tag = $el.prop('tagName').toLowerCase();
                if ( tag != 'html') {//tag != 'body' &&
                    if (!$el.hasClass('medialab-elem') && isEditMode() && !isEditing()) {
                        var offset = $el.offset();
                        editorConf.selectEle = $el;
                        dataCtl.cacheEl($el);
                        editorConf.selector.css({
                            left: offset.left,
                            top: offset.top,
                            width: $el.outerWidth(),
                            height: $el.outerHeight()
                        }).addClass('medialab-abtest-selector-on').show();
                        setTimeout(function () {
                            dropMenu.show();
                        }, 100);
                    }
                }
            }
 
        }).bind('mouseout', function (evt) {
            var $el = $(evt.target);
            //隐藏的改变类
            if ($el.hasClass("_abt_hidden_show")) {
                $el.removeClass("_abt_hidden_show");
                $el.addClass("_abt_hidden");
            }
        });
        editorConf.doc.delegate('a,form :submit', 'click', function () {
            editorConf.firstLoad = false;
        });
        //editorIframeLoadEvent();
 
        //模式切换
        /*$('.view_ctls').delegate('.btn', 'click', function(){
         var $this = $(this);
         if($this.hasClass('on')){
         return false;
         }
         var t = $this.attr('t');
          
         $('.view_ctls').find('.btn').removeClass('on');
         $this.addClass('on');
          
         setMode(t);
          
         return false;
         });*/
 
 
 
        $('#_zq-view-mode').delegate('.btn:not(.x)', 'click', function () {
            var $this = $(this);
            if ($this.hasClass('on')) {
                return false;
            }
            var t = $this.attr('t');
            $('.btn.on', $('#_zq-view-mode')).removeClass('on');
            $this.addClass('on');
            editorConf.mode = t;
            dataCtl.resetCurrent();
            if (dataCtl.getCurVersion().ctl) {
                dataCtl.getCurVersion().ctl.setMode(t);
            } else {
                setMode(t, editorConf.curDoc);
            }
 
            bindEditEvents();
        }).delegate('[t=current-url]', 'click', function () {
            $.Dialog.alert($(this).attr('current-url'), '当前编辑页面URL');
        });
        var $lname = $('.proj_info .lname');
        $lname.delegate('.read', 'click', function () {
            $lname.find('.read').hide();
            $lname.find('.write').show();
            $lname.find('.write input').focus().val(editorConf.caseData.caseName).setSelect(0, 1000);
            return false;
        });
        var saveLname = function () {
            var n = $lname.find('.write input').val().trim();
            if (n == '') {
                n = editorConf.caseData.caseName;
            }
 
            if (n != editorConf.caseData.caseName) {
                $.postEx(MAIN_URL, '/updateCaseName', {
                    caseId: editorConf.caseData.caseId,
                    caseName: n
                }, function (data) {
                    if (data.result == 'success') {
                        editorConf.caseData.caseName = n;
                        $lname.find('.read span').text(n);
                    } else {}
                    $lname.find('.read').show();
                    $lname.find('.write').hide();
                });
            } else {
                $lname.find('.read').show();
                $lname.find('.write').hide();
            }
        }
 
        $lname.find('.write input').blur(function () {
            saveLname();
        }).keyup(function (evt) {
            if (evt.keyCode == 13) {
                saveLname();
            }
        });
        $('.goal_ctl_chk').delegate('input', 'change', function () {
            var $this = $(this);
            if ($this.attr('checked')) {
                dataCtl.showGoals(0);
            } else {
                dataCtl.showGoals(1);
            }
        });
        $('#start_lab').click(function () {
            if (editorConf.caseData.caseStatus.toString() == '0') {
                var $this = $(this);
                if ($this.hasClass('doing')) {
                    return false;
                }
 
                $this.addClass('doing');
                dataCtl.doSaveVersion(function () {
                    dataCtl.getGoalList(function () {
                        if (!checkCanSave()) {
                            return false;
                        }
 
                        $this.removeClass('doing');
                        if ($('.version_wrap .btn').length == 1) {
                            $.Dialog.alert('请先添加对比版本。');
                            return false;
                        }
 
                        if (editorConf.goalList) {
                            if (editorConf.goalList.normalGoalData.length == 0 &&
                                editorConf.goalList.kpiGoalData.length == 0) {
                                $.Dialog.alert('请先添加统计目标。');
                                return false;
                            }
 
                            var hasMain = false;
                            var list = editorConf.goalList.normalGoalData.concat(editorConf.goalList.kpiGoalData);
                            for (var i = 0; i < list.length; i++) {
                                if (list[i].isMaster == 1) {
                                    hasMain = true;
                                    break;
                                }
                            }
 
                            if (!hasMain) {
                                //$.Dialog.alert('<div style="text-align:left;line-height:24px;"><p>请先添加主追踪目标</p><p> </p><p>1.管理目标 -> 页面类目标 -> 设为主追踪目标</p><p style="text-align:center;">或</p><p>2.管理目标 -> KPI类目标 -> 设为主追踪目标</p></div>');
                                $.Dialog.showPopTip("请先添加主追踪目标");
                                $('#lab_goal_setting').click();
                                return false;
                            }
 
                        }
 
                        StarLab.show();
                    });
                });
                return false;
            }
        });
    }
 
    var showCantEditTip = function (caseStatus) {
        if (!caseStatus) {
            caseStatus = 2;
        };
        if (!$('#edit_top_tip-' + caseStatus).length) {
            $('#edit_top_tip-2').show();
        } else {
            $('#edit_top_tip-' + caseStatus).show();
        }
        $('.ctl_bar').css('box-shadow', 'none');
        $('.page_editor').css({
            top: parseInt($('.page_editor').css('top')) + 36
        });
    }
 
    var checkMenuForHide = function ($menu, cb) {
        var $chk = $('#chk_menu');
        if ($chk.length == 0) {
            $chk = $('<div id="chk_menu" style="position:absolute; top:-1000px;"><input type="text"></div>');
            $('body').append($chk);
        }
 
        $chk.find('input').unbind('blur').bind('blur', function () {
            setTimeout(function () {
                $menu.hide();
                cb && cb();
            }, 200);
            $chk.find('input').unbind('blur');
        }).focus();
    }
 
    var getEleSelector = function ($el, arr) {
        arr = arr || [];
        if (!$el || $el.length == 0) {
            return arr.join('>');
        }
 
        var tagName = $el.prop('tagName').toLowerCase();
        if (tagName == 'body') {
            arr.unshift('body');
            return arr.join('>');
        }
 
        var id = $el.attr('id'); //此处不能用prop，对于form来说，prop是获取其下名为id的input元素
 
        if (id && (!id.match(/[:>\s\[\]\.\(\)#]/)) && ($el.parent().find('#' + id).length == 1)) {
            arr.unshift(tagName + '#' + id);
        } else {
            var eclass = $el.attr('class');
            eclass = eclass ? eclass.trim() : '';
            if (eclass != '') {
                eclass = eclass.split(/\s+/);
                eclass = eclass.join('.');
                if ($el.parent().find('.' + eclass).length == 1) {
                    arr.unshift(tagName + '.' + eclass);
                } else {
                    arr.unshift(':eq(' + $el.index() + ')');
                }
            } else {
                arr.unshift(':eq(' + $el.index() + ')');
            }
        }
        return arguments.callee($el.parent(), arr); //递归
    }
 
    var dropMenu = (function () {
 
        var $menu = editorConf.dropMenu;
        //判断按钮是否可用
 
        function checkValid($el) {
            $menu.find("div").removeClass("dis");
            if ( !! !$el.attr("href")) {
                $menu.find("#edit-url").attr("disabled", true);
                $menu.find("#edit-url").addClass("dis");
            }
            if ($el.prop("tagName") != 'IMG') {
                $menu.find("#edit-img").attr("disabled", true);
                $menu.find("#edit-img").addClass("dis");
            }
            //隐藏元素
            if ($el.hasClass("_abt_hidden") || $el.hasClass("_abt_hidden_show")) {
                $menu.find("#edit-hide,#edit-show").find("span").text("显示");
                $menu.find("#edit-hide,#edit-show").attr("id", 'edit-show');
                $menu.find("#edit-hide,#edit-show").find("img").attr("src", EDITOR_URL + '/assets/images/ico-show.png');
            } else {
                $menu.find("#edit-hide,#edit-show").find("span").text("隐藏");
                $menu.find("#edit-hide,#edit-show").attr("id", 'edit-hide');
                $menu.find("#edit-hide,#edit-show").find("img").attr("src", EDITOR_URL + '/assets/images/ico-hide.png');
            }
            //判断是否目标
            var el_slt = getEleSelector($el);
            if (isGoal(el_slt)) {
                $menu.find("#edit-addgoal,#edit-editgoal").find("span").text("管理目标");
                $menu.find("#edit-addgoal,#edit-editgoal").attr("id", 'edit-editgoal');
                $menu.find("#edit-addgoal,#edit-editgoal").find("img").attr("src", EDITOR_URL +
                    '/assets/images/ico-manager.png');
            } else {
                $menu.find("#edit-addgoal,#edit-editgoal").find("span").text("添加目标");
                $menu.find("#edit-addgoal,#edit-editgoal").attr("id", 'edit-addgoal');
                $menu.find("#edit-addgoal,#edit-editgoal").find("img").attr("src", EDITOR_URL +
                    '/assets/images/ico-add.png');
            }
        }
 
        var isGoal = function (slt) {
            var list = editorConf.goalList.normalGoalData;
            var html = [];
            var item;
            for (var i = 0; i < list.length; i++) {
                item = list[i];
                if (item.goalSelector == slt) {
                    return true;
                }
            }
            return false;
        }
        var getSelectedGoal = function (slt) {
            var list = editorConf.goalList.normalGoalData;
            var html = [];
            var item;
            for (var i = 0; i < list.length; i++) {
                item = list[i];
                if (item.goalSelector == slt) {
                    return item;
                }
            }
            return undefined;
        }
 
        var init = function () {
            $menu.find('.menu_list').html(
                '<ul id = "edit-ls" >\
                <li >\
                <div id = "edit-text"  class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-txt.png" / >\
                <span > 编辑文本 </span>\
                </div> \
                <div id = "edit-url"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-url.png" / >\
                <span > 编辑URL </span>\
                </div> \
                </li> \
                <li >\
                <div id = "edit-htm"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-ht.png" / >\
                <span > 编辑HTML </span>\
                </div>\
                <div id = "edit-sty"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-sty.png" / >\
                <span > 编辑样式 </span>\
                </div> \
                </li> \
                <li >\
                <div id = "edit-img"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-pic.png" / >\
                <span > 编辑图片 </span>\
                </div> \
                <div id = "edit-common"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/icon-common.png" / >\
                <span > 常用修改 </span>\
                </div> \
                </li> \
                <li  class = "b-t" >\
                <div id = "edit-move"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-move.png" / >\
                <span > 移动 </span>\
                </div> \
                <div id = "edit-drag"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-change.png" / >\
                <span > 调整大小 </span>\
                </div> </li >\
                <li  class = "b-tb" >\
                <div id = "edit-del"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-del.png" / >\
                <span > 删除 </span>\
                </div> \
                <div id = "edit-hide"   class="menu medit">' +
                '<img src = "' + EDITOR_URL +
                '/assets/images/ico-hide.png" / >\
                <span > 隐藏 </span>\
                </div> </li >\
                </ul> \
                <div id="edit-addgoal" class = "op-btn menu"    >' +
                '<img src = "' + EDITOR_URL + '/assets/images/ico-add.png" / > <span>添加目标</span>\
                </div>');
 
            $menu.delegate('.menu', 'click', function () {
                $menu.data('isclick', 1);
                var $item = $(this);
                if ($item.hasClass('disabled')) {
                    return false;
                }
 
                var type = $item.attr('id');
                var $el = editorConf.selectEle;
                var el_slt = getEleSelector($el);
                switch (type) {
                case 'edit-text':
                    popMenu.show('edit-text', '编辑文本', $el.text(), function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').text("' + html.replace(/\"/gi, '\\"') + '")';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('text', html);
                    });
                    break;
                case 'edit-htm':
                    popMenu.show('edit-htm', '编辑HTML', $el.outerHtml(), function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').replaceWith("' + html.replace(/\"/gi, '\\"') + '")';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('html', html);
                    });
                    break;
                case 'edit-img':
                    var objTagName = $el[0].tagName === 'IMG';
                    var imgSrc = $el.attr('src');
                    if( !objTagName ) {
                        imgSrc = $el.css('background-image').match(/\(.*\)/);
                        imgSrc = imgSrc ? imgSrc[0].replace(/[\(\)\"]/g, '') : '';
                    }

                    popMenu.show('edit-img', '编辑图片', imgSrc, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').attr(\'src\',\'' + html.replace(/\"/gi, '\\"') + '\')';
                            if( !objTagName ) {
                                code = '$(\'' + el_slt + '\').css(\'background-image\',\'url(' + html.replace(/\"/gi, '\\"') + ')\')';
                            }
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('img', html);
                    });
                    break;
                case 'edit-url':
                    popMenu.show('edit-url', '编辑URL', $el, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').attr(\'href\',\'' + html[1].replace(/\"/gi, '\\"') +
                                '\');';
                            code += '$(\'' + el_slt + '\').html(\'' + html[0].replace(/\"/gi, '\\"') + '\');';
                            if (html[2] == 1) {
                                code += '$(\'' + el_slt + '\').attr(\'target\',\'_blank\');';
                            }
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('url', html);
                    });
                    break;
                case 'edit-common':
                    popMenu.show('edit-common', '常用修改', $el, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').css(' + html + ')';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('sty', html);
                    });
                    break;
                case 'edit-sty':
                    popMenu.show('edit-sty', '编辑样式', $el, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').css(' + html + ')';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('sty', html);
                    });
                    break;
                case 'edit-move':
                    popMenu.show('edit-move', '移动', $el, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').offset({\'top\':' + html.top + ',\'left\':' + html.left +
                                '})';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('move', html);
                    });
                    break;
                case 'edit-drag':
                    popMenu.show('edit-drag', '调整大小', $el, function (flag, html) {
                        if (flag) {
                            var code = '$(\'' + el_slt + '\').width( ' + html.width + ' );';
                            code += '$(\'' + el_slt + '\').height( ' + html.height + ' );';
                            dataCtl.addCode({
                                slt: el_slt,
                                code: code
                            });
                        } else {
                            dataCtl.resetCurrent();
                        }
                    }, function (html) {
                        dataCtl.changeElTemp('drag', html);
                    });
                    break;
                case 'target':
                    // goalEditor.show({
                    //  slt:el_slt
                    // })
                    break;
                case 'edit-del':
                    var code = '$(\'' + el_slt + '\').remove()';
                    dataCtl.addCode({
                        slt: el_slt,
                        code: code
                    });
                    break;
                case 'edit-hide':
                    var code = '$(\'' + el_slt + '\').addClass(\'_abt_hidden\')';
                    dataCtl.addCode({
                        slt: el_slt,
                        code: code
                    });
                    break;
                case 'edit-show':
                    var code = '$(\'' + el_slt + '\').removeClass(\'_abt_hidden\')';
                    dataCtl.addCode({
                        slt: el_slt,
                        code: code
                    });
                    break;
                case 'edit-addgoal':
                    goalEditor.show({
                        slt: el_slt
                    });
                    break;
                case 'edit-editgoal':
                    GoalManager.show();
                    //                        var goal = getSelectedGoal(el_slt);
                    //                        goalEditor.show({}, goal, function () {
                    //                            GoalManager.show();
                    //                        }, true);
                    break;
                }
 
 
 
                return false;
            });
 
            var getSubEditGoal = function (slt) {
                var list = editorConf.goalList.normalGoalData;
                var html = [];
                var item;
                for (var i = 0; i < list.length; i++) {
                    item = list[i];
                    if (item.goalSelector == slt) {
                        html.push('<div class="item" gid="' + item.goalId + '" t="edit">编辑目标[' + item.goalName +
                            ']</div>');
                    }
                }
 
                return html.join('');
            }
 
            $menu.delegate('.sub_m', 'mouseenter', function () {
                var $this = $(this);
                var t = $this.attr('type');
                var $sub = $this.find('.menu_sub');
                var timer = $this.data('timer');
                clearTimeout(timer);
                var $el = editorConf.selectEle;
                var el_slt = getEleSelector($el);
                switch (t) {
                case 'target':
                    $sub.find('.list').html('<div class="item" t="add">添加目标</div>' + getSubEditGoal(el_slt));
                    var l = $this.outerWidth();
                    var t = ($sub.outerHeight() - $this.outerHeight()) / 2;
                    $sub.css({
                        left: l + 28,
                        top: -t
                    }).show();
                    if (($menu.outerWidth() + $sub.outerWidth() + $menu.offset().left) > $(window).width()) {
                        $menu.css({
                            left: $(window).width() - $menu.outerWidth() - $sub.outerWidth() - 10
                        });
                    }
                    break;
                }
 
            }).delegate('.sub_m', 'mouseleave', function () {
                var $this = $(this);
                var timer = $this.data('timer');
                clearTimeout(timer);
                timer = setTimeout(function () {
                    $this.find('.menu_sub').hide();
                }, 300);
                $this.data('timer', timer);
            }).delegate('.sub_m .list .item', 'click', function () {
                var $this = $(this);
                var type = $this.parents('.sub_m').attr('type');
                var $el = editorConf.selectEle;
                var el_slt = getEleSelector($el);
                switch (type) {
                case 'target':
                    var t = $this.attr('t');
                    switch (t) {
                    case 'add':
                        goalEditor.show({
                            slt: el_slt
                        });
                        break;
                    case 'edit':
                        var gid = $this.attr('gid');
                        var goal = editorConf.goalData[gid];
                        goalEditor.show({
                            slt: el_slt
                        }, goal);
                        break;
                    }
                    break;
                }
                // return false;
            })
        }
 
        var show = function (info) {
            var $el;
            var selector;
            var $doc;
            if (info) {
                $el = info.selector;
                selector = info.selector;
                $doc = info.doc;
            } else {
                $el = editorConf.selector;
                selector = editorConf.selector;
                $doc = editorConf.doc;
            }
 
            var offset = $el.offset();
            var left = offset.left + $el.outerWidth();
            var top;
            if (offset.top < $doc.scrollTop()) {
                top = (offset.top - $doc.scrollTop() + $el.outerHeight()) / 2 + editorConf.fixTop;
            } else {
                top = offset.top + $el.outerHeight() / 2 + editorConf.fixTop - $doc.scrollTop();
            }
 
            var $win = $(window);
            if ($menu.outerWidth() + left > $win.width()) {
                left = $win.width() - $menu.outerWidth() - 10;
            }
 
            top = top - $menu.height() / 2;
            if (top + $menu.outerHeight() > $win.height()) {
                top = $win.height() - $menu.outerHeight();
            }
 
            $menu.find('.title').text('元素<' + editorConf.selectEle.prop('tagName').toLowerCase() + '>');
            var version = dataCtl.getCurVersion();
            checkValid(editorConf.selectEle);
            if (version.type != 2) {
                $menu.find('.medit').addClass('disabled');
                $menu.find('.medit').addClass('dis');
            } else {
                $menu.find('.medit').removeClass('disabled');
                $menu.find('.medit').removeClass('dis');
            }
            $menu.css({
                left: left,
                top: top
            }).show();
            editorConf.isdropmenu = true;
            checkMenuForHide($menu, function () {
                editorConf.isdropmenu = false;
                if (!$menu.data('isclick')) {
                    editorConf.selectEle = null;
                    selector.hide();
                    selector.removeClass('medialab-abtest-selector-on');
                    $menu.data('isclick', 0);
                }
            });
        }
 
 
 
        init();
        return {
            show: show
        }
    })();
 
    var fixPopDom = function ($codeDom, $slt) {
        $slt = $slt || editorConf.selector;
        var version = dataCtl.getCurVersion();
        if (version.type == 3) {
            $slt = version.ctl.getSelector();
        }
 
        var $el = editorConf.selectEle;
        var offset = $slt.offset();
        var $win = $(window);
        var left = offset.left;
        var top;
        if (offset.top < editorConf.doc.scrollTop()) {
            top = (offset.top - editorConf.doc.scrollTop() + $slt.outerHeight()) / 2 + editorConf.fixTop;
        } else {
            top = offset.top - editorConf.doc.scrollTop() + $slt.outerHeight() / 2 + editorConf.fixTop;
        }
 
        if (left > $win.width() / 2) {
            left = left - $codeDom.outerWidth();
            if (left < 0) {
                left = 0;
            }
        } else {
            left = left + $slt.outerWidth();
            if (left + $codeDom.outerWidth() > $win.width()) {
                left = $win.width() - $codeDom.outerWidth();
            }
        }
 
        top = top - $codeDom.outerHeight() / 2;
        if (top < 0) {
            top = 0;
        }
 
        if (top + $codeDom.outerHeight() > $win.height()) {
            top = $win.height() - $codeDom.outerHeight();
        }
 
        $codeDom.css({
            left: left,
            top: top
        })
    }
 
 
    //代码编辑器
    var codeEditor = (function () {
        var callback;
        var onCodeChange;
        var $codeDom = $('#code_editer');
        var editor = CodeMirror($codeDom.find('#code_area').get(0), {
            mode: "text/html",
            lineNumbers: true,
            lineWrapping: true,
            value: ''
        });
        editor.setSize(500, 250);
        editor.on('change', function () {
            onCodeChange && onCodeChange(editor.getValue());
        });
        var show = function (title, content, cb, onChange) {
 
            fixPopDom($codeDom);
            callback = cb;
            $codeDom.show();
            editorConf.editing = true;
            $codeDom.find('.dialog_title').html(title || '编辑');
            editor.setValue(content || '');
            // editor.focus();
            editor.refresh();
            onCodeChange = onChange;
            if (window._zqDragable) {
                _zqDragable($codeDom.get(0));
            }
        }
 
        var init = function () {
            $codeDom.delegate('.ok_btn', 'click', function () {
                callback && callback(1, editor.getValue());
                editorConf.dropMenu.data('isclick', 0);
                editorConf.editing = false;
                onCodeChange = null;
                $codeDom.hide();
                return false;
            }).delegate('.cancel_btn', 'click', function () {
                callback && callback(0);
                editorConf.editing = false;
                onCodeChange = null;
                editorConf.dropMenu.data('isclick', 0);
                $codeDom.hide();
                return false;
            })
        }
 
        init();
        return {
            show: show
        }
 
    })();
    //menu弹出框
    var popMenu = (function () {
        var callback;
        var onCodeChange;
        var $popDom;
        var menuType;
        var currentContent;
        var styles = ['font-family',
            'font-size',
            'font-weight',
            'font-style',
            'color',
            'text-decoration',
            'text-align',
            'line-height',
            'background-image',
            'background-color',
            'background-repeat',
            'background-position',
            'background-size',
            'background-attachment',
            'opacity',
            'width',
            'height',
            'left',
            'right',
            'top',
            'bottom',
            'margin-left',
            'margin-right',
            'margin-top',
            'margin-bottom',
            'padding-left',
            'padding-right',
            'padding-top',
            'padding-bottom',
            'box-shadow',
            'border-left-width',
            'border-left-color',
            'border-left-style',
            'border-top-width',
            'border-top-color',
            'border-top-style',
            'border-right-width',
            'border-right-color',
            'border-right-style',
            'border-bottom-width',
            'border-bottom-color',
            'border-bottom-style',
            'border-radius',
            'position',
            'display',
            'visibility',
            'z-index',
            'overflow-x',
            'overflow-y',
            'white-space',
            'clip',
            'float',
            'clear',
            'cursor',
            'list-style-image',
            'list-style-position',
            'list-style-type',
            'marker-offset'
        ];
        var commonStyles = [
            'font-size',
            'color',
            'background-image',
            'background-color'
        ];
        var show = function (type, title, content, cb, onChange) {
            menuType = type;
 
            if (type == 'edit-htm') {
                $popDom = $(".edit-text")
            } else {
                $popDom = $("." + type);
            }
 
            fixPopDom($popDom);
            callback = cb;
            init();
            setValue(content);
            $popDom.show();
            editorConf.editing = true;
            $popDom.find('.title').html(title || '编辑');
            // editor.focus();
            onCodeChange = onChange;
            currentContent = content;
            if (window._zqDragable) {
                _zqDragable($popDom.get(0));
            }
        }
 
            function setValue(content) {
                switch (menuType) {
                case 'edit-text':
                    var ue = $popDom.find('.editor');
                    ue.val(content);
                    break;
                case 'edit-htm':
                    // var ue = UE.getEditor('container');
                    // ue.setContent(content);
                    var ue = $popDom.find('.editor');
                    ue.val(content);
                    break;
                case 'edit-img':
                    var src = $popDom.find('input');
                    src.val(content);
                    break;
                case 'edit-url':
                    var text = $popDom.find('input.edit-url-name');
                    var link = $popDom.find('input.edit-url-link');
                    text.val(content.html());
                    link.val(content.attr('href'));
                    if (content.attr("target") == "_blank") {
                        $popDom.find(".ck-bg").text("√");
                    } else {
                        $popDom.find(".ck-bg").text("");
                    }
                    break;
                case 'edit-sty':
                    for (var i in styles) {
                        var styleTmp = content.css(styles[i]);
                        if (styles[i] == 'color' || styles[i] == 'background-color') {
                            $popDom.find('input:text.sty-' + styles[i]).val(styleTmp);
                            styleTmp = styleTmp.colorHex();
                            // comStyleTmp = comStyleTmp == 'transparent' ? '#ffffff' : comStyleTmp;
                            $popDom.find('input[type="color"].sty-' + styles[i]).val(styleTmp);
                        } else {
                            // if (commonStyles[i] == 'color') {
                            //     comStyleTmp = comStyleTmp.colorHex();
                            // }
                            $popDom.find('input.sty-' + styles[i]).val(styleTmp);
                        }
                    }
                    break;
                case 'edit-common':
                    for (var i in commonStyles) {
                        var comStyleTmp = content.css(commonStyles[i]);
                        if (commonStyles[i] == 'color' || commonStyles[i] == 'background-color') {
                            $popDom.find('input:text.com-' + commonStyles[i]).val(comStyleTmp);
                            comStyleTmp = comStyleTmp.colorHex();
                            // comStyleTmp = comStyleTmp == 'transparent' ? '#ffffff' : comStyleTmp;
                            $popDom.find('input[type="color"].com-' + commonStyles[i]).val(comStyleTmp);
                        } else {
                            // if (commonStyles[i] == 'color') {
                            //     comStyleTmp = comStyleTmp.colorHex();
                            // }
                            $popDom.find('input.com-' + commonStyles[i]).val(comStyleTmp);
                        }
                    }
                    break;
                case 'edit-move':
                    var posInfo = content.css("position");
                    if (!posInfo || posInfo == 'static') {
                        content.css("position", "relative");
                    }
                    editorConf.selector.dragDrop({
                        position: 'absolute',
                        selectEle: editorConf.selectEle,
                        callback: function (params) {
                            editorConf.selectEle.css('top', (params.moveY - params.topRaw + params.posY) + 'px');
                            editorConf.selectEle.css('left', (params.moveX - params.leftRaw + params.posX) + 'px');
                        }
                    });
                    break;
                case 'edit-drag':
                    editorConf.selector.dragDivResize({
                        callback: function (w, h) {
                            editorConf.selectEle.width(w + 'px');
                            editorConf.selectEle.height(h + 'px');
                        }
                    });
                    break;
                }
            }
 
            function getValue() {
                switch (menuType) {
                case 'edit-text':
                    var ue = $popDom.find('.editor');
                    return ue.val();
                    break;
                case 'edit-htm':
                    // var ue = UE.getEditor('container');
                    // return  ue.getContent();
                    var ue = $popDom.find('.editor');
                    return ue.val();
                    break;
                case 'edit-img':
                    var src = $popDom.find('input');
                    return src.val();
                    break;
                case 'edit-url':
                    var text = $popDom.find('input.edit-url-name');
                    var link = $popDom.find('input.edit-url-link');
                    if ($popDom.find(".ck-bg").text() == "√") {
                        return [text.val(), link.val(), 1];
                    } else {
                        return [text.val(), link.val(), 0];
                    }
                    break;
                case 'edit-move':
                    return currentContent.offset();
                    break;
                case 'edit-drag':
                    return {
                        width: currentContent.width(),
                        height: currentContent.height()
                    };
                    break;
                case 'edit-sty':
                    var ret = "{";
                    for (var i in styles) {
                        var styleTmp = $popDom.find('input.sty-' + styles[i]).val();
                        if (styles[i] == 'color' || styles[i] == 'background-color') {
                            styleTmp = $popDom.find('input:text.sty-' + styles[i]).val();
                        }
                        if (styles[i] == 'background-image') {
                            styleTmp = styleTmp.replace(/\"/g, '');
                        }
                        ret += "\"" + styles[i] + '\":\"' + styleTmp + "\",";
                    }
                    ret = ret.slice(0, -1);
                    ret += "}";
                    return ret;
                    break;
                case 'edit-common':
                    var ret = "{";
                    for (var i in commonStyles) {
                        var comStyleTmp = $popDom.find('input.com-' + commonStyles[i]).val();
                        if (commonStyles[i] == 'color' || commonStyles[i] == 'background-color') {
                            comStyleTmp = $popDom.find('input:text.com-' + commonStyles[i]).val();
                        }
                        if (commonStyles[i] == 'background-image') {
                            styleTmp = comStyleTmp.replace(/\"/g, '');
                        }
                        ret += "\"" + commonStyles[i] + '\":\"' + comStyleTmp + "\",";
                    }
                    ret = ret.slice(0, -1);
                    ret += "}";
                    return ret;
                    break;
                }
            }
 
 
        var init = function () {
            $popDom.delegate('.btn-ok', 'click', function () {
                callback && callback(1, getValue());
                $popDom.undelegate('.btn-ok', 'click');
                editorConf.dropMenu.data('isclick', 0);
                editorConf.editing = false;
                onCodeChange = null;
                $popDom.hide();
                return false;
            }).delegate('.btn-cancel,.close', 'click', function () {
                callback && callback(0);
                $popDom.undelegate('.btn-cancel,.close', 'click');
                editorConf.editing = false;
                onCodeChange = null;
                editorConf.dropMenu.data('isclick', 0);
                $popDom.hide();
                return false;
            })
        }
 
        return {
            show: show
        }
 
    })();
    var GoalManager = new Dialog({
        html: '<div style="display:none;min-width:300px;width:665px;" class="dialog">\
                        <div class="dialog_title">管理目标</div>\
                        <div class="content goal_mgr">\
                            <div class="tab_nav">\
                                <div t="1" class="nav on">页面类目标</div>\
                                <div t="2" class="nav">KPI类目标</div>\
                            </div>\
                            <div class="tab on" t="1">\
                                <div class="tab_top">\
                                    <div class="fl total_n">共个</div>\
                                    <div class="fr"><div class="btn add">添加目标</div></div>\
                                </div>\
                                <table class="l_table">\
                                    <colgroup>\
                                        <col style="width:60px;">\
                                        <col style="width:220px;">\
                                        <col style="width:120px;">\
                                        <col style="width:240px;">\
                                    </colgroup>\
                                    <thead>\
                                        <tr>\
                                            <th>序号</th>\
                                            <th>目标名称</th>\
                                            <th>目标类型</th>\
                                            <th>操作</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody id="list">\
                                    </tbody>\
                                </table>\
                                <div class="none_txt"></div>\
                            </div>\
                            <div class="tab" t="2">\
                                <div class="tab_top">\
                                    <div class="fl total_n">共个</div>\
                                    <div class="fr"><div class="btn add">添加目标</div></div>\
                                </div>\
                                <table class="l_table">\
                                    <colgroup>\
                                        <col style="width:60px;">\
                                        <col style="width:320px;">\
                                        <col style="width:260px;">\
                                    </colgroup>\
                                    <thead>\
                                        <tr>\
                                            <th>序号</th>\
                                            <th>目标名称</th>\
                                            <th>操作</th>\
                                        </tr>\
                                    </thead>\
                                    <tbody id="list">\
                                    </tbody>\
                                </table>\
                                <div class="none_txt"></div>\
                            </div>\
                        </div>\
                        <div class="dialog_cls"></div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            var goalItemInfo = {};
            dialog.renderKpiGoal = function (i, item) {
                goalItemInfo[item.goalId] = item;
                var name = $.TypeHelper.getKpiGoalTypes(item.goalTypeId).value;
                var html = '<tr seq="' + i + '" >\
                                <td>' + (i) + '</td>\
                                <td class="' + (item.isMaster ==
                    1 ? 'red' : '') + '">' + name + '<i t="' + goalTip[name] +
                    '" class="qus"></i></td>\
                                <td class="ctls" gid="' + item.goalId +
                    '">\
                                    <a href="#" t="del" class="ctl">删除</a>';
                if (item.isMaster == 1) {
                    html += '<a href="#" t="cancelmain" g="2" class="ctl">取消主追踪</a>';
                } else {
                    html += '<a href="#" t="setmain" g="2" class="ctl">设为主追踪目标</a>';
                }
 
                html += '</td>\
                            </tr>';
                return html
            }
 
            var goalTip = {
                '注册数': '手机号验证成功人数',
                '提交订单数': '点击自助付费产品的“立即开通”按钮的人数',
                '成功订单数': '成功付费购买网站自助类产品的人数',
                '自助收入': '网站自助类产品收入总和，不扣除广告费等成本'
            }
 
            dialog.renderNormalGoal = function (i, item) {
                goalItemInfo[item.goalId] = item;
                var name = $.TypeHelper.getNormalGoalTypes(item.goalTypeId).value;
                var html = '<tr seq="' + i + '">\
                        <td>' + i + '</td>\
                        <td class="' + (item.isMaster == 1 ?
                    'red' : '') + '">' + item.goalName + '</td>\
                        <td>' + name +
                    '</td>\
                        <td class="ctls" gid="' + item.goalId +
                    '">\
                            <a href="#" t="edit" class="ctl">编辑</a>\
                            <a href="#" t="del" class="ctl">删除</a>';
                if (item.isMaster == 1) {
                    html += '<a href="#" t="cancelmain" g="1" class="ctl">取消主追踪</a>';
                } else {
                    html += '<a href="#" t="setmain" g="1" class="ctl">设为主追踪目标</a>';
                }
 
                html += '</td>\
                    </tr>';
                return html;
            }
 
            $dom.find('#list').delegate('.qus', 'mouseenter', function () {
                $.Dialog.conTip.show($(this), $(this).attr('t'));
            }).delegate('.qus', 'mouseleave', function () {
                $.Dialog.conTip.hide();
            })
 
            $dom.find('#list').delegate('.ctl', 'click', function () {
                var $this = $(this);
                var t = $this.attr('t');
                var gid = $this.parent().attr('gid');
                var goal = editorConf.goalData[gid];
                switch (t) {
                case 'edit':
                    goalEditor.show({}, goal, function () {
                        GoalManager.show();
                    }, true);
                    break;
                case 'del':
                    if (!checkCanSave()) {
                        return false;
                    }
 
                    $.Dialog.confirm({
                        content: '确认要删除目标“' + goal.goalName + '”?',
                        callback: function (flag) {
                            if (flag) {
                                $.postEx(MAIN_URL, '/doDeleteGoal', {
                                    goalId: gid,
                                    caseId: goal.caseId
                                }, function (data) {
                                    if (data.result == 'success') {
                                        $.Dialog.showPopTip('删除成功');
                                        setTimeout(function () {
                                            GoalManager.show();
                                        }, 1);
                                    } else {
                                        $.Dialog.showPopTip(data.errMsg);
                                    }
                                })
                            } else {
                                setTimeout(function () {
                                    GoalManager.show();
                                }, 1);
                            }
                        }
                    })
                    break;
                case 'setmain':
                    if (!checkCanSave()) {
                        return false;
                    }
 
                    $.postEx(MAIN_URL, '/doSetMasterGoal', {
                        goalId: gid,
                        caseId: goal.caseId
                    }, function (data) {
                        if (data.result == 'success') {
                            $.Dialog.showPopTip('设置主追踪成功');
                            var $tr = $this.parents('tr');
                            var goal = goalItemInfo[gid];
                            goal.isMaster = 1;
                            $dom.find('.tab #list').find('a.ctl[t=cancelmain]').each(function () {
                                $(this).attr('t', 'setmain').html('设为主追踪目标');
                                $(this).parents('tr').find('.red').removeClass('red');
                            });
                            if ($this.attr('g') == 1) {
                                $tr.replaceWith(dialog.renderNormalGoal($tr.attr('seq'), goal));
                            } else {
                                $tr.replaceWith(dialog.renderKpiGoal($tr.attr('seq'), goal));
                            }
 
                        } else {
                            $.Dialog.showPopTip(data.errMsg);
                        }
                    });
                    break;
                case 'cancelmain':
                    if (!checkCanSave()) {
                        return false;
                    }
 
                    $.postEx(MAIN_URL, '/doCancelMasterGoal', {
                        goalId: gid,
                        caseId: goal.caseId
                    }, function (data) {
                        if (data.result == 'success') {
                            $.Dialog.showPopTip('取消主追踪成功');
                            var $tr = $this.parents('tr');
                            var goal = goalItemInfo[gid];
                            goal.isMaster = 0;
                            if ($this.attr('g') == 1) {
                                $tr.replaceWith(dialog.renderNormalGoal($tr.attr('seq'), goal));
                            } else {
                                $tr.replaceWith(dialog.renderKpiGoal($tr.attr('seq'), goal));
                            }
                        } else {
                            $.Dialog.showPopTip(data.errMsg);
                        }
                    });
                    break;
                }
                return false;
            });
            $dom.delegate('.add', 'click', function () {
                var t = $(this).parents('.tab').attr('t');
                if (t == 1) {
                    goalEditor.show({}, null, function (flag) {
                        GoalManager.show();
                    }, true);
                } else {
                    kpiEditor.show({
                        cb: function () {
                            GoalManager.show();
                        }
                    });
                }
 
                return false;
            });
            $dom.find('.tab_nav').delegate('.nav', 'click', function () {
                var $this = $(this);
                var t = $this.attr('t');
                t = parseInt(t);
                t = t - 1;
                $dom.find('.tab_nav').find('.nav').removeClass('on');
                $this.addClass('on');
                $dom.find('.tab').removeClass('on').eq(t).addClass('on');
                return false;
            });
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
 
            function showList($tab, list, render) {
                var html = [];
                var item;
                for (var i = 0; i < list.length; i++) {
                    item = list[i];
                    html.push(render(i, item));
                }
 
                if (html.length > 0) {
                    $tab.find('#list').html(html.join(''));
                    $tab.find('.none_txt').hide();
                } else {
                    $tab.find('#list').html('');
                    $tab.find('.none_txt').html('还没有添加目标，<a href="#" class="btn add">立即添加</a>').show();
                }
 
                $tab.find('.total_n').html('共' + list.length + '个');
            }
 
 
 
            function showData() {
                var list = editorConf.goalList;
                showList($dom.find('.tab').eq(0), list.normalGoalData, function (i, item) {
                    return dialog.renderNormalGoal(i + 1, item);
                });
                showList($dom.find('.tab').eq(1), list.kpiGoalData, function (i, item) {
                    return dialog.renderKpiGoal(i + 1, item);
                });
                $.Dialog.autoFix();
            }
 
            $dom.find('#list').html('');
            $dom.find('.none_txt').html('正在加载...').show();
            dataCtl.getGoalList(function () {
                showData();
            });
        }
    });
    $.getGoalType = function (type) {
        switch (type) {
        case 'click':
            return '点击';
        }
    }
 
    var checkCanSave = function () {
        if (!$.Login.isLogin()) {
            $.Dialog.confirm({
                content: '请先登录',
                ok_txt: '立即登录',
                callback: function (flag) {
                    if (flag) {
                        location.href = MAIN_URL + '/v/login';
                    }
                }
            })
            return false;
        }
 
        if (editorConf.caseData.caseStatus != 0) {
            $.Dialog.alert('您好，当前实验已经开始测试，为了保证数据的准确性，您所做的一切修改均不能保存。');
            return false;
        }
 
        return true;
    }
 
    var addUrlVersion = new Dialog({
        html: '<div style="display:none;min-width:300px;" class="dialog">\
                        <div class="dialog_title">添加URL分离版本</div>\
                        <div class="content">\
                            <div class="s_form">\
                                <div class="item">\
                                    <div class="l">版本名称：</div>\
                                    <div class="r"><input id="name" type="text" style="width:350px;" /></div>\
                                </div>\
                                <div class="item">\
                                    <div class="l">分离URL：</div>\
                                    <div class="r"><input id="link" type="text" style="width:350px;"/></div>\
                                </div>\
                                <div class="item" style="margin-top: -8px; padding-left: 10px;">\
                                    <p style="color:red;">*此版本元素不可编辑</p>\
                                </div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">添加</a>\
                            <a href="#" class="btn cancel_btn">取消</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.delegate('.ok_btn', 'click', function () {
                var name = $dom.find('#name').val().trim();
                var url = $dom.find('#link').val().trim();
                if (name == '') {
                    $.Dialog.showPopTip('请输入名称');
                    return false;
                }
 
                if (url == '' || url == 'http://') {
                    $.Dialog.showPopTip('请输入正确的网址');
                    return false;
                }
 
                if (!$.IsURL(url)) {
                    $.Dialog.showPopTip('请输入正确的网址');
                    return false;
                }
 
                dialog.info.cb({
                    name: name,
                    url: url
                });
                dialog.hide();
                return false;
            })
 
 
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var info = dialog.info;
            $dom.find('#name').val(info.name);
            $dom.find('#link').focus().val('http://');
        }
    })
 
    //目标编辑器
    var goalEditor = (function () {
        var callback;
        var $popDom = $('#goal_editor');
        var init = function () {
            $popDom.delegate('.cancel_btn', 'click', function () {
                hide();
                callback && callback(0);
                return false;
            }).delegate('._zq-closeme', 'click', function () {
                hide();
                return false;
            }).delegate('.ok_btn', 'click', function () {
                // editorConf.editing = false;
                // callback && callback(1);
                // $popDom.hide();
                var name = $popDom.find('#goal_name').val().trim();
                var type = $popDom.find('#goal_type').val().trim();
                var isMaster = $('#_zq-set_as_main', $popDom).is(':checked') ? 1 : 0;
                var selector = $popDom.find('#goal_slt').val().trim();
                if (name == '') {
                    $.Dialog.showPopTip('请输入目标名称');
                    return false;
                }
 
                if (selector == '') {
                    $.Dialog.showPopTip('请输入目标元素');
                    return false;
                }
 
                var version = dataCtl.getCurVersion();
                if (version.type == 3) {
                    if (version.ctl.getDoc().find(selector).length == 0) {
                        $.Dialog.showPopTip('找不到目标元素');
                        return false;
                    }
                } else {
                    if (editorConf.doc.find(selector).length == 0) {
                        $.Dialog.showPopTip('找不到目标元素');
                        return false;
                    }
                }
 
                if (!checkCanSave()) {
                    return false;
                }
 
 
                var params = {
                    gName: name,
                    gTypeId: type,
                    gSelector: selector,
                    caseId: editorConf.caseData.caseId,
                    gBuizType: 1,
                    projectId: editorConf.caseData.projectId,
                    isMaster: isMaster
                }
 
                if (curGoal) {
                    params.goalId = curGoal.goalId;
                }
 
                $.postEx(MAIN_URL, '/doSaveGoal', params, function (data) {
                    if (data.result == 'success') {
                        if (curGoal) {
                            $.Dialog.showPopTip('修改目标成功');
                        } else {
                            $.Dialog.showPopTip('添加目标成功');
                        }
 
                        dataCtl.getGoalList();
                        dataCtl.setChange(true);
                        hide();
                        callback && callback(1);
                    } else {
                        $.Dialog.showPopTip(data.errMsg);
                    }
                })
 
                return false;
            })
        }
 
        var hide = function () {
            editorConf.dropMenu.data('isclick', 0);
            editorConf.editing = false;
            $popDom.hide();
        }
 
        var curGoal;
        var show = function (data, goal, cb, pop) {
            if (pop) {
                $.Dialog.show($popDom);
            } else {
                fixPopDom($popDom);
            }
 
            var list = $.TypeHelper.getNormalGoalTypes();
            var html = [];
            for (var i = 0, item; i < list.length; i++) {
                item = list[i];
                html.push('<option value="' + item.key + '">' + item.value + '</option>')
            }
            $popDom.find('#goal_type').html(html.join(''));
            callback = cb;
            curGoal = goal;
            editorConf.editing = true;
            $popDom.show();
            if (goal) {
                $popDom.find('#goal_name').val(goal.goalName);
                $popDom.find('#goal_slt').val(goal.goalSelector);
                $popDom.find('#goal_type').val(goal.goalType);
                $popDom.find('#add_tip').hide();
            } else {
                $popDom.find('#goal_name').val(data.name || '');
                // $popDom.find('#goal_type').val(data.type||'');
                $popDom.find('#goal_slt').val(data.slt || '');
                if (data.slt) {
                    $popDom.find('#add_tip').hide();
                } else {
                    $popDom.find('#add_tip').show();
                }
 
            }
            if (window._zqDragable) {
                _zqDragable($popDom.get(0));
            }
            $('#_zq-set_as_main:checked', $popDom).click();
        }
 
        init();
        return {
            show: show
        };
    })();
    var kpiEditor = new Dialog({
        html: '<div class="dialog" id="goal_editor" style="display:none;">\
                    <div class="dialog_title">添加KPI类目标</div>\
                    <div class="content goal_set">\
                        <div class="s_form">\
                            <div class="item">\
                                <div class="l">目标名称：</div>\
                                <div class="r"><select name="" id="goal_type"></select></div>\
                            </div>\
                            <div class="item">\
                                <div class="l"></div>\
                                <div class="r">\
                                    <input type="checkbox" id="_zq-set_as_main" style="vertical-align:middle;display:inline">\
                                    <label>作为主目标（会替换已经设置的主目标）？</label>\
                                </div>\
                            </div>\
                        </div>\
                    </div>\
                    <div class="ctl_wrap">\
                        <span class="btn ok_btn">确定</span>\
                        <span class="btn cancel_btn">取消</span>\
                    </div>\
                </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            var list = $.TypeHelper.getKpiGoalTypes();
            var html = ['<option value="0">请选择KPI目标</option>'];
            for (var i = 0, item; i < list.length; i++) {
                item = list[i];
                html.push('<option value="' + item.key + '">' + item.value + '</option>')
            }
            $dom.find('#goal_type').html(html.join(''));
            $dom.delegate('.ok_btn', 'click', function () {
                var type = $dom.find('#goal_type').val();
                var isMaster = $('#_zq-set_as_main', $dom).is(':checked') ? 1 : 0;
                if (type == 0) {
                    $.Dialog.showPopTip('请选择KPI目标');
                    return false;
                }
 
                if (!checkCanSave()) {
                    return false;
                }
 
                var params = {
                    gName: $.TypeHelper.getKpiGoalTypes(type).value,
                    gTypeId: type,
                    caseId: editorConf.caseData.caseId,
                    gBuizType: 2,
                    projectId: editorConf.caseData.projectId,
                    isMaster: isMaster
                }
 
                $.postEx(MAIN_URL, '/doSaveGoal', params, function (data) {
                    if (data.result == 'success') {
                        dialog.hide();
                        $.Dialog.showPopTip('添加KPI目标成功');
                        dialog.info.cb && dialog.info.cb();
                    } else {
                        $.Dialog.showPopTip(data.errMsg);
                    }
 
                })
 
                return false;
            })
 
        },
        onClose: function (dialog) {
            dialog.info.cb && dialog.info.cb();
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            $dom.find('#goal_type').val(0);
            if (window._zqDragable) {
                _zqDragable($dom.get(0));
            }
            $('#_zq-set_as_main:checked', $dom).click();
        }
    })
 
    var ProjCode = new Dialog({
        html: '<div style="display:none;min-width:300px;" class="dialog">\
                        <div class="dialog_title">实验代码</div>\
                        <div class="content">\
                            <div class="setting_wrap" style="padding-top:0px;">\
                                <div class="top_tl">请将以下代码拷贝至网页的<head>与</head>之间或紧跟在<body>之后的位置</div>\
                                <div>\
                                    <textarea readonly class="inp" style="width:700px;height:40px;" id="proj_code-2"></textarea>\
                                    <input type="button" value="复制" id="_zq-btn-copy-proj_code-2" class="_zq-docopy">\
                                </div>\
                                <div class="_zq-copy-success-tip-2" style="display:none;">内容已经复制到剪贴板</div>\
                                <div class="orange">注：请将代码嵌入至控制版本中，如果采用URL版本，URL版本中也需嵌入代码</div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">确定</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.find('#proj_code').click(function () {
                $(this).setSelect(0, 1000);
            });
            $dom.delegate('.ok_btn', 'click', function () {
                dialog.hide();
                return false;
            });
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var code = '<script src="' + DATA_HOST + '/abtest/code/' + editorConf.caseData.projectId +
                '/stat.js"></script>';
            $dom.find('#proj_code-2').val(code);
            $dom.show();
            $('#_zq-btn-copy-proj_code-2').data('zclipId') || $('#_zq-btn-copy-proj_code-2').zclip({
                path: EDITOR_URL + '/assets/js/ZeroClipboard.swf',
                copy: function () {
                    return $('#proj_code-2').val();
                },
                afterCopy: function () {
                    $('._zq-copy-success-tip-2').show();
                    setTimeout(function () {
                        $('._zq-copy-success-tip-2').hide(1000);
                    }, 2000)
                }
            });
        }
    });
 
    function dealStatus(op) {
        if (op) {
            var ops = {
                '-1': 'archive',
                '2': 'start',
                '1': 'pause',
                '0': 'draft'
            };
            if (ops[op]) {
                op = ops[op];
            }
            if ('draft' != op) {
                $('#start_lab .con').html('').removeClass('green');
                $('#start_lab').removeClass('green').addClass('_zq-icon');
            }
            $('._zq-exprm-status').attr('status', op);
            switch (op) {
            case 'start':
                $('._zq-exprm-status .btn').hide();
                $('#_zq-pause-exprm,#_zq-status-started,#_zq-archive-exprm').show();
                $('#_zq-archive-exprm').addClass('disabled');
                break;
            case 'pause':
                $('._zq-exprm-status .btn').hide();
                $('#_zq-status-paused,#_zq-archive-exprm,#start_lab').show();
                $('#_zq-archive-exprm').removeClass('disabled');
                break;
            case 'archive':
                $('._zq-exprm-status .btn').hide();
                $('#_zq-status-archived').show();
                break;
            default:
                $('._zq-exprm-status .btn').hide();
                $('#start_lab').show()
                break;
            }
            return;
        }
        $('._zq-exprm-status').delegate('.btn', 'click', function () {
            var $this = $(this);
            var c = $this.attr('c');
            switch (c) {
            case 'start':
                var params = {
                    projectId: editorConf.caseData.projectId,
                    caseId: editorConf.caseData.caseId
                }
                $this.addClass('doing');
                $.postEx(MAIN_URL, '/runCase', params, function (data) {
                    if (data.result == 'success') {
                        $.Dialog.showPopTip('启动试验成功');
                        editorConf.caseData.caseStatus = 2;
                        dealStatus('start');
                    } else {
                        $.Dialog.showPopTip(data.errMsg);
                    }
                    $this.removeClass('doing');
                })
                break;
            case 'pause':
                $.Dialog.confirm({
                    content: '确认要暂停实验“' + editorConf.caseData.caseName + '”？',
                    callback: function (flag) {
                        if (flag) {
                            var params = {
                                projectId: editorConf.caseData.projectId,
                                caseId: editorConf.caseData.caseId
                            }
 
                            $this.addClass('doing');
                            $.postEx(MAIN_URL, '/stopCase', params, function (data) {
                                if (data.result == 'success') {
                                    $.Dialog.showPopTip('暂停试验成功');
                                    editorConf.caseData.caseStatus = 1;
                                    dealStatus('pause');
                                } else {
                                    $.Dialog.showPopTip(data.errMsg);
                                }
                                $this.removeClass('doing');
                            })
                        }
                    }
                })
                break;
            case 'archive':
                $('#_zq-archive-exprm').hasClass('disabled') ||
                    $.Dialog.confirm({
                    content: '注意，归档后的实验无法再次运行，确认要归档实验“' + editorConf.caseData.caseName + '”?',
                    callback: function (flag) {
                        if (flag) {
                            var params = {
                                caseId: editorConf.caseData.caseId
                            }
                            $.postEx(MAIN_URL, '/doDeleteCase', params, function (data) {
 
                                if (data.result == 'success') {
                                    $.Dialog.showPopTip('归档成功');
                                    dealStatus('archive');
                                } else {
                                    $.Dialog.showPopTip(data.errMsg);
                                }
 
                            })
                        }
                    }
                });
                break;
            default:
                break;
            }
        });
    }
 
    var StarLab = new Dialog({
        html: '<div style="display:none;min-width:300px;" class="dialog">\
                        <div class="dialog_title">温馨提醒</div>\
                        <div class="content">\
                            <div class="setting_wrap" style="padding-top:0px;">\
                                <div class="top_tl">请将以下代码拷贝至网页的<head>与</head>之间或紧跟在<body>之后的位置</div>\
                                <div><textarea readonly class="inp" style="width:700px;height:40px;" id="proj_code"></textarea></div>\
                                <div style="color:red;">请注意：为保证实验数据的准确与统一，实验启动后，所有实验相关设置将无法修改。</div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">确定</a>\
                            <a href="#" class="btn cancel_btn">取消</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.find('#proj_code').click(function () {
                $(this).setSelect(0, 1000);
            });
            $dom.delegate('.ok_btn', 'click', function () {
                var caseData = editorConf.caseData;
                var params = {
                    projectId: caseData.projectId,
                    caseId: caseData.caseId
                }
 
                $.Dialog.loader.show('正在启动试验...');
                $.postEx(MAIN_URL, '/runCase', params, function (data) {
                    if (data.result == 'success') {
                        dialog.hide();
                        $.Dialog.showPopTip('实验已启动');
                        editorConf.caseData.caseStatus = 1;
                        dealStatus('start');
                        dealStatus();
                        showCantEditTip(editorConf.caseData.caseStatus);
                    } else {
                        $.Dialog.alert(data.errMsg);
                    }
                    $.Dialog.loader.hide();
                });
                return false;
            })
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var code = '<script src="' + DATA_HOST + '/abtest/code/' + editorConf.caseData.projectId +
                '/stat.js"></script>';
            $dom.find('#proj_code').val(code);
        }
    });
    var Assign = new Dialog({
        html: '<div style="display:none;min-width:300px;" class="dialog">\
                        <div class="dialog_title">分流配置</div>\
                        <div class="content">\
                            <div style="max-height:300px; overflow-y:auto; overflow-x:hidden;">\
                                <div class="s_form">\
                                    <div class="item">\
                                        <div class="l">实验名称：</div>\
                                        <div class="r"><input id="name" type="text" style="width:40px;" /></div>\
                                    </div>\
                                </div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">确定</a>\
                            <a href="#" class="btn cancel_btn">取消</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.delegate('.ok_btn', 'click', function () {
                if (!checkCanSave()) {
                    return false;
                }
 
                var versionInfo = dataCtl.getVersionsInfo();
                var total = 0;
                var versions = [];
                $dom.find('.s_form').find('.item input').each(function () {
                    var $this = $(this);
                    var p = $this.val();
                    p = parseInt(p);
                    if (isNaN(p)) {
                        p = 0;
                    }
                    total += p;
                    versions.push({
                        id: $this.attr('v'),
                        v: p
                    });
                });
                if (total < 100) {
                    $.Dialog.showPopTip('分配的流量总和小于100%');
                    return false;
                }
 
                if (total > 100) {
                    $.Dialog.showPopTip('分配的流量总和超过100%');
                    return false;
                }
 
                for (var i = 0; i < versions.length; i++) {
                    var item = versions[i];
                    var ver = versionInfo[item.id];
                    if (ver.percent != item.v) {
                        ver.percent = item.v;
                        ver.isChange = 1;
                    }
                }
 
                dataCtl.setChange(true);
                $.Dialog.showPopTip('流量分配完成，记得保存设置');
                dialog.hide();
            });
            $dom.delegate('.item input', 'keyup', function () {
                var $this = $(this);
                var v = $this.val().trim();
                var $items = $this.parents('.item').nextAll('.item');
                var $pre_items = $this.parents('.item').prevAll('.item');
                if (v != '') {
                    var used = 0;
                    $pre_items.each(function () {
                        var v = $(this).find('input').val().trim();
                        if (v != '') {
                            used += parseInt(v);
                        }
                    });
                    var remain = 100 - used - parseInt(v);
                    if (remain < 0) {
                        remain = 0;
                    }
 
                    if ($items.length > 0) {
                        var n = $items.length;
                        var p = Math.floor(remain / n);
                        for (var i = 0, item; i < n - 1; i++) {
                            item = $items.eq(i);
                            item.find('input').val(p);
                        }
 
                        $items.eq(n - 1).find('input').val(remain - p * (n - 1));
                    }
                }
 
            });
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var list = dataCtl.getVersions();
            var html = [];
            for (var i = 0; i < list.length; i++) {
                var item = list[i];
                if (!item.isDel) {
                    html.push('<div class="item">\
                                    <div class="l" style="width:auto;">' + item.name +
                        '：</div>\
                                    <div class="l" style="width:auto;"><input v="' + item.id + '" value="' +
                        item.percent + '" type="text" style="width:40px;" /><span> %</span></div>\
                                </div>');
                }
            }
 
            $dom.find('.s_form').html(html.join(''));
        }
    })
 
 
    var LabLimit = new Dialog({
        html: '<div style="display:none;min-width:500px;" class="dialog">\
                        <div class="dialog_title">限定实验人群</div>\
                        <div class="content">\
                            <div style="max-height:300px; overflow-y:auto; overflow-x:hidden;">\
                                <div class="s_form">\
                                </div>\
                                <div class="none_txt" style="display:block;padding:6px;">更多限定条件？<a href="#" class="btn add">立即添加</a></div>\
                            </div>\
                        </div>\
                        <div class="ctl_wrap">\
                            <a href="#" class="btn ok_btn">保存</a>\
                            <a href="#" class="btn cancel_btn">取消</a>\
                        </div>\
                  </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            var addLimit = function (typeId, valueId) {
                var $limit = $(
                    '<div class="item">\
                                <div class="r r2"><select style="width:120px;" name="limit"></select></div>\
                                <div class="r r2"><select style="width:180px;" name="limit_sub"></select></div>\
                                <div class="r r2"><a href="#" class="del blue">删除</a></div>\
                            </div>');
                var $slt_m = $limit.find('select[name=limit]');
                var $slt_sub = $limit.find('select[name=limit_sub]');
                var limitTypes = $.TypeHelper.getLimitTypes();
                var html = ['<option value="0">选择限定类型</option>'];
                for (var i = 0, item; i < limitTypes.length; i++) {
                    item = limitTypes[i];
                    html.push('<option value="' + item.typeId + '">' + item.name + '</option>')
                }
 
                $slt_m.html(html.join(''));
                $slt_m.change(function () {
                    var id = $(this).val();
                    var html = ['<option value="0">选择限定条件</option>'];
                    var data = $.TypeHelper.getLimitTypes(id);
                    if (data) {
                        var list = data.subValues;
                        for (var i = 0, item; i < list.length; i++) {
                            item = list[i];
                            html.push('<option value="' + item.id + '">' + item.name + '</option>')
                        }
                    }
 
                    $slt_sub.html(html.join(''));
                });
                if (typeId) {
                    $slt_m.val(typeId).change();
                    setTimeout(function () {
                        $slt_sub.val(valueId);
                    }, 100);
                } else {
                    $slt_m.change();
                }
 
                $slt_m.click(function () {
                    var used = [];
                    $(this).parents('.item').siblings('.item').find('select[name=limit]').each(function () {
                        var id = $(this).val();
                        var limit = $.TypeHelper.getLimitTypes(id);
                        if (limit) {
                            if (limit.isMutil == 0) {
                                used.push(id);
                            }
                        }
                    });
                    $(this).find('option').each(function () {
                        var $option = $(this);
                        if ($.inArray($option.attr('value'), used) == -1) {
                            $option.show();
                        } else {
                            $option.hide();
                        }
                    });
                });
                $slt_sub.click(function () {
                    var $item = $(this).parents('.item');
                    var id = $item.find('select[name=limit]').val();
                    if (id != 0) {
                        var used = [];
                        $item.siblings('.item').find('select[name=limit]').each(function () {
                            var pid = $(this).val();
                            if (pid != 0) {
                                if (pid == id) {
                                    var t = $(this).parents('.item').find('select[name=limit_sub]').val();
                                    if (t != 0) {
                                        used.push(t);
                                    }
                                }
                            }
                        });
                        $(this).find('option').each(function () {
                            var $option = $(this);
                            if ($.inArray($option.attr('value'), used) == -1) {
                                $option.show();
                            } else {
                                $option.hide();
                            }
                        });
                    }
                })
 
                $dom.find('.s_form').append($limit);
                $.Dialog.autoFix();
            }
 
            $dom.delegate('.ok_btn', 'click', function () {
                var limitTypes = [];
                $dom.find('.s_form .item').each(function () {
                    var $item = $(this);
                    var type = $item.find('select[name=limit]').val();
                    var sub_type = $item.find('select[name=limit_sub]').val();
                    if (type != 0 && sub_type != 0) {
                        limitTypes.push(type + ':' + sub_type);
                    }
                });
                if (limitTypes.length == 0) {
                    $.Dialog.showPopTip('请先添加实验限定');
                    return false;
                }
 
                var params = {
                    projectId: editorConf.caseData.projectId,
                    caseId: editorConf.caseData.caseId,
                    limitTypes: limitTypes
                }
 
                $.postEx(MAIN_URL, '/doSaveLimit', params, function (data) {
                    if (data.result == 'success') {
                        dialog.hide();
                        $.Dialog.showPopTip('保存实验限定成功');
                    } else {
                        $.Dialog.showPopTip(data.errMsg)
                    }
                })
 
                return false;
            }).delegate('.item .del', 'click', function () {
                $(this).parents('.item').remove();
                return false;
            })
 
            $dom.delegate('.none_txt .add', 'click', function () {
                addLimit();
                return false;
            });
            dialog.getLimitList = function () {
                $dom.find('.s_form').empty();
                $dom.find('.none_txt').html('正在加载...');
                var params = {
                    projectId: editorConf.caseData.projectId,
                    caseId: editorConf.caseData.caseId
                }
 
                $.getEx(MAIN_URL, '/doSearchLimitByCaseId', params, function (data) {
                    if (data.result == 'success') {
                        var list = data.data;
                        for (var i = 0, item; i < list.length; i++) {
                            item = list[i];
                            addLimit(item.limitTypeId, item.limitValueId);
                        }
                        if (list.length == 0) {
                            addLimit();
                        }
                        $dom.find('.none_txt').html('更多限定条件？<a href="#" class="btn add">立即添加</a>');
                    }
 
                })
 
            }
 
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            dialog.getLimitList();
        }
    })
 
 
    var LabPreview = new Dialog({
        html: '<div style="display:none;min-width:600px;" class="dialog _zq-large-viewer _zq-flat">\
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
                        <div class="tab_tl">实验人群</div>\
                        <div class="tab_con">\
                            <div class="tab_form">\
                                <div class="item">\
                                    <div class="r" id="limit"></div>\
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
                            <div class="top_tl">请将以下代码拷贝至网页的<head>与</head>之间或紧跟在<body>之后的位置</div>\
                            <div style="width:900px;">\
                                <textarea readonly class="inp" style="width:700px;height:40px;" id="proj_code"></textarea>\
                                <input type="button" class="_zq-docopy" id="_zq-btn-copy-proj_code" value="复制"/>\
                            </div>\
                            <div class="_zq-copy-success-tip" style="display:none;">内容已经复制到剪贴板</div>\
                            <div class="orange">注：请将代码嵌入至控制版本中，如果采用URL版本，URL版本中也需嵌入代码</div>\
                        </div>\
                        </div>\
                    </div>\
                    <div class="none_txt">正在加载...</div>\
                </div>\
          </div>',
        onInit: function (dialog) {
            var $dom = dialog.dom;
            $dom.delegate('.ok_btn', 'click', function () {
                dialog.hide();
                return false;
            })
 
        },
        onShow: function (dialog) {
            var $dom = dialog.dom;
            var showInfo = function (data) {
                var caseInfo = data.caseInfo;
                $dom.find('#case_info').html(
                    '<div class="item clearfix">\
                        <div class="l">实验名称：</div>\
                        <div class="r2">' +
                    caseInfo.caseName +
                    '</div>\
                        <div class="l">所属业务：</div>\
                        <div class="r2">' +
                    $.TypeHelper.getBuizTypes(caseInfo.buizType).name +
                    '</div>\
                        <div class="l">移动版：</div>\
                        <div class="r2"><select><option value="0">否</option><option value="1">是</option></select></div>\
                </div>\
                <div class="item clearfix">\
                        <div class="l">实验网址：</div>\
                        <div class="r"><a href="' +
                    caseInfo.url + '" target="_blank">' + caseInfo.url + '</a></div>\
                </div>');
                var goalInfo = data.pageGoal;
                var html = [];
                for (var i = 0, item; i < goalInfo.length; i++) {
                    item = goalInfo[i];
                    html.push('<span class="tag">' + item.goalName + (item.isMaster == 1 ?
                        ' <span class="red">*</span>' : '') + '</span>')
                }
                $dom.find('#page_goal').html(html.join('') || '无');
                goalInfo = data.kpiGoal;
                html = [];
                for (var i = 0, item; i < goalInfo.length; i++) {
                    item = goalInfo[i];
                    html.push('<span class="tag">' + item.goalName + (item.isMaster == 1 ?
                        ' <span class="red">*</span>' : '') + '</span>')
                }
                $dom.find('#kpi_goal').html(html.join('') || '无');
                var limit = data.caseConfig;
                html = [];
                for (var i = 0, item; i < limit.length; i++) {
                    item = limit[i];
                    html.push('<span class="tag">' + $.TypeHelper.getLimitTypes(item.limitTypeId).subMap[item.limitValueId]
                        .name + '</span>');
                }
                $dom.find('#limit').html(html.join('') || '无');
                var code = '<script src="' + DATA_HOST + '/abtest/code/' + caseInfo.projectId + '/stat.js"></script>';
                $dom.find('#proj_code').val(code);
                var pieData = [];
                var i = 0;
                $('.version_wrap').find('.btn').each(function () {
                    var $this = $(this);
                    var v = $this.attr('v');
                    var version = dataCtl.getVersionsInfo()[v];
                    var _oneData = {
                        name: version.name,
                        y: version.percent,
                        color: Page.versionColors[i++]
                    };
                    pieData.push(_oneData);
                });
                showPie(pieData);
                $dom.find('.lab_preview').show();
                $.Dialog.autoFix();
                $('#_zq-btn-copy-proj_code').data('zclipId') || $('#_zq-btn-copy-proj_code').zclip({
                    path: EDITOR_URL + '/assets/js/ZeroClipboard.swf',
                    copy: function () {
                        return $('#proj_code').val();
                    },
                    afterCopy: function () {
                        $('._zq-copy-success-tip', $dom).show();
                        setTimeout(function () {
                            $('._zq-copy-success-tip', $dom).hide(1000);
                        }, 2000)
                    }
                });
            }
 
            var showPie = function (data) {
                $dom.find('#version').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        marginLeft: 0,
                        width: 300,
                        spacingLeft: -200
                    },
                    xAxis: {
                        tickLength: 0
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
                        labelFormat: '{name}',
                        itemMarginBottom: 5,
                        x: 0,
                        y: 10,
                        symbolHeight: 6,
                        symbolWidth: 15
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
                                connectorWidth: 0,
                                connectorPadding: -25
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
                projectId: editorConf.caseData.projectId,
                caseId: editorConf.caseData.caseId
            }
 
            $dom.find('.none_txt').show();
            $dom.find('.lab_preview').hide();
            $.getEx(MAIN_URL, '/doSearchConfigInfoByCaseId', params, function (data) {
                if (data.result == 'success') {
                    showInfo(data);
                    $dom.find('.none_txt').hide();
                } else {
                    $.Dialog.showPopTip(data.errMsg);
                }
            })
        }
 
    })
 
 
    var UrlVersionCtl = function (version) {
        var self = this;
        self.isload = false;
        var $editorDom;
        var editorWin;
        var editorDoc;
        var selector;
        var cacheDoc;
        var selectEle;
        self.setMode = function (type, $doc) {
            type = type || 'edit';
            editorConf.mode = type;
            $doc = $doc || editorDoc;
            switch (type) {
            case 'edit':
                $doc.find("html, body").css({
                    "user-select": "none",
                    "-o-user-select": "none",
                    "-moz-user-select": "none",
                    "-khtml-user-select": "none",
                    "-webkit-user-select": "none"
                });
                //dataCtl.resetCurrent();
                $doc.bind("contextmenu", editorConf.contextMenuHandler);
                $('.goal_ctl_chk :checkbox:not(:checked)').click();
                $('a[href].proxied', $doc).each(function () {
                    if ($(this).attr('_url')) {
                        $(this).attr('href', $(this).attr('_url'));
                        $(this).attr('target', $(this).attr('_target') || "");
                        $(this).removeClass('proxied');
                    }
                });
                $('form[action].proxied', $doc).each(function () {
                    if ($(this).attr('_url')) {
                        $(this).attr('action', $(this).attr('_url'));
                        $(this).attr('target', $(this).attr('_target') || "");
                        $(this).removeClass('proxied');
                    }
                });
                /*if( ! $('.cursour-default').length ){
                     $('head',$doc).append('<style class="cursour-default">html,body,*{ cursor:default !important;}</style>');
                     }*/
                break;
            case 'navigate':
                //dataCtl.resetCurrent();
                //dataCtl.resetDoc();
                $doc.find("html, body").css({
                    "user-select": "",
                    "-o-user-select": "",
                    "-moz-user-select": "",
                    "-khtml-user-select": "",
                    "-webkit-user-select": ""
                });
                $doc.unbind("contextmenu", editorConf.contextMenuHandler);
                $('.medialab-abtest-selector', $doc).hide();
                $('.goal_ctl_chk :checkbox:checked').click();
                var curVersion = dataCtl.getCurVersion(); //
                $('a[href]:not(.proxied)', $doc).each(function () {
                    $(this).attr('_url', $(this).attr('href'));
                    $(this).attr('_target', $(this).attr('target') || "");
                    $(this).attr('target', curVersion.editFrameId);
                    $(this).attr('href', proxyUrl($(this).attr('href')));
                    if (!$(this).attr('prx')) {
                        $(this).attr('prx', '1');
                    }
                    $(this).on('click', function () {
                        $('#' + version.editFrameId).data('newSrc', $(this).attr('href'));
                        dataCtl.resetCurrent($(this).attr('href'));
                    });
                    $(this).addClass('proxied');
                });
                $('form[action]:not(.proxied)', $doc).each(function () {
                    $(this).attr('_url', $(this).attr('action'));
                    $(this).attr('_target', $(this).attr('target') || "");
                    $(this).attr('target', curVersion.editFrameId);
                    $(this).attr('action', proxyUrl($(this).attr('action')));
                    if (!$(this).attr('prx')) {
                        $(this).attr('prx', '1');
                    }
                    var $this = $(this);
                    $this.on('submit', function () {
                        $('#' + version.editFrameId).data('newSrc', $this.attr('action'));
                        dataCtl.resetCurrent($this.attr('action'));
                    });
                    $this.addClass('proxied');
                });
                //editorIframeLoadEvent();
                //$('.cursour-default',$doc).remove();
                break;
            }
        }
 
        self.__resetDoc = function () {
            if ($('.goal_ctl_chk input').attr('checked')) {
                showGoals();
            } else {
                showGoals(1);
            }
        }
 
        var showGoals = function (hide) {
            var $doc = editorDoc;
            if (!$doc) {
                return;
            }
            if (hide) {
                $doc.find('.medialab-abtest-goal').remove();
            } else {
                var list = editorConf.goalList.normalGoalData;
                $doc.find('.medialab-abtest-goal').remove();
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    var $el = $doc.find(item.goalSelector);
                    if ($el.length > 0) {
                        $el.each(function () {
                            var $el = $(this);
                            if (!$el.hasClass('medialab-elem')) {
                                var $goal = $('<div class="medialab-abtest-goal medialab-elem"></div>');
                                $goal.css({
                                    left: $el.offset().left,
                                    top: $el.offset().top - 2,
                                    width: $el.outerWidth(),
                                    height: $el.outerHeight()
                                }).appendTo($doc.find('body'));
                            }
                        });
                    }
                }
            }
        }
 
        self.showGoals = showGoals;
        self.getDoc = function () {
            return editorDoc;
        }
 
        self.getSelector = function () {
            return selector;
        }
 
        self.show = function () {
            self.loadPage(function () {
                $editorDom.show().siblings('.page_editor').hide();
                self.setMode(editorConf.mode, editorConf.curDoc);
                dataCtl.resetDoc();
                self.__resetDoc();
                __bindEditEvents();
                //bindEditEvents();
                //setMode(editorConf.mode);
                //self.resetDoc();
            });
        }
 
        self.loadPage = function (cb) {
            if (self.isload) {
                $editorDom = $('#' + version.editFrameId).parent();
                var iframe = $editorDom.find('iframe');
                editorWin = iframe.get(0).contentWindow;
                editorDoc = $(editorWin.document);
                editorDoc.bind("contextmenu", function (e) {
                    return false;
                });
                /*insertEditCode(editorDoc, function($doc){
                 selector = $doc.find('.medialab-abtest-selector');
                 $doc.find('body').find('script').remove();
                 dataCtl.cacheDoc[iframe.attr('id')] = $doc.find('body').children();
                 });*/
                editorConf.curDoc = editorDoc;
                //bindEditEvents();
                cb && cb();
            } else {
                var shallow = false;
                if ($('#' + version.editFrameId).length) {
                    //浏览模式打开新的页面
                    $editorDom = $('#' + version.editFrameId).parent();
                    shallow = true;
                } else {
                    //载入url分离版本
                    $editorDom = $('<div class="page_editor" id="' + version.editFrameId +
                        '-wrapper">\
                                        <iframe frameborder="0" class="html_loader"></iframe>\
                                    </div>')
                        .find('iframe').attr('id', version.editFrameId).attr('name', version.editFrameId)
                        .parent();
                    $editorDom.insertAfter($('#base_editor'));
                }
 
                //editorConf.doc=$editorDom.get(0).contentWindow
 
                $('.page_loading').show();
                var iframe = $editorDom.find('iframe');
                //console.log('1:'+version.url);
                setTimeout(function () {
                    if (iframe.get(0).contentWindow.document.readyState != 'loading') {
                        $('.page_loading').hide();
                    }
                }, 500); //临时解决办法
 
                var iframeHandler = function () {
                    self.isload = true;
                    if (!shallow) {
                        iframe.unbind('load');
                    }
 
                    $('.page_loading').hide();
                    editorWin = iframe.get(0).contentWindow;
                    editorDoc = $(editorWin.document);
                    editorDoc.bind("contextmenu", function (e) {
                        return false;
                    });
                    insertEditCode(editorDoc, function ($doc) {
                        editorConf.selector = selector = $doc.find('.medialab-abtest-selector');
                        $doc.find('body').find('script').remove();
                        dataCtl.cacheDoc[iframe.attr('id')] = $doc.find('body').children();
                    });
                    editorConf.curDoc = editorDoc;
                    if (shallow) {
                        editorConf.doc = editorConf.curDoc;
                    }
                    //bindEditEvents();
                    //                  setMode(editorConf.mode,editorDoc);
                    //                  self.resetDoc();
                    cb && cb();
                };
                if (shallow) {
                    iframe.bind('load', iframeHandler);
                } else {
                    iframe.bind('load', iframeHandler);
                    iframe.attr('src', parseEditUrl(version.url));
                }
            }
 
 
            $editorDom.show().siblings('.page_editor').hide();
        }
 
        var __bindEditEvents = function () {
            $('a[href].proxied,form[action].proxied', editorDoc).on('click', function () {
                //注意这里有Bug，如果页面发生重定向，则这里捕捉不到，所以最合理的方式是由服务器端代理将url写入返回的页面
                $('#_zq-view-mode [t=current-url]').attr('current-url', $(this).attr('_url'));
            });
            $('.ctl_bar').on('mouseover', function (event) {
                selector.hide();
            });
            editorDoc.bind('mouseover', function (evt) {
                var $this = $(this);
                var $el = $(evt.target);
                var tag = $el.prop('tagName').toLowerCase();
                if (tag != 'body' && tag != 'html') {
                    //隐藏的改变类
                    if ($el.hasClass("_abt_hidden")) {
                        $el.removeClass("_abt_hidden");
                        $el.addClass("_abt_hidden_show");
                    }
                    if (!$el.hasClass('medialab-elem') && isEditMode() && !isEditing()) {
                        var offset = $el.offset();
                        selectEle = $el;
                        selector.css({
                            left: offset.left,
                            top: offset.top,
                            width: $el.outerWidth(),
                            height: $el.outerHeight()
                        }).removeClass('medialab-abtest-selector-on').show();
                    }
                }
 
            }).bind('click', function (evt) {
                var $this = $(this);
                if (isEditMode()) {
                    //$('.medialab-abtest-selector-fullmask',$this).show();
                    // editorConf.selector.addClass('medialab-abtest-selector-on');
                    // showDropMenu();
                    return false;
                }
            }).bind('mousedown', function (evt) {
                var $el = $(evt.target);
                var tag = $el.prop('tagName').toLowerCase();
                if (tag != 'body' && tag != 'html') {
                    if (!$el.hasClass('medialab-elem') && isEditMode() && !isEditing()) {
                        var offset = $el.offset();
                        selectEle = $el;
                        editorConf.selectEle = selectEle;
                        selector.css({
                            left: offset.left,
                            top: offset.top,
                            width: $el.outerWidth(),
                            height: $el.outerHeight()
                        }).addClass('medialab-abtest-selector-on').show();
                        setTimeout(function () {
                            dropMenu.show({
                                selector: selector,
                                doc: editorDoc
                            });
                        }, 100);
                    }
                }
 
            }).bind('mouseout', function (evt) {
                var $el = $(evt.target);
                //隐藏的改变类
                if ($el.hasClass("_abt_hidden_show")) {
                    $el.removeClass("_abt_hidden_show");
                    $el.addClass("_abt_hidden");
                }
            });
        }
 
 
    }
 
 
    initPage();
})