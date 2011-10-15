package scalaz
package syntax

/** Wraps a value `self` and provides methods related to `Applicative` */
trait ApplicativeV[F[_],A] extends SyntaxV[F[A]] {
  ////
  def map2[B,C](fb: F[B])(f: (A,B) => C)(implicit F: Applicative[F]): F[C] = F.map2(self,fb)(f)
  def pair[B](fb: F[B])(implicit F: Applicative[F]): F[(A, B)] = F.map2(self, fb)((_,_))
  def traverse[G[_], B](f: A => G[B])(implicit F: Traverse[F], G: Applicative[G]): G[F[B]] = G.traverse(self)(f)
  def sequence[G[_], B](implicit ev: F[A] <:< F[G[B]], F: Traverse[F], G: Applicative[G]): G[F[B]] = G.sequence(ev(self))
  ////
}

trait ToApplicativeSyntax extends ToApplySyntax with ToPointedSyntax {
  implicit def ToApplicativeV[F[_],A](v: F[A]) =
    new ApplicativeV[F,A] { def self = v }
  implicit def ToApplicativeVFromBin[F[_, _], X, A](v: F[X, A]) =
    new ApplicativeV[({type f[a] = F[X, a]})#f,A] { def self = v }
  implicit def ToApplicativeVFromBinT[F[_, _[_], _], G[_], X, A](v: F[X, G, A]) =
    new ApplicativeV[({type f[a] = F[X, G, a]})#f,A] { def self = v }
  implicit def ToApplicativeVFromBinTId[F[_, _[_], _], X, A](v: F[X, Id, A]) =
    new ApplicativeV[({type f[a] = F[X, Id, a]})#f,A] { def self = v }

  ////

  ////
}

trait ApplicativeSyntax[F[_]] extends ApplySyntax[F] with PointedSyntax[F] {
  implicit def ToApplicativeV[A](v: F[A]): ApplicativeV[F, A] = new ApplicativeV[F,A] { def self = v }

  ////
  implicit def lift2[A,B,C](f: (A,B) => C)(implicit F: Applicative[F]) = F.lift2(f)
  implicit def lift3[A,B,C,D](f: (A,B,C) => D)(implicit F: Applicative[F]) = F.lift3(f)
  ////
}
