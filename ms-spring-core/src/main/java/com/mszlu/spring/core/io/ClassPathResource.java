package com.mszlu.spring.core.io;


import com.mszlu.spring.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 */
public class ClassPathResource implements Resource{

    private final String path;

    private ClassLoader classLoader;

    private Class<?> clazz;


    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("test.properties");
        File file = classPathResource.getFile();
        System.out.println(file);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : getDefaultClassLoader());
    }
    public ClassPathResource(String path, Class<?> clazz) {
        this.path = StringUtils.cleanPath(path);
        this.clazz = clazz;
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // getClassLoader() returning null indicates the bootstrap ClassLoader
            try {
                cl = ClassLoader.getSystemClassLoader();
            }
            catch (Throwable ex) {
                // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
            }
        }
        return cl;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.clazz != null){
            is = this.clazz.getResourceAsStream(this.path);
        }
        else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        }
        else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (is == null) {
            throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
        }
        return is;
    }

    @Override
    public boolean exists() {
        return (resolveURL() != null);
    }

    protected URL resolveURL() {
        try {
            if (this.classLoader != null) {
                return this.classLoader.getResource(this.path);
            }
            else {
                return ClassLoader.getSystemResource(this.path);
            }
        }
        catch (IllegalArgumentException ex) {
            // Should not happen according to the JDK's contract:
            // see https://github.com/openjdk/jdk/pull/2662
            return null;
        }
    }

    @Override
    public URL getURL() throws IOException {
        URL url = resolveURL();
        if (url == null) {
            throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
        }
        return url;
    }

    @Override
    public URI getURI() throws IOException {
        try {
            return getURL().toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File getFile() throws IOException {
        URL url = getURL();
        return new File(url.getFile());
    }

    @Override
    public long contentLength() throws IOException {
        return getFile().length();
    }

    @Override
    public long lastModified() throws IOException {
        return getFile().lastModified();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        String p;
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith("/")) {
                newPath += "/";
            }
            p = newPath + relativePath;
        }
        else {
            p = relativePath;
        }

        return new ClassPathResource(p,this.classLoader);
    }

    @Override
    public String getFilename() {
        if (path == null) {
            return null;
        }

        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
    }

    @Override
    public String getDescription() {
        return path;
    }

    public String getPath() {
        return path;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
