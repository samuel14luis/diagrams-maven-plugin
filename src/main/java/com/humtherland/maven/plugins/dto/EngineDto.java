package com.humtherland.maven.plugins.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.maven.plugin.logging.Log;

@Data
@Builder(toBuilder = false)
public class EngineDto {

    private String templatePath;
    private String outputFileName;
    private Object scopes;
    private Log logger;

}
