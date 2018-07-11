支持操作音频，视频，图片，txt，zip文件<br> 
支持查看制定文件类型<br> 
支持音频，视频播放，图片查看，zip解压<br> 
支持单选，多选，最大数量限制，和返回上一级选中<br> 
支持排序<br>
支持指定文件路径访问<br> 
支持样式自定义<br><br>

使用：<br>
1) 在Application中 <br>
FileManageHelp.getInstance()<br>
                .setImgeLoad(IFileImageListener())// 图片加载方式<br>
                .setJumpListener(IJumpListener())//跳转方式 <br>
                .setMaxLength(9, "最大选取数量：9") <br>
                .setCanRightTouch(true)// 滑动删除 <br>
                .setShowHiddenFile(false)// 是否显示隐藏文件 <br>
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP))// 设置过滤规则<br>
                .setSortordByWhat(FileManageHelp.BY_DEFAULT)//设置排序方式<br>
                .setSortord(FileManageHelp.ASC)//升序或降序<br>
                .isShowLog = true//是否显示日志<br>
