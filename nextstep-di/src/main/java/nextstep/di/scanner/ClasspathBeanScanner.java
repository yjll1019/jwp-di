package nextstep.di.scanner;

import com.google.common.collect.Sets;
import nextstep.di.factory.BeanFactory;
import nextstep.stereotype.Controller;
import nextstep.stereotype.Repository;
import nextstep.stereotype.Service;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClasspathBeanScanner implements BeanScanner {
    private static final Logger log = LoggerFactory.getLogger(ClasspathBeanScanner.class);

    private Reflections reflections;

    private BeanFactory beanFactory;

    public ClasspathBeanScanner(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void scanBeans(Object... basePackage) {
        reflections = new Reflections(basePackage);
        Set<Class<?>> preInstantiatedBeans = getTypesAnnotatedWith(Controller.class, Service.class, Repository.class);
        beanFactory.appendPreInstantiatedBeans(preInstantiatedBeans);
    }

    @SuppressWarnings("unchecked")
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation>... annotations) {
        Set<Class<?>> beans = Sets.newHashSet();
        for (Class<? extends Annotation> annotation : annotations) {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        }
        log.debug("Scan Beans Type : {}", beans);
        return beans;
    }
}