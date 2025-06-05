package com.example.grumblehub.sharedviewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.grumblehub.features.grievance.data.Grievance

class GrievanceSharedViewModel: ViewModel() {
    var selectedGrievance by mutableStateOf<Grievance?>(null)
}