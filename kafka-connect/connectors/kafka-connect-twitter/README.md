# Introduction

This connector uses the twitter streaming api to listen for status update messages and 
convert them to a Kafka Connect struct on the fly. The goal is to match as much of the 
Twitter Status object as possible.

# Configuration

## TwitterSourceConnector

This Twitter Source connector is used to pull data from Twitter in realtime.

```properties
name=connector1
tasks.max=1
connector.class=com.github.jcustenborder.kafka.connect.twitter.TwitterSourceConnector

# Set these required values
twitter.oauth.accessTokenSecret=
process.deletes=
filter.keywords=
kafka.status.topic=
kafka.delete.topic=
twitter.oauth.consumerSecret=
twitter.oauth.accessToken=
twitter.oauth.consumerKey=
```

| Name                            | Description                                       | Type     | Default | Valid Values | Importance |
|---------------------------------|---------------------------------------------------|----------|---------|--------------|------------|
| filter.keywords                 | Twitter keywords to filter for.                   | list     |         |              | high       |
| kafka.delete.topic              | Kafka topic to write delete events to.            | string   |         |              | high       |
| kafka.status.topic              | Kafka topic to write the statuses to.             | string   |         |              | high       |
| process.deletes                 | Should this connector process deletes.            | boolean  |         |              | high       |
| twitter.oauth.accessToken       | OAuth access token                                | password |         |              | high       |
| twitter.oauth.accessTokenSecret | OAuth access token secret                         | password |         |              | high       |
| twitter.oauth.consumerKey       | OAuth consumer key                                | password |         |              | high       |
| twitter.oauth.consumerSecret    | OAuth consumer secret                             | password |         |              | high       |
| twitter.debug                   | Flag to enable debug logging for the twitter api. | boolean  | false   |              | low        |


# Schemas

## com.github.jcustenborder.kafka.connect.twitter.Place

Returns the place attached to this status

| Name          | Optional | Schema                                                                                                | Default Value | Documentation |
|---------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| Name          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| StreetAddress | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| CountryCode   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| Id            | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| Country       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| PlaceType     | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| URL           | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| FullName      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.GeoLocation

Returns The location that this tweet refers to if available.

| Name      | Optional | Schema                                                                                                  | Default Value | Documentation                             |
|-----------|----------|---------------------------------------------------------------------------------------------------------|---------------|-------------------------------------------|
| Latitude  | false    | [Float64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#FLOAT64) |               | returns the latitude of the geo location  |
| Longitude | false    | [Float64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#FLOAT64) |               | returns the longitude of the geo location |

## com.github.jcustenborder.kafka.connect.twitter.StatusDeletionNotice

Message that is received when a status is deleted from Twitter.

| Name     | Optional | Schema                                                                                              | Default Value | Documentation |
|----------|----------|-----------------------------------------------------------------------------------------------------|---------------|---------------|
| StatusId | false    | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64) |               |               |
| UserId   | false    | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.StatusDeletionNoticeKey

Key for a message that is received when a status is deleted from Twitter.

| Name     | Optional | Schema                                                                                              | Default Value | Documentation |
|----------|----------|-----------------------------------------------------------------------------------------------------|---------------|---------------|
| StatusId | false    | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.StatusKey

Key for a twitter status.

| Name | Optional | Schema                                                                                              | Default Value | Documentation |
|------|----------|-----------------------------------------------------------------------------------------------------|---------------|---------------|
| Id   | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.Status

Twitter status message.

| Name                 | Optional | Schema                                                                                                                                         | Default Value | Documentation                                                                                                                    |
|----------------------|----------|------------------------------------------------------------------------------------------------------------------------------------------------|---------------|----------------------------------------------------------------------------------------------------------------------------------|
| CreatedAt            | true     | [Timestamp](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Timestamp.html)                                                |               | Return the created_at                                                                                                            |
| Id                   | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                            |               | Returns the id of the status                                                                                                     |
| Text                 | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                          |               | Returns the text of the status                                                                                                   |
| Source               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                          |               | Returns the source                                                                                                               |
| Truncated            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               | Test if the status is truncated                                                                                                  |
| InReplyToStatusId    | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                            |               | Returns the in_reply_tostatus_id                                                                                                 |
| InReplyToUserId      | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                            |               | Returns the in_reply_user_id                                                                                                     |
| InReplyToScreenName  | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                          |               | Returns the in_reply_to_screen_name                                                                                              |
| GeoLocation          | true     | [com.github.jcustenborder.kafka.connect.twitter.GeoLocation](#com.github.jcustenborder.kafka.connect.twitter.GeoLocation)                      |               | Returns The location that this tweet refers to if available.                                                                     |
| Place                | true     | [com.github.jcustenborder.kafka.connect.twitter.Place](#com.github.jcustenborder.kafka.connect.twitter.Place)                                  |               | Returns the place attached to this status                                                                                        |
| Favorited            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               | Test if the status is favorited                                                                                                  |
| Retweeted            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               | Test if the status is retweeted                                                                                                  |
| FavoriteCount        | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                            |               | Indicates approximately how many times this Tweet has been "favorited" by Twitter users.                                         |
| User                 | false    | [com.github.jcustenborder.kafka.connect.twitter.User](#com.github.jcustenborder.kafka.connect.twitter.User)                                    |               | Return the user associated with the status.
This can be null if the instance is from User.getStatus().                           |
| Retweet              | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               |                                                                                                                                  |
| Contributors         | false    | Array of [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                   |               | Returns an array of contributors, or null if no contributor is associated with this status.                                      |
| RetweetCount         | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                            |               | Returns the number of times this tweet has been retweeted, or -1 when the tweet was created before this feature was enabled.     |
| RetweetedByMe        | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               |                                                                                                                                  |
| CurrentUserRetweetId | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                            |               | Returns the authenticating user's retweet's id of this tweet, or -1L when the tweet was created before this feature was enabled. |
| PossiblySensitive    | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)                                        |               |                                                                                                                                  |
| Lang                 | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                          |               | Returns the lang of the status text if available.                                                                                |
| WithheldInCountries  | false    | Array of [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                 |               | Returns the list of country codes where the tweet is withheld                                                                    |
| HashtagEntities      | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.HashtagEntity](#com.github.jcustenborder.kafka.connect.twitter.HashtagEntity)         |               | Returns an array if hashtag mentioned in the tweet.                                                                              |
| UserMentionEntities  | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.UserMentionEntity](#com.github.jcustenborder.kafka.connect.twitter.UserMentionEntity) |               | Returns an array of user mentions in the tweet.                                                                                  |
| MediaEntities        | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.MediaEntity](#com.github.jcustenborder.kafka.connect.twitter.MediaEntity)             |               | Returns an array of MediaEntities if medias are available in the tweet.                                                          |
| SymbolEntities       | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.SymbolEntity](#com.github.jcustenborder.kafka.connect.twitter.SymbolEntity)           |               | Returns an array of SymbolEntities if medias are available in the tweet.                                                         |
| URLEntities          | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.URLEntity](#com.github.jcustenborder.kafka.connect.twitter.URLEntity)                 |               | Returns an array if URLEntity mentioned in the tweet.                                                                            |

## com.github.jcustenborder.kafka.connect.twitter.User

Return the user associated with the status.
This can be null if the instance is from User.getStatus().

| Name                           | Optional | Schema                                                                                                         | Default Value | Documentation                                                                                |
|--------------------------------|----------|----------------------------------------------------------------------------------------------------------------|---------------|----------------------------------------------------------------------------------------------|
| Id                             | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)            |               | Returns the id of the user                                                                   |
| Name                           | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the name of the user                                                                 |
| ScreenName                     | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the screen name of the user                                                          |
| Location                       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the location of the user                                                             |
| Description                    | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the description of the user                                                          |
| ContributorsEnabled            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               | Tests if the user is enabling contributors                                                   |
| ProfileImageURL                | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the profile image url of the user                                                    |
| BiggerProfileImageURL          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| MiniProfileImageURL            | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| OriginalProfileImageURL        | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileImageURLHttps           | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| BiggerProfileImageURLHttps     | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| MiniProfileImageURLHttps       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| OriginalProfileImageURLHttps   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| DefaultProfileImage            | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               | Tests if the user has not uploaded their own avatar                                          |
| URL                            | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the url of the user                                                                  |
| Protected                      | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               | Test if the user status is protected                                                         |
| FollowersCount                 | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               | Returns the number of followers                                                              |
| ProfileBackgroundColor         | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileTextColor               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileLinkColor               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileSidebarFillColor        | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileSidebarBorderColor      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileUseBackgroundImage      | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| DefaultProfile                 | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               | Tests if the user has not altered the theme or background                                    |
| ShowAllInlineMedia             | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| FriendsCount                   | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               | Returns the number of users the user follows (AKA "followings")                              |
| CreatedAt                      | true     | [Timestamp](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Timestamp.html)                |               |                                                                                              |
| FavouritesCount                | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               |                                                                                              |
| UtcOffset                      | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               |                                                                                              |
| TimeZone                       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBackgroundImageURL      | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBackgroundImageUrlHttps | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerURL               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerRetinaURL         | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerIPadURL           | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerIPadRetinaURL     | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerMobileURL         | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBannerMobileRetinaURL   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               |                                                                                              |
| ProfileBackgroundTiled         | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| Lang                           | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)          |               | Returns the preferred language of the user                                                   |
| StatusesCount                  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               |                                                                                              |
| GeoEnabled                     | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| Verified                       | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| Translator                     | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               |                                                                                              |
| ListedCount                    | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)            |               | Returns the number of public lists the user is listed on, or -1 if the count is unavailable. |
| FollowRequestSent              | true     | [Boolean](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#BOOLEAN)        |               | Returns true if the authenticating user has requested to follow this user, otherwise false.  |
| WithheldInCountries            | false    | Array of [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the list of country codes where the user is withheld                                 |

## com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant

| Name        | Optional | Schema                                                                                                | Default Value | Documentation |
|-------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------|
| Url         | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |
| Bitrate     | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               |               |
| ContentType | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size

| Name   | Optional | Schema                                                                                              | Default Value | Documentation |
|--------|----------|-----------------------------------------------------------------------------------------------------|---------------|---------------|
| Resize | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32) |               |               |
| Width  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32) |               |               |
| Height | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32) |               |               |

## com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity

| Name                   | Optional | Schema                                                                                                                                                                                                                                            | Default Value | Documentation                                                               |
|------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|-----------------------------------------------------------------------------|
| VideoAspectRatioWidth  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               |                                                                             |
| VideoAspectRatioHeight | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               |                                                                             |
| VideoDurationMillis    | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                                                                                                                               |               |                                                                             |
| VideoVariants          | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant](#com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant)                                                                                |               |                                                                             |
| ExtAltText             | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               |                                                                             |
| Id                     | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                                                                                                                               |               | Returns the id of the media.                                                |
| Type                   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media type photo, video, animated_gif.                          |
| MediaURL               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media URL.                                                      |
| Sizes                  | false    | Map of <[Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32), [com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size](#com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size)> |               | Returns size variations of the media.                                       |
| MediaURLHttps          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media secure URL.                                               |
| URL                    | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the URL mentioned in the tweet.                                     |
| Text                   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the URL mentioned in the tweet.                                     |
| ExpandedURL            | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the expanded URL if mentioned URL is shorten.                       |
| Start                  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               | Returns the index of the start character of the URL mentioned in the tweet. |
| End                    | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               | Returns the index of the end character of the URL mentioned in the tweet.   |
| DisplayURL             | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the display URL if mentioned URL is shorten.                        |

## com.github.jcustenborder.kafka.connect.twitter.HashtagEntity

| Name  | Optional | Schema                                                                                                | Default Value | Documentation                                            |
|-------|----------|-------------------------------------------------------------------------------------------------------|---------------|----------------------------------------------------------|
| Text  | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the text of the hashtag without #.               |
| Start | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the start character of the hashtag. |
| End   | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the end character of the hashtag.   |

## com.github.jcustenborder.kafka.connect.twitter.MediaEntity

| Name                   | Optional | Schema                                                                                                                                                                                                                                            | Default Value | Documentation                                                               |
|------------------------|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------------|-----------------------------------------------------------------------------|
| Id                     | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                                                                                                                               |               | Returns the id of the media.                                                |
| Type                   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media type photo, video, animated_gif.                          |
| MediaURL               | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media URL.                                                      |
| Sizes                  | false    | Map of <[Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32), [com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size](#com.github.jcustenborder.kafka.connect.twitter.MediaEntity.Size)> |               |                                                                             |
| MediaURLHttps          | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the media secure URL.                                               |
| VideoAspectRatioWidth  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               |                                                                             |
| VideoAspectRatioHeight | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               |                                                                             |
| VideoDurationMillis    | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)                                                                                                                                               |               |                                                                             |
| VideoVariants          | true     | Array of [com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant](#com.github.jcustenborder.kafka.connect.twitter.ExtendedMediaEntity.Variant)                                                                                |               | Returns size variations of the media.                                       |
| ExtAltText             | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               |                                                                             |
| URL                    | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the URL mentioned in the tweet.                                     |
| Text                   | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the URL mentioned in the tweet.                                     |
| ExpandedURL            | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the expanded URL if mentioned URL is shorten.                       |
| Start                  | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               | Returns the index of the start character of the URL mentioned in the tweet. |
| End                    | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)                                                                                                                                               |               | Returns the index of the end character of the URL mentioned in the tweet.   |
| DisplayURL             | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING)                                                                                                                                             |               | Returns the display URL if mentioned URL is shorten.                        |

## com.github.jcustenborder.kafka.connect.twitter.SymbolEntity

| Name  | Optional | Schema                                                                                                | Default Value | Documentation                                           |
|-------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------------------------------------------------|
| Start | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the start character of the symbol. |
| End   | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the end character of the symbol.   |
| Text  | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the text of the entity                          |

## com.github.jcustenborder.kafka.connect.twitter.URLEntity

| Name        | Optional | Schema                                                                                                | Default Value | Documentation                                                               |
|-------------|----------|-------------------------------------------------------------------------------------------------------|---------------|-----------------------------------------------------------------------------|
| URL         | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the URL mentioned in the tweet.                                     |
| Text        | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the URL mentioned in the tweet.                                     |
| ExpandedURL | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the expanded URL if mentioned URL is shorten.                       |
| Start       | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the start character of the URL mentioned in the tweet. |
| End         | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the end character of the URL mentioned in the tweet.   |
| DisplayURL  | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the display URL if mentioned URL is shorten.                        |

## com.github.jcustenborder.kafka.connect.twitter.UserMentionEntity

| Name       | Optional | Schema                                                                                                | Default Value | Documentation                                                 |
|------------|----------|-------------------------------------------------------------------------------------------------------|---------------|---------------------------------------------------------------|
| Name       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the name mentioned in the status.                     |
| Id         | true     | [Int64](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT64)   |               | Returns the user id mentioned in the status.                  |
| Text       | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the screen name mentioned in the status.              |
| ScreenName | true     | [String](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#STRING) |               | Returns the screen name mentioned in the status.              |
| Start      | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the start character of the user mention. |
| End        | true     | [Int32](https://kafka.apache.org/0102/javadoc/org/apache/kafka/connect/data/Schema.Type.html#INT32)   |               | Returns the index of the end character of the user mention.   |


# Running in development

```
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
$CONFLUENT_HOME/bin/connect-standalone connect/connect-avro-docker.properties config/TwitterSourceConnector.properties
```
