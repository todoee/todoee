package io.todoee.faces.core;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.servlet.ServletContext;

import com.sun.faces.config.DelegatingAnnotationProvider;

import io.todoee.base.utils.PackageScanner;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author James.zhang
 *
 */
@Slf4j
public class ScanAnnotationProvider extends DelegatingAnnotationProvider {

    public ScanAnnotationProvider(ServletContext sc) {
        super(sc);
    }

    @Override
    public Map<Class<? extends Annotation>, Set<Class<?>>> getAnnotatedClasses(Set<URI> urls) {
        Map<Class<? extends Annotation>, Set<Class<?>>> parentRes = super.getAnnotatedClasses(urls);

        Map<Class<? extends Annotation>, Set<Class<?>>> ret = new HashMap<>(parentRes);

        Set<Class<?>> parentSet = ret.get(ManagedBean.class);
        Set<Class<?>> set = (parentSet == null) ? new HashSet<>() : new HashSet<>(parentSet);

        Set<Class<?>> classes = PackageScanner.scanAnnotation(ManagedBean.class);
        for(Class<?> clazz : classes) {
        	set.add(clazz);
            log.debug("Found ManagedBean: " + clazz.getName());
        }

        ret.put(ManagedBean.class, set);
        return ret;
    }

}
