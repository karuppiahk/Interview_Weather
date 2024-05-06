package test.projects.interview_weather.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import test.projects.interview_weather.R
import test.projects.interview_weather.databinding.FragmentHomeBinding
import test.projects.interview_weather.databinding.FragmentHomeBinding.*
import test.projects.interview_weather.model.citiesModel
import test.projects.interview_weather.ui.citiesWeather.*
import test.projects.interview_weather.ui.currentWeather.CurrentWeather
import test.projects.interview_weather.ui.currentWeather.CurrentWeatherViewModel
import test.projects.interview_weather.ui.favorite.FavoriteViewModel
import test.projects.interview_weather.utils.*
import kotlinx.android.synthetic.main.fragment_home.*
import test.projects.interview_weather.databinding.ActivityMainBinding
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home), cityDetailInterface {

    //model
    lateinit var currWViewModel: CurrentWeatherViewModel
    lateinit var viewModel: cityWeatherViewModel
    lateinit var favViewModel: FavoriteViewModel

    //view binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //location shared SharedPreferences
    var pref: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    //adapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = context?.getSharedPreferences("location", Context.MODE_PRIVATE)
        editor = pref?.edit()

        // Cities weather data
        viewModel = ViewModelProvider(this).get(cityWeatherViewModel::class.java)
        favViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        //current weather data
        currWViewModel = ViewModelProvider(this).get(CurrentWeatherViewModel::class.java)
        currWViewModel.WeatherService.observe(this, androidx.lifecycle.Observer {

            //set data
            homeCity.text = currWViewModel.city

            Glide.with(this)
                .load(IconManager().getIcon(currWViewModel.icon))
                .into(homeWeatherIcon)

            homeStatus.text = currWViewModel.description

            homeTemperature.text = currWViewModel.temperature

            homeWater_Drop.text = currWViewModel.water_drop

            homeWind_Speed.text = currWViewModel.wind_speed

            homeFragmentBG.setBackgroundResource(BackgroundManager().getHomeBackground(currWViewModel.description))

        })


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout for this fragment
        _binding = inflate(inflater, container, false)

        ViewCompat.setTransitionName(binding.homeTemperature, "item_image")



        //get data for citiesModel
        getCitiesWeather()

        return binding.root
    }

    @SuppressLint("RtlHardcoded", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latitude = pref?.getString("latitude", "").toString()
        val longitude = pref?.getString("longitude", "").toString()

        //get weather data
        context?.let { CurrentWeather(currWViewModel,binding.currentLayout, binding.homeProgressBar).showCurrentLocationData(it,latitude,longitude) }
        cityWeather(viewModel).showCitiesWeather(context)





        //on city clicked
        homeCity.setOnClickListener {
            showCurrentLocationForecast()
        }

        //fav btn


        val user = Firebase.auth.currentUser
        val name = user?.displayName.toString()



    }


    //show current forecast
    private fun showCurrentLocationForecast() {
        onItemClick(
            currWViewModel.city, currWViewModel.icon,
            currWViewModel.description, currWViewModel.temperature,
            currWViewModel.wind_speed, currWViewModel.water_drop, currWViewModel.min_temp,
            currWViewModel.max_temp, currWViewModel.lat, currWViewModel.long)
    }


    //get cities weather
    private fun getCitiesWeather() {

        viewModel.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer  {



            //list of favorite cities
            val db = Firebase.firestore
            val user = Firebase.auth.currentUser
            val email = user?.email.toString()

            val docRef = db.collection("cities").document(email)


        })

    }







    override fun onItemClick(city: String, icon: String, description: String, temperature: String,
                             wind_speed: String, water_drop: String, min_temp: String,
                             max_temp: String, lat: String, long: String) {

        val detailedFragment = DetailedFragment()
        val bundle = Bundle()
        bundle.putString("city", city)
        bundle.putString("description", description)
        bundle.putString("icon", icon)
        bundle.putString("temperature", temperature)
        bundle.putString("wind_speed", wind_speed)
        bundle.putString("water_drop", water_drop)
        bundle.putString("min_temp", min_temp)
        bundle.putString("max_temp", max_temp)
        bundle.putString("lat", lat)
        bundle.putString("long", long)

        detailedFragment.arguments = bundle

        (activity as MainActivity?)?.showDetailedWeather(detailedFragment,bundle,binding.homeTemperature)

    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}