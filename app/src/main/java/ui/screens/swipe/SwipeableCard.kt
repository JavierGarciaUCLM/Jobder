package ui.screens.swipe

import android.content.Intent
import android.gesture.Gesture
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.jobder.Augmented_Reality_Activity
import com.example.jobder.R
import com.example.jobder.SharedState
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.gesturerecognizer.GestureRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ui.screens.SharedSwipes

enum class SwipeDirection {
    DEFAULT, ACCEPTED, REJECTED
}
class SwipeableCard:ComponentActivity(){
    var showYes = mutableStateOf(false)
    var showNope = mutableStateOf(false)
    var offsetX = mutableStateOf(0f)
    var swipeThreshold = 20f // Reducir el umbral para detectar el gesto de deslizamiento
    private val companies = mutableListOf(
        R.drawable.company1,
        R.drawable.company2,
        R.drawable.company3,
        R.drawable.company4,
        R.drawable.company5
    )
    private val personas =
        mutableStateListOf(
            R.drawable.persona1,
            R.drawable.persona2,
            R.drawable.persona5,
            R.drawable.persona4,
            R.drawable.persona3,
        )

    fun removeCompany() {
        if(SharedSwipes.isPersona)
            if(personas.isNotEmpty())
                personas.removeAt(0)
        if(!SharedSwipes.isPersona)
            if(companies.isNotEmpty())
                companies.removeAt(0)
    }
    fun pickCompanyOrPerson(): Int {
        if(SharedSwipes.isPersona)
            if(personas.isNotEmpty())
                return personas.first()
        if(personas.isEmpty())
            return R.drawable.matchjaime
        if(companies.isNotEmpty())
            return companies.first()
        return R.drawable.matchuclm
    }

    //var offsetX by  mutableStateOf(0f)
    //val swipeThreshold = 20f
    var content=mutableStateOf("Agus")
    val baseOptionsBuilder = BaseOptions.builder().setModelAssetPath("gesture_recognizer.task")
    val baseOptions = baseOptionsBuilder.build()

    val optionsBuilder =
        GestureRecognizer.GestureRecognizerOptions.builder()
            .setBaseOptions(baseOptions)
            .setMinHandDetectionConfidence(0.5f)
            .setMinTrackingConfidence(0.5f)
            .setMinHandPresenceConfidence(0.5f)
            .setRunningMode(RunningMode.IMAGE)

    val options = optionsBuilder.build()
    var gestureRecognizer: GestureRecognizer? = null

    val getContent =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    uri?.let { mediaUri ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            val source = ImageDecoder.createSource(
                                contentResolver, mediaUri
                            )
                            ImageDecoder.decodeBitmap(source)
                        } else {
                            MediaStore.Images.Media.getBitmap(
                                contentResolver, mediaUri
                            )
                        }
                    }?.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
                        val mpImage = BitmapImageBuilder(bitmap).build()

                        val result = gestureRecognizer?.recognize(mpImage)
                        if (result != null) {
                            withContext(Dispatchers.Main) {
                                if (result.gestures().isNotEmpty()) {
                                    if (result.gestures()[0].isNotEmpty()) {
                                        content.value = result.gestures()[0][0].categoryName()
                                        Log.e("content", content.value)
                                        Log.e("other", result.gestures()[0][0].categoryName())
//                                        textView.text = result.gestures()[0][0].categoryName()
//                                        Log.e("hey",result.gestures()[0][0].categoryName())
                                        when (result.gestures()[0][0].categoryName()) {
//                                            "ILoveYou" -> Log.e("YES","ILOVEYOUU")
//                                            "Victory" -> Log.e("YES","VICTORYY")
                                            "Thumb_Up" -> {
                                                showYes.value = true
//                                                offsetX = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe
                                                offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe

                                                removeCompany()

                                                offsetX.value = 0f // Reinicia la posición
                                                //                                               offsetX = 0f // Reinicia la posición
                                            }

                                            "Thumb_Down" -> {
                                                showNope.value = true
//                                                offsetX = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe
                                                offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe

                                                removeCompany()

                                                offsetX.value = 0f // Reinicia la posición
//                                                offsetX = 0f // Reinicia la posición
                                            }
//                                            "Open_Palm" -> Log.e("YESSS","OPEN PALMMM")
//                                            "Closed_Fist" -> Log.e("YESSS","CLOSED FIISTT")
//                                        }
//                                        if(result.gestures()[0][0].categoryName() == "ILoveYou")
//                                            Log.e("YESS!!!","ILOVEYOUUU")
                                        }
                                    }


//                                if(result.handednesses().isNotEmpty()){
//                                    if(result.handednesses()[0].isNotEmpty()){
//                                        textView.append(result.handednesses()[0][0].displayName())
//                                    }
//                                }

                                }
                            }
                        }
                    }
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
            setContent{
                val context = LocalContext.current
                // Variable para el desplazamiento de la tarjeta

                //var isRejectedPressed by remember { mutableStateOf(false) }
                //var isAcceptedPressed by remember { mutableStateOf(false) }
                // Animar el desplazamiento de la tarjeta para una transición suave
                val animatedOffsetX by animateFloatAsState(
                    targetValue = offsetX.value,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                )

                // Agregar rotación mientras se desliza la tarjeta
                val rotationDegree by remember {
                    derivedStateOf {
                        (animatedOffsetX / 30).coerceIn(-20f, 20f) // Limitar la rotación
                    }
                }

                // Usa remember para optimizar la recomposición de este estado derivado
                val swipeState by remember {
                    derivedStateOf {
                        when {
                            animatedOffsetX > swipeThreshold -> SwipeDirection.ACCEPTED // Deslizado a la derecha
                            animatedOffsetX < -swipeThreshold -> SwipeDirection.REJECTED // Deslizado a la izquierda
                            else -> SwipeDirection.DEFAULT // En el centro o por defecto
                        }
                    }
                }

                SharedSwipes.modifier

                    ?.fillMaxSize()

                    ?.clip(RoundedCornerShape(10.dp))
                    ?.graphicsLayer(
                        translationX = animatedOffsetX, // Mueve la tarjeta con animación
                        rotationZ = rotationDegree // Aplica la rotación a la tarjeta
                    )
                    ?.background(SharedState.theme.value.background, shape = RoundedCornerShape(10.dp))
                    ?.draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            offsetX.value += delta * 2 // Aumentar la sensibilidad del arrastre
                        },
                        onDragStopped = {
                            when (swipeState) {
                                SwipeDirection.ACCEPTED -> {
                                    offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe

                                    removeCompany()



                                    offsetX.value = 0f // Reinicia la posición
                                }

                                SwipeDirection.REJECTED -> {
                                    offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe

                                    removeCompany()


                                    offsetX.value = 0f // Reinicia la posición
                                }

                                else -> {
                                    // Vuelve al centro más lentamente
                                    offsetX.value = animatedOffsetX * 0.5f
                                }
                            }
                        }
                    )?.let {
                        Box(
                            modifier = it
                                .padding(16.dp)
                        ) {
                            val logo = if (SharedState.darkModeIsChecked.value) {
                                painterResource(id = R.mipmap.ic_launcher_foreground) // Logo en modo oscuro
                            } else {
                                painterResource(id = R.mipmap.img) // Logo en modo claro
                            }

                            //Spacer(modifier = Modifier.height(16.dp))
                            // Contenido de la tarjeta
                            Image(
                                painter = painterResource(id = pickCompanyOrPerson()),
                                contentDescription = null,
                                modifier =
                                Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(50.dp))
                                //.align(Alignment.Center)


                            )
            //        Row(
            //            modifier = Modifier
            //                .align(Alignment.TopCenter)
            //        ) {
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(40.dp)
                                    .align(Alignment.TopStart)
                                    //.align(Alignment.TopEnd)
                                    .padding(top = 10.dp)

                            ){
                                Image(
                                    painter = painterResource(id = R.drawable.topstart),
                                    modifier = Modifier.fillMaxSize(),
                                    contentDescription = null
                                )
                            }
                            IconButton(

                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.TopCenter)
                                    .padding(top = 10.dp),
                                onClick = {}
                            ){
                                Image(
                                    logo,contentDescription = null
                                )
                            }
                            IconButton(
                                onClick = {},
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.TopEnd)
                                    .padding(top = 10.dp)
                            ){ Image(
                                painter = painterResource(id = R.drawable.chat),
                                modifier = Modifier.fillMaxSize(),
                                contentDescription = null
                            )
                            }
                            Row(modifier = Modifier.align(Alignment.BottomCenter)){
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Tamaño del círculo
                                        .clip(CircleShape) // Hace la caja circular
                                        .background(Color.White)
                                        .padding(10.dp),//.align(Alignment.BottomStart), // Fondo del círculo
                                    contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
                                ){
                                    IconButton(
                                        onClick = {},
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.volver),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Tamaño del círculo
                                        .clip(CircleShape) // Hace la caja circular
                                        .background(Color.White)
                                        .padding(10.dp),//.align(Alignment.BottomCenter), // Fondo del círculo
                                    contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
                                ){
                                    IconButton(
                                        onClick = {
                                            // Simular el swipe hacia la izquierda
                                            showNope.value = true
                                            offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe


                                                removeCompany()

                                            offsetX.value = 0f // Reinicia la posición

                                        },
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.rechazar),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)) }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Tamaño del círculo
                                        .clip(CircleShape) // Hace la caja circular
                                        .background(Color.White)
                                        .padding(10.dp),//.align(Alignment.BottomCenter), // Fondo del círculo
                                    contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
                                ){
                                    IconButton(
                                        onClick = {
                                            gestureRecognizer = GestureRecognizer.createFromOptions(context,options)

                                            getContent.launch(arrayOf("image/*"))

                                        },
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.estrella),
                                            contentDescription = null,
                                            modifier = Modifier.size(30.dp)
                                        ) }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Tamaño del círculo
                                        .clip(CircleShape) // Hace la caja circular
                                        .background(Color.White)
                                        .padding(10.dp),//.align(Alignment.BottomCenter), // Fondo del círculo
                                    contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
                                ){
                                    IconButton(
                                        onClick = {
                                            showYes.value = true
                                            offsetX.value = -swipeThreshold * 2 // Un valor suficientemente negativo para simular el swipe

                                                removeCompany()

                                            offsetX.value = 0f // Reinicia la posición
                                        },
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.corazoncito),
                                            contentDescription = null,
                                            modifier= Modifier.size(30.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .size(70.dp) // Tamaño del círculo
                                        .clip(CircleShape) // Hace la caja circular
                                        .background(Color.White)
                                        .padding(10.dp),//.align(Alignment.BottomEnd), // Fondo del círculo
                                    contentAlignment = Alignment.Center // Centra el contenido dentro del círculo
                                ){
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(context, Augmented_Reality_Activity::class.java)
                                            context.startActivity(intent)
                                            //ModelScreen()
                                        },
                                        modifier = Modifier.size(50.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.rayito),
                                            contentDescription = null,
                                            modifier= Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }

            //        }



                            if (showYes.value) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart) // Cambiar a la esquina superior izquierda
                                        .border(2.dp, Color.Green, RoundedCornerShape(4.dp))
                                        .padding(4.dp)
                                        .alpha(1f) // Siempre visible mientras showYes es verdadero
                                ) {
                                    Text(
                                        text = "YES",
                                        color = Color.Green,
                                        fontSize = 32.sp
                                    )
                                }
                                LaunchedEffect(Unit) {
                                    delay(1000) // Espera 1 segundo
                                    showYes.value = false
                                }
                            }

                            if (showNope.value) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd) // Cambiar a la esquina superior derecha
                                        .border(2.dp, Color.Red, RoundedCornerShape(4.dp))
                                        .padding(4.dp)
                                        .alpha(1f) // Siempre visible mientras showNope es verdadero
                                ) {
                                    Text(
                                        text = "NOPE",
                                        color = Color.Red,
                                        fontSize = 32.sp
                                    )
                                }
                                LaunchedEffect(Unit) {
                                    delay(1000) // Espera 1 segundo
                                    showNope.value = false
                                }
                            }

                            // Mostrar texto YES/NOPE basado en el estado de deslizamiento
                            when (swipeState) {
                                SwipeDirection.ACCEPTED -> {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopStart) // Cambiar a la esquina superior izquierda
                                            .border(2.dp, Color.Green, RoundedCornerShape(4.dp))
                                            .padding(4.dp)
                                            .alpha(
                                                (animatedOffsetX / swipeThreshold).coerceIn(
                                                    0f,
                                                    1f
                                                )
                                            ) // Transparencia dinámica
                                    ) {
                                        Text(
                                            text = "YES",
                                            color = Color.Green,
                                            fontSize = 32.sp
                                        )
                                    }
                                }

                                SwipeDirection.REJECTED -> {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd) // Cambiar a la esquina superior derecha
                                            .border(2.dp, Color.Red, RoundedCornerShape(4.dp))
                                            .padding(4.dp)
                                            .alpha(
                                                (-animatedOffsetX / swipeThreshold).coerceIn(
                                                    0f,
                                                    1f
                                                )
                                            ) // Transparencia dinámica
                                    ) {
                                        Text(
                                            text = "NOPE",
                                            color = Color.Red,
                                            fontSize = 32.sp
                                        )
                                    }
                                }

                                else -> {
                                    // No mostrar nada si está en el estado por defecto
                                }
                            }
                        }
                    }
            }
    }


}
