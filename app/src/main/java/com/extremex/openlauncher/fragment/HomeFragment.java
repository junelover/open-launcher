package com.extremex.openlauncher.fragment;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.extremex.openlauncher.R;
import com.extremex.openlauncher.adapter.AppAdapter;
import com.extremex.openlauncher.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private PackageManager packageManager;
    private List<ResolveInfo> apps;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentHomeBinding.bind(view);

        packageManager = requireActivity().getPackageManager();
        apps = getInstalledApps();

        // Filter out this app "com.extremex.openlauncher"
        apps = filterOutCurrentApp(apps);

        binding.VerticalAppDock.setAdapter(new AppAdapter(requireActivity(),packageManager,apps));

        binding.VerticalAppDock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ResolveInfo resolveInfo = apps.get(position);
                Intent launchIntent = packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
                if (launchIntent != null){
                    startActivity(launchIntent);
                }
            }
        });
    }


    private List<ResolveInfo> getInstalledApps(){
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return packageManager.queryIntentActivities(intent, PackageManager.GET_META_DATA);
    }

    private List<ResolveInfo> filterOutCurrentApp(List<ResolveInfo> apps) {
        List<ResolveInfo> filteredApps = new ArrayList<>();
        for (ResolveInfo app : apps) {
            if (!app.activityInfo.packageName.equals("com.extremex.openlauncher")) {
                filteredApps.add(app);
            }
        }
        return filteredApps;
    }
}
