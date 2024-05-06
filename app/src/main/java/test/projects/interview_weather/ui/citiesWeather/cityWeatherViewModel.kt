package test.projects.interview_weather.ui.citiesWeather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import test.projects.interview_weather.model.citiesModel

class cityWeatherViewModel : ViewModel() {

    var city = ""
    var icon = ""
    var description = ""
    var temperature = ""
    var wind_speed = ""
    var water_drop = ""
    var min_temp = ""
    var max_temp = ""
    var lat = ""
    var long = ""

    var isCityFav : Boolean = false


    var liveData = MutableLiveData<ArrayList<citiesModel>>()
    var newlist = arrayListOf<citiesModel>()

    fun add(city: citiesModel){
        newlist.add(city)
        liveData.value = newlist
    }


}