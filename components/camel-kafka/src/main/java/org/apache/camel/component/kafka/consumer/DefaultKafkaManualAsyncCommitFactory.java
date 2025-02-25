/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.kafka.consumer;

import java.util.Collection;

import org.apache.camel.Exchange;
import org.apache.camel.spi.StateRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;

public class DefaultKafkaManualAsyncCommitFactory implements KafkaManualCommitFactory {

    @Override
    public KafkaManualCommit newInstance(CamelExchangePayload camelExchangePayload, KafkaRecordPayload kafkaRecordPayload) {
        return new DefaultKafkaManualAsyncCommit(camelExchangePayload, kafkaRecordPayload);
    }

    /**
     * @deprecated Use DefaultKafkaManualAsyncCommitFactory#newInstance(CamelExchangePayload, KafkaRecordPayload)
     */
    @Deprecated(since = "3.15.0")
    @Override
    public KafkaManualCommit newInstance(
            Exchange exchange, Consumer consumer, String topicName, String threadId,
            StateRepository<String, String> offsetRepository,
            TopicPartition partition, long recordOffset, long commitTimeout, Collection<KafkaAsyncManualCommit> asyncCommits) {

        CamelExchangePayload camelExchangePayload
                = new CamelExchangePayload(exchange, consumer, threadId, offsetRepository, asyncCommits);
        KafkaRecordPayload kafkaRecordPayload = new KafkaRecordPayload(partition, recordOffset, commitTimeout);

        return newInstance(camelExchangePayload, kafkaRecordPayload);
    }
}
