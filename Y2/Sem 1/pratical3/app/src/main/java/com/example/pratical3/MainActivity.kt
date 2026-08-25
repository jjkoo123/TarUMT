package com.example.pratical3

import android.R.attr.onClick
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pratical3.ui.theme.Pratical3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pratical3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                }
            }
        }
    }
}

@Composable
fun BmiCalculator(modifier: Modifier = Modifier) {
    Text(text = "BMI Calculator")
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Height") },
        modifier = Modifier.padding(8.dp)
    )
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = "Weight") },
        modifier = Modifier.padding(8.dp)
    )

    Button(onClick = {}){
        Text(text = "Calculate BMI")
    }

    //card(){
    //Text(text = "Body Mass Index")

}