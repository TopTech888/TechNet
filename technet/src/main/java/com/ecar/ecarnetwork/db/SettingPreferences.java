package com.ecar.ecarnetwork.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;


public class SettingPreferences {
    private final static String PREFS_NAME = "parkbees_sp";//BuildConfig.SP_NAME;//"";

    private SharedPreferences shareData;
    private final String UserId = "user_id";// 用户ID
    private final String UserName = "user_name";// 用户名
    private final String ParkNo = "park_no";// 停车号
    private final String UserPhoneNumber = "user_phone_number";// 用户手机号
    private final String Auto_Login = "auto_login";// 自动登录
    private final String Remember_Pw = "remember_pw";// 记住密码
    private final String PASSWORD = "password";// 密码
    private final String FirstStart = "first_start";// 应用首次启动
    private final String ParkEndTime = "park_end_time";// 停车结束时间
    private final String Phone = "invoice_phone";// 停车号
    private final String Address = "invoice_address";// 停车号
    private final String Name = "invoice_name";// 停车号
    private final String Invoice = "invoice_ice";// 停车号
    private final String ParkNumber = "park_number";// 车牌号
    private final String isNewErrorTag = "is_new_error_log";
    private final String phoneTypeTag = "phone_type";
    private final String versionTag = "app_version";
    private final String errorInfoTag = "error_error_info";
    private final String osTag = "system";
    private final String SID = "user_sid";// 会话ID
    private final String headImgTime = "head_Img_Time";// 会话ID


    public static final int DEFAULT_VERSION = -200;// 默认版本
    public static final String Last_Version = "version";// 存储的最新版本
    private static final String CtFirstOpendParking = "firstpark";// 第一次进入应用
    private static final String CtFirstOpendPark = "firstparkopen";// 第一次进入应用


    SpUtil spUtil;

    private static SettingPreferences settingPreferences;


    private SettingPreferences(Context context) {
        shareData = context.getSharedPreferences(PREFS_NAME, 0);
        spUtil = new SpUtil(context, PREFS_NAME);
    }


    public static SettingPreferences getDefault(Context context) {
        if (settingPreferences == null) {
            synchronized (SettingPreferences.class) {
                settingPreferences = new SettingPreferences(context);
            }
        }
        return settingPreferences;

    }

    public String getUserID() {
        return shareData.getString(UserId, "");
    }

    public void setUserId(String str) {
        Editor editor = shareData.edit();
        editor.putString(UserId, str);
        editor.commit();
    }

    public String getUserName() {
        return shareData.getString(UserName, "");
    }

    public void setUserName(String str) {
        Editor editor = shareData.edit();
        editor.putString(UserName, str);
        editor.commit();
    }

//    public String getComId(){
//        return shareData.getString(UserInfo.COMID,"");
//    }

    public String getUserPhoneNumber() {
        return shareData.getString(UserPhoneNumber, "");
    }

    public void setUserPhoneNumber(String str) {
        Editor editor = shareData.edit();
        editor.putString(UserPhoneNumber, str);
        editor.commit();
    }

    public boolean getRememberPw() {
        return shareData.getBoolean(Remember_Pw, true);
    }

    public void setRememberPw(boolean bool) {
        Editor editor = shareData.edit();
        editor.putBoolean(Remember_Pw, bool);
        editor.commit();
    }

    public boolean getFirstStart() {
        return shareData.getBoolean(FirstStart, true);
    }

    public void setFirstStart(boolean bool) {
        Editor editor = shareData.edit();
        editor.putBoolean(FirstStart, bool);
        editor.commit();
    }

    /**
     * @throws Exception
     * @功能：是否第一打开地图详情界面
     * @param：
     * @return：
     */
    public boolean isFirstOpenParking() {
        return shareData.getBoolean(CtFirstOpendParking, true);
    }

    public void setFirstOpenParking(boolean aFirst) {
        shareData.edit().putBoolean(CtFirstOpendParking, false).commit();
    }

    public boolean isFirstOpenPark() {
        return shareData.getBoolean(CtFirstOpendPark, true);
    }

    public void setFirstOpenPark(boolean aFirst) {
        shareData.edit().putBoolean(CtFirstOpendPark, false).commit();
    }

    public boolean getAutoLogin() {
        return shareData.getBoolean(Auto_Login, true);
    }

    public void setAutoLogin(boolean bool) {
        Editor editor = shareData.edit();
        editor.putBoolean(Auto_Login, bool);
        editor.commit();
    }

    public String getPassword() {
        return shareData.getString(PASSWORD, "");
    }

    public void setPassword(String count) {
        Editor editor = shareData.edit();
        editor.putString(PASSWORD, count);
        editor.commit();
    }


    public String getParkNo() {
        return shareData.getString(ParkNo, "");
    }

    public void setParkNo(String no) {
        Editor editor = shareData.edit();
        editor.putString(ParkNo, no);
        editor.commit();
    }

    public String getParkEndTime() {
        return shareData.getString(ParkEndTime, "");
    }

    public void setParkEndTime(String time) {
        Editor editor = shareData.edit();
        editor.putString(ParkEndTime, time);
        editor.commit();
    }

    public void setSid(String sid) {
        Editor editor = shareData.edit();
        editor.putString(SID, sid);
        editor.commit();
    }

    public String getSid() {
        return shareData.getString(SID, "");
    }

    public void setParkNumber(String number) {
        Editor editor = shareData.edit();
        editor.putString(ParkNumber, number);
        editor.commit();
    }

    public void setActivityUrl(String activityUrl) {
        Editor editor = shareData.edit();
        editor.putString("ActivityUrl", activityUrl);
        editor.commit();
    }

    public void setFirstNewsCreateTime(String firstNewsCreateTime) {
        Editor editor = shareData.edit();
        editor.putString("FirstNewsCreateTime", firstNewsCreateTime);
        editor.commit();
    }

    public void setFirstQuestionCreateTime(String firstQuestionCreateTime) {
        Editor editor = shareData.edit();
        editor.putString("FirstQuestionCreateTime", firstQuestionCreateTime);
        editor.commit();
    }


    public String getParkNumber() {
        return shareData.getString(ParkNumber, "");
    }

    /**
     * 保存发票信息
     */
    public void setInvoice(String phoneNo, String address, String name,
                           String invoice) {
        Editor editor = shareData.edit();
        editor.putString(Phone, phoneNo);
        editor.putString(Address, address);
        editor.putString(Name, name);
        editor.putString(Invoice, invoice);
        editor.commit();
    }

    public String getPhone() {
        return shareData.getString(Phone, "");
    }

    public String getAddress() {
        return shareData.getString(Address, "");
    }

    public String getActivityUrl() {
        return shareData.getString("ActivityUrl", "");
    }

    public String getFirstNewsCreateTime() {
        return shareData.getString("FirstNewsCreateTime", "");
    }

//    public String getMebId(){
//        return shareData.getString(UserInfo.USER_ID,"");
//    }

    public String getFirstQuestionCreateTime() {
        return shareData.getString("FirstQuestionCreateTime", "");
    }

    public String getName() {
        return shareData.getString(Name, "");
    }

    public String getInvoice() {
        return shareData.getString(Invoice, "");
    }

    /**
     * 保存账户信息到本地
     *
     * @param phoneNo 电话号码
     * @param pw      密码
     * @param
     */
    public void setUserMsg(String phoneNo, String pw, String parkid) {
        if (!TextUtils.isEmpty(phoneNo) && !TextUtils.isEmpty(pw)) {
            Editor editor = shareData.edit();
//			editor.putString(ParkNo, parkid);
            editor.putString(UserPhoneNumber, phoneNo);
            editor.putString(PASSWORD, pw);
            editor.commit();
        }
    }

    public void clearUserMsg() {
        Editor editor = shareData.edit();
        editor.putString(ParkNo, "");
        // 保留手机号码
        // editor.putString(UserPhoneNumber , "");
        editor.putString(PASSWORD, "");
        editor.commit();
    }

    /**
     * @param isNewError 是否新log
     * @param phoneType  手机型号
     * @param os         操作系统
     * @param version    应用版本号
     * @param errorInfo  错误日志
     */
    public void setErrorLog(boolean isNewError, String phoneType, String os,
                            String version, String errorInfo) {
        Editor editor = shareData.edit();
        editor.putBoolean(isNewErrorTag, isNewError);
        editor.putString(phoneTypeTag, phoneType);
        editor.putString(osTag, os);
        editor.putString(versionTag, version);
        editor.putString(errorInfoTag, errorInfo);
        editor.commit();
    }

    /**
     * 获取值 参数：cla:获取数据的类型
     */
    public Object getData(String key, Class cla, Object obj) {
        String strContent;
        int intContent;
        boolean booleanContent;
        long longContent;
        if (cla == Integer.class) {
            intContent = shareData.getInt(key, (int) obj);
            return intContent;
        } else if (cla == String.class) {
            strContent = shareData.getString(key, (String) obj);
            return strContent;
        } else if (cla == Boolean.class) {
            booleanContent = shareData.getBoolean(key, (boolean) obj);
            return booleanContent;
        } else if (cla == Long.class) {
            longContent = shareData.getLong(key, (long) obj);
            return longContent;
        } else {
            return null;
        }
    }

    /**
     * 保存值
     *
     * @param key
     * @param value
     */
    public void save(String key, Object value) {
        Editor editor = shareData.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value).commit();
        } else if (value instanceof String) {
            editor.putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value).commit();
        }
    }


    /**
     * true 有新的error日志，false 没有
     *
     * @return
     */
    public boolean getErrorFlag() {
        return shareData.getBoolean(isNewErrorTag, false);
    }

    public String getPhoneType() {
        return shareData.getString(phoneTypeTag, "");
    }

    public String getOS() {
        return shareData.getString(osTag, "");
    }

    public String getVersion() {
        return shareData.getString(versionTag, "");
    }

    public String getErrorInfo() {
        return shareData.getString(errorInfoTag, "");
    }


    /**************************************************************
     * 以下为一体化新增的全局公用参数保存/获取方法
     *********************************************************************/

    private final static String uKey = "ukey";//BuildConfig.U_NAME;//
    private final static String tsKey = "ts";//时间戳
    private final static String vKey = "vkey";//BuildConfig.V_NAME;//"v";
    private final static String tKey = "tkey";//BuildConfig.T_NAME;//"t";

//    private String userPhone = UserInfo.USER_PHONE_NUM; //手机号码
//    private String nickName = UserInfo.NICKNAME;  //昵称
    private String balance = "balance";    //钱包余额
    private String carNums = "carNums";    //车牌号

    private final String isLogin = "islogin";    //是否登录

    public void setU(String u) {
        save(uKey, u);
    }

    public void setTs(String ts) {
        save(tsKey, ts);
    }

    public void setV(String v) {
        save(vKey, v);
    }

    public void setT(String t) {
        save(tKey, t);
    }


    public String getU() {
        return (String) getData(uKey, String.class, "");
    }

    public String getV() {
        return (String) getData(vKey, String.class, "");
    }

    public String getT() {
        return (String) getData(tKey, String.class, "");
    }
    public String getTs() {
        return (String) getData(tsKey, String.class, "");
    }


//    public String getUserPhone() {
//        return (String) getData(userPhone, String.class, "");
//    }
//
//    public String getNickName() {
//        return (String) getData(nickName, String.class, "");
//    }

    public String getBalance() {
        return (String) getData(balance, String.class, "0.00");
    }


    //获取数组
    public String[] getCarNums(String key) {
        String regularEx = "#";
        String[] str = null;
        String values;
        values = (String) getData(carNums, String.class, "");
        str = values.split(regularEx);
        return str;

    }

    //获取登录状态
    public boolean getLoginStatus() {
        return (boolean) getData(isLogin, boolean.class, false);
    }

    //保存车牌号数组
    public void setCarNums(String[] values) {
        String regularEx = "#";
        String str = "";
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
        }
        save(carNums, str);
    }

    //保存登录状态
    public void setLoginStasus(boolean isLogined) {
        save(isLogin, isLogined);
    }

//    public void setUserPhone(String userPhone) {
//        save(this.userPhone, userPhone);
//    }
//
//    public void setNickName(String nickName) {
//        save(this.nickName, nickName);
//    }

    public void setBalance(String balance) {
        save(this.balance, balance);
    }


//    public String getProvince() {
//        return (String) getData(Constants.SP_PROVINCE, String.class, "");
//    }

//    public void setHeadImgTime() {
//        save(this.headImgTime, DateDeserializer.getCurrTime());
//    }
//
//    public String getHeadImgTime() {
//        return (String) getData(headImgTime, String.class, DateDeserializer.getCurrTime());
//    }
}
