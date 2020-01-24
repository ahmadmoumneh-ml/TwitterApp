package ca.mobilelive.twitterapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionValidator {

    const val REQUEST_PERMISSION_CODE = 100

    fun requestPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_PERMISSION_CODE
        )
    }

    fun isLocationPermissionAllowed(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isInternetPermissionAllowed(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isAllPermissionsAllowed(context: Context): Boolean {
        var isAllowed = true
        arrayOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).forEach {
            if (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED)
                isAllowed = false
        }
        return isAllowed
    }
}