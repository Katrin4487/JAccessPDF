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
package de.fkkaiser.generator;

/**
 * This class contains constant values used in the generation process.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class GenerateConst {

    private GenerateConst() {
        // Private constructor to prevent instantiation
    }

    // Often used strings
    /**
     * Equals sign
     */
    public static final String EQUALS = "=";
    /**
     * Single space
     */
    public static final String SPACE = " ";
    /**
     * Closing angle bracket
     */
    public static final String CLOSER = ">";

    /**
     * Opening angle bracket
     */
    public static final String OPENER_OPEN_TAG = "<";

    public static final String OPENER_CLOSE_TAG = "</";
    /**
     * Quote
     */
    public static final String GQQ = "\"";

    // == Params for FOP generation ==

    /**
     * Parameter for font-family in FOP XML
     */
    public static final String FLOW_NAME = "flow-name";

}
