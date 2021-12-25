package com.vinikuria.the20first;

import java.io.File;
import java.util.ArrayList;

public class Filefinder {
    static ArrayList<File> fileList=new ArrayList<>();
    static String prefileType="";

    public static ArrayList<File> loadFile(File storageDirectory,String fileType){
        //for clearing the file list to avoid adding another type of file from the previous one
        if (!prefileType.equals(fileType)){
            fileList.clear();
        }
        prefileType=fileType;

        File[] directoryFiles=storageDirectory.listFiles();

        if (!(directoryFiles ==null) && directoryFiles.length>0){

            for (int i=0;i<directoryFiles.length;i++){

                if (directoryFiles[i].isDirectory()){

                    loadFile(directoryFiles[i],fileType);

                }else{
                    String fileName=directoryFiles[i].getName().toLowerCase();
                    switch (fileType) {
                        case "PHOTO":
                            for (String extension : StoreLoadedData.photoExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "DOCUMENT":
                            for (String extension : StoreLoadedData.documentsExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "AUDIO":
                            for (String extension : StoreLoadedData.audioExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                        case "VIDEO":
                            for (String extension : StoreLoadedData.videoExtensions) {
                                if (fileName.endsWith(extension)) {
                                    fileList.add(directoryFiles[i]);
                                }
                            }

                            break;
                    }


                }
            }
        }
        return fileList;
    }
}
