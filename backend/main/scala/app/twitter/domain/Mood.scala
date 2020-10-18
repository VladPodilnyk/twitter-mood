package app.twitter.domain

sealed trait Mood
object Mood {
  case object ANGRY extends Mood
  case object CALM extends Mood
  case object UNKNOWN extends Mood
}
