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
package de.fkkaiser.model.structure;

import de.fkkaiser.model.annotation.PublicAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for a list of block-level content elements.
 *
 * <p>ContentArea is used to define the content structure for different regions
 * of a {@link PageSequence}, specifically:</p>
 * <ul>
 *   <li><b>Header:</b> Content that appears at the top of each page</li>
 *   <li><b>Body:</b> The main content area of the page</li>
 *   <li><b>Footer:</b> Content that appears at the bottom of each page</li>
 * </ul>
 *
 * <p><b>Mutability:</b></p>
 * Unlike most classes in this package, ContentArea is intentionally mutable
 * to allow programmatic construction of document structure through methods like
 * {@link #addElement(Element)}. The internal list is initialized as a mutable
 * {@link ArrayList} to support this use case.
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create content with pre-built elements
 * List<Element> elements = List.of(
 *     new Headline("heading-1", "Chapter 1", 1),
 *     new Paragraph("body-text", "This is the first paragraph.")
 * );
 * ContentArea body = new ContentArea(elements);
 * }</pre>
 *
 * <p><b>Usage Example 2 - Incremental Construction:</b></p>
 * <pre>{@code
 * // Start with empty content area and add elements
 * ContentArea header = new ContentArea();
 * header.addElement(new Paragraph("header-text", "Company Name"));
 * header.addElement(new Paragraph("header-date", LocalDate.now().toString()));
 * }</pre>
 *
 * <p><b>Usage Example 3 - Page Sequence Integration:</b></p>
 * <pre>{@code
 * ContentArea body = new ContentArea(bodyElements);
 * ContentArea header = new ContentArea(headerElements);
 * ContentArea footer = new ContentArea(footerElements);
 *
 * PageSequence sequence = new PageSequence(
 *     "main-page-style",
 *     body,
 *     header,
 *     footer
 * );
 * }</pre>
 *
 * @param elements the list of block-level elements to be contained in this area;
 *                 if {@code null}, an empty mutable list is created
 *
 * @author FK Kaiser
 * @version 1.0
 * @see PageSequence
 * @see Element
 */
public record ContentArea(List<Element> elements) {

    /**
     * Compact constructor that ensures the elements list is never null
     * and is mutable for programmatic modification.
     *
     * <p>If the provided list is {@code null}, a new mutable {@link ArrayList}
     * is created. If a non-null list is provided, it is used as-is, so ensure
     * it is mutable if you plan to use {@link #addElement(Element)}.</p>
     *
     * @throws UnsupportedOperationException if attempting to modify an immutable
     *         list that was passed to the constructor
     */
    public ContentArea {
        if (elements == null) {
            elements = new ArrayList<>();
        }
    }

    /**
     * Default constructor that creates an empty content area.
     * The internal list is initialized as a mutable {@link ArrayList}.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * ContentArea area = new ContentArea();
     * area.addElement(new Paragraph("text", "First paragraph"));
     * area.addElement(new Paragraph("text", "Second paragraph"));
     * }</pre>
     */
    @PublicAPI
    public ContentArea() {
        this(null);
    }

    /**
     * Adds a new element to this content area.
     *
     * <p>This method allows incremental construction of the content area by
     * adding elements one at a time. The element is appended to the end of
     * the internal list.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * ContentArea body = new ContentArea();
     * body.addElement(new Headline("h1", "Introduction", 1));
     * body.addElement(new Paragraph("body", "This is the introduction."));
     * body.addElement(new Paragraph("body", "More content here."));
     * }</pre>
     *
     * @param element the element to be added; must not be {@code null}
     * @throws NullPointerException if element is {@code null}
     * @throws UnsupportedOperationException if the internal list is immutable
     */
    @PublicAPI
    public void addElement(Element element) {
        elements.add(element);
    }
}