module processor {
    exports de.fkkaiser.processor.reader;
    exports de.fkkaiser.processor;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires model;
    requires org.slf4j;
}