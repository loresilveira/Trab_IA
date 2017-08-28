package TrabalhoIA;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class Teste extends AdvancedRobot {

	int contador = 0;
	double posicaoDeTiro; // posicao onde se deve atirar ou mover o robo
	Random random = new Random();
	Map<String, Double> listaRobosAchados = new HashMap<String, Double>();

	public void run() {

		posicaoDeTiro = 10; // Inicializa posicaoDeTiro com angulo de 10

		while (true) { // enquanto nao achar alguem

			if (getRadarTurnRemaining() == 0.0) {
				setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
			}
			
			execute();

		}

	}

	// quando achar um robo adversario
	public void onScannedRobot(ScannedRobotEvent e) {
		
		listaRobosAchados.put(e.getName(), e.getDistance());
		
		for (String key : listaRobosAchados.keySet()) {
            
            Double value = listaRobosAchados.get(key);
            
            System.out.println(key + " = " + value);
		}
		
		execute();
		

		caminhar(e);

		atirar(e);
		
		
		
	}
	
	public void caminhar(ScannedRobotEvent e) {
	
	}

	public void atirar(ScannedRobotEvent e) {
		

		//	 	MOMENTO DO TIRO ABAIXOOOOOOOOOOOOOOOOOOOOOOOOO

		// Calculate exact location of the robot
		double absoluteBearing = getHeading() + e.getBearing();
		double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());
		
		// If it's close enough, fire!
		if (Math.abs(bearingFromGun) <= 3) {
			turnGunRight(bearingFromGun);
			// We check gun heat here, because calling fire()
			// uses a turn, which could cause us to lose track
			// of the other robot.
			if (getGunHeat() == 0) {
				fire(Math.min(3 - Math.abs(bearingFromGun), getEnergy() - .1));
			}
		} // otherwise just set the gun to turn.
		// Note:  This will have no effect until we call scan()
		else {
			turnGunRight(bearingFromGun);
		}
		// Generates another scan event if we see a robot.
		// We only need to call this if the gun (and therefore radar)
		// are not turning.  Otherwise, scan is called automatically.
		if (bearingFromGun == 0) {
			scan();
		}
		
	}
	

	public void onWin(WinEvent e) {
		// Victory dance
		turnRight(36000);
	}

	
}
