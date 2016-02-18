package br.com.sitecom.warkings.br.com.sitecom.warkings.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by alexandre on 1/10/16.
 */
public class StageUtils {

    public static int getAdaptiveHeight(int percentage) {
        return Math.round((Gdx.graphics.getHeight() * percentage)/100);
    }

    public static int getAdaptiveWidth(int percentage) {
        return Math.round((Gdx.graphics.getWidth() * percentage)/100);
    }

    public static int percentualToPixel(int percentage, int size) {
        return (size * percentage)/100;
    }
}
