module processor {
    exports de.kaiser.processor.reader;
    exports de.kaiser.processor;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires model;
    requires org.slf4j;
}