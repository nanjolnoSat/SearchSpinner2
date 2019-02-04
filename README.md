# SearchSpinner2
不清楚怎么回事,创建一个module,然后通过app关联,总是报奇怪的错误,所以只能集成在app目录里面<br/>
效果图:<br/>
<img src="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/pic1.gif"/><img src="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/pic2.gif"/><br/>
使用方式:<br/>
1,可以继承SearchSpinner<T>,然后给个泛型,这样在findViewById的时候就不用强转,并给<b>filterMode</b>这个变量赋值,这个是负责当输入框有内容的时候,根据输入框的内容设置是否显示遍历到的内容.<b>contentMode</b>可以不赋值,如果空的话直接调用该对象分的toString()方法.<br/>
2,也可以直接使用StringSearchSpinner,<b>filterMode</b>默认是判断输入遍历到的字符串是否包含输入的内容.<br/>
3,BaseSpinnerAdapter需要重写2个方法,一个是<b>getNormalView</b>,这个方法的用法和ListView的getView是一样的,然后是<b>getSearchView</b>.这个是当输入框有内容的时候调用,如果输入框有内容和没有内容的显示方式是一样的话,就直接调用getNormalView方法就行了.在<a href="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/app/src/main/java/com/mishaki/searchspinner2/adapter/StringSpinnerAdapter.kt">StringSpinnerAdapter</a>和<a href="https://github.com/nanjolnoSat/SearchSpinner2/blob/master/app/src/main/java/com/mishaki/searchspinner2/adapter/StringMarkSpinnerAdapter.kt">StringMarkSpinnerAdapter</a>给出了2种不同的用法.
