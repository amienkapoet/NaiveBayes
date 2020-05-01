
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
    
    private TreeMap<String, List<mahasiswa>> data_mahasiswa = new TreeMap<>();
    
    public NaiveBayes(TreeMap<String, List<mahasiswa>> data_mahasiswa){
        this.data_mahasiswa = data_mahasiswa;
    }
    
    public void predict(TreeMap<String, String> predict_data){
        probsPerYear(predict_data);
    }
    
    private void probsPerYear(TreeMap<String, String> predict_data){
        // Ambil semua matkul dari mahasiswa yang akan dipredict
        Set<String> set_datas_key = predict_data.keySet();
        String[] data_matkul = set_datas_key.toArray(new String[set_datas_key.size()]);
        // new_data => key: matkul, value : HASHMAP
        // hashmap => String tahun (3.5, 4, ... 7) , Double value => nilai
        TreeMap<String, TreeMap<String, Double>> new_data = new TreeMap<>();
        for (int i = 0; i < data_matkul.length; i++) {
            String matkul = data_matkul[i];
            String nilai = predict_data.get(matkul);
            TreeMap<String, Double> vals = calcPerMatkulPerYear(matkul, nilai);
            new_data.put(matkul, vals);
        }
        for (Map.Entry<String, TreeMap<String, Double>> entry : new_data.entrySet()) {
            String key = entry.getKey();
            TreeMap<String, Double> value = entry.getValue();
            System.out.println("=> "+key);
            System.out.println("Tahun : "+value.keySet());
            System.out.println("VALS : "+value.values());
            System.out.println("_________________________________________");
        }
    }
    
    private TreeMap<String, Double> calcPerMatkulPerYear(String matkul, String nilai){
        // semua key : tahun, value => list of mahasiswa
        TreeMap<String, Double> res = new TreeMap<>();
        Set<String> set_datas_key = data_mahasiswa.keySet();
        String[] tahun = set_datas_key.toArray(new String[set_datas_key.size()]);
        for (int i = 0; i < tahun.length; i++) {
            List<mahasiswa> list_mahasiswa = data_mahasiswa.get(tahun[i]);
            double total_mahasiswa = 0;
            double total_mahasiswa_with_same_val = 0;
            for (int j = 0; j < list_mahasiswa.size(); j++) {
                mahasiswa curr_mahasiswa = list_mahasiswa.get(j);
                if(curr_mahasiswa.hasMatkul(matkul)){
                    total_mahasiswa++;
                    if(curr_mahasiswa.getNilaiString(matkul).compareToIgnoreCase(nilai)==0){
                        total_mahasiswa_with_same_val++;
                    }
                }
            }
            res.put(tahun[i], total_mahasiswa_with_same_val/total_mahasiswa);
        }
        return res;
    }
}


// x = kaliin semua matkul berdasarkan tahun
// y = x * (jumlah mahasiswa di tahun Y / total_mahasiswa_keseluruhan (3.5-7)
// y = 0.7 * (50/200)
// chance 3.5 berapa ? 4 ? 5 ?