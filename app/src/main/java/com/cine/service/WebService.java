package com.cine.service;

/**
 * Created by Renault Nissan Technology & Business Center India Pvt Ltd
 * Copyright (c) 2017 Renault Nissan Technology & Business Center India Pvt Ltd
 */

public class WebService {
    private static final String BASE_URL="http://buyarecaplates.com/";
    private static final String SIGNIN_ENDPOINT="signin";
    private static final String SIGNUP_ENDPOINT="signup";
    private static final String FEED_ENDPOINT="mfeeds";
    private static final String WALL_ENDPOINT="mpwall";
    private static final String WALLPOST_ENDPOINT = "mcwall";
    private static final String WALLPOSTSUBCAT_ENDPOINT = "mscwall";
    private static final String ADS_ENDPOINT = "madvertise";
    private static final String EVENTS_ENDPOINT = "mevents";
    private static final String PROFILE = "mprofile";

    /*get signin url*/
    public  static final String SIGNIN_URL=BASE_URL+SIGNIN_ENDPOINT;

    /*get signui url*/
    public static final String SIGNUP_URL=BASE_URL+SIGNUP_ENDPOINT;


    public static final String FEEDS_URL=BASE_URL+FEED_ENDPOINT;
    public static final String WALLPOST_URL = BASE_URL+WALLPOST_ENDPOINT;
    public static final String WALLPOSTSUBCAT = BASE_URL+WALLPOSTSUBCAT_ENDPOINT;
    public static final String WALLPOST = BASE_URL+WALL_ENDPOINT;
    public static final String ADPOST = BASE_URL+ADS_ENDPOINT;
    public static final String EVENTSURL = BASE_URL+EVENTS_ENDPOINT;
    public static final String USER_PROFILE_URL = BASE_URL+PROFILE;
}
