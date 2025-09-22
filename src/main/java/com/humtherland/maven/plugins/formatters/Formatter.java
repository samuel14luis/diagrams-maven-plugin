package com.humtherland.maven.plugins.formatters;

import com.humtherland.maven.plugins.dto.OutputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;

public interface Formatter {

     /**
     * Generates the diagram representation in text format.
     * @param data The parsed data from the source code.
     * @return A String with the diagram content.
     */
     OutputDto generate(ParsedDto data, String diagramType);

}