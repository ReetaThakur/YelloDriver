package com.app.yellodriver.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.yellodriver.R;
import com.app.yellodriver.adapters.HelpReasonAdapter;
import com.app.yellodriver.model.HelpReasonModel;

import java.util.ArrayList;

public class HelpDeskFragment extends BaseFragment {

    private RecyclerView recyclerHelpReason;
    private HelpReasonAdapter helpRsnAdapter;
    private ArrayList<HelpReasonModel> reasonList;
    private Button btnRequestHelp;

    @Override
    protected void initializeComponent(View view) {

        Toolbar toolbar = view.findViewById(R.id.toolbar_helpdesk);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);

        tvName.setText(R.string.mnu_help_desk);

        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());

        recyclerHelpReason = view.findViewById(R.id.recyclerhelpdesk);
        reasonList = Resonlist();

        helpRsnAdapter = new HelpReasonAdapter(getActivity(), reasonList);
        recyclerHelpReason.setAdapter(helpRsnAdapter);
        recyclerHelpReason.setHasFixedSize(true);
        recyclerHelpReason.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnRequestHelp = view.findViewById(R.id.btnrequesthelp);
        btnRequestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpSubmitSuccess();
            }
        });

    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_helpdesk;
    }

    public ArrayList<HelpReasonModel> Resonlist() {
        ArrayList<HelpReasonModel> lisr = new ArrayList<>();
        HelpReasonModel model = new HelpReasonModel();
        model.setHelpReasonId("1");
        model.setHelpReason("SOS not working");
        lisr.add(model);

        HelpReasonModel model1 = new HelpReasonModel();
        model1.setHelpReasonId("2");
        model1.setHelpReason("Qr scanning problem");
        lisr.add(model1);

        HelpReasonModel model2 = new HelpReasonModel();
        model2.setHelpReasonId("3");
        model2.setHelpReason("Issues in app");
        lisr.add(model2);

        HelpReasonModel model3 = new HelpReasonModel();
        model3.setHelpReasonId("4");
        model3.setHelpReason("Yello Driver app not working");
        lisr.add(model3);

        HelpReasonModel model4 = new HelpReasonModel();
        model4.setHelpReasonId("5");
        model4.setHelpReason("Car engine problem");
        lisr.add(model4);


        HelpReasonModel model5 = new HelpReasonModel();
        model5.setHelpReasonId("6");
        model5.setHelpReason("Car engine oil");
        lisr.add(model5);

        HelpReasonModel model6 = new HelpReasonModel();
        model6.setHelpReasonId("7");
        model6.setHelpReason("Reason 7");
        lisr.add(model6);

        HelpReasonModel model7 = new HelpReasonModel();
        model7.setHelpReasonId("8");
        model7.setHelpReason("Reason 8");
        lisr.add(model7);


        HelpReasonModel model8 = new HelpReasonModel();
        model8.setHelpReasonId("9");
        model8.setHelpReason("Reason 9");
        lisr.add(model8);

        return lisr;
    }

    public void helpSubmitSuccess() {
        Dialog rideCompletedDialog = new Dialog(getActivity(), R.style.FullHeightDialog);
        rideCompletedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        rideCompletedDialog.setContentView(R.layout.ride_success_dialog);

        TextView txtView = rideCompletedDialog.findViewById(R.id.success_dialog_txtMsg);
        txtView.setText(getActivity().getResources().getString(R.string.msg_help_request_success));

        rideCompletedDialog.setCancelable(true);

        //now that the dialog is set up, it's time to show it
        rideCompletedDialog.show();
    }

}
