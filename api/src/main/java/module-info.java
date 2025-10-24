module api {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires java.xml;
    requires org.apache.xmlgraphics.commons;
    requires org.apache.xmlgraphics.fop.core;
    requires org.slf4j;

    requires processor;
    requires generator;
    requires model;

    exports de.fkkaiser.api.simplelayer;

}