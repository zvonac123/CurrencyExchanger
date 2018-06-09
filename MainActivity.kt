package com.example.zvonimir.valuteexchanger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    val retrofit: Retrofit=Retrofit.Builder().baseUrl("http://hnbex.eu").addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    var amount: Double=0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val input: EditText = findViewById(R.id.EditText)
        val output: TextView = findViewById(R.id.Result)
        val currencyTo: Spinner = findViewById(R.id.SpinnerTo)
        val button: Button = findViewById(R.id.SubmitButton)
        val spinnerFrom: Spinner = findViewById(R.id.SpinnerFrom)
        val spinnerTo: Spinner = findViewById(R.id.SpinnerTo)
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.Currency,R.layout.support_simple_spinner_dropdown_item)
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnerFrom.adapter=adapter
        spinnerTo.adapter=adapter
        button.setOnClickListener({view ->
            amount=input.text.toString().toDouble()
            search(currencyTo?.selectedItem.toString())
            output.text=amount.toString()
        })
    }

    private fun search(to: String){
        val rest = retrofit.create(RestInterface::class.java)
        var currencyList: CurrencyList?=null
        rest.getCurrency().subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    currencyList?.currencies=it.currencies
                },{
                    Toast.makeText(this,"Operation failed!",Toast.LENGTH_SHORT).show()
                })
        for(i in 0 until currencyList?.currencies!!.size){
            if(currencyList?.currencies?.get(i)!!.equals(to)){
                amount*=currencyList?.currencies?.get(i)!!.median
                break
            }
        }
    }
}
