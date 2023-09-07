package com.example.autoreportgenerator.utils

import com.example.autoreportgenerator.model.ScanData

interface OnClickListener {
    fun onItemClicked(scanData: ScanData)
}