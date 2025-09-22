package com.humtherland.maven.plugins.parser;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class PomParser {

    private final DocumentBuilderFactory factory;
    private final XPath xpath;

    /**
     * Constructor initializes XML parser and XPath with security features enabled.
     */
    public PomParser() {
        this.factory = DocumentBuilderFactory.newInstance();
        this.xpath = XPathFactory.newInstance().newXPath();

        // --- Security Configurations ---
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Error with XML parser configuration", e);
        }
    }

    /**
     * Extract and format the project artifactId and version from the POM file.
     * Example output: "MY-ARTIFACT V1_0_0"
     * @return the formatted project identifier.
     */
    public String getFormattedProjectId(File pomFile)
            throws IOException, SAXException, XPathExpressionException {
        Document doc = parsePom(pomFile);

        String artifactId = Optional.ofNullable(getTagValue(doc, "/project/artifactId"))
                .map(s -> s.toUpperCase().replaceAll("-", " "))
                .orElse("UNKNOWN_ARTIFACT");

        String version = Optional.ofNullable(getTagValue(doc, "/project/version"))
                .map(s -> "V" + s.replaceAll("-SNAPSHOT", "")
                        .replaceAll("\\.", "_"))
                .orElse("1_0_0");

        return String.format("%s %s", artifactId, version);
    }

    /**
     * Parse the POM file into a Document object.
     * @return the parsed Document.
     */
    private Document parsePom(File pomFile)
            throws IOException, SAXException {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(pomFile);
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException("Error de configuración del parser XML", e);
        }
    }

    /**
     * Helper method to evaluate XPath expressions.
     * @return the text content of the evaluated tag, or null if not found.
     */
    private String getTagValue(Document doc, String expression)
            throws XPathExpressionException {
        return xpath.evaluate(expression, doc);
    }
}