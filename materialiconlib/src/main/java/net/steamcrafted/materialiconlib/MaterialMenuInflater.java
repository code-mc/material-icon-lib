package net.steamcrafted.materialiconlib;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MaterialMenuInflater {
    /** Menu tag name in XML. */
    private static final String XML_MENU = "menu";

    /** Group tag name in XML. */
    private static final String XML_GROUP = "group";

    /** Item tag name in XML. */
    private static final String XML_ITEM = "item";

    private final Context mContext;
    private final MenuInflater mInflater;
    private int mDefaultColor;

    /**
     * Constructs a menu inflater.
     *
     * @see Activity#getMenuInflater()
     */
    private MaterialMenuInflater(Context context, MenuInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mDefaultColor = getDefaultColor();
    }

    public static MaterialMenuInflater with(Context context){
        return new MaterialMenuInflater(
                context,
                context instanceof Activity ?
                        ((Activity)context).getMenuInflater()
                        : new MenuInflater(context)
        );
    }

    public static MaterialMenuInflater with(Context context, MenuInflater inflater){
        return new MaterialMenuInflater(context, inflater);
    }

    public MaterialMenuInflater setDefaultColor(int color){
        mDefaultColor = color;
        return this;
    }

    public MaterialMenuInflater setDefaultColorResource(int colorRes){
        mDefaultColor = MaterialIconUtils.getColorResource(mContext, colorRes);
        return this;
    }

    /**
     * Inflate a menu hierarchy from the specified XML resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param menuRes Resource ID for an XML layout resource to load (e.g.,
     *            <code>R.menu.main_activity)</code>)
     * @param menu The Menu to inflate into. The items and submenus will be
     *            added to this Menu.
     */
    public void inflate(int menuRes, Menu menu) {
        menu.clear();

        mInflater.inflate(menuRes, menu);

        afterInflate(menuRes, menu);
    }

    private void afterInflate(int menuRes, Menu menu){
        IconData root = new IconData(0, 0, 0);
        XmlResourceParser parser = null;
        try {
            parser = mContext.getResources().getLayout(menuRes);
            AttributeSet attrs = Xml.asAttributeSet(parser);

            parseMenu(parser, attrs, root);
        } catch (XmlPullParserException e) {
            throw new InflateException("Error inflating menu XML", e);
        } catch (IOException e) {
            throw new InflateException("Error inflating menu XML", e);
        } finally {
            if (parser != null) parser.close();

            // populate the menu with the parsed icons
            populateIcons(menu, root, mDefaultColor);
        }
    }

    private int getDefaultColor(){
        TypedValue outValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.materialIconColor, outValue, true);

        // Colorstatelist/color resource
        if(outValue.resourceId != 0 && outValue.type == TypedValue.TYPE_ATTRIBUTE){
            ColorStateList stateList = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                stateList = mContext.getResources().getColorStateList(outValue.resourceId, mContext.getTheme());
            }else{
                stateList = mContext.getResources().getColorStateList(outValue.resourceId);
            }
            if(stateList != null) return stateList.getDefaultColor();
        }

        // Regular inline color int
        if(outValue.type <= TypedValue.TYPE_LAST_COLOR_INT && outValue.type >= TypedValue.TYPE_FIRST_COLOR_INT){
            return outValue.data;
        }
        return Color.BLACK;
    }

    private void populateIcons(Menu menu, IconData root, int defaultIconColor) {
        for(int i = 0; i < menu.size(); i++){
            MenuItem m = menu.getItem(i);
            IconData d = root.children.get(i);

            if(m.hasSubMenu()){
                populateIcons(m.getSubMenu(), d, defaultIconColor);
            }

            if(d.itemIconResId >= 0)
                m.setIcon(
                        MaterialDrawableBuilder.with(mContext)
                                .setIcon(MaterialDrawableBuilder.IconValue.values()[d.itemIconResId])
                                .setColor(d.itemColor != -1 ? d.itemColor : defaultIconColor)
                                .setToActionbarSize()
                                .build()
                );
        }
    }

    /**
     * Called internally to fill the given menu. If a sub menu is seen, it will
     * call this recursively.
     */
    private void parseMenu(XmlPullParser parser, AttributeSet attrs, IconData menu)
            throws XmlPullParserException, IOException {
        MenuState menuState = new MenuState(menu);

        int eventType = parser.getEventType();
        String tagName;
        boolean lookingForEndOfUnknownTag = false;
        String unknownTagName = null;

        // This loop will skip to the menu start tag
        do {
            if (eventType == XmlPullParser.START_TAG) {
                tagName = parser.getName();
                if (tagName.equals(XML_MENU)) {
                    // Go to next tag
                    eventType = parser.next();
                    break;
                }

                throw new RuntimeException("Expecting menu, got " + tagName);
            }
            eventType = parser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        boolean reachedEndOfMenu = false;
        while (!reachedEndOfMenu) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (lookingForEndOfUnknownTag) {
                        break;
                    }

                    tagName = parser.getName();
                    if (tagName.equals(XML_GROUP)) {
                        menuState.readGroup(attrs);
                    } else if (tagName.equals(XML_ITEM)) {
                        menuState.readItem(attrs);
                    } else if (tagName.equals(XML_MENU)) {
                        // A menu start tag denotes a submenu for an item
                        IconData subMenu = menuState.addSubMenuItem();

                        // Parse the submenu into returned SubMenu
                        parseMenu(parser, attrs, subMenu);
                    } else {
                        lookingForEndOfUnknownTag = true;
                        unknownTagName = tagName;
                    }
                    break;

                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if (lookingForEndOfUnknownTag && tagName.equals(unknownTagName)) {
                        lookingForEndOfUnknownTag = false;
                        unknownTagName = null;
                    } else if (tagName.equals(XML_GROUP)) {
                        menuState.resetGroup();
                    } else if (tagName.equals(XML_ITEM)) {
                        // Add the item if it hasn't been added (if the item was
                        // a submenu, it would have been added already)
                        if (!menuState.hasAddedItem()) {
                            menuState.addItem();
                        }
                    } else if (tagName.equals(XML_MENU)) {
                        reachedEndOfMenu = true;
                    }
                    break;

                case XmlPullParser.END_DOCUMENT:
                    throw new RuntimeException("Unexpected end of document");
            }

            eventType = parser.next();
        }
    }

    private class IconData {
        public int itemIconResId;
        public int itemColor;
        public int categoryOrder;
        public List<IconData> children = new ArrayList<>();

        public IconData(int itemIconResId, int itemColor, int categoryOrder) {
            this.itemIconResId = itemIconResId;
            this.itemColor = itemColor;
            this.categoryOrder = categoryOrder;
        }
    }

    /**
     * State for the current menu.
     * <p>
     * Groups can not be nested unless there is another menu (which will have
     * its state class).
     */
    private class MenuState {

        /**
         * This is the part of an order integer that the user can provide.
         *
         * @hide
         */
        static final int USER_MASK = 0x0000ffff;
        /**
         * Bit shift of the user portion of the order integer.
         *
         * @hide
         */
        static final int USER_SHIFT = 0;

        /**
         * This is the part of an order integer that supplies the category of the item.
         *
         * @hide
         */
        static final int CATEGORY_MASK = 0xffff0000;
        /**
         * Bit shift of the category portion of the order integer.
         *
         * @hide
         */
        static final int CATEGORY_SHIFT = 16;

        final int[] sCategoryToOrder = new int[]{
                1, /* No category */
                4, /* CONTAINER */
                5, /* SYSTEM */
                3, /* SECONDARY */
                2, /* ALTERNATIVE */
                0, /* SELECTED_ALTERNATIVE */
        };

        private static final int defaultItemCategory = 0;
        private static final int defaultItemOrder = 0;


        private boolean itemAdded;
        private int itemIconResId;
        private int itemIconColor;
        private int categoryOrder;
        private int groupCategory;
        private int groupOrder;

        private IconData menu;

        public MenuState(IconData menu) {
            this.menu = menu;

            resetGroup();
        }

        public void resetGroup() {
            groupCategory = defaultItemCategory;
            groupOrder = defaultItemOrder;
        }

        /**
         * Called when the parser is pointing to a group tag.
         */
        public void readGroup(AttributeSet attrs) {
            TypedArray a = mContext.obtainStyledAttributes(attrs,
                    net.steamcrafted.materialiconlib.R.styleable.MaterialMenuGroup);

            groupCategory = a.getInt(net.steamcrafted.materialiconlib.R.styleable.MaterialMenuGroup_android_menuCategory, defaultItemCategory);
            groupOrder = a.getInt(net.steamcrafted.materialiconlib.R.styleable.MaterialMenuGroup_android_orderInCategory, defaultItemOrder);

            a.recycle();
        }

        /**
         * Called when the parser is pointing to an item tag.
         */
        public void readItem(AttributeSet attrs) {
            TypedArray a = mContext.getApplicationContext().obtainStyledAttributes(attrs,
                    net.steamcrafted.materialiconlib.R.styleable.MaterialIconViewFormat);

            // Inherit attributes from the group as default value
            itemIconResId = a.getInt(net.steamcrafted.materialiconlib.R.styleable.MaterialIconViewFormat_materialIcon, -1);
            itemIconColor = a.getColor(net.steamcrafted.materialiconlib.R.styleable.MaterialIconViewFormat_materialIconColor, -1);

            a.recycle();

            a = mContext.obtainStyledAttributes(attrs,
                    net.steamcrafted.materialiconlib.R.styleable.MaterialMenuItem);

            final int category = a.getInt(net.steamcrafted.materialiconlib.R.styleable.MaterialMenuGroup_android_menuCategory, groupCategory);
            final int order = a.getInt(net.steamcrafted.materialiconlib.R.styleable.MaterialMenuGroup_android_orderInCategory, groupOrder);
            categoryOrder = (category & CATEGORY_MASK) | (order & USER_MASK);

            a.recycle();

            itemAdded = false;
        }

        public IconData addItem() {
            itemAdded = true;

            final int ordering = getOrdering(categoryOrder);

            final IconData item = new IconData(itemIconResId, itemIconColor, ordering);

            menu.children.add(findInsertIndex(menu.children, ordering), item);

            return item;
        }

        public IconData addSubMenuItem() {
            return addItem();
        }

        public boolean hasAddedItem() {
            return itemAdded;
        }

        /**
         * Returns the ordering across all items. This will grab the category from
         * the upper bits, find out how to order the category with respect to other
         * categories, and combine it with the lower bits.
         *
         * @param categoryOrder The category order for a particular item (if it has
         *            not been or/add with a category, the default category is
         *            assumed).
         * @return An ordering integer that can be used to order this item across
         *         all the items (even from other categories).
         */
        private int getOrdering(int categoryOrder) {
            final int index = (categoryOrder & CATEGORY_MASK) >> CATEGORY_SHIFT;

            if (index < 0 || index >= sCategoryToOrder.length) {
                throw new IllegalArgumentException("order does not contain a valid category.");
            }

            return (sCategoryToOrder[index] << CATEGORY_SHIFT) | (categoryOrder & USER_MASK);
        }


        private int findInsertIndex(List<IconData> items, int ordering) {
            for (int i = items.size() - 1; i >= 0; i--) {
                IconData item = items.get(i);
                if (item.categoryOrder <= ordering) {
                    return i + 1;
                }
            }

            return 0;
        }
    }
}