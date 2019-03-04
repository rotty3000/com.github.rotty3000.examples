/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.github.rotty3000.cdi.jaxrs;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.Predicate;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class RuntimeRule<T> extends OSGiRule<T> {

	private AtomicLong serivceChangeCount = new AtomicLong();

	public RuntimeRule(Class<T> clazz) {
		super(clazz);
	}

	public RuntimeRule(Class<T> clazz, long timeout) {
		super(clazz, timeout);
	}

	public RuntimeRule(Class<T> clazz, String filter) {
		super(clazz, filter);
	}

	public RuntimeRule(Class<T> clazz, String filter, long timeout) {
		super(clazz, filter, timeout);
	}

	public long getChangeCount() {
		return serivceChangeCount.get();
	}

	@Override
	public void before() {
		serivceChangeCount.set(-1L);
		super.before();
	}

	@Override
	public ServiceTrackerCustomizer<T, T> customizer() {
		return new ServiceTrackerCustomizer<T, T>() {

			@Override
			public T addingService(ServiceReference<T> reference) {
				final Object obj = reference.getProperty(Constants.SERVICE_CHANGECOUNT);
				if (obj != null) {
					serivceChangeCount.set(Long.valueOf(obj.toString()));
				}
				return context().getService(reference);
			}

			@Override
			public void modifiedService(ServiceReference<T> reference, T service) {
				addingService(reference);
			}

			@Override
			public void removedService(ServiceReference<T> reference, T service) {
				serivceChangeCount.set(-1);
			}

		};
	}

	/**
	 * Wait for specified operation to result in an update to {@code service.changecount}.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @return the result once the {@code service.changecount} has been updated
	 * @throws AssertionError if {@code service.changecount} hasn't updated before timeout
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R awaitChangeCount(Function<BundleContext, R> operation) {
		return awaitChangeCount(operation, Objects::nonNull, 10);
	}

	/**
	 * Wait for specified operation to result in an update to {@code service.changecount}.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate to be satisfied
	 * @return the result once the {@code service.changecount} has been updated
	 * @throws AssertionError if {@code service.changecount} hasn't updated before timeout
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R awaitChangeCount(Function<BundleContext, R> operation, Predicate<R> predicate) {
		return awaitChangeCount(operation, predicate, 10);
	}

	/**
	 * Wait for specified operation to result in an update to {@code service.changecount}.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate to be satisfied
	 * @param maxAttempts the maximum number of attempts to check for update to {@code service.changecount}
	 * @return the result once the {@code service.changecount} has been updated
	 * @throws AssertionError if {@code service.changecount} hasn't updated before maxAttempts
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R awaitChangeCount(Function<BundleContext, R> operation, Predicate<R> predicate, int maxAttempts) {
		long previousCount = serivceChangeCount.get();
		long waits = 10;
		R result;
		while (!predicate.test(result = operation.apply(context()))) {
			if (maxAttempts <= 0) {
				throw new AssertionError("Max attempts exceeded");
			}
			while (previousCount == serivceChangeCount.get()) {
				if (waits <= 0) {
					break;
				}
				try {
					Thread.sleep(20L);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				waits--;
			}
			previousCount = serivceChangeCount.get();
			waits = 10;
			maxAttempts--;
		}
		return result;
	}

}
