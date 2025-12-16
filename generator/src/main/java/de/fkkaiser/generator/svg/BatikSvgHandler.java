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
package de.fkkaiser.generator.svg;

import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SVG handler using Apache Batik (if available on classpath).
 * Uses reflection to avoid compile-time dependency.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class BatikSvgHandler implements SvgHandler {
    private static final Logger log = LoggerFactory.getLogger(BatikSvgHandler.class);
    private static final BatikReflection BATIK;

    static {
        BATIK = BatikReflection.tryLoad();
    }

    /**
     * Converts SVG bytes to PNG format using Batik.
     *
     * @param svgBytes The SVG content
     * @param width    Target width in pixels
     * @param height   Target height in pixels
     * @return bytes of the converted PNG image
     * @throws SvgConversionException if conversion fails or Batik is not available
     */
    @Override
    public byte[] convertToPng(byte[] svgBytes, float width, float height)
            throws SvgConversionException {
        if (!isAvailable()) {
            log.error("Batik SVG handler not available");
            throw new SvgConversionException("Batik not available");
        }

        assert BATIK != null;
        return BATIK.convert(svgBytes, width, height);
    }

    /**
     * Gets the name of the SVG handler.
     *
     * @return the name "batik"
     */
    @Override
    public String getName() {
        return "batik";
    }

    /**
     * Gets the priority of the SVG handler.
     *
     * @return priority value 100
     */
    @Override
    public int getPriority() {
        return 100;
    }

    /**
     * Checks if Batik is available on the classpath.
     *
     * @return true if available, false otherwise
     */
    @Override
    public boolean isAvailable() {
        return BATIK != null && BATIK.isAvailable();
    }

    /**
     * Encapsulates all Batik reflection logic.
     */
    private record BatikReflection(Class<?> transcoderClass, Class<?> inputClass, Class<?> outputClass, Object keyWidth,
                                   Object keyHeight) {

        static BatikReflection tryLoad() {
            try {
                Class<?> tc = Class.forName("org.apache.batik.transcoder.image.PNGTranscoder");
                Class<?> ic = Class.forName("org.apache.batik.transcoder.TranscoderInput");
                Class<?> oc = Class.forName("org.apache.batik.transcoder.TranscoderOutput");
                Object kw = tc.getField("KEY_WIDTH").get(null);
                Object kh = tc.getField("KEY_HEIGHT").get(null);

                log.info("Apache Batik SVG support available");
                return new BatikReflection(tc, ic, oc, kw, kh);
            } catch (ClassNotFoundException e) {
                log.debug("Batik not on classpath");
                return null;
            } catch (Exception e) {
                log.warn("Failed to load Batik", e);
                return null;
            }
        }

        boolean isAvailable() {
            return true;
        }

        byte[] convert(byte[] svgBytes, float width, float height) throws SvgConversionException {
            try {
                Object transcoder = transcoderClass.getDeclaredConstructor().newInstance();

                var addHint = transcoderClass.getMethod("addTranscodingHint",
                        Class.forName("org.apache.batik.transcoder.TranscodingHints$Key"),
                        Object.class);
                addHint.invoke(transcoder, keyWidth, width);
                addHint.invoke(transcoder, keyHeight, height);

                var input = inputClass.getConstructor(InputStream.class)
                        .newInstance(new ByteArrayInputStream(svgBytes));

                var baos = new ByteArrayOutputStream();
                var output = outputClass.getConstructor(OutputStream.class)
                        .newInstance(baos);

                transcoderClass.getMethod("transcode", inputClass, outputClass)
                        .invoke(transcoder, input, output);

                return baos.toByteArray();
            } catch (Exception e) {
                throw new SvgConversionException("Conversion failed: " + e.getMessage(), e);
            }
        }
    }
}