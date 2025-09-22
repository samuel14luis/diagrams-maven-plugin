package com.humtherland.maven.plugins.engine;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.humtherland.maven.plugins.dto.EngineDto;
import com.humtherland.maven.plugins.dto.OutputDto;

import java.io.StringWriter;
import java.io.Writer;

public class MustacheEngine {

    public static OutputDto render(EngineDto engineDto) {
        try {
            Writer writer = new StringWriter();
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("templates/" + engineDto.getTemplatePath());
            mustache.execute(writer, engineDto.getScopes());

            return OutputDto.builder()
                    .outputData(writer.toString())
                    .outputFileName(engineDto.getOutputFileName())
                    .build();
        } catch (Exception e) {
            engineDto.getLogger().error("Error while generating diagram with Mustache", e);
            throw new RuntimeException("Error while generating diagram with Mustache", e);
        }
    }

}
