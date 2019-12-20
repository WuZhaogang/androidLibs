package com.wzg.core.http;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.orhanobut.logger.Logger;
import com.wzg.core.BaseApp;
import com.wzg.core.BuildConfig;
import com.wzg.core.Constant;
import com.wzg.core.model.LoginResponseModel;
import com.wzg.core.utils.SharedPreferencesUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiRetrofit {

    private static ApiRetrofit mApiRetrofit;
    public final Retrofit mRetrofit;

    private GsonBuilder builder;

    private Gson mGson;
    public  static String BASE_SERVER_URL;

    //缓存配置
    private Interceptor mCacheInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!NetWorkUtils.INSTANCE.isNetworkAvailable(BaseApp.Companion.getInstance())) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtils.INSTANCE.isNetworkAvailable(BaseApp.Companion.getInstance())) {
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };

    /**
     * 请求访问quest和response拦截器
     */
    private Interceptor mLogInterceptor = new Interceptor() {
        private final Charset UTF8 = Charset.forName("UTF-8");

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Logger.e(request.toString() + request.headers().toString());
            Response response = chain.proceed(chain.request());
            String body = "";
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                body = buffer.readString(charset);
            }
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            if (!TextUtils.isEmpty(body)) {
                try {
                    Logger.e(body);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Logger.json(content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();
        }
    };


    private ApiRetrofit() {
        //cache url
        File httpCacheDirectory = new File(getDiskCachePath(BaseApp.Companion.getInstance()), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//请求/响应行 + 头 + 体
        /*
         * 增加头部信息的拦截器
         */
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                LoginResponseModel userModel = SharedPreferencesUtil.getPreferences(Constant.SP_USER_KEY);
                if (userModel != null && !TextUtils.isEmpty(userModel.getToken())) {
                    builder.addHeader("X-UserToken", userModel.getToken());
                }
                String url = chain.request().url().toString();
                StringBuilder sb = new StringBuilder(url);
                String managementService = "management-service/";
                String commonService = "common-service/";
                String domainService = "domain-service/";
                if (url.contains(managementService)) {
                    sb.insert(url.indexOf(managementService) + managementService.length(), "lcs-management/");
                } else if (url.contains(commonService)) {
                    sb.insert(url.indexOf(commonService) + commonService.length(), "lcs-common/");
                } else if (url.contains(domainService)) {
                    sb.insert(url.indexOf(domainService) + domainService.length(), "lcs-domain/");
                }
                Logger.e(sb.toString());
                builder.url(sb.toString());
                return chain.proceed(builder.build());
            }
        };
        OkHttpClient client = RetrofitUrlManager.getInstance().with(new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)//添加头部信息拦截器
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)).build();
        initGsonBuilder();
        if (mGson == null) {
            mGson = builder.create();
        }

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(CustomGsonConverterFactory.create(mGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持RxJava
                .client(client)
                .build();
    }

    private void initGsonBuilder() {
        builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.registerTypeAdapter(Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
        builder.registerTypeAdapter(Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        builder.registerTypeAdapter(Date.class, new DoubleSerializer());
    }

    private String getDiskCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    public static ApiRetrofit getInstance() {
        if (mApiRetrofit == null) {
            synchronized (Object.class) {
                if (mApiRetrofit == null) {
                    mApiRetrofit = new ApiRetrofit();
                }
            }
        }
        return mApiRetrofit;
    }

    public Gson getGson() {
        if (mGson == null) {
            if (builder == null) {
                initGsonBuilder();
            }
            mGson = builder.create();
        }
        return mGson;
    }

    private class DoubleSerializer implements JsonSerializer<Double> {
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == src.longValue())
                return new JsonPrimitive(src.longValue());
            return new JsonPrimitive(src);
        }
    }

    private class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }

    private class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsJsonPrimitive().getAsLong());
        }
    }
}
