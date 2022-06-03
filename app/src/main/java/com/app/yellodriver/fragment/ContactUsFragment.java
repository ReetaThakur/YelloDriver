package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apollographql.apollo.api.Input;
import com.apollographql.apollo.api.Mutation;
import com.app.yellodriver.InsertSupportRequestMutation;
import com.app.yellodriver.R;
import com.app.yellodriver.UpdateProfileMutation;
import com.app.yellodriver.UpdateSupportRequestMutation;
import com.app.yellodriver.UploadSupportRequestAttachmentMutation;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelProfile.ModelUploadProfilePhoto;
import com.app.yellodriver.model.ModelSupportRequest.ModelSupportRequestAttachement;
import com.app.yellodriver.model.ModelSupportRequest.ModelUpdateSupportRequest;
import com.app.yellodriver.model.ModelSupportRequest.SupportRequestModel;
import com.app.yellodriver.model.ModelSupportRequest.SupportRequestViewModel;
import com.app.yellodriver.model.ModelVehicle.VehicleViewModel;
import com.app.yellodriver.service.ApiService;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.bumptech.glide.Glide;
import com.developer.kalert.KAlertDialog;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactUsFragment extends BaseFragment {

    private SupportRequestViewModel supportRequestViewModel;

    private static final int REQ_IMAGE_CODE = 541;
    private String ID_IMAGE_MAIN = "imgMain";
    private String ID_IMAGE_FRAME = "imgFrame";
    private String ID_IMAGE_REMOVE = "imgRemove";
    private LinearLayout fragmentContactLinearImageSection;

    private Button btnSubmit;
    private Button btnCancel;
    private Activity mActivity;
    private TextInputEditText edtIssue;
    private TextInputLayout txtIssue;

    private String supportRequestId;
    private String issueReason;
    private ArrayList<String> alMimeType = new ArrayList<>();
    private ArrayList<File> alFile = new ArrayList<>();
    private ArrayList<String> alFilepath = new ArrayList<>();
    private ArrayList<ModelSupportRequestAttachement.Data.UploadSupportRequestAttachment> alUploadUrl = new ArrayList<>();
    private ArrayList<String> alFileUploadId = new ArrayList<>();
    private JSONArray jsonArray = new JSONArray();

    private CustomProgressDialog customProgressDialog;


    private int total = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    protected void initializeComponent(View view) {
        setUpToolBar(view);

        btnSubmit = view.findViewById(R.id.btnContactSubmit);
        btnCancel = view.findViewById(R.id.btnContactCancel);
        edtIssue = view.findViewById(R.id.fragment_contactus_edtIssue);
        txtIssue = view.findViewById(R.id.fragment_contactus_txtIssue);

        fragmentContactLinearImageSection = (LinearLayout) view.findViewById(R.id.fragment_contactus_llimagesection);
        fragmentContactLinearImageSection.removeAllViews();

        addImageLayout();

        setUpViewModel();

        btnSubmit.setOnClickListener(view1 -> {

            issueReason = edtIssue.getText().toString();

            if (issueReason.length() == 0) {
                txtIssue.setError(getString(R.string.err_emptyIssue));
            } else {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "Issue Type Support ");
                    jsonObject.put("title", "Help desk");
                    jsonObject.put("description", issueReason);

                    YLog.e("Json", jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customProgressDialog = new CustomProgressDialog(mActivity);
                customProgressDialog.showDialog();

                Input<Object> content = new Input<>(jsonObject, true);
                Input<String> status = new Input<>("NEW", true);
                InsertSupportRequestMutation insertSupportRequestMutation = new InsertSupportRequestMutation(content, status);
                supportRequestViewModel.insertSupportRequest(insertSupportRequestMutation);
            }
        });

        btnCancel.setOnClickListener(view2 -> {
            getFragmentManager().popBackStack();
        });
    }

    private void addImageLayout() {
        for (int p = 1; p <= 3; p++) {
            RelativeLayout pickerView = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_contactus_imagepickitem, fragmentContactLinearImageSection, false); //Message view
            pickerView.setTag(p);
            CardView fragmentContactUsCard = pickerView.findViewById(R.id.fragment_contactus_card);
            fragmentContactUsCard.setTag(p);
            fragmentContactUsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int viewTag = (int) view.getTag();
                    int requestCode = REQ_IMAGE_CODE + viewTag;
                    startContactImageSelection(requestCode);
                }
            });

            ImageView imgContactUsMain = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgMain);
            imgContactUsMain.setTag(ID_IMAGE_MAIN + p);

            ImageView imgContactUsFrame = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgframe);
            imgContactUsFrame.setTag(ID_IMAGE_FRAME + p);

            ImageView fragmentContactUsImgRemove = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgRemove);
            fragmentContactUsImgRemove.setTag(ID_IMAGE_REMOVE + p);

            fragmentContactUsImgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentContactUsImgRemove.setVisibility(View.GONE);
                    imgContactUsMain.setImageResource(0);
                    //imgContactFirst.setImageResource(R.drawable.ic_add);
                    imgContactUsFrame.setBackgroundResource(R.drawable.drawable_contactus_imageempty);
                }
            });
            fragmentContactLinearImageSection.addView(pickerView);
        }
    }

    private void removeImageLayout() {
        fragmentContactLinearImageSection.removeAllViews();

        for (int p = 1; p <= 3; p++) {
            RelativeLayout pickerView = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_contactus_imagepickitem, fragmentContactLinearImageSection, false); //Message view
            pickerView.setTag(p);
            CardView fragmentContactUsCard = pickerView.findViewById(R.id.fragment_contactus_card);
            fragmentContactUsCard.setTag(p);
            fragmentContactUsCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int viewTag = (int) view.getTag();
                    int requestCode = REQ_IMAGE_CODE + viewTag;
                    startContactImageSelection(requestCode);
                }
            });

            ImageView imgContactUsMain = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgMain);
            imgContactUsMain.setTag(ID_IMAGE_MAIN + p);

            ImageView imgContactUsFrame = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgframe);
            imgContactUsFrame.setTag(ID_IMAGE_FRAME + p);

            ImageView fragmentContactUsImgRemove = (ImageView) pickerView.findViewById(R.id.fragment_contactus_imgRemove);
            fragmentContactUsImgRemove.setTag(ID_IMAGE_REMOVE + p);

            fragmentContactUsImgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentContactUsImgRemove.setVisibility(View.GONE);
                    imgContactUsMain.setImageResource(0);
                    //imgContactFirst.setImageResource(R.drawable.ic_add);
                    imgContactUsFrame.setBackgroundResource(R.drawable.drawable_contactus_imageempty);
                }
            });
            fragmentContactLinearImageSection.addView(pickerView);
        }
    }

    private void setUpViewModel() {

        supportRequestViewModel = ViewModelProviders.of((HomeActivity) getActivity()).get(SupportRequestViewModel.class);
        supportRequestViewModel.mutableInsertRequest.observe(((HomeActivity) getActivity()), observerInsertRequest);
        supportRequestViewModel.mutableUpload.observe(((HomeActivity) getActivity()), observerUploadPhoto);
        supportRequestViewModel.mutableUpdateSupport.observe(((HomeActivity) getActivity()), observerUpdateSupport);
    }

    Observer<ModelUpdateSupportRequest.Data.UpdateSupportRequest> observerUpdateSupport = updateSupportRequest -> {
        YLog.e("Sopport", "Success");
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }
        successDialog();
    };

    Observer<SupportRequestModel.Data.InsertYtSupportRequestOne> observerInsertRequest = insertYtSupportRequestOne -> {
        supportRequestId = insertYtSupportRequestOne.getId();
        YLog.e("ID", supportRequestId);
//        Input<Object> inputSupportRequestId = new Input<>(supportRequestId, true);

        if (alFile.size() == 0) {
            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            successDialog();
        }

        for (int i = 0; i < alFile.size(); i++) {
            String mimeType = alMimeType.get(i);
            String title = alFile.get(i).getName();

            UploadSupportRequestAttachmentMutation uploadSupportRequestAttachmentMutation = new UploadSupportRequestAttachmentMutation(mimeType, title, supportRequestId);
            supportRequestViewModel.uploadSupportRequestAttachment(uploadSupportRequestAttachmentMutation);
        }
    };

    Observer<ModelSupportRequestAttachement.Data.UploadSupportRequestAttachment> observerUploadPhoto = uploadSupportRequestAttachment -> {
        YLog.e("Response observerUploadPhoto", "observerUploadPhoto");

        alUploadUrl.add(uploadSupportRequestAttachment);
//        submitAttachmentDataToAWS(uploadSupportRequestAttachment, alFile.get(alUploadUrl.size() - 1));

        if (alUploadUrl.size() == alFile.size()) {
            for (int i = 0; i < alFile.size(); i++) {
                submitAttachmentDataToAWS(alUploadUrl.get(i), alFile.get(i));
            }
        }
    };

    private void submitAttachmentDataToAWS(ModelSupportRequestAttachement.Data.UploadSupportRequestAttachment uploadSupportRequestAttachment, File file) {
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
        Call<Void> call = service.uploadProfileData(uploadSupportRequestAttachment.getUploadUrl(), requestFile);
        //calling the api
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                YLog.e("Response Upload", response.toString());

                total = total + 1;

                alFileUploadId.add(uploadSupportRequestAttachment.getFileUploadId());

                if (alFile.size() == total) {

                    for (int i = 0; i < alFileUploadId.size(); i++) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("id", alFileUploadId.get(i));
                            YLog.e("Json Object", jsonObject.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jsonArray.put(jsonObject);
                        YLog.e("Json Array", jsonArray.toString());
                    }

                    Input<Object> attachment = new Input<>(jsonArray, true);
                    UpdateSupportRequestMutation updateSupportRequestMutation = new UpdateSupportRequestMutation(supportRequestId, attachment);
                    supportRequestViewModel.updateSupportRequest(updateSupportRequestMutation);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                YLog.e("Response", t.toString());
            }
        });
    }

    private void successDialog() {
        KAlertDialog successDlg = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText(YelloDriverApp.getInstance().getString(R.string.yello_driver))
                .setContentText(getString(R.string.msg_success_supportrequest));
        successDlg.setCanceledOnTouchOutside(false);
        successDlg.setConfirmText(getResources().getString(R.string.lbl_ok));
        successDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                successDlg.dismiss();
                clearAllData();
                removeImageLayout();
            }
        });
        successDlg.show();
    }

    private void clearAllData() {
        alMimeType = new ArrayList<>();
        alFile = new ArrayList<>();
        alFilepath = new ArrayList<>();
        alUploadUrl = new ArrayList<>();
        alFileUploadId = new ArrayList<>();
        jsonArray = new JSONArray();
        edtIssue.setText("");
    }

    private void startContactImageSelection(int reqImageCode) {

        KAlertDialog imagePickAlertDlg = new KAlertDialog(getContext())
                .setTitleText(YelloDriverApp.getInstance().getString(R.string.app_name))
                //.setContentText("Do you want to logout?")
                .setConfirmText(YelloDriverApp.getInstance().getString(R.string.title_camera))
                .setCancelText(YelloDriverApp.getInstance().getString(R.string.title_gallery));

        imagePickAlertDlg.setCanceledOnTouchOutside(true);
        imagePickAlertDlg.setConfirmClickListener(kAlertDialog -> {
            imagePickAlertDlg.dismiss();
            ImagePicker.Companion.with(ContactUsFragment.this)
                    .cameraOnly()
                    //.crop()
                    //.compress(1024)
                    //.maxResultSize(1080, 1080)
                    .saveDir(new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name)))
                    .start(reqImageCode);


        });
        imagePickAlertDlg.setCancelClickListener(kAlertDialog -> {
            imagePickAlertDlg.dismiss();
            ImagePicker.Companion.with(ContactUsFragment.this)
                    .galleryOnly()
                    //.crop()
                    //.compress(1024)
                    //.maxResultSize(1080, 1080)
                    .saveDir(new File(Environment.getExternalStorageDirectory(), getResources().getString(R.string.app_name)))
                    .start(reqImageCode);
        });

        imagePickAlertDlg.show();
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_contactus;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_contactus);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        tvName.setText(R.string.mnu_help_desk);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            for (int v = 0; v < fragmentContactLinearImageSection.getChildCount(); v++) {
                int codeTag = (int) fragmentContactLinearImageSection.getChildAt(v).getTag();

                if (requestCode == REQ_IMAGE_CODE + codeTag) {
                    String filePath = ImagePicker.Companion.getFilePath(data);
                    YLog.i("Got result File", filePath + "");

                    ImageView imgMain = fragmentContactLinearImageSection.findViewWithTag(ID_IMAGE_MAIN + codeTag);
                    ImageView imgRemove = fragmentContactLinearImageSection.findViewWithTag(ID_IMAGE_REMOVE + codeTag);
                    ImageView imgFrame = fragmentContactLinearImageSection.findViewWithTag(ID_IMAGE_FRAME + codeTag);

                    Glide.with(this).load(filePath).into(imgMain);
                    imgRemove.setVisibility(View.VISIBLE);
                    imgFrame.setBackgroundResource(R.drawable.drawable_contactus_imagefilled);

                    alFilepath.add(filePath);
                    //You can get File object from intent
                    alFile.add(ImagePicker.Companion.getFile(data));
                    YLog.e("*** FilePath", filePath);
                    YLog.e("*** FileType", Utils.getMimeType(filePath));
                    YLog.e("*** FileName", ImagePicker.Companion.getFile(data).getName());
                    alMimeType.add(Utils.getMimeType(filePath));

//                    YLog.e("*** MimeType", mimeType);
//                    YLog.e("*** Title", file.getName());
                    break;
                }
            }
        }
    }
}