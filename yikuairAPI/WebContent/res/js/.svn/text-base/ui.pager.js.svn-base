var createPageRange = function( index , count , omit ) {
    var r = [];
    var limit = 0;
    index = parseInt( index , 10 );
    count = parseInt( count , 10 );
    omit = parseInt( omit , 10 );
    for( var i = 1; i <= count; i++ ) {
        if( i == 1 || i == count ) {
            r.push( i );
        } else if( i == index ) {
            r.push( i );
        } else if( i < index ) {    
            limit = index - omit - 1;
            if( i == limit ) {
                r.push( -1 );
            } else if( i > limit ) {
                r.push( i );
            }
        } else if ( i > index ) {
            limit = index + omit + 1;
            if( i == limit ) {
                r.push( -1 );
            } else if( i < limit ) {
                r.push( i );
            }
        }
    }
    return r;
}

/**
    ms-widget="pager"
    ms-pager-omit="3"                     显示当前页码前后页码的数量，如设置为1,当前页码为５就是 1...4,5,6...10。
 */
avalon.ui["pager"] = function( element , data , vmodels ) {

    var $e = $(element);
    var opts = data.pagerOptions; 
    var omit = opts.omit ? parseInt( opts.omit , 10 ) : 2;

    var html = [];
    html.push('    <div ms-visible="count" class="ui-pager">');
    html.push('        <div ms-click="prev" ms-visible="gt(index,1)" class="ui-pager-next">上一页</div>');
    html.push('        <div class="ui-pager-items" ms-each-i="range">')
    html.push('        <div ');
    html.push('            ms-click="select(i)"');
    html.push('            ms-class="ui-pager-current:i==index"');
    html.push('            ms-class-2="ui-pager-item:cls2(i,index)"');
    html.push('            ms-class-3="ui-pager-omit:cls3(i)"');
    html.push('            >{{wrap(i)}}</div>');
    html.push('        </div>');
    html.push('        <div ms-click="next" ms-visible="lt(index,count)" class="ui-pager-next">下一页</div>');
    html.push('        <div class="ui-pager-jump">');
    html.push('            <span class="ui-pager-text">共{{count}}页</span>');
    html.push('            <span class="ui-pager-text">，到第</span>');
    html.push('            <div class="ui-textbox">');
    html.push('                <input ms-duplex="gotoindex" type="text" class="ui-textbox-input" />');
    html.push('            </div>');
    html.push('            <span class="ui-pager-text">页</span>');
    html.push('            <button ms-click="go(gotoindex)" class="ui-btn">确定</button>');
    html.push('        </div>');
    html.push('    </div>');

    var model = $$.define( data.pagerId , function(vm){
        vm.$skipArray = ['onselect'];
        // 数据条数
        vm.totalcount = 0;
        // 单页条数
        vm.pagesize = 10;
        // 总页码
        vm.count = 0;
        vm.index = 1;
        vm.gotoindex = 1;
        vm.range = [];

        vm.onselect = function(){}

        vm.gt = function( a , b ) {
            return a > b;
        }

        vm.lt = function( a , b ) {
            return a < b;
        }

        vm.cls1 = function( i , index ){
            return i == index;
        }

        vm.cls2 = function( i , index ) {
            return ~i&&i!=index;
        }

        vm.cls3 = function( i ) {
            return !~i;
        }

        vm.$watch( 'count' , function(){
            vm.range = createPageRange( vm.index , vm.count , omit );
        });

        vm.$watch( 'index' , function(){
            vm.range = createPageRange( vm.index , vm.count , omit );
        });

        vm.$watch('totalcount',function( val ){
            var n = val / vm.pagesize;
            var _n = Math.floor(n);
            vm.count = ( n > _n ) ? _n + 1 : _n;
        });

        vm.$watch('pagesize',function( val ){
            var n = vm.totalcount / val;
            var _n = Math.floor(n);
            vm.count = ( n > _n ) ? _n + 1 : _n;
        })

        vm.wrap = function(i){
            return i == -1 ? '...' : i;
        }

        vm.prev = function(){
            vm.index--;
            vm.onselect && vm.onselect(vm.index);
        }

        vm.next = function(){
            vm.index++;
            vm.onselect && vm.onselect(vm.index);
        }

        vm.select = function(i){
            if( i >= 1 && i <= vm.count && vm.index !=i) {
                vm.index = i;
                vm.onselect && vm.onselect(i);
            }
        }

        vm.go = function(i){
            if( i >= 1 && i <= vm.count ) {
                vm.index = i;
                vm.onselect && vm.onselect(i);
            } else {
                vm.gotoindex = 1;
            }
        }

    });

    element.innerHTML = html.join('');
    $$.scan( element , [model].concat( vmodels ));

    return model;
}


/***
    <div class="ui-pager">
        <div class="ui-pager-current">1</div>
        <div class="ui-pager-item">2</div>
        <div class="ui-pager-item">3</div>
        <div class="ui-pager-omit">...</div>
        <div class="ui-pager-item">100</div>
        <div class="ui-pager-next">下一页</div>
        <div class="ui-pager-jump">
            <span class="ui-pager-text">共100页</span>
            <span class="ui-pager-text">，到第</span>
            <div class="ui-textbox">
                <input type="text" value="1" class="ui-textbox-input" />
            </div>
            <span class="ui-pager-text">页</span>
            <button class="ui-btn">确定</button>
        </div>
    </div>
*/