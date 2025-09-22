package com.humtherland.maven.plugins.formatters.mermaidjs;

import com.humtherland.maven.plugins.dto.OutputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import com.humtherland.maven.plugins.formatters.Formatter;

import java.util.Map;
import java.util.Set;

public class MermaidJsFormatter implements Formatter {

    private final String[] connectionFormats = {":R -- L:", ":T -- B:", ":B -- T:", ":L -- R:"};

    @Override
    public OutputDto generate(ParsedDto parsedDto, String diagramType) {
        parsedDto.getLogger().info("Generating a " + diagramType + " diagram in " + "mermaidjs" + " format.");
        return generateArchitectureBetaDiagram(parsedDto);
    }

    private OutputDto generateArchitectureBetaDiagram(ParsedDto parsedDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("architecture-beta\n");
        sb.append("\t\tgroup api(cloud)[").append(parsedDto.getArtifactId()).append("]\n");
        sb.append("\n");

        for (String service : parsedDto.getServices()) {
            sb.append("\t\t\tservice ").append(service.toLowerCase()).append("(server)[").append(service).append("] in api\n");
        }

        for (String redis : parsedDto.getRedisClients()) {
            sb.append("\t\t\tservice ").append(redis.toLowerCase()).append("(database)[").append(redis).append("] in api\n");
        }

        for (String restClient : parsedDto.getRestClients()) {
            sb.append("\t\t\tservice ").append(restClient.toLowerCase()).append("(server)[").append(restClient).append("] in api\n");
        }

        sb.append("\n");

        for (Map.Entry<String, Set<String>> entry : parsedDto.getDependencies().entrySet()) {
            String sourceService = entry.getKey();

            int connectionCounter = 0;
            for (String targetDependency : entry.getValue()) {
                String format = connectionFormats[connectionCounter % connectionFormats.length];
                sb.append("\t").append(sourceService.toLowerCase())
                        .append(format)
                        .append(targetDependency.toLowerCase())
                        .append("\n");
                connectionCounter++;
            }

        }

        if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '\n') {
            sb.deleteCharAt(sb.length() - 1);
        }

        return OutputDto.builder()
                .outputData(sb.toString())
                .outputFileName("architecture-diagram.mmd")
                .build();
    }

}
