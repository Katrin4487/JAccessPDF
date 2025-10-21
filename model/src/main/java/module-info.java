module model {
    exports de.kaiser.model.structure;
    exports de.kaiser.model.font;
    exports de.kaiser.model.style;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    opens de.kaiser.model.structure to com.fasterxml.jackson.databind;
    opens de.kaiser.model.style to com.fasterxml.jackson.databind;
    opens de.kaiser.model.font to com.fasterxml.jackson.databind;
}