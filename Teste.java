package Trab_IA;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import java.util.Random;
import java.util.ArrayList;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

public class Teste extends AdvancedRobot {

	int contador = 0;
	double posicaoDeTiro; // posicao onde se deve atirar ou mover o robo
	Random random = new Random(); 
	ArrayList< ScannedRobotEvent > listaRobosAchados = new ArrayList< ScannedRobotEvent >();
	ScannedRobotEvent roboMenorDistancia;
	ScannedRobotEvent roboAtual;
	int i = 0;
	
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
		
		listaRobosAchados.add(e);
		
		roboMenorDistancia = listaRobosAchados.get(i); // roboAlvo recebe o primeiro robo da lista, ele será o a escolha de menor custo da busca gulosa
		
		for(ScannedRobotEvent robo  : listaRobosAchados){
			  System.out.println(i + " - " + listaRobosAchados.get(i));
			  roboAtual = listaRobosAchados.get(i); // guarda o roboAtual do for
			  
			  if (roboMenorDistancia.getDistance() > roboAtual.getDistance() ){  // compara a energia do robô roboAlvo(anterior) com o roboAtual
				  roboMenorDistancia = roboAtual;
			  }
		}
		
		execute();
		
		atirar(roboMenorDistancia);
		
//		if (e.getDistance() < 100) { // se a distanca for menor que 100 ele tá mto proximo
//			if (e.getBearing() > -90 && e.getBearing() <= 90) {  // se ele estver do lado esquerdo
//				back(40); // volta distancia de 40 para tras
//			} else {
//				ahead(40); // caminha distancia de 40 para frente
//			}
//		}
		
		
		
		
		
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
