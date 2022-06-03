package com.app.yellodriver.StripeTerminal;

import android.util.Log;

import com.app.yellodriver.util.YLog;
import com.stripe.stripeterminal.callable.TerminalListener;
import com.stripe.stripeterminal.model.external.ConnectionStatus;
import com.stripe.stripeterminal.model.external.PaymentStatus;
import com.stripe.stripeterminal.model.external.Reader;
import com.stripe.stripeterminal.model.external.ReaderEvent;

import org.jetbrains.annotations.NotNull;

/**
 * The `TerminalEventListener` implements the [TerminalListener] interface and will
 * forward along any events to other parts of the app that register for updates.
 *
 * TODO: Finish implementing
 */
public class TerminalEventListener implements TerminalListener {

    @Override
    public void onReportReaderEvent(@NotNull ReaderEvent event) {
        YLog.e("ReaderEvent", event.toString());
    }

    @Override
    public void onReportLowBatteryWarning() {
        Log.i("LowBatteryWarning", "");
    }

    @Override
    public void onUnexpectedReaderDisconnect(@NotNull Reader reader) {
        YLog.e("UnexpectedDisconnect", reader.getSerialNumber() != null ?
                reader.getSerialNumber() : "reader's serialNumber is null!");
    }

    @Override
    public void onConnectionStatusChange(@NotNull ConnectionStatus status) {
        YLog.e("ConnectionStatusChange", status.toString());
    }

    @Override
    public void onPaymentStatusChange(@NotNull PaymentStatus status) {
        YLog.e("PaymentStatusChange", status.toString());
    }
}
