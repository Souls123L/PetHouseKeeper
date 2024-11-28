package com.example.pethousekeeper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.webkit.MimeTypeMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadFragment extends Fragment {

    private FloatingActionButton uploadButton;
    private ImageView uploadImage;
    private EditText uploadCaption;
    private ProgressBar progressBar;
    private Uri imageUri;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    public UploadFragment() {
        // Required empty public constructor
    }

    public static UploadFragment newInstance() {
        return new UploadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 Firebase 引用
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // 載入 Fragment 的佈局
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // 初始化 UI 元件
        uploadButton = view.findViewById(R.id.uploadphotoButton);
        uploadCaption = view.findViewById(R.id.uploadCaption);
        uploadImage = view.findViewById(R.id.uploadphotoImage2);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        // 註冊 ActivityResultLauncher
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){
                    @Override
                    public void onActivityResult(ActivityResult result){
                        if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            uploadImage.setImageURI(imageUri);
                        } else {
                            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        // 點擊 ImageView 選擇圖片
        uploadImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openImagePicker();
            }
        });

        // 點擊上傳按鈕進行上傳
        uploadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(imageUri != null){
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void openImagePicker(){
        Intent photoPicker = new Intent();
        photoPicker.setAction(Intent.ACTION_GET_CONTENT);
        photoPicker.setType("image/*");
        activityResultLauncher.launch(photoPicker);
    }

    private void uploadToFirebase(Uri uri){
        String caption = uploadCaption.getText().toString().trim();
        if(caption.isEmpty()){
            uploadCaption.setError("Caption is required");
            uploadCaption.requestFocus();
            return;
        }

        final StorageReference imageReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        progressBar.setVisibility(View.VISIBLE);

        imageReference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                        imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
                            @Override
                            public void onSuccess(Uri downloadUri){
                                DataClass dataClass = new DataClass(downloadUri.toString(), caption);
                                String key = databaseReference.push().getKey();
                                if(key != null){
                                    databaseReference.child(key).setValue(dataClass);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                getParentFragmentManager().popBackStack();
                            }
                        });
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot){
                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}