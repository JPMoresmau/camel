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
package org.apache.camel.component.aws.secretsmanager.integration;

import org.apache.camel.FailedToCreateRouteException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.assertThrows;

@EnabledIfEnvironmentVariable(named = "AWS_ACCESS_KEY", matches = ".*")
@EnabledIfEnvironmentVariable(named = "AWS_SECRET_KEY", matches = ".*")
@EnabledIfEnvironmentVariable(named = "AWS_REGION", matches = ".*")
public class SecretsManagerPropertiesSourceTestIT extends CamelTestSupport {

    @Test
    public void testFunction() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").setBody(simple("{{aws:hello}}")).to("mock:bar");
            }
        });
        context.start();

        getMockEndpoint("mock:bar").expectedBodiesReceived("hello");

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testSecretNotFoundFunction() throws Exception {
        Exception exception = assertThrows(FailedToCreateRouteException.class, () -> {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:start").setBody(simple("{{aws:testExample}}")).to("mock:bar");
                }
            });
            context.start();

            getMockEndpoint("mock:bar").expectedBodiesReceived("hello");

            template.sendBody("direct:start", "Hello World");

            assertMockEndpointsSatisfied();
        });
    }

}
