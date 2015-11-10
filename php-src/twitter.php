require_once("TwitterApiExchange.php");

class Twitter {
	
	protected $wrapper;

	public function __construct($consumerToken, $consumerSecret, $accessToken, $accessSecret) {
		$this->wrapper = new TwitterApiExchange(array(
			'oauth_access_token' => $consumerToken,
			'oauth_access_token_secret' => $consumerSecret,
			'consumer_key' => $accessToken,
			'consumer_secret' => $accessSecret
		));
	}

	public function getUserTweets($userHandle) {
		return sendGetRequest("https://api.twitter.com/1.1/statuses/user_timeline.json", "q", "@" . $userHandle);
	}

	protected function sendGetRequest($url, $field_name, $field_val) {
		return sendRequest($url, $field_name, $field_val, "GET");
	}

	protected function sendRequest($url, $field_name, $field_val, $method) {
		return $wrapper->setGetField("?" . urlencode($field_name . "=" . $field_val))
			->buildOauth(urlencode($url), $method)->performRequest();
	}

}
