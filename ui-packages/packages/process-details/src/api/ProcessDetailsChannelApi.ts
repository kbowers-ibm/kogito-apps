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

import {
  ProcessInstance,
  Job,
  JobCancel,
  SvgSuccessResponse,
  SvgErrorResponse,
  TriggerableNode,
  NodeInstance
} from '@kogito-apps/management-console-shared/dist/types';
export interface ProcessDetailsChannelApi {
  processDetails__getProcessDiagram(
    data: ProcessInstance
  ): Promise<SvgSuccessResponse | SvgErrorResponse>;
  processDetails__handleProcessAbort(
    processInstance: ProcessInstance
  ): Promise<void>;
  processDetails__cancelJob(
    job: Pick<Job, 'id' | 'endpoint'>
  ): Promise<JobCancel>;
  processDetails__rescheduleJob(
    job,
    repeatInterval: number | string,
    repeatLimit: number | string,
    scheduleDate: Date
  ): Promise<{ modalTitle: string; modalContent: string }>;
  processDetails__getTriggerableNodes(
    processInstance: ProcessInstance
  ): Promise<TriggerableNode[]>;
  processDetails__handleNodeTrigger(
    processInstance: ProcessInstance,
    node: TriggerableNode
  ): Promise<void>;
  processDetails__handleProcessVariableUpdate(
    processInstance: ProcessInstance,
    updatedJson: Record<string, unknown>
  );
  processDetails__processDetailsQuery(id: string): Promise<ProcessInstance>;
  processDetails__jobsQuery(id: string): Promise<Job[]>;
  processDetails__openProcessDetails(id: string): void;
  processDetails__handleProcessRetry(
    processInstance: ProcessInstance
  ): Promise<void>;
  processDetails__handleNodeInstanceCancel(
    processInstance: ProcessInstance,
    node: NodeInstance
  ): Promise<void>;
  processDetails__handleProcessSkip(
    processInstance: ProcessInstance
  ): Promise<void>;
  processDetails__handleNodeInstanceRetrigger(
    processInstance: ProcessInstance,
    node: Pick<NodeInstance, 'id'>
  ): Promise<void>;
}
