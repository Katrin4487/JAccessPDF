module model {
    exports de.fkkaiser.model.structure;
    exports de.fkkaiser.model.font;
    exports de.fkkaiser.model.style;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.slf4j;

    opens de.fkkaiser.model.structure to com.fasterxml.jackson.databind;
    opens de.fkkaiser.model.style to com.fasterxml.jackson.databind;
    opens de.fkkaiser.model.font to com.fasterxml.jackson.databind;
}