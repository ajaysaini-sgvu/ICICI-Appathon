/*
 *
 *  Copyright 2017 Nagarro Agile. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package com.icici.iciciappathon.shopping;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.TextureView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.icici.iciciappathon.R;
import com.icici.iciciappathon.databinding.ActivityScanBarcodeBinding;
import com.icici.iciciappathon.ui.BaseActivity;
import com.icici.iciciappathon.utils.LogUtils;

import java.io.IOException;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ScanBarcodeActivity extends BaseActivity {

    private TextureView textureView;

    private BarcodeDetector barcodeDetector;

    private Camera camera;

    private String TAG = LogUtils.makeLogTag(ScanBarcodeActivity.class);

    private int nothing;

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityScanBarcodeBinding activityScanBarcodeBinding = setContentView(this, R.layout.activity_scan_barcode);

        frameLayout = activityScanBarcodeBinding.textureFrameLayout;

        barcodeDetector = new BarcodeDetector.Builder(this).build();

        ScanBarcodeActivityPermissionsDispatcher.requestCameraPermissionWithCheck(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        ScanBarcodeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

//        if (permissions[0].equals(Manifest.permission.CAMERA)) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initializeCamera();
//            }
//        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void requestCameraPermission() {
        initializeCamera();
    }

    void initializeCamera() {

        TextureView textureView = new TextureView(this);
        textureView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
        frameLayout.addView(textureView);

        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                camera = Camera.open();

                /* Set Auto focus */
                Camera.Parameters parameters = camera.getParameters();
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                }

                camera.setParameters(parameters);

                try {
                    camera.setPreviewTexture(surface);
                } catch (IOException io) {
                    LogUtils.LOGD(TAG, io.getMessage());
                }
                camera.startPreview();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                LogUtils.LOGD(TAG, "Hello");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                try {
                    camera.stopPreview();
                    camera.release();
                } catch (Exception e) {
                    LogUtils.LOGD(TAG, e.getMessage());
                }
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
                ++nothing;

                if (nothing == 20) {
                    promptCheckOut(ScanBarcodeActivity.this, getString(R.string.scan_barcode_ask_to_check_out));
                }
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    LogUtils.LOGD(TAG, barcodes.valueAt(0).displayValue);
                }
            }
        });

    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.permission_camera_rationale)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .show();
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showPermissionDeniedForCamera() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskAgainForCamera() {
        Toast.makeText(this, R.string.permission_camera_never_ask_again, Toast.LENGTH_SHORT).show();
    }

}
