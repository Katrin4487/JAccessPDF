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
/**
 * Contains classes responsible for reading and parsing external configuration files.
 * <p>
 * These classes act as the primary input layer for the application. They use the
 * Jackson library to deserialize JSON streams into the application's internal
 * data models, such as {@link de.fkkaiser.model.structure.Document} or
 * {@link de.fkkaiser.model.style.StyleSheet}. This package also defines the common
 * {@link de.fkkaiser.processor.reader.JsonReadException} for consistent error
 * handling during these operations.
 */
package de.fkkaiser.processor.reader;