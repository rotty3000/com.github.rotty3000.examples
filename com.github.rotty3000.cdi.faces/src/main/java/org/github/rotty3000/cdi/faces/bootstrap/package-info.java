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

@Beans
@RequireGogo
@Requirement(namespace = CDIConstants.CDI_EXTENSION_PROPERTY, name = "aries.cdi.http")
@Requirement(namespace = CDIConstants.CDI_EXTENSION_PROPERTY, name = "faces-view-scoped")
@Requirement(namespace = CDIConstants.CDI_EXTENSION_PROPERTY, name = "faces-flow")
@Requirement(namespace = CDIConstants.CDI_EXTENSION_PROPERTY, name = "faces-flow-discovery")
package org.github.rotty3000.cdi.faces.bootstrap;

import org.apache.felix.service.command.annotations.RequireGogo;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.service.cdi.CDIConstants;
import org.osgi.service.cdi.annotations.Beans;
