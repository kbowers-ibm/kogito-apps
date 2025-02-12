/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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

import { WorkflowFormChannelApi, WorkflowFormDriver } from '../api';

export class EmbeddedWorkflowFormChannelApiImpl
  implements WorkflowFormChannelApi
{
  constructor(private readonly driver: WorkflowFormDriver) {}

  workflowForm__resetBusinessKey(): Promise<void> {
    return this.driver.resetBusinessKey();
  }

  workflowForm__getCustomWorkflowSchema(): Promise<Record<string, any>> {
    return this.driver.getCustomWorkflowSchema();
  }

  workflowForm__startWorkflow(
    endpoint: string,
    data: Record<string, any>
  ): Promise<void> {
    return this.driver.startWorkflow(endpoint, data);
  }
}
