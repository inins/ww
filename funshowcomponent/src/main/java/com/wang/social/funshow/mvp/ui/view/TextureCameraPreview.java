package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TextureCameraPreview extends TextureView implements TextureView.SurfaceTextureListener, Camera.PictureCallback, Camera.ShutterCallback {

    private Camera mCamera;
    private boolean isFontCamera;
    private boolean isOpenFlash;
    private OnPictureTakenListener onPictureTakenListener;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private boolean isTakenPicing = false;
    private OnStartVideoListener onStartVideoListener;
    private SurfaceTexture mSurface;

    private boolean isSurfaceAvailable;
    private boolean _autoStart;
    private Point size;

    public TextureCameraPreview(Context context) {
        this(context, null);
    }

    public TextureCameraPreview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureCameraPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        isSurfaceAvailable = true;
        mSurface = surface;
        if (_autoStart) openCamera();
    }

    private void openCamera() {
        // 1.打开相机
        mCamera = getCameraInstance(getContext(), isFontCamera);
        if (mCamera != null) {
            // 2.设置相机参数
            Camera.Parameters parameters = mCamera.getParameters();
            // 3.调整预览方向
//            CameraInfo cameraInfo = new CameraInfo();
//            Camera.getCameraInfo(isFontCamera ? 1 : 0, cameraInfo);
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
//                mCamera.setDisplayOrientation(cameraInfo.orientation);
//            else
//                mCamera.setDisplayOrientation((360 - cameraInfo.orientation) % 360);
            mCamera.setDisplayOrientation(90);
            // 4.设置预览尺寸
            size = CameraUtil.getBestCameraResolution(parameters, new Point(getMeasuredWidth(), getMeasuredHeight()));
            parameters.setPreviewSize(size.x, size.y);
            parameters.setPictureSize(size.x, size.y);
            if (!isFontCamera)
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            //5.调整拍照图片方向
            if (!isFontCamera)
                parameters.setRotation(90);
            if (isFontCamera)
                parameters.setRotation(270);
            mCamera.setParameters(parameters);
            // 7.开始相机预览
            try {
                mCamera.setPreviewTexture(mSurface);
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error setting camera preview:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        releaseCamera();
        return true;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public static Camera getCameraInstance(Context context, boolean isFontCamera) {
        Camera c = null;
        try {
            c = Camera.open(isFontCamera ? 1 : 0);
        } catch (Exception e) {
            Toast.makeText(context, "相机打开失败", Toast.LENGTH_SHORT).show();
        }
        return c;
    }

    public void takePicture(OnPictureTakenListener listener) {
        //如果照片正常处理中，再次点击拍摄，直接返回，视为无效
        if (isTakenPicing) return;
        if (listener != null) {
            onPictureTakenListener = listener;
            if (mCamera != null) {
                isTakenPicing = true;
                mCamera.takePicture(this, null, this);
            } else {
                onPictureTakenListener.onFailed("拍照失败");
            }
        }
    }

    public void setZoom(int progress) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setZoom((int) (progress * 1.0f / (40 * 100) * 40));
            mCamera.setParameters(parameters);
        }
    }

    public void switchCamera(boolean isFontCamera) {
        if (this.isFontCamera == isFontCamera) return;
        if (isRecording) {
            stopRecord();
        }
        releaseCamera();
        this.isFontCamera = isFontCamera;
        openCamera();
    }

    public void openFlash(boolean isOpenFlash) {
        if (this.isOpenFlash == isOpenFlash) return;
        this.isOpenFlash = isOpenFlash;
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(isOpenFlash ? parameters.FLASH_MODE_TORCH : parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
    }

    public void switchCamera() {
        switchCamera(!isFontCamera);
    }

    public void openFlash() {
        openFlash(!isOpenFlash);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (isFontCamera) {
            bitmap = reversalBitmap(bitmap, -1, 1);
        }
        if (onPictureTakenListener != null) {
            onPictureTakenListener.onSuccess(bitmap);
        }

        // 使拍照结束后重新预览
        releaseCamera();
        openCamera();
        isTakenPicing = false;
    }

    //水平翻转照片
    private Bitmap reversalBitmap(Bitmap srcBitmap, float sx, float sy) {
        Bitmap cacheBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        int w = cacheBitmap.getWidth();
        int h = cacheBitmap.getHeight();
        Canvas canvas = new Canvas(cacheBitmap);
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, w, h, matrix, true);
        canvas.drawBitmap(bitmap, new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight()), new Rect(0, 0, w, h), null);
        return bitmap;
    }

    @Override
    public void onShutter() {

    }

    public interface OnPictureTakenListener {
        void onSuccess(Bitmap bitmap);

        void onFailed(String msg);
    }

    // ---------------- 录像

    private String videoPath;

    private boolean initMediaRecorder(String outputPath) {
        mMediaRecorder = new MediaRecorder();
        if (mCamera != null) {
            // 1.解锁并将相机设置daoMediaRecorder
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            // 2.设置资源
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
            // 3.设置CamcorderProfile（需要API级别8或更高版本）
            mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
            mMediaRecorder.setVideoSize(size.x, size.y);
            mMediaRecorder.setVideoEncodingBitRate(2 * size.x * size.y);
            mMediaRecorder.setOrientationHint(isFontCamera ? 270 : 90);
            // 4.设置输出文件
            mMediaRecorder.setOutputFile(outputPath);
            // 5.准备配置的MediaRecorder
            try {
                mMediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "录像配置准备失败", Toast.LENGTH_SHORT).show();
                releaseMediaRecorder();
                mCamera.lock();
                return false;
            }
            return true;
        }
        return false;
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void setRecordListener(OnStartVideoListener listener) {
        this.onStartVideoListener = listener;
    }

    public void startRecord() {
        startRecord(null);
    }

    public void startRecord(String outputPath) {
        videoPath = !TextUtils.isEmpty(outputPath) ? outputPath : FileUtil.getVideoFullPath();
        if (initMediaRecorder(videoPath)) {
            new MediaPrepareTask(onStartVideoListener).execute();
        } else {
            Toast.makeText(getContext(), "开始录制视频失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopRecord() {
        if (isRecording) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();
            if (onStartVideoListener != null) onStartVideoListener.onStop(videoPath);
        } else {
            releaseMediaRecorder();
        }
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public boolean isStartCamera() {
        return mCamera != null;
    }

    public void start() {
        if (isSurfaceAvailable) {
            openCamera();
        } else {
            _autoStart = true;
        }
    }

    public void stop() {
        releaseCamera();
    }

    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        OnStartVideoListener listener;

        MediaPrepareTask(OnStartVideoListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mMediaRecorder.start();
            isRecording = true;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (listener != null)
                listener.onStart();
        }
    }

    public interface OnStartVideoListener {
        void onStart();

        void onStop(String path);
    }


    //////////////////////内部工具类

    private static class CameraUtil {
        public static Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
            Log.e("root", screenResolution.x + ":" + screenResolution.y);
            float tmp;
            float mindiff = 100f;
            float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
            Camera.Size best = null;
            List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
            if (supportedPreviewSizes.get(0).height < supportedPreviewSizes.get(supportedPreviewSizes.size() - 1).height) {
                Collections.reverse(supportedPreviewSizes);
            }
            for (Camera.Size s : supportedPreviewSizes) {
                Log.e("size", s.width + ":" + s.height);
                tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
                if (tmp < mindiff) {
                    mindiff = tmp;
                    best = s;
                }
            }
            Log.e("select", best.width + ":" + best.height);
            return new Point(best.width, best.height);
        }
    }

    private static class FileUtil {

        private static final String AppFolderName = "appFolder";
        private static final String VideoFolderName = "Video";

        /**
         * 获取不重复的图片文件名
         */
        public static String getVideoFileName() {
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
            return dateFormat.format(date) + ".mp4";
        }

        public static String getVideoFullPath() {
            String dir = Environment.getExternalStorageDirectory() + File.separator + AppFolderName + File.separator + VideoFolderName;
            createDir(dir);
            String path = dir + File.separator + getVideoFileName();
            createFile(path);
            return path;
        }

        /**
         * 检查是否存在path目录，不存在则创建
         */
        private static String createDir(String path) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            return fileDir.getPath();
        }

        /**
         * 检查是否存在path文件，不存在则创建
         */
        private static String createFile(String path) {
            File fileDir = new File(path);
            if (!fileDir.exists()) {
                try {
                    fileDir.createNewFile();
                    return fileDir.getPath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "";
        }
    }
}