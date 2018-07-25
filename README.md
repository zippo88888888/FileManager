采用Kotlin编码， SwipeMenuView 该文件使用方式请参考https://github.com/jdsjlzx/LRecyclerView<br><br>
支持操作音频，视频，图片，txt，zip，word，excel，ppt，pdf等文件<br> 
支持查看指定文件类型<br> 
支持音频，视频播放，图片查看，zip解压<br> 
支持多选，最大数量限制<br> 
支持实时排序<br>
支持指定文件路径访问<br> 

使用：<br>
1) 在Application中，请根据需要自行添加，初始均为为默认值<br><br>
FileManageHelp.getInstance()<br>
                .setFileTypeListener(FileTypeListener()) // 获取文件类型<br>
                .setImgeLoad(FileImageListener()) // 图片加载方式<br>
                .setJumpListener(JumpByTypeListener()) // 跳转方式 <br>
                .setFileInfoListener(FileInfoListener()) // 文件详情 <br>
                .setMaxLength(9, "最大选取数量：9") <br>
                .setCanRightTouch(true) // 滑动删除 <br>
                .setShowHiddenFile(false) // 是否显示隐藏文件 <br>
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP)) // 设置过滤规则<br>
                .setSortordByWhat(FileManageHelp.BY_DEFAULT) // 设置排序方式<br>
                .setSortord(FileManageHelp.ASC) // 升序或降序<br>
                .isShowLog = true // 是否显示日志<br><br>
2) 在Activity或Fragment中<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、FileManageHelp.getInstance().start(this) // 默认SD卡根目录<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;或FileManageHelp.getInstance().start(this,"指定目录")<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、实现 FileResultListener 接口 重写 resultSuccess(list:ArrayList\<FileBean\>?)方法 <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、FileManageHelp.getInstance().fileResultListener = this 
<br><br>
3) 文件类型拓展 <br><br>
如果上述类型不能满足，可自定义文件类型！ 请注意：以下 " : " 是继承 ，不是 冒号<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1、新建一个类 : FileType，重写里面的openFile()、loadingFile()方法<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2、新建一个类 : FileTypeListener，重写里面的getFileType()方法(参考FileTypeListener)<br><br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;3、新建一个类 : JumpByTypeListener，自己新建jump()方法(参考JumpByTypeListener)<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;4、在Application中<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;FileManageHelp.getInstance().setFileTypeListener(FileTypeListener()).setJumpListener(JumpByTypeListener())<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;5、在openFile()方法中直接调用第3步的方法即可
<br><br>
4）关于自定义<br>
查看 file 工程里面的 drawable,values里面的值，并在主工程目录下的相同位置 保持命名一致即可替换 颜色，图片，选中样式，或者自己修改file工程里面的样式



 
