package com.humtherland.maven.plugins.parser;

import com.humtherland.maven.plugins.dto.InputDto;
import com.humtherland.maven.plugins.dto.ParsedDto;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

public interface Parser {

    /**
     * Generates the parsed data from the source code.
     * @param inputDto The input data from the source code.
     * @return ParsedDto containing the parsed data.
     */
    ParsedDto parse(InputDto inputDto)
            throws XPathExpressionException, IOException, SAXException;

}