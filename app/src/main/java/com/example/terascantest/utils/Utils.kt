package com.example.terascantest.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.terascantest.R

object Utils {
    fun loadFragment(activity: FragmentActivity?, fragment: Fragment) {
        activity?.let {
            val fragmentManager: FragmentManager = it.supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Replace the current fragment with the new one
            transaction.replace(R.id.fragmentContainer, fragment)

            // Commit the transaction
            transaction.commit()
        } ?: run {
            // Handle the case where the activity is null (optional, based on your requirements)
        }
    }
    fun retrieveIsLocked(context: Context,key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }
    @SuppressLint("CommitPrefEdits")
    fun storeValIsLocked(context: Context,key:String,value:Boolean){
        val sharedPreferences = context.getSharedPreferences("MY_PREFS", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun storePasswordValue(context: Context,key: String,value:String){
        val sharedPreferences = context.getSharedPreferences("PASSWORD_PREF", AppCompatActivity.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun retrievePasswordValue(context: Context,key: String):String{
        val sharedPreferences = context.getSharedPreferences("PASSWORD_PREF", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(key,"").toString()
    }
    @SuppressLint("CommitPrefEdits")
    fun storeQuestionValue(context: Context,key: String,value: String){
        val sharedPreferences = context.getSharedPreferences("QUESTION_PREF", AppCompatActivity.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun retrieveQuestionValue(context: Context,key:String):String{
        val sharedPreferences = context.getSharedPreferences("QUESTION_PREF", AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getString(key,"").toString()
    }

}