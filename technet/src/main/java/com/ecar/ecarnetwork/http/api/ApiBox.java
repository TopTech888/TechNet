package com.ecar.ecarnetwork.http.api;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.ecar.ecarnetwork.http.converter.ConverterFactory;
import com.ecar.ecarnetwork.http.util.ConstantsLib;
import com.ecar.ecarnetwork.http.util.HttpsUtils;
import com.ecar.ecarnetwork.http.util.NetWorkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * ===============================================
 * <p>
 * 类描述: retrofit 代理包装类
 * <p>
 * 创建人:   happy
 * <p>
 * 创建时间: 2016/6/22 0022 上午 10:42
 * <p>
 * 修改人:   happy
 * <p>
 * 修改时间: 2016/6/22 0022 上午 10:43
 * <p>
 * 修改备注: BaseUrl 、Debug 、应用上下文需要作为参数注入
 * <p>
 * ===============================================
 */
public class ApiBox {

    //可能有多个代理类，每个代理类要求的时间不一样
    private int CONNECT_TIME_OUT = 10 * 1000;//跟服务器连接超时时间
    private int READ_TIME_OUT = 10 * 1000;    // 数据读取超时时间
    private int WRITE_TIME_OUT = 10 * 1000;   //数据写入超时时间
    private static final String CACHE_NAME = "cache";   //缓存目录名称


    /**
     * 单例 持有引用
     */
    private final Gson gson;
    private OkHttpClient okHttpClient;

    public Application application;//应用上下文(需注入参数)
    private File cacheFile;//缓存路径

    private Map<String, Object> serviceMap;


    /**
     * 在访问时创建单例
     */
    private static class SingletonHolder {
        private static ApiBox INSTANCE;// = new ApiBox();
    }

    /**
     * 获取单例
     */
    public static ApiBox getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 构造方法
     */
    private ApiBox(Builder builder) {
        //1.设置应用上下文、debug参数\
        ConstantsLib.DEBUG = builder.debug;
        ConstantsLib.VeriNgis = builder.veriNgis;
        ConstantsLib.REQUEST_KEY = builder.reqKey;
        this.application = builder.application;
        this.cacheFile = builder.cacheDir;
        this.serviceMap = new HashMap<>();
        if (builder.connetTimeOut > 0) {
            this.CONNECT_TIME_OUT = builder.connetTimeOut;
        }
        if (builder.readTimeOut > 0) {
            this.READ_TIME_OUT = builder.readTimeOut;
        }

        if (builder.writeTimeOut > 0) {
            this.WRITE_TIME_OUT = builder.writeTimeOut;
        }

        //2.gson
        gson = getReponseGson();

        //3.okhttp
        okHttpClient = getClient();
    }

    /**
     * 提供一个动态创建代理的 方法。
     */
    public <T> T createService(Class<T> serviceClass, String baseUrl) {
        //4.创建retrofit. 3.1 请求客户端 3.2 GsonAdpter转换类型 3.3 支持Rxjava 3.4.基url

        //1.缓存中获取
        if (TextUtils.isEmpty(baseUrl)) {
            baseUrl = "";
        }
//        Object serviceObj = serviceMap.get(serviceClass.getName() + baseUrl);
//        if (serviceObj != null) {
//            return (T) serviceObj;
//        }

        //2.创建
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(ConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        T service = retrofit.create(serviceClass);
        serviceMap.put(serviceClass.getName() + baseUrl, service);
        return service;
    }

    /**
     * Builder
     */
    public static final class Builder {
        private Application application;//应用上下文(需注入参数)
        private File cacheDir;//缓存路径
        private boolean debug;
        private String appId;

        private String reqKey;
        private int connetTimeOut;
        private int readTimeOut;
        private int writeTimeOut;
        private boolean veriNgis = true;
        private String header;

        private Map<String, String> serverCodeMap = new HashMap<>();

        public Builder application(Application application) {
            this.application = application;
            this.cacheDir = new File(application.getCacheDir(), CACHE_NAME);
            return this;
        }


        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder codeMap(HashMap<String, String> serverCodeMap) {
            ConstantsLib.serverCodeMap = serverCodeMap;
            return this;
        }

        public Builder veriNgis(boolean veriNgis) {
            this.veriNgis = veriNgis;//设置绕过参数
            return this;
        }

        public Builder reqKey(String reqKey) {
            this.reqKey = reqKey;
            return this;
        }

        public Builder appId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder connetTimeOut(int connetTime) {
            this.connetTimeOut = connetTime;
            return this;
        }

        public Builder readTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder writeTimeOut(int writeTimeOut) {
            this.writeTimeOut = writeTimeOut;
            return this;
        }

        public Builder setHeader(String header) {
            this.header = header;
            return this;
        }

        public ApiBox build() {
            if (SingletonHolder.INSTANCE == null) {
                ApiBox apiBox = new ApiBox(this);
                SingletonHolder.INSTANCE = apiBox;
            } else {
                SingletonHolder.INSTANCE.application = this.application;
                ConstantsLib.DEBUG = this.debug;
                ConstantsLib.VeriNgis = this.veriNgis;
                ConstantsLib.REQUEST_KEY = this.reqKey;
                ConstantsLib.HEADER_TOKEN = this.header;
                ConstantsLib.serverCodeMap = this.serverCodeMap;

            }

            return SingletonHolder.INSTANCE;
        }

    }

    /**
     * 创建ok客户端
     */
    private OkHttpClient getClient() {
        //1. 设置打印log
//        HttpLoggingInterceptor interceptor = getLogInterceptor();

        //2.支持https
//        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
        HostnameVerifier hostnameVerifier = HttpsUtils.getHostnameVerifier();
        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslFactory();

        //3.缓存
        Cache cache = getReponseCache();

        //4.配置创建okhttp客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(getHeader(new String[]{"1111111111111111"}, new String[]{"222222222222222222"}))
                .addInterceptor(getLogInterceptor())//
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS) //与服务器连接超时时间
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)//路由等失败自动重连
                .sslSocketFactory(sslSocketFactory)//https 绕过验证
                .hostnameVerifier(hostnameVerifier)
//                .cache(cache)//缓存
//                .addNetworkInterceptor(new HttpCacheInterceptor())//
//                .cookieJar()//cookie
                .build();
//        builder.interceptors().add(interceptor);
        return okHttpClient;
    }

    private Interceptor getResponseInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                response.header(ConstantsLib.RESPONES_HEADERNAME, "");
                return response;
            }
        };
    }

    /****************************************
     方法描述：添加header
     @param  headerKeys  头部key集合
     @param  headerValues  头部key值集合

     @return
     ****************************************/
    public void setHeader(String[] headerKeys, String[] headerValues) {
        //1. 设置打印log
//        HttpLoggingInterceptor interceptor = getLogInterceptor();

        //2.支持https
//        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslSocketFactory(null, null, null);
        HostnameVerifier hostnameVerifier = HttpsUtils.getHostnameVerifier();
        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
        SSLSocketFactory sslSocketFactory = HttpsUtils.getSslFactory();

        //3.缓存
        Cache cache = getReponseCache();

        //4.配置创建okhttp客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(getHeader(headerKeys, headerValues))
                .addInterceptor(getLogInterceptor()).//
                connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS) //与服务器连接超时时间
                .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)//路由等失败自动重连
                .sslSocketFactory(sslSocketFactory)//https 绕过验证
                .hostnameVerifier(hostnameVerifier);
        okHttpClient = builder.build();
    }
//
//    //网络状态判断
//    private Interceptor getNetStateInterceptor() {
//        return new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
////                Request request = chain.request();
////                Log.e(TAG, "okhttp3:" + request.toString());//输出请求前整个url
////                long t1 = System.nanoTime();
//                okhttp3.Response response = chain.proceed(chain.request());
////                long t2 = System.nanoTime();
////          Log.v(TAG,response.request().url()+response.headers());//输出一个请求的网络信息
//                okhttp3.MediaType mediaType = response.body().contentType();
//                String content = response.body().string();
//                Log.e("response", "response head:" + response.header(ConstantsLib.RESPONES_HEADERNAME, "response不存在此head"));//输出返回信息
//                return response;
//            }
//        };
//    }

    //head
    public Interceptor getHeader(final String[] headerKeys, final String[] headerValues) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (headerKeys != null && headerKeys.length != 0) {
                    int leng = headerKeys.length;
                    for (int i = 0; i < leng; i++) {
                        builder.header(headerKeys[i], headerValues[i]);
                    }
                }
                Request request = builder.build();
                return chain.proceed(request);
            }
        };
    }


    /**
     * 设置打印log
     * 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
     *
     * @return
     */
    private HttpLoggingInterceptor getLogInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (ConstantsLib.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return interceptor;
    }

    /**
     * 缓存路径
     *
     * @return
     */
    private Cache getReponseCache() {
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        return cache;
    }

    /**
     * 配置gson对象
     */
    private Gson getReponseGson() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
//                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写
//                .registerTypeAdapter(String.class, new StringConverter())//对为null的字段进行转换
                .create();
        return gson;
    }

    /**
     * 方法描述：取消所有请求
     * <p>
     *
     * @param
     * @return
     */
    public void cancleAllRequest() {
        okHttpClient.dispatcher().cancelAll();
    }

    /**
     * 缓存拦截器
     */
    private Interceptor cacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(application)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .url(path)
                        .build();
            }

            Response response = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(application)) {//有网保持一小时
                int maxAge = 60 * 60; // read from cache for 1 minute
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {//无网保持四周
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    };
}