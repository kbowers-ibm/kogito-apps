/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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

package org.kie.kogito.index.oracle.storage;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntity;
import org.kie.kogito.index.oracle.model.ProcessDefinitionEntityRepository;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.StorageService;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
class ProcessDefinitionStorageIT extends AbstractStorageIT<ProcessDefinitionEntity, ProcessDefinition> {

    @Inject
    ProcessDefinitionEntityRepository repository;

    @Inject
    StorageService storage;

    public ProcessDefinitionStorageIT() {
        super(ProcessDefinition.class);
    }

    @Override
    public ProcessDefinitionEntityStorage.RepositoryAdapter getRepository() {
        return new ProcessDefinitionEntityStorage.RepositoryAdapter(repository);
    }

    @Override
    public StorageService getStorage() {
        return storage;
    }

    @Test
    @Transactional
    void testProcessInstanceEntity() {
        String processId = "travels";
        String version = "1.0";
        ProcessDefinition pdv1 = TestUtils.createProcessDefinition(processId, version, Set.of("admin", "kogito"));
        ProcessDefinition pdv2 = TestUtils.createProcessDefinition(processId, version, Set.of("kogito"));
        testStorage(pdv1.getKey(), pdv1, pdv2);
    }

}
