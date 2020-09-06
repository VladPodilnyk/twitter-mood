package app.twitter.effects

import cats.Monad
import cats.effect.Sync

object effects {
  final type SyncThrowable[F[_, _]] = Sync[F[Throwable, ?]]
  object SyncThrowable {
    implicit def apply[F[_, _]: SyncThrowable]: SyncThrowable[F] = implicitly
  }

  final type MonadThrowable[F[_, _]] = Monad[F[Throwable, ?]]
  object MonadThrowable {
    def apply[F[_, _]: MonadThrowable]: MonadThrowable[F] = implicitly
  }
}
