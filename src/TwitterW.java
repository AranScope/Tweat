import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.imageio.ImageIO;
import javax.xml.ws.Response;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class TwitterW {

    private Twitter wrapper = TwitterFactory.getSingleton();
    private TwitterStream stream;
    private FilterQuery listenQuery = new FilterQuery();
    private User gameUser;
    private boolean listenerAdded;

	public TwitterW(String handle, String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
        ConfigurationBuilder b = new ConfigurationBuilder();
        b.setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
        Configuration config = b.build();
        stream = new TwitterStreamFactory(config).getInstance();
        wrapper = new TwitterFactory(config).getInstance();

        try {
            gameUser = getUser(handle);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
	}

    /**
     * Add a status listener, which is fired whenever a listened user makes a tweet
     * @param listener
     */
    public void onTweet(StatusListener listener) {
        listenerAdded = true;
        stream.addListener(listener);
    }

    /**
     * Tweet from the user's account
     * @param msg
     */
    public void tweet(String msg) throws TwitterException {
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
    public void listen(User... users) {
        if(!listenerAdded) throw new IllegalStateException("onTweet() must be called before calling listen()");
        long[] d = new long[users.length];
        int i = 0;
        for(User u : users){
            long id = u.getId();
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
    public void follow(User user) throws TwitterException {
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
    public int getFollowerCount(User user) {
        return user.getFollowersCount();
    }

    /**
     * Get a list of followers of the specified user.
     * @return
     * @throws TwitterException
     */
    public ArrayList<User> getFollowers() throws TwitterException{

        ArrayList<User> followersArrayList = new ArrayList<>(getFollowerCount(gameUser));

        long cursor = -1L;
        IDs ids;
        do {
            ids = wrapper.getFollowersIDs(gameUser.getId(), cursor);
            for(long id : ids.getIDs()) {
                User u = getUser(id);
                followersArrayList.add(u);
            }
        }while((cursor = ids.getNextCursor()) != 0);

        return followersArrayList;
    }

    /**
     * Get the user's blob size.
     * In the case of the methods below returning 0, this method will return 1.
     * <br>
     * Dependent on getFollowers(), getFollowing() and getLikes()
     * @param user
     * @return
     */
    public float getSize(User user) {
        float size =  (float)Math.log(1 + getFollowerCount(user) + ((float)getLikes(user) / (getFollowing(user) + 1)));
        if(size < 1) return 1;

        return size;
    }

    /**
     * Get the user object for the specified handle (not including the @ character)
     * @param handle
     * @return
     */
    public User getUser(String handle) throws TwitterException {
        ResponseList<User> response = wrapper.lookupUsers(handle);
        return response.size() == 0 ? null : response.get(0);
    }

    public User getUser(long id) throws TwitterException {
        ResponseList<User> response = wrapper.lookupUsers(id);
        return response.size() == 0 ? null : response.get(0);
    }

	public int getFollowing() {
		return getFollowing(gameUser);
	}

    /**
     * Get the number of users that the specified user follows
     * @param user
     * @return
     */
    public int getFollowing(User user) {
        return user.getFriendsCount();
    }

	public int getLikes() {
		return getLikes(gameUser);
	}

    /**
     * Get the number of tweets that the user has favourited/liked
     * @param user
     * @return
     */
    public  int getLikes(User user) {
        return user.getFavouritesCount();
    }

	public String getProfileImageURL() {
		return getProfileImageURL(gameUser);
	}

    /**
     * Get the URL of the user's profile image
     * @param user
     * @return
     */
    public String getProfileImageURL(User user) {
        return user.getProfileImageURL();
    }

	public BufferedImage getProfileImage() {
		return getProfileImage(gameUser);
	}

    /**
     * Gets the user's profile image.
     * <br>
     * Prints to System.err if an error occurs
     * @param user
     * @return
     */
    public BufferedImage getProfileImage(User user) {
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
