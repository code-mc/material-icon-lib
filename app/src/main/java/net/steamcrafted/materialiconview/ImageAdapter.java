package net.steamcrafted.materialiconview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconUtils;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

/**
 * Created by Wannes2 on 21/07/2015.
 */
public class ImageAdapter extends BaseAdapter {

    List<MaterialDrawableBuilder.IconValue> icons;

    public ImageAdapter(@NonNull List<MaterialDrawableBuilder.IconValue> iconin){
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
            view = new ImageView(viewGroup.getContext());
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        }
        ImageView v = (ImageView) view;
        v.setImageDrawable(
                MaterialDrawableBuilder.with(viewGroup.getContext())
                    .setIcon(icons.get(i))
                    .setColor(Color.BLACK)
                    .setSizePx(viewGroup.getWidth() / 5)
                    .build()
        );
        return v;
    }
}
