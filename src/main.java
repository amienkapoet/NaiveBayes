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
                data.read(filename, tahun);
            }
        }
        
        String[]  filter = new String[]{"Algoritma & Struktur Data", "Pemrograman Berorientasi Objek", "Manajemen Informasi & Basis Data", "Desain & Analisis Algoritma"};
        
        if (mode == 1) {
            for (int i = 0; i < n_partition; i++) {
                System.out.println("PARTITION " + (i + 1));
                data.partition.get(i).entrySet().forEach(entry -> {
                    System.out.println(entry.getKey() + " : " + entry.getValue().size());
                });
                System.out.println("_______________________________________");
            }
            for (int i = 0; i < data.partition.size(); i++) {
                List<mahasiswa> data_test = new ArrayList<>();
                TreeMap<String, List<mahasiswa>> data_train = new TreeMap<>();
                int jumlahMahasiswa = 0;
                for (int j = 0; j < data.partition.size(); j++) {
                    if (i == j) {
                        List<List<mahasiswa>> vals = new ArrayList(data.partition.get(j).values());
                        vals.forEach((next) -> {
                            for (Iterator<mahasiswa> iterator1 = next.iterator(); iterator1.hasNext();) {
                                data_test.add(iterator1.next());
                            }
                        });
                    } else {
                        TreeMap<String, List<mahasiswa>> vals = data.partition.get(j);
                        for (Map.Entry<String, List<mahasiswa>> entry : vals.entrySet()) {
                            String key = entry.getKey();
                            List<mahasiswa> value = entry.getValue();
                            jumlahMahasiswa += value.size();
                            if (!value.isEmpty()) {
                                List<mahasiswa> recent = data_train.get(key);
                                if (recent == null) {
                                    recent = value;
                                } else {
                                    recent.addAll(value);
                                }
                                data_train.put(key, recent);
                            }
                        }
                    }

                }
//                System.out.println("DATA TEST Baris ke-" + (i + 1) + ": " + data_test.size());
//                System.out.println("Data mahasiswa : ");
//                data_mahasiswa.entrySet().forEach(entry -> {
//                    System.out.println(entry.getKey() + " : " + entry.getValue().size());
//                });
//                System.out.println("___________________________________________________");
                for (int j = 0; j < data_test.size(); j++) {
                    mahasiswa m = data_test.get(j);
                    TreeMap<String, String> nilai = filterNilai(m.nilai, filter);
                    NaiveBayes nb = new NaiveBayes(data_train, jumlahMahasiswa);
                    nb.predict(nilai);
                    break;
                }
                break;
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

    private static TreeMap<String, String> filterNilai(TreeMap<String, String> nilai, String filter[]) {
        TreeMap<String, String> res = new TreeMap<>();
        for (int i = 0; i < filter.length; i++) {
            res.put(filter[i], nilai.get(filter[i]));
        }
        return res;
    }
}
