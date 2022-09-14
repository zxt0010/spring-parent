package com.mszlu.spring.beans.factory.support;

import com.mszlu.spring.beans.BeanWrapper;
import com.mszlu.spring.beans.BeansException;
import com.mszlu.spring.beans.MutablePropertyValues;
import com.mszlu.spring.beans.PropertyValue;
import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import com.mszlu.spring.beans.factory.NoSuchBeanDefinitionException;
import com.mszlu.spring.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import com.mszlu.spring.beans.factory.annotation.Autowired;
import com.mszlu.spring.beans.factory.config.BeanDefinition;
import com.mszlu.spring.beans.factory.config.BeanPostProcessor;
import com.mszlu.spring.beans.factory.config.BeanReference;
import com.mszlu.spring.beans.factory.config.TypedStringValue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 */
public class DefaultListableBeanFactory  extends AbstractBeanFactory implements BeanDefinitionRegistry{
    /** Map of bean definition objects, keyed by bean name. */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    /** BeanPostProcessors to apply. */
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    private static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES = new HashMap<>();

    static {
        //
//        boolean.class, false,
//                byte.class, (byte) 0,
//                short.class, (short) 0,
//                int.class, 0,
//                long.class, 0L,
//                float.class, 0F,
//                double.class, 0D,
//                char.class, '\0'
//
        DEFAULT_TYPE_VALUES.put(boolean.class,false);
        DEFAULT_TYPE_VALUES.put(byte.class,(byte) 0);
        DEFAULT_TYPE_VALUES.put(short.class,0);
        DEFAULT_TYPE_VALUES.put(long.class,0L);
        DEFAULT_TYPE_VALUES.put(float.class,0F);
        DEFAULT_TYPE_VALUES.put(double.class,0D);
        DEFAULT_TYPE_VALUES.put(char.class,'\0');
    }
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        synchronized (this.beanDefinitionMap) {
            beanDefinitionMap.put(beanName,beanDefinition);
            List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
            updatedDefinitions.addAll(this.beanDefinitionNames);
            updatedDefinitions.add(beanName);
            this.beanDefinitionNames = updatedDefinitions;
        }

    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        BeanDefinition bd = this.beanDefinitionMap.remove(beanName);
        synchronized (this.beanDefinitionMap) {
            List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames);
            updatedDefinitions.remove(beanName);
            this.beanDefinitionNames = updatedDefinitions;
        }
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionNames.toArray(new String[]{});
    }


    @Override
    protected boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return beanDefinitionMap.get(beanName);
    }
    //从xml中读取了bean的定义 <bean id="xx" class="sss" init-method="init" /> -> BeanDefinition
    //使用一个类的时候 实例化  new  BeanDefinition 转换为具体的实例
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        GenericBeanDefinition genericBeanDefinition = (GenericBeanDefinition) beanDefinition;

        Object beanClass = genericBeanDefinition.getBeanClass();
        Object singleton = this.getSingleton(beanName);
        if (singleton != null){
            return singleton;
        }
        Class<?> clazz = (Class<?>) beanClass;
        if (clazz.isInterface()){
            throw new BeansException("bean不能为接口");
        }
        try {
            Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
            Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0 ; i < args.length; i++) {
                if (args[i] == null) {
                    Class<?> parameterType = parameterTypes[i];
                    argsWithDefaultValues[i] = (parameterType.isPrimitive() ? DEFAULT_TYPE_VALUES.get(parameterType) : null);
                }
                else {
                    argsWithDefaultValues[i] = args[i];
                }
            }
            try {
                Object instance = declaredConstructor.newInstance(argsWithDefaultValues);
                Object exposedObject = instance;
                //先生成代理实例
                Object beanProxy = this.postProcessBeforeProxy(beanName,instance);
                //aop 生成的代理类 才需要扔进工厂缓存，如果不需要代理 放入的就是原有的实例
                if (beanProxy instanceof BeanWrapper){
                    BeanWrapper beanWrapper = (BeanWrapper) beanProxy;
                    if (beanWrapper.isAop()){
                        this.addSingletonFactory(beanName,()-> beanWrapper.getBean());
                    }else{
                        this.addSingletonFactory(beanName,()-> instance);
                    }
                }else{
                    this.addSingletonFactory(beanName,()-> instance);
                }


                //解决循环依赖，把已经实例化好的bean，提前暴露出去
//                addEarlySingletonObjects(beanName,instance);
                //判断是否有proerty，有就进行设置  自动注入
                if (beanDefinition.hasPropertyValues()){
                    MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
                    for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                        Field declaredField = null;
                        try {
                            declaredField = clazz.getDeclaredField(propertyValue.getName());
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        declaredField.setAccessible(true);
                        Object value = propertyValue.getValue();
                        if (value instanceof TypedStringValue){
                            declaredField.set(exposedObject, ((TypedStringValue)value).getValue());
                        }
                        if (value instanceof BeanReference){
                            BeanReference beanReference = (BeanReference) value;
                            Object bean = this.getBean(beanReference.getBeanName());
                            declaredField.set(exposedObject,bean);
                        }
                    }
                }
                //annotation判断
                if (beanDefinition instanceof AnnotatedGenericBeanDefinition){
                    Field[] declaredFields = clazz.getDeclaredFields();
                    for (Field declaredField : declaredFields) {
                        Autowired annotation = declaredField.getAnnotation(Autowired.class);
                        if (annotation != null){
                            declaredField.setAccessible(true);
                            declaredField.set(exposedObject,this.getBean(declaredField.getName()));
                        }
                    }
                }
                //初始化之前 执行 BeanPostProcessor的before方法
                exposedObject = postProcessBefore(beanName,exposedObject);
                String initMethodName = genericBeanDefinition.getInitMethodName();
                if (initMethodName != null && !initMethodName.isEmpty()){
                    Method method = clazz.getMethod(initMethodName, null);
                    method.invoke(instance,null);
                }
                //初始化之后 执行 BeanPostProcessor的after方法
                exposedObject = postProcessAfter(beanName,exposedObject);

                Object earlySingletonReference = getSingleton(beanName);
                if (earlySingletonReference != null){
                    exposedObject = earlySingletonReference;
                }
                return exposedObject;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object postProcessAfter(String beanName, Object instance) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            try {
                instance = beanPostProcessor.postProcessAfterInitialization(instance,beanName,this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    private Object postProcessBeforeProxy(String beanName, Object instance) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            try {
                //aop 实现
                instance = beanPostProcessor.postProcessBeforeInitialization(instance,beanName,this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    private Object postProcessBefore(String beanName, Object instance) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            instance = beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
        }
        return instance;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object singleton = getSingleton(beanName);
        if (singleton == null){
            if (beanDefinitionMap.containsKey(beanName)){
                if (isSingleton(beanName)){
                    BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                    Object bean = createBean(beanName, beanDefinition,new Object[]{});
                    registerSingleton(beanName,bean);
                    return bean;
                }
            }
        }
        return singleton;
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        // Remove from old position, if any
        this.beanPostProcessors.remove(beanPostProcessor);
        // Add to end of list
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public String[] getBeanPostProcessors(){
        List<String> beanDefinitionNames = this.beanDefinitionNames;
        List<String> names = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanDefinitionMap.get(beanDefinitionName);
            Object beanClass = beanDefinition.getBeanClass();
            Class<?> clazz = (Class<?>) beanClass;
            try {
                Constructor<?> declaredConstructor = clazz.getDeclaredConstructor();
                Object instance = declaredConstructor.newInstance(null);
                boolean isInstance = BeanPostProcessor.class.isInstance(instance);
                if (isInstance){
                    names.add(beanDefinitionName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return names.toArray(new String[]{});
    }
}
