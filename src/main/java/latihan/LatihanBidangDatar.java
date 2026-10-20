```java
package latihan; // package

public class LatihanBidangDatar { // class
    public static void main(String[] args) { // main method

        // Persegi Panjang
        String nama = "Persegi Panjang";
        double panjang = 10;
        double lebar = 5;
        double luas = panjang * lebar;
        double keliling = 2 * (panjang + lebar);

        // Lingkaran
        String namaLingkaran = "Lingkaran";
        double r = 7;
        double luasLingkaran = Math.PI * r * r;

        // Output Persegi Panjang
        System.out.println("=== PERSEGI PANJANG ===");
        System.out.println("Nama       : " + nama);
        System.out.println("Panjang    : " + panjang);
        System.out.println("Lebar      : " + lebar);
        System.out.println("Luas       : " + luas);
        System.out.println("Keliling   : " + keliling);

        // Output Lingkaran
        System.out.println("\n=== LINGKARAN ===");
        System.out.println("Nama       : " + namaLingkaran);
        System.out.println("Jari-jari  : " + r);
        System.out.printf("Luas       : %.2f%n", luasLingkaran);
    }
}
```
