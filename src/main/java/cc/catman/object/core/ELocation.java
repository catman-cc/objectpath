package cc.catman.object.core;

import lombok.Getter;

/**
 * 位置枚举
 * @author jpanda
 * @since 0.0.1
 */
@Getter
public enum ELocation {
    ROOT("$"),
    PARENT("@@"),
    CURRENT("@");
    /**
     * 位置
     */
    private final String location;

    ELocation(String location) {
        this.location = location;
    }
    /**
     * 根据位置获取枚举
     * @param location 位置
     * @return 枚举
     */
    public static ELocation fromLocation(String location) {
        for (ELocation eLocation : ELocation.values()) {
            if (eLocation.location.equals(location)) {
                return eLocation;
            }
        }
        return null;
    }

}
