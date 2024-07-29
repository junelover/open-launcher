package com.extremex.openlauncher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.extremex.openlauncher.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {
    private Activity activity;
    private PackageManager packageManager;
    private List<ResolveInfo> apps;

    public AppAdapter(Activity activity, PackageManager packageManager, List<ResolveInfo> apps){
        this.activity = activity;
        this.packageManager = packageManager;
        this.apps = apps;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, android.view.ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.item_app, parent, false);
        }

        ResolveInfo resolveInfo = apps.get(position);

        ImageView appIcon = convertView.findViewById(R.id.appIcon);
        TextView appName = convertView.findViewById(R.id.appName);

        Drawable icon = resolveInfo.loadIcon(packageManager);
        String name = resolveInfo.loadLabel(packageManager).toString();

        appIcon.setImageDrawable(icon);
        appName.setText(name);

        return convertView;
    }
}
