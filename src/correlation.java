/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author zerd
 */
public class correlation {

    TreeMap<String, List<Integer>> data = new TreeMap<>();
    List<mahasiswa> data_mahasiswa = new ArrayList<>();
    TreeMap<String, TreeMap<String, Double>> correlation_data = new TreeMap<>();

    public correlation(List<mahasiswa> data_mahasiswa, TreeMap data) {
        this.data_mahasiswa = data_mahasiswa;
        this.data = data;
    }

    public void calculate() {
        List<String> keys = new ArrayList<String>();
        keys.addAll(data.keySet());
        for (int i = 0; i < keys.size(); i++) {
            String head = keys.get(i);
            TreeMap<String, Double> temp = new TreeMap<>();
            for (int j = 0; j < keys.size(); j++) {
                String current_matkul = keys.get(j);
                List<List<Double>> data;
                if (i != j) {
                    data = getAllNilai(head, current_matkul);
                } else {
                    data = getAllNilaiTahun(head);
                    current_matkul = "Year";
                }

                List<Double> x = data.get(0);
                List<Double> y = data.get(1);

                double correlation_value = calculate_correlation(x, y);

                if (correlation_value >= -1) {
                    temp.put(current_matkul, correlation_value);
                }
            }
            correlation_data.put(head, temp);
        }
        printCorrelation();
    }

    public void printCorrelation() {
        List<String> keys = new ArrayList<String>();
        keys.addAll(correlation_data.keySet());
        for (int i = 0; i < keys.size(); i++) {
            TreeMap<String, Double> values = correlation_data.get(keys.get(i));
            System.out.println(keys.get(i) + " : ");
            List<String> keys_val = new ArrayList<String>();
            keys_val.addAll(values.keySet());
//            System.out.println("KEYS : "+keys_val);
            for (int j = 0; j < keys_val.size(); j++) {
                // Kalau tidak ada korelasi , maka hasilnya null
                Double val = values.get(keys_val.get(j));
                if (val != null) {
                    System.out.println("- " + keys_val.get(j) + " -----------> " + val);
                }
            }
            System.out.println("__________________________________________________");
        }
    }

    public List<List<Double>> getAllNilai(String matkul1, String matkul2) {
        List<Double> data_nilai1 = new ArrayList<>();
        List<Double> data_nilai2 = new ArrayList<>();
        for (int i = 0; i < data_mahasiswa.size(); i++) {
            double nilai1 = data_mahasiswa.get(i).getNilai(matkul1);
            double nilai2 = data_mahasiswa.get(i).getNilai(matkul2);
            if ((nilai1 > -1) && (nilai2 > -1)) {
                data_nilai1.add(nilai1);
                data_nilai2.add(nilai2);
            }
        }
        List<List<Double>> result = new ArrayList<>();
        result.add(data_nilai1);
        result.add(data_nilai2);
        return result;
    }

    public List<List<Double>> getAllNilaiTahun(String matkul1) {
        List<Double> data_nilai = new ArrayList<>();
        List<Double> data_tahun = new ArrayList<>();
        for (int i = 0; i < data_mahasiswa.size(); i++) {
            double nilai = (double) data_mahasiswa.get(i).getNilai(matkul1);
            double tahun = data_mahasiswa.get(i).getLulus();
            data_nilai.add(nilai);
            data_tahun.add(tahun);
        }
        List<List<Double>> result = new ArrayList<>();
        result.add(data_nilai);
        result.add(data_tahun);
        return result;
    }

    public double calculate_correlation(List<Double> x, List<Double> y) {
        double sum_x = 0;
        double sum_y = 0;
        double sum_x_pow = 0;
        double sum_y_pow = 0;
        double sum_xy = 0;
        for (int k = 0; k < x.size(); k++) {
            sum_x += (double) x.get(k);
            sum_y += (double) y.get(k);
            sum_x_pow += Math.pow((double) x.get(k), 2);
            sum_y_pow += Math.pow((double) y.get(k), 2);
            sum_xy += ((double) x.get(k) * (double) y.get(k));
        }
        double correlation = (x.size() * sum_xy) - (sum_x * sum_y);
        double divider = Math.sqrt((x.size() * sum_x_pow) - Math.pow(sum_x, 2));
        divider *= Math.sqrt((x.size() * sum_y_pow) - Math.pow(sum_y, 2));
        correlation = correlation / divider;
        if (divider != 0) {
            return correlation;
        } else {
            return -2;
        }
    }
}