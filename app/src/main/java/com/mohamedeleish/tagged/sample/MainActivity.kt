package com.mohamedeleish.tagged.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamedeleish.tagged.R
import tagged.generated.SAMPLE_FRAGMENT_ONE_TAG

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SampleFragmentOne(), SAMPLE_FRAGMENT_ONE_TAG)
            .commit()

    }
}
