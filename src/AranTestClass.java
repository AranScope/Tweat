import twitter4j.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by aranscope on 07/11/15.
 */
public class AranTestClass extends JPanel{
    public static void main(String[] args){
        JFrame frame = new JFrame("Twat");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<User> followers = null;

        try{
            followers = TwitterW.getFollowers(TwitterW.getUser("tweatgame"));

           
        }catch(Exception e){}

        try {
            TwitterW.onTweet(new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    System.out.println(status);
                    if(status.getText().toLowerCase().contains("@tweatgame")){

                    }

                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                }

                @Override
                public void onTrackLimitationNotice(int i) {

                }

                @Override
                public void onScrubGeo(long l, long l1) {

                }

                @Override
                public void onStallWarning(StallWarning stallWarning) {

                }

                @Override
                public void onException(Exception e) {

                }
            });

            //frame.add(new AranTestView(TwitterW.getProfileImage(TwitterW.getUser("aranscope"))));
            int i = 0;
            User[] us = new User[followers.size()];
            for(User user: followers){
                System.out.println(user.getScreenName());
                us[i++] = user;
            }
            TwitterW.listen(us);



        }catch(Exception e){e.printStackTrace();}

        frame.pack();
    }
}
