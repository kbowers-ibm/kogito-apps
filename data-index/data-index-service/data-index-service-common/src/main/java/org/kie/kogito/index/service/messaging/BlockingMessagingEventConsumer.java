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
package org.kie.kogito.index.service.messaging;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.kie.kogito.event.DataEvent;
import org.kie.kogito.event.process.ProcessInstanceDataEvent;
import org.kie.kogito.event.process.UserTaskInstanceDataEvent;
import org.kie.kogito.index.event.KogitoJobCloudEvent;
import org.kie.kogito.index.event.ProcessInstanceEventMapper;
import org.kie.kogito.index.event.UserTaskInstanceEventMapper;
import org.kie.kogito.index.service.IndexingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.arc.properties.IfBuildProperty;
import io.smallrye.reactive.messaging.annotations.Blocking;

import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_JOBS_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_PROCESSINSTANCES_EVENTS;
import static org.kie.kogito.index.service.messaging.ReactiveMessagingEventConsumer.KOGITO_USERTASKINSTANCES_EVENTS;

@ApplicationScoped
@IfBuildProperty(name = "kogito.data-index.blocking", stringValue = "true")
public class BlockingMessagingEventConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockingMessagingEventConsumer.class);

    @Inject
    Event<DataEvent> eventPublisher;

    @Inject
    IndexingService indexingService;

    @Incoming(KOGITO_PROCESSINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public void onProcessInstanceEvent(ProcessInstanceDataEvent event) {
        LOGGER.debug("Process instance consumer received ProcessInstanceDataEvent: \n{}", event);
        indexingService.indexProcessInstance(new ProcessInstanceEventMapper().apply(event));
        eventPublisher.fire(event);
    }

    @Incoming(KOGITO_USERTASKINSTANCES_EVENTS)
    @Blocking
    @Transactional
    public void onUserTaskInstanceEvent(UserTaskInstanceDataEvent event) {
        LOGGER.debug("Task instance received UserTaskInstanceDataEvent \n{}", event);
        indexingService.indexUserTaskInstance(new UserTaskInstanceEventMapper().apply(event));
        eventPublisher.fire(event);
    }

    @Incoming(KOGITO_JOBS_EVENTS)
    @Blocking
    @Transactional
    public void onJobEvent(KogitoJobCloudEvent event) {
        LOGGER.debug("Job received KogitoJobCloudEvent \n{}", event);
        indexingService.indexJob(event.getData());
    }

}
