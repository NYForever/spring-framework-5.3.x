package rwn.spring;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenan.ren
 * @date 2022/4/13 14:36
 * @Description
 */
public class NYApplicationContext {

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Object> singletonBeanMap = new ConcurrentHashMap<>();

    private Class configClass;

    public NYApplicationContext(Class configClass) {
        this.configClass = configClass;

        //扫描
        if (configClass.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScanAnno = (ComponentScan) configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnno.value();
            path = path.replace(".", "/");

            ClassLoader classLoader = NYApplicationContext.class.getClassLoader();
            URL resource = classLoader.getResource(path);

            File file = new File(resource.getFile());
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f : files) {
                    String fileName = f.getAbsolutePath();
                    if (fileName.endsWith(".class")) {
                        String className = fileName.substring(fileName.indexOf("com"), fileName.indexOf(".class"));
                        className = className.replace("/", ".");
                        try {
                            Class<?> clazz = classLoader.loadClass(className);

                            if (clazz.isAnnotationPresent(Component.class)) {

                                Component componentAnno = clazz.getAnnotation(Component.class);
                                String beanName = componentAnno.value();

                                if ("".equals(beanName)) {
                                    beanName = Introspector.decapitalize(clazz.getSimpleName());
                                }

                                BeanDefinition beanDefinition = new BeanDefinition();
                                beanDefinition.setType(clazz);

                                if (clazz.isAnnotationPresent(Scope.class)) {
                                    Scope scope = clazz.getAnnotation(Scope.class);
                                    if (null != scope.value() && !"".equals(scope.value())) {
                                        beanDefinition.setScope(scope.value());
                                    } else {
                                        beanDefinition.setScope("singleton");
                                    }
                                }
                                beanDefinitionMap.put(beanName, beanDefinition);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }

        //创建单例bean
        for (String beanName : beanDefinitionMap.keySet()) {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")) {
                Object bean = createBean(beanName, beanDefinition);
                singletonBeanMap.put(beanName, bean);
            }
        }

    }

    private Object createBean(String beanName, BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getType();
        try {
            Object obj = clazz.getConstructor().newInstance();
            //依赖注入
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowried.class)) {
                    field.setAccessible(true);
                    field.set(obj, getBean(field.getName()));
                }
            }

            //依赖注入之后，初始化前，执行PostConstruct注解标注的方法
            for (Method declaredMethod : clazz.getDeclaredMethods()) {
                if(declaredMethod.isAnnotationPresent(PostConstruct.class)){
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(obj);
                }
            }

            if (obj instanceof BeanNameAwar) {
                ((BeanNameAwar) obj).setBeanName(beanName);
            }

            return obj;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getBean(String beanName) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new NullPointerException();
        }
        if (beanDefinition.getScope().equals("singleton")) {
            Object bean = singletonBeanMap.get(beanName);
            if (bean == null) {
                Object obj = createBean(beanName, beanDefinition);
                singletonBeanMap.put(beanName, obj);
                return obj;
            }
            return bean;
        } else {
            return createBean(beanName, beanDefinition);
        }
    }
}
