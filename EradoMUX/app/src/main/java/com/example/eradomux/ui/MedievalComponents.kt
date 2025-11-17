package com.example.eradomux.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CampoMedieval(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            color = CorTextoBranco,
            fontSize = 14.sp, // Fonte um pouco menor para caber melhor
            fontFamily = FontFamily.Serif
        ),
        cursorBrush = SolidColor(CorDourado),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp) // <--- REDUZIDO DE 45.dp PARA 40.dp
                    .background(CorVermelhoEscuro, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, CorDourado.copy(alpha = 0.5f), shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray,
                        fontFamily = FontFamily.Serif,
                        style = TextStyle(fontSize = 14.sp) // Fonte menor
                    )
                }
                innerTextField()
            }
        }
    )
}

@Composable
fun BotaoMedieval(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CorDourado.copy(alpha = 0.3f)),
        modifier = Modifier.height(38.dp) // <--- REDUZIDO DE 40.dp PARA 38.dp
    ) {
        Text(
            text = text,
            color = CorDourado,
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp
        )
    }
}