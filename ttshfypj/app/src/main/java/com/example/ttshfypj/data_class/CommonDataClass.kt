package com.example.ttshfypj.data_class

import android.widget.ImageView

data class reminders(
    val MedicineImage: String,
    val medicineN: String,
    val MedicineTime: String,
    val MedicineDosage: String,
    val LastTaken: String,
    val docid: String
)

data class medications(
    val MedicineImage: String,
    val medicineN: String,
    val MedicineTime: String,
    val docid: String,
    val expirydate: String,
    val finish: String
)

data class generalhelpfirstlayer(
    val helptitle: String,
    val helpdescription: String,
    val helpimage: String,
    val docid: String
)

data class instruction(
    val instructiontitle: String,
    val instructiondesc: String,
    val instructionimage: String
)

data class generalinstruction(
    val instructiontitle: String,
    val instructiondesc: String,
    val instructionimage: String
)