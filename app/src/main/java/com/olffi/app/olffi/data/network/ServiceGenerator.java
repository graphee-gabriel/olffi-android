package com.olffi.app.olffi.data.network;

import android.content.Context;

import com.olffi.app.olffi.BuildConfig;
import com.olffi.app.olffi.data.Auth;
import com.olffi.app.olffi.data.network.services.GetService;
import com.squareup.moshi.Moshi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by gabrielmorin on 10/01/2017.
 */

public class ServiceGenerator {

    private final static String TAG = ServiceGenerator.class.getSimpleName();

    private static OkHttpClient httpClient;
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(MoshiConverterFactory
                            .create(new Moshi.Builder().build()));

    private static OkHttpClient getHttpClientInstance(Context context) {
        if (httpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (builder != null) {
                //httpClient.interceptors().clear();
                /*if (BuildConfig.USE_DEBUG) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                    builder.addInterceptor(interceptor);
                }*/
                builder.addInterceptor(chain -> {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header(Auth.HEADER_CREDENTIALS, Auth.getCredentials(context))
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });
                httpClient = builder.build();
            } else {
                return builder == null ? null : builder.build();
            }
        }

        return httpClient;
    }


    public static void clear() {
        httpClient = null;
    }

    private static <S> S createService(Context context, Class<S> serviceClass) {
        Retrofit retrofit = builder.client(getHttpClientInstance(context)).build();
        return retrofit.create(serviceClass);
    }



    public static GetService get(Context context) {
        return createService(context, GetService.class);
    }
}
