package net.steamcrafted.materialiconlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

public class MaterialIconValues
{

    static protected HashMap<String, Integer> mIconValues;
    Context mContext;
    static protected boolean mHashInitialized = false;

    static
    {
        mIconValues = new HashMap<>();
    }

    public MaterialIconValues(Context context)
    {
        mContext = context;
        if (!mHashInitialized)
        {
            try
            {
                final XmlResourceParser parser = context.getResources().getXml(R.xml.icons);
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if (eventType == XmlPullParser.START_DOCUMENT)
                    {

                        Log.d("XML parsing", "In start document");
                    } else if (eventType == XmlPullParser.START_TAG)
                    {
                        Log.d("XML parsing", "In start tag = " + parser.getName());
                        if ("enum".equals(parser.getName()))
                        {
                            String s = parser.getAttributeValue(null, "name");
                            String sv = parser.getAttributeValue(null, "value");
                            Integer i = Integer.parseInt(sv, 16);

                            mIconValues.put(s, i);
                            Log.d("XML parsing", "Name is = " + parser.getAttributeValue(null, "name"));
                        }
                    } else if (eventType == XmlPullParser.END_TAG)
                    {
                        Log.d("XML parsing", "In end tag = " + parser.getName());

                    } else if (eventType == XmlPullParser.TEXT)
                    {
                        Log.d("XML parsing", "Have text = " + parser.getAttributeValue(7));
                    }
                    eventType = parser.next();

                }
            } catch (IOException | XmlPullParserException e)
            {
                e.printStackTrace();
            }
            mHashInitialized = true;
        }
    }


    static public Integer get(Integer i)
    {
        return (Integer) mIconValues.values().toArray()[i];
    }

    static public Integer size()
    {
        return mIconValues.size();
    }

    static public String getName(Integer i)
    {
        return (String) mIconValues.keySet().toArray()[i];
    }

    public static Integer getValueByName(String name)
    {
        return mIconValues.get(name);
    }

    public static boolean iconExists(String name)
    {
        return (mIconValues.get(name) != null ? true : false);
    }
}


