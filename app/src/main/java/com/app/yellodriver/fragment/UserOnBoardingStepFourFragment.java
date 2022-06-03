package com.app.yellodriver.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.yellodriver.BoardingPassSubscription;
import com.app.yellodriver.R;
import com.app.yellodriver.RideRequestSubscription;
import com.app.yellodriver.SendBoardingPassMutation;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.model.ModelStripe.StripeViewModel;
import com.app.yellodriver.model.ModelUserOnboarding.ModelBoardingPass.ModelBoardingPassSubscription;
import com.app.yellodriver.model.ModelUserOnboarding.ModelSendBoardingPass;
import com.app.yellodriver.model.ModelUserOnboarding.UserOnboardingViewModel;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.DateUtils;
import com.developer.kalert.KAlertDialog;

import java.util.ArrayList;

public class UserOnBoardingStepFourFragment extends BaseFragment {

    //    private Button btnSend;
    private String orderId;
    private Activity mActivity;
    private CustomProgressDialog customProgressDialog;
    private UserOnboardingViewModel viewModel;

    private TextView txtPassType;
    private TextView txtPassId;
    private TextView txtPurchaseDate;
    private TextView txtValidity;
    private TextView txtAmount;
    private Button btnSendPass;

    private String boardingPassId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    protected void initializeComponent(View view) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            orderId = bundle.getString(Constants.INTENT_KEY_ORDER_ID);
        }

        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        Button btnCancel = view.findViewById(R.id.toolbar_btnCancel);

        txtPassType = view.findViewById(R.id.fragment_user_onboarding_step_four_tvPassTypeValue);
        txtPassId = view.findViewById(R.id.fragment_user_onboarding_step_four_tvPassIDValue);
        txtAmount = view.findViewById(R.id.fragment_user_onboarding_step_four_tvAmountValue);
        txtPurchaseDate = view.findViewById(R.id.fragment_user_onboarding_step_four_tvPurchasedValue);
        txtValidity = view.findViewById(R.id.fragment_user_onboarding_step_four_tvValdityValue);

        btnSendPass = view.findViewById(R.id.fragment_user_onboarding_step_four_btnSend);

        btnCancel.setVisibility(View.VISIBLE);

        tvName.setText(R.string.lbl_user_boarding);

        imgBack.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        btnCancel.setOnClickListener(view1 -> {
            getFragmentManager().popBackStack();
        });

        viewModel = ViewModelProviders.of((HomeActivity) mActivity).get(UserOnboardingViewModel.class);
        viewModel.liveDataBoardingPass.observe(getViewLifecycleOwner(), observerBoardingPlan);
        viewModel.liveDataSendPass.observe(getViewLifecycleOwner(), observerSendPass);

        customProgressDialog = new CustomProgressDialog(mActivity);
        customProgressDialog.showDialog();

        viewModel.boardingPassSubscription = new BoardingPassSubscription(orderId);
        viewModel.getBoardingPassSubscription();

        btnSendPass.setOnClickListener(view12 -> {
            // on click to open dialog

            customProgressDialog = new CustomProgressDialog(mActivity);
            customProgressDialog.showDialog();

            SendBoardingPassMutation sendBoardingPassMutation = new SendBoardingPassMutation(boardingPassId);
            viewModel.sendBoardingPass(sendBoardingPassMutation);
        });
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_user_onboarding_step_four;
    }

    Observer<ModelSendBoardingPass.Data.SendBoardingPass> observerSendPass = sendBoardingPass -> {
        if (customProgressDialog != null) {
            customProgressDialog.dismissDialog();
        }

        KAlertDialog successDlg = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Rider Onboarded !!")
                .setContentText("The app link has been shared with Rider. Happy Riding!!")
                .setConfirmText("Back to home");
        successDlg.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
            @Override
            public void onClick(KAlertDialog kAlertDialog) {
                successDlg.dismiss();
                getFragmentManager().popBackStack(HomeFragment.class.getSimpleName(), 0);
            }
        });
        successDlg.setCanceledOnTouchOutside(false);
        successDlg.setCancelable(false);
        successDlg.show();
    };

    Observer<ArrayList<ModelBoardingPassSubscription.Data.YtBoardingPas>> observerBoardingPlan = ytBoardingPas -> {

        if (ytBoardingPas != null) {
            if (ytBoardingPas.size() > 0) {

                if (customProgressDialog != null) {
                    customProgressDialog.dismissDialog();
                }

                boardingPassId = ytBoardingPas.get(0).getId();
                txtPassType.setText(ytBoardingPas.get(0).getPlan().getTitle());
                txtPassId.setText(ytBoardingPas.get(0).getPlan().getId());
                txtPurchaseDate.setText(DateUtils.convertDate(DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS, DateUtils.DATE_FORMAT_DD_MM_YYYY, ytBoardingPas.get(0).getPurchasedAt()));
                txtValidity.setText(ytBoardingPas.get(0).getPlan().getValidityDays() + " days");
                txtAmount.setText("$" + ytBoardingPas.get(0).getPlan().getPrice());

                viewModel.cancelSubscription();
            }
        }
    };
}