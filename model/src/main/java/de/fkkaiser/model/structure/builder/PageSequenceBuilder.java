package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.structure.ContentArea;
import de.fkkaiser.model.structure.PageSequence;

public class PageSequenceBuilder {

    private final String styleClass;
    private ContentArea body;
    private ContentArea header;
    private ContentArea footer;

    /**
     * A builder for creating instances of {@link PageSequence}.
     */
    public PageSequenceBuilder(String styleClass) {
        this.styleClass = styleClass;
    }

    public PageSequenceBuilder body(ContentArea body) {
        this.body = body;
        return this;
    }

    public PageSequenceBuilder header(ContentArea header) {
        this.header = header;
        return this;
    }

    public PageSequenceBuilder footer(ContentArea footer) {
        this.footer = footer;
        return this;
    }

    public PageSequence build() {
        return new PageSequence(styleClass, body, header, footer);
    }

}
