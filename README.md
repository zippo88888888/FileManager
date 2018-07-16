采用Kotlin编码， SwipeMenuView 该文件使用方式请参考https://github.com/jdsjlzx/LRecyclerView<br><br>
支持操作音频，视频，图片，txt，zip，word，excel，ppt，pdf等文件<br> 
支持查看指定文件类型<br> 
支持音频，视频播放，图片查看，zip解压<br> 
支持多选，最大数量限制<br> 
支持实时排序<br>
支持指定文件路径访问<br> 

使用：<br>
1) 在Application中 <br><br>
FileManageHelp.getInstance()<br>
                .setImgeLoad(IFileImageListener()) // 图片加载方式<br>
                .setJumpListener(IJumpListener()) // 跳转方式 <br>
                .setMaxLength(9, "最大选取数量：9") <br>
                .setCanRightTouch(true) // 滑动删除 <br>
                .setShowHiddenFile(false) // 是否显示隐藏文件 <br>
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP)) // 设置过滤规则<br>
                .setSortordByWhat(FileManageHelp.BY_DEFAULT) // 设置排序方式<br>
                .setSortord(FileManageHelp.ASC) // 升序或降序<br>
                .isShowLog = true // 是否显示日志<br><br>
2) 在Activity或Fragment中<br><br>
FileManageHelp.getInstance().start(this) // 默认SD卡根目录<br>
FileManageHelp.getInstance().start(this,"指定目录")<br>
实现 FileResultListener 接口 重写 resultSuccess(list:ArrayList\<FileBean\>?)方法 <br><br>
3) 文件类型拓展 <br><br>
如果上述类型不能满足，可自定义文件类型！<br>
nbsp;nbsp;nbsp;1、继承自FileType，实现里面的方法<br>
nbsp;nbsp;nbsp;2、继承自FileType，实现里面的方法<br>
3）关于自定义<br>
查看 file 工程里面的 drawable,values里面的值，并在主工程目录下的相同位置 保持命名一致即可替换 颜色，图片，选中样式，或者自己修改file工程里面的样式

<br>zip解压，剪切，复制，粘贴功能还在努力将项目中的原代码独立出来...

 
