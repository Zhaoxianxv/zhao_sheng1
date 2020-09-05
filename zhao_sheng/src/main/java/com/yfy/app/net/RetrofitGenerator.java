package com.yfy.app.net;


import com.yfy.final_tag.Base;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Retrofit变量初始化
 */
public class RetrofitGenerator {

    //接口
    public static InterfaceApi weatherInterfaceApi;

    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);

    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

   private static Retrofit.Builder retrofitBuilder =  new Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
            .baseUrl(Base.RETROFIT_URI);

    public static <S> S createService(Class<S> serviceClass) {
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original
                        .newBuilder()
                        .header("Content-Type", "text/xml;charset=UTF-8")   // 对于SOAP 1.1， 如果是soap1.2 应是Content-Type: application/soap+xml; charset=utf-8
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
//                Log.e(TAG, "intercept: " +request.tag());
                return chain.proceed(request);
            }
        });

        OkHttpClient client = okHttpClient
                .connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .sslSocketFactory(SSLHelper.getSSLCertifcation(context))//为OkHttp对象设置SocketFactory用于双向认证
//                .hostnameVerifier(new UnSafeHostnameVerifier())
//                .build();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://10.2.8.56:8443")
//                .addConverterFactory(GsonConverterFactory.create())//添加 json 转换器
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加 RxJava 适配器
//                .client(okHttpClient)//添加OkHttp代理对象
//                .build();

        Retrofit retrofit = retrofitBuilder.client(client).build();

        return retrofit.create(serviceClass);
    }

    public static InterfaceApi getWeatherInterfaceApi() {
        if(weatherInterfaceApi == null) {
            weatherInterfaceApi = createService(InterfaceApi.class);
        }
        return weatherInterfaceApi;
    }




}
