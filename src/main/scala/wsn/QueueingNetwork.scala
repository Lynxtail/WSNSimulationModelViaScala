package wsn

import scala.math.log
import scala.util.Random
import scala.util.control.Breaks

class QueueingNetwork(val tMax: Double,  val L: Int,
                      var lambda0: Double, var theta: Array[Array[Double]],
                      var mu: Array[Double], var gamma: Array[Double],
                      val tauThreshold: Double) {
	private val initialTheta: Array[Array[Double]] = theta
	var tNow: Double = 0
	private var tOld: Double = 0
	private var indicator: Boolean = false

	// моменты активации процессов
	// обслуживания (_ = 1, L + 1)
	// генерации (_ = 0)
	private val tProcesses: Array[Double] = Array.fill(L + 1)(tMax + 1)
	tProcesses(0) = 0
	private val systems: List[QueueingSystem] = initSystems()

	private var servicedDemands: Int = 0
	private var lostDemands: Int = 0
	private var totalDemands: Int = 0
	private var sumLifeTime: Double = 0
	private var b: Array[Int] = Array.fill(L)(1)
	private var countStates: Int = 1
	private var tauSummarized: Double = 0

	private var tau: Double = 0

	private def initSystems(): List[QueueingSystem] = {
		var systems: List[QueueingSystem] = List(new QueueingSystem(0, 0, 0, 0)) // источник
//		systems.head.serializationTimeStates(List(0))
		for (system <- 1 to L) {
			systems :+= new QueueingSystem(system, 1, mu(system - 1), gamma(system - 1))
//			systems.last.serializationTimeStates(List(0))
			systems.last.beDestroyedAt = tNow + systems.last.calculateDestroyTime()
			// print(systems[-1].beDestroyedAt)
		}
		systems
	}

	private def arrivalTime(): Double = {
		-log(Random.nextDouble()) / lambda0
	}

	private def conditionNorm(omega: Array[Double], oldOmega: Array[Double]): Double = {
		var newOmega = new Array[Double](omega.length)
		for (i <- newOmega.indices) {
			newOmega(i) = omega(i) - oldOmega(i)
		}

		def frobeniusNorm(vector: Array[Double]): Double = {
			var sumSq = 0.0
			for (i <- 0 until vector.length) {
				sumSq += math.pow(vector(i), 2)
			}
			math.sqrt(sumSq)
		}

		frobeniusNorm(newOmega) / frobeniusNorm(oldOmega)
	}

	def getOmegaIterationGaussSeidelMethod(L: Int, theta: Array[Array[Double]]): Array[Double] = {
		// нормирование матрицы
		var thetaNormalized = theta
		for (i <- thetaNormalized.indices;
		     j <- thetaNormalized(i).indices) {
			if (i == j){
				thetaNormalized(i)(j) -= 1
			}
		}

		var omega = Array.fill[Double](L + 1)(1 / (L.toDouble + 1))
		var oldOmega = Array.fill[Double](L + 1)(0)
		oldOmega(0) = 1
		var b = Array.fill[Double](L + 1)(0)
		val eps = 0.0000001

		var k = 1
		
		while (k <= 500 && conditionNorm(omega, oldOmega) > eps) {
			oldOmega = omega.clone()
			for (j <- 0 to L) {
				if (thetaNormalized(j)(j) != 0) {

					var sum1 = 0.0
					for (i <- 0 until j){
						sum1 += thetaNormalized(i)(j) * omega(i)
					}

					var sum2 = 0.0
					for (i <- j + 1 to L){
						sum2 += thetaNormalized(i)(j) * oldOmega(i)
					}

					omega(j) = (b(j) - (sum1 + sum2)) / thetaNormalized(j)(j)

					// omega(j) = (b(j) - 
					// 	(thetaNormalized.slice(0, j).map(i => thetaNormalized(i)(j) * omega(i)).sum + 
					// 	thetaNormalized.slice(j + 1, L + 1).map(i => thetaNormalized(i)(j) * oldOmega(i)).sum)) / thetaNormalized(j)(j)

					// omega(j) = (b(j) - (thetaNormalized.slice(0, j).map(_.zip(omega.slice(0, j)).map { case (a, b) => a * b }.sum) + thetaNormalized.slice(j + 1, L + 1).map(_.zip(oldOmega.slice(j + 1, L + 1)).map { case (a, b) => a * b }.sum)) / thetaNormalized(j)(j)
				} else {
					omega(j) = 0
				}
			}
			k += 1
		}

		omega = omega.map(_ / omega.sum)
		println(s"k = $k\nOmega: ${omega.mkString(",")}\nCheck (~1): ${omega.sum}")
		omega
	}

	private def changeTheta(): Array[Array[Double]] = {
		var oldTheta: Array[Array[Double]] = theta.map(_.clone)
		val newTheta: Array[Array[Double]] = oldTheta.map(_.clone)
		for (m <- b.indices) {
			if (b(m) == 0) {
				for {
					i <- 0 to L
					k <- 0 to L
				} {
					if (i != m + 1 && k != m + 1) {
						if (oldTheta(i)(m + 1) != 1) {
							newTheta(i)(k) = oldTheta(i)(k) / (1 - oldTheta(i)(m + 1))
						}
					} else if (k != m + 1) {
						newTheta(m + 1)(k) = 0
					} else if (i != m + 1) {
						newTheta(i)(m + 1) = 0
					}
				}
				newTheta(m + 1)(m + 1) = 1
			}
			oldTheta = newTheta.map(_.clone)
		}
		newTheta
	}

	private def checkMatrix(): Boolean = {
		var excepted: List[Int] = List()
		for (i <- theta.indices) {
			if (theta(i)(i) == 1) excepted :+= i
		}

		def dfs(start: Int, visited: Array[Boolean]): Unit = {
			for ((v, ind) <- theta(start).zipWithIndex) {
				if (v > 0) {
					visited(start) = true
					if (!visited(ind)) {
						dfs(ind, visited)
					}
				}
			}
		}

		var visited: Array[Boolean] = Array.fill(L + 1)(false)
		for (i <- 0 to L) {
			if (!excepted.contains(i)) {
				visited = Array.fill(L + 1)(false)
				excepted.foreach(m => visited(m) = true)
				if (!visited(i)) {
					dfs(i, visited)
					if (!visited.forall(identity)) return false
				}
			}
		}
		visited.forall(identity)
	}

	private def routing(i: Int, demand: Demand): Unit = {
		val r = scala.util.Random.nextDouble()
		var tmpSum = 0.0
		var j = 0
		val innerLoop = new Breaks
		innerLoop.breakable {
			while (j < L) {
				if (theta(i)(j) > 0) {
					tmpSum += theta(i)(j)
				}
				if (tmpSum >= r) {
					innerLoop.break()
				}
				j += 1
			}
		}
		// println(s'\tтребование ${demand.id} переходит из $i в $j')

		if (i != 0) {
			systems(i).updateTimeStates(tNow)
			systems(i).demands -= demand
			// println(s'\tтребования в $i: ${systems(i).currentDemands()}')
		}
		if (j != 0) {
			systems(j).updateTimeStates(tNow)
			systems(j).demands += demand
			// println(s'\tтребования в $j: ${systems(j).currentDemands()}')
		} else {
			servicedDemands += 1
			sumLifeTime += tNow - demand.arrival
		}
	}

	private def restore(): Unit = {
		println(s"Сеть восстанавливается при \nb: ${b.mkString("(", ", ", ")")}")
		b = Array.fill(L)(1)
		theta = initialTheta.clone()
		for (system <- systems.drop(1)) {
			// println(system.gamma)
			system.beDestroyedAt = tNow + system.calculateDestroyTime
		}
	}

	def simulation(): Unit = {
		var demandId = 0
		while (tNow < tMax) {
			println(s"$tNow")
			// [println(s"${system.id}\n\t${system.deserializationTimeStates()}\n\t${system.demands.length}") for system <- systems]
			indicator = false

			// генерация требования
			if (tProcesses(0) == tNow) {
				indicator = true
				tProcesses(0) = tNow + arrivalTime()
				demandId += 1
				val demand = Demand(demandId, tNow)
				totalDemands += 1
//				println(s"\tтребование $demandId поступило в сеть")
				routing(0, demand)
			}

			for (i <- 1 to L) {
				// начало обслуживания
				if (!systems(i).serviceFlag && systems(i).demands.nonEmpty) {
					indicator = true
					systems(i).serviceFlag = true
					tProcesses(i) = tNow + systems(i).calculateServiceTime
//					println(s"\tтребование ${systems(i).demands(0).id} начало обслуживаться в системе ${systems(i).id}")
				}

				// завершение обслуживания
				if (tProcesses(i) == tNow) {
					indicator = true
					systems(i).serviceFlag = false
//					println(s"\tтребование ${systems(i).demands(0).id} закончило обслуживаться в системе ${systems(i).id}")
					routing(i, systems(i).demands(0))
					tau = if (servicedDemands != 0) sumLifeTime / servicedDemands else 0
					tProcesses(i) = tMax + 1
				}

				// выход из строя
				if (systems(i).beDestroyedAt == tNow) {
					println(s"\tСистема $i выходит из строя")
					systems(i).state = false
					lostDemands += systems(i).demands.length
					// println(systems(i).currentDemands())
					systems(i).demands.clear()
					b(i - 1) = 0
					systems(i).beDestroyedAt = tMax + 1
					tProcesses(i) = tMax + 1
					
					// поиск омега
					val omega = getOmegaIterationGaussSeidelMethod(L, theta)

					theta = changeTheta()
					if (!checkMatrix()) {
						restore()
					}
					val currentTau = if (servicedDemands != 0) sumLifeTime / servicedDemands else 0
					tauSummarized += currentTau
					countStates += 1
					servicedDemands = 0
					sumLifeTime = 0
				}
			}

			if (tau > tauThreshold && !b.sameElements(Array.fill(L)(1))) {
				println(s"\ttau = ${tau}, tau_ = ${tauThreshold}")
				restore()
				val currentTau = if (servicedDemands != 0) sumLifeTime / servicedDemands else 0
				tauSummarized += currentTau
				countStates += 1
				servicedDemands = 0
				sumLifeTime = 0
			}

			if (!indicator) {
				// статистика
				for (system <- systems) {
					system.updateTimeStates(tNow)
				}

				// println('------')
				// println(s'tau for $b = $tau')
				// for (i <- 0 to L) {
				// println(s'Система $i:\n${for (state <- systems(i).deserializationTimeStates()) yield state / tMax}')
				// }
				// println('------\n')

				tOld = tNow
				tNow = (tProcesses :++ systems.tail.map(_.beDestroyedAt).toArray).min
			}
		}

		println(s"\nВсего требований: $totalDemands\nОбслужено ${totalDemands - lostDemands}, потеряно $lostDemands")
		println(s"Сеть перезапускалась ${countStates} раз")
		println(s"tau = ${if (tauSummarized != 0) tauSummarized / countStates else tau}")
		println(s"pLost = ${lostDemands.toDouble / totalDemands.toDouble}")
	}
}
