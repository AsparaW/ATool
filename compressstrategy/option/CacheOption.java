package pers.asparaw.fakeneteasecloudmusic.test.compressstrategy.option;

/**
 * Created by asparaw on 2019/4/10.
 */
public class CacheOption {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "cache option:"+path;
    }
}
