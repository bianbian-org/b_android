package com.techjumper.polyhome.b.setting.net;

import com.techjumper.commonres.entity.BaseArgumentsEntity;
import com.techjumper.commonres.entity.BaseEntity;
import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class NetHelper {

    private static final String PRIVATE_KEY = "GjcfbhCIJ2owQP1Kxn64DqSk5X4YRZ7u";
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_NOT_LOGIN = 109;
    public static final int CODE_NO_DATA = 404;
    public static final int CODE_NO_THIS_FAMILY = 301;

    /**
     * 全局超时时间
     */
    public static int GLOBAL_TIME_OUT = 5000;

    public static String encrypt(String json) {
        String pre = json + PRIVATE_KEY;
        return StringUtils.md5(pre);
    }

    public static BaseArgumentsEntity createBaseArguments(KeyValuePair argMap) {
        String json = GsonUtils.toJson(argMap.toMap());
        String encrypt = encrypt(json);
        return new BaseArgumentsEntity(encrypt, json);
    }

    public static Map<String, String> createBaseArgumentsMap(KeyValuePair argMap) {
        String json = GsonUtils.toJson(argMap.toMap());
        String encrypt = encrypt(json);
        HashMap<String, String> map = new HashMap<>();
        map.put(BaseArgumentsEntity.FILED_SIGN, encrypt);
        map.put(BaseArgumentsEntity.FILED_DATA, json);
        return map;
    }

//    public static MaxenseQueryEntity createMaxenseQueryEntity(KeyValuePair<String, Object> argMap) {
//        return new MaxenseQueryEntity(argMap.get("method").toString(), NumberUtil.convertToint(argMap.get("deviceId").toString(), 331)
//                , NumberUtil.convertToint(argMap.get("userId").toString(), 1));
//    }
//
//    public static BaseTcpArgumentsEntity createTcpBaseArguments(KeyValuePair argMap) {
//        String json = GsonUtils.toJson(argMap.toMap());
//        String encrypt = encrypt(json);
//        return new BaseTcpArgumentsEntity(encrypt, json);
//    }

    public static String createTcpJson(KeyValuePair argMap, String method) {
        KeyValuePair pair = KeyValueCreator.generateParamMethod(argMap.toMap(), method);
        return GsonUtils.toJson(pair.toMap());
    }

    public static boolean isSuccess(BaseEntity entity) {
        return entity != null && entity.getError_code() == CODE_SUCCESS;
    }
}

