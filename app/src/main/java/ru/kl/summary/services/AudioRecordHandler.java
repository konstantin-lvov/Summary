package ru.kl.summary.services;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import ru.kl.summary.MyApp;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AudioRecordHandler {
    public static String getErrorResult() {
        return errorResult;
    }

    private static String errorResult = "";

    //Call Recording varibales
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private static final String AUDIO_RECORDER_FILE_EXT_AWB = ".awb";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    private MediaRecorder recorder = null;
    private int currentFormat = 0;
    private int output_formats[] = {MediaRecorder.OutputFormat.AMR_WB, MediaRecorder.OutputFormat.MPEG_4,
            MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_AWB, AUDIO_RECORDER_FILE_EXT_MP4,
            AUDIO_RECORDER_FILE_EXT_3GP};
    private int encoder = MediaRecorder.AudioEncoder.AMR_WB;
    private int sampleRate = 16000;
    private String CurrentAudioFileName = "";
    private String CurrentAudioFilePath = "";

    AudioManager audioManager;

    public String getCurrentAudioFileName() {
        String[] splittedPath = CurrentAudioFilePath.split("/");
        CurrentAudioFileName = splittedPath[splittedPath.length - 1];
        return CurrentAudioFileName;
    }

    public String getCurrentAudioFilePath() {
        return CurrentAudioFilePath;
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            Toast.makeText(MyApp.getContext(),
                    "Error: " + what + ", " + extra, Toast.LENGTH_SHORT).show();
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            Toast.makeText(MyApp.getContext(),
                            "Warning: " + what + ", " + extra, Toast.LENGTH_SHORT)
                    .show();
        }
    };

    public String getFilename() {
        String filepath = MyApp.getContext().getFilesDir().getPath();
        File dir = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return (dir.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    public void setSpeakerOn() {
        audioManager = (AudioManager) MyApp.getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(true);
    }

    public void startRecording() {
        CurrentAudioFilePath = getFilename();
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        //recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(encoder);
        recorder.setAudioSamplingRate(sampleRate);
        recorder.setOutputFile(CurrentAudioFilePath);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);
        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            errorResult = e.getMessage();
            Log.e("REDORDING :: ", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errorResult = e.getMessage();
            Log.e("REDORDING :: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        audioManager.setSpeakerphoneOn(false);
        try {
            if (null != recorder) {
                recorder.stop();
                recorder.reset();
                recorder.release();

                recorder = null;
            }
        } catch (RuntimeException stopException) {
        }
    }


}
