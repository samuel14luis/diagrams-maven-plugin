package com.humtherland.maven.plugins.formatters.mermaidjs;

import com.humtherland.maven.plugins.dto.OutputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import com.humtherland.maven.plugins.formatters.Formatter;

public class MermaidJsFormatter implements Formatter {

    @Override
    public OutputDto generate(ParsedDto parsedDto, String diagramType) {
        parsedDto.getLogger().info("Generating a " + diagramType + " diagram in " + "mermaidjs" + " format.");
        return generateArchitectureBetaDiagram(parsedDto);
    }

    private OutputDto generateArchitectureBetaDiagram(ParsedDto parsedDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("architecture-beta\n");
        sb.append("\t\tgroup api(cloud)[").append(parsedDto.getArtifactId()).append("]\n");

        for (String service : parsedDto.getServices()) {
            sb.append("\t\t\tservice ").append(service.toLowerCase()).append("(server)[").append(service).append("] in api\n");
        }

        for (String redis : parsedDto.getRedisClients()) {
            sb.append("\t\t\tservice ").append(redis.toLowerCase()).append("(database)[").append(redis).append("] in api\n");
        }

        for (String restClient : parsedDto.getRestClients()) {
            sb.append("\t\t\tservice ").append(restClient.toLowerCase()).append("(server)[").append(restClient).append("] in api\n");
        }

        for (String service : parsedDto.getServices()) {
            for (String injectedClient : parsedDto.getInjectedClients()) {
                if (parsedDto.getRestClients().contains(injectedClient)) {
                    sb.append("\t\t\t").append(service.toLowerCase()).append(":L -- R:").append(injectedClient.toLowerCase()).append("\n");
                } else if (parsedDto.getRedisClients().contains(injectedClient)) {
                    sb.append("\t\t\t").append(service.toLowerCase()).append(":B -- L:").append(injectedClient.toLowerCase()).append("\n");
                }
            }
        }

        return OutputDto.builder()
                .outputData(sb.toString())
                .outputFileName("architecture-diagram.mmd")
                .build();
    }

}
