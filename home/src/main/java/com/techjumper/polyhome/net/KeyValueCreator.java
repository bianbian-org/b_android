package com.techjumper.polyhome.net;

import com.techjumper.lib2.others.KeyValuePair;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/3
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class KeyValueCreator {

    public static KeyValuePair login(String mobile, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("password", password);
    }

    public static KeyValuePair sendVerificationCode(String mobile, String type) {
        return newPair()
                .put("mobile", mobile)
                .put("type", type);
    }

    public static KeyValuePair register(String mobile, String sms_captcha, String password) {
        return newPair()
                .put("mobile", mobile)
                .put("sms_captcha", sms_captcha)
                .put("password", password);
    }

    public static KeyValuePair findPassword(String mobile, String sms_captcha, String new_password) {
        return newPair()
                .put("mobile", mobile)
                .put("sms_captcha", sms_captcha)
                .put("new_password", new_password);
    }

    public static KeyValuePair familyUserQueryFamilies(String user_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket);
    }

    private static KeyValuePair<String, Object> newPair() {
        return new KeyValuePair<>();
    }


    public static KeyValuePair newFamily(String user_id, String ticket, String family_name) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_name", family_name);
    }

    public static KeyValuePair roomQuery(String user_id, String ticket, String room_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("room_id", room_id);

    }

    public static KeyValuePair roomAllQuery(String user_id, String ticket, String family_id, String query_user_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id).put("query_user_id", query_user_id);

    }

    public static KeyValuePair familyUserQueryUsers(String user_id, String ticket, String family_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id);
    }

    public static KeyValuePair newRoom(String user_id, String ticket, String room_name, String family_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("room_name", room_name).put("family_id", family_id);
    }

    public static KeyValuePair renameRoom(String user_id, String ticket, String room_id, String room_name) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("room_id", room_id).put("room_name", room_name);
    }

    public static KeyValuePair deleteRoom(String user_id, String ticket, String room_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("room_id", room_id);
    }

    public static KeyValuePair newMember(String user_id, String ticket, String family_id, String user_id_new) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id).put("user_id_new", user_id_new);
    }

    public static KeyValuePair addCamera(String user_id, String ticket, String family_id, String room_id, String sn, String chn_index, String camera_name) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("room_id", room_id)
                .put("sn", sn)
                .put("chn_index", chn_index)
                .put("camera_name", camera_name);
    }

    public static KeyValuePair queryFamily(String user_id, String ticket, String family_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id);
    }

    public static KeyValuePair queryFamilyInfo(String user_id, String ticket, String family_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id);
    }

    public static KeyValuePair newFamilyMember(String user_id, String ticket, String family_id, String user_id_new) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id).put("user_id_new", user_id_new);
    }

    public static KeyValuePair deleteMember(String user_id, String ticket, String family_id, String delete_user_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id).put("delete_user_id", delete_user_id);
    }

    public static KeyValuePair updateFamily(String user_id, String ticket, String family_id, String family_name) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("family_id", family_id).put("family_name", family_name);
    }
    /**
     * 不能重命名成员,所以无接口,暂时不做
     *
     * @return
     */
//    public static KeyValuePair renameMember() {
//        return newPair().put()
//    }

    /**
     * 成员管理页面,给新增成员添加房间管理权限
     */
    public static KeyValuePair newRoomMember(String user_id, String ticket, String add_user_id, String room_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("add_user_id", add_user_id).put("room_id", room_id);
    }

    /**
     * 成员管理界面,删除成员
     */
    public static KeyValuePair deleteRoomMember(String user_id, String ticket, String delete_room_id, String delete_user_id) {
        return newPair().put("user_id", user_id).put("ticket", ticket).put("delete_room_id", delete_room_id).put("delete_user_id", delete_user_id);
    }

    /**
     * 构造tcp的参数
     */
    public static KeyValuePair generateParamMethod(Object param, String method) {
        return newPair()
                .put("param", param)
                .put("method", method);
    }

    public static KeyValuePair empty() {
        return newPair();
    }

    public static KeyValuePair tcpRegisterToCloud(String userid, String ticket, String familyid, String isbind) {
        return newPair()
                .put("userid", userid)
                .put("ticket", ticket)
                .put("familyid", familyid)
                .put("isbind", isbind);
    }

    public static KeyValuePair controlSocketSerialDevice(String way, String status, String sn, String productname) {
        return newPair()
                .put("way", way)
                .put("status", status)
                .put("sn", sn)
                .put("productname", productname);
    }

    public static KeyValuePair controlIntelligentLock(String type, String sn) {
        return newPair()
                .put("type", type)
                .put("sn", sn);
    }

    public static KeyValuePair controlNightLight(String time, String sn, String productname) {
        return newPair()
                .put("time", time)
                .put("productname", productname)
                .put("sn", sn);
    }

    public static KeyValuePair changeDeviceName(String name, String sn, String way) {
        return newPair()
                .put("name", name)
                .put("sn", sn)
                .put("way", way);
    }

    public static KeyValuePair<String, Object> queryRealTimeData(String deviceId, String userId) {
        return newPair()
                .put("method", "getsensorsdata")
                .put("deviceId", deviceId)
                .put("userId", userId);
    }

    public static KeyValuePair infraredLearn(String key, String name, String sn) {
        return newPair()
                .put("key", key)
                .put("name", name)
                .put("sn", sn);
    }

    public static KeyValuePair queryBySn(String sn) {
        return newPair()
                .put("sn", sn);
    }

    public static KeyValuePair remoteControl(String key, String sn) {
        return newPair()
                .put("key", key)
                .put("sn", sn);
    }

    public static KeyValuePair controlFreshAirSystem(String type, String sn) {
        return newPair()
                .put("type", type)
                .put("sn", sn);
    }

    public static KeyValuePair changeRoomId(String room_id, String sn) {
        return newPair()
                .put("roomid", room_id)
                .put("sn", sn);
    }


    public static KeyValuePair deleteFamily(String user_id, String ticket, String family_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id);
    }

    public static KeyValuePair addDevice(String sn) {
        return newPair()
                .put("sn", sn);
    }

    public static KeyValuePair deleteDevice(String sn) {
        return newPair()
                .put("sn", sn);
    }


    public static KeyValuePair unbindHost(String user_id, String ticket, String family_id, String device_id) {
        return newPair()
                .put("user_id", user_id)
                .put("ticket", ticket)
                .put("family_id", family_id)
                .put("device_id", device_id);
    }

    public static KeyValuePair onClickScene(String senceid) {
        return newPair()
                .put("senceid", senceid);
    }

    public static KeyValuePair changeSecurityState(String sceneId, String state) {
        return newPair()
                .put("senceid", sceneId)
                .put("state", state);
    }

    public static KeyValuePair deleteScene(String sceneid) {
        return newPair()
                .put("senceid", sceneid);
    }

    public static KeyValuePair uploadShortcutDeviceInfo(String user_id, String family_id, String ticket
            , String scene_device) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket)
                .put("scene_device", scene_device);
    }

    public static KeyValuePair uploadShortcutSceneInfo(String user_id, String family_id, String ticket
            , String scenes) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket)
                .put("scenes", scenes);
    }

    public static KeyValuePair fetchShortcutInfo(String user_id, String family_id, String ticket) {
        return newPair()
                .put("user_id", user_id)
                .put("family_id", family_id)
                .put("ticket", ticket);
    }

    public static class TcpMethod {
        public static final String REGISTER_HOST_TO_CLOUD = "RegisterHostToCloud";
        public static final String GET_DEV_LIST = "GetDevList";
        public static final String GET_DB_DEV_LIST = "GetDbDevList";
        public static final String CONTROL_DEV_CMD = "ControlDevCmd";
        public static final String CHANGE_DEVICE_NAME = "ChangeDeviceName";
        public static final String REMOTE_STUDY_CMD = "RemoteStudyCmd";
        public static final String QUERY_REMOTE_KEY_CMD = "QueryRemoteKeyCmd";
        public static final String REMOTE_SEND_CMD = "RemoteSendCmd";
        public static final String CONTROLLS_DEV_CMD = "ControlLSdevCmd";
        public static final String QUERY_LS_STATE_CMD = "QueryLSStateCmd";
        public static final String CHANGE_ROOM_ID = "ChangeRoomId";
        public static final String GET_SCENE_DETAILS = "GetSenceDetails";
        public static final String GET_SCENE_LIST = "GetSenceList";
        public static final String ADD_CONTROL_DEV_CMD = "AddControlDevCmd";
        public static final String DELETE_CONTROL_DEV_CMD = "DeleteControlDevCmd";
        public static final String SAVE_SCENE = "SaveSence";
        public static final String ON_CLICK_SCENE = "OnClickSence";
        public static final String DISCOVERY = "Discovery";
        public static final String IKAIR = "Ikair";
        public static final String INTEL_LOCK_CTRL_CMD = "IntelLockCtrlCmd";
        public static final String DELETE_SCENE = "DeleteSence";
        public static final String CHANGE_SECURITY_STATE = "ChangeSecurityState";
    }
}
