package com.app.yellodriver.model;

import com.mapbox.geojson.Point;

public class StoredRoute {
    public StoredRoute() {

    }

    public String getStartAddrs() {
        return startAddrs;
    }

    public void setStartAddrs(String startAddrs) {
        this.startAddrs = startAddrs;
    }

    public String getEndAddrs() {
        return endAddrs;
    }

    public void setEndAddrs(String endAddrs) {
        this.endAddrs = endAddrs;
    }

    public StoredRoute(Point sourcePoint, Point destinationPoint, Point driverLocationPoint, String statusOfRide, String startAddr, String endAddr) {
        startPoint = sourcePoint;
        endPoint = destinationPoint;
        driverPoint = driverLocationPoint;
        rideStatus = statusOfRide;
        startAddrs = startAddr;
        endAddrs = endAddr;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    Point startPoint;
    Point endPoint;
    Point driverPoint;
    String rideStatus="";
    String startAddrs="";
    String endAddrs="";

    public double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public double getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(double durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    double distanceMeters = 0;
    double durationSeconds = 0;
}
