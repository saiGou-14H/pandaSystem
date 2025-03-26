package com.saigou.util;

import cn.hutool.json.JSONUtil;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.google.protobuf.util.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoBufUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProtoBufUtil.class);

    private static final  JsonFormat.Printer printer = JsonFormat.printer();

    private static final JsonFormat.Parser parser = JsonFormat.parser();

    /**
     * Proto 转化为Json
     * @param target
     * @return
     */
    public static String copyProtoBeanToJson(GeneratedMessage target) {
        try {
            return printer.print(target);
        } catch (InvalidProtocolBufferException e) {
            logger.error("ProtoBufUtil复制到Json异常",e);
        }
        return null;
    }

    /**
     * javabean转化为Proto
     * @param <T>
     * @param source
     * @param target
     * @return
     */
    public static <T extends GeneratedMessage> T copyJavaBeanToProtoBean(Object source, T.Builder target) {
        // javaBean 转换为Json
        String sourceStr =  JSONUtil.toJsonStr(source);
        try {
            parser.merge(sourceStr, target);
            return (T) target.build();
        } catch (InvalidProtocolBufferException e) {
            logger.error("ProtoBufUtil复制到Proto异常",e);
        }
        return null;
    }


    /**
     * proto 转化为javabean
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyProtoBeanToJavaBean(GeneratedMessage source, Class<T> target){
        // protoBuf 转换为Json
        String soutceStr = copyProtoBeanToJson(source);
        return (T) JSONUtil.toBean(soutceStr,target);
    }

    /**
     * 使用proto序列化javabean
     * @param source
     * @param target
     * @return
     */
    public static byte[] serializFromJavaBean(Object source,GeneratedMessage.Builder target){
        GeneratedMessage messageV3 = copyJavaBeanToProtoBean(source,target);
        if (null != messageV3){
            return messageV3.toByteArray();
        }
        return new byte[0];
    }

    /**
     * 使用proto反序列化javabean
     * @param source
     * @param parser
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T deserializToJavaBean(byte[] source, Parser parser, Class<T> target) {
        try {
            return copyProtoBeanToJavaBean((GeneratedMessage) parser.parseFrom(source),target);
        } catch (InvalidProtocolBufferException e) {
            logger.error("发序列化错误",e);
        }
        return null;
    }

}
