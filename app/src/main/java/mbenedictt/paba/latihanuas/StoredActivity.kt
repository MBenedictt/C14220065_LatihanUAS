package mbenedictt.paba.latihanuas

import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class StoredActivity : AppCompatActivity() {
    val db = Firebase.firestore

    var DataHasil = ArrayList<Hasil>()
    lateinit var lvAdapter : SimpleAdapter

    var data: MutableList<Map<String, String>> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stored)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        readData(db)

        val _lvData = findViewById<ListView>(R.id.lvData)

        lvAdapter = SimpleAdapter(
            this,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("Nama", "Hasil"),
            intArrayOf(
                android.R.id.text1,
                android.R.id.text2
            )
        )
        _lvData.adapter = lvAdapter
    }

    fun readData(db: FirebaseFirestore) {
        // Filter data with Firestore query where "nohp" matches StoredActivity.nohp
        db.collection("tbHasil")
            .whereEqualTo("nohp", nohp) // Filter condition
            .get()
            .addOnSuccessListener { result ->
                DataHasil.clear()
                data.clear()
                for (document in result) {
                    val readData = Hasil(
                        document.data["nohp"].toString(),
                        document.data["nama"].toString(),
                        document.data["berat"].toString(),
                        document.data["jumlahAir"].toString()
                    )
                    DataHasil.add(readData)

                    // Prepare data for the SimpleAdapter
                    val dt: MutableMap<String, String> = HashMap(2)
                    dt["Nama"] = readData.nama
                    dt["Hasil"] = readData.jumlahAir
                    data.add(dt)
                }
                lvAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }

    companion object {
        var nohp: String = ""
    }
}