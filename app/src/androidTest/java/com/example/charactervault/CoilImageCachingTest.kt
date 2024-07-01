package com.example.charactervault

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoilImageCachingTest {

    @Test
    fun testImageCaching() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val imageLoader = ImageLoader.Builder(context).build()
        val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png" // Reliable image URL

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = runBlocking {
            try {
                imageLoader.execute(request)
            } catch (e: Exception) {
                Log.e("CoilImageCachingTest", "Image loading failed", e)
                null
            }
        }

        assert(result is SuccessResult) { "Expected a SuccessResult but got $result" }
        assertNotNull((result as? SuccessResult)?.drawable)
    }
}