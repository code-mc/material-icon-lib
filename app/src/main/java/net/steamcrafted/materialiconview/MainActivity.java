package net.steamcrafted.materialiconview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialIconView mIcon;
    GridView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListview = (GridView) findViewById(R.id.listview);
        List<MaterialDrawableBuilder.IconValue> vals = Arrays.asList(MaterialDrawableBuilder.IconValue.values());

        ImageAdapter adapt = new ImageAdapter(vals);
        mListview.setAdapter(adapt);

        /*mIcon = (MaterialIconView) findViewById(R.id.icon);
        ImageView imgicon = (ImageView) findViewById(R.id.image_icon);

        mIcon.setIcon(MaterialDrawableBuilder.IconValue.CONTENT_COPY);
        imgicon.setImageDrawable(
                MaterialDrawableBuilder.with(this)
                        .setColor(Color.WHITE)
                        .setToActionbarSize()
                        .setIcon(MaterialDrawableBuilder.IconValue.AMAZON_CLOUDDRIVE)
                        .build()
        );
        */
    }
}
