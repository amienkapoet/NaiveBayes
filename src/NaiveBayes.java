
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ZerD
 */
public class NaiveBayes {

    private TreeMap<String, List<mahasiswa>> data_mahasiswa;
    private TreeMap<String, TreeMap<String, Double>> probs;
    private double jumlahMahasiswa;

    public NaiveBayes(TreeMap<String, List<mahasiswa>> data_mahasiswa, double jumlahMahasiswa) {
        this.data_mahasiswa = data_mahasiswa;
        this.jumlahMahasiswa = jumlahMahasiswa;
    }

    public void predict(TreeMap<String, String> predict_data) {
        calcProbs(predict_data);
//        printProbs();
        doPredict();
        printPredict();
    }

    private void calcProbs(TreeMap<String, String> predict_data) {
        Set<String> set_datas_key = predict_data.keySet();
        String[] data_matkul = set_datas_key.toArray(new String[set_datas_key.size()]);
        probs = new TreeMap<>();
        System.out.println("NILAI = ");
        for (int i = 0; i < data_matkul.length; i++) {
            String matkul = data_matkul[i];
            String nilai = predict_data.get(matkul);
            System.out.println("- "+matkul+" : "+nilai);
            TreeMap<String, Double> vals = calcProbMatkulYear(matkul, nilai);
            probs.put(matkul, vals);
//            break;
        }
    }

    private void printProbs() {
        for (Map.Entry<String, TreeMap<String, Double>> entry : probs.entrySet()) {
            String key = entry.getKey();
            TreeMap<String, Double> value = entry.getValue();
            System.out.println("=> " + key);
            System.out.println("Tahun : " + value.keySet());
            System.out.println("VALS : " + value.values());
            System.out.println("_________________________________________");
        }
    }

    private void printPredict() {
        String graduate = "-";
        double max = 0;
        for (Map.Entry<String, Double> entry : predict.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            if (value > max) {
                max = value;
                graduate = key;
            }
        }
        System.out.println("GRADUATE IN : " + graduate + " years.");
        System.out.printf("Predict Value : %.20f \n", max);
        System.out.println("_____________________________________________________________________________");
    }

    TreeMap<String, Double> predict = new TreeMap<>();

    private TreeMap<String, Double> calcProbMatkulYear(String matkul, String nilai) {
        TreeMap<String, Double> res = new TreeMap<>();
        Set<String> set_datas_key = data_mahasiswa.keySet();
        String[] tahun = set_datas_key.toArray(new String[set_datas_key.size()]);
        for (int i = 0; i < tahun.length; i++) {
            List<mahasiswa> list_mahasiswa = data_mahasiswa.get(tahun[i]);
            double total_mahasiswa = 0;
            double total_mahasiswa_with_same_val = 0;
            for (int j = 0; j < list_mahasiswa.size(); j++) {
                mahasiswa curr_mahasiswa = list_mahasiswa.get(j);
                if (curr_mahasiswa.hasMatkul(matkul)) {
                    total_mahasiswa++;
                    if (curr_mahasiswa.getNilaiString(matkul).compareToIgnoreCase(nilai) == 0) {
                        total_mahasiswa_with_same_val++;
                    }
                }
            }
            double val = total_mahasiswa_with_same_val / total_mahasiswa;
            if (0 == total_mahasiswa) {
                val = 0;
            }
            res.put(tahun[i], val);
        }
        return res;
    }

    private void doPredict() {
        for (Map.Entry<String, TreeMap<String, Double>> entry : probs.entrySet()) {
            String matkul = entry.getKey();
            TreeMap<String, Double> values = entry.getValue();
            for (Map.Entry<String, Double> entry1 : values.entrySet()) {
                String year = entry1.getKey();
                Double value = entry1.getValue();
                if (predict.containsKey(year)) {
                    Double curr_val = predict.get(year);
                    predict.replace(year, value * curr_val);
                } else {
                    predict.put(year, value * (data_mahasiswa.get("" + year).size() / jumlahMahasiswa));
                }
            }
        }
//        System.out.println("TAHUN : " + predict.keySet());
//        System.out.println("VALUE : " + predict.values());
    }
}
