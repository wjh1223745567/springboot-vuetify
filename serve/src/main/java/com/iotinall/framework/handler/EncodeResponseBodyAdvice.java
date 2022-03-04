package com.iotinall.framework.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iotinall.framework.util.AesEncryptUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

@RestControllerAdvice
public class EncodeResponseBodyAdvice implements ResponseBodyAdvice {
    @Value("${spring.encrypt.debug}")
    private Boolean debug;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(debug){
            return body;
        }else{
            String bod = objectMapper.writeValueAsString(body);
            return AesEncryptUtils.aesEncrypt(bod);
        }

    }
}
