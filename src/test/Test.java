package test;

import java.io.File;

public class Test {
    public static void main(String[] args) {
        File file = new File("D:\\img\\");
        searchFile(file);
    }
    public static void searchFile(File dir){//dir 目录
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file);
                } else {
                    searchFile(file);
                }
            }
        }
    }
}
