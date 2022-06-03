package com.app.yellodriver.StripeTerminal;

import com.stripe.stripeterminal.callable.ConnectionTokenCallback;
import com.stripe.stripeterminal.callable.ConnectionTokenProvider;

/**
 * A simple implementation of the [ConnectionTokenProvider] interface. We just request a
 * new token from our backend simulator and forward any exceptions along to the SDK.
 */
public class TokenProvider implements ConnectionTokenProvider {

    private String token;

    public TokenProvider(String token) {
        this.token = token;
    }

    @Override
    public void fetchConnectionToken(ConnectionTokenCallback callback) {
        //            final String token = ApiClient.createConnectionToken();
        callback.onSuccess(token);
    }
}