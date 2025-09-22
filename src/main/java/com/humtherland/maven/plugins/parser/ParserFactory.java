package com.humtherland.maven.plugins.parser;

import com.humtherland.maven.plugins.parser.architecturediagram.ArchitectureDiagramParser;

public class ParserFactory {

    /**
     * Returns a parser instance based on the specified diagram type.
     * @param diagramType The desired diagram type (e.g., "architecture-diagram").
     * @return Parser instance.
     */
    public Parser getParser(String diagramType) {
        if (diagramType == null || diagramType.trim().isEmpty()) {
            throw new IllegalArgumentException("Diagram type must not be null or empty");
        }

        switch (diagramType.toLowerCase()) {
            case "architecture-diagram":
                return new ArchitectureDiagramParser();
            default:
                throw new IllegalArgumentException("Not supported diagram type: " + diagramType);
        }
    }
}