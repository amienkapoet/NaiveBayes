/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author zerd
 */
public class readCSV {

    TreeMap<String, List<mahasiswa>> data_mahasiswa = new TreeMap<>();
    TreeMap<String, List<Integer>> data_nilai = new TreeMap<>();
    TreeMap semester = new TreeMap<String, String>();
    int jumlahMahasiswa = 0;

    public void read(String file_path, double tahun) throws FileNotFoundException, IOException {
        String path = file_path;
        List<mahasiswa> data_mahasiswa_temp = new ArrayList<>();
        List<String> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
            }
        }
        
        for (int i = 0; i < records.size(); i++) {
            String[] data = records.get(i).split("\t");
//            System.out.println("===> "+data[0]+" / "+data.length);
            if (data.length == 1){
                if(data[0].contains("+")){
                    mahasiswa m = new mahasiswa(tahun, semester);
                    jumlahMahasiswa++;
                    data_mahasiswa_temp.add(m);
                    semester = new TreeMap<String, String>();
                }
            }
            if (data.length == 4) { // semester
                String data_split[] = data[0].split(" ");
                
                if (data_split[0].contains("SEMESTER 1") || data_split[0].contains("+") ) {
                    mahasiswa m = new mahasiswa(tahun, semester);
                    data_mahasiswa_temp.add(m);
                    jumlahMahasiswa++;
                    semester = new TreeMap<String, String>();
                } else {
                    String matkul = data[1];
                    String nilai = data[2];
                    insertData(matkul, nilai);

                }
            } else if (data.length == 8) { // full data
                String matkul = data[1];
                String nilai = data[2];
                insertData(matkul, nilai);
                matkul = data[5];
                nilai = data[6];
                insertData(matkul, nilai);
            }
        }

        List<mahasiswa> recent = data_mahasiswa.get("" + tahun);
        if (recent == null) {
//            System.out.println("PRESENT : "+tahun);
            recent = data_mahasiswa_temp;
            data_mahasiswa.put("" + tahun, recent);
        } else {
//            System.out.println("EMPTY : "+tahun);
            recent.addAll(data_mahasiswa_temp);
            data_mahasiswa.put("" + tahun, recent);
        }
    }

    private void insertData(String matkul, String nilai) {
        if (matkul.compareToIgnoreCase("") != 0) {
            if (!matkul.contains("[X]")) {
                matkul = matkul.split("<<")[0].trim();
                nilai = nilai.split("-")[0].trim();

                if (semester.get(matkul) != null) {
                    semester.replace(matkul, nilai);
                } else {
                    semester.put(matkul.trim(), nilai);
                }
                if (data_nilai.get(matkul) == null) {
                    List<Integer> nilai_list = new ArrayList<>();
                    nilai_list.add(nilaiToInt(nilai));
                    data_nilai.put(matkul, nilai_list);
                } else {
                    List<Integer> nilai_list = data_nilai.get(matkul);
                    nilai_list.add(nilaiToInt(nilai));
                    data_nilai.replace(matkul, nilai_list);
                }
            }
        }
    }

    public int nilaiToInt(String nilai) {
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

    public List<mahasiswa> getAllMahasiswa() {
        List<mahasiswa> all_mahasiswa = new ArrayList<>();
        Collection<List<mahasiswa>> values = data_mahasiswa.values();
        for (Iterator<List<mahasiswa>> iterator = values.iterator(); iterator.hasNext();) {
            List<mahasiswa> next = iterator.next();
            all_mahasiswa.addAll(next);
        }
        return all_mahasiswa;
    }
}
