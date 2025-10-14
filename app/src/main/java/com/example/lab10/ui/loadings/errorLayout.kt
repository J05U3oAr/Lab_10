package com.example.lab10.ui.loadings

//Librerias importadas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

//Funcion de layout de error.
@Composable
fun ErrorCard(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.error
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Icono dentro de círculo, con fondo sutil
            Surface(
                shape = CircleShape,
                color = tint.copy(alpha = 0.12f),
                contentColor = tint
            ) {
                Icon(
                    imageVector = Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = tint,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onRetry,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.2.dp, tint),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = tint)
            ) {
                Text("Reintentar")
            }
        }
    }
}