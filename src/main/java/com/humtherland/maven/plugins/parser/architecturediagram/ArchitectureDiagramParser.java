package com.humtherland.maven.plugins.parser.architecturediagram;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.humtherland.maven.plugins.dto.InputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import com.humtherland.maven.plugins.parser.Parser;
import com.humtherland.maven.plugins.parser.PomParser;
import com.humtherland.maven.plugins.utils.FileUtils;
import org.xml.sax.SAXException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ArchitectureDiagramParser implements Parser {

    private final PomParser pomParser = new PomParser();

    public ParsedDto parse(InputDto inputDto)
            throws XPathExpressionException, IOException, SAXException {
        String artifactId = pomParser.getFormattedProjectId(inputDto.getPomFile());
        Set<String> services = new HashSet<>();
        Set<String> restClients = new HashSet<>();
        Set<String> injectedClients = new HashSet<>();
        Set<String> redisClients = new HashSet<>();
        Map<String, Set<String>> dependencies = new HashMap<>();

        List<Path> javaFiles = FileUtils.listJavaFiles(inputDto.getSourceDir());
        JavaParser parser = new JavaParser();

        for (Path path:javaFiles) {
            CompilationUnit cu = parser.parse(path)
                    .getResult()
                    .orElseThrow(() -> new IOException("Could not parse " + path));

            for (TypeDeclaration<?> type: cu.getTypes()) {
                if (type instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration clazz = (ClassOrInterfaceDeclaration) type;
                    String className = clazz.getNameAsString();
                    services.add(className);

                    for(AnnotationExpr annotation: clazz.getAnnotations()) {
                        if (annotation.getNameAsString().equals("RegisterRestClient")) {
                            restClients.add(className);
                        } else if (annotation.getNameAsString().equals("InjectRestClient")) {
                            injectedClients.add(className);
                        } else if (annotation.getNameAsString().equals("RedisClient")) {
                            redisClients.add(className);
                        }
                    }

                    for(BodyDeclaration<?> member: clazz.getMembers()) {
                        if (member instanceof FieldDeclaration) {
                            FieldDeclaration field = (FieldDeclaration) member;
                            String fieldType = field.getElementType().asString();

                            for(AnnotationExpr annotation: field.getAnnotations()) {
                                if (annotation.getNameAsString().equals("RestClient")) {
                                    //injectedClients.add(fieldType);
                                    dependencies.computeIfAbsent(className, k -> new HashSet<>()).add(fieldType);
                                } else if (annotation.getNameAsString().equals("RedisClient")) {
                                    redisClients.add(fieldType);
                                }
                            }

                            if (fieldType.toLowerCase().contains("redis")) {
                                redisClients.add(fieldType);
                            }
                        }
                    }
                }
            }
        }

        services.removeAll(restClients);
        services.removeAll(redisClients);

        return ParsedDto.builder()
                .artifactId(artifactId)
                .services(services)
                .restClients(restClients)
                .injectedClients(injectedClients)
                .redisClients(redisClients)
                .logger(inputDto.getLogger())
                .dependencies(dependencies)
                .build();
    }
}
