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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author zerd
 */
public class main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("(1) Pengujian \n(2) Prediksi \nPilih Mode : ");
        int mode = sc.nextInt();
        int n_partition = 1;
        if (mode == 1) {
            System.out.println("Masukkan jumlah partisi : ");
            n_partition = sc.nextInt();
        }
        String folder_path = "src\\AllData\\";
//        String folder_path = "src\\DataTesting\\";
        File folder = new File(folder_path);
        File[] listOfFiles = folder.listFiles();
        readCSV data = new readCSV(mode, n_partition);
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
                data.read(filename, tahun, filename);
            }
        }

        System.out.println("JUMLAH MAHASISWA : " + data.jumlahMahasiswa);

        String[] filter = new String[]{"Algoritma & Struktur Data", "Pemrograman Berorientasi Objek", "Manajemen Informasi & Basis Data", "Desain & Analisis Algoritma"};

        if (mode == 1) {
            int n = 1;
            List<TreeMap<String, List<mahasiswa>>> partition = data.partition;
            for (int i = 0; i < n_partition; i++) {
                List<mahasiswa> data_test = new ArrayList<>();
                TreeMap<String, List<mahasiswa>> data_train = defineDataTrain();

                System.out.println("PARTITION " + (i + 1));
                int n_data_test = 0;
                int n_data_train = 0;
                for (int j = 0; j < n_partition; j++) {
                    TreeMap<String, List<mahasiswa>> curr_partition = partition.get(j);

                    if (i == j) {
                        for (Map.Entry<String, List<mahasiswa>> entry : curr_partition.entrySet()) {
                            data_test.addAll(entry.getValue());
                        }
                    } else {
                        curr_partition.entrySet().forEach((entry) -> {
                            String key = entry.getKey();
                            List<mahasiswa> vals = entry.getValue();
                            List<mahasiswa> recent = data_train.get(key);
                            recent.addAll(vals);
                            data_train.replace(key, recent);
                        });
                    }

                }

                for (Map.Entry<String, List<mahasiswa>> entry : data_train.entrySet()) {
                    String key = entry.getKey();
                    List<mahasiswa> value = entry.getValue();
                    n_data_train += value.size();
                }
                System.out.println("Total test = " + data_test.size());
                System.out.println("Total train = " + n_data_train);
                for (Iterator<mahasiswa> iterator = data_test.iterator(); iterator.hasNext();) {
                    mahasiswa m = iterator.next();
                    System.out.println("Mahasiswa ke-" + n);
                    System.out.println("info = " + m.info);
                    TreeMap<String, String> nilai = filterNilai(m.nilai, filter);
                    NaiveBayes nb = new NaiveBayes(data_train, n_data_train);
                    nb.predict(nilai);
                    n++;
                }
            }
        } else {
            TreeMap<String, String> predict_data = new TreeMap<>();
            predict_data.put("Algoritma & Struktur Data", "B");
            predict_data.put("Pemrograman Berorientasi Objek", "B");
            predict_data.put("Manajemen Informasi & Basis Data", "C");
            predict_data.put("Desain & Analisis Algoritma", "C");

            System.out.println(predict_data.keySet());
            System.out.println(predict_data.values());

            NaiveBayes nb = new NaiveBayes(data.data_mahasiswa, data.jumlahMahasiswa);
            nb.predict(predict_data);
        }
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

    public static TreeMap<String, List<mahasiswa>> defineDataTrain() {
        TreeMap<String, List<mahasiswa>> blank_data = new TreeMap<>();
        blank_data.put("3.5", new ArrayList<>());
        blank_data.put("4.0", new ArrayList<>());
        blank_data.put("4.5", new ArrayList<>());
        blank_data.put("5.0", new ArrayList<>());
        blank_data.put("5.5", new ArrayList<>());
        blank_data.put("6.0", new ArrayList<>());
        blank_data.put("6.5", new ArrayList<>());
        blank_data.put("7.0", new ArrayList<>());
        return blank_data;
    }

    private static TreeMap<String, String> filterNilai(TreeMap<String, String> nilai, String filter[]) {
        TreeMap<String, String> res = new TreeMap<>();
        for (int i = 0; i < filter.length; i++) {
            res.put(filter[i], nilai.get(filter[i]));
        }
        return res;
    }
}
