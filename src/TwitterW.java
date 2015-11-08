import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class TwitterW {

    private static twitter4j.Twitter wrapper = TwitterFactory.getSingleton();
    private static TwitterStream stream;
    private static FilterQuery listenQuery = new FilterQuery();
    private static ArrayList<User> followers = new ArrayList<>();
    private static User gameUser;
    private static boolean listenerAdded;

    static {
        ConfigurationBuilder b = new ConfigurationBuilder();
        b.setOAuthConsumerKey("xqlljOXRePfUjb5D48sXrUPUo").setOAuthConsumerSecret("zzq4TpUANIwQTXIiw5Sbio1OHuzAEHUbo4bJMx4RKgoemPTtNC")
                .setOAuthAccessToken("4134402347-0oKGUVTDnFRZBsj4NEhH2ZWWN2a7Yd4QiP2flsS").setOAuthAccessTokenSecret("1HOwBLYnEVevVUS9jQfFPsE1UnGoed4ybrARsmq9XYtRk");
        Configuration config = b.build();
        stream = new TwitterStreamFactory(config).getInstance();
        wrapper = new TwitterFactory(config).getInstance();
        try {
            gameUser = getUser("tweatgame");
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

  /*  public static void startFollowerRefresh() {
        Thread followerThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        ArrayList<User> newFollowers = getNewFollowers();
                        for(User u : newFollowers){
                            // Add user to world
                            System.out.println(u.getScreenName() + " just followed!");
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {}
                }
            }
        };
        followerThread.start();
    } */

    /**
     * Add a status listener, which is fired whenever a listened user makes a tweet
     * @param listener
     */
    public static void onTweet(StatusListener listener) {
        listenerAdded = true;
        stream.addListener(listener);
    }

    /**
     * Tweet from the game's account
     * @param msg
     */
    public static void tweet(String msg) throws TwitterException {
        StatusUpdate status = new StatusUpdate(msg);
        wrapper.updateStatus(status);
        System.out.println(status);
    }

    /**
     * Listens to tweets from the specified users.
     * <br>
     * Only call this once.
     * <br>
     * The listeners that have been added by onTweet() will be triggered when the user tweets
     * @param users
     */
    public static void listen(User... users) {
        if(!listenerAdded) throw new IllegalStateException("onTweet() must be called before calling listen()");
        long[] d = new long[users.length];
        int i = 0;
        for(User u : users){
            long id = u.getId();
            System.out.println("Listening to id #" + id);
            d[i++] = id;
        }
        listenQuery.follow(d);
        stream.filter(listenQuery);
    }

    /**
     * Follows the specified user
     * @param user
     * @throws TwitterException
     */
    public static void follow(User user) throws TwitterException {
        long[] ids = wrapper.getFriendsIDs(gameUser.getId()).getIDs();
        for(long l : ids){
            if(l == user.getId()) return;
        }
        wrapper.createFriendship(user.getId());
    }

    /**
     * Get the number of followers for the specified user
     * @param user
     * @return
     */
    public static int getFollowerCount(User user) {
        return user.getFollowersCount();
    }

    /**
     * Get a list of followers of the specified user.
     * @param user
     * @return
     * @throws TwitterException
     */
    public static ArrayList<User> getFollowers(User user) throws TwitterException{
        PagableResponseList<User> followers =  wrapper.getFollowersList(user.getId(), -1);
        ArrayList<User> followersArrayList = new ArrayList<>(followers.size());
        for(User follower: followers){
            followersArrayList.add(follower);
        }

        return followersArrayList;
    }

    /**
     * Get the user's game score.
     * In the case of the methods below returning 0, this method will return 1.
     * <br>
     * Dependent on getFollowers(), getFollowing() and getLikes()
     * @param user
     * @return
     */
    public static float getSize(User user) {
        float d = getFollowerCount(user) + ((float)getLikes(user) / (getFollowing(user) + 1));
        return d <= 0 ? 1 : (float)Math.log(d);
    }

    /**
     * Get the user object for the specified handle (not including the @ character)
     * @param handle
     * @return
     */
    public static User getUser(String handle) throws TwitterException {
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
            URL url = new URL(user.getOriginalProfileImageURL());
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