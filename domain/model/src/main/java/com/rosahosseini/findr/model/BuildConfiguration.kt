package com.rosahosseini.findr.model

data class BuildConfiguration(
    val flickerApiKey: String,
    val isDebug: Boolean,
    val device: Device
)

enum class Device {
    ANDROID,
    IOS
}
