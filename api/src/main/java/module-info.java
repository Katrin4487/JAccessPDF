/*
 * Copyright 2025 Katrin Kaiser
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
module api {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires org.apache.xmlgraphics.commons;
    requires org.apache.xmlgraphics.fop.core;
    requires org.slf4j;

    requires transitive model;
    requires transitive processor;
    requires transitive generator;

    exports de.fkkaiser.api.simplelayer;
    exports de.fkkaiser.api.utils;
    exports de.fkkaiser.api;

}