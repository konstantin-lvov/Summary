package ru.kl.summary.services;

import ru.kl.summary.MyApp;

public class FileSystemReader {

    public void printListOfFiles(){
        String [] ls = MyApp.getContext().getFilesDir().list();
        System.out.println("list of files " + ls.length);
        for (int i = 0; i < ls.length; i++){
            System.out.println(ls [i]);
        }
    }
}
