package test.projects.interview_weather.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import test.projects.interview_weather.R
import test.projects.interview_weather.data.cloud.FavoriteDataModel
import test.projects.interview_weather.databinding.FragmentDetailedBinding
import test.projects.interview_weather.ui.favorite.FavoriteViewModel
import test.projects.interview_weather.ui.forecast.ForecastAdapter
import test.projects.interview_weather.ui.forecast.ForecastViewModel
import test.projects.interview_weather.ui.forecast.forecastWeather
import test.projects.interview_weather.ui.user.SignInDialog
import test.projects.interview_weather.utils.*
import java.text.DateFormat
import java.util.*

class DetailedFragment : Fragment(R.layout.fragment_detailed) {

    //view_model
    lateinit var viewModel: FavoriteViewModel
    lateinit var forecastViewModel: ForecastViewModel


    //view binding
    private var _binding: FragmentDetailedBinding? = null
    private val binding get() = _binding!!


    //shared resources
    var data_city: String = ""
    var data_description: String = ""
    var data_icon: String = ""
    var data_temperature: String = ""
    var data_wind_speed: String = ""
    var data_water_drop: String = ""
    var data_min: String = ""
    var data_max: String = ""
    var data_lat: String = ""
    var data_long: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //entrace animation
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)

        //get favorite status
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
      /*  viewModel.currentIcon.observe(this, androidx.lifecycle.Observer  {
            favBtn.setImageResource(it)
        })*/

        forecastViewModel = ViewModelProvider(this).get(ForecastViewModel::class.java)

        activity?.window?.statusBarColor= getResources().getColor(R.color.black)

        val bundle = this.arguments
        if (bundle != null) {
            //get city details
            data_city = bundle.getString("city").toString()
            data_description = bundle.getString("description").toString()
            data_icon = bundle.getString("icon").toString()
            data_temperature = bundle.getString("temperature").toString()
            data_wind_speed = bundle.getString("wind_speed").toString()
            data_water_drop = bundle.getString("water_drop").toString()
            data_min = bundle.getString("min_temp").toString()
            data_max = bundle.getString("max_temp").toString()
            data_lat = bundle.getString("lat").toString()
            data_long = bundle.getString("long").toString()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View{
        // Inflate the layout for this fragment

        _binding = FragmentDetailedBinding.inflate(inflater, container, false)

        ViewCompat.setTransitionName(binding.temperature, "image")

        //request data from api
        context?.let { forecastWeather(forecastViewModel, it).getForecast(binding.progressBar,data_lat,data_long) }

        //variables
        initUI()

        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun initUI() {

        binding.city.text = data_city
        binding.status.text = data_description
        binding.temperature.text = "$data_temperature\t°"
        binding.waterDrop.text = data_water_drop
        binding.windSpeed.text = data_wind_speed
        binding.minTemp.text = "Min:\t $data_min°"
        binding.maxTemp.text = "Max:\t $data_max°"

        binding.forecastTitle.text = "$data_city, forecast."


        //set background colorScheme
        binding.cardView.setBackgroundResource(BackgroundManager().getBackground(data_description))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.cardView.outlineAmbientShadowColor = IconManager().getColor(data_description)
            binding.cardView.outlineSpotShadowColor = IconManager().getColor(data_description)
        }

        context?.let {
            Glide.with(it)
                .load(IconManager().getIcon(data_icon))
                .into(binding.weatherIcon)
        }

        val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        binding.time.text = currentDateTimeString

        //check if city exists on my favorite db
        FavoriteDataModel(viewModel, context, binding.progressBar).checkCityExistence(data_city)


        getWeatherForecast()


        binding.arrowBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStackImmediate()
        }


    //    Animator().animate(binding.favBtn,1.0f,1.2f,1100)
        binding.weatherIcon.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in))

        //get currentUser
        val user = Firebase.auth.currentUser

        //on favBtn clicked
       /* binding.favBtn.setOnClickListener {

            if (user != null){

                if(viewModel.favIcon == R.drawable.baseline_favorite_24){
                    viewModel.currentIcon.value =
                        context?.let { it1 -> FavoriteDataModel(viewModel, it1, binding.progressBar).deleteCity(data_city, it1) }
                }else{
                    viewModel.currentIcon.value =
                        context?.let { it1 -> FavoriteDataModel(viewModel, it1, binding.progressBar).addCity(data_city, it1) }
                }
            }else{
                //show sing up dialog
                activity?.let { it1 -> SignInDialog().show(it1.supportFragmentManager, "signin_diag") }
            }

        }*/

        Animator().animate(binding.imageViewBigCloud,0.5f,1.3f,10000)

    }



    //get weather forecast
    private fun getWeatherForecast(){
        forecastViewModel.liveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer  {

            //recyclerView
            binding.detailedCityRecyclerview.layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL ,false)

            val adapter = context?.let { ForecastAdapter(forecastViewModel.newlist, it) }
            binding.detailedCityRecyclerview.adapter = adapter

            binding.detailedCityRecyclerview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.recycler_view_anim))

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}