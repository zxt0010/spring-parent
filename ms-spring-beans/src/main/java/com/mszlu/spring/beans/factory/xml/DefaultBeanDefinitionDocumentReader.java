package com.mszlu.spring.beans.factory.xml;

import com.mszlu.spring.beans.PropertyValue;
import com.mszlu.spring.beans.factory.BeanDefinitionStoreException;
import com.mszlu.spring.beans.factory.config.BeanDefinition;

import com.mszlu.spring.beans.factory.config.RuntimeBeanReference;
import com.mszlu.spring.beans.factory.config.TypedStringValue;
import com.mszlu.spring.beans.factory.support.AbstractBeanDefinition;
import com.mszlu.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mszlu.spring.beans.factory.support.GenericBeanDefinition;
import com.mszlu.spring.core.io.Resource;
import com.mszlu.spring.utils.ClassUtils;
import com.mszlu.spring.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader{

    public static final String BEAN_ELEMENT = "bean";

    public static final String NESTED_BEANS_ELEMENT = "beans";

    public static final String ALIAS_ELEMENT = "alias";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String ALIAS_ATTRIBUTE = "alias";

    public static final String IMPORT_ELEMENT = "import";

    public static final String RESOURCE_ATTRIBUTE = "resource";

    public static final String PROFILE_ATTRIBUTE = "profile";

    private XmlReaderContext readerContext;

    private BeanDefinitionParserDelegate delegate;

    private final Set<String> usedNames = new HashSet<>();

    @Override
    public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) throws BeanDefinitionStoreException {
        this.readerContext = readerContext;
        doRegisterBeanDefinitions(doc.getDocumentElement());
    }

    private void doRegisterBeanDefinitions(Element root) {
        BeanDefinitionParserDelegate parent = this.delegate;
        this.delegate = createDelegate(getReaderContext(), root, parent);
        parseBeanDefinitions(root, this.delegate);
        this.delegate = parent;
    }


    protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
        try {
            parseDefaultElement(root, delegate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseDefaultElement(Element root, BeanDefinitionParserDelegate delegate) throws Exception {
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                //元素节点
                System.out.println(node.getNodeName());
                if ("bean".equals(node.getNodeName())){
                    Element element = (Element) node;
                    parseBeanDefinitionElement(element);
                }
            }

        }
    }


    private void parseBeanDefinitionElement(Element ele) throws Exception {
        String id = ele.getAttribute("id");
        String beanName = id;
        if (beanName == null || beanName.isEmpty()){
            throw new Exception("bean 必须要有 id属性");
        }
        checkUniqueName(beanName);
        //读取class 属性，生成BeanDefinition
        String className = ele.getAttribute("class");
        AbstractBeanDefinition bd = createBeanDefinition(className);

        // 读取attribute
        parseBeanDefinitionAttributes(ele, beanName, bd);

        //添加property属性读取
        parsePropertyElements(ele, bd);

        bd.setResource(readerContext.getResource());
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(bd, beanName);
        BeanDefinitionRegistry registry = this.readerContext.getRegistry();
        registry.registerBeanDefinition(beanName, beanDefinitionHolder.getBeanDefinition());
    }

    private void parseBeanDefinitionAttributes(Element ele, String beanName, AbstractBeanDefinition bd) {
        if (ele.hasAttribute("init-method")) {
            String initMethodName = ele.getAttribute("init-method");
            bd.setInitMethodName(initMethodName);
        }
    }

    private void parsePropertyElements(Element beanEle, AbstractBeanDefinition bd) {
        NodeList nl = beanEle.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if ("property".equals(node.getNodeName())) {
                parsePropertyElement((Element) node, bd);
            }
        }
    }

    private void parsePropertyElement(Element ele, AbstractBeanDefinition bd) {
        String propertyName = ele.getAttribute(NAME_ATTRIBUTE);
        if (!StringUtils.hasLength(propertyName)) {
            return;
        }
        if (bd.getPropertyValues().contains(propertyName)) {
            return;
        }
        Object val = parsePropertyValue(ele, bd, propertyName);
        PropertyValue pv = new PropertyValue(propertyName, val);
        bd.getPropertyValues().addPropertyValue(pv);
    }

    private Object parsePropertyValue(Element ele, AbstractBeanDefinition bd, String propertyName) {
        // Should only have one child element: ref, value, list, etc.
        NodeList nl = ele.getChildNodes();
        boolean hasRefAttribute = ele.hasAttribute("ref");
        boolean hasValueAttribute = ele.hasAttribute("value");
        if (hasRefAttribute) {
            String refName = ele.getAttribute("ref");
            RuntimeBeanReference ref = new RuntimeBeanReference(refName);
            return ref;
        }
        else if (hasValueAttribute) {
            TypedStringValue valueHolder = new TypedStringValue(ele.getAttribute("value"));
            return valueHolder;
        }
        else {
            return null;
        }
    }

    private AbstractBeanDefinition createBeanDefinition(String className) throws ClassNotFoundException {
        GenericBeanDefinition bd = new GenericBeanDefinition();
        ClassLoader classLoader = this.readerContext.getBeanClassLoader();
        if (classLoader != null){
            bd.setBeanClass(ClassUtils.forName(className, classLoader));
        }else{
            bd.setBeanClassName(className);
        }
        return bd;
    }

    private void checkUniqueName(String beanName) throws Exception {
        if (usedNames.contains(beanName)){
            throw new Exception("bean name: "+beanName+"已经存在");
        }
        usedNames.add(beanName);
    }

    protected final XmlReaderContext getReaderContext() {
        return this.readerContext;
    }

    protected BeanDefinitionParserDelegate createDelegate(
            XmlReaderContext readerContext, Element root, BeanDefinitionParserDelegate parentDelegate) {

        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate(readerContext);
        return delegate;
    }

}
