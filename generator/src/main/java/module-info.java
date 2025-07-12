module de.kaiser.generator {
    requires org.slf4j;
    requires java.compiler;
    requires de.kaiser.model;

    exports de.kaiser.generator to de.kaiser.api;
}