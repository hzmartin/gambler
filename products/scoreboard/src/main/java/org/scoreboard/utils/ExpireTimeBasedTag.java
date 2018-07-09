package org.scoreboard.utils;

public enum ExpireTimeBasedTag {
    ONE_HOUR(3600), HALF_HOUR(1800), TEN_MINUTES(600), ONE_DAY(86400);
    private int expireSeconds;
    private int maxObjects = 100000;
    ExpireTimeBasedTag(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public int getMaxObjects() {
        return maxObjects;
    }
}
