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

### Design Decision Log

### Tracing

### Screenshot Hasil Program

### Analisis Kompleksitas

### What-if analysis

### Kesimpulan
