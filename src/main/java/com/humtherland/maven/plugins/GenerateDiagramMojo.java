package com.humtherland.maven.plugins;

import com.humtherland.maven.plugins.dto.InputDto;
import com.humtherland.maven.plugins.dto.OutputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import com.humtherland.maven.plugins.formatters.FormatterFactory;
import com.humtherland.maven.plugins.parser.ParserFactory;

import javax.xml.xpath.XPathExpressionException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.xml.sax.SAXException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo( name = "generate-diagram", defaultPhase = LifecyclePhase.PROCESS_SOURCES )
public class GenerateDiagramMojo extends AbstractMojo {

    @Parameter( defaultValue = "${project.basedir}/src/main/java", property = "sourceDir", required = true )
    private File sourceDir;

    @Parameter( defaultValue = "${project.basedir}/pom.xml", property = "pomFile", required = true )
    private File pomFile;

    @Parameter( defaultValue = "${project.build.directory}/generated-diagrams", property = "outputDir", required = true )
    private File outputDir;

    @Parameter( defaultValue = "architecture-diagram", property = "diagramType", required = true )
    private String diagramType;

    @Parameter( defaultValue = "mermaid-js", property = "format", required = true )
    private String format;

    private final FormatterFactory formatterFactory = new FormatterFactory();
    private final ParserFactory parserFactory = new ParserFactory();

    public void execute() throws MojoExecutionException {
        getLog().info("Starting diagram generation...");

        try {
            ParsedDto parsedDto = parserFactory.getParser(diagramType)
                    .parse(InputDto.builder()
                            .sourceDir(sourceDir)
                            .pomFile(pomFile)
                            .outputDir(outputDir)
                            .diagramType(diagramType)
                            .format(format)
                            .logger(getLog())
                            .build());

            OutputDto diagram = formatterFactory.getFormatter(format)
                    .generate(parsedDto, diagramType);

            outputDir.mkdirs();
            File outputFile = new File(outputDir, diagram.getOutputFileName());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(diagram.getOutputData());
            }

            getLog().info("Diagram generated at: " + outputFile.getAbsolutePath());
        } catch (IOException | SAXException e) {
            getLog().error("Error during diagram generation", e);
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
