package com.mszlu.spring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public interface InputStreamSource {
    /**
     * 定位并打开资源，返回用于从资源读取的输入流，当加载的资源不存在的时候，抛出文件找不到的IO异常
     * 调用者使用完后，应当关闭流
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;
}
