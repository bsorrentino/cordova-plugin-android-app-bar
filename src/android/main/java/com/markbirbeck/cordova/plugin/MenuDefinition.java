package com.markbirbeck.cordova.plugin;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class MenuDefinition {
    final JSONArray mDefinition;
    final CallbackContext mCallbackContext;

    /**
     *
     * @param definition
     */
    public MenuDefinition(JSONArray definition) {
        this(definition, null);
    }

    /**
     *
     * @param definition
     * @param callbackContext
     */
    public MenuDefinition(JSONArray definition, final CallbackContext callbackContext) {
        if( definition == null ) {
            throw new IllegalArgumentException("array definition is null");
        }
        mDefinition = definition;
        mCallbackContext = callbackContext;
    }

    /**
     *
     * @param menu
     * @param ctx
     */
    public void createMenu(final Menu menu, final Activity ctx) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < mDefinition.length(); ++i) {
                    try {
                        final JSONObject itemDef = mDefinition.getJSONObject(i);
                        final String title = itemDef.isNull("action") ? "" : itemDef.getString("action");

                        final MenuItem item = menu.add(title);
                        item.setShowAsAction((itemDef.has("button") && itemDef.getBoolean("button")) ?
                                MenuItem.SHOW_AS_ACTION_IF_ROOM :
                                MenuItem.SHOW_AS_ACTION_WITH_TEXT);
                    } catch (JSONException e) {
                        fire("ERROR processing menu" + e);
                    }
                }
                fire("SUCCESS processing menu");
            }
        });
    }

    /**
     *
     * @param action
     */
    public void fire(String action) {

        if( mCallbackContext!= null ) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, action);

            result.setKeepCallback(true);
            mCallbackContext.sendPluginResult(result);
        }
    }
}
