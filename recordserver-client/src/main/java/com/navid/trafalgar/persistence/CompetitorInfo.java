package com.navid.trafalgar.persistence;

public final class CompetitorInfo {

    private int position;
    private float time;
    private boolean local;
    private String userName;
    private String gameId;

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the time
     */
    public float getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * @return the local
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * @param local the local to set
     */
    public void setLocal(boolean local) {
        this.local = local;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the gameId
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * @param gameId the gameId to set
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return Float.toString(time) + " " + userName;
    }

}
