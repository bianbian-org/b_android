package com.techjumper.polyhomeb.manager;

import com.techjumper.corelib.utils.Utils;
import com.techjumper.corelib.utils.file.FileUtils;
import com.techjumper.lib2.utils.GsonUtils;
import com.techjumper.polyhomeb.entity.QueryFamilyEntity;

import okio.Buffer;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class PolyPluginFileManager {


    private static final String FAMILY_INFO_FILE_NAME = "family_info";

    private PolyPluginFileManager() {
    }

    private static class SingletonInstance {
        private static final PolyPluginFileManager INSTANCE = new PolyPluginFileManager();
    }

    public static PolyPluginFileManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public Observable<Boolean> saveFamilyInfoToLocal(QueryFamilyEntity queryFamilyEntity) {
        return Observable
                .create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        FileUtils.saveInputstreamToPath(new Buffer().writeUtf8(GsonUtils.toJson(queryFamilyEntity)).inputStream()
                                , Utils.appContext.getFilesDir().getAbsolutePath(), FAMILY_INFO_FILE_NAME);
                        subscriber.onNext(true);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<String> getFamilyInfoFromLocal() {
        return Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(FileUtils.loadTextFile(Utils.appContext.getFilesDir().getAbsolutePath(), FAMILY_INFO_FILE_NAME));
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Observable<Boolean> clearFamilyInfoFile() {
        return Observable
                .create(new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        subscriber.onNext(FileUtils.deleteFileIfExist(Utils.appContext.getFilesDir().getAbsolutePath(), FAMILY_INFO_FILE_NAME));
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}