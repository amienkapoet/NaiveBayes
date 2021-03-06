/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author zerd
 */
public class mahasiswa {

    private double lulus;
    public TreeMap<String, String> nilai = new TreeMap<String, String>();
    public String info;

    public mahasiswa(double lulus, TreeMap nilai, String info) {
        this.lulus = lulus;
        this.nilai = nilai;
        this.info = info;
    }

    public double getLulus() {
        return this.lulus;
    }

    public int getNilai(String key) {
        String s_nilai = nilai.get(key);
        if(s_nilai == null){
            return -1;
        }
        return nilaiToInt(nilai.get(key));
    }
    
    public String getNilaiString(String matkul) {
        String s_nilai = nilai.get(matkul);
        if(s_nilai == null){
            return "";
        }
        return s_nilai;
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
    
    public boolean hasMatkul(String matkul){
        if(nilai.containsKey(matkul)){
            return true;
        }else{
            return false;
        }
    }

    public void printNilai() {
        System.out.println("==========================================================================");
        nilai.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        });
        System.out.println("LULUS : "+lulus);
    }
}