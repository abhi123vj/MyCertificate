package com.abhi.mycertificate.models

import com.abhi.mycertificate.Model

/**
 * Created by Shahbaz Hashmi on 26/04/19.
 */
data class Subject(val name : Model, var yearList : MutableList<Year>?) {}