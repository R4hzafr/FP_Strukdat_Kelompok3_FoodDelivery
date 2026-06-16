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

### Struktur Graph yang digunakan

### Struktur Tree yang digunakan

### Algoritma yang digunakan

### Design Decision Log

### Tracing

### Screenshot Hasil Program

### Analisis Kompleksitas

### What-if analysis

### Kesimpulan
