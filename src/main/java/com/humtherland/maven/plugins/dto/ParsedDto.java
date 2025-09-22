package com.humtherland.maven.plugins.dto;

import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.apache.maven.plugin.logging.Log;

@Data
@Builder(toBuilder = false)
public class ParsedDto {

    private String artifactId;
    private Set<String> services;
    private Set<String> restClients;
    private Set<String> injectedClients;
    private Set<String> redisClients;
    private Map<String, Set<String>> dependencies;
    private Log logger;

}
