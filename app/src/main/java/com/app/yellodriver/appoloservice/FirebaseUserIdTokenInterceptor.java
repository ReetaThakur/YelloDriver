package com.app.yellodriver.appoloservice;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FirebaseUserIdTokenInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                throw new Exception("User is not logged in.");
            } else {
                Task<GetTokenResult> task = user.getIdToken(false);
                GetTokenResult tokenResult = Tasks.await(task);
                String idToken = tokenResult.getToken();

                if (idToken == null) {
                    throw new Exception("idToken is null");
                } else {
                    //YLog.e("GotFromInterceptor Token",idToken);
                    //Todo:Discuss is everytime update required?
                    //Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_TOKEN, idToken);
                    Request modifiedRequest = request.newBuilder()
                            .addHeader("authorization", "Bearer " + idToken)
                            //.addHeader(X_FIREBASE_ID_TOKEN, idToken)
                            .build();
                    return chain.proceed(modifiedRequest);
                }
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}