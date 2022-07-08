package com.iotinall.framework.modules.codegeneration;

import com.iotinall.framework.modules.admin.entity.Org;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码生成工具类
 */
public class GenerationCreate {

    public void create(Class<?> cla){
        String entityKey = cla.getName();
        String entityKeyLower = entityKey.toLowerCase();
        String entityKeyLowerLine = humpToUnderline(entityKey, Boolean.TRUE);
        String entityKeyUpperLine = humpToUnderline(entityKey, Boolean.FALSE);

        String itemSetToDto = getItemSet(cla, "item");
        String reqSetToEntity = getItemSet(cla, "req");

        String entityProperty = getProperty(cla);


    }

    /**
     * 获取set方法
     * @param cla
     * @return
     */
    private String getItemSet(Class<?> cla, String targetParam){
        StringBuilder stringBuilder = new StringBuilder();
        for (Field declaredField : cla.getDeclaredFields()) {
            String property = declaredField.getName();
            stringBuilder.append(".set").append(property).append("(").append(targetParam).append(".get").append(property).append(")\r\n");
        }

        for (Field declaredField : cla.getSuperclass().getDeclaredFields()) {
            String property = declaredField.getName();
            stringBuilder.append(".set").append(property).append("(").append(targetParam).append(".get").append(property).append(")\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 获取属性
     * @param cla
     * @return
     */
    private String getProperty(Class<?> cla){
        StringBuilder stringBuilder = new StringBuilder();
        for (Field declaredField : cla.getDeclaredFields()) {
            stringBuilder.append("  private ").append(declaredField.getGenericType().toString()).append(" ").append(declaredField.getName()).append(";\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 驼峰转下划线
     * @param str
     * @return
     */
    public String humpToUnderline(String str, Boolean lower) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            matcher.appendReplacement(sb,  "_" + (lower ? matcher.group(0).toLowerCase() : matcher.group(0).toUpperCase()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
