/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author zerd
 */
public class main {

    public static void main(String[] args) throws IOException {
        String folder_path = "src\\data\\";
        File folder = new File(folder_path);
        File[] listOfFiles = folder.listFiles();
        readCSV data = new readCSV();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = folder_path + listOfFiles[i].getName();
                String[] filenames = filename.split(".csv")[0].split(" ");
                double tahun = Double.parseDouble(filenames[2]) - Double.parseDouble(filenames[0].split("20")[1]);
                String semester = filenames[1];
                if (semester.equalsIgnoreCase("ganjil")) {
                    tahun += 0.5;
                } else {
                    tahun += 1;
                }
                data.read(filename, tahun);
            }
        }
        
//        List<String> keys = new ArrayList<String>();
//        keys.addAll(data.data_mahasiswa.keySet());
//        int x = 0;
//        for (int i = 0; i < keys.size(); i++) {
//            List<mahasiswa> datas = data.data_mahasiswa.get(keys.get(i));
//            int y = 0;
//            for (int j = 0; j < datas.size(); j++) {
//                x++;
//                y++;
//            }
//            
//            System.out.println("==>" + keys.get(i) + " = " + y);
//        }
//        
//        System.out.println("TOTAL : "+x);
//        System.out.println("---------------------------------------------------");


//        UNCOMMENT BUAT JALANIN CORRELATION
//        correlation correlation = new correlation(data.getAllMahasiswa(), data.data_nilai);
//        correlation.calculate();
        
//        System.out.println("Mahasiswa : " + data.getAllMahasiswa().size());
//        System.out.println("====> "+correlation.correlation_data.get("Pemrograman Berorientasi Objek").get("Arsitektur & Organisasi Komputer"));

        Scanner sc = new Scanner(System.in);
        TreeMap<String, String> predict_data = new TreeMap<>();
        predict_data.put("Etika", "A");
        predict_data.put("Pendidikan Pancasila", "B");
        predict_data.put("Pemrograman Berorientasi Objek", "A");
        predict_data.put("Arsitektur & Organisasi Komputer", "A");
        
        System.out.println(predict_data.keySet());
        System.out.println(predict_data.values());
        
        NaiveBayes nb = new NaiveBayes(data.data_mahasiswa);
        nb.predict(predict_data);
    }
    
    public static int nilaiToInt(String nilai) {
        if (nilai.compareToIgnoreCase("A") == 0) {
            return 4;
        } else if (nilai.compareToIgnoreCase("B") == 0) {
            return 3;
        } else if (nilai.compareToIgnoreCase("C") == 0) {
            return 2;
        } else if (nilai.compareToIgnoreCase("D") == 0) {
            return 1;
        } else {
            return 0;
        }
    }
}