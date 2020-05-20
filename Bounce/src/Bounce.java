
import acm.program.ConsoleProgram;
import acm.program.GraphicsProgram;
import acm.graphics.GOval;
import acm.graphics.GRect;
import acm.graphics.GLine;
import java.awt.Color;


public class Bounce extends GraphicsProgram {
	
	private static final boolean TEST = true;
	
//defining my display window
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int OFFSET = 200;
			
//defining my display window in simulation coordinates
	private static final double XMAX = 100;
	private static final double YMAX = 100;
	
//conversion from simulation coordinates to pixels 
	private static final double SCALE = HEIGHT/XMAX;

			
//defining simulation coordinates
	private static final double g= 9.8;
	private static  double initialX = 5.0; //initial position of ball in X
	private static final double initialY= 100.0; //initial position of ball in Y. since height of ball is 30 pixels, then 600-30 = 570pixels = 95 m
	private static final double m = 1.0; //mass of ball
	private static final double Pi= 3.141592654;
	private static final double k=0.0016;
	private static final double TICK = 0.1; // delta t 
	private static final double PD = 1;
	private static final double ETHR = 0.01;
		
	public void run() {		
				
		this.resize(WIDTH,HEIGHT+OFFSET);
				
		//finding screen coordinates of initial positions
			double scaledX = SCALE*initialX;
			double scaledY = SCALE*initialY;	
			double scaledGroundX = SCALE*0;
			double scaledGroundY = SCALE*YMAX;
					
		//read simulation parameters to users
			double Vo = readDouble("Enter the initial velocity of the ball [0,100]:");
			double launchAngle = readDouble("Enter the initial launch angle in degrees [0,90]:");
			double loss = readDouble("Enter the energy lost parameter [0,1]:");
			double bSize = readDouble("Enter the radius of the ball in meters [0.1,5.0]:");
			
		//draw ground plane
			GRect ground = new GRect(0,600,600, 3); // ground has to be the width of the display of window
			ground.setFilled(true);;
			ground.setFillColor(Color.BLACK);	
			add(ground);
			
		// drawing the ball for the simulation
			GOval ball = new GOval(scaledX,scaledY,2*bSize*SCALE,2*bSize*SCALE);
			ball.setFilled(true);
			ball.setFillColor(Color.CYAN);	
			add(ball);
						
		
		//Initializing variables
			double Vt = (m*g)/(4*Pi*bSize*bSize*k);
			double Vox = Vo*Math.cos(launchAngle*Pi/180);
			double Voy=Vo*Math.sin(launchAngle*Pi/180);
				
		// Initializing parameters for Simulation
			double time = 0.0;
			double X = scaledX;
			double Y = scaledY;
			double Xlast = 0;
			double Ylast = 0;
			double Vx = Vox;
			double Vy = Voy;
			double KEx = 0.5*Vx*Vx*(1-loss); //kinetic energy of ball in the X direction
			double KEy = 0.5*Vy*Vy*(1-loss); //kinetic energy of ball in the Y direction
				
		// the path taken by the ball once its launched	
		while(true) {
			X = Vox*Vt/g*(1-Math.exp(-g*time/Vt))+initialX; // must add initialX so that we can reinitialize Xlast as our new starting point
			Y= bSize+Vt/g*(Voy+Vt)*(1-Math.exp(-g*time/Vt))-Vt*time; 
			Vx = (X-Xlast)/TICK;
			Vy = (Y-Ylast)/TICK;
			
		if (TEST)
				System.out.printf("t: %.2f X: %.2f Y:%.2f Vx: %.2f Vy: %.2f\n", time, initialX+X, Y, Vx, Vy);
	// loop when there is a collision
		if (Y <= bSize && Vy <0 ) { 
			KEx = 0.5*Vx*Vx*(1-loss);
			KEy = 0.5*Vy*Vy*(1-loss);
					
			time = 0.0; // must re-initialize time 
			initialX = X;
			Xlast = X;
			Ylast = Y;
					
			Vox = Math.sqrt(2*KEx);
			Voy = Math.sqrt(2*KEy);	
					
		if ((KEx <= ETHR)|| (KEy <= ETHR)) break;		
		}
;
		
		int ScrX = (int) ((X-bSize)*SCALE);  
		int ScrY = (int)(HEIGHT - (Y)*SCALE);
		ball.setLocation(ScrX,Math.min(ScrY,HEIGHT));
				
		// to follow the trajectory of the ball during the motion	
		GOval trajectoire = new GOval(ScrX,ScrY, PD/2, PD/2);
		trajectoire.setFilled(true);
		trajectoire.setFillColor(Color.BLACK);
					
		println("Y ="+Y);
		println ("X="+X);
		pause(45);
		add(trajectoire);
		time += TICK;
		Xlast = X;
		Ylast = Y;		
	
		
			}	

				}
	
				

				

}


			



