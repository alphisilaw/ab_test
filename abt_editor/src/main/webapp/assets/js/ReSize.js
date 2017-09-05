(function ($) {
    $.fn.dragDivResize = function (config) {
        var deltaX, deltaY, _startX, _startY;
        var resizeW, resizeH;
        var size = 20;
        var minSize = 10;
        var _this = this;
        var target = this[0];
        var scaleBale = false;

        function initEvent() {
            $(target).on("mouseover mousemove", overHandler);
            $(target).on("mousemove", moveHandler);
            $(target).on('mousedown', downHandler);
            $(target).on("mouseup", upHandler);
            $(target).on("mouseout", outHandler);
        }

        function outHandler() {
            scaleBale = false;
            target.style.outline = "none";
        }
        function overHandler(event) {
            target = event.target || event.srcElement;
            var startX = event.pageX;
            var startY = event.pageY;
            var w = $(target).width();
            var h = $(target).height();
            var _posx = target.offsetLeft + w - startX;
            var _posy = target.offsetTop + h - startY;

            if ((0 < _posx && _posx < size) || (0 < _posy && _posy < size)) {
                target.style.outline = "2px dashed #333";

                if ((0 > _posx || _posx > size) && 0 < _posy && _posy < size) {//垂直方向
                    resizeW = false;
                    resizeH = true;
                    target.style.cursor = "s-resize";
                } else if (0 < _posx && _posx < size && (0 > _posy || _posy > size)) {//水平方向
                    resizeW = true;
                    resizeH = false;
                    target.style.cursor = "w-resize";
                } else if (0 < _posx && _posx < size && 0 < _posy && _posy < size) {////斜线方向
                    resizeW = true;
                    resizeH = true;
                    // document.body.style.cursor = "se-resize !important";
                    target.style.cursor = "se-resize";
                }
                // $(target).on('mousedown', downHandler);
            } else {
                resizeW = false;
                resizeH = false;
                target.style.outline = "";
                target.style.cursor = 'default'
                // $(target).off('mousedown', downHandler);
            }
        }
        function downHandler(event) {
            scaleBale = true;
            target = event.target || event.srcElement;
            _startX = event.pageX;
            _startY = event.pageY;

            if (event.stopPropagation) {//ff
                event.stopPropagation();
            } else {//IE
                event.cancelBubble = true;
            }
            if (event.preventDefault) {
                event.preventDefault();
            } else {
                event.returnValue = false;
            }
        }
        function moveHandler(e) {
            if( !scaleBale ) {
                return;
            }
            if (!e) e = window.event;
            var w, h;

            var startX = e.pageX;
            var startY = e.pageY;
            target = target || e.target || e.srcElement;
            if (target == document.body) {
                return;
            }
            if (resizeW) {
                deltaX = startX - _startX;
                w = $(target).width() + deltaX < minSize ? minSize : $(target).width() + deltaX;
                target.style.width = w + "px";
                _startX = startX;

            }
            if (resizeH) {
                deltaY = startY - _startY;
                h = $(target).height() + deltaY < minSize ? minSize : $(target).height() + deltaY;
                target.style.height = h + "px";
                _startY = startY;
            }

            config.callback && config.callback.call(_this, w, h);

            if (e.stopPropagation) {//ff
                e.stopPropagation();
            } else {//IE
                e.cancelBubble = true;
            }
        }
        function upHandler( event ) {
            scaleBale = false;
            var e = event || window.event;
            resizeW = false;
            resizeH = false;

            if (e.stopPropagation) {//ff
                e.stopPropagation();
            } else {//IE
                e.cancelBubble = true;
            }
        }

        initEvent();
    }
}(jQuery));