/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.jobs.service.repository.impl;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import javax.enterprise.context.ApplicationScoped;

import org.kie.kogito.jobs.service.model.JobServiceManagementInfo;
import org.kie.kogito.jobs.service.repository.JobServiceManagementRepository;
import org.kie.kogito.jobs.service.utils.DateUtil;

import io.quarkus.arc.DefaultBean;
import io.smallrye.mutiny.Uni;

@DefaultBean
@ApplicationScoped
public class DefaultJobServiceManagementRepository implements JobServiceManagementRepository {

    private AtomicReference<JobServiceManagementInfo> instance = new AtomicReference<>(new JobServiceManagementInfo(null, null, null));

    @Override
    public Uni<JobServiceManagementInfo> getAndUpdate(String id, Function<JobServiceManagementInfo, JobServiceManagementInfo> computeUpdate) {
        return set(computeUpdate.apply(instance.get()));
    }

    @Override
    public Uni<JobServiceManagementInfo> set(JobServiceManagementInfo info) {
        instance.set(info);
        return Uni.createFrom().item(instance.get());
    }

    @Override
    public Uni<JobServiceManagementInfo> heartbeat(JobServiceManagementInfo info) {
        info.setLastHeartbeat(DateUtil.now().toOffsetDateTime());
        return set(info);
    }
}
