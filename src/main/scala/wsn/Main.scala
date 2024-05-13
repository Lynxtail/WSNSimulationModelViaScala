package wsn
object Main extends App {
	val L: Int = 8
	val L_var: Double = L.toDouble

//	полносвязная сеть
//	val theta: Array[Array[Double]] = Array(
//		Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
//		Array(.5, 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
//		Array(.5, 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
//		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
//		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1))),
//		Array(.5, 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 1 / (2 * (L_var - 1)), 0, 1 / (2 * (L_var - 1))),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0)
//	)
//	val lambda0: Double = 1
//	val mu: Array[Double] = Array(2.5, 1.7, 1.4, 1, 1.5, 1.6, 1.9, 1)
//	val gamma: Array[Double] = Array(0, 10e-2, 10e-3, 0, 0, 10e-3, 0, 0)

//	топология звезда
//	val theta: Array[Array[Double]] = Array(
//		Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//		Array(1, 0, 0, 0, 0, 0, 0, 0, 0),
//	)

//	ячеистая топология
	val theta: Array[Array[Double]] = Array(
		Array(0, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var, 1 / L_var),
		Array(0, 0, 0, 1.0 / 2.0, 1.0 / 2.0, 0, 0, 0, 0),
		Array(0, 0, 0, 1.0 / 2.0, 1.0 / 2.0, 0, 0, 0, 0),
		Array(0, 0, 0, 0, 0, 1.0 / 2.0, 0, 1.0 / 2.0, 0),
		Array(0, 0, 0, 0, 0, 1.0 / 2.0, 0, 1.0 / 2.0, 0),
		Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
		Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
		Array(0, 0, 0, 0, 0, 1.0 / 3.0, 1.0 / 3.0, 0, 1.0 / 3.0),
		Array(1.0, 0, 0, 0, 0, 0, 0, 0, 0),
	)

	println(s"${theta.map(_.sum).mkString("Array(", ", ", ")")}")

//	скорость бит/с
//	val lambda0: Double = 100000
//	val mu: Array[Double] = Array(100000, 100000, 100000, 100000, 100000, 100000, 100000, 100000)

//	пакеты (по 13 байт)
//	val lambda0: Double = 3200.0 / 8.0 / 13.0
	val lambda0: Double = 40000.0 / 8.0 / 13.0
	val mu_tmp: Double = 100000.0 / 8.0 / 13.0
	val mu: Array[Double] = Array.fill(L)(mu_tmp)


//	200 часов
	val gamma: Array[Double] = Array(1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6, 1.39 * 10e-6)
//	20 часов
//	val gamma: Array[Double] = Array(1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5, 1.39 * 10e-5)
//	2 часа
//	val gamma: Array[Double] = Array(1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4, 1.39 * 10e-4)
//	1 час
//	val gamma: Array[Double] = Array(2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4, 2.78 * 10e-4)

	val tauThreshold: Double = 1
	val tMax = math.pow(10, 6) / lambda0 * 2
//	val tMax = math.pow(10, 1)

	val nw: QueueingNetwork = new QueueingNetwork(tMax, L, lambda0, theta, mu, gamma, tauThreshold)

	nw.simulation()
	val omega = nw.getOmegaIterationGaussSeidelMethod(L.toInt, theta)
}
