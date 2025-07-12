module de.kaiser.processor {
    exports de.kaiser.processor.reader;
    exports de.kaiser.processor;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires de.kaiser.model;
    requires org.slf4j;
}