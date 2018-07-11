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
                .setImgeLoad(IFileImageListener())<br>
                .setJumpListener(IJumpListener()) <br>
                .setMaxLength(9, "最大选取数量：9") <br>
                .setCanRightTouch(true) <br>
                .setShowHiddenFile(false) <br>
                .setFileFilterArray(arrayOf(PNG, JPG, GIF, MP3, AAC, MP4, _3GP, TXT, ZIP))<br>
                .setSortordByWhat(FileManageHelp.BY_DEFAULT)<br>
                .setSortord(FileManageHelp.ASC)<br>
                .isShowLog = true<br>
