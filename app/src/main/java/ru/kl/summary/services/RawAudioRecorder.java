package ru.kl.summary.services;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.view.View;
import android.widget.TextView;
import ru.kl.summary.MyApp;

public class RawAudioRecorder {

    AudioRecord audioRecord;
    Boolean isRecording = true;
    private int sampleRate = 16000;
    private int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    //int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
    int BytesPerElement = 2; // 2 bytes in 16bit format
    byte data[] = new byte[BufferElements2Rec * BytesPerElement];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private String currentAudioFileName = "";
    private String currentAudioFilePath = "";
    AudioManager audioManager;
    Thread rawAudioRecordThread = null;

    public void setSpeakerOn() {
        audioManager = (AudioManager) MyApp.getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(true);
    }

    public void startRecorder(TextView infoTextView) {
        isRecording = true;
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, BufferElements2Rec * BytesPerElement);
        currentAudioFilePath = this.getFilename();
        infoTextView.append("Start recording\n");
        infoTextView.append(BufferElements2Rec * BytesPerElement+"\n");
        rawAudioRecordThread = new Thread(new Runnable() {

            @Override
            public void run() {
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(currentAudioFilePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    infoTextView.append(e.getMessage());
                }
                while (isRecording) {
                    audioRecord.read(data, 0, data.length);
                    try {
                        os.write(data, 0, BufferElements2Rec * BytesPerElement);
                    } catch (IOException e) {
                        e.printStackTrace();
                        infoTextView.append(e.getMessage());
                    }
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    infoTextView.append(e.getMessage());
                }
            }
        });
        rawAudioRecordThread.start();
    }

    public void stopRecorder() {
        isRecording = false;
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            rawAudioRecordThread = null;
        }
    }

    public String getFilename() {
        String filepath = MyApp.getContext().getExternalFilesDir(null).getPath();// /.../internal/
        File dir = new File(filepath, AUDIO_RECORDER_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return (dir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".pcm");
    }

    public String getCurrentAudioFileName() {
        String[] splittedPath = currentAudioFilePath.split("/");
        currentAudioFileName = splittedPath[splittedPath.length - 1];
        return currentAudioFileName;
    }

    public String getCurrentAudioFilePath() {
        return currentAudioFilePath;
    }

}