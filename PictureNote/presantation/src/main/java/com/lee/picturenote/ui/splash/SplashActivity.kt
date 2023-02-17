package com.lee.picturenote.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lee.picturenote.R
import com.lee.picturenote.common.Utils
import com.lee.picturenote.databinding.ActivitySplashBinding
import com.lee.picturenote.ui.picturelist.PictureListActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private var isReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // SplashScreen 추가 (gradle에 의존성을 추가함으로써 api 31미만에서도 해당 부분은 동작한다.)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        initSplashScreen()
        checkNetworkState()
    }

    /**
     * 앱 시작 후 이미지 로딩 전에 네트워크 상태를 체크하는 함수
     * **/
    private fun checkNetworkState() {
        if(Utils.checkNetworkConnection(this)){ // 네트워크가 연결되어 있을 경우
            with(Intent(this@SplashActivity , PictureListActivity::class.java)){
                startActivity(this)
                finish()
            }
        } else { // 네트워크가 연결 되어 있지 않은 경우
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.network))
                .setMessage(getString(R.string.check_network))
                .setPositiveButton(getString(R.string.confirm)) { dialog, _ ->
                    checkNetworkState()
                    dialog.dismiss()
                }.create().show()
        }
    }

    /**
     * SplashScreen 초기화 하는 함수
     * **/
    private fun initSplashScreen() {
        lifecycleScope.launch { // Data를 초기화 해줄 영역이 있다면 해당 부분에서 해준다.
            delay(1500)
            isReady = true
        }

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener( // SplashScreen이 생성되고 그려질 때 계속해서 호출된다.
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isReady) {
                        // 1.5초 후 Splash Screen 제거
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // 아직 Data들이 초기화 되지 않았다면
                        false
                    }
                }
            }
        )
    }
}