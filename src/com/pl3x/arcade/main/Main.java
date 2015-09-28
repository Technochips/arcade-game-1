package com.pl3x.arcade.main;

import java.awt.*;
import java.awt.image.BufferStrategy;

import com.pl3x.arcade.entities.*;
import com.pl3x.arcade.entities.list.*;
import com.pl3x.arcade.hud.*;

public class Main extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 6230533464412165714L; //random number
	
	public static final int WIDTH = 750;  //the screen resolution
	public static final int HEIGHT = 500;
	public static final int HUD = 50;
	
	public static String name = "Arcade game"; //TODO: change the title
	
	private Thread thread;
	private boolean isRunning = false; //it's running? nah, very logic inside a program
	
	private Handler handler;
	private HUD hud;
	
	public Main(){
		handler = new Handler(); //the handler is a new Handler :O
		this.addKeyListener(new KeyInput(handler)); //it's will listen to keys NOTE: it's seem to not work in mac
		
		new Windows(WIDTH, HEIGHT + HUD, name, this); //it's make a new Windows
		
		hud = new HUD();
		
		handler.addObject(new Player(WIDTH/2-50, HEIGHT/2-16, ID.Player, handler)); //it's will spawn a player in the middle of the screen
		handler.addObject(new Enemy(WIDTH/2-8, HEIGHT/2-8, ID.Enemy, 5, 5));
		handler.addObject(new Enemy(WIDTH/2-8, HEIGHT/2-8, ID.Enemy, -5, 5));
		handler.addObject(new Enemy(WIDTH/2-8, HEIGHT/2-8, ID.Enemy, 5, -5));
		handler.addObject(new Enemy(WIDTH/2-8, HEIGHT/2-8, ID.Enemy, -5, -5));
	}
	
	public synchronized void start(){ //when it's start
		thread = new Thread(this);
		thread.start();
		isRunning = true; //when it's start, it's will run
		System.out.println("Started"); //it's write "started" in the console
	}
	public synchronized void stop(){
		try{
			thread.join();
			isRunning = false; //when it's stop, it's stop running
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){ //many things about fps stuff
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				delta--;
			}
			if(isRunning)
				render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames); //will say "fps: <fps>" every seconds
				frames = 0;
			}
		}
		stop();
	}
	
	private void tick(){
		handler.tick();
		hud.tick();
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		g.setColor(Color.black);         //the background will be black
		g.fillRect(0,  0, WIDTH, HEIGHT);//and it's do the size of the screen
		
		handler.render(g);
		
		hud.render(g);
		
		g.dispose();
		bs.show();
	}
	
	public static void main(String[] args) {
		new Main(); //what happen when the program start? it's make a new "Main"
	}
}
