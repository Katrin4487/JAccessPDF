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

import de.fkkaiser.generator.svg.BatikSvgHandler;
import de.fkkaiser.generator.svg.NoOpSvgHandler;
import de.fkkaiser.generator.svg.SvgConversionException;
import de.fkkaiser.generator.svg.SvgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for image handling in PDF generation.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public class ImageUtils {
    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    private static final SvgHandler svgHandler;

    static {
        // Auto-detect best available SVG handler
        List<SvgHandler> handlers = Arrays.asList(
                new BatikSvgHandler(),
                new NoOpSvgHandler()
        );

        svgHandler = handlers.stream()
                .filter(SvgHandler::isAvailable)
                .max(Comparator.comparingInt(SvgHandler::getPriority))
                .orElseGet(NoOpSvgHandler::new);

        log.info("Using SVG handler: {}", svgHandler.getName());
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageUtils() {
        // Utility class
    }

    /**
     * Resolves an image path and converts it to a base64 data URI.
     * SVG images are automatically converted to PNG.
     */
    public static String resolveToDataUri(String path, ImageResolver imageResolver) {
        if (path == null || path.isEmpty()) {
            log.warn("Image path is null or empty");
            return null;
        }

        try {
            URL absoluteUrl = imageResolver.resolve(path);
            if (absoluteUrl == null) {
                log.warn("Image resolver returned null for path: {}", path);
                return null;
            }

            String mimeType = detectMimeType(absoluteUrl.toString());
            boolean isSvg = "image/svg+xml".equals(mimeType);

            try (InputStream inputStream = absoluteUrl.openStream()) {
                byte[] imageBytes = inputStream.readAllBytes();

                if (isSvg) {
                    try {
                        byte[] pngBytes = svgHandler.convertToPng(imageBytes, 96f, 96f);
                        String base64String = Base64.getEncoder().encodeToString(pngBytes);
                        return "data:image/png;base64," + base64String;
                    } catch (SvgConversionException e) {
                        log.error("SVG conversion failed for {}: {}", path, e.getMessage());
                        return null;
                    }
                } else {
                    String base64String = Base64.getEncoder().encodeToString(imageBytes);
                    return "data:" + mimeType + ";base64," + base64String;
                }
            }
        } catch (IOException e) {
            log.error("Unable to read image at path: {}", path, e);
            return null;
        } catch (URISyntaxException e) {
            log.error("Invalid URI syntax for path: {}", path, e);
            return null;
        }
    }

    private static String detectMimeType(String urlString) {
        String lower = urlString.toLowerCase();
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";

        log.warn("Unknown image format for: {}. Using default MIME type image/jpeg", urlString);
        return "image/jpeg";
    }

    static String getSvgHandlerName(){
        return svgHandler.getName();
    }
}