package app.twitter.client

import app.twitter.domain.{ForbiddenWords, Mood}

final class TextProcessor(blackListedWords: ForbiddenWords) {
  /**
    * Super simple function to process tweets with the following logic:
    * 1) calculate total number of words
    * 2) calculate total  number of bad words occurrence
    * 3) make a verdict base on bad-words/all-words ratio
    *
    * TODO: next step - more sophisticated algorithm or NLP
    */
  def defineMood(texts: List[String]): Mood = {
    val meanTweets = texts.map(isTweetMean).collect(_ == true).size
    if (meanTweets > (texts.size / 2)) Mood.ANGRY
    else Mood.CALM
  }

  // NOTE: super straightforward, probably not efficient
  private[this] def isTweetMean(text: String): Boolean = {
    val regexPattern = "[,.!?)(}{\\s\\]\\[]"
    val words        = text.split(regexPattern).filter(_.nonEmpty)
    val numberOfBadWords = words.foldLeft(0) {
      (badWordsCounter, word) =>
        if (blackListedWords.list.contains(word)) badWordsCounter + 1
        else badWordsCounter
    }
    numberOfBadWords > (words.length / 2)
  }
}
