package com.drleinbach.minecraftstats.beans;

import com.drleinbach.model.AllPlayerStatus;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This bean is an implementation of the interface that represents
 * the database view 'all_player_status'.
 * <p/>
 * Created: 5/5/13
 *
 * @author Daniel
 */
public class AllPlayerStatusImpl implements Serializable, AllPlayerStatus {

    /**
     * Version of the object
     */
    private static final long serialVersionUID = 1L;

    /**
     * The screen name is the player's in game name
     */
    @Column(name = "screen_name")
    private String screenName;

    /**
     * The login is the time when the player starts playing
     */
    @Column
    private Timestamp login;

    /**
     * The quit is the time when the player stops playing
     */
    @Column
    private Timestamp quit;

    /**
     * Provides access to the private member holding the user's
     * screen name.
     *
     * @return The screen name of the user
     * @see com.drleinbach.model.AllPlayerStatus#getScreenName()
     */
    @Override
    public String getScreenName() {
        return this.screenName;
    }

    /**
     * Provides change access to the private member holding the
     * user's screen name.
     *
     * @param screenName - Screen name of the user to be set
     * @see com.drleinbach.model.AllPlayerStatus#setScreenName(String)
     */
    @Override
    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    /**
     * Provides change access to the private member holding the
     * login time. This is the time that the user started playing.
     *
     * @param login - The time the user logged in or started playing
     * @see com.drleinbach.model.AllPlayerStatus#setLogin(java.sql.Timestamp)
     */
    @Override
    public void setLogin(Timestamp login) {
        this.login = login;
    }

    /**
     * Provides change access to the private member holding the time
     * the player quit, or stopped playing.
     *
     * @param quit - The time the player quit the server.
     * @see com.drleinbach.model.AllPlayerStatus#setQuit(java.sql.Timestamp)
     */
    @Override
    public void setQuit(Timestamp quit) {
        this.quit = quit;
    }

    /**
     * Provides access to the private member holding the time the player
     * logged in.
     *
     * @return The time that the player started playing.
     * @see com.drleinbach.model.AllPlayerStatus#getLogin()
     */
    @Override
    public Timestamp getLogin() {
        return login;
    }

    /**
     * Provides access to the private member holding the time the player
     * logged out.
     *
     * @return The time that the player stopped playing.
     * @see com.drleinbach.model.AllPlayerStatus#getQuit()
     */
    @Override
    public Timestamp getQuit() {
        return quit;
    }
}
