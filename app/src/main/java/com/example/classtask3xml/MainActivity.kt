package com.example.classtask3xml

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.classtask3xml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var hexText = s.toString()
                if (hexText.firstOrNull() != '#')
                    hexText = ("#" + hexText)
                if (hexText.length == 9 && isValidHexColor(hexText)) {
                    try {
                        val result = hexToRGBA(hexText)
                        binding.slider1.value = result.alpha.toFloat()
                        binding.slider2.value = result.red.toFloat()
                        binding.slider3.value = result.green.toFloat()
                        binding.slider4.value = result.blue.toFloat()

                        binding.view.setBackgroundColor(android.graphics.Color.parseColor(hexText))
                    } catch (e: Exception) {
                        Log.e("HexToRGBA", "Invalid color format", e)
                    }

                }
                else{
                    binding.view.setBackgroundColor(Color.BLACK)
                }
            }
        })

        val sliders = listOf(binding.slider1, binding.slider2, binding.slider3, binding.slider4)
        sliders.forEach { slider ->
            slider.addOnChangeListener { _, _, _ ->
                val hexColor = rgbaToHex(
                    alpha = binding.slider1.value.toInt(),
                    red = binding.slider2.value.toInt(),
                    green = binding.slider3.value.toInt(),
                    blue = binding.slider4.value.toInt()
                )
                binding.editTextText.setText(hexColor)
                binding.view.setBackgroundColor(android.graphics.Color.parseColor(hexColor))
            }
        }
    }
}

data class RGBA(val alpha: Int, val red: Int, val green: Int, val blue: Int)

fun hexToRGBA(hex: String): RGBA {
    var newHex = hex
    if (hex.firstOrNull() != '#')
        newHex = ("#" + hex)
    Log.d("HexToRGBA", "Converting Hex: $hex")

    val color = if (newHex.length == 7) {
        android.graphics.Color.parseColor(newHex)
    } else {
        android.graphics.Color.parseColor(newHex)
    }

    return RGBA(
        alpha = (color shr 24) and 0xFF,
        red = (color shr 16) and 0xFF,
        green = (color shr 8) and 0xFF,
        blue = color and 0xFF
    )
}

fun rgbaToHex(alpha: Int, red: Int, green: Int, blue: Int): String {
    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

fun isValidHexColor(hex: String): Boolean {
    return hex.matches(Regex("^#([0-9A-Fa-f]{6}|[0-9A-Fa-f]{8})$"))
}