package com.iotinall.framework.util;

import com.iotinall.framework.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {

    public static final String tmpfilepath = "tmpfile";

    public static RestTemplate restTemplate;

    public static String uploadFile(MultipartFile file){
        createTmpDir();
        String path = tmpfilepath + File.separator + UUID.randomUUID().toString();
        return uploadFilePath(file, path);
    }

    public static String uploadFile(MultipartFile file, String dir){
        createTmpDir();
        String path = dir + File.separator + UUID.randomUUID().toString();
        return uploadFilePath(file, path);
    }

    private static String uploadFilePath(MultipartFile file, String path){
        InputStream inputStream = null;
        String fileName = file.getOriginalFilename();
        if(fileName != null && fileName.contains(".")){
            String type = fileName.substring(fileName.indexOf("."));
            path += type;
        }

        File file1 = new File(path);
        try (FileOutputStream out = new FileOutputStream(file1)){
            inputStream =file.getInputStream();
            byte[] by = new byte[1024];
            int n;
            while ((n = inputStream.read(by)) != -1){
                out.write(by, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path;
    }

    /**
     * 创建随机文件夹
     * @return
     */
    public static String createRandomDir(String tmpGroup){
        String fileDir = RandomStringUtils.randomAlphanumeric(5);
        File file = new File(tmpfilepath + File.separator + (StringUtils.isNotBlank(tmpGroup) ? tmpGroup + File.separator : "") + fileDir);
        file.mkdirs();
        return file.getPath();
    }

    public static String createRandomDir(){
        return createRandomDir("");
    }

    /**
     * 上传文件
     * @param url
     * @return
     */
    public static String uploadFile(String url){
        File file = loadUrlImage(url);
        return uploadFile(file);
    }

    /**
     * 加载远程文件
     *
     * @param url
     * @return
     */
    public static File loadUrlImage(String url) {
        File file = restTemplate.execute(url, HttpMethod.GET, null, clientHttpResponse -> {
            File retfile = File.createTempFile("urlimage-", ".jpg", new File("tmpfile"));
            FileOutputStream outputStream = new FileOutputStream(retfile);
            StreamUtils.copy(clientHttpResponse.getBody(), outputStream);
            outputStream.close();
            retfile.deleteOnExit();
            return retfile;
        });
        return file;
    }

    /**
     * 判断文件是否存在
     * @param url
     * @return
     */
    public static Boolean validExist(String url){
        File file = new File(url);
        if(file.exists()){
            return file.isFile();
        }else{
            return Boolean.FALSE;
        }
    }

    public static String uploadFile(File file){
        createTmpDir();
        String path = tmpfilepath + File.separator + UUID.randomUUID().toString();
        String fileName = file.getName();
        if(fileName.contains(".")){
            String type = fileName.substring(fileName.indexOf("."));
            path += type;
        }
        try {
            Files.copy(file.toPath(), Paths.get(path));
        } catch (IOException e) {
            throw new BizException("", "文件生成失败");
        }
        file.delete();
        return path;
    }

    /**
     * 删除头像
     * @param url
     * @return
     */
    public static Boolean deleteFile(String url){
        if(StringUtils.isNotBlank(url)){
            File file = new File(url);
            return file.delete();
        }else{
            return Boolean.FALSE;
        }
    }

    /**
     * 创建默认临时文件夹
     */
    public static void createTmpDir(){
        File file = new File(tmpfilepath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void loadFile(String name, HttpServletResponse response){
        if(StringUtils.isNotBlank(name)){
            File file = new File(tmpfilepath + File.separator + name);
            response.addHeader("Content-Disposition", "attachment;filename=" + name);
            response.setContentType("multipart/form-data");
            FileInputStream fs = null;
            OutputStream out = null;
            try{
                out = response.getOutputStream();
                fs = new FileInputStream(file);
                //循环写入输出流
                byte[] b = new byte[1024*8];
                int len;
                while ((len = fs.read(b)) > 0) {
                    out.write(b, 0, len);
                }
            }catch (Exception e){
                log.error("文件生成失败：{}", e.getMessage());
            }finally {
                try {
                    if(fs != null){
                        fs.close();
                    }
                    if(out != null){
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 文件File类型转BASE64
     *
     * @param file
     * @return
     */
    public static String fileToBase64(File file) {
        return Base64.getEncoder().encodeToString(fileToByte(file));
    }

    /**
     * 文件File类型转byte[]
     *
     * @param file
     * @return
     */
    private static byte[] fileToByte(File file) {
        byte[] fileBytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

    /**
     * 删除文件夹
     */
    public static void deletedDir(File file){
        if(file.exists()){
            if(file.isDirectory()){
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    deletedDir(listFile);
                }
                file.delete();
            }else{
                file.delete();
            }
        }
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        FileUtil.restTemplate = restTemplate;
    }
}
