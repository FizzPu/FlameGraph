package com.mx.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContextVar<T> implements ContextVar<T> {
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	private final List<ContextVarObserver<T>> observers = new ArrayList<>();

	private final ThreadLocal<T> contextStoreThreadLocal = new ThreadLocal<>();

	@Override
	public T get() {
		return contextStoreThreadLocal.get();
	}

	@Override
	public void set(T value) {
		T oldValue = contextStoreThreadLocal.get();
		contextStoreThreadLocal.set(value);
		invokeObserversAfterSet(oldValue, value);
	}

	@Override
	public void remove() {
		T oldValue = contextStoreThreadLocal.get();
		contextStoreThreadLocal.remove();
		invokeObserversAfterRemove(oldValue);
	}

	protected void invokeObserversAfterSet(T oldValue, T newValue) {
		for (ContextVarObserver<T> observer : observers) {
			try {
				observer.observeContextVarSet(oldValue, newValue);
			} catch (Exception e) {
				log.error("Failed to invoke observer#observeContextVarSet.", e);
			}
		}
	}

	protected void invokeObserversAfterRemove(T oldValue) {
		for (ContextVarObserver<T> observer : observers) {
			try {
				observer.observeContextVarRemove(oldValue);
			} catch (Exception e) {
				log.error("Failed to invoke observer#observeContextVarRemove.", e);
			}
		}
	}

	@Override
	public void registerObserver(ContextVarObserver<T> observer) {
		observers.add(observer);
	}

	@Override
	public void removeObserver(ContextVarObserver<T> observer) {
		observers.remove(observer);
	}

	@Override
	public void removeAllObservers() {
		observers.clear();
	}
}
