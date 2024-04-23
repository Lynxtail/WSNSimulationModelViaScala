import QueueingNetwork._
import scala.math.pow
object Main {
	@main def main(args: Array[String]): Unit = {
		val L: Int = 8

		val theta: Array[Array[Double]] = Array(
			Array(0, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L, 1 / L),
			Array(.5, 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
			Array(.5, 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
			Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
			Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
			Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
			Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1)), 1 / (2 * (L - 1))),
			Array(.5, 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 1 / (2 * (L - 1)), 0, 1 / (2 * (L - 1))),
			Array(1, 0, 0, 0, 0, 0, 0, 0, 0)
		)
		val lambda0: Int = 5
		val mu: Array[Double] = Array(5, 7, 4, 10, 5, 6, 9, 10)

		val gamma: Array[Double] = Array(10e-3, 10e-3, 10e-3, 10e-4, 10e-3, 10e-3, 10e-3, 10e-4)
		val tauThreshold: Double = 0.5

		val nw: QueueingNetwork = new QueueingNetwork(math.pow(10, 4).toInt, mu.length, lambda0, theta, mu, gamma, tauThreshold)
		nw.simulation()
	}
}
