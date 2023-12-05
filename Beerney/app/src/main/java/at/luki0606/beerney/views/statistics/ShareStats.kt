package at.luki0606.beerney.views.statistics

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import at.luki0606.beerney.models.BeerRepository
import at.luki0606.beerney.ui.theme.Alabaster
import at.luki0606.beerney.ui.theme.Ebony

@Composable
fun ShareStats(){
    val shareText by remember { mutableStateOf(TextFieldValue("Check out my Beerney stats:\n\nI have drunk ${BeerRepository.getBeerCount().toString()}  \uD83C\uDF7B since I use this app!")) }
    val context = LocalContext.current
    val shareLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, "Shared data successfully", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "Sharing failed", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .clickable {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, shareText.text)
                    putExtra(Intent.EXTRA_SUBJECT, "Beerney stats")
                    putExtra(Intent.EXTRA_TITLE, "Check out my Beerney stats!")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Share Beerney stats")
                shareLauncher.launch(shareIntent)
            }
            .background(Ebony, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = Alignment.Center,
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = "Share stats",
                tint = Alabaster
            )
            Text(
                text = "Share your stats!",
                color = Alabaster
            )
        }
    }
}