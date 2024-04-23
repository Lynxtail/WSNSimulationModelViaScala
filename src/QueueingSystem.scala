//import _root_.Demand
import scala.collection.mutable.ArrayBuffer
class QueueingSystem(val id: Int, val serverCnt: Int,
                     var mu: Double, var gamma: Double,
                     var state: Boolean = true,
                     var kMu: Int = 1, var kGamma: Int = 1) {

	var beDestroyedAt: Int = 0
	var serviceFlag: Boolean = false
	var demands: ArrayBuffer[Demand] = ArrayBuffer[Demand]()
	private var lastState: Int = 0
	private var timeStates: ArrayBuffer[Double] = ArrayBuffer[Double]()

	def calculateServiceTime(kMu: Int, mu: Double): Double = {
		val randomNumbers = List.fill(kMu)(scala.util.Random.nextDouble())
		val product = randomNumbers.foldLeft(1.0)(_ * _)
		-math.log(product) / mu
	}

	def calculateDestroyTime(kGamma: Int, gamma: Double): Double = {
		val randomNumbers = List.fill(kGamma)(scala.util.Random.nextDouble())
		val product = randomNumbers.foldLeft(1.0)(_ * _)
		-math.log(product) / gamma
	}

	def currentDemands(): List[Int] = {
		demands.map(_.id)
	}

	def updateTimeStates(tNow: Double): Unit = {
		// println(s"\tsystem $id last state: $lastState")
		// десериализация timeStates
		if (timeStates.length <= demands.length + 1) {
//			timeStates ++= List.fill(demands.length + 1 - timeStates.length)(0)
			timeStates += 0
		}

		try {
			timeStates(demands.length) += tNow - lastState
		} catch {
			case e: IndexOutOfBoundsException =>
				println(s"$id $timeStates ${demands.length} $lastState")
				throw e
		}

		lastState = tNow
		// сериализация timeStates
	}

}
