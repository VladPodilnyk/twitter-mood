package app.twitter.domain

import io.circe.{Encoder, Json}

final case class Status(value: Mood)
object Status {
  implicit val encoder: Encoder[Status] = Encoder.instance {
    s =>
      s.value match {
        case Mood.ANGRY   => Json.obj("mood" -> Json.fromString(Mood.ANGRY.toString))
        case Mood.CALM    => Json.obj("mood" -> Json.fromString(Mood.CALM.toString))
        case Mood.UNKNOWN => Json.obj("mood" -> Json.fromString(Mood.UNKNOWN.toString))
      }
  }
}
