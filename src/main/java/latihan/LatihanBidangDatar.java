package latihan; // package

public class LatihanBidangDatar { // class
    public static void main(String[] args){ // main method

        String nama = "Persegi Panjang"; // variabel
        double panjang = 10;
        double lebar = 5;
        double luas = panjang * lebar;
        double keliling = 2 * (panjang + lebar);

        // output
        System.out.println("Nama       : " + nama);
        System.out.println("Panjang    : " + panjang);
        System.out.println("Lebar      : " + lebar);
        System.out.println("Luas       : " + luas);
        System.out.println("Keliling   : " + keliling);
    }
}