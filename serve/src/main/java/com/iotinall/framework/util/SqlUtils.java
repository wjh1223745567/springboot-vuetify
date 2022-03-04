package com.iotinall.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 处理sql
 */
public class SqlUtils {

    /**
     * 数组转换in sql字符串
     * @param collection
     * @return
     */
    public static String getInStr(Collection<?> collection){
        if(!CollectionUtils.isEmpty(collection)){
            String inSql = collection.stream().filter(Objects::nonNull).map(String::valueOf).collect(Collectors.joining("','"));
            if(StringUtils.isNotBlank(inSql)) {
                return "'" + inSql + "'";
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

}
