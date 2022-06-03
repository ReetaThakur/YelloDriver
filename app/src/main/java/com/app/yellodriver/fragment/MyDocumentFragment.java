package com.app.yellodriver.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo.api.Input;
import com.app.yellodriver.DocumentTypeQuery;
import com.app.yellodriver.MyDocumentsQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.VehicleDocQuery;
import com.app.yellodriver.activity.home.HomeActivity;
import com.app.yellodriver.adapters.MyDocumentAdapter;
import com.app.yellodriver.model.ModelDocType.DocTypeModel;
import com.app.yellodriver.model.ModelDocType.DocTypeViewModel;
import com.app.yellodriver.model.ModelDocument.DocumentModel;
import com.app.yellodriver.model.ModelDocument.DocumentViewModel;
import com.app.yellodriver.model.ModelVehicleDoc.VehicleDocModel;
import com.app.yellodriver.model.ModelVehicleDoc.VehicleDocViewModel;
import com.app.yellodriver.model.MyDocItem;
import com.app.yellodriver.util.Constants;
import com.app.yellodriver.util.CustomProgressDialog;
import com.app.yellodriver.util.YLog;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MyDocumentFragment extends BaseFragment {
    RecyclerView recycleMyDoc;

    private DocumentViewModel myDocsViewModel;
    private VehicleDocViewModel myVehicleDocViewModel;
    private DocTypeViewModel docTypeViewModel;
    private ArrayList<MyDocItem> myDocListData;
    private MyDocumentAdapter documentAdapter;

    private CustomProgressDialog customProgressDialog;
    private Activity activity;

    @Override
    protected void initializeComponent(View view) {

        activity = getActivity();

        setUpToolBar(view);

        recycleMyDoc = (RecyclerView) view.findViewById(R.id.recycleMyDoc);
        /*MyDocItem[] myListData = new MyDocItem[]{
                new MyDocItem("Driver Licence", "https://i.ibb.co/gM880S5/c8bf2a0e-e282-4876-8c4d-812db703257b.png"),
                new MyDocItem("Vehicle Insurance",null),
                new MyDocItem("Number Plate",null),
                new MyDocItem("Vehicle Fitness Certificate",null),
                new MyDocItem("ID Proof",null),
                new MyDocItem("Profile Photo",null),
                new MyDocItem("Email",null),
                new MyDocItem("Vehicle Reg.Certificate",null)
        };*/
        myDocListData = new ArrayList<>();
        documentAdapter = new MyDocumentAdapter(getActivity(), myDocListData);

        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recycleMyDoc.setLayoutManager(manager);
        recycleMyDoc.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recycleMyDoc.setAdapter(documentAdapter);


        setUpViewModel();

        customProgressDialog = new CustomProgressDialog(activity);
        customProgressDialog.showDialog();

        docTypeViewModel.getDocTypeData(new DocumentTypeQuery());
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_mydocument;
    }

    private void setUpToolBar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_mydoc);
        TextView tvName = view.findViewById(R.id.toolbar_tvTitle);
        ImageView imgBack = view.findViewById(R.id.toolbar_imgBack);
        tvName.setText(R.string.mnu_mydocuments);
        imgBack.setOnClickListener(view1 -> getFragmentManager().popBackStack());
    }

    private void setUpViewModel() {

        docTypeViewModel = ViewModelProviders.of((HomeActivity) getActivity()).get(DocTypeViewModel.class);
        docTypeViewModel.docTypeMutableApoloResponse.observe(((HomeActivity) getActivity()), docTypeObserverApoloResponse);

        myDocsViewModel = ViewModelProviders.of((HomeActivity) getActivity()).get(DocumentViewModel.class);
        myDocsViewModel.mutableLiveDataDocList.observe(((HomeActivity) getActivity()), driverDocObserverApoloResponse);

        myVehicleDocViewModel = ViewModelProviders.of((HomeActivity) getActivity()).get(VehicleDocViewModel.class);
        myVehicleDocViewModel.mutableLiveDataVehicleData.observe(((HomeActivity) getActivity()), vehicleDocObserverApoloResponse);

    }

    ArrayList<String> docTypeIdList = new ArrayList<>();
    Observer<List<DocTypeModel.Data.Yt_doc_type>> docTypeObserverApoloResponse = listContent -> {
        if (listContent != null) {
            ArrayList<MyDocItem> temDocTypeList = new ArrayList<>();

            for (int q = 0; q < listContent.size(); q++) {
                String title = listContent.get(q).getTitle();
                String desc = listContent.get(q).getDescription();
                String docTypeID = listContent.get(q).getId();
                YLog.e("title:description:typeId", title + ":" + desc + ":" + docTypeID);

                MyDocItem myDocItem = new MyDocItem();
                myDocItem.setTitle(title);
                myDocItem.setDescription(desc);
                myDocItem.setImgId("");
                myDocItem.setDocTypeId(docTypeID);
//                myDocItem.setFileUploadID();

                temDocTypeList.add(myDocItem);

                docTypeIdList.add(docTypeID);
            }

            myDocListData.addAll(temDocTypeList);
            documentAdapter.notifyDataSetChanged();

            if (listContent.size() > 0) {
                //We have docTypes so now get doc list of the user
                myDocsViewModel.getDocumentsList(new MyDocumentsQuery());
            }
        } else {
            //emptyNotifyview.setVisibility(View.VISIBLE);
            //rvMyNotifs.setVisibility(View.GONE);
        }
    };

    Observer<List<DocumentModel.DocumentData.YtDriverDoc>> driverDocObserverApoloResponse = listContent -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (listContent != null) {
                for (int q = 0; q < listContent.size(); q++) {
                    String typeId = listContent.get(q).getDocTypeId();
                    String fileUploadID = listContent.get(q).getFile_upload_id();
                    YLog.e("typeId:fileID", typeId + ":" + fileUploadID);
                    int idx = docTypeIdList.indexOf(typeId);
                    if (idx >= 0) {
                        myDocListData.get(idx).setFileUploadID(fileUploadID);
                        documentAdapter.notifyDataSetChanged();
                    }
                }

                Input<Object> vehicleId = new Input<>(Paper.book(Constants.PAPER_BOOK_USER).read(Constants.PAPER_KEY_VEHICLE_ID,""),true);
                myVehicleDocViewModel.getDocumentsList(new VehicleDocQuery(vehicleId));
            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };
    Observer<List<VehicleDocModel.Data.YtVehicleDoc>> vehicleDocObserverApoloResponse = ytVehicleDocs -> {
        if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {

            if (customProgressDialog != null) {
                customProgressDialog.dismissDialog();
            }
            if (ytVehicleDocs != null) {
                for (int q = 0; q < ytVehicleDocs.size(); q++) {
                    String typeId = ytVehicleDocs.get(q).getDocTypeId();
                    String fileUploadID = ytVehicleDocs.get(q).getFileUploadId();
                    YLog.e("typeId:fileID", typeId + ":" + fileUploadID);
                    int idx = docTypeIdList.indexOf(typeId);
                    if (idx >= 0) {
                        myDocListData.get(idx).setFileUploadID(fileUploadID);
                        documentAdapter.notifyDataSetChanged();
                    }
                }
            } else {
                //emptyNotifyview.setVisibility(View.VISIBLE);
                //rvMyNotifs.setVisibility(View.GONE);
            }
        }
    };
}