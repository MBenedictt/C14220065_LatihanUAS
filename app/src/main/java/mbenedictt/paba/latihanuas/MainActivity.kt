package mbenedictt.paba.latihanuas

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etNoHP = findViewById<EditText>(R.id.etNoHP)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val _btnRegister = findViewById<Button>(R.id.btnRegister)

        _btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterPage::class.java))
        }

        btnLogin.setOnClickListener {
            val noHp = etNoHP.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (noHp.isNotEmpty() && password.isNotEmpty()) {
                validateLogin(noHp, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLogin(noHp: String, password: String) {
        db.collection("tbUser")
            .whereEqualTo("nohp", noHp)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                } else {
                    // Login successful, redirect to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    HomeActivity.nohp = noHp
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}