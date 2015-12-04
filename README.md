# Material Icon Library

A library containing over 1000 material vector icons that can be easily used as Drawable or as a standalone View. Tired of having to search for and generate png resources every time you want to test something? This library puts an end to that burden and makes swapping icons a breeze, check out the usage below and you'll see why.

# Demo

![](http://i.imgur.com/KXHfXo8.gif)

# Stats

 - MinSdk 4
 - LIVE previews and code completion in the Android Studio Designer (Does NOT work out of the box, see step 0 below!)
 - Currently contains 1341 icons, you can look at them here: https://materialdesignicons.com
 - Configured in less than a minute
 - Adds about 200kb to your apk (so a wopping average of __170 bytes per icon__)

# Usage

## Step 0

Now I still have your attention, to get the previews to work in Android Studio you'll have to put the font file inside the assets of your project yourself. Due to a bug it does not think about looking inside the library's assets for some odd reason.

Get the font file [__here__](https://github.com/code-mc/material-icon-lib/blob/master/materialiconlib/src/main/assets/materialdesignicons-webfont.ttf?raw=true).

You don't have to worry about android including the file twice in your apk. Android Studio recognizes the duplicate file name and only keeps one copy in your apk!

## Step 1

#### Gradle

```groovy
dependencies {
    compile 'net.steamcrafted:materialiconlib:1.0.7'
}
```

## Step 2

You now have the choice of using the provided `MaterialIconView` or just your preferred ImageView and use the `MaterialDrawable` as Drawable resource. Let's first go over the provided view:

Add the view to your XML:

```xml
<net.steamcrafted.materialiconlib.MaterialIconView
    xmlns:app="http://schemas.android.com/apk/res-auto" <!-- VERY IMPORTANT -->

    android:layout_width="48dp"
    android:layout_height="48dp"
    app:materialIcon="clipboard_arrow_down" <!-- This sets the icon, HAS AUTOCOMPLETE ;) -->
    app:materialIconColor="#fff" <!-- Sets the icon color -->
    app:materialIconSize="24dp"  <!-- Sets the icon size -->
    android:scaleType="center" <!-- Centers the icon (all scale types supported) -->
    android:background="@android:color/darker_gray"
    android:id="@+id/icon"
/>
```

You can also use the other route: the "wrap_content" way:

```xml
<net.steamcrafted.materialiconlib.MaterialIconView
    xmlns:app="http://schemas.android.com/apk/res-auto" <!-- VERY IMPORTANT -->

    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="12dp" <!-- now we use a padding to center the icon -->
    app:materialIcon="clipboard_arrow_down" <!-- This sets the icon, HAS AUTOCOMPLETE ;) -->
    app:materialIconColor="#fff" <!-- Sets the icon color -->
    app:materialIconSize="24dp"  <!-- Sets the icon size -->
    <!-- scaleType is no longer required for this method -->
    android:background="@android:color/darker_gray"
    android:id="@+id/icon"
/>
```

The view is inherited from `ImageView`. This means that you can use any and all features of the very flexible `ImageView` BUT be reminded that this view does not cache any of the drawables it creates, so every time you change something about the icon, it's going to regenerate the drawable. Using this view inside listviews is highly discouraged, if you want to use these icons in a `ListView`, cache the drawables and use the `MaterialDrawableBuilder` in combination with an `ImageView`!

As mentioned before this extends the android `ImageView` class, there's a few methods unique to this particular view:

```java
// Sets the icon, all 1000+ icons are available inside the MaterialDrawableBuilder.IconValue enum
yourMaterialIconView.setIcon(IconValue iconValue);

// Sets the size of the icon to the default action bar icon size
yourMaterialIconView.setToActionbarSize();

// Provide a dimension resource to use as icon size
yourMaterialIconView.setSizeResource(int dimenRes);

// Set the icon size using a value in dp units
yourMaterialIconView.setSizeDp(int size);

// Set the icon size using a pixel value
yourMaterialIconView.setSizePx(int size);

// Set the icon color
yourMaterialIconView.setColor(int color);

// Set the icon color using a color resource
yourMaterialIconView.setColorResource(int colorRes);

// Set the icon's alpha value (0-255) 0 for completely transparent
yourMaterialIconView.setAlpha(int alpha);

// Sets a custom colorfilter to the drawing paint (for the more advanced devs)
yourMaterialIconView.setColorFilter(ColorFilter cf);

// Clear the color filter set using above method
yourMaterialIconView.clearColorFilter();

// Sets a custom paint style (for the more advanced devs)
yourMaterialIconView.setStyle(Paint.Style style);


// You can use any of the ImageView methods as well:
yourMaterialIconView.setBackgroundColor(Color.WHITE)
yourMaterialIconView.setScaleType(ScaleType.CENTER)
// etc...
```

That was easy, right? Next up the custom drawables, they are internally used by the `MaterialIconView` so you'll see that they share many of the same methods.

The initialisation happens using the `MaterialDrawableBuilder`, which you can use to set all the properties of the drawable:

```java
// The method returns a MaterialDrawable, but as it is private to the builder you'll have to store it as a regular Drawable ;)
Drawable yourDrawable = MaterialDrawableBuilder.with(context) // provide a context
        .setIcon(MaterialDrawableBuilder.IconValue.WEATHER_RAINY) // provide an icon
        .setColor(Color.WHITE) // set the icon color
        .setToActionbarSize() // set the icon size
    .build(); // Finally call build
```

This will throw an `IconNotSetException` if you forget to provide an icon.

Once you call build, your Drawable will be spit out and you are ready to use it everywhere you please! Setting it to a view is just as easy as with any other `Drawable` (e.g. for `ImageView`):

```java
yourImageView.setImageDrawable(yourDrawable);
```

And that's all there is to it. Below are all the methods you can use with the `MaterialDrawableBuilder` for reference.

```java
// Sets the icon, all 1000+ icons are available inside the MaterialDrawableBuilder.IconValue enum
builder.setIcon(IconValue iconValue);

// Builds the drawable, this method always comes last ofcourse
builder.build();

// Sets the size of the icon to the default action bar icon size
builder.setToActionbarSize();

// Provide a dimension resource to use as icon size
builder.setSizeResource(int dimenRes);

// Set the icon size using a value in dp units
builder.setSizeDp(int size);

// Set the icon size using a pixel value
builder.setSizePx(int size);

// Set the icon color
builder.setColor(int color);

// Set the icon color using a color resource
builder.setColorResource(int colorRes);

// Set the icon's alpha value (0-255) 0 for completely transparent
builder.setAlpha(int alpha);

// Sets a custom colorfilter to the drawing paint (for the more advanced devs)
builder.setColorFilter(ColorFilter cf);

// Clear the color filter set using above method
builder.clearColorFilter();

// Returns the alpha value
builder.getOpacity();

// Sets a custom paint style (for the more advanced devs)
builder.setStyle(Paint.Style style);
```

#License

Released under the [Apache 2.0 License](https://github.com/code-mc/material-icon-lib/blob/master/license.md)

* __MaterialDesignicons__ Released under the [SIL Open Font License 1.1](http://opensource.org/licenses/OFL-1.1)
* __Google Material Design Icons__ Released under the [Attribution 4.0 International License](https://github.com/google/material-design-icons/blob/master/LICENSE)