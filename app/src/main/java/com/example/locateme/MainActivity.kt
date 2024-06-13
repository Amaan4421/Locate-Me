package com.example.locateme

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity()
{

    //elements from UI file
    private lateinit var tvLatitude: TextView
    private lateinit var tvLongitude: TextView
    private lateinit var btnLocate: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //declare permission value as 1(true)
    companion object
    {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    //method to show UI
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //text and buttons
        tvLatitude = findViewById(R.id.tvLatitude)
        tvLongitude = findViewById(R.id.tvLongitude)
        btnLocate = findViewById(R.id.btnLocateMe)


        //location getter
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        //button click event
        btnLocate.setOnClickListener {

            //checking user granted permission or not
            //if block will be implemented if user did not granted permission
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }//end of if
            //if user already granted location service
            else
            {

                //for debugging
                Log.d("Button", "Button clicked bu user!!!");

                //calling method
                getLocation()
            }//end of else
        }//end og btn click event
    }//end of on create method


    //method to locate user's device
    private fun getLocation()
    {
        //checking permission again to avoid security issues and exceptions
        //here if block will be implemented when user grants the permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)
        {
            //get last location of device
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        //change text of location info
                        tvLatitude.text = "Latitude: ${it.latitude}"
                        tvLongitude.text = "Longitude: ${it.longitude}"

                        //for debugging
                        Log.d("Latitude", "Device's Latitude: $tvLatitude")
                        Log.d("Longitude", "Device's Longitude: $tvLongitude")

                    } ?: run {
                        //if failed to locate user's device
                        Toast.makeText(this, "Unable to find location. Try again later.", Toast.LENGTH_SHORT).show()
                    }//end of run
                }//end of listener
        }//end of if
        else
        {
            //ask for permission when it is not already granted
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }//end of else
    }//end of if


    //override method calls to check user's permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLocation()
            }//end of if
            else
            {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }//end of else
        }//end of if
    }//end of override method
}//end of class
