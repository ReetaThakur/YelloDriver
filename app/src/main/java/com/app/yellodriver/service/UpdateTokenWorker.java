package com.app.yellodriver.service;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.JWTUtils;
import com.app.yellodriver.util.YLog;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class UpdateTokenWorker extends Worker {

    private static final String TAG = UpdateTokenWorker.class.getSimpleName();

    public UpdateTokenWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NotNull
    @Override
    public Result doWork() {

        // Do the work here

        // Initialize Firebase Auth
         FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Task<GetTokenResult> tokenResultTask = currentUser.getIdToken(true);

            tokenResultTask.addOnSuccessListener(tokenResult -> {
                String token = tokenResult.getToken();
                YLog.e(TAG+"UserToken", "" + token);
                Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_TOKEN, token);

                try {
                    JWTUtils jToken = new JWTUtils();
                    jToken.decoded(token);
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_ID, jToken.getHasuraUserId());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_NAME, jToken.getHasuraUserName());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_EMAIL, jToken.getHasuraUserEmail());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_PHONE, jToken.getHasuraUserPhone());
                    Paper.book(Constants.PAPER_BOOK_USER).write(Constants.PAPER_KEY_HASURA_ROLE, jToken.getHasuraUserRole());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

            // Indicate whether the work finished successfully with the Result
            return Result.success();
        }
        return Result.failure();
    }
}