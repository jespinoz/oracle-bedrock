/*
 * File: StorageDisabledMember.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.tools.junit;

import com.oracle.tools.Option;
import com.oracle.tools.Options;

import com.oracle.tools.runtime.LocalPlatform;
import com.oracle.tools.runtime.Profile;

import com.oracle.tools.runtime.coherence.options.CacheConfig;
import com.oracle.tools.runtime.coherence.options.LocalHost;
import com.oracle.tools.runtime.coherence.options.LocalStorage;
import com.oracle.tools.runtime.coherence.options.RoleName;

import com.oracle.tools.util.SystemProperties;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.ScopedCacheFactoryBuilder;

import java.util.Properties;

/**
 * A {@link SessionBuilder} for Coherence Storage Disabled Members.
 * <p>
 * Copyright (c) 2015. All Rights Reserved. Oracle Corporation.<br>
 * Oracle is a registered trademark of Oracle Corporation and/or its affiliates.
 *
 * @author Brian Oliver
 */
public class StorageDisabledMember implements SessionBuilder
{
    @Override
    public ConfigurableCacheFactory build(LocalPlatform                 platform,
                                          CoherenceClusterOrchestration orchestration,
                                          Option...                     options)
    {
        // ----- establish the options for launching a local storage-disabled member -----
        Options launchOptions = new Options(options);

        launchOptions.add(RoleName.of("client"));
        launchOptions.add(LocalStorage.disabled());
        launchOptions.add(LocalHost.only());
        launchOptions.addIfAbsent(CacheConfig.of("coherence-cache-config.xml"));

        // ----- notify the Profiles that we're about to launch an application -----

        for (Profile profile : launchOptions.getInstancesOf(Profile.class))
        {
            profile.onBeforeLaunch(platform, launchOptions);
        }

        // ----- create local system properties based on those defined by the launch options -----

        // take a snapshot of the system properties as we're about to mess with them
        Properties systemPropertiesSnapshot = SystemProperties.createSnapshot();

        // modify the current system properties to include/override those in the schema
        com.oracle.tools.runtime.java.options.SystemProperties systemProperties =
            launchOptions.get(com.oracle.tools.runtime.java.options.SystemProperties.class);

        Properties properties = systemProperties.resolve(platform, launchOptions);

        for (String propertyName : properties.stringPropertyNames())
        {
            System.setProperty(propertyName, properties.getProperty(propertyName));
        }

        // create the session
        ConfigurableCacheFactory session =
            new ScopedCacheFactoryBuilder().getConfigurableCacheFactory(launchOptions.get(CacheConfig.class).getUri(),
                                                                        getClass().getClassLoader());

        // as this is a cluster member we have to join the cluster
        CacheFactory.ensureCluster();

        // replace the system properties
        SystemProperties.replaceWith(systemPropertiesSnapshot);

        return session;
    }


    @Override
    public boolean equals(Object other)
    {
        return other instanceof StorageDisabledMember;
    }


    @Override
    public int hashCode()
    {
        return StorageDisabledMember.class.hashCode();
    }
}
