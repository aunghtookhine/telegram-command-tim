package com.aunghtookhine.telegram.enums;

public enum ReportType {
    DATA_PACK,
    CORE_SYSTEM,
    MAU,
    GIFT_CODE;

    public static boolean contains(String test) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
