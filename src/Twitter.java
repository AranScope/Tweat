import twitter4j.ResponseList;
import twitter4j.StatusUpdate;
import twitter4j.User;

import javax.xml.ws.Response;

public class Twitter {

    private twitter4j.Twitter wrapper = TwitterFactory.getSingleton();

    public void tweet(String msg) {
        StatusUpdate status = new StatusUpdate(msg);
        wrapper.updateStatus(status);
        System.out.println(status);
    }

    public int getFollowers(User user) {
        return user.getFollowersCount();
    }

    public double getScore(User user) {
        return Math.log(getFollowers(user) + ((double)getLikes(user) / (getFollowing(user) + 1)));
    }

    public User getUser(String handle) {
        ResponseList<User> response = wrapper.lookupUsers(handle);
        return response.size() == 0 ? null : response.get(0);
    }

    public int getFollowing(User user) {
        return user.getFriendsCount();
    }

    public int getLikes(User user) {
        return user.getFavouritesCount();
    }

}