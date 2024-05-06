package wsn
object Main extends App {
	val L: Int = 8
	val L_var: Double = L.toDouble

	val theta: Array[Array[Double]] = Array(
		Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
		Array(.5, 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
		Array(.5, 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1))),
		Array(1, 0, 0, 0, 0, 0, 0, 0, 0)
	)
	val lambda0: Double = 3
	val mu: Array[Double] = Array(2.5, 1.7, 1.4, 1, 1.5, 1.6, 1.9, 1)

	val gamma: Array[Double] = Array(0, 10e-2, 10e-3, 0, 0, 10e-3, 0, 0)
//	val gamma: Array[Double] = Array(10e-3, 10e-3, 10e-3, 10e-4, 10e-3, 10e-3, 10e-3, 10e-4)
	val tauThreshold: Double = 1.5

	val nw: QueueingNetwork = new QueueingNetwork(math.pow(10, 6) / lambda0 * 2, mu.length, lambda0, theta, mu, gamma, tauThreshold)

	nw.simulation()
	val omega = nw.getOmegaIterationGaussSeidelMethod(L.toInt, theta)
}
