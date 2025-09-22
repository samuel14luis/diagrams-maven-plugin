package com.humtherland.maven.plugins.dto;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder(toBuilder = false)
public class OutputDto {

    private String outputData;
    private String outputFileName;

}
