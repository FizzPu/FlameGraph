package com.mx.context;

import com.google.common.base.Preconditions;

import java.util.function.Supplier;

public class WithContextImpl<V> implements WithContext {
	private final ContextVar<V> var;
	private final V value;

	public WithContextImpl(ContextVar<V> var, V value) {
		Preconditions.checkNotNull(var);
		this.var = var;
		this.value = value;
	}

	@Override
	public <R> R call(Supplier<R> supplier) {
		V oldValue = var.get();
		var.set(value);
		try {
			return supplier.get();
		} finally {
			if (oldValue == null) {
				var.remove();
			} else {
				var.set(oldValue);
			}
		}
	}

	@Override
	public void call(Runnable runnable) {
		call(() -> {
			runnable.run();
			return null;
		});
	}
}
