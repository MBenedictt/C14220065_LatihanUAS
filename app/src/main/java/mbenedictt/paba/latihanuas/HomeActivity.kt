package mbenedictt.paba.latihanuas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mbenedictt.paba.latihanuas.database.daftarAir
import mbenedictt.paba.latihanuas.database.daftarAirDB
import java.util.Date

class HomeActivity : AppCompatActivity() {
    val db = Firebase.firestore

    private lateinit var DBAir : daftarAirDB
    private lateinit var adapterDaftar : AdapterDaftar
    private var arDaftar : MutableList<daftarAir> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DBAir = daftarAirDB.getDatabase(this)

        val _btnAdd = findViewById<Button>(R.id.btnAdd)
        val _etNama = findViewById<EditText>(R.id.etNama)
        val _etBerat = findViewById<EditText>(R.id.etBerat)
        val _btnStored = findViewById<Button>(R.id.btnStored)

        _btnStored.setOnClickListener {
            StoredActivity.nohp = nohp
            startActivity(Intent(this, StoredActivity::class.java))
        }

        _btnAdd.setOnClickListener {
            val hasil = hitungAir(_etBerat.text.toString().toInt())
            CoroutineScope(Dispatchers.IO).async {
                DBAir.fundaftarAirDAO().insert(
                    daftarAir(
                        nama = _etNama.text.toString(),
                        berat = _etBerat.text.toString(),
                        air = hasil.toString()
                    )
                )
                runOnUiThread {
                    // Restart the current activity
                    finish()
                    startActivity(intent)
                }
            }
        }

        adapterDaftar = AdapterDaftar(arDaftar)

        var _rvListBerat = findViewById<RecyclerView>(R.id.rvListBerat)
        _rvListBerat.layoutManager = LinearLayoutManager(this)
        _rvListBerat.adapter = adapterDaftar

        adapterDaftar.setOnItemClickCallback(
            object : AdapterDaftar.OnItemClickCallback {
                override fun delData(dtAir: daftarAir) {
                    CoroutineScope(Dispatchers.IO).async {
                        DBAir.fundaftarAirDAO().delete(dtAir)
                        val daftar = DBAir.fundaftarAirDAO().selectAll()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

                override fun sendData(dtAir: daftarAir) {
                    delData(dtAir)

                    tambahDataHasil(db, nohp, dtAir.nama.toString(), dtAir.berat.toString(), dtAir.air.toString())
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarAir = DBAir.fundaftarAirDAO().selectAll()
            Log.d("data ROOM", daftarAir.toString())
            adapterDaftar.isiData(daftarAir)
        }
    }

    private fun hitungAir(berat: Int): Double {
        return when {
            berat <= 10 -> 1.0 // 1 liter untuk 10 kg pertama
            berat <= 20 -> 1.0 + 0.5 // Tambah 0.5 liter untuk 10 kg kedua
            else -> 1.0 + 0.5 + (berat - 20) * 0.02 // Tambah 0.02 liter untuk sisa kg
        }
    }

    fun tambahDataHasil(db: FirebaseFirestore, NoHp: String, Nama: String, Berat: String, jumlahAir: String) {
        val dataBaru = Hasil(NoHp, Nama, Berat, jumlahAir)
        db.collection("tbHasil")
            .document()
            .set(dataBaru)
            .addOnSuccessListener {
                Log.d("Firebase", "Data Berhasil Disimpan")
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    companion object {
        var nohp: String = ""
    }
}