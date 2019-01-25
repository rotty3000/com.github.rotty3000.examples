/*  Licensed to the Apache Software Foundation (ASF) under one
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

package org.apache.portals.samples;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import javax.inject.Inject;
import javax.inject.Named;

import org.osgi.service.cdi.annotations.BeanPropertyType;
import org.osgi.service.cdi.annotations.Reference;
import org.osgi.service.cdi.annotations.Service;

import com.liferay.portal.kernel.service.UserLocalService;

@Named
@Service
@Users.GogoCommand(scope = "cdiportlet", function = "getUsersCount")
public class Users {

	@BeanPropertyType
	@Retention(RUNTIME)
	public static @interface GogoCommand {
		String PREFIX_ = "osgi.command.";
		String scope();
		String[] function();
	}

	@Inject
	@Reference
	private UserLocalService userLocalService;

	public int getUsersCount() {
		return userLocalService.getUsersCount();
	}

}
