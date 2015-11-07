import twitter4j.ResponseList;
import twitter4j.StatusUpdate;
import twitter4j.User;

import javax.xml.ws.Response;

public class Twitter {

    private twitter4j.Twitter wrapper = TwitterFactory.getSingleton();

    /**
     * Tweet from the game's account
     * @param msg
     */
    public void tweet(String msg) {
        StatusUpdate status = new StatusUpdate(msg);
        wrapper.updateStatus(status);
        System.out.println(status);
    }

    /**
     * Get the number of followers for the specified user
     * @param user
     * @return
     */
    public int getFollowers(User user) {
        return user.getFollowersCount();
    }

    /**
     * Get the user's game score.
     * In the case of the methods below returning 0, this method will return 1.
     * <br>
     * Dependent on getFollowers(), getFollowing() and getLikes()
     * @param user
     * @return
     */
    public double getScore(User user) {
        double d = getFollowers(user) + ((double)getLikes(user) / (getFollowing(user) + 1)));
        return d <= 0 ? 1 : Math.log(d);
    }

    /**
     * Get the user object for the specified handle (not including the @ character)
     * @param handle
     * @return
     */
    public User getUser(String handle) {
        ResponseList<User> response = wrapper.lookupUsers(handle);
        return response.size() == 0 ? null : response.get(0);
    }

    /**
     * Get the number of users that the specified user follows
     * @param user
     * @return
     */
    public int getFollowing(User user) {
        return user.getFriendsCount();
    }

    /**
     * Get the number of tweets that the user has favourited/liked
     * @param user
     * @return
     */
    public int getLikes(User user) {
        return user.getFavouritesCount();
    }

    /**
     * Get the URL of the user's profile image
     * @param user
     * @return
     */
    public String getProfileImageURL(User user) {
        return user.getProfileImageURL();
    }

}