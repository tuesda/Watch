package com.tuesda.watch.frescohelp;

import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tuesda.watch.R;
import com.tuesda.watch.log.Log;

/**
 * Created by zhanglei on 15/7/31.
 */
public class ImageHelper {

    public static void setupImage(Resources resources, Uri imgUri, Uri placeholder, SimpleDraweeView imageView) {


        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(resources);
        GenericDraweeHierarchy hierarchy = builder.setProgressBarImage(new CustomProgressBar())
                .build();

        imageView.setHierarchy(hierarchy);


        BaseControllerListener controllerListener = new BaseControllerListener() {
            @Override
            public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
                if (animatable != null) {
                    animatable.start();
                }
            }
        };


        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imgUri)
                .setControllerListener(controllerListener)
                .build();



        imageView.setController(controller);
    }

    static class CustomProgressBar extends ProgressBarDrawable {
        @Override
        protected boolean onLevelChange(int level) {
            return super.onLevelChange(level);
        }
    }

}
