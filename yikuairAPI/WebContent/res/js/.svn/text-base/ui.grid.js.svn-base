

var attr = function( elem ) {
    var s = [];
    for( var i = 0, attr; attr = elem.attributes[i++]; ) {
        s.push( attr.name + '="' , attr.value , '"' );
    }
    return s.join(' ');
}

var wrapTable = function( type , $table , opts , fn ) {

    var ths = $table.find('thead td, thead th');
    var tds = $table.find('tbody td');

    var html = [];
    html.push('<div ');
    if( type == 'content' ) {
         html.push(' ms-visible="list.length" ');
    }
    html.push(' class="ui-grid-' , type , '">');
    html.push('    <div class="ui-grid-' , type , '-inner">');
    html.push('        <table>');
    html.push('            <colgroup>');
    if( opts.checkall ) {
        html.push('                <col style="width:23px" />');
    }
    $.each( ths , function(){
        html.push('                <col style="width:' , $(this).data('width') , ';" />');
    });
    html.push('            </colgroup>');

    switch( type ) {
        case 'header':
            html.push('            <thead>');
            html.push('            <tr>');
            if( opts.checkall  )  {
                html.push('        <th><input ms-click="click_all($event)" ms-duplex-radio="all" type="checkbox" class="ui-grid-select" /></th>');
            }
            $.each( ths , function(){
                html.push('                <th ' , attr(this) , '>' , fn ? fn( this ) : this.innerHTML , '</th>');
            });
            html.push('            </tr>');
            html.push('            </thead>');
            break;
        case 'content':
            html.push('            <tbody ms-each-item="list">');
            html.push('            <tr>');
            if( opts.checkall ) {
                html.push('        <td><input ms-click="click_one($event, $index)" ms-checked="~checkStatus.indexOf($index)" type="checkbox" class="ui-grid-select" /></td>');
            }
            $.each( tds , function(){
                html.push('                <td ' , attr(this) , ' >' , fn ? fn( this ) : this.innerHTML , '</td>');
            });
            html.push('            </tr>');
            html.push('            </tbody>');
            break;

    }

    html.push('        </table>');
    html.push('    </div>');
    html.push('</div>');

    return html.join('');

}

var wrapNoData = function( $block , opts ){
    if( !$block.length ) return "";
    var html = [];
    html.push('<div ms-visible="!list.length" class="ui-grid-nodata">');
    html.push( $block.html() );
    html.push('</div>');
    return html.join('');
}

var wrapPager = function( opts ){
    var html = [];
    html.push('<div class="ui-grid-footer clrfix">');
    html.push('    <div class="ui-grid-pagebar">');
    html.push('        <div ms-widget="pager,' , opts.pagerId , '"></div>');
    html.push('    </div>');
    html.push('</div>');
    return html.join('');
}

/**
    ms-widget="grid"
    ms-grid-checkall="true"
    ms-grid-onselectpage="xxx"      // 选择页码时触发
 */
avalon.ui["grid"] = function( element , data , vmodels ) {

    var $e = $(element);
    var opts = data.gridOptions;
    opts.pagerId = data.gridId + "_pager";

    // 重组内容

    var t = $e.find('table');
    var f = document.createDocumentFragment();
    var html = [];

    html.push( wrapTable( 'header' , t , opts ) );
    html.push( wrapTable( 'content' , t , opts ) );
    html.push( wrapNoData( $e.find('blockquote') , opts ) );
    html.push( wrapPager( opts ) );

    var model = $$.define( data.gridId , function(vm){

        vm.all = false;
        vm.checkStatus = [];
        vm.list = [];

        vm.setData = function( list ) {
            vm.all = false;
            vm.checkStatus = [];
            vm.list = list;
        }
        vm.setPageCount = function( count ) {
            var pager = $$.$ui( opts.pagerId );
            pager.count = count;
            pager.index = 1;
        }
        vm.click_all = function( $evt ){
            if( $evt.target.checked ) {
                vm.checkStatus = $$.range(0,vm.list.$model.length);
            } else {
                vm.checkStatus = [];
            }
        }
        vm.click_one = function( $evt , index ){
            if( $evt.target.checked ) {
                vm.checkStatus.ensure( index );
            } else {
                vm.checkStatus.remove( index );
            }
            vm.all = vm.checkStatus.length == vm.list.length;
        }
        vm.getSelectedItems = function(){
            return $.map( vm.checkStatus.$model , function(idx , i){
                return vm.list[idx].$model;
            })
        }

        vm.getPager = function(){
            return $$.$ui( opts.pagerId );
        }

    });

    element.innerHTML = html.join('');
    $$.scan( element , [model].concat( vmodels ));

    var pager = $$.$ui( opts.pagerId );
    pager.onselect = function(i){
        if( !opts.onselectpage ) return;
        var arr = $$.parseExpr( opts.onselectpage , vmodels , data );
        var fn = arr[0].apply( 0 , arr[1] );
        fn(i);
    }

    return model;
};


/*


        <div class="ui-grid-header">
            <div class="ui-grid-header-inner">
                <table>
                    <colgroup>
                        <col style="width:23px" />
                        <col style="width:150px" />
                        <col style="width:auto" />
                        <col style="width:150px" />
                        <col style="width:60px" />
                        <col style="width:60px" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th><input type="checkbox" class="ui-grid-select" /></th>
                            <th>规则名称</th>
                            <th>规则内容</th>
                            <th>适用产品</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
        <div class="ui-grid-nodata">尚未新建酒店促销规则，<a href="javascript:void(0);">新建酒店促销规则</a></div>
        <div class="ui-grid-content">
            <table>
                <colgroup>
                    <col style="width:23px" />
                    <col style="width:150px" />
                    <col style="width:auto" />
                    <col style="width:150px" />
                    <col style="width:60px" />
                    <col style="width:60px" />
                </colgroup>
                <tbody>
                    <tr>
                        <td><input type="checkbox" class="ui-grid-select" /></td>
                        <td>连住3晚</td>
                        <td>
                            <ul>
                                <li>预订日期在2013-10-01至2014-05-01</li>
                                <li>连住3晚起，第3晚及以后优惠10%</li>
                            </ul>
                        </td>
                        <td>
                            <ul class="mod-list">
                                <li class="item">大床房-无早</li>
                                <li class="item">标准间-无早</li>
                                <li class="item">大床房-单早</li>
                            </ul>
                        </td>
                        <td><i class="g-icon-start ui-icon ui-icon-circle">&#xf111;</i></td>
                        <td><a href="javascript:void(0);">修改</a></td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" class="ui-grid-select" /></td>
                        <td>连住3晚</td>
                        <td>
                            <ul>
                                <li>预订日期在2013-10-01至2014-05-01</li>
                                <li>连住3晚起，第3晚及以后优惠10%</li>
                            </ul>
                        </td>
                        <td>
                            <ul class="mod-list">
                                <li class="item">大床房-无早</li>
                            </ul>
                        </td>
                        <td><i class="g-icon-pause ui-icon ui-icon-pause">&#xf04c;</i></td>
                        <td><a href="javascript:void(0);">修改</a></td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="ui-grid-footer clrfix">
            <div class="ui-grid-pagebar">
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
            </div>
        </div>


*/
