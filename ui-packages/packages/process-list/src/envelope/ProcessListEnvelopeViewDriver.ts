/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { MessageBusClientApi } from '@kie-tools-core/envelope-bus/dist/api';
import {
  BulkProcessInstanceActionResponse,
  ProcessInstance,
  ProcessInstanceFilter,
  ProcessListSortBy
} from '@kogito-apps/management-console-shared/dist/types';
import { OperationType } from '@kogito-apps/management-console-shared/dist/components/BulkList';
import { ProcessListChannelApi, ProcessListDriver } from '../api';

export default class ProcessListEnvelopeViewDriver
  implements ProcessListDriver
{
  constructor(
    private readonly channelApi: MessageBusClientApi<ProcessListChannelApi>
  ) {}
  initialLoad(
    filter: ProcessInstanceFilter,
    sortBy: ProcessListSortBy
  ): Promise<void> {
    return this.channelApi.requests.processList__initialLoad(filter, sortBy);
  }
  openProcess(process: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__openProcess(process);
  }
  applyFilter(filter: ProcessInstanceFilter): Promise<void> {
    return this.channelApi.requests.processList__applyFilter(filter);
  }
  applySorting(sortBy: ProcessListSortBy): Promise<void> {
    return this.channelApi.requests.processList__applySorting(sortBy);
  }
  handleProcessSkip(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessSkip(
      processInstance
    );
  }
  handleProcessRetry(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessRetry(
      processInstance
    );
  }
  handleProcessAbort(processInstance: ProcessInstance): Promise<void> {
    return this.channelApi.requests.processList__handleProcessAbort(
      processInstance
    );
  }
  handleProcessMultipleAction(
    processInstances: ProcessInstance[],
    operationType: OperationType
  ): Promise<BulkProcessInstanceActionResponse> {
    return this.channelApi.requests.processList__handleProcessMultipleAction(
      processInstances,
      operationType
    );
  }
  query(offset: number, limit: number): Promise<ProcessInstance[]> {
    return this.channelApi.requests.processList__query(offset, limit);
  }
  getChildProcessesQuery(
    rootProcessInstanceId: string
  ): Promise<ProcessInstance[]> {
    return this.channelApi.requests.processList__getChildProcessesQuery(
      rootProcessInstanceId
    );
  }

  openTriggerCloudEvent(processInstance?: ProcessInstance): void {
    this.channelApi.notifications.processList__openTriggerCloudEvent.send(
      processInstance
    );
  }
}
