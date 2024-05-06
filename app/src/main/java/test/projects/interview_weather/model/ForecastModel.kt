package test.projects.interview_weather.ui.forecast


data class forecastModel(val dayOfTheWeek: String,
                         val temperature: String,
                         val icon: String, val min_temp: String,
                         val max_temp: String)