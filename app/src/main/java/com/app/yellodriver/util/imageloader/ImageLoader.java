package com.app.yellodriver.util.imageloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.OperationDataJsonSerializer;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.app.yellodriver.GenViewUrlQuery;
import com.app.yellodriver.R;
import com.app.yellodriver.YelloDriverApp;
import com.app.yellodriver.appoloservice.ApoloCall;
import com.app.yellodriver.customview.CircleImageView;
import com.app.yellodriver.model.ModelImageDisplay.ModelGenViewUrl;
import com.app.yellodriver.model.ModelRideAccept.RideAcceptModel;
import com.app.yellodriver.util.Utils;
import com.app.yellodriver.util.YLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Image loader class which will use Glide to load images.
 */
public class ImageLoader {


    /**
     * Loads the image from url into ImageView.
     */
    public static void loadGraphQlImage(final Context context, final ImageView imageView, final String file_upload_id, int placeHolderRes) {

        ApoloCall.getApolloClient(YelloDriverApp.getInstance())
                .query(new GenViewUrlQuery(file_upload_id))
                .enqueue(new ApolloCall.Callback<GenViewUrlQuery.Data>() {
                    @Override
                    public void onResponse(@NotNull Response<GenViewUrlQuery.Data> response) {
                        if (!response.hasErrors()) {

//                            YLog.e("Image Response", response.toString());

                            Operation.Data data = (Operation.Data) response.getData();
                            String json = OperationDataJsonSerializer.serialize(data, "  ");
                            Gson gson = new Gson();

                            ModelGenViewUrl modelGenViewUrl = gson.fromJson(json, ModelGenViewUrl.class);

                            ArrayList<ModelGenViewUrl.Data.GenViewUrl> genViewUrl = modelGenViewUrl.getData().getGenViewUrl();

                            Glide.with(YelloDriverApp.getInstance())
                                    .asBitmap()
                                    .load(genViewUrl.get(0).getUrl())
//                                    .placeholder(R.drawable.placeholder_banner)
                                    .error(R.drawable.placeholder_banner)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            imageView.setImageBitmap(resource);
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            imageView.setImageBitmap(null);
                                        }

                                        @Override
                                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                            super.onLoadFailed(errorDrawable);
                                            imageView.setImageDrawable(ContextCompat.getDrawable(YelloDriverApp.getInstance(), R.drawable.placeholder_banner));
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull ApolloException e) {

                    }
                });
    }

    /**
     * Loads the image from url into ImageView.
     */
    public static void loadImage(final Context context, final ImageView imageView, final String imageUrl, int placeHolderRes) {

        Glide.with(context).asBitmap().load(imageUrl).apply(new RequestOptions().placeholder(placeHolderRes).error(placeHolderRes)).into(imageView);
    }

    /**
     * Loads the image resource into ImageView.
     */
    public static void loadImage(final Context context, final ImageView imageView, final int resource, int placeHolderRes) {
        Glide.with(context).asBitmap().load(resource)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(new RequestOptions().placeholder(placeHolderRes)
                        .error(placeHolderRes))
                .into(imageView);
    }

    /**
     * Set rounded image view
     * <p>
     * If want to do something after setting the image so don't use this generalized method. Use it at same place at your own.
     * </p>
     *
     * @param uri       String image Uri
     * @param imageView imageView into set image
     */

    public static void getGlideRounded(final Context context, final String uri, final CircleImageView imageView) {
        Glide.with(context).asBitmap().load(uri).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                //                mImageView.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.img_placeholder, null));
                super.onLoadFailed(errorDrawable);
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_banner));
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_banner));
            }
        });
    }
}
