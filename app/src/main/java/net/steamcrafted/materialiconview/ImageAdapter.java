package net.steamcrafted.materialiconview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconUtils;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

/**
 * Created by Wannes2 on 21/07/2015.
 */
public class ImageAdapter extends BaseAdapter {

    List<Integer> icons;

    public ImageAdapter(@NonNull List<Integer> iconin) {
        this.icons = iconin;
    }

    @Override
    public int getCount() {
        return icons.size();
    }

    @Override
    public Object getItem(int i) {
        return icons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item, null, false);
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        }
        FrameLayout host = (FrameLayout) view;
        ImageView v = (ImageView) host.getChildAt(0);
        TextView tv = (TextView)  host.getChildAt(1);
        v.setImageDrawable(
                MaterialDrawableBuilder.Companion.with(viewGroup.getContext())
                    .setIcon(icons.get(i))
                    .setColor(Color.BLACK)
                    .setSizePx(viewGroup.getWidth() / 5)
                    .build()
        );
        tv.setText(String.valueOf(icons.get(i)));
        return view;
    }
}
