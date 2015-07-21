package net.steamcrafted.materialiconview;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

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
            view = new MaterialIconView(viewGroup.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        MaterialIconView v = (MaterialIconView) view;
        v.setIcon(icons.get(i));
        v.setSizePx(viewGroup.getWidth()/5);
        v.setColor(Color.BLACK);
        return v;
    }
}
