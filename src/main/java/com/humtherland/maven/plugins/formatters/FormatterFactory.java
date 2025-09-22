package com.humtherland.maven.plugins.formatters;

import com.humtherland.maven.plugins.formatters.mermaidjs.MermaidJsFormatter;

public class FormatterFactory {

    /**
     * Returns a formatter instance based on the specified format.
     * @param format The desired format (e.g., "mermaid-js").
     * @return Formatter instance.
     */
    public Formatter getFormatter(String format) {
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("Format must not be null or empty");
        }

        switch (format.toLowerCase()) {
            case "mermaid-js":
                return new MermaidJsFormatter();
            default:
                throw new IllegalArgumentException("Not supported format: " + format);
        }
    }
}