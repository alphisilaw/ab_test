function getByClass(oParent,sClass){
        var aEle=oParent.getElementsByTagName('*');
        var arr=[];
        var reg=new RegExp('\\b'+sClass+'\\b','i');

        for(var i=0; i<aEle.length; i++){
            if(reg.test(aEle[i].className)){
                arr.push(aEle[i]);
            }
        }

        return arr;
    }

    //
    function addMouseWheel(obj,fn){
        obj.onmousewheel=fnWheel;
        if(obj.addEventListener)obj.addEventListener('DOMMouseScroll',fnWheel,false);

        function fnWheel(e){
            var oEvent=e||event;
            var d=oEvent.wheelDelta?oEvent.wheelDelta>0:oEvent.detail<0;

            fn(d);

            if(oEvent.preventDefault)oEvent.preventDefault();

            return false;
        }
    }

    function scrollBar(obj,direction){
        var oSbar=getByClass(obj,'sBar')[0];
        var oSbtn=oSbar.children[0];

        var oMain=getByClass(obj,'s_main')[0];
        var oCont=getByClass(obj,'cont')[0];

        //
        oSbtn.onmousedown=function(e){
            var oEvent=e||event;

            var disX=oEvent.clientX-oSbtn.offsetLeft;
            var disY=oEvent.clientY-oSbtn.offsetTop;

            function fnMove(e){
                var oEvent=e||event;
                switch(direction){
                    case 'l':
                        var l=oEvent.clientX-disX;
                        setDirection({left:l});
                        break;

                    case 't':
                        var t=oEvent.clientY-disY;
                        setDirection({down:t});
                        break;

                    case '0':
                        setDirection({down:0});
                        break;
                }
            }

            function fnUp(){
                this.onmousemove=null;
                this.onmouseup=null;
                if(oSbar.releaseCapture)oSbar.releaseCapture();
            }

            if(oSbar.setCapture){
                oSbar.onmousemove=fnMove;
                oSbar.onmouseup=fnUp;
                oSbar.setCapture();
            }else{
                document.onmousemove=fnMove;
                document.onmouseup=fnUp;
            }
            return false;
        };

        //
        function setDirection(json){
            if(!isNaN(json.down)){
                if(json.down<=0){
                    json.down=0;
                }else if(json.down>oSbar.offsetHeight-oSbtn.offsetHeight){
                    json.down=oSbar.offsetHeight-oSbtn.offsetHeight;
                }
                oSbtn.style.top=json.down+'px';
                var scale=json.down/(oSbar.offsetHeight-oSbtn.offsetHeight);
            }else{
                if(json.left<=0){
                    json.left=0;
                }else if(json.left>oSbar.offsetWidth-oSbtn.offsetWidth){
                    json.left=oSbar.offsetWidth-oSbtn.offsetWidth;
                }
                oSbtn.style.left=json.left+'px';

                var scale=json.left/(oSbar.offsetWidth-oSbtn.offsetWidth);
            }
            oCont.style.top=-(oCont.offsetHeight-oMain.offsetHeight)*scale+'px';
        }

        //
        switch(direction){
            case 'l':
                addMouseWheel(obj,function(d){
                    var mouseX=0;
                    if(d){
                        mouseX=oSbtn.offsetLeft-10;
                    }else{
                        mouseX=oSbtn.offsetLeft+10;
                    }
                    setDirection({left:mouseX});
                });
                //
                var oBtnW=oMain.offsetHeight/oCont.offsetHeight*oSbar.offsetWidth;
                if(oBtnW<50) oBtnW=50;
                if(oBtnW>100) oBtnW=100;
                oSbtn.style.width=oBtnW+'px';
                break;
            case 't':
                addMouseWheel(obj,function(d){
                    var mouseY=0;
                    if(d){
                        mouseY=oSbtn.offsetTop-10;
                    }else{
                        mouseY=oSbtn.offsetTop+10;
                    }
                    setDirection({down:mouseY});
                });
                //
                var oBtnH=oMain.offsetHeight/oCont.offsetHeight*oSbar.offsetHeight;
                if(oBtnH<50) oBtnH=50;
                if(oBtnH>100) oBtnH=100;
                oSbtn.style.height=oBtnH+'px';
                break;
            case '0':
                addMouseWheel(obj,function(d){
                    setDirection({down:0});
                });
                break;
        }
    }
