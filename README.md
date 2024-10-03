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

# Preview 사용 예제 구현
카메라 애플리케이션에서 뷰파인더를 사용하면 사용자가 촬영할 사진을 미리 볼 수 있다. CameraX `Preview`클래스를 사용하여 뷰파인더를 구현한다

`Preview`를 사용하려면 먼저 구성을 정의해야 이를 사용하여 사용 사례 인스턴스를 만들 수 있다. 결과 인스턴스는 CameraX 수명 주기에 바인딩하는 대상이다.

1. startCamera() 구현

```kotlin
private fun startCamera() {
   val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

   cameraProviderFuture.addListener({
       // Used to bind the lifecycle of cameras to the lifecycle owner
       val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

       // Preview
       val preview = Preview.Builder()
          .build()
          .also {
              it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
          }

       // Select back camera as a default
       val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

       try {
           // Unbind use cases before rebinding
           cameraProvider.unbindAll()

           // Bind use cases to camera
           cameraProvider.bindToLifecycle(
               this, cameraSelector, preview)

       } catch(exc: Exception) {
           Log.e(TAG, "Use case binding failed", exc)
       }

   }, ContextCompat.getMainExecutor(this))
}
```

- `ProcessCameraProvider`인스턴스를 만든다.
  - 카메라의 수명 주기를 수명 주기 소유자와 바인딩하는 데 사용
  - CameraX가 수명 주기를 인식하므로 카메라를 열고 닫는 작업이 필요하지 않다

> val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

- `cameraProviderFuture`에 리스너를 추가한다.
  - `Runnable`을 하나의 인수로 추가한다. 나중에 채워넣는다.
  - `ContextCompat``.getMainExecutor()`를 두 번째 인수로 추가한다. 그러면 `기본 스레드`에서 실행되는 `Executor`가 반환된다.

> cameraProviderFuture.addListener(Runnable {}, ContextCompat.getMainExecutor(this))

- `Runnable`에서 `ProcessCameraProvider`를 추가한다. 이는 카메라의 수명 주기를 애플리케이션 프로세스 내의 `LifecycleOwner`에 바인딩하는 데 사용한다.

> val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

- `Preview` 객체를 초기화하고 이 객체에서 build를 호출하고 뷰파인더에서 노출 영역 제공자를 가져온 다음 미리보기에서 설정한다

> val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

- `try` 블록을 만든다.
  - 이 블록 내에서 `cameraProvider`에 바인딩된 항목이 없도록 한다
  - `cameraSelector` 및 미리보기 객체를 `cameraProvider`에 바인딩한다

> try {
cameraProvider.unbindAll()
cameraProvider.bindToLifecycle(
this, cameraSelector, preview)
}

- 앱에 더 이상 포커스가 없는 경우와 같이 이 코드는 몇 가지 원인에 의해 실패할 수 있다.
  - 다음 코드를 `catch` 블록 내에 wrapping하여 실패가 발생한 경우 기록한다

> catch(e: Exception){ Log.e(TAG, "UseCase binding failed", e) }

# ImageCapture 사용 사례 구현
다른 사용 사례도 `Preview`와 매우 유사한 방식으로 작동한다. 먼저 실제 사용 사례 객체를 인스턴스화하는 데 사용하는 구성 객체를 정의한다.
사진을 캡처하려면 `Take Photo` 버튼을 누르면 호출되는 `takePhoto()` 메서드를 구현한다.

1. `takePhoto()` 구현

- 먼저 `ImageCapture` 사용 사례에 대한 참조를 가져온다.
  - 사용 사례가 null이면 함수를 종료한다.
  - 이미지 캡처가 설정되기 전에 사진 버튼을 탭하면 null이 된다.
  - `return`문이 없으면 `null`인 경우 앱이 비정상적으로 종료된다.
```kotlin
val imageCapture = imageCapture?: return
```

- 다음으로 이미지를 보관할 `MediaStore` 콘텐츠 값을 만든다.
  - MediaStroe의 표시 이름이 고유하도록 `타임스탬프`를 사용한다.

```kotlin
import java.text.SimpleDateFormat
import java.util.Locale

val name = SimpleDateFormat(FILENAME_FROMAT, Locale.KOREA).format(System.currentTimeMillis())

val contentValues = ContentValues().apply{
    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    if (Build.VERSION.SDK_INT > Build.VERSIONT_CODES.P){
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
    }
}
```
- `OutputFileOptions` 객체를 만든다.
  - 이 객체에서 원하는 출력방법에 대한 사항을 지정할 수 있다.
  - 출력을 `MediaStore`에 저장하여 다른 앱에서 표시할 수 있도록 MediaStore 항목을 추가한다.

```kotlin
val outputOptions = ImageCapture.OutputFileOptions.Builder(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()
```

- `imageCapture` 객체에서 `takePicture()`를 호출한다.
  - `outputOptions`, 실행자, 이미지가 저장될 때의 콜백을 저장한다.
  - 그 다음에 콜백을 작성한다.

```kotlin
imageCapture.takePicture(
    // 출력방식
    outputOptions,
    // 실행자
    ContextCompat.getMainExecutor(this),
    // 이미지가 저장될 때의 콜백
    object: ImageCapture.OnImageSavedCallback {}
)
```
- 이미지 캡처에 실패하거나 이미지 캡처 저장에 실패하는 경우 오류 사례를 추가하여 실패했음을 기록한다.
  - object: ImageCapture.OnImageSavedCallback의 블록 안에 작성한다.
```kotlin
override fun onError(e: ImageCaptureException){
    Log.e(TAG, "Photo capture failedL ${e.message}", e)
}
```
- 캡처에 실패하지 않았으면 사진이 성공적으로 촬영된 것이다.
  - 앞서 만든 파일에 사진을 저장한다.
  - 사용자에게 사진 촬영이 완료되었음을 알리는 Toast 메시지를 표시한다.
  - 로그 구문을 출력한다.
  - object: ImageCapture.OnImageSavedCallback의 블록 안에 작성한다.

```kotlin
override fun onImageSaved(output: ImageCapture.OutputFileResults){
    // 사진이 저장된 경로를 기록한다
    val msg = "Photo capture succeeded: ${output.savedUri}"
    // 사용자에게 Toast 메시지를 표시하여 사진촬영이 완료되었음을 알린다
    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
    Log.d(TAG, msg)
}
```

- `takePhoto`메서드 전체 코드 
```kotlin
private fun takePhoto() {
   val imageCapture = imageCapture ?: return

   val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
              .format(System.currentTimeMillis())
   val contentValues = ContentValues().apply {
       put(MediaStore.MediaColumns.DISPLAY_NAME, name)
       put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
       if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
           put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
       }
   }

   val outputOptions = ImageCapture.OutputFileOptions
           .Builder(contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
           .build()

   imageCapture.takePicture(
       outputOptions,
       ContextCompat.getMainExecutor(this),
       object : ImageCapture.OnImageSavedCallback {
           override fun onError(exc: ImageCaptureException) {
               Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
           }

           override fun
               onImageSaved(output: ImageCapture.OutputFileResults){
               val msg = "Photo capture succeeded: ${output.savedUri}"
               Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
               Log.d(TAG, msg)
           }
       }
   )
}
```

2. `startCamera()` 메서드로 이동하여 이 코드를 미리보기용 코드 아래에 복사한다.

```kotlin
imageCapture = ImageCapture.Builder().build()
```
3. 새로운 사용 사례를 포함하도록 `try`블록에서 `bindToLifecycle()`호출을 업데이트 한다.
```kotlin
cameraProvider.bindToLifecycle(
  this, // lifecycle
  cameraSelector, // CamerSelector
  preview, // UseCase
  imageCapture
)
```





