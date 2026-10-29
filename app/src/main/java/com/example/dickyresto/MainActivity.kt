package com.example.dickyresto

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val daftarMenu = listOf(
        MenuMakanan("Nasi Goreng", 15_000),
        MenuMakanan("Mie Goreng", 13_000),
        MenuMakanan("Ayam Geprek", 18_000),
        MenuMakanan("Soto Ayam", 16_000),
        MenuMakanan("Bakso", 14_000),
        MenuMakanan("Sate Ayam", 20_000)


    )

    private var posisiPilihan = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        val tvMenuDipilih =
            findViewById<TextView>(R.id.tvMenuDipilih)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etJumlah = findViewById<EditText>(R.id.etJumlah)

        val btnPesan = findViewById<Button>(R.id.btnPesan)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val rupiah = NumberFormat.getCurrencyInstance(
            Locale("id", "ID")
        ).apply {
            maximumFractionDigits = 0
        }

        // Ubah data menu menjadi teks untuk setiap baris.
        val teksMenu = daftarMenu.map { menu ->
            "${menu.nama} - ${rupiah.format(menu.harga)}"
        }

        // Hubungkan data dengan layout pilihan tunggal bawaan.
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_single_choice,
            teksMenu
        )

        lvMenu.adapter = adapter

        // Pulihkan pilihan jika Activity dibuat ulang.
        posisiPilihan = savedInstanceState
            ?.getInt("posisi_pilihan", -1) ?: -1

        if (posisiPilihan in daftarMenu.indices) {
            lvMenu.setItemChecked(posisiPilihan, true)

            val menu = daftarMenu[posisiPilihan]
            tvMenuDipilih.text =
                "Dipilih: ${menu.nama} " +
                        "(${rupiah.format(menu.harga)})"
        }

        // Tangani klik pada menu.
        lvMenu.setOnItemClickListener { _, _, position, _ ->
            posisiPilihan = position

            val menu = daftarMenu[position]

            tvMenuDipilih.text =
                "Dipilih: ${menu.nama} " +
                        "(${rupiah.format(menu.harga)})"
        }

        btnPesan.setOnClickListener {
            etNama.error = null
            etJumlah.error = null

            // Periksa apakah pengguna sudah memilih menu.
            if (posisiPilihan !in daftarMenu.indices) {
                Toast.makeText(
                    this,
                    "Pilih menu terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val nama = etNama.text.toString().trim()
            val jumlah = etJumlah.text.toString()
                .trim()
                .toIntOrNull()

            if (nama.isBlank()) {
                etNama.error = "Masukkan nama pelanggan"
                etNama.requestFocus()
                return@setOnClickListener
            }

            if (jumlah == null || jumlah !in 1..100) {
                etJumlah.error = "Jumlah harus 1–100 porsi"
                etJumlah.requestFocus()
                return@setOnClickListener
            }

            val menu = daftarMenu[posisiPilihan]

            val tujuan = Intent(
                this,
                RingkasanActivity::class.java
            )

            tujuan.putExtra(KunciPesanan.NAMA, nama)
            tujuan.putExtra(KunciPesanan.JUMLAH, jumlah)
            tujuan.putExtra(KunciPesanan.MENU, menu.nama)
            tujuan.putExtra(KunciPesanan.HARGA, menu.harga)

            startActivity(tujuan)
        }

        btnReset.setOnClickListener {
            etNama.text.clear()
            etJumlah.text.clear()

            etNama.error = null
            etJumlah.error = null

            posisiPilihan = -1
            lvMenu.clearChoices()

            // Perbarui tampilan tanda pilihan.
            adapter.notifyDataSetChanged()

            tvMenuDipilih.text = "Menu belum dipilih"
            etNama.requestFocus()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("posisi_pilihan", posisiPilihan)
        super.onSaveInstanceState(outState)
    }
}