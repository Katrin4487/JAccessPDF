package de.fkkaiser.bundle.validation;

import java.util.Set;

public record PlaceholderValidationResult(
        Set<String> missingTextKeys,
        Set<String> missingDataKeys
) {
    public boolean isValid() {
        return missingTextKeys.isEmpty() && missingDataKeys.isEmpty();
    }
}
