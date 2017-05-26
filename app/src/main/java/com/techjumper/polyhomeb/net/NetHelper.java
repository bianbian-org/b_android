package com.techjumper.polyhomeb.net;

import com.techjumper.corelib.utils.basic.StringUtils;
import com.techjumper.lib2.others.KeyValuePair;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.entity.BaseArgumentsEntity;
import com.techjumper.polyhomeb.entity.BaseEntity;
import com.techjumper.polyhomeb.entity.medicalEntity.BaseArgumentsMedicalEntity;
import com.techjumper.polyhomeb.entity.tcp_udp.BaseTcpArgumentsEntity;

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
    public static final int CODE_UPLOAD_FAILED = 208;

    /**
     * 全局超时时间
     */
    public static int GLOBAL_TIMEOUT = 14000;

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

    public static BaseArgumentsEntity createBaseCAPPArguments(KeyValuePair keyValuePair) {
        String json = GsonUtils.toJson(keyValuePair.toMap());
        String encrypt = encrypt(json);
        return new BaseArgumentsEntity(encrypt, json);
    }

    public static Map<String, String> createBaseCAPPArgumentsMap(KeyValuePair keyValuePair) {
        String json = GsonUtils.toJson(keyValuePair.toMap());
        String encrypt = encrypt(json);
        HashMap<String, String> map = new HashMap<>();
        map.put(BaseArgumentsEntity.FILED_SIGN, encrypt);
        map.put(BaseArgumentsEntity.FILED_DATA, json);
        return map;
    }

    public static BaseArgumentsMedicalEntity createMedicalUserLoginArguments(KeyValuePair<String, Integer> argMap) {
        return new BaseArgumentsMedicalEntity(argMap.get("devicetype"), argMap.get("logintype"));
    }

    public static BaseTcpArgumentsEntity createTcpBaseArguments(KeyValuePair argMap) {
        String json = GsonUtils.toJson(argMap.toMap());
        String encrypt = encrypt(json);
        return new BaseTcpArgumentsEntity(encrypt, json);
    }

//    public static String createTcpJson(KeyValuePair argMap, String method) {
//        KeyValuePair pair = KeyValueCreator.generateParamMethod(argMap.toMap(), method, UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
//        return GsonUtils.toJson(pair.toMap());
//    }

//    public static String createTcpCloudAuthJson() {
//        CloudAuthEntity entity = new CloudAuthEntity();
//        entity.setCode("0");
//        entity.setMsg("validate");
//        CloudAuthEntity.DataEntity dataEntity = new CloudAuthEntity.DataEntity();
//        dataEntity.setFamily_id(UserManager.INSTANCE.getCurrentFamilyInfo(UserManager.KEY_CURRENT_FAMILY_ID));
//        dataEntity.setPlatform("Android");
//        dataEntity.setUser_id(UserManager.INSTANCE.getUserInfo(UserManager.KEY_ID));
//        entity.setData(dataEntity);
//        return GsonUtils.toJson(entity);
//    }

    public static boolean isSuccess(BaseEntity entity) {
        return entity != null && entity.getError_code() == CODE_SUCCESS;
    }
}

