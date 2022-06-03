package com.app.yellodriver.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class UserGudesModel {
    public static HashMap<String, List<String>> getData() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();

        ArrayList<String> titleList = new ArrayList<>();

        List<String> acceptingInfo = new ArrayList<String>();
        acceptingInfo.add("contact infromation, ID issue ,and expiry date, financial records.,cridet infromation, medical history where on travel and intance to aquires googd and service");

        List<String> afterRideInfo = new ArrayList<String>();
        afterRideInfo.add("After Ride Step 1");
        afterRideInfo.add("After Ride Step 2");

        List<String> youtubeLinks = new ArrayList<String>();
        youtubeLinks.add("For more info check this video: https://www.youtube.com/watch?v=C0DPdy98e4c");

        List<String> empty = new ArrayList<String>();

        //-----Just Trying the different way to add title-----
        titleList.add("Accept a ride");
        titleList.add("Taking a ride");
        titleList.add("After a ride");
        titleList.add("Renew Rider's boarding pass");
        titleList.add("My trips");
        titleList.add("Safety Measurements");
        titleList.add("Privacy");
        titleList.add("Insurance Coverage");
        titleList.add("Documents");
        titleList.add("Analytics");
        //----------

        expandableListDetail.put(titleList.get(0), acceptingInfo);
        expandableListDetail.put(titleList.get(1), youtubeLinks);
        expandableListDetail.put(titleList.get(2), afterRideInfo);
        expandableListDetail.put(titleList.get(3), acceptingInfo);
        expandableListDetail.put(titleList.get(4), youtubeLinks);
        expandableListDetail.put(titleList.get(5), afterRideInfo);
        expandableListDetail.put(titleList.get(6), acceptingInfo);
        expandableListDetail.put(titleList.get(7), youtubeLinks);
        expandableListDetail.put(titleList.get(8), empty);
        expandableListDetail.put(titleList.get(9), afterRideInfo);

        return expandableListDetail;
    }
}
