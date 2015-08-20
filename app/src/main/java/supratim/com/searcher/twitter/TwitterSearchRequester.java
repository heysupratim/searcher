package supratim.com.searcher.twitter;

import java.util.List;

import twitter4j.Status;

/**
 * Created by borax12 on 20/08/15.
 */
public interface TwitterSearchRequester {

    void finishSuccess(List<Status> statuses);
}
