import org.apache.commons.io.FileUtils;
import twitter4j.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class Main {

    private static final String SEARCH_QUERY = "lang:ja スタバなう";
    private static final String IMAGE_PREFIX = "Image_";
    private static final String PROJECT_DIR = "/Users/keisuke/program/IdeaProjects/starbucks-image-collector/Image/";
    private static final int MAX_TWEET_NUM = 50;

    public static void main(String[] args){

        // Twitterのインスタンスを得る
        Twitter twitter = new TwitterFactory().getInstance();

        // 検索クエリを表すQueryのインスタンスを得る
        Query query = new Query(SEARCH_QUERY);
        query.setCount(MAX_TWEET_NUM);

        try {
            // 検索を実行
            QueryResult search_result = twitter.search(query);
            for(Status tweet : search_result.getTweets()) {
                // Mediaの数だけループ
                for(MediaEntity media : tweet.getMediaEntities()) {
                    // jpgの場合のみ実行
                    if(!media.getMediaURL().endsWith(".jpg")) continue;

                    // ランダムな文字列を付加した画像ファイルを作成
                    String extension = media.getMediaURL().substring(media.getMediaURL().lastIndexOf(".") + 1);
                    File image_file = new File(PROJECT_DIR + IMAGE_PREFIX + getRandomString() + "." + extension);

                    // commons ioのcopyURLToFileを使って画像をダウンロード
                    URL url = new URL(media.getMediaURL());
                    FileUtils.copyURLToFile(url, image_file);
                }
            }
        } catch (TwitterException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ランダムな文字列を返す
     * @return String UUIDを表す文字列
     */
    private static String getRandomString() {
        return UUID.randomUUID().toString();
    }
}