package com.example.zvonimir.valuteexchanger

import retrofit2.http.GET
import io.reactivex.Observable

interface RestInterface {
    @GET("/api/v1/rates/daily/?date=YYYY-MM-DD ") fun getCurrency():Observable<CurrencyList>
}