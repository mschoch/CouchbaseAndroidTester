package com.couchbase.androidtester.workloads.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.ektorp.AttachmentInputStream;

import android.content.res.AssetManager;
import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.androidtester.workloads.CouchbaseWorkload;

public class PhotoShare extends CouchbaseWorkload {

    @Override
    protected String performWork() {

        AssetManager assetManager = context.getAssets();

        int photosUploaded = 0;
        while(!task.isCancelled()) {

            Map<String,Object> document = documentTemplate();
            couchDbConnector.create(document);
            String id = (String)document.get("_id");
            String rev = (String)document.get("_rev");

            String filename = "small.jpg";
            if(photosUploaded % 2 == 0) {
                filename = "large.jpg";
            }

            try {
                AttachmentInputStream ais = new AttachmentInputStream(filename, assetManager.open("attachments/images/" + filename), "image/jpeg");
                couchDbConnector.createAttachment(id, rev, ais);
                photosUploaded++;
                task.publishWorkProgress("Uploaded Photo " + photosUploaded);
            } catch (IOException e) {
                Log.e(CouchbaseAndroidTesterActivity.TAG, "Error reading attachment", e);
            }

            try {
                int delayBetweenPosts = 1000 * (500 + new Random().nextInt(500));
                Thread.sleep(delayBetweenPosts);
            } catch (InterruptedException e) {
                //ignore
            }

        }


        String resultMessage = "" +  photosUploaded + " photos uploaded";

        if(task.isCancelled()) {
            resultMessage = "Cancelled PhotoShare after " + resultMessage;
        }
        else {
            resultMessage = "Finished PhotoShare " + resultMessage;
        }
        progress = 0;
        return resultMessage;

    }


    protected HashMap<String, Object> documentTemplate() {
        HashMap<String, Object> exif = new HashMap<String, Object>();
        exif.put("ColorSpace", 1);
        exif.put("PixelYDimension", 2048);
        exif.put("DateTimeOriginal", "2011:07:03 12:10:49");
        exif.put("DateTimeDigitized", "2011:07:03 12:10:49");
        exif.put("PixelXDimension", 1536);

        HashMap<String, Object> tiff = new HashMap<String, Object>();
        tiff.put("Make", "Apple");
        tiff.put("Model", "iPhone 3GS");
        tiff.put("YResolution", 72);
        tiff.put("DateTime", "2011:07:03 12:10:49");
        tiff.put("Software", "4.3.3");
        tiff.put("XResolution", 72);

        HashMap<String, Object> gyro = new HashMap<String, Object>();
        gyro.put("x", 4.301908592578078e-78);
        gyro.put("y", 7.065152736643451e-307);
        gyro.put("z", -7.382861389261252e+306);

        HashMap<String, Object> attitude = new HashMap<String, Object>();
        attitude.put("pitch", 0);
        attitude.put("roll", 0);
        attitude.put("yaw", 0);

        HashMap<String, Object> accel = new HashMap<String, Object>();
        accel.put("x", -0.0543365478515625);
        accel.put("y", -0.8150482177734375);
        accel.put("z", -0.6339263916015625);

        HashMap<String, Object> motionData = new HashMap<String, Object>();
        motionData.put("gyro", gyro);
        motionData.put("attitude", attitude);
        motionData.put("accel", accel);

        HashMap<String, Object> media = new HashMap<String, Object>();
        media.put("DPIHeight", 72);
        media.put("{Exif}", exif);
        media.put("DPIWidth",  72);
        media.put("Orientation", 6);
        media.put("{TIFF}", tiff);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("mediaMetaData", media);
        result.put("motionData", motionData);
        return result;
    }

    @Override
    public String getName() {
        return "Upload Photos";
    }

}
