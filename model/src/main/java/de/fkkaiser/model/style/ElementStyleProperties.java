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
package de.fkkaiser.model.style;

import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import de.fkkaiser.model.annotation.Inheritable;

/**
 * An abstract class representing style properties for an element.
 * Base class for all StyleProperties.
 *
 * <p>This class provides a reflection-based inheritance mechanism using
 * the {@link Inheritable} annotation. Properties marked as inheritable will
 * be merged from parent elements if they are null in the child element.</p>
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public abstract class ElementStyleProperties {

    private static final Logger log = LoggerFactory.getLogger(ElementStyleProperties.class);

    /**
     * Cache for inheritable fields per class to avoid repeated reflection lookups.
     */
    private static final Map<Class<?>, List<Field>> INHERITABLE_FIELDS_CACHE =
            new ConcurrentHashMap<>();

    /**
     * Merges properties from a base style into this style.
     * Only properties marked with {@link Inheritable} annotation and having null values
     * will be inherited from the base style.
     *
     * <p>This method uses reflection to automatically merge all annotated fields,
     * eliminating the need for manual property-by-property merging in subclasses.</p>
     *
     * @param elemBase The base style to inherit from. Must be of the same type as this instance.
     *                 If null or of incompatible type, merge is skipped.
     */
    @Internal
    public void mergeWith(ElementStyleProperties elemBase) {
        if (elemBase == null) {
            log.debug("Attempted to merge with null base. Merge will be skipped.");
            return;
        }

        if (!this.getClass().equals(elemBase.getClass())) {
            log.info("Attempted to merge with an incompatible style type: {}. Merge will be skipped.",
                    elemBase.getClass().getName());
            return;
        }

        // Get cached inheritable fields for this class
        List<Field> inheritableFields = INHERITABLE_FIELDS_CACHE.computeIfAbsent(
                this.getClass(),
                this::findInheritableFields
        );

        // Merge all inheritable fields
        for (Field field : inheritableFields) {
            mergeField(field, elemBase);
        }
    }

    /**
     * Finds all fields in the class hierarchy that are marked with {@link Inheritable}.
     * Results are cached to avoid repeated reflection lookups.
     *
     * @param clazz The class to scan for inheritable fields
     * @return List of fields marked as inheritable
     */
    private List<Field> findInheritableFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();

        for (Field field : getAllFields(clazz)) {
            Inheritable annotation = field.getAnnotation(Inheritable.class);
            if (annotation != null && annotation.value()) {
                field.setAccessible(true);  // Make private fields accessible
                result.add(field);
            }
        }

        log.debug("Found {} inheritable fields in class {}", result.size(), clazz.getSimpleName());
        return result;
    }

    /**
     * Gets all fields from a class including inherited fields from superclasses.
     *
     * @param clazz The class to get fields from
     * @return List of all fields in the class hierarchy
     */
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // Walk up the class hierarchy
        Class<?> currentClass = clazz;
        while (currentClass != null && currentClass != Object.class) {
            fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
            currentClass = currentClass.getSuperclass();
        }

        return fields;
    }

    /**
     * Merges a single field from the base style if the current value is null.
     *
     * @param field The field to merge
     * @param base The base style to inherit the value from
     */
    private void mergeField(Field field, ElementStyleProperties base) {
        try {
            Object currentValue = field.get(this);
            Object baseValue = field.get(base);

            if (currentValue == null && baseValue != null) {
                field.set(this, baseValue);
                log.trace("Inherited field '{}' from base style: {}",
                        field.getName(), baseValue);
            }
        } catch (IllegalAccessException e) {
            log.error("Failed to merge field '{}': {}", field.getName(), e.getMessage(), e);
        }
    }

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    public abstract ElementStyleProperties copy();

    /**
     * Validates the ElementStyle instance.
     * Subclasses can override this to provide specific validation logic.
     *
     * @return a list of validation error messages; empty if valid
     */
    @Internal
    public List<String> validate() {
        return new ArrayList<>();
    }
}