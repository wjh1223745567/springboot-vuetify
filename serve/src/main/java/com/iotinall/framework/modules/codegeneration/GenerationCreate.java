package com.iotinall.framework.modules.codegeneration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码生成工具类
 */
public class GenerationCreate {

    public void create(Class<?> cla) {
        String entityKey = cla.getName();
        String entityKeyLower = entityKey.toLowerCase();
        String entityKeyLowerLine = humpToUnderline(entityKey, Boolean.TRUE);
        String entityKeyUpperLine = humpToUnderline(entityKey, Boolean.FALSE);

        String itemSetToDto = getItemSet(cla, "item");
        String reqSetToEntity = getItemSet(cla, "req");

        String entityProperty = getProperty(cla);

        GenerationDto dto = new GenerationDto()
                .setEntityKey(entityKey)
                .setEntityKeyLower(entityKeyLower)
                .setEntityKeyLowerLine(entityKeyLowerLine)
                .setEntityKeyUpperLine(entityKeyUpperLine)
                .setItemSetToDto(itemSetToDto)
                .setReqSetToEntity(reqSetToEntity)
                .setEntityProperty(entityProperty);

    }

    /**
     * 创建mapper
     */
    private void createMapper() {

    }

    /**
     * 创建service
     */
    private void createService() {

    }

    /**
     * 创建controller
     */
    private void createController() {

    }

    /**
     * 获取文件内容
     *
     * @return
     */
    private void getFileText(String fileName, String targetPath, GenerationDto dto) {
        File file = new File(System.getProperty("user.dir") + File.separator + "codegenerationtemp" + File.separator + fileName);
        try (FileOutputStream out = new FileOutputStream(targetPath);
             PrintStream ps = new PrintStream(out)) {
             Files.lines(file.toPath()).forEach((item) -> {
                 ps.println(coverParam(item, dto));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String coverParam(String str, GenerationDto dto) {
        return str.replaceAll("\\$\\{entityKey\\}", dto.getEntityKey())
                .replaceAll("\\$\\{entityKeyLower\\}", dto.getEntityKeyLower())
                .replaceAll("\\$\\{entityKeyLowerLine\\}", dto.getEntityKeyLowerLine())
                .replaceAll("\\$\\{entityKeyUpperLine\\}", dto.getEntityKeyUpperLine())
                .replaceAll("\\$\\{itemSetToDto\\}", dto.getItemSetToDto())
                .replaceAll("\\$\\{reqSetToEntity\\}", dto.getReqSetToEntity())
                .replaceAll("\\$\\{entityProperty\\}", dto.getEntityProperty());
    }

    /**
     * 获取set方法
     *
     * @param cla
     * @return
     */
    private String getItemSet(Class<?> cla, String targetParam) {
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
     *
     * @param cla
     * @return
     */
    private String getProperty(Class<?> cla) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field declaredField : cla.getDeclaredFields()) {
            stringBuilder.append("  private ").append(declaredField.getGenericType().toString()).append(" ").append(declaredField.getName()).append(";\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * 驼峰转下划线
     *
     * @param str
     * @return
     */
    public String humpToUnderline(String str, Boolean lower) {
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + (lower ? matcher.group(0).toLowerCase() : matcher.group(0).toUpperCase()));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
