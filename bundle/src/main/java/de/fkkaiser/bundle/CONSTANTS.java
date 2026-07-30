package de.fkkaiser.bundle;

import java.util.regex.Pattern;

public final class CONSTANTS {

    public static final Pattern KEY_PATTERN = Pattern.compile("^\\$\\{(.+)}$");
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");



}
