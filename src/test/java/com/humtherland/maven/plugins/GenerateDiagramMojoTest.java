package com.humtherland.maven.plugins;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.junit.Rule;
import org.junit.Test;

public class GenerateDiagramMojoTest {

    private static final String TEST_PROJECTS_BASE_PATH = "src/test/resources/project-to-test/";
    private static final String TEST_EXPECTED_BASE_PATH = "src/test/resources/expected-diagrams/";

    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable { }

        @Override
        protected void after() { }
    };

    /** Do not need the MojoRule. */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        assertTrue( true );
    }

    /**
     * Prueba la generación del diagrama con la nueva estructura de 'pets'.
     * @throws Exception si ocurre algún error.
     */
    @Test
    public void testPetsDiagramGeneration() throws Exception {
        String testCase = "pets-service";
        String projectPath = TEST_PROJECTS_BASE_PATH + testCase;
        File pom = new File( projectPath + "/pom.xml" );
        assertNotNull( pom );
        assertTrue(pom.exists());

        String mojoName = "generate-diagram";
        GenerateDiagramMojo generateDiagramMojo = (GenerateDiagramMojo) rule.lookupConfiguredMojo(pom.getParentFile(), mojoName);
        assertNotNull(generateDiagramMojo);
        generateDiagramMojo.execute();

        String diagramFileName = "architecture-diagram.mmd";
        File outputDirectory = (File) rule.getVariableValueFromObject(generateDiagramMojo, "outputDir");
        File generatedDiagramFile = new File(outputDirectory, diagramFileName);
        assertTrue("El archivo del diagrama .mmd debe existir", generatedDiagramFile.exists());

        String expectedPath = TEST_EXPECTED_BASE_PATH + testCase;
        File expectedDiagram = new File( expectedPath + "/" + diagramFileName );
        assertNotNull("El archivo esperado no se encontró en los recursos", expectedDiagram);

        String generatedContent = new String(Files.readAllBytes(Paths.get(generatedDiagramFile.toURI())));
        String expectedContent = new String(Files.readAllBytes(expectedDiagram.toPath()));

        assertEquals(
                expectedContent.replaceAll("\\r\\n", "\n"),
                generatedContent.replaceAll("\\r\\n", "\n")
        );

    }

}
