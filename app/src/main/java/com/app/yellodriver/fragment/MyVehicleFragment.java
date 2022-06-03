package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.yellodriver.MyVehicleQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelVehicle.VehicleModel;
import com.app.yellodriver.model.ModelVehicle.VehicleViewModel;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;
import com.app.yellodriver.util.YLog;
import com.app.yellodriver.util.imageloader.ImageLoader;
import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.List;

public class MyVehicleFragment extends BaseFragment {
    private static final int REQ_IMAGE = 532;
    ImageView imgCurrentVehicleImage;

    private VehicleViewModel model;
    private TextView tvMyVehicleType;
    private TextView tvMyVehicleName;
    private TextView tvMyVehicleNumber;
    private TextView tvMyVehicleInsured;
    private TextView tvMyVehicleFit;
    private TextView tvMyVehicleAge;
    private TextView tvEmptyVehicleInfo;
    private ScrollView scrollViewVehicleInfo;

    private boolean insurance = false;
    private boolean fitness = false;

    private CustomProgressDialog customProgressDialog;
    private Activity activity;

    @Override
    protected void initializeComponent(View view) {

        activity = getActivity();

        setUpToolBar(view);
        setUpViewModel();

        tvMyVehicleType = view.findViewById(R.id.tvvehicletype);
        tvMyVehicleName = view.findViewById(R.id.tvvehiclename);
        tvMyVehicleNumber = view.findViewById(R.id.tvvehiclenumber);
        tvMyVehicleInsured = view.findViewById(R.id.tvvehicleinsured);
        tvMyVehicleFit = view.findViewById(R.id.tvvehicleisfit);
        tvMyVehicleAge = view.findViewById(R.id.tvvehicleyears);

        tvEmptyVehicleInfo = view.findViewById(R.id.tvnovehicleinfo);
        scrollViewVehicleInfo = view.findViewById(R.id.svmyvehicleinfo);

        imgCurrentVehicleImage = view.findViewById(R.id.imgCurrentVehicleImage);

        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        model.getVehicleData(new MyVehicleQuery());
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_myvehicle;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_vehicleinfo);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
//        tvName.setText(R.string.mnu_myvehicle);
        toolbar.setBackgroundColor(ContextCompat.getColor(YelloDriverApp.getInstance(), R.color.colorTransparent));
        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    private void setUpViewModel() {

        model = ViewModelProviders.of((HomeActivity) getActivity()).get(VehicleViewModel.class);

        model.mutableApoloResponse.observe(((HomeActivity) getActivity()), observerApoloResponse);

    }

    Observer<List<VehicleModel.VehicleData.YtVehicle>> observerApoloResponse = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            // your code here ...

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }

            if (listContent != null) {
                for (int q = 0; q < listContent.size(); q++) {

                    tvEmptyVehicleInfo.setVisibility(View.GONE);
                    scrollViewVehicleInfo.setVisibility(View.VISIBLE);

                    //String regDate = listContent.get(q).getRegistrationDate();
                    String regNum = listContent.get(q).getRegistrationNumber();
                    String make = listContent.get(q).getVehicleMake().getTitle();
                    String model = listContent.get(q).getVehicleModel().getTitle();
                    String type = listContent.get(q).getVehicleType().getTitle();
                    String imageFileId = listContent.get(q).getPhoto_file_id();
                    String age = listContent.get(q).getRegistrationDate();

                    if (listContent.get(q).getVehicleDocs() != null) {
                        for (int i = 0; i < listContent.get(q).getVehicleDocs().size(); i++) {
                            if (listContent.get(q).getVehicleDocs().get(i).getDocType() != null) {
                                if (listContent.get(q).getVehicleDocs().get(i).getDocType().getSlug().equals("RC")) {
                                    insurance = true;
                                    continue;
                                }
                            }
                        }
                    }

                    if (listContent.get(q).getVehicleDocs() != null) {
                        for (int i = 0; i < listContent.get(q).getVehicleDocs().size(); i++) {
                            if (listContent.get(q).getVehicleDocs().get(i).getDocType() != null) {
                                if (listContent.get(q).getVehicleDocs().get(i).getDocType().getSlug().equals("VI")) {
                                    fitness = true;
                                    continue;
                                }
                            }
                        }
                    }

                    if (imageFileId != null) {
                        ImageLoader.loadGraphQlImage(YelloDriverApp.getInstance(), imgCurrentVehicleImage, imageFileId, R.drawable.placeholder_banner);
                    }

                    tvMyVehicleType.setText(type + "");
                    tvMyVehicleName.setText(make + " " + model);
                    tvMyVehicleNumber.setText(regNum + "");
                    if (insurance) {
                        tvMyVehicleInsured.setText("YES");
                    } else {
                        tvMyVehicleInsured.setText("NO");
                    }

                    if (fitness) {
                        tvMyVehicleFit.setText("YES");
                    } else {
                        tvMyVehicleFit.setText("NO");
                    }
//                    tvMyVehicleAge.setText(DateUtils.age(age));
                    tvMyVehicleAge.setText(DateUtils.age(age));
                    //tvMyVehicleAge.setText(regDate+"");
                    return;
                }
            }
            {
                tvEmptyVehicleInfo.setVisibility(View.VISIBLE);
                scrollViewVehicleInfo.setVisibility(View.GONE);
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_IMAGE) {
                //YLog.e("Got result","For Image");
                //Uri path = data.getData();
                //imgCurrentVehicleImage.setImageURI(path);
                String filePath = ImagePicker.Companion.getFilePath(data);
                YLog.e("Got result File", filePath + "");
                Glide.with(this).load(filePath).into(imgCurrentVehicleImage);

            }
        }
    }
}
