package com.cksolutions.tarea7.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.cksolutions.tarea7.R;
import com.cksolutions.tarea7.adapter.ListCustomAdapter;
import com.cksolutions.tarea7.model.FotoModel;
import com.cksolutions.tarea7.model.dbData;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFoto;
    private ListView lvFotos;
    private Uri fileUri;
    private String APP_FOLDER = "fotos_prueba";
    private boolean isValid = false;
    private dbData conn;
    private ArrayList<FotoModel> arrayFotos = new ArrayList<FotoModel>();
    private List<String> dataFotos ;
    private ListCustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        permisions();
        loadData();
    }

    private void permisions() {
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        isValid = true;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                })
                .check();
    }

    private void initUI() {
        btnFoto = findViewById(R.id.btnFoto);
        lvFotos = findViewById(R.id.lvFotos);
        btnFoto.setOnClickListener(this);
        conn = new dbData(this);
        arrayFotos = new ArrayList<>();
    }

    private void loadData() {
        arrayFotos.clear();
        dataFotos = conn.getFotos();
        for (String data : dataFotos){
            arrayFotos.add(new FotoModel(data));
        }

        adapter = new ListCustomAdapter(getApplicationContext(),arrayFotos);
        adapter.notifyDataSetChanged();
        lvFotos.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        File imageFile = null;
        if (v.getId() == R.id.btnFoto) {
            if (isValid) {
                Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "foto_" + timeStamp + "_";
                    File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + APP_FOLDER );
                    storageDir.mkdirs();
                    File image = File.createTempFile(imageFileName,
                            ".jpg",
                            storageDir);
                    imageFile = image;
                } catch (IOException e) {
                    finish();
                }
                fileUri = FileProvider.getUriForFile(this,this.getPackageName() + ".fileprovider", imageFile);
                camara.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(camara, 101);
            } else {
                Toast.makeText(this, "Es necesario validar todos los permisos", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Log.e("TAG", "URI " + fileUri);
                if (fileUri != null){
                    conn.inserta_foto(String.valueOf(fileUri));
                    //foto.setImageURI(fileUri);
                    loadData();
                }
            }
        }
    }

}
