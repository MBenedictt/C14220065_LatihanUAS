package mbenedictt.paba.latihanuas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.util.Date

class RegisterPage : AppCompatActivity() {
    val db = Firebase.firestore

    var DaftarUser = ArrayList<User>()
    lateinit var lvAdapter : ArrayAdapter<User>

    lateinit var _etNama : EditText
    lateinit var _etNoHP : EditText
    lateinit var _etPassword : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _etNama = findViewById(R.id.etNama)
        _etNoHP = findViewById(R.id.etNoHP)
        _etPassword = findViewById(R.id.etPassword)
        val _btnRegister = findViewById<Button>(R.id.btnRegister)
        val _btnLogin = findViewById<Button>(R.id.btnLogin)

        _btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        _btnRegister.setOnClickListener {
            tambahData(db, _etNama.text.toString(), _etNoHP.text.toString(), _etPassword.text.toString())
        }

        lvAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            DaftarUser
        )
    }

    fun tambahData(db: FirebaseFirestore, Nama: String, NoHp: String, Password: String) {
        val dataBaru = User(Nama, NoHp, Password, Date())
        db.collection("tbUser")
            .document(dataBaru.nama)
            .set(dataBaru)
            .addOnSuccessListener {
                _etNama.setText("")
                _etNoHP.setText("")
                _etPassword.setText("")
                Log.d("Firebase", "Data Berhasil Disimpan")
            }
            .addOnFailureListener {
                Log.d("Firebase", it.message.toString())
            }
    }
}