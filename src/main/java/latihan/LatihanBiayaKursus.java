
package latihan; // package

public class LatihanBiayaKursus { // class
    public static void main(String[] args){ // main method
    String kode = "JAVA-BSC"; // variabel
    String nama = "Java Desktop Fundamental";
    double biaya = 3_800_000;
    double regs = 500_000;
    double diskon = 0.10;
    boolean aktif = true;
    double potongan = biaya * diskon;
    double total = biaya;
    
    
    //3 kondisi
    if (biaya >= 3_000_000) {
        total = (biaya + regs) - ((biaya + regs) *0.15);
    }
    
    else if (biaya >= 1_500_000) {
        total = (biaya + regs) - ((biaya + regs) *0.10);     
    }
    
    else{
        total = (biaya + regs) - ((biaya + regs) *0.05);
    }
    // deklarasi / output
    System.out.println("Kode   : " + kode);
    System.out.println("Kursus : " + nama);
    System.out.println("Aktif  : " + aktif);
    System.out.printf("Total  : Rp%,.0f%n", total);    
     }
}
