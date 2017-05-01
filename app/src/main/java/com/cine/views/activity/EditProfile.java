package com.cine.views.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cine.R;
import com.cine.service.WebService;
import com.cine.service.WebServiceWrapper;
import com.cine.service.network.Params;
import com.cine.service.network.callback.ICallBack;
import com.cine.utils.ToastUtil;
import com.cine.utils.permission.Permission;
import com.cine.views.widgets.CircularImageView;
import com.cine.views.widgets.Loader;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EditProfile extends AppCompatActivity {
    @BindView(R.id.center)
    public CircularImageView civProfileImage;
    private Permission permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        permission =  new Permission(this);
        civProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }
    private void selectImage() {

        final CharSequence[] items = { "Take Photo", "Gallery",
                "Cancel" };

        TextView title = new TextView(this);
        title.setText("Add Photo!");
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(
                EditProfile.this);



        builder.setCustomTitle(title);

        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    if(permission.hasPermission(Manifest.permission.CAMERA)){
                        takePhoto();
                    }else {
                        permission.requestPermission(Manifest.permission.CAMERA,100);
                    }

                } else if (items[item].equals("Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Picture"),
                            1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }else {
            updateErrorUI("Need camera permission");
        }
    }

    void takePhoto(){

        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        Uri selectedImage = null;
        try {
            switch (requestCode) {


                case 0:
                    if (resultCode == RESULT_OK) {
                        if (data.getData() == null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        }

                        break;
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK) {
                        if (data.getData() == null) {
                            bitmap = (Bitmap) data.getExtras().get("data");
                        } else {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        }
                    }
                    break;

            }
            if (bitmap != null) {
                try {
                /*REQUEST_METHOD = POST
		cg_api_req_name = updateusephoto
		user_name = (pass current username)
		tmp_name = (pass user selected new profile image)
*/
                    showLoader();
                    Params query = new Params();
                    query.addParam("REQUEST_METHOD", "POST");
                    query.addParam("cg_api_req_name", "updateusephoto");
                    query.addParam("user_name", "prabu944");
                    query.addParam("tmp_name", getStringImage(bitmap));
                    final Bitmap finalBitmap = bitmap;
                    WebServiceWrapper.getInstance().callService(this, WebService.USER_PROFILE_URL, query, new ICallBack<String>() {
                        @Override
                        public void onSuccess(String response) {
                            dismissLoader();
                            civProfileImage.setImageBitmap(finalBitmap);
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.getString("status").equals("1")) {


                                }
                                updateErrorUI(json.getString("cg_msg"));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(String response) {
                            dismissLoader();
                            updateErrorUI(response);
                        }
                    });
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateErrorUI(String errorMsg) {
        ToastUtil.showErrorUpdate(this, errorMsg);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void showLoader(){
        Loader.showProgressBar(this);
    }

    void dismissLoader(){
        Loader.dismissProgressBar();
    }

    void updateEditProfileApi(){
        /*lcity=Salem Tamil&cg_statename=Tamilnadu Tamil&cg_country=India Tamil&cg_marriage=Single&cg_uerrcat=Casting&cg_uerscat=Hero&cg_gender=Male&cg_bday=12&cg_bmonth=12&cg_byear=2001&cg_bdysecure=Full&cg_aboutu=Hai&cg_rprjts=Hai&cg_cptpjts=Hai&cg_cawards=Hai&cg_eknwlanguages=Hai&cg_eknwskills=Hai&cg_eknwhobbies=Hai&cg_eknwlikes=Hai
 */

        Params query = new Params();
        query.addParam("cg_api_req_name","updatepageowner_details");
        query.addParam("cg_user_name","");
        query.addParam("cg_fullname","");
        query.addParam("cg_email","");
        query.addParam("cg_mobile","");
        query.addParam("cg_address","");
        query.addParam("cg_fcity","");
    }

}
