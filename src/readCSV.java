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
import java.util.Collection;
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
    List<TreeMap<String, List<mahasiswa>>> partition;
    int jumlahMahasiswa = 0;
    int n_partition = 0;
    int mode = 1;

    public readCSV(int mode, int n_partition) {
        this.mode = mode;
        if (this.mode == 1) {
            this.n_partition = n_partition;
            definePartition();
        }
    }

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
            if (data.length == 1) {
                if (data[0].contains("+")) {
                    mahasiswa m = new mahasiswa(tahun, semester);
                    jumlahMahasiswa++;
                    data_mahasiswa_temp.add(m);
                    semester = new TreeMap<String, String>();
                }
            }
            if (data.length == 4) { // semester
                String data_split[] = data[0].split(" ");

                if (data_split[0].contains("SEMESTER 1") || data_split[0].contains("+")) {
                    mahasiswa m = new mahasiswa(tahun, semester);
                    data_mahasiswa_temp.add(m);
                    jumlahMahasiswa++;
                    semester = new TreeMap<String, String>();
                } else {
                    String matkul = data[1];
                    String nilai = data[2];
                    insertData(matkul, nilai);

                }
            } else if (data.length == 8) {
                String matkul = data[1];
                String nilai = data[2];
                insertData(matkul, nilai);
                matkul = data[5];
                nilai = data[6];
                insertData(matkul, nilai);
            }
        }

        if (mode == 1) {
            spliting(tahun, data_mahasiswa_temp);
        } else {
            List<mahasiswa> recent = data_mahasiswa.get("" + tahun);
            if (recent == null) {
                recent = data_mahasiswa_temp;
                data_mahasiswa.put("" + tahun, recent);
            } else {
                recent.addAll(data_mahasiswa_temp);
                data_mahasiswa.put("" + tahun, recent);
            }
        }

    }

    private void spliting(double tahun, List<mahasiswa> new_list_mahasiswa) {
        int biggest = 1;
//        System.out.println("TAHUN = " + tahun);
        for (int i = 0; i < new_list_mahasiswa.size(); i++) {
            mahasiswa curr_mahasiswa = new_list_mahasiswa.get(i);
            boolean stat = true;
            while (stat) {
                for (int j = 0; j < this.n_partition; j++) {
                    TreeMap<String, List<mahasiswa>> list_part = partition.get(j);
//                    System.out.println("START Partition[" + j + "] --> " + list_part.values());
                    List<mahasiswa> recent = list_part.get("" + tahun);
                    if (recent.size() < biggest) {
                        list_part.get(tahun + "").add(curr_mahasiswa);
//                        System.out.println("END Partition[" + j + "] --> " + partition.get(j).values());
                        stat = false;
                        break;
                    }

                    if (j + 1 == n_partition) {
                        biggest += 1;
                    }
                }
            }
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
        values.forEach((next) -> {
            all_mahasiswa.addAll(next);
        });
        return all_mahasiswa;
    }

    private void definePartition() {
        partition = new ArrayList<>();
        for (int i = 0; i < this.n_partition; i++) {
            TreeMap<String, List<mahasiswa>> blank_data = new TreeMap<>();
            blank_data.put("3.5", new ArrayList<>());
            blank_data.put("4.0", new ArrayList<>());
            blank_data.put("4.5", new ArrayList<>());
            blank_data.put("5.0", new ArrayList<>());
            blank_data.put("5.5", new ArrayList<>());
            blank_data.put("6.0", new ArrayList<>());
            blank_data.put("6.5", new ArrayList<>());
            blank_data.put("7.0", new ArrayList<>());
            partition.add(blank_data);
        }
    }
}
