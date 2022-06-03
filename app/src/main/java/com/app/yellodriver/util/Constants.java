package com.app.yellodriver.util;

/**
 * Manages public constants.
 */
public class Constants {

    /**
     * Max time interval to prevent double click
     */
    public static final long MAX_CLICK_INTERVAL = 1000;
    public static final int GPS_REQUEST = 1111;
    public static final int QR_SCAN_REQUEST = 723;
    public static final int DECLINE_TRIP_REQUEST = 834;
    public static final int LOCATION_REQUEST = 555;
    public static final int LOCATION_REQUEST_SETTINGS = 666;
    public static final int CAMERA_REQUEST = 777;
    public static final int GALLERY_REQUEST = 888;

    public static final String BASE_URL = "https://devhasura.solulab.org/v1/graphql";
    public static final String BASE_URL_SUBSCRIPTION = "wss://devhasura.solulab.org/v1/graphql";
    //    public static final String BASE_URL = "https://api.dev.solulab.org/v1/graphql";
//    public static final String BASE_URL_SUBSCRIPTION = "wss://api.dev.solulab.org/v1/graphql";
//
    public static final String PAPER_BOOK_USER = "USER";
    public static final String PAPER_KEY_TOKEN = "token";
    public static final String PAPER_KEY_FIREBASE_TOKEN = "firebase_token";
    public static final String PAPER_KEY_HASURA_ID = "hasura_id";
    public static final String PAPER_KEY_HASURA_NAME = "hasura_user_name";
    public static final String PAPER_KEY_HASURA_EMAIL = "hasura_email";
    public static final String PAPER_KEY_HASURA_PHONE = "hasura_phone";
    public static final String PAPER_KEY_HASURA_ROLE = "hasura_role";

    public static final String FLOATING_BUTTON_CLICK = "floating_action_button";

    public static final String BUNDLE_PROFILE_DATA = "profile_data";

    public static final String PAPER_KEY_VEHICLE_ID = "vehicle_id";

    public static final String PAPER_KEY_NAV_TO_RIDER = "nav_to_rider";

    public static final String PAPER_KEY_IS_RIDE_ONGOING = "isRideOngoing";

    public static final String INTENT_KEY_ACTION_RIDE_REQUEST_SUBSCRIPTION = "actionRideRequestSubscription";
    public static final String INTENT_KEY_ACTION_LOCATION_UPDATE = "actionUpdateLocaion";
    public static final String INTENT_KEY_DATA_LOCATION_UPDATE = "locationUpdate";
    public static final String INTENT_KEY_DATA_RIDE_REQUEST_SUBSCRIPTION = "rideRequestSubscriptionData";
    public static final String INTENT_KEY_NOTIFICATION_TYPE = "notification_type";

    public static final String INTENT_KEY_LOCATION = "location";
    public static final String INTENT_KEY_LATITUDE = "latitude";
    public static final String INTENT_KEY_LONGITUDE = "longitude";

    public static final String INTENT_KEY_COUNTRY_CODE = "countryCode";
    public static final String INTENT_KEY_MODEL_USER_OTP = "modelUserOtp";
    public static final String INTENT_KEY_ON_BOARD_USER_ID = "onBoardUserId";
    public static final String INTENT_KEY_ORDER_ID = "orderId";

    public static final String PAPER_KEY_CURRENT_RIDE_ID = "current_rideid";
    public static final String PAPER_KEY_RIDER_USER_ID = "rider_userid";
    public static final String PAPER_KEY_RIDER_INFO = "rider_info";

    public static final String PAPER_KEY_RIDE_ID = "rideId";
    public static final String PAPER_KEY_RIDE_REQUEST_ID = "rideRequestId";

    public static final String PAPER_KEY_ONGOING_ROUTE = "nav_ongoingroute";
    public static final String PAPER_KEY_QR_CONFIRM_CODE = "qr_confirmcode";

    public static final String PAPER_KEY_DEVICE_ID = "device_id";
    public static final String PAPER_KEY_ALLOW_VOICE_PLAY = "allow_voice_play";

    public static final String PAPER_KEY_MODEL_CANCEL_REASON = "modelCancelReason";
    public static final String CANCEL_SLUG_NAME = "ride-cancel-reasons";

    public static final String PAPER_KEY_BOARDING_PASS_ID = "boardingPassID";

    public static final String IS_SUBSCRIPTION_CONNECTED = "isSubscriptionConnected";

    public static final String COUNTRY_CODE = "+1";
    public static final String USER_TYPE_RIDER = "rider";

    public static final String PAYMENT_CURRENCY = "inr";

    public static final String RIDE_STATUS_CANCELLED = "CANCELED";
}