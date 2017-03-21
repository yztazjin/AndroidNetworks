package ttyy.com.jinnetwork.ext_image.cache;

/**
 * author: admin
 * date: 2017/03/21
 * version: 0
 * mail: secret
 * desc: ImageCacheType
 */

public enum ImageCacheType {

    /**
     * 使用内存缓存
     */
    RuntimeCache{
        @Override
        public boolean useRuntimeCache() {
            return true;
        }
    },
    /**
     * 使用磁盘缓存
     */
    DiskCache{
        @Override
        public boolean useDiskCache() {
            return true;
        }
    },
    /**
     * 使用所有类型缓存
     */
    AllCache{
        @Override
        public boolean useDiskCache() {
            return true;
        }

        @Override
        public boolean useRuntimeCache() {
            return true;
        }
    },
    /**
     * 禁用缓存
     */
    NoneCache;

    public boolean useDiskCache(){
        return false;
    }

    public boolean useRuntimeCache(){
        return false;
    }
}
