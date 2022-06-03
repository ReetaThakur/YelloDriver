package com.app.yellodriver.appoloservice;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.cache.http.ApolloHttpCache;
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.app.yellodriver.util.Constants;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApoloCall {

    public static ApolloClient getApolloClient(Context context) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

        okHttpClient.addInterceptor(chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    //.header("x-hasura-admin-secret", "vj3erCoz")
                    //.header("authorization", "Bearer " + Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_TOKEN, ""))
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        })
                .addInterceptor(new FirebaseUserIdTokenInterceptor());

        // Directory where cached responses will be stored
        File file = new File(context.getCacheDir(), "apolloCache");
        // Size in bytes of the cache
        int size = 1024 * 1024;
        // Create the http response cache store
        DiskLruHttpCacheStore cacheStore = new DiskLruHttpCacheStore(file, size);

        return ApolloClient.builder()
                .serverUrl(Constants.BASE_URL)
//                .httpCache(new ApolloHttpCache(cacheStore))
                .okHttpClient(okHttpClient.build())
                .subscriptionTransportFactory(
                        new WebSocketSubscriptionTransport.Factory(
                                Constants.BASE_URL_SUBSCRIPTION,
                                okHttpClient.build()))
                .build();
    }
}