package com.module.framework.binding.viewadapter.image;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 *  on 2017/6/18.
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(final ImageView imageView, String url, int placeholderRes) {
        if (url==null){
            return;
        }
        Glide.with(imageView.getContext())
                    .load(url)
                    .apply(
                            new RequestOptions()
                                    .placeholder(imageView.getDrawable())
                                    .error(placeholderRes)
                                    .dontAnimate()
//                                    .centerCrop()
                    )
                    .into(imageView);
    }
}

