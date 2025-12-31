Isim: Yusuf Enes Kütük
Ogrenci No: 231101013
Ders: BIL 212 - HW-2


DERLEME 
-------------------------------------------------------
Kodları derlemek için terminalde kaynak dosyaların olduğu dizine gelip şu komutu çalıştırabilirsiniz:
javac *.java

Bu komut Main.java, PPMImage.java ve Quadtree.java dosyalarını derleyecektir.





CALISTIRMA 
-------------------------------------------------------
Program komut satırı argümanlarıyla (flags) çalışır.
Ornek kullanimlar asagidaki gibidir:

1. SIKISTIRMA MODU :
Girilen resmi 8 farklı seviyede sıkıştırır ve dosyaları üretir.
Komut:
java Main -i kira.ppm -o sonuc -c

Eger kareleri de çizdirmek isterseniz -t ekleyebilirsiniz:
java Main -i kira.ppm -o sonuc -c -t

2. KENAR BULMA MODU :
Girilen resimdeki kenarları filtreler.
Komut:
java Main -i kira.ppm -o kenar -e

Kenar bulurken quadtree yapısını görmek için:
java Main -i kira.ppm -o kenar -e -t

Argümanlar:
-i <dosya> : Girdi dosyası (örnek: kira.ppm)
-o <isim>  : Çıktı dosyası ismi
-c         : Sıkıştırma modunu aktif eder (8 dosya üretir)
-e         : kenar bulma modunu aktif eder
-t         : (Opsiyonel) Quadtree sınırlarını mavi çizgilerle gösterir








DOSYA LISTESI (FILE DIRECTORY)
-------------------------------------------------------
1. Main.java:
   Programın başlangıç noktası. Komut satiri argümanlarını (-i, -o, -c vb.) isler.
   İstenen sıkıştırma oranlarını yakalamak için Binary Search kullanarak en uygun threshold değerini otomatik bulur.

2. Quadtree.java:
   Resmi recursive olarak 4 parçaya bölen ağaç yapısını tutar.
   - decompress(): Sıkıştırılmış veriyi resme çevirir.
   - edgeDetect(): Küçük yapraklara filtre uygular, büyük blokları siyah yapar.

3. PPMImage.java:
   .ppm (P3 format) dosyalarını okumak ve yazmak için kullanılan yardımcı sınıf.








NOTLAR VE SINIRLAMALAR (BUGS & LIMITATIONS)
-------------------------------------------------------
- Ödevde belirtildiği üzere program sadece kare (N x N) resimlerle çalışmaktadır. Kare olmayan resim girilirse hata verip kapanır.
- İstenen tam sıkıştırma oranlarını (0.002, 0.004 vb.) yakalamak için program threshold değerini dinamik olarak arar. Bu yüzden oranlar tam birebir ayni olmasa da (örneğin 0.2 yerine 0.2001 gibi) çok yakındır.
- Kenar bulma kısmında detay seviyesini korumak için threshold değeri otomatik olarak optimize edilir.