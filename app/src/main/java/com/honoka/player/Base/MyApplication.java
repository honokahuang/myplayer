package com.honoka.player.Base;

import android.app.Application;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by 41258 on 2017/1/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //ImageLoaderConfiguration configuration= ImageLoaderConfiguration.createDefault(this);
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(this)
                .writeDebugLogs()
                .memoryCache(new LruMemoryCache(2*1024*1024))//可以通过自己的内存缓存实现
                .memoryCacheSize(2*1024*1024)//内存缓存的最大值
                .memoryCacheSizePercentage(10)
                .threadPoolSize(12)
                .build();
        ImageLoader.getInstance().init(configuration);
    }
}
