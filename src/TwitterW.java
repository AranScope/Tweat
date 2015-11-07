import twitter4j.*;
import twitter4j.FilterQuery;
import twitter4j.ResponseList;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.ImageIO;
import javax.xml.ws.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class TwitterW {

    private static twitter4j.Twitter wrapper = TwitterFactory.getSingleton();
    private static TwitterStream stream = new TwitterStreamFactory(new ConfigurationBuilder().setJSONStoreEnabled(true).build()).getInstance();
    private static ArrayList<Long> listenedUsers = new ArrayList<>();
    private static FilterQuery listenQuery = new FilterQuery();

    static {
        /*try {
            stream.setOAuthAccessToken(wrapper.getOAuthAccessToken());
            stream.setOAuthConsumer("xqlljOXRePfUjb5D48sXrUPUo", "zzq4TpUANIwQTXIiw5Sbio1OHuzAEHUbo4bJMx4RKgoemPTtNC");
        } catch (TwitterException e){
            System.err.println("TwitterW.<static> : " + e.getErrorMessage());
        }*/
        stream.setOAuthAccessToken(new AccessToken("4134402347-0oKGUVTDnFRZBsj4NEhH2ZWWN2a7Yd4QiP2flsS", "1HOwBLYnEVevVUS9jQfFPsE1UnGoed4ybrARsmq9XYtRk"));
        stream.setOAuthConsumer("xqlljOXRePfUjb5D48sXrUPUo", "zzq4TpUANIwQTXIiw5Sbio1OHuzAEHUbo4bJMx4RKgoemPTtNC");
    }
    /**
     * Add a status listener, which is fired whenever a listened user makes a tweet
     * @param listener
     */
    public static void onTweet(StatusListener listener) {
        stream.addListener(listener);
    }

    /**
     * Tweet from the game's account
     * @param msg
     */
    public static void tweet(String msg) throws TwitterException{
        StatusUpdate status = new StatusUpdate(msg);
        wrapper.updateStatus(status);
        System.out.println(status);
    }

    public static void refreshFollowers() {
<<<<<<< HEAD
        
=======
       // wrapper.getFollowersIDs()
>>>>>>> 9ef33e503bb9d30a2a1fa3b112c9cef9f8d6218b
    }

    /**
     * Listens to tweets from the specified user.
     * <br>
     * The listeners that have been added by onTweet() will be triggered when the user tweets
     * @param user
     */
    public static void listen(User user) {
        listenedUsers.add(user.getId());
        for(long l : listenedUsers) listenQuery.follow(l);
        stream.filter(listenQuery);
    }

    /**
     * Follows the specified user
     * @param user
     * @throws TwitterException
     */
    public static void follow(User user) throws TwitterException{
        wrapper.createFriendship(user.getName());
    }

    /**
     * Get the number of followers for the specified user
     * @param user
     * @return
     */
    public static int getFollowers(User user) {
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
    public static double getScore(User user) {
        double d = getFollowers(user) + ((double)getLikes(user) / (getFollowing(user) + 1));
        return d <= 0 ? 1 : Math.log(d);
    }

    /**
     * Get the user object for the specified handle (not including the @ character)
     * @param handle
     * @return
     */
    public static User getUser(String handle) throws TwitterException{
        ResponseList<User> response = wrapper.lookupUsers(handle);
        return response.size() == 0 ? null : response.get(0);
    }

    /**
     * Get the number of users that the specified user follows
     * @param user
     * @return
     */
    public static int getFollowing(User user) {
        return user.getFriendsCount();
    }

    /**
     * Get the number of tweets that the user has favourited/liked
     * @param user
     * @return
     */
    public static int getLikes(User user) {
        return user.getFavouritesCount();
    }

    /**
     * Get the URL of the user's profile image
     * @param user
     * @return
     */
    public static String getProfileImageURL(User user) {
        return user.getProfileImageURL();
    }

    /**
     * Gets the user's profile image.
     * <br>
     * Prints to System.err if an error occurs
     * @param user
     * @return
     */
    public static BufferedImage getProfileImage(User user) {
        File dest;
        try {
            dest = File.createTempFile("res/tmp/" + user.getName(), ".png");
            URL url = new URL(getProfileImageURL(user));
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(dest);

            byte[] bytes = new byte[1024];
            int length;

            while ((length = is.read(bytes)) != -1) {
                os.write(bytes, 0, length);
            }

            is.close();
            os.close();
            return ImageIO.read(dest);
        } catch (IOException e) {
            System.err.println("Twitter.getProfileImage() : Error loading temp file : " + e.getMessage());
            return null;
        }
    }

}
