package com.example.covidtracker

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.CountryCodePicker
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import retrofit2.Call
import retrofit2.Response
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.lang.NumberFormatException
import java.lang.String.format
import java.text.NumberFormat
import javax.security.auth.callback.Callback
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    lateinit var countryCodePicker: CountryCodePicker
    lateinit var mtodaytotal: TextView
    lateinit var mtotal: TextView
    lateinit var country: String
    lateinit var mactive: TextView
    lateinit var mtodayactice: TextView
    lateinit var mrecovered: TextView
    lateinit var mtodayrecovered: TextView
    lateinit var mdeaths: TextView
    lateinit var mtodaydeaths: TextView
    lateinit var mfilter: TextView
    lateinit var spinner: Spinner

    val types = arrayOf("cases", "deaths", "recovered", "active")
    lateinit var modelClassList: List<ModelClass>
    lateinit var modelClassList2: List<ModelClass>

    lateinit var mpiechart: PieChart

    lateinit var recyclerView: RecyclerView
    lateinit var adapter : Adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner)
        countryCodePicker = findViewById(R.id.ccp)
        mtodayactice = findViewById(R.id.todayactive)
        mactive = findViewById(R.id.activecase)
        mdeaths = findViewById(R.id.totaldeaths)
        mtodaydeaths = findViewById(R.id.todaydeaths)
        mrecovered = findViewById(R.id.recoverdcases)
        mtodayrecovered = findViewById(R.id.todayrecoverd)
        mtotal = findViewById(R.id.totalcase)
        mtodaytotal = findViewById(R.id.todaytotal)
        mfilter = findViewById(R.id.filter)
        mpiechart = findViewById(R.id.piechart)
        recyclerView = findViewById(R.id.recyclerview)
        modelClassList = ArrayList<ModelClass>()
        modelClassList2 = ArrayList<ModelClass>()


        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter


        val apiInterface = ApiInterface.getApiInterface().getCountryData()
        apiInterface.enqueue(object : retrofit2.Callback<List<ModelClass>> {
            override fun onResponse(
                call: Call<List<ModelClass>>,
                response: Response<List<ModelClass>>
            ) {
                if (response.body() != null)
                    modelClassList2 = response.body()!!.toList()
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {

            }
        });

        adapter= Adapter(applicationContext, modelClassList2)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter

        countryCodePicker.setAutoDetectedCountry(true)
        country = countryCodePicker.selectedCountryName
        countryCodePicker.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener { func() })
        fetchData()
    }

    private fun func() {
        country = countryCodePicker.selectedCountryName
        fetchData()
    }


    fun fetchData() {
        ApiInterface.getApiInterface().getCountryData()
            .enqueue(object : retrofit2.Callback<List<ModelClass>> {
                override fun onResponse(
                    call: Call<List<ModelClass>>,
                    response: Response<List<ModelClass>>
                ) {
                    if (response.body() != null) {
                        modelClassList = response.body()!!.toList()
                    }
                    for (i: ModelClass in modelClassList) {
                        if (i.country.equals(country)) {
                            mactive.text = (i.active)
                            mtodaydeaths.text = (i.todayDeaths)
                            mtodayrecovered.text = (i.todayRecovered)
                            mtodaytotal.text = (i.todayCases)

                            mtotal.text = (i.cases)
                            mdeaths.text = (i.deaths)
                            mrecovered.text = (i.recovered)

                            updateGraph(i.cases, i.active, i.recovered, i.deaths)
                        }
                    }
                }

                override fun onFailure(call: Call<List<ModelClass>>, t: Throwable) {

                }
            });
    }

    private fun updateGraph(cases: String, active: String, recovered: String, deaths: String) {
        mpiechart.clearChart()
        mpiechart.addPieSlice(PieModel("Confirm", parseFloat(cases), Color.parseColor("#FF0701")))
        mpiechart.addPieSlice(PieModel("Confirm", parseFloat(active), Color.parseColor("#FF4CAF50")))
        mpiechart.addPieSlice(PieModel("Confirm", parseFloat(recovered), Color.parseColor("#FF38ACCD")))
        mpiechart.addPieSlice(PieModel("Confirm", parseFloat(deaths), Color.parseColor("#FFC47")))
        mpiechart.startAnimation()

    }
//    fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
//        val item : String
//        item = types[position]
//        mfilter.setText(item)
//        adapter.filter(item);
//    }


}
