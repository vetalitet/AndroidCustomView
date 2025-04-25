package com.example.customviewsproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewsproject.databinding.ActivityMainBinding
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var car: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSomeComponent.builder().action("Hello world!").build().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonStartLIDL.setOnClickListener {
            startActivity(Intent(this, LidlActivity::class.java))
        }

        car.aaa()
    }

}

interface Car {
    fun aaa()
}

class CarImpl @Inject constructor(
    @Named("action") val action: String
) : Car {
    override fun aaa() {
        Log.d("AAAAA", "Action: $action")
    }
}

@Module
interface CarModule {
    @Binds
    fun bindCar(car: CarImpl): Car
}

@Component(modules = [CarModule::class])
interface SomeComponent {
    @Component.Builder
    interface Builder {
        fun build(): SomeComponent

        @BindsInstance
        fun action(@Named("action") act: String): Builder
    }
    fun inject(activity: MainActivity)
}
