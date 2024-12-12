package mbenedictt.paba.latihanuas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import mbenedictt.paba.latihanuas.database.daftarAir

class AdapterDaftar (private val daftarAir: MutableList<daftarAir>) : RecyclerView.Adapter<AdapterDaftar.ListViewHolder>() {

    private lateinit var onItemClickCallback : OnItemClickCallback

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvNama = itemView.findViewById<TextView>(R.id.tvNama)
        var _tvBerat = itemView.findViewById<TextView>(R.id.tvBerat)
        var _tvAir = itemView.findViewById<TextView>(R.id.tvAir)

        var _btnSend = itemView.findViewById<Button>(R.id.btnSend)
    }

    interface OnItemClickCallback {
        fun sendData(dtAir: daftarAir)
        fun delData(dtAir: daftarAir)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_adapter_daftar, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daftarAir.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var daftar = daftarAir[position]
        holder._tvNama.setText(daftar.nama)
        holder._tvBerat.setText(daftar.berat)
        holder._tvAir.setText(daftar.air)

        holder._btnSend.setOnClickListener {
            onItemClickCallback.sendData(daftar)
        }
    }

    fun isiData(daftar: List<daftarAir>){
        daftarAir.clear()
        daftarAir.addAll(daftar)
        notifyDataSetChanged()
    }

}