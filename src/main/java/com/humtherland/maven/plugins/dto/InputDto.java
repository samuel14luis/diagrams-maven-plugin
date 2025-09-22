package com.humtherland.maven.plugins.dto;

import java.io.File;
import lombok.Builder;
import lombok.Data;
import org.apache.maven.plugin.logging.Log;

@Data
@Builder(toBuilder = false)
public class InputDto {

    private File sourceDir;
    private File pomFile;
    private File outputDir;
    private String diagramType;
    private String format;
    private Log logger;

}
