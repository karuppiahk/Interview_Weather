package test.projects.interview_weather.ui.favorite

import android.content.Context
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import test.projects.interview_weather.api.WeatherApi
import test.projects.interview_weather.model.citiesModel
import test.projects.interview_weather.model.favoriteModel
import test.projects.interview_weather.ui.citiesWeather.cityWeatherViewModel
import test.projects.interview_weather.utils.Convert

class favoriteCitiesWeather(val viewModel: FavoriteViewModel, val cities: List<String?>) {

    fun showCitiesWeather(context: Context?, favprogressBar: ProgressBar?){

        favprogressBar?.isVisible = true

        //loop through cities
        for (city in cities) {

            val queue = Volley.newRequestQueue(context)
            val jsonRequest = JsonObjectRequest(
                Request.Method.GET, city?.let { WeatherApi().getCitiesWeather(it) },null, { response ->

                    favprogressBar?.isVisible = false

                    //city
                    viewModel.city = response.getString("name")

                    //description
                    viewModel.description = response.getJSONArray("weather").getJSONObject(0).getString("main")

                    //icon
                    viewModel.icon = response.getJSONArray("weather").getJSONObject(0).getString("icon")

                    //temperature
                    viewModel.temperature = Convert().convertTemp(response.getJSONObject("main").getString("temp"))

                    //water drop and wind speed
                    viewModel.water_drop = response.getJSONObject("main").getString("humidity")+"\t%"
                    viewModel.wind_speed = response.getJSONObject("wind").getString("speed")+"\tkm/h"


                    //get lat and long
                    viewModel.lat = response.getJSONObject("coord").getString("lat").toString()
                    viewModel.long = response.getJSONObject("coord").getString("lon").toString()


                    //max and min temperature
                    viewModel.min_temp = Convert().convertTemp(response.getJSONObject("main").getString("temp_min"))
                    viewModel.max_temp = Convert().convertTemp(response.getJSONObject("main").getString("temp_max"))


                    val model = favoriteModel(
                        viewModel.city, viewModel.temperature,
                        viewModel.icon, viewModel.description,
                        viewModel.wind_speed, viewModel.water_drop,
                        viewModel.min_temp, viewModel.max_temp,
                        viewModel.lat, viewModel.long
                    )

                    viewModel.add(model)

                }, {
                    Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show()
                    favprogressBar?.isVisible = false
                })
            queue.add(jsonRequest)

        }
    }

}