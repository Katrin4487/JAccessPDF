module bundle {
    requires org.slf4j;
    requires model;
    requires processor;
    requires com.fasterxml.jackson.databind;

    exports de.fkkaiser.bundle;
    opens de.fkkaiser.bundle to com.fasterxml.jackson.databind;
    exports de.fkkaiser.bundle.resolver;
    opens de.fkkaiser.bundle.resolver to com.fasterxml.jackson.databind;
    exports de.fkkaiser.bundle.validation;
    opens de.fkkaiser.bundle.validation to com.fasterxml.jackson.databind;
}