package com.example.covidtracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.NumberFormatException
import java.text.NumberFormat

class Adapter(var context: Context, var countryList: List<ModelClass>) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    var m : Int = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.layout_item, null, false)
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var obj = ModelClass()
        obj = countryList.get(position)

        if(m == 1){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(obj.todayCases)))
        }
        else if(m == 2){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(obj.recovered)))
        }
        else if(m == 3){
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(obj.deaths)))
        }
        else{
            holder.cases.setText(NumberFormat.getInstance().format(Integer.parseInt(obj.active)))
        }
        holder.cases.setText(obj.country)
    }

    override fun getItemCount(): Int {
        return countryList.size
    }

    fun filter(item: String) {
        if(item.equals("cases")){
            m = 1;
        }
        else if(item.equals("recovered")){
            m = 2;
        }
        else if(item.equals("deaths")){
            m = 3;
        }
        else {
            m = 4;
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val cases : TextView = v.findViewById(R.id.countryCase)
        val country : TextView = v.findViewById(R.id.textView_countryName)
    }

}