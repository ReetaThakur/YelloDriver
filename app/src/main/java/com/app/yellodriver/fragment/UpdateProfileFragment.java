package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Mutation;
import com.app.yellodriver.R;
import com.app.yellodriver.UpdateProfileMutation;
import com.app.yellodriver.UploadProfilePhotoMutation;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelProfile.ModelUploadProfilePhoto;
import com.app.yellodriver.model.ModelProfile.MyProfileModel;
import com.app.yellodriver.model.ModelUpdateProfile.UpdateProfileModel;
import com.app.yellodriver.model.ModelUpdateProfile.UpdateProfileViewModel;
import com.app.yellodriver.service.ApiService;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.developer.kalert.KAlertDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileInputStream;

import io.paperdb.Paper;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateProfileFragment extends BaseFragment {

    private MyProfileModel.Data.YtUser userList = null;

    private TextInputEditText edtName;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPhone;
    private TextView txtUserName;
    private TextView txtSince;
    private Button btnUpdateProfile;
    private FrameLayout flProfile;
    private ImageView imgUser;
    private RatingBar ratingBar;

    private String mimeType;
    private File file;

    private UpdateProfileViewModel model;

    private CustomProgressDialog customProgressDialog;
    private Activity activity;

    @Override
    protected void initializeComponent(View view) {

        activity = getActivity();

        if (getArguments() != null) {
            if (getArguments().getParcelable(Constants.BUNDLE_PROFILE_DATA) != null) {
                userList = getArguments().getParcelable(Constants.BUNDLE_PROFILE_DATA);
            }
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar_notification);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        edtName = view.findViewById(R.id.edtTxtFullName);
        edtEmail = view.findViewById(R.id.edtTxtEmail);
        edtPhone = view.findViewById(R.id.edtTxtMobile);
        txtUserName = view.findViewById(R.id.txtUserName);
        btnUpdateProfile = view.findViewById(R.id.fragment_update_profile_btnUpdate);
        flProfile = view.findViewById(R.id.fragment_update_profile_fl);
        imgUser = view.findViewById(R.id.fragment_update_profile_imgUser);
        txtSince = view.findViewById(R.id.txtSince);
        ratingBar = view.findViewById(R.id.ratingBar);

        tvName.setText(R.string.my_account);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        if (userList != null) {
            txtUserName.setText(userList.getFullName());
            edtName.setText(userList.getFullName());
            edtEmail.setText(userList.getEmail());
            edtPhone.setText(userList.getMobile());
            txtSince.setText(DateUtils.timeAgo(userList.getCreatedAt()));

            if (userList.getAverage_rate() != null) {
                ratingBar.setRating(Float.parseFloat(userList.getAverage_rate()));
            } else {
                ratingBar.setRating(0f);
            }

            if (userList.getProfilePhoto() != null) {
                if (userList.getProfilePhoto().getId() != null) {
                    ImageLoader.loadGraphQlImage(getActivity(), imgUser, userList.getProfilePhoto().getId(), R.drawable.ic_place_holder_user);
                }
            }
        }

        setUpViewModel();

        btnUpdateProfile.setOnClickListener(view12 -> {

            customProgressDialog = new CustomProgressDialog(activity);
            customProgressDialog.showDialog();

            if (file != null) {

                Mutation mutation = new UploadProfilePhotoMutation(mimeType, file.getName());
                model.updateProfilePhoto(mutation);

            } else {
                Input<String> name = new Input<String>(edtName.getText().toString(), true);
                Input<String> email = new Input<String>(edtEmail.getText().toString(), true);
                Input<String> phone = new Input<String>(edtPhone.getText().toString(), true);
                Input<String> countryCode = new Input<String>(Constants.COUNTRY_CODE, true);

                String userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID,"");
                Mutation updateProfileMutation = new UpdateProfileMutation(userId,countryCode, email,name, phone);

                model.updateProfileData(updateProfileMutation);
            }
        });

        flProfile.setOnClickListener(view13 -> {
            KAlertDialog imagePickAlertDlg = new KAlertDialog(getContext())
                    .setTitleText(YelloDriverApp.getInstance().getString(R.string.app_name))
                    //.setContentText("Do you want to logout?")
                    .setConfirmText(YelloDriverApp.getInstance().getString(R.string.title_camera))
                    .setCancelText(YelloDriverApp.getInstance().getString(R.string.title_gallery));

            imagePickAlertDlg.setCanceledOnTouchOutside(true);
            imagePickAlertDlg.setConfirmClickListener(kAlertDialog -> {
                imagePickAlertDlg.dismiss();
                ImagePicker.Companion.with(this)
                        .cameraOnly()
                        .crop()
                        //.compress(1024)
                        //.maxResultSize(1080, 1080)
                        .saveDir(new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name)))
                        .start(Constants.CAMERA_REQUEST);


            });
            imagePickAlertDlg.setCancelClickListener(kAlertDialog -> {
                imagePickAlertDlg.dismiss();
                ImagePicker.Companion.with(this)
                        .galleryOnly()
                        .crop()
                        //.compress(1024)
                        //.maxResultSize(1080, 1080)
                        .saveDir(new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name)))
                        .start(Constants.GALLERY_REQUEST);
            });

            imagePickAlertDlg.show();
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_update_profile;
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) activity).get(UpdateProfileViewModel.class);

        model.mutableLiveDataUpdateProfile.observe(getViewLifecycleOwner(), observerApoloResponse);

        model.mutableProfileData.observe(getViewLifecycleOwner(), observerProfilePhoto);
    }

    Observer<ModelUploadProfilePhoto.Data.UploadProfilePhoto> observerProfilePhoto = profilePhotoData -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            submitProfileDataToAWS(profilePhotoData);
        }
    };

    Observer<UpdateProfileModel.Data.UpdateUser> observerApoloResponse = updateYtUser -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            // your code here ...

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (updateYtUser != null) {

                if (updateYtUser != null && updateYtUser.getUser() !=null) {
                    txtUserName.setText(updateYtUser.getUser().getFullName());
                    edtName.setText(updateYtUser.getUser().getFullName());
                    edtEmail.setText(updateYtUser.getUser().getEmail());
                    edtPhone.setText(updateYtUser.getUser().getMobile());

                    ((HomeActivity) activity).setProfileData(updateYtUser);
                }

                final KAlertDialog pDialog = new KAlertDialog(activity, KAlertDialog.SUCCESS_TYPE)
                        .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                        .setContentText(activity.getString(R.string.msg_success_profile_update))
                        .setConfirmText(activity.getResources().getString(R.string.lbl_ok));
                pDialog.show();
                pDialog.setCancelable(true);
                pDialog.setCanceledOnTouchOutside(true);
//            txtName.setText(listUser.get(0).getFullName());
            } else {

            }
        }
    };

    private void submitProfileDataToAWS(ModelUploadProfilePhoto.Data.UploadProfilePhoto uploadProfilePhoto) {
        //Defining retrofit api service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.dummy.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//        File fileDuplicate = new File(file.getPath());    // create new file on device
//init array with file length
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestBody requestFile = RequestBody.create(bytesArray);
//        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        ApiService service = retrofit.create(ApiService.class);
        Call<Void> call = service.uploadProfileData(uploadProfilePhoto.getUploadUrl(), requestFile);
        //calling the api
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                YLog.e("Response", response.toString());

                Input<String> name = new Input<String>(edtName.getText().toString(), true);
                Input<String> email = new Input<String>(edtEmail.getText().toString(), true);
                Input<String> phone = new Input<String>(edtPhone.getText().toString(), true);
                Input<String> countryCode = new Input<String>(Constants.COUNTRY_CODE, true);

                String userId = Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_HASURA_ID,"");
                Mutation updateProfileMutation = new UpdateProfileMutation(userId,countryCode, email,name, phone);

                model.updateProfileData(updateProfileMutation);

//                Glide.with(ProfileActivity.this)
//                        .load(viewUrl)
//                        .centerCrop()
//                        .into(imgServerPreview);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                if (customProgressDialog != null) {
                    customProgressDialog.dismissDialog();
                }

                YLog.e("Response", t.toString());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = data.getData();
            imgUser.setImageURI(fileUri);

            //You can get File object from intent
            file = ImagePicker.Companion.getFile(data);

            //You can also get File Path from intent
            String filePath = ImagePicker.Companion.getFilePath(data);

            YLog.e("*** FilePath", filePath);

            mimeType = Utils.getMimeType(filePath);

            YLog.e("*** MimeType", mimeType);
            YLog.e("*** Title", file.getName());

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(activity, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}