package com.humtherland.maven.plugins.formatters.mermaidjs;

import com.humtherland.maven.plugins.dto.EngineDto;
import com.humtherland.maven.plugins.dto.OutputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import com.humtherland.maven.plugins.engine.MustacheEngine;
import com.humtherland.maven.plugins.formatters.Formatter;

import java.util.*;

public class MermaidJsFormatter implements Formatter {

    private final String[] connectionFormats = {":R -- L:", ":T -- B:", ":B -- T:", ":L -- R:"};

    @Override
    public OutputDto generate(ParsedDto parsedDto, String diagramType) {
        parsedDto.getLogger().info("Generating a " + diagramType + " diagram in " + "mermaidjs" + " format.");
        Map<String, Object> scopes = generateArchitectureEngineParams(parsedDto);
        OutputDto output = MustacheEngine.render(
                EngineDto.builder()
                        .templatePath("mermaidjs/" + diagramType + ".mustache")
                        .scopes(scopes)
                        .outputFileName(diagramType + ".mmd")
                        .logger(parsedDto.getLogger())
                        .build());

        output.setOutputData(cleanOutputData(output.getOutputData()));
        return output;
    }

    private String cleanOutputData(String outputData) {
        if (!outputData.isEmpty()
            && outputData.charAt(outputData.length() - 1) == '\n') {
            outputData = outputData.substring(0, outputData.length() - 1);
        }
        return outputData.trim();
    }

    private Map<String, Object> generateArchitectureEngineParams(ParsedDto parsedDto) {
        Map<String, Object> scopes = new HashMap<>();

        scopes.put("artifactId", parsedDto.getArtifactId());
        List<Map<String, String>> components = new ArrayList<>();
        parsedDto.getServices().forEach(s -> mapOfComponent(components, s));
        parsedDto.getRestClients().forEach(s -> mapOfComponent(components, s));
        parsedDto.getRedisClients().forEach(s -> mapOfComponent(components, s));
        scopes.put("components", components);

        String[] connectionFormats = {":R -- L:", ":T -- B:", ":B -- T:", ":L -- R:"};
        List<Map<String, String>> relationships = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : parsedDto.getDependencies().entrySet()) {
            String sourceService = entry.getKey();
            int connectionCounter = 0;

            for (String targetDependency : entry.getValue()) {
                // Selecciona el formato de conexión dinámicamente
                String connectionStyle = connectionFormats[connectionCounter % connectionFormats.length];

                // Crea un objeto de relación listo para la plantilla
                Map<String, String> rel = new HashMap<>();
                rel.put("source", sourceService.toLowerCase());
                rel.put("connection", connectionStyle);
                rel.put("target", targetDependency.toLowerCase());
                relationships.add(rel);

                connectionCounter++;
            }
        }
        scopes.put("relationships", relationships);

        return scopes;
    }

    private void mapOfComponent(List<Map<String, String>> components, String s) {
        Map<String, String> component = new HashMap<>();
        component.put("id", s.toLowerCase());
        component.put("label", s);
        components.add(component);
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
