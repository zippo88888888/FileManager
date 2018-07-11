支持操作音频，视频，图片，txt，zip文件<br> 
支持查看制定文件类型<br> 
支持音频，视频播放，图片查看，zip解压<br> 
支持单选，多选，最大数量限制，和返回上一级选中<br> 
支持排序<br>
支持指定文件路径访问<br> 
支持样式自定义<br><br>

使用：<br>
1) 在Application中 <br>
FileManageHelp.getInstance()
                .setImgeLoad(IFileImageListener()) // 设置图片加载
                .setJumpListener(IJumpListener()) // 设置自定义跳转
                .setMaxLength(9, "最大选取数量：9") 
                .setCanRightTouch(true) // 是否可以滑动删除
                .setShowHiddenFile(false) // 是否显示隐藏文件
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP)) // 设置过滤规则，不写或null访问所有文件类型
                .setSortordByWhat(FileManageHelp.BY_DEFAULT) // 设置排序
                .setSortord(FileManageHelp.ASC) // 升序还是降序
                .isShowLog = true // 是否显示日志
