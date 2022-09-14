package com.mszlu.spring.context.support;

import com.mszlu.spring.aop.aspectj.AspectJAdvice;
import com.mszlu.spring.aop.framework.autoproxy.AutoProxyCreator;
import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.factory.BeanFactory;
import com.mszlu.spring.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import com.mszlu.spring.beans.factory.config.BeanDefinition;
import com.mszlu.spring.beans.factory.config.BeanPostProcessor;
import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mszlu.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mszlu.spring.beans.factory.xml.BeanDefinitionHolder;
import com.mszlu.spring.context.annotation.Component;
import com.mszlu.spring.context.annotation.ComponentScan;
import com.mszlu.spring.context.annotation.Configuration;
import com.mszlu.spring.context.stereotype.Controller;
import com.mszlu.spring.context.stereotype.Service;
import com.mszlu.spring.core.io.FileSystemResource;
import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.utils.ClassUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class PostProcessorRegistrationDelegate {

    private static Map<String,List<AspectJAdvice>> aspectMethods = new HashMap<>();

    private static List<String> patterns = new ArrayList<>();

    public static void registerBeanPostProcessors(DefaultListableBeanFactory beanFactory) {
        //注册到IOC容器中的beanDefinition 判断其是否为BeanPostProcessor的子类
        //如果是，将其缓存起来，调用getBean将其实例获取到，并进行缓存，以便其他地方使用
        String[] postProcessorNames = beanFactory.getBeanPostProcessors();
        for (String postProcessorName : postProcessorNames) {
            Object bean = beanFactory.getBean(postProcessorName);
            beanFactory.addBeanPostProcessor((BeanPostProcessor) bean);
        }
    }

    public static void invokeBeanFactoryPostProcessors(DefaultListableBeanFactory beanFactory) {
        //2. 找到配置类上方的注解 ComponentScan 获取需要扫包的路径
        //3. 根据扫包路径 扫描其下的所有 class文件，然后找到哪些是加了@Service @Component等等这些注解
        //4. beanFactory进行管理 生成BeanDefinition注册，getBean时候 判断是否注册为单例，没有实例化，并注册
        processConfigBeanDefinitions(beanFactory);
        BeanPostProcessor autoProxyCreator = new AutoProxyCreator(aspectMethods,patterns);
        beanFactory.addBeanPostProcessor(autoProxyCreator);
    }

    private static void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
        List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
        String[] candidateNames = registry.getBeanDefinitionNames();

        for (String beanName : candidateNames) {
            AnnotatedGenericBeanDefinition beanDef = (AnnotatedGenericBeanDefinition) registry.getBeanDefinition(beanName);
            //判断其是否为配置类
            Annotation[] annotations = beanDef.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Configuration){
                    configCandidates.add(new BeanDefinitionHolder(beanDef,beanName));
                }
            }
        }

        // Return immediately if no @Configuration classes were found
        if (configCandidates.isEmpty()) {
            return;
        }

        for (BeanDefinitionHolder configCandidate : configCandidates) {
            //找componentScan注解 获取需要扫描的class文件
            AnnotatedGenericBeanDefinition beanDefinition = (AnnotatedGenericBeanDefinition) configCandidate.getBeanDefinition();
            Annotation[] annotations = beanDefinition.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof ComponentScan){
                    ComponentScan componentScan = (ComponentScan) annotation;
                    String[] value = componentScan.value();
                    for (String scanPackages : value) {
                        Set<BeanDefinition> candidates = findCandidateComponents(scanPackages);
                        for (BeanDefinition candidate : candidates) {
                            String beanName = candidate.getBeanClassName();
                            if (candidate instanceof AnnotatedGenericBeanDefinition){
                                AnnotatedGenericBeanDefinition b = (AnnotatedGenericBeanDefinition) candidate;
                                Annotation[] as = b.getAnnotations();
                                for (Annotation an : as){
                                    if (an instanceof Component){
                                        beanName = ((Component)an).value();
                                    }
                                    if (an instanceof Service){
                                        beanName = ((Service)an).value();
                                    }
                                    if (an instanceof Controller){
                                        beanName = ((Controller)an).value();
                                    }
                                }
                            }
                            if (beanName == null || beanName.equals("")){
                                throw new BeansException("bean value 必须有值");
                            }
                            if (registry.getBeanDefinition(beanName) == null) {
                                registry.registerBeanDefinition(beanName, candidate);
                            }
                            if (candidate instanceof AnnotatedGenericBeanDefinition){
                                AnnotatedGenericBeanDefinition b = (AnnotatedGenericBeanDefinition) candidate;
                                Annotation[] as = b.getAnnotations();
                                for (Annotation an : as){
                                    if (an instanceof Aspect){
                                        //如果切面 查找 切点 和 通知
                                        Class<?> target = (Class<?>) b.getBeanClass();
                                        Method[] methods = target.getMethods();
                                        Map<String,String> pointCutMap = new HashMap<>();
                                        for (Method method : methods){
                                            if (method.isAnnotationPresent(Pointcut.class)){
                                                Pointcut pointcut = method.getAnnotation(Pointcut.class);
                                                String value1 = pointcut.value();
                                                if (value1.startsWith("execution")){
                                                    //正则表达式
                                                    String pattern = "execution([\\s\\S]*)";
                                                    Pattern compile = Pattern.compile(pattern);
                                                    Matcher matcher = compile.matcher(value1);
                                                    if (matcher.find()){
                                                        String group = matcher.group(1);
                                                        group = group.replaceFirst("\\(","");
                                                        group = group.substring(0,group.length() -1);
                                                        pointCutMap.put(method.getName(),group);
                                                        patterns.add(group);
                                                    }
                                                }
                                            }
                                        }
                                        for (Method method : methods){
                                            BeanFactory beanFactory = null;
                                            if (registry instanceof BeanFactory){
                                                beanFactory = (BeanFactory) registry;
                                            }
                                            if (method.isAnnotationPresent(Before.class)){
                                                Before before = method.getAnnotation(Before.class);
                                                String s = pointCutMap.get(before.value().replace("()", ""));
                                                if (s != null){
                                                    List<AspectJAdvice> aspectJAdvices = aspectMethods.get(s);
                                                    if (aspectJAdvices == null){
                                                        aspectJAdvices = new ArrayList<>();
                                                    }
                                                    aspectJAdvices.add(new AspectJAdvice(method,beanFactory.getBean(beanName)));
                                                    aspectMethods.put(s,aspectJAdvices);
                                                }
                                            }
                                            if (method.isAnnotationPresent(After.class)){
                                                After after = method.getAnnotation(After.class);
                                                String s = pointCutMap.get(after.value().replace("()", ""));
                                                if (s != null){
                                                    List<AspectJAdvice> aspectJAdvices = aspectMethods.get(s);
                                                    if (aspectJAdvices == null){
                                                        aspectJAdvices = new ArrayList<>();
                                                    }
                                                    aspectJAdvices.add(new AspectJAdvice(method,beanFactory.getBean(beanName)));
                                                    aspectMethods.put(s,aspectJAdvices);
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private static Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        Resource[] resources = getResources(basePackage);
        for (Resource resource : resources) {
            //TODO
            URL[] urls = new URL[0];
            try {
                URL url = resource.getURL();
                urls = new URL[]{url};
                URLClassLoader classLoader = new URLClassLoader(urls);
                String path = url.getPath();
                if (path.contains("classes")){
                    String[] classes = path.split("classes");
                    String className = classes[1];
                    className = className.replace("/",".");
                    className = className.substring(1,className.indexOf(".class"));
                    Class<?> clazz = Class.forName(className);
//                    Class<?> clazz = classLoader.loadClass(className);
                    Annotation[] annotations = clazz.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation instanceof Configuration ||
                                annotation instanceof Component ||
                                annotation instanceof Service ||
                                annotation instanceof Controller){
                            AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(clazz);
                            beanDefinition.setAnnotations(annotations);
                            candidates.add(beanDefinition);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return candidates;
    }

    private static Resource[] getResources(String basePackage) {
        //com.mszlu.spring.mall -> com/mszlu/spring/mall
        String path = basePackage.replace(".","/");
        URL resource = ClassUtils.getDefaultClassLoader().getResource(path);
        List<Resource> resourceList = new ArrayList<>();
        try {
            File file = new File(resource.toURI().getSchemeSpecificPart());
            scan(file,resourceList);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return resourceList.toArray(new Resource[0]);
    }

    private static void scan(File file, List<Resource> resourceList) {
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.isFile()){
                    FileSystemResource fileSystemResource = new FileSystemResource(f);
                    resourceList.add(fileSystemResource);
                }else {
                    scan(f, resourceList);
                }
            }
        }
    }

}
