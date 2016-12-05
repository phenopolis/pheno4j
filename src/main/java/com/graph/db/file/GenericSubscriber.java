package com.graph.db.file;

import java.lang.reflect.Constructor;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import com.google.common.eventbus.Subscribe;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.ManagedEventBus.PoisonPill;

public class GenericSubscriber<T> extends AbstractSubscriber<T> {
	
	public GenericSubscriber(String outputFolder, Class<?> parserClass, OutputFileType outputFileType) {
		super(outputFolder, parserClass, outputFileType);
	}

	@Subscribe
    public void processRow(T object) {
    	if (object instanceof PoisonPill) {
    		return;
    	}
    	try {
    		Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(outputFileType.getBeanClass(), object.getClass());
    		Object v = constructor.newInstance(object);
			beanWriter.write(v);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }

}
