package com.iotinall.framework.modules.codegeneration;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenerationDto {

    private String entityKey;
    private String entityKeyLower;
    private String entityKeyLowerLine;
    private String entityKeyUpperLine;

    private String itemSetToDto;
    private String reqSetToEntity;

    private String entityProperty;

}
