package com.app.yellodriver.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.app.yellodriver.R;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.fragment.HomeFragment;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.QRCodeFoundListener;
import com.app.yellodriver.util.QRCodeImageAnalyzer;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.Result;

import java.util.concurrent.ExecutionException;

import io.paperdb.Paper;

public class ScanQRcodeActivity extends BaseActivity implements View.OnClickListener {
    private CodeScanner mCodeScanner;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView scannerView;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_scan_qr_code;
    }

    @Override
    protected void initializeComponents() {
        setUpToolBar();
        scannerView = findViewById(R.id.scanner_view);
        if (Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_IS_RIDE_ONGOING, false))
        {
            Intent intent = new Intent(ScanQRcodeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        startCamera();
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindCameraPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(this, "Error starting camera " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        scannerView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(scannerView.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {
                    Intent in = getIntent();
                    in.putExtra("qrCodeData", "" + _qrCode);
                    setResult(Activity.RESULT_OK,in);
                    finish();


            }

            @Override
            public void qrCodeNotFound() {
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageAnalysis, preview);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
       // mCodeScanner.releaseResources();
        super.onPause();
    }
    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar_notification);
        TextView tvName = findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.scanqrcode);

        imgBack.setOnClickListener(view1 -> finish());
    }

}