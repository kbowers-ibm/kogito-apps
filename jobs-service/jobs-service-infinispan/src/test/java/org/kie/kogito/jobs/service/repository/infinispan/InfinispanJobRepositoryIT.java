/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.jobs.service.repository.infinispan;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.kie.kogito.jobs.service.repository.ReactiveJobRepository;
import org.kie.kogito.jobs.service.repository.impl.BaseJobRepositoryTest;
import org.kie.kogito.testcontainers.quarkus.InfinispanQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import static org.awaitility.Awaitility.await;

@QuarkusTest
@QuarkusTestResource(InfinispanQuarkusTestResource.class)
class InfinispanJobRepositoryIT extends BaseJobRepositoryTest {

    @Inject
    InfinispanJobRepository tested;

    @Inject
    InfinispanConfiguration infinispanConfiguration;

    @Inject
    RemoteCacheManager remoteCacheManager;

    @BeforeEach
    public void setUp() throws Exception {
        await().atMost(2, TimeUnit.SECONDS).until(() -> infinispanConfiguration.isInitialized());
        remoteCacheManager.getCache(InfinispanConfiguration.Caches.JOB_DETAILS).clear();
        super.setUp();
    }

    @Override
    public ReactiveJobRepository tested() {
        return tested;
    }
}
