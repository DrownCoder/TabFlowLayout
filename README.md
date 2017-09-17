# TabFlowLayout
流式布局，仿照网易标签栏实现，可拖拽

![](https://github.com/sdfdzx/TabFlowLayout/blob/master/show.gif)  

# Usage

## *.xml
    <com.study.library.widget.TabMoveLayout
        android:id="@+id/tab_move_layout"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:clickable="true"
        app:itemNum="4"
	/>
### More Properties:
**app:itemNum**:"the count of one line"-int  
**app:widthMargin**:"the width between two tag"-int  
**app:heightMargin**:"the height between two line"-int  
**app:itemWidth**:"the tag's width"-int  
**app:itemHeight**:"the tag's height"-int  
**app:textColor**:"the tag's textcolor"-color  
**app:tabBac**:"the tag's bac"-ref  
**app:textSize**:"the tag's textsize"-dimen  

## *.java
**you just need to bind the datasource and set the tag bac**

    tab_move_layout.setChildView(List<String> data, int bacID);
    
blog:[仿网易新闻标签选择器（可拖动）-TabMoveLayout](http://blog.csdn.net/sdfdzx/article/details/70230959)
