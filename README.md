# SearchSpinner2
**2020/09/01更新**
主要涉及以下5个方面
 - 开发语言换成Java
 - 将ListView替换成RecyclerView
 - 将SearchSpinner独立成一个Module
 - 迁移至androidX
 - 对代码进行一定程度上的优化

下一次的优化计划：优化代码结构。更新时间：未知。现在SearchSpinner里面一堆set方法，看起来特别乱（这个方面还是觉得Kotlin好）。虽然用各种注释区分，但方法太多，还是没办法从根本上解决乱这个问题。<br/>
更换过程只是简单粗暴将Kotlin语言翻译成Java，并对小部分代码优化，测试只是从功能上进行测试，并没有检查代码逻辑。所以没办法保证和修改前完全一致，有Bug麻烦提issues，看到并且有时间的话会修改。
<br/>
效果图:<br/>
<img src="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/pic1.gif"/><img src="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/pic2.gif"/><br/>
使用方式:<br/>
1：可以继承SearchSpinner<T>，然后给个泛型，这样在findViewById的时候就不用强转，并给<b>filterModel</b>这个变量赋值，
这个是负责当输入框有内容的时候，根据输入框的内容设置是否显示遍历到的内容。<b>contentModel</b>可以不赋值，如果空的话直接调用该对象分的toString()方法。<br/>
2：也可以直接使用StringSearchSpinner,<b>filterModel</b>默认是判断输入遍历到的字符串是否包含输入的内容.<br/>
3：继承BaseSpinnerAdapter的用法，该类需要指定2个泛型：数据类型和ViewHolder。ViewHolder提供了SimpleViewHolder，内部并没有做复杂的实现，只是直接继承。<br/>
BaseSpinnerAdapter需要重写4个方法：onCreateNormalViewHolder、onCreateSearchViewHolder、onBindNormalViewHolder和onBindSearchViewHolder。<br/>
其实就是将onCreateViewHolder和onBindViewHolder两个方法拆成四个，用法见名知义。<br/>
如果两个search方法的逻辑和两个normal方法的逻辑是一样的话，那可以继承SimpleSpinnerAdapter。
<h3>SearchSpinner自带的属性</h3>
<table>
<tr>
<th>属性名称</th>
<th>类型</th>
<th>注释</th>
</tr>
<tr align="center">
<td>ss_elevationSize</td>
<td>dimension</td>
<td>阴影的大小</td>
</tr>
<tr align="center">
<td>ss_adapterItemHeight</td>
<td>dimension</td>
<td>adapter的高度</td>
</tr>
<tr align="center">
<td>ss_defaultText</td>
<td>string</td>
<td>默认的文本</td>
</tr>
<tr align="center">
<td>ss_textColor</td>
<td>color</td>
<td>默认/选择后显示的文本的颜色</td>
</tr>
<tr align="center">
<td>ss_textSize</td>
<td>dimension</td>
<td>默认/选择后文本的大小</td>
</tr>
<tr align="center">
<td>ss_showArrow</td>
<td>boolean</td>
<td>是否显示三角形</td>
</tr>
<tr align="center">
<td>ss_changeArrowColor</td>
<td>boolean</td>
<td>是否改变三角形的颜色,<b>默认不改变</b></td>
</tr>
<tr align="center">
<td>ss_arrowColor</td>
<td>color</td>
<td>如果改变的话改变的颜色,默认<b>#FFAAAAAA</b></td>
</tr>
<tr align="center">
<td>ss_arrowImage</td>
<td>color|reference</td>
<td>三角形的图片,<b>要更换</b>的时候才使用</td>
</tr>
<tr align="center">
<td>ss_arrowWidth</td>
<td>dimension</td>
<td>三角形的宽度,不设置使用<b>原始高度</b></td>
</tr>
<tr align="center">
<td>ss_arrowHeight</td>
<td>dimension</td>
<td>三角形的高度,不设置使用<b>原始高度</b></td>
</tr>
<tr align="center">
<td>ss_tipText</td>
<td>string</td>
<td>当没有搜索结果的时候,提示的文本,默认<b>暂无搜索结果</b></td>
</tr>
<tr align="center">
<td>ss_tipTextColor</td>
<td>color</td>
<td>当没有搜索结果的时候,提示的文本的<b>颜色</b>,默认<b>@android:color/black</b></td>
</tr>
<tr align="center">
<td>ss_tipTextSize</td>
<td>dimension</td>
<td>当没有搜索结果的时候,提示的文本的<b>大小</b>,默认<b>15sp</b></td>
</tr>
<tr align="center">
<td>ss_tipViewHeight</td>
<td>dimension</td>
<td>当没有搜索结果的时候,提示的文本的<b>高度</b>,默认跟随根View的高度</td>
</tr>
<tr align="center">
<td>ss_searchTextSize</td>
<td>dimension</td>
<td>搜索框的文本的<b>大小</b>,默认<b>15sp</b></td>
</tr>
<tr align="center">
<td>ss_searchTextColor</td>
<td>color</td>
<td>搜索框的文本的<b>颜色</b>,默认<b>#FF000000</b></td>
</tr>
<tr align="center">
<td>ss_searchHint</td>
<td>string</td>
<td>搜索框<b>提示的文本</b>,默认空</td>
</tr>
<tr align="center">
<td>ss_searchHintColor</td>
<td>color</td>
<td>搜索框<b>提示的文本的颜色</b>,默认<b>#FFFAFAFA</b></td>
</tr>
<tr align="center">
<td>ss_searchHeight</td>
<td>dimension</td>
<td>搜索框的<b>高度</b>,默认跟随根View的高度</td>
</tr>
</table>
<h3>StringSearchSpinner附带的属性</h3>
<table>
<tr>
<th>属性名称</th>
<th>类型</th>
<th>注释</th>
</tr>
<tr align="center">
<td>ss_ignoreCase</td>
<td>boolean</td>
<td>搜索的时候是否忽略大小写</td>
</tr>
</table>
