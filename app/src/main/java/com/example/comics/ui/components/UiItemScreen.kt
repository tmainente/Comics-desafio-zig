package com.example.comics.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.example.comics.R
import com.example.comics.data.entities.MovieEntity


@Preview
@Composable
private fun Preview(){

    var movie = MovieEntity(id = 1L, title = "Holland", overview ="Nancy is a teacher whose life with her husband in Holland, Michigan, tumbles into a twisted tale when she and her colleague become suspicious of a secret.", image =  "https://image.tmdb.org/t/p/w500/1YMrOtrW7b4pL2lfD8UciZPOJGs.jpg")

    UiItemScreen(movie = movie)
}



@Composable
fun UiItemScreen(
    modifier: Modifier = Modifier,
    movie: MovieEntity
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        if (movie.image.isNotEmpty()) {

            Column(
                Modifier.weight(1f)
                    .align(alignment = Alignment.CenterVertically)
            ){
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(data = movie.image)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                                .transformations(
                                )
                                .build()
                        }).build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(120.dp)
                ){
                    val state = painter.state
                    if ( state is AsyncImagePainter.State.Error) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val image: Painter = painterResource(id = R.drawable.ic_launcher_background)
                            Image(
                                painter = image,
                                contentDescription = "Meu Ícone",
                                contentScale = ContentScale.Crop
                            )
                        }
                    } else {
                        SubcomposeAsyncImageContent()
                    }
                }
            }

        }

        Column(
            modifier = Modifier
                .weight(2f)
                .align(alignment = Alignment.CenterVertically)
                .wrapContentHeight()
        ) {

            if(movie.title.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding( 8.dp),
                    text = movie.title,
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            if(movie.overview.isNotEmpty()) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = movie.overview,
                    maxLines = 3
                )
            }
        }
    }

}