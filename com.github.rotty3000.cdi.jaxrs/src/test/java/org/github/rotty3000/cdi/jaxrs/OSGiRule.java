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
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class OSGiRule<T> extends ExternalResource {

	public static final long DEFAULT_TIMEOUT = 10000;

	private final Class<T> clazz;
	private final Filter filter;
	private final long timeout;

	private BundleContext bundleContext;
	private ServiceTracker<T, T> serviceTracker;

	public OSGiRule(Class<T> clazz) {
		this(clazz, null, DEFAULT_TIMEOUT);
	}

	public OSGiRule(Class<T> clazz, long timeout) {
		this(clazz, null, timeout);
	}

	public OSGiRule(Class<T> clazz, String filter) {
		this(clazz, filter, DEFAULT_TIMEOUT);
	}

	public OSGiRule(Class<T> clazz, String filter, long timeout) {
		this.clazz = Objects.requireNonNull(clazz);
		try {
			this.filter = FrameworkUtil.createFilter(filter != null ? filter : String.format("(objectClass=%s)", clazz.getName()));
		}
		catch (InvalidSyntaxException ise) {
			throw new AssertionError("Invalid filter", ise);
		}
		this.timeout = timeout;
	}

	@Override
	public Statement apply(Statement statement, Description description) {
		bundleContext = FrameworkUtil.getBundle(description.getTestClass()).getBundleContext();
		return super.apply(statement, description);
	}

	public BundleContext context() {
		return bundleContext;
	}

	public ServiceReference<T> reference() {
		try {
			if (serviceTracker.isEmpty()) {
				serviceTracker.waitForService(timeout);
			}
			return serviceTracker.getServiceReference();
		}
		catch (InterruptedException e) {
			throw new AssertionError("Service " + clazz.getName() + " didn't arrive by timeout: " + timeout);
		}
	}

	public T service() {
		try {
			return serviceTracker.waitForService(timeout);
		}
		catch (InterruptedException e) {
			throw new AssertionError("Service " + clazz.getName() + " didn't arrive by timeout: " + timeout);
		}
	}

	public long timeout() {
		return timeout;
	}

	@Override
	public void after() {
		serviceTracker.close();
	}

	@Override
	public void before() {
		serviceTracker = new ServiceTracker<>(context(), filter, customizer());
		serviceTracker.open();
	}

	public ServiceTrackerCustomizer<T, T> customizer() {
		return null;
	}

	/**
	 * Perform the specified operation then wait until the predicate is satisfied.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate to be satisfied
	 * @return the result once the predicate has been satisfied
	 * @throws AssertionError if predicate hasn't been satisfied before timeout
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R await(Function<BundleContext, R> operation, Predicate<R> predicate) {
		return await(operation, predicate, 100);
	}

	/**
	 * Perform the specified operation then wait until the predicate is satisfied.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate to be satisfied
	 * @param maxAttempts the maximum number of attempts to check for the predicate to be satisfied
	 * @return the result once the predicate has been satisfied
	 * @throws AssertionError if predicate hasn't been satisfied before timeout
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R await(Function<BundleContext, R> operation, Predicate<R> predicate, int maxAttempts) {
		R result = operation.apply(context());
		while (!predicate.test(result)) {
			if (maxAttempts <= 0) {
				throw new AssertionError("Max attempts exceeded: " + maxAttempts);
			}
			try {
				Thread.sleep(20L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			maxAttempts--;
		}
		return result;
	}

	/**
	 * Wait for specified operation to satisfy the predicate before returning the result.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate the result must satisfy
	 * @return the result once the predicate is satisfied
	 * @throws AssertionError if predicate hasn't been satisfied before timeout
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R until(Function<BundleContext, R> operation, Predicate<R> predicate) {
		return until(operation, predicate, 100);
	}

	/**
	 * Wait for specified operation to satisfy the predicate before returning the result.
	 *
	 * @param <R> the type returned by the operation
	 * @param operation the operation to execute
	 * @param predicate the predicate the result must satisfy
	 * @param maxAttempts the maximum number of attempts to check
	 * @return the result once the predicate is satisfied
	 * @throws AssertionError if predicate hasn't been satisfied before maxAttempts
	 * @throws InterruptedException if task is interrupted
	 */
	public <R> R until(Function<BundleContext, R> operation, Predicate<R> predicate, int maxAttempts) {
		R result;
		while (!predicate.test(result = operation.apply(context()))) {
			if (maxAttempts <= 0) {
				throw new AssertionError("Max attempts exceeded: " + maxAttempts);
			}
			try {
				Thread.sleep(20L);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			maxAttempts--;
		}
		return result;
	}

}
