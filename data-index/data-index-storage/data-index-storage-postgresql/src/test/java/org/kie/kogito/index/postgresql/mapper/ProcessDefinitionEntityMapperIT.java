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

package org.kie.kogito.index.postgresql.mapper;

import java.util.Set;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.Node;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.postgresql.model.NodeEntity;
import org.kie.kogito.index.postgresql.model.ProcessDefinitionEntity;

import io.quarkus.test.junit.QuarkusTest;

import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class ProcessDefinitionEntityMapperIT {

    ProcessDefinition pd = new ProcessDefinition();
    ProcessDefinitionEntity entity = new ProcessDefinitionEntity();

    @Inject
    ProcessDefinitionEntityMapper mapper;

    @BeforeEach
    void setup() {
        String version = "1.0";
        String processId = "testProcessId";
        Set<String> roles = singleton("testRoles");
        String type = "testType";
        Set<String> addons = singleton("testAddons");

        String nodeId = "testNodeId";
        String nodeName = "testNodeName";
        String nodeUniqueId = "testNodeUniqueId";
        String nodeMetadataUniqueId = "testMetadataUniqueId";
        String nodeType = "testNodeType";

        Node node = new Node();
        node.setId(nodeId);
        node.setName(nodeName);
        node.setType(nodeType);
        node.setUniqueId(nodeUniqueId);
        node.setMetadata(singletonMap("UniqueId", nodeMetadataUniqueId));

        pd.setId(processId);
        pd.setVersion(version);
        pd.setRoles(roles);
        pd.setAddons(addons);
        pd.setType(type);
        pd.setNodes(singletonList(node));

        NodeEntity nodeEntity = new NodeEntity();
        nodeEntity.setId(nodeId);
        nodeEntity.setName(nodeName);
        nodeEntity.setType(nodeType);
        nodeEntity.setUniqueId(nodeUniqueId);
        nodeEntity.setMetadata(singletonMap("UniqueId", nodeMetadataUniqueId));
        nodeEntity.setProcessDefinition(entity);

        entity.setId(processId);
        entity.setVersion(version);
        entity.setRoles(roles);
        entity.setAddons(addons);
        entity.setType(type);
        entity.setNodes(singletonList(nodeEntity));
    }

    @Test
    void testMapToEntity() {
        ProcessDefinitionEntity result = mapper.mapToEntity(pd);
        assertThat(result).usingRecursiveComparison().ignoringFieldsMatchingRegexes(".*\\$\\$_hibernate_tracker").isEqualTo(entity);
    }

    @Test
    void testMapToModel() {
        ProcessDefinition result = mapper.mapToModel(entity);
        assertThat(result).usingRecursiveComparison().isEqualTo(pd);
    }

}
