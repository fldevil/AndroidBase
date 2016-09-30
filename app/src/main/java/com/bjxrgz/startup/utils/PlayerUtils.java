package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * author cipherGG
 * Created by Administrator on 2016/5/25.
 * describe 多媒体类
 */
public class PlayerUtils {

    public static boolean isStop = true;
    public static boolean isPlay = false;

    /**
     * Idle状态
     */
    public static MediaPlayer getMediaPlayer() {

        return new MediaPlayer();
    }

    /**
     * 直接返回init状态的MediaPlayer
     */
    public static MediaPlayer getMediaPlayer(Context context, int resID) {
        return MediaPlayer.create(context, resID);
    }

    /**
     * init状态
     */
    public static void setData(MediaPlayer player, String path) {
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放/暂停
     */
    public static void toggle(MediaPlayer player) {
        if (!isPlay) { // 没有播放
            play(player);
        } else { // 正在播放
            stop(player);
        }
    }

    private static void play(MediaPlayer player) {
        if (isStop) {
            prepare(player); // 那就去准备，并播放
        } else {
            player.start();
            isPlay = true;
        }
    }

    /**
     * 停止状态,需prepare再start
     */
    public static void stop(MediaPlayer player) {
        player.stop();
//        player.reset();
        isStop = true;
        isPlay = false;
    }

    /**
     * 停止状态,需prepare再start
     */
    public static void destroy(MediaPlayer player) {
        player.stop();
        player.release();
        player = null;
        isStop = true;
        isPlay = false;
    }

    /**
     * 准备状态,只有在接口中知道什么时候准备好
     */
    public static void prepare(final MediaPlayer player) {

        // 准备之后直接开始
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
                isStop = false;
                isPlay = true;
            }
        });
        // 结束,恢复init状态
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isPlay = false;
                isStop = false;
            }
        });
        // 错误,恢复init状态
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true; // return false;
            }
        });
        // 定位完成,没设置直接开始
        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
            }
        });

        player.prepareAsync();
    }

    private static MediaRecorder mRecorder;

    /**
     * 开始录音
     */
    public static void startRecordSound(String outFilePath) {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        mRecorder.reset();
        // 文件输出路径
        mRecorder.setOutputFile(outFilePath);
        // 音频为麦克风
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 音频格式  THREE_GPP????
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        // 音频编码
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Camera mCamera;

    /**
     * 初始化录视频
     */
    public static void initRecordVideo(Activity activity, SurfaceView surfaceView) {
        final int width = surfaceView.getWidth();
        final int height = surfaceView.getHeight();
        SurfaceHolder holder = surfaceView.getHolder();
        holder.setKeepScreenOn(true);

//        camera.lock();
//        camera.setDisplayOrientation(90);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    mCamera.setPreviewDisplay(holder);
//                    mCamera.startPreview();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                initCamera(holder, width, height);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }
        });
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private static void initCamera(SurfaceHolder holder, int width, int height) {
        if (mCamera != null) {
            releaseCamera();
        }

        try {
            // 创建摄像头对象 0是后置摄像头，1是前置摄像头
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            mCamera.lock();
            mCamera.setDisplayOrientation(90); // 预览方向

        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }

        // 设置参数
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        if (previewSizes != null) {
            Camera.Size previewSize = getOptimalPreviewSize(previewSizes, Math.max(width, height), Math.min(width, height));
            params.setPreviewSize(previewSize.width, previewSize.height);
        }
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        // 设置为竖屏
        params.set("orientation", "portrait");
        mCamera.setParameters(params);
        // 开始初始化
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取与surfaceView相差最小的预览画面
     */
    private static Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) {
            return null;
        }
        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;
        int targetHeight = h;
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    private static void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始录视频
     */
    public static void startRecordVideo(String outFilePath, SurfaceView surfaceView) {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            // 防止因录制视频太短崩溃
            mRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    try {
                        if (mr != null)
                            mr.reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            mRecorder.reset();
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.unlock();
            mRecorder.setCamera(mCamera);
        }
        // 文件输出路径
        mRecorder.setOutputFile(outFilePath);
        // 音频源为麦克风
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 视频源为Camera(相机)
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 视频输出格式mp4
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 音频录制格式
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 视频录制格式
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        // 获取相机的宽高
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
        mRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
        mRecorder.setVideoEncodingBitRate(profile.videoBitRate);
        // 最大时长
        mRecorder.setMaxDuration(30 * 1000);
        // 录制角度
        mRecorder.setOrientationHint(90);
        // 显示的位置
        mRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制
     */
    public static void stopRecord() {
        if (mRecorder != null) {
            // 防止因录制视频太短崩溃
            mRecorder.setOnInfoListener(null);
            mRecorder.setOnErrorListener(null);
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
//        releaseCamera();
    }

    public static void releaseRecordVideo() {
        releaseCamera();
    }

}
