package com.humtherland.maven.plugins.parser;

import com.humtherland.maven.plugins.parser.quarkus.QuarkusParser;

public class ParserFactory {

    /**
     * Returns a parser instance based on the specified library.
     * @param library The desired library (e.g., "quarkus", "spring").
     * @return Parser instance.
     */
    public Parser getParser(String library) {
        if (library == null || library.trim().isEmpty()) {
            throw new IllegalArgumentException("Library must not be null or empty");
        }

        switch (library.toLowerCase()) {
            case "quarkus":
                return new QuarkusParser();
            default:
                throw new IllegalArgumentException("Not supported library: " + library);
        }
    }
}