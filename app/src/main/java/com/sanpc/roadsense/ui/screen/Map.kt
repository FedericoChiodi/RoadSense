package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.theme.RoadSenseTheme

@Composable
fun Map(context: Context, potholes: List<Pothole>, drops: List<Drop>) {
    val potholeIcon: Drawable? = context.getDrawable(R.drawable.pothole_icon)
    val dropIconStart: Drawable? = context.getDrawable(R.drawable.drop_icon_start)
    val dropIconEnd: Drawable? = context.getDrawable(R.drawable.drop_icon_end)


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}


@Preview
@Composable
fun MapPreview(){
    RoadSenseTheme {
    }
}