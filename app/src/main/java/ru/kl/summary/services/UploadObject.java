package ru.kl.summary.services;

import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.common.collect.Lists;
import ru.kl.summary.MyApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import android.content.res.Resources;

public class UploadObject {

    private static Storage storage;
    private static Thread t;
    private static String CREDENTIALS_JSON = "silver-aurora-294418-ae39bc3e8abe.json";
    private static String GOOGLE_API_LINK = "https://www.googleapis.com/auth/cloud-platform";

    public static void uploadObject(
            String projectId, String bucketName, String objectName, String filePath) throws IOException {
        /*
        Storage setup
         */
        ;
        GoogleCredentials credentials = GoogleCredentials.fromStream(MyApp.getContext().getAssets().open(CREDENTIALS_JSON))
                .createScoped(Lists.newArrayList(GOOGLE_API_LINK));
        storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

        /*
        Upload audio
         */
        t = new Thread() {
            public void run() {
                try {
                    storage.create(blobInfo, Files.readAllBytes(Paths.get(filePath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (t != null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e){
                    }
                }
            }
        };
        t.start();
        System.out.println(
                "File " + filePath + " uploaded to bucket " + bucketName + " as " + objectName);
    }

    public static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public void printSomeStaff() {
        System.out.println("LIST OF DIRECTORIES (UPLOAD CLASS): ");
        File directory = new File(MyApp.getContext().getFilesDir().toString() + "/AudioRecorder");
        String[] directories = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(directories));

        System.out.println("LIST OF FILES (UPLOAD CLASS): ");
        Set<String> listOfFiles = listFilesUsingJavaIO(directory.toString());
        listOfFiles.stream().forEach(x -> System.out.println(x));

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }

    public void stopThread(){
        t = null;
    }
}