package com.example.autoreportgenerator.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.autoreportgenerator.R
import com.example.autoreportgenerator.model.ScanData
import com.example.autoreportgenerator.utils.OnClickListener

class ScanAdapter(
    private val lists: ArrayList<ScanData>,
    private val btnListener: OnClickListener
) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {
    companion object {
        var mClickListener: OnClickListener? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan, parent, false)
        return ScanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        mClickListener = btnListener
        val scanData = lists[position]
        holder.scannedOn.text = scanData.scannedOn ?: "-"
        holder.patientName.text = scanData.patientName ?: "-"
        holder.age.text = scanData.patientAge ?: "-"

        holder.viewReport.setOnClickListener {

            /*val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(scanData.pdfUrl))
            holder.itemView.context.startActivity(browserIntent)*/
        }
        holder.itemView.setOnClickListener {
            mClickListener?.onItemClicked(scanData)
        }
    }

    class ScanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val patientName = view.findViewById<TextView>(R.id.patient_name)
        val age = view.findViewById<TextView>(R.id.patient_age)
        val scannedOn = view.findViewById<TextView>(R.id.tv_scanned_on)
        val viewReport = view.findViewById<Button>(R.id.btn_report)
    }
}