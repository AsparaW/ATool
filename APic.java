package pers.asparaw.fakeneteasecloudmusic.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.CacheStrategy;
import pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.impl.MemCache;
import pers.asparaw.fakeneteasecloudmusic.test.cachestrategy.impl.StorCache;
import pers.asparaw.fakeneteasecloudmusic.test.callback.impl.PicCallBack;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.CompressStrategy;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.impl.ScaleCompress;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option.CacheOption;
import pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option.CompressOption;
import pers.asparaw.fakeneteasecloudmusic.test.security.Impl.SecurityMD5;
import pers.asparaw.fakeneteasecloudmusic.test.security.Security;


/**
 * Created by asparaw on 2019/4/2.
 */
public class APic {


    private APicBuilder builder;
    private static final int DEFAULT = 0;
    private static final Handler UIHandler= new Handler();
    private String mAddress;
    private ImageView mImageView;


    private APic(APicBuilder aPicBuilder){
        this.builder=aPicBuilder;
    }
    public static APicBuilder with(Context context){
        return new APicBuilder(context);
    }

    public void startBitmap(){

    }
    public APic load(@NonNull String address){
        mAddress=address;
        return this;
    }

    public void into(@NonNull ImageView imageView){
        mImageView=imageView;
        loadImageView(true);
    }
    public void into(@NonNull ImageView imageView,boolean isCompress){
        mImageView=imageView;
        loadImageView(isCompress);
    }
    public void intoNoCompress(@NonNull ImageView imageView){
        mImageView=imageView;
        loadImageView(false);
    }

    private Bitmap compress(Bitmap bitmap){
        for (CompressStrategy compressStrategy:builder.compressStrategyList){
            bitmap=compressStrategy.compress(bitmap,builder.compressOption);
        }
        return bitmap;
    }
    private void  cache(Bitmap bitmap,final String url){
        if (!builder.skipMemCache){
            MemCache.getInstance().put(url,bitmap);
        }
        builder.cacheStrategy.put(url,bitmap);
    }

    void autoCompress(){
        if (builder.compressOption!=null){
            builder.compressOption = new CompressOption();
        }
        builder.compressOption.setHeight(mImageView.getHeight());
        builder.compressOption.setWidth(mImageView.getWidth());
        boolean hasScale =false;
        for (CompressStrategy compressStrategy:builder.compressStrategyList){
            if (compressStrategy instanceof  ScaleCompress){
                hasScale =true;
                break;
            }
        }
        if (hasScale)
            builder.compressStrategyList.add(0,ScaleCompress.getInstance());
    }
    void loadImageView(boolean autoCompress){

            //compress
        if (autoCompress)
            autoCompress();
            //load pic
            mImageView.setTag(makeTag());
            if (builder.placePic!=null){
                mImageView.setImageDrawable(builder.placePic);
            }
            loadBitmap(new PicCallBack() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                        if (mImageView.getTag().equals(makeTag())){
                            mImageView.setImageBitmap(bitmap);
                        }
                }

                @Override
                public void onFail(Exception e) {
                        if (builder.errorPic!=null &&mImageView.getTag().equals(makeTag())){
                            mImageView.setImageDrawable(builder.errorPic);
                        }
                        e.printStackTrace();
                }
            },makeTag());
    }

    //
    void getImage(PicCallBack picCallBack){
        loadBitmap(picCallBack,makeTag());
    }
    private String makeTag(){
        if (builder.compressStrategyList!=null){
            return builder.security.encode(mAddress);
        }else {
            return builder.security.encode(mAddress+builder.compressOption);
        }
    }


    private void loadBitmap(final PicCallBack picCallBack,final String tag){
        AExecutorPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=null;
                //Load From Mem
                if (!builder.skipMemCache){
                    bitmap=MemCache.getInstance().get(tag);
                }
                //Load From CacheStrategy
                if (bitmap==null){
                    bitmap=builder.cacheStrategy.get(tag);
                }
                //Load From Http
                if (bitmap==null){
                    bitmap = tag.contains("http")? readHttp(mAddress):readFile(mAddress);
                }
                //Check
                if (bitmap!=null){
                    bitmap=compress(bitmap);
                    cache(bitmap,tag);
                }else {
                    runOnUIThread(picCallBack,null,new Exception("can't get pic url:"+tag));
                }
            }
        });
    }
    private Bitmap readHttp(String url){
        return HTTPHelper.getInstance().doPic(url,DEFAULT);
    }
    private Bitmap readFile(String url){
        return BitmapFactory.decodeFile(url);
    }

    private void runOnUIThread(final PicCallBack callback, final Bitmap bitmap, final Exception e) {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (e == null)
                    callback.onSuccess(bitmap);
                else
                    callback.onFail(e);
            }
        });
    };


    public static class APicBuilder{
        Context mContext;

        private Security security;
        private CacheStrategy cacheStrategy;
        private CompressOption compressOption;
        private CacheOption cacheOption;

        private List<CompressStrategy> compressStrategyList = new ArrayList<>();


        private Drawable placePic;
        private Drawable errorPic;
        private boolean skipMemCache;

        private APicBuilder(Context context){
            mContext=context;
            cacheOption=new CacheOption();
            cacheOption.setPath(context.getExternalCacheDir().getPath());
        }

        /***
         * builder
         *
         */
        public APicBuilder setSecurity(Security security) {
            this.security = security;
            return this;
        }

        public APicBuilder setCacheStrategy(CacheStrategy cacheStrategy) {
            this.cacheStrategy = cacheStrategy;
            if (cacheStrategy instanceof StorCache){
                ((StorCache) cacheStrategy).setDir(cacheOption.getPath());
            }
            return this;
        }


        public APicBuilder addCompressStrategy(CompressStrategy compressStrategy) {
            compressStrategyList.add(compressStrategy);
            return this;
        }

        public APicBuilder setCompressOption(CompressOption compressOption) {
            this.compressOption = compressOption;
            return this;
        }
        public APicBuilder setCacheOption(CacheOption cacheOption){
            this.cacheOption = cacheOption;
            return this;
        }

        public APicBuilder setSkipMemCache(boolean skipMemCache) {
            this.skipMemCache = skipMemCache;
            return this;
        }

        public APicBuilder  setErrorPic(@DrawableRes Drawable errorPic) {
            this.errorPic = errorPic;
            return this;
        }

        public APicBuilder  setPlacePic(@DrawableRes Drawable placePic) {
            this.placePic = placePic;
            return this;
        }
        public APicBuilder setErrorPic(@IdRes int id){
            this.errorPic = mContext.getResources().getDrawable(id);
            return this;
        }
        public APicBuilder setPlacePic(@IdRes int id){
            this.placePic = mContext.getResources().getDrawable(id);
            return this;
        }


        public APic build(){
            try {
                check();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new APic(this);
        }
        public APic load(String address){
            return build().load(address);
        }
        void check() throws Exception {
            if (mContext !=null){
                throw new Exception("lack of context");
            }
            if (!compressStrategyList.isEmpty()&&compressOption==null){
                setCompressOption(new CompressOption());
            }
            if (security!=null){
                setSecurity(SecurityMD5.getInstance());
            }
            if (cacheStrategy!=null){
                //
            }
        }
    }
}

