package com.lee.picturenote.ui.picturedetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding3.view.clicks
import com.lee.picturenote.R
import com.lee.picturenote.common.*
import com.lee.picturenote.data.remote.model.Picture
import com.lee.picturenote.databinding.ActivityPictureDetailBinding
import com.lee.picturenote.ui.picturedetail.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

private const val CLICK_TIME_OUT = 1000L
private const val TAG = "PictureDetailActivity"
/**
 * 상세페이지 activity class
 * **/
@AndroidEntryPoint
class PictureDetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPictureDetailBinding
    private lateinit var compositeDisposable: CompositeDisposable
    private val viewModel : DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_picture_detail)
        binding.detailViewModel = viewModel
        binding.detailActivity = this@PictureDetailActivity

        observeData()
        addClickListener()
        getIntentExtras()
        viewModel.checkFavorite() // 현재 보여주는 그림이 즐겨찾기가 된것인지 확인
    }

    override fun onDestroy() {
        compositeDisposable.clear() // RxBinding 사용 후 생성된 Disposable객체 clear
        super.onDestroy()
    }

    /**
     * intent로부터 전달받은 extra를 가져오는 함수
     * **/
    private fun getIntentExtras() {
        val selectedPicture : Picture = if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){ // API Level 33 미만
            intent.extras?.getSerializable(EXTRA_SELECTED_PICTURE) as Picture
        } else { // API Level 33 이상
            intent.extras?.getSerializable(EXTRA_SELECTED_PICTURE , Picture::class.java) as Picture
        }
        viewModel.setPicture(selectedPicture)
    }

    private fun observeData() {
        with(viewModel){
            selectedPicture.observe(this@PictureDetailActivity){ // 선택된 그림
                settingContents(it)
            }
            toastMessage.observe(this@PictureDetailActivity){ // Toast Message
                Toast.makeText(this@PictureDetailActivity , it , Toast.LENGTH_SHORT).show()
            }
            isProgress.observe(this@PictureDetailActivity){
                if(it){
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }
            previousButtonEnable.observe(this@PictureDetailActivity){ // 이전 버튼 enable 여부
                binding.previousButton.isEnabled = it
            }
            nextButtonEnable.observe(this@PictureDetailActivity){ // 다음 버튼 enable 여부
                binding.nextButton.isEnabled = it
            }
        }
    }

    /**
     * Contents들에 대해 setting해주는 함수
     * **/
    private fun settingContents(picture : Picture) {
        with(binding){
            Glide.with(this@PictureDetailActivity)
                .load(picture.downloadUrl)
                .error(R.drawable.no_image)
                .into(pictureImageView)

            authorTextView.text = String.format(getString(R.string.detail_author) , picture.author)
            widthTextView.text = String.format(getString(R.string.detail_width) , picture.width)
            heightTextView.text = String.format(getString(R.string.detail_height) , picture.height)
            Log.d(TAG, "settingContents: ${picture.id.toInt()}")
            when(picture.id.toInt()){
                FIRST_ITEM_ID -> viewModel.setPreviousButtonEnable(false)
                LAST_ITEM_ID -> viewModel.setNextButtonEnable(false)
                else -> {
                    viewModel.setPreviousButtonEnable(true)
                    viewModel.setNextButtonEnable(true)
                }
            }
            if(picture.isFavorite){ // 즐겨찾기인 경우
                favoriteButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.setting_favorite_icon
                        , null))
            } else { // 즐겨찾기가 아닌 경우
                favoriteButton.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.favorite_icon
                        , null))
            }
        }
        viewModel.setProgress(false)
    }

    /**
     * Next , Previous , 즐겨찾기 클릭 리스너 등록하는 함수
     * **/
    private fun addClickListener() {
        compositeDisposable = CompositeDisposable()
        with(binding){
            val prevDisposable = previousButton.clicks() // 이전 버튼
                .throttleFirst(CLICK_TIME_OUT , TimeUnit.MILLISECONDS)
                .subscribe{
                    if(Utils.checkNetworkConnection(this@PictureDetailActivity)){ // 인터넷이 연결 되어있는 경우
                        viewModel.selectedPicture.value?.let {
                            val id = it.id.toInt() - 1
                            viewModel.getPictureByButtonClick(id , false)
                        }
                    } else { // 인터넷이 연결 되어있지 않은 경우
                        viewModel.onError(getString(R.string.detail_check_network))
                    }
                }

            val nextDisposable = nextButton.clicks() // 다음 버튼
                .throttleFirst(CLICK_TIME_OUT , TimeUnit.MILLISECONDS)
                .subscribe {
                    if(Utils.checkNetworkConnection(this@PictureDetailActivity)){ // 인터넷이 연결 되어있는 경우
                        viewModel.selectedPicture.value?.let {
                            val id = it.id.toInt() + 1
                            viewModel.getPictureByButtonClick(id, true)
                        }
                    } else { // 인터넷이 연결 되어있지 않은 경우
                        viewModel.onError(getString(R.string.detail_check_network))
                    }
                }

            val favoriteDisposable = favoriteButton.clicks() // 즐겨찾기
                .throttleFirst(CLICK_TIME_OUT , TimeUnit.MILLISECONDS)
                .subscribe{
                    clickFavoriteButton()
                }
            compositeDisposable.addAll(prevDisposable , nextDisposable , favoriteDisposable)
        }
    }

    /**
     * 즐겨찾기 버튼 클릭 시 다이얼로그를 생성하는 함수 ( 다이얼로그 버튼 클릭에 따라 즐겨찾기 등록 / 해제 )
     * **/
    private fun clickFavoriteButton() {
        val alertBuilder = AlertDialog.Builder(this@PictureDetailActivity)
        alertBuilder.setTitle(getString(R.string.favorite))
        viewModel.selectedPicture.value?.let {
            if(it.isFavorite){ // 이미 즐겨찾기가 설정된 경우
                alertBuilder.setMessage(getString(R.string.delete_dialog_message))
                    .setNegativeButton(getString(R.string.cancel)) { dialog , _ -> dialog.dismiss() }
                    .setPositiveButton(getString(R.string.confirm)) { dialog , _ ->
                        viewModel.deleteFavorite() // 즐겨찾기 해제
                        with(Intent(INTENT_RELEASE_FAVORITE)){ // BroadcastReceiver에게 상태 변경 알려주기
                            putExtra(EXTRA_UPDATE_ID , it.id)
                            sendBroadcast(this)
                        }
                        dialog.dismiss()
                    }
            } else { // 아직 즐겨찾기가 설정되지 않은 경우
                alertBuilder.setMessage(getString(R.string.add_dialog_message))
                    .setNegativeButton(getString(R.string.cancel)) {dialog , _ -> dialog.dismiss() }
                    .setPositiveButton(getString(R.string.confirm)) {dialog , _ ->
                        viewModel.addFavorite() // 즐겨찾기 등록
                        with(Intent(INTENT_SETTING_FAVORITE)){ // BroadcastReceiver에게 상태 변경 알려주기
                            putExtra(EXTRA_UPDATE_ID , it.id)
                            sendBroadcast(this)
                        }
                        dialog.dismiss()
                    }
            }
            alertBuilder.create().show()
        }
    }
}