package test.projects.interview_weather.ui.forecast

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import test.projects.interview_weather.model.citiesModel

class ForecastViewModel : ViewModel() {

    var date = ""
    var temperature = ""
    var weatherIcon = ""
    var min_temperature = ""
    var max_temperΩature = ""

    var liveData = MutableLiveData<ArrayList<forecastModel>>()

    var newlist = arrayListOf<forecastModel>()

    fun add(forecast: forecastModel){
        newlist.add(forecast)
        liveData.value = newlist
    }


}