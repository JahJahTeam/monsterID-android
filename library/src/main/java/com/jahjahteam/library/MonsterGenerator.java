package com.jahjahteam.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import java.util.Random;

/**
 * Created by Alexandre on 02-06-2015.
 */
public class MonsterGenerator {

    String order[] = {"legs","clegs", "hair","chair", "arms","carms", "body","cbody", "eyes","ceyes", "mouth","cmouth"};
    Context ctx;
    HashMap parts;

    public MonsterGenerator(Context ctx) {
        this.ctx = ctx;
        parts = new HashMap();
    }


    public Bitmap getMyMonster(String stringId,int color) {


        getRandomBodyParts(stringId);
        Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.back);


        for (int i = 0; i < this.parts.size(); i+=2) {
            //get part from resource
            String s = (new StringBuilder()).append(order[i]).append("_").append(this.parts.get(order[i])).toString();
            int j = ctx.getResources().getIdentifier(s, "drawable", ctx.getPackageName());

            //apply color
            Bitmap partColored = colorizeThisPart(BitmapFactory.decodeResource(ctx.getResources(),j),(float)this.parts.get(order[i+1]));

            //overlay to previous monster
            bitmap = overlay(bitmap, partColored);
        }

        Bitmap auxBack = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        auxBack.eraseColor(color);

        bitmap = overlay(auxBack,bitmap);
        return bitmap;
    }

    private void getRandomBodyParts(String stringID) {

        String original = stringID;
        MessageDigest md = null;
        StringBuffer sb = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            byte[] digest = md.digest();

            sb = new StringBuffer();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String md5 = sb.toString();
        String substring = md5.substring(0, 12);

        long seedValue = hex2decimal(substring);

        Random rand = new Random();
        rand.setSeed(seedValue);

        int legs = (rand.nextInt(18 - 1) + 1);
        int hair = (rand.nextInt(12 - 1) + 1);
        int arms = (rand.nextInt(14 - 1) + 1);
        int body = (rand.nextInt(20 - 1) + 1);
        int eyes = (rand.nextInt(20 - 1) + 1);
        int mouth = (rand.nextInt(17 - 1) + 1);

        float clegs = (rand.nextFloat());
        float chair = (rand.nextFloat());
        float carms = (rand.nextFloat());
        float cbody = (rand.nextFloat());
        float ceyes = (rand.nextFloat());
        float cmouth = (rand.nextFloat());

        this.parts.put("mouth", mouth);
        this.parts.put("cmouth", cmouth);

        this.parts.put("eyes", eyes);
        this.parts.put("ceyes", ceyes);

        this.parts.put("body", body);
        this.parts.put("cbody", cbody);

        this.parts.put("hair", hair);
        this.parts.put("chair", chair);

        this.parts.put("legs", legs);
        this.parts.put("clegs", clegs);

        this.parts.put("arms", arms);
        this.parts.put("carms", carms);



    }


    private Bitmap colorizeThisPart(Bitmap partToColorize, float color){

        Bitmap bitmap1 = Bitmap.createBitmap(partToColorize.getWidth(), partToColorize.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap1);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(generateRandomColor(color), android.graphics.PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(partToColorize, 0.0F, 0.0F, paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.LIGHTEN));
        canvas.drawBitmap(partToColorize, 0.0F, 0.0F, paint);
        return bitmap1;
    }

    private int generateRandomColor(float hue){
        //Log.d("color",hue+"");

        //int colorPos = 0;
        double goldenRatioConj = (1.0 + Math.sqrt(5.0)) / 2.0;
        //float hue = new Random().nextFloat();

        hue += goldenRatioConj;
        hue = hue % 1;
        hue = Math.round(hue * 360);
        float[] hsvTmp = {hue, 0.5f, 0.95f};

        return Color.HSVToColor(hsvTmp);
    }

    private Bitmap overlay(Bitmap bitmap, Bitmap bitmap1) {
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, new Matrix(), null);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        return bitmap2;
    }
    
    //aux
    private static int hex2decimal(String s) {
        String digits = "0123456789ABCDEF";
        s = s.toUpperCase();
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }
}