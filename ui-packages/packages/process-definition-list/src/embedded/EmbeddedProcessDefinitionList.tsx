/*
 * Copyright 2023 Red Hat, Inc. and/or its affiliates.
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
import React, { useCallback } from 'react';
import { EnvelopeServer } from '@kie-tools-core/envelope-bus/dist/channel';
import {
  EmbeddedEnvelopeProps,
  RefForwardingEmbeddedEnvelope
} from '@kie-tools-core/envelope/dist/embedded';
import {
  ProcessDefinitionListApi,
  ProcessDefinitionListChannelApi,
  ProcessDefinitionListEnvelopeApi,
  ProcessDefinitionListDriver
} from '../api';
import { ProcessDefinitionListChannelApiImpl } from './ProcessDefinitionListChannelApiImpl';
import { ContainerType } from '@kie-tools-core/envelope/dist/api';
import { init } from '../envelope';

export interface Props {
  targetOrigin: string;
  driver: ProcessDefinitionListDriver;
  singularProcessLabel: string;
  isTriggerCloudEventEnabled?: boolean;
}

export const EmbeddedProcessDefinitionList = React.forwardRef(
  (props: Props, forwardedRef: React.Ref<ProcessDefinitionListApi>) => {
    const refDelegate = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDefinitionListChannelApi,
          ProcessDefinitionListEnvelopeApi
        >
      ): ProcessDefinitionListApi => ({}),
      []
    );
    const pollInit = useCallback(
      (
        envelopeServer: EnvelopeServer<
          ProcessDefinitionListChannelApi,
          ProcessDefinitionListEnvelopeApi
        >,
        container: () => HTMLDivElement
      ) => {
        init({
          config: {
            containerType: ContainerType.DIV,
            envelopeId: envelopeServer.id
          },
          container: container(),
          bus: {
            postMessage(message, targetOrigin, transfer) {
              window.postMessage(message, targetOrigin, transfer);
            }
          }
        });
        return envelopeServer.envelopeApi.requests.processDefinitionList__init(
          {
            origin: envelopeServer.origin,
            envelopeServerId: envelopeServer.id
          },
          {
            singularProcessLabel: props.singularProcessLabel,
            isTriggerCloudEventEnabled: props.isTriggerCloudEventEnabled
          }
        );
      },
      []
    );

    return (
      <EmbeddedProcessDefinitionListEnvelope
        ref={forwardedRef}
        apiImpl={new ProcessDefinitionListChannelApiImpl(props.driver)}
        origin={props.targetOrigin}
        refDelegate={refDelegate}
        pollInit={pollInit}
        config={{ containerType: ContainerType.DIV }}
      />
    );
  }
);

const EmbeddedProcessDefinitionListEnvelope = React.forwardRef<
  ProcessDefinitionListApi,
  EmbeddedEnvelopeProps<
    ProcessDefinitionListChannelApi,
    ProcessDefinitionListEnvelopeApi,
    ProcessDefinitionListApi
  >
>(RefForwardingEmbeddedEnvelope);
