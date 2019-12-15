package net.steamcrafted.materialiconview;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.SupportMenuInflater;
import androidx.appcompat.widget.Toolbar;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconValues;
import net.steamcrafted.materialiconlib.MaterialIconView;
import net.steamcrafted.materialiconlib.MaterialMenuInflater;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    MaterialIconView mIcon;
    GridView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mListview = (GridView) findViewById(R.id.listview);
        //List<MaterialDrawableBuilder.IconValue> vals = Arrays.asList(MaterialDrawableBuilder.IconValue.values());

        ImageAdapter adapt = new ImageAdapter(new MaterialIconValues(this));
        mListview.setAdapter(adapt);

        final Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar_1);
        MaterialMenuInflater.with(toolbar1.getContext(), new SupportMenuInflater(toolbar1.getContext()))
                .inflate(R.menu.menu_nocolor, toolbar1.getMenu());

        final MaterialIconView miv = (MaterialIconView) findViewById(R.id.file_icon);
        miv.setIcon("file_account");

        // Activity Theme materialIconColor attribute (lowest priority default color)
        // View specific Theme e.g. app:theme="..."   (2nd lowest in priority)
        // setDefaultColor(Resource) methods (highest priority default color)
        // app:materialIconColor set on an <item> tag in the menu XML file (overrides any other color choice)

        MaterialMenuInflater
                .with(this)
                .setDefaultColor(Color.BLUE)
                .inflate(R.menu.menu_nocolor, toolbar1.getMenu());


        /*
        setContentView(R.layout.circletest);
        ((CircleImageView)findViewById(R.id.profile_image)).setImageDrawable(
                MaterialDrawableBuilder.with(this)
                        .setSizeDp(96)
                        .setIcon(MaterialDrawableBuilder.IconValue.FACEBOOK_BOX)
                        .setColor(Color.BLUE)
                        .build()
        );
        */


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MaterialMenuInflater.with(this).inflate(R.menu.menu_main, menu);
        return true;
    }


}
