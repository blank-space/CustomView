package com.example.customview

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.utils.dp
import com.example.customview.view.StickerEditView.StickerEditView
import com.example.customview.view.StickerEditView.model.StickerTransformInfoBean

class MainActivity : AppCompatActivity() {

    private var time = 10

    private var position = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_stick)
        val stickerEditView: StickerEditView = findViewById(R.id.sticker_container)
        findViewById<Button>(R.id.btn_add).setOnClickListener {
            stickerEditView.addTextSticker(createStickerTransformInfoBean("Time#$time", position))
            position += 300
            time += 100
        }
    }

    private fun createStickerTransformInfoBean(
        text: String,
        position: Float
    ): StickerTransformInfoBean {
        val bean = StickerTransformInfoBean.Builder.create()
            .setId("${System.currentTimeMillis()}")
            .setType(StickerTransformInfoBean.TEXT_STICKER_TYPE)
            .setPosition(position, position)
            .setText(text)
            .setAdaptive(true)
            .build()
        return bean;
    }


}