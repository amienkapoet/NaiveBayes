
import java.util.List;
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
    
    public void probsPerYear(TreeMap<String, String> predict_data){
        Set<String> set_datas_key = predict_data.keySet();
        String[] data_matkul = set_datas_key.toArray(new String[set_datas_key.size()]);
        for (int i = 0; i < data_matkul.length; i++) {
            String matkul = data_matkul[i];
            String nilai = predict_data.get(matkul);
            TreeMap<String, Double> vals = calc(matkul, nilai);
            System.out.println("---> "+matkul);
            System.out.println("KEYS : "+vals.keySet());
            System.out.println("VALS : "+vals.values());
            System.out.println("________________________________________");
        }
    }
    
    public TreeMap<String, Double> calc(String matkul, String nilai){
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
