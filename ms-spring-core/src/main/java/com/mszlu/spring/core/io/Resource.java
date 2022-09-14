package com.mszlu.spring.core.io;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 *
 */
public interface Resource extends InputStreamSource{
    /**
     * 资源是否存在
     * @return
     */
    boolean exists();

    /**
     * 是否可读
     * @return
     */
    default boolean isReadable() {
        return exists();
    }

    /**
     * 该值指示此资源是否表示具有开放流的句柄。
     * 如果为真，则不能多次读取InputStream，并且必须只读取一次，然后关闭，以避免资源泄漏。
     * 除了InputStreamResource之外，所有常用资源实现都为false。
     * @return
     */
    default boolean isOpen() {
        return false;
    }

    default boolean isFile() {
        return false;
    }

    /**
     * 返回资源的URL
     * @return
     * @throws IOException
     */
    URL getURL() throws IOException;

    /**
     * 返回资源的URI
     * @return
     * @throws IOException
     */
    URI getURI() throws IOException;

    /**
     * File
     * @return
     * @throws IOException
     */
    File getFile() throws IOException;

    /**
     * 可读字节Channel，
     * 在任意给定时刻，一个可读取通道上只能进行一个读取操作。
     * 如果某个线程在通道上发起读取操作，那么在第一个操作完成之前，将阻塞其他所有试图发起另一个读取操作的线程。
     * 其他种类的 I/O 操作是否继续与读取操作并发执行取决于该通道的类型。
     * 每一次调用都应该创建一个新的Channel
     * @return
     * @throws IOException
     */
    default ReadableByteChannel readableChannel() throws IOException {
        return Channels.newChannel(getInputStream());
    }

    /**
     * 资源的内容长度
     * @return
     * @throws IOException
     */
    long contentLength() throws IOException;

    /**
     * 最后修改时间
     * @return
     * @throws IOException
     */
    long lastModified() throws IOException;

    /**
     * 根据相对路径创建对应的资源
     * @param relativePath
     * @return
     * @throws IOException
     */
    Resource createRelative(String relativePath) throws IOException;

    /**
     * 资源文件名
     * @return
     */
    String getFilename();

    /**
     * 返回此资源的描述，用于处理该资源时的错误输出。这通常是完全限定的文件名或资源的实际URL。
     * @return
     */
    String getDescription();
}