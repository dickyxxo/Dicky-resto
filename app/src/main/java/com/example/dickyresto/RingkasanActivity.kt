package com.example.dickyresto

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.NumberFormat
import java.util.Locale

class RingkasanActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ringkasan)

        val tvNama = findViewById<TextView>(R.id.tvNama)
        val tvMenu = findViewById<TextView>(R.id.tvMenu)
        val tvHarga = findViewById<TextView>(R.id.tvHarga)
        val tvJumlah = findViewById<TextView>(R.id.tvJumlah)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)

        val btnEdit = findViewById<Button>(R.id.btnEdit)

        val nama = intent.getStringExtra(KunciPesanan.NAMA)
        val menu = intent.getStringExtra(KunciPesanan.MENU)

        val jumlah = intent.getIntExtra(
            KunciPesanan.JUMLAH,
            0
        )

        val harga = intent.getIntExtra(
            KunciPesanan.HARGA,
            0
        )

        if (
            nama.isNullOrBlank() ||
            menu.isNullOrBlank() ||
            jumlah !in 1..100 ||
            harga <= 0
        ) {
            Toast.makeText(
                this,
                "Data pesanan tidak valid",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        val total = jumlah.toLong() * harga

        val rupiah = NumberFormat.getCurrencyInstance(
            Locale("id", "ID")
        ).apply {
            maximumFractionDigits = 0
        }

        tvNama.text = "Pelanggan: $nama"
        tvMenu.text = "Menu: $menu"
        tvHarga.text = "Harga satuan: ${rupiah.format(harga)}"
        tvJumlah.text = "Jumlah: $jumlah porsi"
        tvTotal.text = "Total: ${rupiah.format(total)}"

        btnEdit.setOnClickListener {
            finish()
        }
    }
}