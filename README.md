# Food Delivery Route Optimizer

### Mata Kuliah
Struktur Data dan Pemrograman Berorientasi Objek

### Dosen Pengampu
Hafara Firdausi, S. Kom, M.Kom

---

## Anggota Kelompok

| NRP | Nama |
|---|---|
| 5027251018 | Nazwa Aulia Dwi Purnomo |
| 5027251048 | M. Faris Roisul Azhar |
| 5027251045 | Ahmad Nayottama Juliansyah |
| 5027251008 | Silfi Rochmatul Auliyah |
| 5027251129 | Dafa Ridho Zhafif |

 
## Cara Menjalankan Program
 
### 1. Persiapan
 
Pastikan seluruh file sudah diunduh terlebih dahulu dengan struktur folder sebagai berikut:
 
```
FP_Strukdat_Kelompok3/
├── src/
│   ├── Main.java
│   ├── graph/
│   │   └── Graph.java
│   ├── tree/
│   │   └── MinHeap.java
│   └── model/
│       ├── Order.java
│       ├── Node.java
│       ├── Edge.java
│       └── CSVLoader.java
├── data/
│   ├── nodes.csv
│   ├── edges.csv
│   └── orders.csv
├── docs/
│   ├── laporan.pdf
│   └── tracing.pdf
└── README.md
```

### 2. Menjalankan Program

Buka folder proyek di Visual Studio Code, kemudian buka file `src/Main.java`
dan klik tombol **Run Java** yang muncul di pojok kanan atas editor.

Pastikan ekstensi **Extension Pack for Java** sudah terinstall di VS Code.
Jika belum, install melalui Extensions (Ctrl+Shift+X) dan cari
"Extension Pack for Java".

## Daftar Isi

1. Deskripsi Masalah
2. Dataset
3. Struktur Graph yang digunakan
4. Struktur Tree yang digunakan
5. Algoritma yang digunakan
6. Design Decision Log
7. Tracing 
8. Screenshot Hasil Program
9. Analisis Kompleksitas
10. What-if analysis
11. Kesimpulan

--- 

### Deskripsi Masalah

### 1.1 Latar Belakang
Industri pengantaran makanan daring mengalami pertumbuhan yang signifikan dalam beberapa tahun terakhir. Pertumbuhan tersebut membawa tantangan operasional yang semakin kompleks, terutama bagi kurir yang harus mengelola banyak pesanan secara bersamaan dalam waktu yang terbatas.
Terdapat tiga permasalahan utama yang dihadapi dalam operasional pengantaran makanan. Pertama, kurir memerlukan informasi rute tercepat dari lokasi restoran menuju lokasi pelanggan agar pengantaran dapat diselesaikan tepat waktu. Kedua, kondisi jalan tidak selalu dapat diprediksi, kemacetan maupun penutupan jalan dapat terjadi kapan saja dan memaksa kurir untuk mencari jalur alternatif secara cepat. Ketiga, pesanan dapat masuk sewaktu-waktu dengan tingkat urgensi yang berbeda-beda, termasuk pesanan VIP yang masuk secara mendadak dan harus segera didahulukan tanpa mengabaikan pesanan lain yang sudah mengantre.
Ketiga permasalahan tersebut tidak dapat diselesaikan hanya dengan pendekatan manual. Diperlukan sebuah sistem yang mampu merepresentasikan jaringan jalan sebagai Weighted Undirected Graph, sehingga setiap perubahan kondisi jalan dapat langsung memengaruhi keputusan rute yang diambil. Selain itu, sistem juga harus dapat mengelola antrean pesanan berdasarkan tingkat kepentingannya, tidak hanya berdasarkan urutan masuknya pesanan. Dengan demikian, integrasi antara struktur data Graf dan Tree menjadi pendekatan yang relevan untuk menjawab kompleksitas operasional pengantaran makanan secara nyata.

### 1.2 Rumusan Masalah 
Berdasarkan latar belakang di atas, rumusan masalah dalam project ini adalah sebagai berikut.
Bagaimana menentukan rute tercepat dari lokasi restoran menuju lokasi pelanggan berdasarkan waktu tempuh pada jaringan jalan yang ada?
Bagaimana sistem menentukan rute alternatif dan memvalidasi keterhubungan jaringan apabila terdapat jalan yang ditutup atau tidak dapat dilewati?
Bagaimana menentukan urutan pengantaran yang optimal ketika terdapat banyak pesanan dengan tingkat prioritas dan deadline yang berbeda, termasuk pesanan VIP yang masuk secara mendadak?

### 1.3 Tujuan
Proyek ini bertujuan membangun sistem Food Delivery Route Optimizer berbasis Java yang mampu:
Menentukan urutan pengantaran secara otomatis berdasarkan prioritas dan deadline menggunakan MinHeap, termasuk menangani order VIP yang masuk mendadak.
Menentukan rute pengantaran tercepat serta menghitung total waktu dan jarak menggunakan algoritma Dijkstra pada Weighted Undirected Graph.
Memvalidasi keterhubungan jaringan jalan menggunakan algoritma BFS.
Mengintegrasikan struktur Tree dan Graph secara langsung, di mana order yang keluar dari MinHeap langsung diproses oleh Dijkstra untuk menentukan rute pengantarannya.
Membandingkan tiga strategi pengantaran, yaitu berdasarkan prioritas pelanggan, deadline tercepat, dan jarak terdekat agar operator dapat memahami trade-off dari masing-masing pendekatan.

### 1.4 Batasan Sistem (Constraint)
Sistem ini dibuat dengan batasan sebagai berikut.
Struktur graph pengantaran terdiri atas 25 node yang merepresentasikan 6 restoran, 12 lokasi pelanggan, dan 7 simpang jalan.
Graph dihubungkan oleh 41 edge dua arah dengan bobot utama berupa waktu tempuh dalam satuan menit.
Setiap edge memiliki atribut tambahan berupa jarak, biaya, status jalan, dan rating jalan.
Sistem dibuat tanpa menggunakan library graph atau tree dari pihak ketiga.
Algoritma Dijkstra dan BFS diimplementasikan secara mandiri tanpa menggunakan implementasi bawaan Java.
Data node, edge, dan order dibaca dari dataset sehingga dapat diperbarui tanpa mengubah kode program.
Sistem dijalankan sebagai aplikasi command-line interface (CLI).

### Dataset
#### Gambaran Umum Dataset
Dataset yang digunakan dalam proyek ini merupakan dataset sintetis yang dirancang secara khusus untuk mensimulasikan skenario pengiriman makanan di wilayah perkotaan. Dataset dibuat sendiri karena belum tersedia dataset publik yang secara spesifik memodelkan jaringan jalan + restoran + pelanggan dalam satu format graph yang terintegrasi dan sesuai kebutuhan algoritma optimasi rute.

<img width="584" height="155" alt="Screenshot 2026-06-16 at 23 13 17" src="https://github.com/user-attachments/assets/8233a54f-bb6c-4c26-ae8a-695e1cdadb9a" />

#### 2.2 Alasan Dataset Dibuat Sendiri
	Dataset dibuat secara mandiri karena beberapa alasan:
Tidak ada dataset publik yang sesuai. Dataset pengiriman makanan yang tersedia secara publik (seperti dari GrubHub atau Grab) umumnya hanya menyediakan data transaksional (waktu, jarak tempuh total), bukan data struktural graf yang mencakup node jalan/simpang, bobot tepi berlapis, dan relasi restoran-pelanggan secara simultan.
Fleksibilitas desain eksperimen. Dengan membuat dataset sendiri, struktur graf dapat disesuaikan untuk menguji berbagai kondisi seperti variasi prioritas pesanan, kepadatan node, dan distribusi jarak.
Kontrol kualitas data. Dataset sintetis memungkinkan tidak ada nilai kosong (missing values) dan semua atribut dapat dikontrol untuk memastikan konsistensi pengujian algoritma.
Representasi wilayah lokal. Dataset dirancang menggunakan koordinat geografis yang merepresentasikan kawasan kota Surabaya, sehingga lebih relevan dengan konteks penggunaan nyata.

#### 2.3 File Nodes (nodes_copy.csv)

<img width="587" height="187" alt="Screenshot 2026-06-16 at 23 14 12" src="https://github.com/user-attachments/assets/48966825-22f4-46c2-9335-c2302b654d46" />

##### Distribusi node berdasarkan tipe:


<img width="570" height="169" alt="Screenshot 2026-06-16 at 23 15 48" src="https://github.com/user-attachments/assets/66c7409a-87af-420e-8cb0-9fa6e38ca301" />

##### Seluruh data nodes:

<img width="566" height="544" alt="Screenshot 2026-06-16 at 23 16 20" src="https://github.com/user-attachments/assets/d11484e8-79e4-475a-b29f-65509b7fb117" />

<img width="558" height="242" alt="Screenshot 2026-06-16 at 23 16 34" src="https://github.com/user-attachments/assets/e4e78785-c0e8-4e38-b26f-2b379ea29402" />

#### 2.4 Penjelasan Pembagian Tipe Node

Pembagian node menjadi tiga tipe didasarkan pada peran fungsional masing-masing titik dalam jaringan pengiriman:
a) Restaurant (R) — 6 node (R1–R6)
Node restoran berfungsi sebagai titik asal (source) dalam setiap order pengiriman. Pada restoran inilah makanan disiapkan dan menjadi titik keberangkatan kurir. Dalam pemodelan graf, node ini memiliki derajat keluar (out-degree) yang tinggi karena menjadi sumber banyak rute.
b) Customer (C) — 12 node (C1–C12)
Node pelanggan berfungsi sebagai titik tujuan (destination) pengiriman. Setiap pelanggan memiliki tenggat waktu (deadline) dan prioritas yang berbeda-beda, sehingga algoritma optimasi perlu memperhitungkan constraint ini saat merencanakan rute.
c) Simpang (S) — 7 node (S1–S7)
Node simpang merepresentasikan persimpangan jalan atau titik perantara dalam jaringan jalan kota. Node ini tidak menjadi asal maupun tujuan pengiriman secara langsung, tetapi berperan sebagai relay point yang menghubungkan restoran dengan pelanggan melalui jalur yang lebih efisien. Keberadaan node simpang membuat pemodelan graf lebih realistis karena mencerminkan kondisi jaringan jalan nyata di mana kurir tidak selalu bisa langsung dari restoran ke pelanggan.

#### 2.5 File Edges (edges_copy.csv)

<img width="569" height="255" alt="Screenshot 2026-06-16 at 23 17 21" src="https://github.com/user-attachments/assets/556d88d7-b6ce-4b30-bbda-8f659e9eea29" />

#### 2.6 File Orders (orders_copy.csv)

<img width="563" height="215" alt="Screenshot 2026-06-16 at 23 17 48" src="https://github.com/user-attachments/assets/9147b5a1-70c5-4ae1-ba7e-01494f4c2cea" />

#### Distribusi prioritas order:

<img width="578" height="122" alt="Screenshot 2026-06-16 at 23 18 15" src="https://github.com/user-attachments/assets/1c464ec7-d321-4492-81c8-9d01f1ef81d9" />

### Struktur Graph yang digunakan
#### 3.1 Jenis Graph
Sistem Food Delivery Route Optimizer menggunakan Weighted Undirected Graph, yaitu graf berbobot yang hubungan antar nodenya bersifat dua arah. Pemilihan jenis graf ini didasarkan pada karakteristik jaringan jalan nyata, di mana jalan yang menghubungkan dua titik lokasi umumnya dapat dilalui dari kedua arah dengan waktu tempuh yang sama.
Tiga karakteristik utama graph yang digunakan:
- Weighted (Berbobot): Setiap edge memiliki bobot berupa waktuTempuh dalam satuan menit. Bobot ini menjadi penentu utama dalam pencarian rute terbaik oleh algoritma Dijkstra. Tanpa bobot, semua jalan dianggap sama dan sistem tidak bisa membedakan mana rute yang lebih cepat.
- Undirected (Dua Arah): Setiap edge berlaku untuk kedua arah perjalanan. Jika ada jalan dari R1 ke S1, maka secara otomatis ada juga jalan dari S1 ke R1 dengan waktu tempuh yang sama. Ini merepresentasikan kondisi jalan dua arah di dunia nyata.
- Connected (Terhubung): Dalam kondisi normal (tidak ada jalan yang ditutup), seluruh node dapat saling terhubung satu sama lain melalui jalur-jalur yang tersedia. Keterhubungan ini diverifikasi menggunakan algoritma BFS.
#### 3.2 Representasi: Adjacency List 
Graph direpresentasikan menggunakan Adjacency List dengan struktur data berikut yang didefinisikan di Graph.java:
```
Map<String, List<Edge>> adjacencyList;
```
Setiap key berupa nodeId (String), dan value-nya adalah List<Edge> yang berisi semua tetangga dari node tersebut beserta seluruh atribut jalannya. Selain adjacency list, graph juga menyimpan semua node dalam:
Map<String, Node> nodes;

Sehingga informasi lengkap suatu node (nama, tipe, koordinat) bisa diakses langsung dari nodeId-nya.
Contoh representasi adjacency list pada dataset:
R1 (Restoran Makan Enak)   → S1(2m), R2(3m), C9(8m)
S1 (Simpang Jl Raya Utama) → R1(2m), R2(2m), R5(2m), S2(2m), S3(3m), C1(4m)
C1 (Pelanggan Budi)        → R2(5m), S1(4m), C2(3m)

<img width="934" height="578" alt="Screenshot 2026-06-16 165758" src="https://github.com/user-attachments/assets/8a9332d9-c454-4357-a2f0-14cf79721d0d" />

#### 3.3 Penjelasan Node
Node merepresentasikan titik-titik lokasi dalam jaringan pengantaran makanan. Setiap node memiliki 5 atribut yang didefinisikan dalam Node.java : <br>
<img width="682" height="191" alt="Screenshot 2026-06-16 232205" src="https://github.com/user-attachments/assets/40ddbdae-353c-4ff3-98e9-dba97efdfdf1" /> <br>
Dataset menggunakan 25 node yang dibagi menjadi 3 tipe berdasarkan perannya dalam sistem: <br>
<img width="649" height="173" alt="Screenshot 2026-06-16 232244" src="https://github.com/user-attachments/assets/3e39e8ea-30b1-4d0c-b945-8263efc12671" /><br>
Node bertipe Simpang berperan penting sebagai jembatan konektivitas yang memungkinkan kurir berpindah dari zona restoran ke zona pelanggan yang tidak terhubung langsung. Misalnya, untuk mencapai C1 dari R1, kurir harus melewati S1 terlebih dahulu karena R1 dan C1 tidak terhubung langsung.<br>
Semua node di-load dari nodes.csv melalui CSVLoader.loadNodes() saat program pertama kali dijalankan, dan disimpan di Map<String, Node> nodes di dalam Graph.
#### 3.4 Penjelasan Edge
Edge merepresentasikan jalan yang menghubungkan dua node. Setiap edge memiliki 5 atribut tambahan yang didefinisikan dalam Edge.java: <br>
<img width="488" height="217" alt="image" src="https://github.com/user-attachments/assets/2719511b-ef1f-400f-86ef-9f2e863b2aeb" /> <br>
Dataset memiliki 40 edge di edges.csv yang semuanya berstatus "Lancar" pada kondisi awal. Karena graph bersifat undirected, setiap baris di CSV menghasilkan dua directed edge di adjacency list, satu arah asli dan satu arah balik, sehingga total edge di adjacency list menjadi 82 directed edges. <br>
Pemilihan waktuTempuh sebagai bobot utama (bukan jarak) didasarkan pada tujuan sistem yakni mengoptimalkan kecepatan pengantaran, bukan meminimalkan kilometer. Jalan yang lebih panjang bisa saja lebih cepat jika kondisinya lebih lancar. Total jarak dan biaya tetap dihitung dan ditampilkan di ShortestPathResult sebagai informasi tambahan bagi kurir.

#### 3.5 Alasan Memilih Adjacency List
Keputusan menggunakan Adjacency List dibandingkan Adjacency Matrix didasarkan pada dua alasan utama: efisiensi memori dan karakteristik graph.

<img width="499" height="344" alt="image" src="https://github.com/user-attachments/assets/d3322edc-7718-48e6-a975-dbd4fbda1b63" /> <br>
Algoritma Dijkstra dan BFS pada implementasi ini sangat sering melakukan iterasi tetangga suatu node. Dengan Adjacency List, iterasi hanya menyentuh node yang benar-benar terhubung (O(degree)), sedangkan jika menggunakan Matrix harus scan semua 25 kolom meskipun sebagian besar kosong (O(V)). <br>
Satu-satunya keunggulan Matrix adalah cek edge langsung O(1), namun operasi ini tidak sering dilakukan dalam implementasi Dijkstra dan BFS, sehingga tidak menjadi faktor penentu.
#### 3.6 Fitur closedEdges — Simulasi Jalan Tertutup
Sistem menyediakan fitur simulasi penutupan jalan menggunakan sebuah Set<String> bernama closedEdges yang didefinisikan di Graph.java:
```
Set<String> closedEdges = new HashSet<>();
```
Dipilihnya HashSet (bukan List atau Array) karena operasi contains() pada HashSet berjalan dalam O(1), sedangkan List butuh O(n). Mengingat isEdgeActive() dipanggil setiap kali Dijkstra atau BFS melewati sebuah edge, yang bisa ratusan kali dalam satu pencarian rute. Olrh ksrena itu, efisiensi ini sangat penting.

### Struktur Tree yang digunakan

#### 4.1 Jenis Tree yang Digunakan
Pada proyek Food Delivery Route Optimizer, struktur tree yang digunakan adalah Min-Heap (Binary Heap). Min-Heap merupakan struktur data berbentuk pohon biner lengkap (Complete Binary Tree) yang disimpan menggunakan ArrayList.
Karakteristik utama Min-Heap adalah setiap parent node memiliki nilai prioritas yang lebih tinggi dibandingkan child node-nya. Dengan demikian, elemen dengan prioritas tertinggi selalu berada pada root (indeks 0) sehingga dapat diakses dengan cepat.
Implementasi Min-Heap digunakan untuk mengatur antrian pesanan makanan agar sistem dapat menentukan pesanan mana yang harus diproses terlebih dahulu.

#### 4.2 Tujuan Penggunaan Min-Heap
Min-Heap digunakan sebagai struktur antrian order karena memiliki efisiensi yang baik dalam operasi penyisipan dan pengambilan data.
Tujuan utama penggunaannya adalah:
Menyimpan seluruh order yang masuk ke sistem.
Menentukan urutan pengantaran berdasarkan prioritas.
Mengambil order dengan prioritas tertinggi secara cepat.
Menangani order mendadak (VIP Order).
Aturan prioritas yang digunakan adalah:

<img width="631" height="137" alt="Screenshot 2026-06-16 at 23 28 07" src="https://github.com/user-attachments/assets/80c007ae-9184-45f3-869d-a6e655fa2f66" />

Urutan pemrosesan:
Prioritas lebih tinggi didahulukan (Urgent > High > Normal).
Jika prioritas sama, maka order dengan deadline lebih kecil diproses terlebih dahulu.


#### 4.3 Hubungan dengan Order.compareTo()

Pengurutan pada Min-Heap tidak dilakukan secara langsung oleh heap, melainkan menggunakan method compareTo() pada class Order.
Secara logika:


<img width="348" height="101" alt="Screenshot 2026-06-16 at 23 28 46" src="https://github.com/user-attachments/assets/7ed8fa13-60f0-4e31-9e39-ed5dbbb8c8f6" />

Ketika dua order dibandingkan:
Sistem akan membandingkan nilai prioritas terlebih dahulu.
Jika prioritas berbeda, order dengan prioritas lebih tinggi dianggap lebih penting.
Jika prioritas sama, sistem membandingkan deadline.
Deadline yang lebih kecil akan mendapatkan prioritas lebih tinggi.
Contoh:

<img width="629" height="96" alt="Screenshot 2026-06-16 at 23 29 07" src="https://github.com/user-attachments/assets/bfdeab3f-77e2-4469-8f63-4e120362cb2e" />

Karena prioritas sama, maka:
02 diproses terlebih dahulu karena memiliki “deadline” yang lebih kecil
#### 4.4 Cara Kerja Enqueue (Insert)
Method:

<img width="627" height="65" alt="Screenshot 2026-06-16 at 23 29 45" src="https://github.com/user-attachments/assets/67d658b4-9c8b-4d0c-84a7-d29b13e1b706" />

Langkah-langkah:
1. Insert ke posisi terakhir
   <img width="632" height="55" alt="Screenshot 2026-06-16 at 23 30 09" src="https://github.com/user-attachments/assets/e7f79d1b-2b54-4475-994f-62caddcb1c22" />

2. Heapify-Up

<img width="619" height="66" alt="Screenshot 2026-06-16 at 23 30 44" src="https://github.com/user-attachments/assets/052e6a25-7abc-48a0-95f6-a81cb3e96ad4" />


Setelah dimasukkan, order akan dibandingkan dengan parent-nya.
Jika order memiliki prioritas lebih tinggi, maka dilakukan pertukaran posisi (swap).
Proses ini terus dilakukan hingga:
mencapai root, atau
parent memiliki prioritas lebih tinggi.
#### 4.5 Cara Kerja Dequeue (Remove Root)
Method:

<img width="631" height="68" alt="Screenshot 2026-06-16 at 23 31 09" src="https://github.com/user-attachments/assets/b1a2bfe1-da16-4bc7-ab9f-9b73a2298ad4" />

digunakan untuk mengambil order dengan prioritas tertinggi.
Karena root selalu menyimpan order terbaik, maka order tersebut langsung diambil.
Langkah-langkah
1. Simpan root


<img width="622" height="42" alt="Screenshot 2026-06-16 at 23 31 28" src="https://github.com/user-attachments/assets/701357f4-9645-43da-839c-6d05283a3f5a" />

2. Ambil elemen terakhir
   
<img width="623" height="51" alt="Screenshot 2026-06-16 at 23 31 44" src="https://github.com/user-attachments/assets/2cab6b45-57f9-4191-ab3e-66c2ec6d465c" />

3. Pindahkan ke root
   
<img width="630" height="35" alt="Screenshot 2026-06-16 at 23 32 05" src="https://github.com/user-attachments/assets/e2e18172-36b1-4f7b-929d-13ec0aaaf06f" />

4. Heapify-Down
   
<img width="630" height="32" alt="Screenshot 2026-06-16 at 23 32 34" src="https://github.com/user-attachments/assets/edd742ed-b958-42dc-b53e-8ccfdca1b7c7" />

Elemen root yang baru akan dibandingkan dengan kedua child-nya.
Jika child memiliki prioritas lebih tinggi, maka dilakukan swap.
Proses berlangsung sampai struktur heap kembali valid.
#### 4.6 Heapify-Up dan Heapify-Down
Heapify-Up
Digunakan saat insert data baru.
Algoritma:
Bandingkan node dengan parent.
Jika prioritas node lebih tinggi, lakukan swap.
Ulangi hingga heap valid.
Fungsi:

<img width="646" height="83" alt="Screenshot 2026-06-16 at 23 33 04" src="https://github.com/user-attachments/assets/2906de2a-50d2-432c-831d-d95940fb6daa" />

Heapify-Down
Digunakan setelah root dihapus.
Algoritma:
Bandingkan parent dengan kedua child.
Cari child dengan prioritas tertinggi.
Tukar posisi jika diperlukan.
Ulangi hingga heap valid.
Fungsi:

<img width="632" height="64" alt="Screenshot 2026-06-16 at 23 33 27" src="https://github.com/user-attachments/assets/a2311bc9-f408-4089-89ba-52135d89aba3" />

4.7 Penanganan VIP Order
Sistem menyediakan fitur:
untuk menangani pesanan VIP yang masuk secara mendadak.

<img width="624" height="75" alt="Screenshot 2026-06-16 at 23 33 44" src="https://github.com/user-attachments/assets/c926f9e3-4d10-40f0-807b-6b0649c406e8" />
Impelementasinya:

<img width="635" height="48" alt="Screenshot 2026-06-16 at 23 33 58" src="https://github.com/user-attachments/assets/e3e14225-8b0d-4510-b312-786ac0a164bc" />

VIP order akan dimasukkan ke heap seperti order biasa, kemudian dilakukan proses heapifyUp().
Karena VIP order umumnya memiliki prioritas paling tinggi, order tersebut akan bergerak ke atas heap dan berpotensi menjadi root.
#### 4.8 Kesimpulan
Min-Heap digunakan sebagai struktur data utama untuk mengelola antrian pengantaran makanan. Struktur ini memungkinkan sistem mengambil order dengan prioritas tertinggi secara efisien menggunakan operasi enqueue, dequeue, heapifyUp, dan heapifyDown. Pengurutan order ditentukan oleh method compareTo() dengan aturan Urgent > High > Normal, kemudian mempertimbangkan deadline terkecil. Selain itu, sistem juga mendukung penanganan VIP Order sehingga pesanan penting dapat segera diprioritaskan dalam antrian pengantaran.

### Algoritma yang digunakan
Sistem Food Delivery Route Optimizer mengimplementasikan dua algoritma graph: Dijkstra untuk mencari rute pengantaran tercepat, dan BFS untuk memvalidasi keterhubungan antar lokasi. Keduanya diimplementasikan secara mandiri di Graph.java tanpa menggunakan library eksternal. 
#### 5.1 Algoritma Dijkstra 
##### 5.1.1 Tujuan
Dijkstra digunakan untuk mencari rute tercepat dari node asal ke node tujuan berdasarkan bobot waktuTempuh (menit). Setiap kali kurir hendak mengantarkan order, Dijkstra menentukan jalur mana yang harus dilewati agar waktu pengantaran paling singkat.
##### 5.1.2 Tiga Struktur Data Internal 
Dijkstra pada implementasi ini menggunakan tiga struktur data yang bekerja bersama:

1. dist : Menyimpan waktu tercepat ke setiap node
```
Map<String, Integer> dist = new HashMap<>();
```

Pada awalnya, semua node diisi nilai Integer.MAX_VALUE (tak terhingga) karena belum diketahui jalurnya. Node awal diisi 0 karena kurir sudah berada di sana. Setiap kali ditemukan jalur yang lebih cepat, nilai ini diperbarui.
2. prev : Menyimpan jejak jalur

```
Map<String, String> prev = new HashMap<>();
```

Menyimpan informasi "node ini dicapai dari mana." Digunakan di akhir proses untuk merekonstruksi urutan jalur dari tujuan kembali ke asal.

Contoh: jika prev["C1"] = "S1" dan prev["S1"] = "R1", maka jalurnya adalah R1 → S1 → C1.
4. Priority Queue : Selalu proses yang tercepat dulu
```
PriorityQueue<String> queue = new PriorityQueue<>(
Comparator.comparingInt(id -> dist.getOrDefault(id, Integer.MAX_VALUE)));
```

Priority Queue memastikan node dengan waktu tempuh terkecil selalu diproses lebih dulu. Inilah yang membuat Dijkstra efisien, alias jalur yang paling menjanjikan dieksplorasi lebih awal.

##### 5.1.3 Skip Edge yang Ditutup
Sebelum memproses setiap edge, Dijkstra memanggil isEdgeActive() untuk mengecek apakah jalan tersebut sedang aktif atau ditutup:
```
for (Edge edge : neighbors) {
    if (!isEdgeActive(current, edge.destination)) continue; // skip jalan tutup
    int newDist = dist.get(current) + edge.waktuTempuh;
    if (newDist < dist.getOrDefault(edge.destination, Integer.MAX_VALUE)) {
        dist.put(edge.destination, newDist);
        prev.put(edge.destination, current);
        queue.offer(edge.destination);
    }
}
```

Jika jalan S1↔S2 ditutup, Dijkstra otomatis melewatinya dan mencari jalur alternatif. Apabila tidak ada jalur lain sama sekali, nilai dist[endId] tetap Integer.MAX_VALUE dan method mengembalikan null.

##### 5.1.4 ShortestPathResult 
Hasil Dijkstra dikemas dalam inner class ShortestPathResult yang berisi empat informasi sekaligus: 
<img width="442" height="109" alt="image" src="https://github.com/user-attachments/assets/e745a4d1-3b4b-482e-a8c8-1223777ad2a6" />

Total jarak & biaya dihitung dengan menelusuri setiap edge di sepanjang path:

```
for (int i = 0; i < path.size() - 1; i++) {
    Edge e = getEdge(path.get(i), path.get(i + 1));
    if (e != null) {
        totalJarak += e.jarak;
        totalBiaya += e.biaya;
    }
}

```
Screenshot result.display() : <br>
<img width="455" height="247" alt="image" src="https://github.com/user-attachments/assets/9882e8f4-a1e4-475a-849e-2feccf744dd0" />

##### 5.1.5 Kompleksitas Dijkstra
<img width="442" height="85" alt="image" src="https://github.com/user-attachments/assets/99fa8f25-c201-4b02-85e1-5da23f0c74df" /> <br>
Untuk dataset ini: V=25, E=82, sehingga O((25+82) × log 25) ≈ 107 × 4.64 ≈ 496 operasi.

#### 5.2 Algoritma BFS (Breadth-First Search)  
##### 5.2.1 Tujuan

BFS digunakan untuk memvalidasi keterhubungan area, seperti memastikan bahwa dari suatu lokasi masih bisa dicapai lokasi lain, terutama setelah ada simulasi penutupan jalan. Berbeda dengan Dijkstra yang memperhitungkan bobot, BFS hanya peduli apakah jalur ada atau tidak. 

##### 5.2.2 Tiga Varian BFS
- Varian 1 : isConnected(startId, endId)
  
Mengecek apakah dua node masih terhubung, mengembalikan true atau false. Begitu node tujuan ditemukan, BFS langsung berhenti tanpa perlu menjelajahi seluruh graph.

```
while (!queue.isEmpty()) {
    String current = queue.poll();
    if (current.equals(endId)) return true; // langsung berhenti
    for (Edge edge : adjacencyList.getOrDefault(current, ...)) {
        if (!visited.contains(edge.destination)
                && isEdgeActive(current, edge.destination)) {
            visited.add(edge.destination);
            queue.offer(edge.destination);
        }
    }
}
return false;
```

Dipanggil di checkConnectivity() - menu Simulasi → Cek Keterhubungan BFS. User memilih dua lokasi dan sistem menampilkan apakah keduanya masih terhubung setelah simulasi penutupan jalan.
- Varian 2  : bfsLevelTraversal(startId)
  
Mengembalikan semua node yang bisa dicapai dari startId. BFS berjalan sampai habis tanpa berhenti di node tertentu. Berguna untuk melihat dampak menyeluruh dari penutupan jalan, node yang tidak masuk ke dalam hasil berarti areanya terisolasi. 

Dipanggil di TestGraph.java untuk keperluan pengujian konektivitas graph setelah simulasi. 

- Varian 3 : isConnected(startId, endId)
- 
BFS dengan visualisasi level — setiap node ditampilkan beserta levelnya dari titik awal. Digunakan untuk memperlihatkan struktur jaringan secara visual saat demo. Contoh output dari S1:

Level 0 → S1 (Simpang Jl Raya Utama) <br>
Level 1 → R1 (Restoran Makan Enak)<br>
Level 1 → S2 (Simpang Jl Sudirman)<br>
Level 1 → C1 (Pelanggan Budi)<br>
Level 2 → C2 (Pelanggan Siti)<br>
Level 2 → S4 (Simpang Jl Gatot Subroto)<br>
Level 3 → C8 (Pelanggan Ani) <br>
Dipanggil di TestGraph.java untuk keperluan visualisasi.<br>

Screenshot result.display() : 
<img width="379" height="455" alt="Screenshot 2026-06-16 185206" src="https://github.com/user-attachments/assets/4af0bad4-66d8-4719-8c68-631a072a2cf2" />

##### 5.2.3 Kompleksitas BFS 

<img width="437" height="133" alt="image" src="https://github.com/user-attachments/assets/78f30449-3973-4ed0-9215-fb8445b41d72" />

Untuk dataset ini: O(25+82) = O(107 operasi). Lebih cepat dari Dijkstra karena tidak menggunakan Priority Queue.






### Design Decision Log

<img width="662" height="767" alt="image" src="https://github.com/user-attachments/assets/ecd876f9-6b2c-4cd6-87cd-a30c55f71c8f" />

Keputusan desain pada sistem tidak hanya ditentukan berdasarkan kemudahan implementasi, tetapi juga mempertimbangkan efisiensi struktur data dan kesesuaian terhadap kebutuhan studi kasus. Graph direpresentasikan menggunakan Adjacency List karena lebih sesuai untuk graph sparse dan menghemat penggunaan memori dibanding Adjacency Matrix. 
Untuk proses routing dipilih algoritma Dijkstra karena sistem menggunakan bobot waktu tempuh sehingga pencarian rute tidak cukup hanya menggunakan traversal biasa.
Pengelolaan order dilakukan menggunakan MinHeap agar proses pemilihan order dapat mempertimbangkan prioritas dan deadline secara otomatis. 
Sistem juga menggunakan HashSet untuk menyimpan jalan yang ditutup agar proses pengecekan status edge tetap cepat ketika Dijkstra dan BFS berjalan. 
Dataset disimpan dalam format CSV agar seluruh anggota kelompok dapat memperbarui data tanpa harus mengubah source code.
Selain itu, dipilih pendekatan Weighted Undirected Graph karena lebih sesuai untuk merepresentasikan jaringan jalan pada simulasi delivery. Node disimpan menggunakan HashMap agar pencarian berdasarkan ID efisien, sedangkan aplikasi dijalankan menggunakan Command Line Interface untuk menjaga fokus pengerjaan pada implementasi struktur data dibanding pengembangan antarmuka.



### Tracing

#### 7.1 Tracing MinHeap

Tracing dilakukan untuk memverifikasi bahwa struktur data MinHeap dapat mempertahankan urutan prioritas order secara benar. Pada simulasi ini digunakan tiga data order, yaitu ORD010 dengan prioritas Urgent, ORD004 dengan prioritas High, dan ORD001 dengan prioritas Normal.

| Order ID | Priority | Deadline |
| -------- | -------- | -------- |
| ORD010   | Urgent   | 16       |
| ORD004   | High     | 18       |
| ORD001   | Normal   | 15       |

##### 7.1.1 Insert ORD010 (Urgent, 16)

Setelah `enqueue(ORD010)`:

```text
[ORD010]
```

Karena node berada pada root, proses heapify-up tidak melakukan perpindahan.

---

##### 7.1.2 Insert ORD004 (High, 18)

Array setelah insert:

```text
[ORD010, ORD004]
```

Perbandingan:

```text
ORD004 (High) vs ORD010 (Urgent)
```

Karena High memiliki prioritas lebih rendah daripada Urgent, tidak terjadi swap.

Representasi heap:


<img width="192" height="190" alt="Screenshot 2026-06-16 204000" src="https://github.com/user-attachments/assets/744fc5e4-a0ea-4748-bf11-3bd0d0385949" />


---

##### 7.1.3 Insert ORD001 (Normal, 15)

Array setelah insert:


<img width="650" height="288" alt="Screenshot 2026-06-16 204035" src="https://github.com/user-attachments/assets/5ae2f0a0-7e83-49ec-a824-ba12ff79aa1f" />


Perbandingan:

```text
ORD001 (Normal) vs ORD010 (Urgent)
```

Tidak terjadi swap karena Normal memiliki prioritas lebih rendah.

Representasi heap:


<img width="713" height="706" alt="Screenshot 2026-06-16 204221" src="https://github.com/user-attachments/assets/2e712663-4b38-4581-9c01-b685256e4c40" />



Array final:

```text
[ORD010, ORD004, ORD001]
```

---

##### 7.1.4 Dequeue dan Heapify-Down

Root heap:

```text
ORD010
```

Node terakhir (`ORD001`) dipindahkan ke root.

Array sebelum heapify-down:

```text
[ORD001, ORD004]
```

Proses heapify-down:

| Index | Left Child      | Hasil |
| ----- | --------------- | ----- |
| 0     | ORD004 (High)   | Swap  |
| 1     | Tidak ada child | Stop  |

Array akhir:

```text
[ORD004, ORD001]
```

Representasi heap:


<img width="683" height="298" alt="Screenshot 2026-06-16 204511" src="https://github.com/user-attachments/assets/67470b00-a214-4d26-96ed-0f13c36265c4" />



Hasil dequeue:

```text
ORD010
```

---

#### 7.2 Tracing Algoritma Dijkstra (R1 → C1)

Tracing dilakukan untuk pencarian rute:

```text
R1 → C1
```

Bobot yang digunakan adalah waktu tempuh (menit).

##### 7.2.1 Inisialisasi

```java
dist[R1] = 0
dist[node lain] = ∞
```

Priority Queue:

```text
[R1:0]
```

---

##### 7.2.2 Iterasi Algoritma

| Iterasi | Node Diproses (dist) | Hasil Relaksasi                      |
| ------- | -------------------- | ------------------------------------ |
| 1       | R1 (0)               | S1 = 2, R2 = 3, C9 = 8               |
| 2       | S1 (2)               | S2 = 4, S3 = 5, C1 = 6               |
| 3       | R2 (3)               | Jalur ke C1 = 8 (lebih buruk dari 6) |
| 4       | S2 (4)               | Update node lain                     |
| 5       | S3 (5)               | Update node lain                     |
| 6       | C1 (6)               | Tujuan ditemukan, algoritma berhenti |

---

##### 7.2.3 Rekonstruksi Path

Nilai `prev` yang relevan:

```java
prev[C1] = S1
prev[S1] = R1
```

Rekonstruksi:

```text
C1 ← S1 ← R1
```

Dibalik menjadi:

```text
R1 → S1 → C1
```

---

##### 7.2.4 Hasil Akhir

Path tercepat:

```text
R1 → S1 → C1
```

Total waktu:

```text
2 + 4 = 6 menit
```

Total jarak:

```text
0.3 + 0.6 = 0.9 km
```

Total biaya:

```text
Rp 2.000 + Rp 4.000 = Rp 6.000
```

Sebagai pembanding:

```text
R1 → R2 → C1 = 8 menit
```

Karena lebih lambat, jalur tersebut tidak dipilih oleh Dijkstra.

---

#### 7.3 Tracing Edge Case: Semua Jalan dari R1 Ditutup

Simulasi dilakukan dengan menutup seluruh edge yang terhubung langsung ke R1:

```java
graph.closeEdge("R1", "S1");
graph.closeEdge("R1", "R2");
graph.closeEdge("R1", "C9");
```

Akibatnya node R1 menjadi terisolasi dari graph.

---

##### 7.3.1 Tracing BFS

Kondisi awal:

```text
Queue   = [R1]
Visited = {R1}
```

Saat R1 diproses, seluruh tetangganya di-skip karena edge berstatus tidak aktif.

Queue menjadi kosong:

```text
[]
```

Algoritma berhenti dan mengembalikan:

```text
false
```

Artinya C1 tidak dapat dicapai dari R1.

---

##### 7.3.2 Tracing Dijkstra (R1, C1)

Inisialisasi:

```java
dist[R1] = 0
dist[node lain] = ∞
```

Priority Queue:

```text
[R1]
```

Ketika R1 diproses:

```text
Edge R1-S1 ditutup
Edge R1-R2 ditutup
Edge R1-C9 ditutup
```

Tidak ada relaksasi yang terjadi sehingga queue langsung kosong.

Nilai akhir:

```java
dist[C1] = Integer.MAX_VALUE
```

Karena tujuan tidak pernah tercapai, method mengembalikan:

```text
null
```

dan sistem menampilkan pesan:

```text
Tidak ada jalur dari R1 ke C1
```

---

##### 7.3.3 Kesimpulan Edge Case

Hasil tracing menunjukkan bahwa mekanisme `closedEdges` bekerja konsisten pada BFS maupun Dijkstra. Ketika seluruh jalur keluar dari node awal ditutup:

1. BFS tidak dapat menemukan node tujuan sehingga menghasilkan `false`.
2. Dijkstra tidak dapat melakukan relaksasi sehingga `dist[endId]` tetap bernilai `Integer.MAX_VALUE`.
3. Sistem menyimpulkan bahwa tidak tersedia rute yang dapat digunakan untuk pengantaran.


### Screenshot Hasil Program

### Analisis Kompleksitas

Bagian ini menyajikan analisis kompleksitas waktu dari seluruh operasi utama yang digunakan dalam sistem Food Delivery Route Optimizer. Analisis mencakup struktur data MinHeap, algoritma Graf (Dijkstra dan BFS), serta operasi pendukung lainnya.

| Operasi | Struktur / Algoritma | Kompleksitas Waktu | Alasan |
|----------|---------------------|-------------------|---------|
| Insert Order (enqueue) | MinHeap (ArrayList) | O(log n) | Setelah elemen ditambahkan di akhir array, `heapifyUp()` memindahkan elemen ke atas. Jumlah swap maksimal = tinggi heap = ⌊log₂ n⌋. |
| Pop Order (dequeue) | MinHeap (ArrayList) | O(log n) | Root dihapus, elemen terakhir dipindah ke root, lalu `heapifyDown()` menukar elemen ke bawah. Jumlah swap maksimal = tinggi heap = ⌊log₂ n⌋. |
| Peek Order | MinHeap (ArrayList) | O(1) | Cukup mengakses `heap.get(0)`, yaitu elemen pertama di array. Tidak ada traversal. |
| Delete Order | MinHeap | O(n log n) | Harus mencari elemen terlebih dahulu kemudian melakukan perbaikan heap. |
| Search Order | MinHeap | O(n) | Pencarian dilakukan secara linear pada seluruh isi heap. |
| Display Queue | MinHeap | O(n) | Menampilkan seluruh elemen dalam heap. |
| Dijkstra (cari rute) | Graph + Priority Queue | O((V+E) log V) | Setiap node di-extract dari PriorityQueue satu kali O(V log V) dan setiap edge diperiksa satu kali O(E log V). Untuk dataset ini: V=25, E=82 → ≈ 496 operasi. |
| BFS (cek keterhubungan) | Graph + LinkedList Queue | O(V + E) | Setiap node masuk queue maksimal satu kali dan setiap edge diperiksa satu kali. Untuk dataset ini ≈ 107 operasi. |
| Load Dataset CSV | Graph | O(V + E) | Seluruh node dan edge dibaca satu kali saat program dijalankan. |
| closeEdge() | HashSet | O(1) | Operasi add pada HashSet. |
| openEdge() | HashSet | O(1) | Operasi remove pada HashSet. |
| isEdgeActive() | HashSet | O(1) | Operasi contains pada HashSet. |

#### Ringkasan Kompleksitas per Komponen

- **MinHeap**
  - enqueue → O(log n)
  - dequeue → O(log n)
  - peek → O(1)
  - delete → O(n log n)
  - search → O(n)
  - display → O(n)

- **Graph — Dijkstra**
  - O((V + E) log V)
  - Untuk dataset ini ≈ 496 operasi

- **Graph — BFS**
  - O(V + E)
  - Untuk dataset ini ≈ 107 operasi

- **Graph — Load CSV**
  - O(V + E)
  - Hanya dilakukan sekali saat program dijalankan

- **HashSet closedEdges**
  - isEdgeActive → O(1)
  - closeEdge → O(1)
  - openEdge → O(1)

---

### 10. What-if Analysis

What-if Analysis dilakukan untuk mengevaluasi ketahanan (robustness), skalabilitas, dan kemampuan adaptasi sistem terhadap berbagai perubahan kondisi yang mungkin terjadi pada lingkungan operasional. Analisis ini berfokus pada perilaku struktur data Graph dan MinHeap yang digunakan dalam sistem.

#### 10.1 Skenario Peningkatan Jumlah Node dari 25 Menjadi 10.000

##### a. Kondisi Awal

Sistem awal dirancang untuk menangani sekitar 25 node yang terdiri atas restoran, pelanggan, dan titik distribusi. Pada skenario ini jumlah node ditingkatkan menjadi 10.000 node untuk menguji skalabilitas sistem.

##### b. Dampak terhadap Graph

Algoritma Dijkstra yang digunakan memiliki kompleksitas:

```text
O((V + E) log V)
```

Ketika jumlah node meningkat secara signifikan, ukuran adjacency list, tabel jarak (`dist`), tabel predecessor (`prev`), dan himpunan `visited` akan bertambah.

Akibatnya:

- Penggunaan memori meningkat.
- Waktu komputasi pencarian rute menjadi lebih lama.

### c. Dampak terhadap MinHeap

MinHeap tidak menyimpan node graph, melainkan antrian order.

Kompleksitas operasi:

```text
enqueue  = O(log n)
dequeue  = O(log n)
```

tetap tidak berubah.

Namun apabila jumlah order meningkat seiring pertumbuhan node:

- Ukuran heap menjadi lebih besar.
- Penggunaan memori meningkat.

### d. Kesimpulan

Bottleneck utama sistem berada pada proses pencarian rute dalam Graph.

MinHeap tetap mampu menangani pertambahan data secara efisien.

---

#### 10.2 Skenario Penutupan Jalan (Edge Closure)

##### a. Kondisi Awal

Beberapa ruas jalan tidak dapat digunakan karena perbaikan, kecelakaan, atau kondisi darurat sehingga edge tertentu harus dinonaktifkan.

##### b. Dampak terhadap Graph

Sistem menggunakan mekanisme:

```java
closeEdge()
```

untuk menandai jalan yang ditutup.

Selama proses Dijkstra maupun BFS, edge yang ditutup akan diabaikan sehingga sistem secara otomatis mencari jalur alternatif yang masih tersedia.

##### c. Dampak terhadap MinHeap

Tidak terdapat pengaruh langsung terhadap struktur heap karena MinHeap hanya mengatur prioritas order.

Namun waktu pengiriman dapat meningkat apabila rute alternatif lebih panjang.

##### d. Kesimpulan

Sistem tetap dapat beroperasi selama masih tersedia jalur alternatif yang menghubungkan titik asal dan tujuan.

---

#### 10.3 Skenario Perubahan Bobot Edge Akibat Kemacetan

##### a. Kondisi Awal

Waktu tempuh suatu jalan dapat berubah karena kemacetan, cuaca buruk, atau peningkatan volume kendaraan.

##### b. Dampak terhadap Graph

Karena Dijkstra menggunakan waktu tempuh sebagai bobot utama, perubahan bobot edge akan memengaruhi hasil pencarian rute.

Kemungkinan yang terjadi:

- Jalur tercepat sebelumnya tidak lagi optimal.
- Dijkstra akan memilih rute baru dengan total waktu lebih kecil.

##### c. Dampak terhadap MinHeap

Tidak ada dampak langsung terhadap MinHeap karena prioritas order tidak bergantung pada bobot edge.

##### d. Kesimpulan

Sistem mampu beradaptasi terhadap perubahan kondisi lalu lintas selama data bobot edge diperbarui.

---

#### 10.4 Skenario Node Tidak Ditemukan

##### a. Kondisi Awal

Pengguna memasukkan ID node yang tidak terdapat dalam graph.

Contoh:

```text
R999
C999
```

##### b. Dampak terhadap Graph

Operasi pencarian rute maupun BFS tidak dapat dilakukan karena node sumber atau tujuan tidak tersedia.

##### c. Dampak terhadap MinHeap

Tidak ada pengaruh terhadap struktur heap.

##### d. Kesimpulan

Sistem harus melakukan validasi input terlebih dahulu dan menampilkan pesan kesalahan yang sesuai.

---

#### 10.5 Skenario Graph Tidak Terhubung

##### a. Kondisi Awal

Penutupan sejumlah jalan menyebabkan graph terpecah menjadi beberapa komponen yang tidak saling terhubung.

##### b. Dampak terhadap Graph

BFS akan menghasilkan:

```text
false
```

karena node tujuan tidak dapat dicapai.

Dijkstra akan menghasilkan:

```text
dist[endId] = Integer.MAX_VALUE
```

dan method mengembalikan:

```text
null
```

##### c. Dampak terhadap MinHeap

Order tidak hilang.

Order dapat dikembalikan ke heap untuk diproses ulang ketika jaringan jalan kembali terhubung.

##### d. Kesimpulan

Sistem mampu mendeteksi kondisi graph yang tidak terhubung dan mencegah pengiriman menggunakan rute yang tidak valid.

---

#### 10.6 Kesimpulan dari What-if Analysis

Berdasarkan berbagai skenario yang diuji, dapat disimpulkan bahwa:

1. Struktur Graph menjadi komponen yang paling berpengaruh terhadap performa ketika jumlah node meningkat.
2. MinHeap tetap stabil karena operasi utamanya hanya bergantung pada jumlah order.
3. Mekanisme `closedEdges` memungkinkan sistem beradaptasi terhadap penutupan jalan secara dinamis.
4. Perubahan bobot edge dapat langsung memengaruhi hasil optimasi rute tanpa perlu mengubah struktur graph.
5. Sistem mampu menangani kondisi error seperti node tidak ditemukan maupun graph yang tidak terhubung.
6. Secara keseluruhan, desain Graph + MinHeap yang digunakan cukup robust dan scalable untuk simulasi pengantaran makanan berbasis jaringan jalan.

### Kesimpulan
