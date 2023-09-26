package com.example.presensi

//import kelas dan pustaka yang diperlukan
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.presensi.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
//    deklarasi variabel binding
    private lateinit var binding: ActivityMainBinding

//     Menentukan bahwa sebuah fungsi atau method hanya dapat dijalankan pada perangkat dengan versi Android N (API level 24) atau yang lebih tinggi.
    @RequiresApi(Build.VERSION_CODES.N)

    override fun onCreate(savedInstanceState: Bundle?) {

//      Menonaktifkan mode malam
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
//        Inisialisasi variabel binding menggunakan data binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            var selectedDate = ""
            var selectedTime = ""
            var selectedKehadiran = ""
            editTextKeterangan.setSingleLine(true)

//            Menangani perubahan tanggal pada calendarView
//            dan menyimpan tanggal yang dipilih dalam format tertentu
            calendarView.setOnDateChangeListener{_, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                selectedDate = dateFormatter.format(calendar.time)
            }

//            menyimpan waktu yang dipilih dalam format tertentu.
            presenceTimepicker.setOnTimeChangedListener{_, hourOfDay, minute ->
                val AM_PM = if (hourOfDay < 12) { "AM" } else { "PM" }
                val minute = if (minute < 10) { "0" + minute.toString() } else { minute }
                selectedTime = "$hourOfDay:$minute $AM_PM"
            }
//            Menginisialisasi spinner dengan daftar opsi kehadiran dari sumber daya (resources)
//            dan menghubungkannya dengan adapter.
            val kehadiran = resources.getStringArray(R.array.kehadiran)
            val adapterKehadiran = ArrayAdapter<String>(this@MainActivity, R.layout.spinner_item, kehadiran)
            spinnerKehadiran.adapter = adapterKehadiran

            spinnerKehadiran.onItemSelectedListener =
                object  : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        if (kehadiran[position] != "Hadir Tepat Waktu") {
                            editTextKeterangan.visibility = View.VISIBLE
                        } else {
                            editTextKeterangan.visibility = View.GONE
                        }
                        selectedKehadiran = kehadiran[position]
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                        TODO("Not yet implemented")
                    }
                }

//            tombol submit dan menampilkan pesan sesuai dengan informasi yang telah dimasukkan oleh pengguna
            btnSubmit.setOnClickListener {
                val keteranganKehadiran = editTextKeterangan.text.toString()
                if (selectedDate != "" && selectedTime != "") {
                    if (selectedKehadiran == "Hadir Tepat Waktu") {
                        Toast.makeText(this@MainActivity, "Presensi berhasil $selectedDate jam $selectedTime", Toast.LENGTH_LONG).show()
                    } else {
                        if (keteranganKehadiran != "") {
                            Toast.makeText(this@MainActivity, "$selectedKehadiran dengan keterangan $keteranganKehadiran.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MainActivity, "Keterangan tidak boleh kosong.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Harap melengkapi waktu presensi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}