package com.example.pethelper

import com.google.type.DateTime

data class Schedule(
    var ID: String,
    var pet: String,
    var activity: String,
    var days: String,
    var time: String,
    var notify: Boolean
    )
