# Android에서 카메라 시작하기

---

카메라 기반 애플리케이션을 빌드하거나 기존 애플리케이션에 카메라를 통합하려면 먼저 카메라 사용을 위한 간소한 API인 CameraX API를 살펴볼 수 있다.

## CameraX
CameraX는 더 쉬운 카메라 앱 개발을 위해 빌드된 Jetpack 라이브러리이다. 새 앱을 개발할 때는 CameraX로 시작하는 것이 좋다. CameraX는 대부분의 Android 기기에서 작동하며 이전 버전인 Android 5.0(API 수준 21)까지 호환되고 일관되고 사용하기 쉬운 API를 제공한다.

CameraX는 여러가지 방식으로 개발자 환경을 개선한다
### 광범위한 기기 호환성
CamerX는 Android5.0(API 수준 21)이상을 실행하는 기기(기존 Android 기기의 98% 이상)를 지원한다

### 사용 편의성
CameraX에서는 기기별 차이를 관리하는 대신 실행해야 하는 작업에만 집중할 수 있도록 지원하는 사용 사례가 도입되었다. 다음과 같은 대부분의 일반적인 카메라 사용 사례가 지원된다.
- 미리보기: 화면에서 이미지를 본다
- 이미지 분석: ML Kit로 전달하는 경우와 같이 알고리즘에 사용할 수 있도록 버퍼에 원할하게 액세스한다.
- 이미지 캡처: 이미지를 저장한다
- 동영상 캡처: 동영상과 오디오를 저장한다


### 기기 간 일관성
카메라 동작을 일관되게 유지하기란 쉽지 않은 일이다. `가로세로 비율`, `방향`, `회전`, `미리보기 크기`, `이미지 크기`를 고려해야 한다. CameraX를 사용하면 이러한 기본적인 동작이 자동으로 해결된다.

![그림 2. 자동화된 CameraX Test Lab은 여러 기기 유형과 제조업체에 걸쳐 일관된 API 환경을 보장한다](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fb3%2Fl4pjm1ys4k93kkx6g0s7r6p80000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_9aqUCH%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-10-01%20%EC%98%A4%EC%A0%84%2012.26.01.png)

### 카메라 확장 프로그램
CameraX에는 적게는 단 두 줄의 코드로 기기의 기본 카메라 앱과 동일한 기능에 액세스 할 수 있게 해주는 선택적 `Extensions API`가 있다.
확장 프로그램으로는 `빛망울 효과(세로 모드)`, `HDR(High Dynamic range)`, `야간 모드`, `얼굴 보정` 등이 있다.(모두 기기 지원이 필요함)

![그림 3. CameraX를 사용하여 빛망울 효과(세로 모드)로 캡처한 이미지.](..%2F..%2F..%2F..%2Fvar%2Ffolders%2Fb3%2Fl4pjm1ys4k93kkx6g0s7r6p80000gn%2FT%2FTemporaryItems%2FNSIRD_screencaptureui_12DHUr%2F%EC%8A%A4%ED%81%AC%EB%A6%B0%EC%83%B7%202024-10-01%20%EC%98%A4%EC%A0%84%2012.25.28.png)


# 프로젝트 설정

## Gradle 종속 항목 추가

1. 앱/모듈 수준의 `build.gradle`파일을 열고 CameraX 종속 항목을 추가한다

```kotlin
dependencies {
    val camerax_version = "1.1.0-beta01"
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")
    implementation("androidx.camera:camera-view:${camerax_version}")
    implementation("androidx.camera:camera-extensions:${camerax_version}")
}
```

2. CameraX에서는 Java8에 포함된 메서드가 필요하므로 이에 따라 컴파일 옵션을 설정해야 한다. (기본으로 설정됨)

```kotlin
compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
```

3. 이 Repository에서는 ViewBinding을 사용할 것이므로 viewBinding 설정도 해준다
```kotlin
buildFeatures { 
    viewBinding = true
}
```

## Codelab 레이아웃 만들기

1. activty_main.xml 파일 설정

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.camera.view.PreviewView
        android:id="@+id/viewFinder"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <Button
        android:id="@+id/button_imageCapture"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginBottom="50dp"
        android:layout_marginEnd="50dp"
        android:elevation="2dp"
        android:text="@string/take_photo"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintEnd_toStartOf="@id/vertical_centerline" />

    <Button
        android:id="@+id/button_videoCapture"
        android:layout_width="110dp"
        android:layout_height="110dp"
        android:layout_marginBottom="50dp"
        android:layout_marginStart="50dp"
        android:elevation="2dp"
        android:text="@string/start_capture"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintStart_toEndOf="@id/vertical_centerline" />

    <androidx.constraintlayout.widget.Guideline
        android:id="@+id/vertical_centerline"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:layout_constraintGuide_percent=".50" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

2. MainActivity.kt 설정
카메라 권한을 확인하고, 카메라를 시작하고, 사진 및 캡처 버튼의 `onClickListener()`를 설정하고, `cameraExecutor`를 구현할 수 있도록 `onCreate()`가 이미 구현되어 있다.
`onCreate()`가 구현되어 있어도 파일에서 메서드를 구현할 때까지는 카메라가 작동하지 않는다.

```kotlin
package com.example.camerapractice

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.camerapractice.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale


typealias LumaListener = (luma: Double) -> Unit
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // For ImageCaputure
    private var imageCapture: ImageCapture? = null
    // For VideoCapture
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    // For Executor
    private lateinit var cameraExecutor: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 카메라 권한 요청
        if (allPermissionsGranted()) startCamera()
        else ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

        binding.buttonImageCapture.setOnClickListener { takePhoto() }
        binding.buttonVideoCapture.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Executor 종료
        cameraExecutor.shutdown()
    }
    private fun takePhoto() {}

    private fun captureVideo() {}

    private fun startCamera() {}

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // 카메라 권한 요청
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all{
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object{
        private const val TAG = "CameraPractice"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).apply{
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
}
```

