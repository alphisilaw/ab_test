$(function() {
    var dataObject = null;
    var pageNo = 1;
    var totalPage = 1;
    var clickItemIdx = 0;
    var onePageNum = 10;
    function init() {
        getItems( true );
        initEvent();
    }

    function initEvent() {
        var pages = $('.pages'),
            staple1 = $('#staple1'),
            defineOptions = $('.form-defined-options'),
            channelList = $('#channelList');
        
        document.onkeydown = function(event) { 
        	if(event.keyCode == 13) {
                pageNo = 1;
                getItems( true );
        	}
        }

        $('#queryBtn').on('click', function() {
            pageNo = 1;
            getItems( true );
        });

        $('body').on('click', function() {
            defineOptions.hide();
        });

        defineOptions.on('click', '.defined-option', function( e ) {
            e.stopPropagation();
            staple1.val( $(this).attr('value') );
            defineOptions.hide();
        });

        staple1.on('focus', function() {
            defineOptions.show();
        }).on('click', function(e) {
            e.stopPropagation();
        });

        staple1.on('keyup input', function() {
            var text = $(this).val();

            defineOptions.find('.defined-option').each(function() {
                var aText = $(this).html();
                if( aText.indexOf(text) != -1 ) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            });
        });

        pages.on('click', 'a', function() {
            var tag = $(this).html(),
                curNum = 1;

            switch( tag ) {
                case '首页':
                    curNum = 1;
                    break;
                case '上一页':
                    curNum = pageNo - 1;
                    break;
                case '下一页':
                    curNum = pageNo + 1;
                    break;
                case '末页':
                    curNum = totalPage;
                    break;
                case 'go':
                    curNum = +$(this).prev().val();
                    break;
                default:
                    break;
            }

            if( curNum != pageNo && curNum > 0 && curNum <= totalPage ) {
                pageNo = curNum;
                getItems();
            }
        });

        channelList.on('click', 'a[data-num]', function() {
            clickItemIdx = $(this).attr('data-num');
            var str = getModeTpl( clickItemIdx ),
                box = modeDialog( str );
            box.show();
            return false;
        });

        channelList.on('click', 'a.mode-link', function( e ) {
            e.preventDefault();
            var url = $(this).attr('href');
            createQcordeBox( url, '链接地址' );
        });
    }

    function getItems( flag ) {
    	$.Dialog.loader.show();
        $.ajax({
            type: 'post',
            url: CHANNEL_URL+'/getChannelList',
            data: getQueryData(),
            dataType: 'json',
            success: function( res ) {
                var res = JSON.parse(res);
                if( res.result == 1 ) {
                    dataObject = res;
                    createTableItems();
                    flag && createPages();
                    $('.pages-wrap span').eq(1).html( '第'+pageNo+'/'+totalPage+'页' );
                } else {
                    $.Dialog.loader.hide();
                    alert( res.errMsg );
                }
            }
        });
    }

    function getQueryData() {
        var data = {
            pageNum: pageNo - 1 || 0,
            channelId: $('#channelId').val(),
            subChannelId: $('#subChannelId').val(),
            channelName: $('#channelName').val(),
            staple1: $('#staple1').val(),
            tempId: $('#tempId').val()
        }

        return data;
    }

    function createTableItems() {
        var lists = dataObject.data,
            str = '';
        for( var i = 0, len = lists.length; i < len; i++ ) {
            str += getTrTpl( lists[i], i );
        }
        $('#channelList').html( str );
        $.Dialog.loader.hide();
        
        $('.copy').zclip({
            path: MAIN_URL+'/assets/js/ZeroClipboard.swf',
            copy: function() {
                return $(this).attr('data-link');
            },
            afterCopy: function() {
               alert('已成功复制到剪切板！');
            }
        });
        
        $('#channelList .mode-uv').each(function() {
        	renderUv(this);
        });

        $('.zclip').attr('title', '复制渠道链接');
    }
    
    function renderUv(mode_uv) {
    	var $mode_uv = $(mode_uv);
    	var verid = $mode_uv.attr('verid');
        $.ajax({
            type: 'post',
            url: CHANNEL_URL+'/getVersionUv',
            data: {'versionId': verid},
            dataType: 'json',
            success: function( res ) {
                var res = JSON.parse(res);
                if( res.result == 1 ) {
                	$mode_uv.html(res.data);
                } else {
                    alert( res.errMsg );
                }
            }
        });
    }

    function getTrTpl(tmp, num ) {
        var Staple1 = tmp.Staple1 || '',
            ChannelName = tmp.ChannelName || '',
            channelId = tmp.channelId > -1 ? tmp.channelId : '',
            subid = tmp.subid > -1 ? tmp.subid : '',
            url = tmp.url || '',
            versions = tmp.versions || [];
        var vers = getVersionsText(versions, tmp.preview);
        var str = '<tr>\
                    <td>'+Staple1+'</td>\
                    <td>'+ChannelName+'</td>\
                    <td>'+channelId+'</td>\
                    <td>'+subid+'</td>\
                    <td>'+vers.verIdStr+'</td>\
                    <td>'+vers.versStr+'</td>\
                    <td>'+vers.perStr+'</td>\
                    <td style="position:relative;">\
                        <a class="config-mode" href="javascript:;" data-num="'+num+'" title="模板配置"><img width="20" src="'+MAIN_URL+'/assets/images/icon_config.png"></a>\
                        <a class="see-data" target="_blank" href="'+CHANNEL_URL+'/channel-data?channelId='+channelId+'&subChannelId='+subid+'" title="查看数据"><img width="18" src="'+MAIN_URL+'/assets/images/icon_seedata.png"></a>\
                         <a data-link="'+url+'" class="copy" href="javascript:;" title="复制渠道链接"><img width="20" src="'+MAIN_URL+'/assets/images/copy_link.png"></a>\
                    </td>\
                </tr>';

        return str;
    }

    function getVersionsText( vers, view ) {
        var versStr = '',
            perStr = '',
            verIdStr = '';
        for( var i = 0, len = vers.length; i < len; i++ ) {
            // versStr += vers[i].tempName + ' ' + vers[i].per * 100 + '%<br>';
            versStr += '<a class="mode-link" target="_blank" href="'+view+'&templateId='+vers[i].tempId+'">'+vers[i].tempName+'</a><br>';
            perStr += vers[i].per * 100 + '%<br>';
            verIdStr += '<p class="mode-uv" verid="'+vers[i].versionId+'"></p>'
        }
        return {
            versStr: versStr,
            perStr: perStr,
            verIdStr: verIdStr
        };
    }

    function createPages() {
        totalPage = Math.ceil(dataObject.itemNum/onePageNum) || 1;
        var pageStr = '<div class="pages-wrap">\
                        <span>共'+dataObject.itemNum+'条</span>\
                        <span>第'+pageNo+'/'+totalPage+'页</span>\
                        <a href="javascript:;" class="chg-pg">首页</a>\
                        <a href="javascript:;" class="chg-pg">上一页</a>\
                        <a href="javascript:;" class="chg-pg">下一页</a>\
                        <a href="javascript:;" class="chg-pg">末页</a>\
                        <input type="number">\
                        <a href="javascript:;" class="chg-pg go-page">go</a>\
                      </div>';

        $('.pages').html( pageStr );
    }

    function getModeOptions() {
        var options = '';
        $('#tempId').find('option:gt(0)').each(function() {
            options += this.outerHTML;
        });
        return options;
    }
    function getModeTpl( idx ) {
        var str = '',
            options = getModeOptions(),
            arr = dataObject.data[idx].versions;

        for( var i = 0, len = arr.length; i < len; i++ ) {
            var perTmp = arr[i].per * 100;
            str += '<tr>\
                        <td><span>模板'+(i+1)+'</span> &nbsp;<select data-id="'+arr[i].tempId+'">'+options+'</select></td>\
                        <td class="row"><input type="number" value="'+perTmp+'">%</td>\
                        <td style="text-align: center;"><a class="del" href="javascript:;">删除</a></td>\
                    </tr>';
        }

        return str;
    }

    function addOneMode( num ) {
        if( num > 6 ) {
            alert('最多添加6个模板！');
            return;
        }
        var options = getModeOptions();
        var str = '<tr>\
                    <td><span>模板'+num+'</span> &nbsp;<select>'+options+'</select></td>\
                    <td class="row"><input type="number">%</td>\
                    <td style="text-align: center;"><a class="del" href="javascript:;">删除</a></td>\
                </tr>';
        return str;
    }

    function collectModeVersions( box ) {
        var modetext = '',
            pertext = '',
            arr = [],
            tmp = {};
        box.find('tbody tr').not(':last').each(function() {
            var sel = $(this).find('select'),
                ipt = $(this).find('input');
            var tempName = sel.find('option:selected').html(),
                per = ipt.val()||0,
                previewUrl = dataObject.data[clickItemIdx].preview + '&templateId='+sel.val();

            modetext += '<a class="mode-link" href="'+previewUrl+'">'+tempName+'</a><br>';
            pertext += per + '%' + '<br>';
            tmp = {
                per: per/100,
                tempId: sel.val(),
                tempName: tempName
            }

            arr.push( tmp );
        });

        return {
            arr: arr,
            modetext: modetext,
            pertext: pertext
        };
    }

    function resetTdTexts( tmp ) {
        $('#channelList').find('[data-num="'+clickItemIdx+'"]').parents('td').prev().html( tmp.pertext ).prev().html(tmp.modetext);
    }

    function checkPerVals( dom ) {
        var flag = true;
        dom.find('input[type="number"]').each(function() {
            var tmp = $(this).val();
            if( flag ) {
                if( tmp > 100 ) {
                    alert('分流百分比不能大于100');
                    flag = false;
                } else if( tmp < 0 ) {
                    alert('分流百分比不能小于0');
                    flag = false;
                } else if( !/^\d{1,}$/.test(tmp) ) {
                    alert('分流百分比必须是数字');
                    flag = false;
                }
            }
        });

        return flag;
    }

    function modeDialog( str ) {
        var AddLab = new Dialog({
                html:'<div style="display:none;min-width:300px;" class="dialog">\
                                <div class="dialog_title">模版配置</div>\
                                <div class="content mode-set">\
                                    <table>\
                                        <thead><th width="271">选择模板</th><th class="row" width="87">流量分配</th><th>操作</th></thead>\
                                        <tbody>'+str+'<tr class="add-mode">\
                                                <td colspan="1"><a href="javascript:;">添加模板</a></td>\
                                                <td colspan="3"><label for="isbatch">同步修改所有子渠道</label><input id="isbatch" type="checkbox"/></td>\
                                            </tr>\
                                        </tbody>\
                                    </table>\
                                    <p style="color:#EC9923;text-align: center;">注意：不能配置相同的模板，且流量分配之和必须100%。</p>\
                                </div>\
                                <div class="ctl_wrap">\
                                    <a href="#" class="btn ok_btn">确定</a>\
                                    <a href="#" class="btn cancel_btn">取消</a>\
                                </div>\
                          </div>',
                onInit:function(dialog){
                    var $dom = dialog.dom;

                    $dom.find('select').each(function() {
                        $(this).val( $(this).attr('data-id') );
                    });

                    $dom.on('click', '.add-mode a', function() {
                        var tbody = $dom.find('tbody'),
                            trNum = tbody.find('tr').length;
                        tbody.find('tr:last').before( addOneMode( trNum ) );
                    });

                    $dom.on('click', 'a.del', function() {
                        $(this).parents('tr').remove();
                        $dom.find('tr').not(':last').each(function(idx) {
                            $(this).find('td:eq(0)').find('span').html('模板'+idx);
                        });
                    });

                    $dom.on('click', '.ok_btn', function() {
                        if( !checkPerVals( $dom ) ) {
                            return;
                        }

                        var myTmp = collectModeVersions( $dom ),
                            tmpUrl = dataObject.data[clickItemIdx].url;

                        dataObject.data[clickItemIdx].versions = myTmp.arr;
                        
                    	dataObject.data[clickItemIdx].batchModify = $('#isbatch')[0].checked;

                        $.ajax({
                            type: 'post',
                            url: CHANNEL_URL + '/modifyVersion',
                            data: JSON.stringify( dataObject.data[clickItemIdx] ),
                            contentType: 'text/json',
                            dataType: 'json',
                            success: function( res ) {
                                var res = JSON.parse( res );
                                if( res.result == 1 ) {
                                	if ( !dataObject.data[clickItemIdx].batchModify ) {
                                		resetTdTexts( myTmp );
                                	} else {
                                		$('#queryBtn').click();
                                	}
                                    dialog.hide();
                                    createQcordeBox( tmpUrl );
                                } else {
                                    alert( res.errMsg );
                                }
                            }
                        });
                    });


                }
            });

        return AddLab;
    }

    function createQcordeBox( tmpUrl, title ) {
        var title = title || '配置成功';
        var qcordeBox = $("#qcordeBox");
        if( qcordeBox.length == 0 ) {
            qcordeDialog( tmpUrl, title );
        } else {
            $('#qcordeurl').html( tmpUrl );
            qcordeBox.html('');
            qcordeBox.qrcode({
                render: "canvas",
                width: 200, //宽度
                height:200, //高度
                text: tmpUrl //任意内容
            });
            $('#qcordeTitle').html( title +'<span class="_zq-closeme"></span>');
            $('._zq-closeme').click(function() {
                $('.dialog, .dialog_overlay').hide();
            });
            qcordeBox.parents('.dialog').show();
            $('.dialog_overlay').show();
        }
    }

    function qcordeDialog( url, title ) {
        var AddLab = new Dialog({
                html:'<div style="display:none;min-width:300px;" class="dialog">\
                                <div class="dialog_title" id="qcordeTitle">'+title+'</div>\
                                <div class="content mode-set">\
                                    <p>可复制以下链接到浏览器或扫描二维码查看</p>\
                                    <p id="qcordeurl">'+url+'</p>\
                                    <div id="qcordeBox" style="width: 200px; height:200px; margin: 15px auto 0;"></div>\
                                </div>\
                          </div>'
            });

        AddLab.show();
        $("#qcordeBox").qrcode({
            render: "canvas",
            width: 200, //宽度
            height:200, //高度
            text: url //任意内容
        });
    }

    init();
});