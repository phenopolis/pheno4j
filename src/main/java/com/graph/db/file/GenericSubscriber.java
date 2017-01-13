package com.graph.db.file;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
    	
    	final Object objectToWrite = getObjectToWrite(object);
    	
		try {
			beanWriter.write(objectToWrite);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }

	private Object getObjectToWrite(T object) {
		// if the input bean is the same type as the output bean to be written
		// out, then there's nothing to do
    	if (object.getClass().equals(outputFileType.getBeanClass())) {
    		return object;
    	} else {
			Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(outputFileType.getBeanClass(), object.getClass());
			try {
				return constructor.newInstance(object);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
    	}
	}
}
